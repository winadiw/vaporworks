import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;

//20 Juli 2016 Mananda
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
//====================
public class Parabolic extends JFrame implements Runnable, MouseListener, MouseMotionListener
{
        private boolean delay;
	Cannon cannon;				//the cannon who shoot the bullets		
	Target target;				//the target to be drawn
	ArrayList<Bullet> bullets;              //the bullets to be drawn
	private int maxBullets;
	private int trigger;
	private boolean line=false;
        
	//variabel to control the parameters
	private double v0Bullet;			// the initial velocity of the bullet
	private double incV0;				// the increment of initial velocity
	private double currentT;			// the time of the game 
	private double incT;				// the increment in time
		
	//Variables used to draw the xy-coordinate  
	private double scale;			// the number of pixels for each one step (the distance between two numbers / steps)
	private double originX;			// the x origin
	private double originY;			// the y origin
	private int canvasHeight;		// the height of the canvas (where images are drawn) 
	private int canvasStartY;		// the starting y position on the frame where the canvas is drawn
	//Thread where animation is run
	private Thread animator;
        private boolean turnRandom = true;
	//Image where functions are drawn, which then drawn to the canvas
	BufferedImage dbImage;	
        private int level = 1;
        private int maxLevel = 15;
        private int bulletCounter = 0;
        private boolean drawAxis = false;
        
	BufferedImage img = null;
        BufferedImage img2 = null;
	public enum STATE
	{
		MENU, GAME;
	};
	
	private STATE state;
	
	public Rectangle playButton;
	public Rectangle quitButton;
	
	private int maxNumX;
	private int maxNumY;
	long startTime = 0;
        long endTime = 0;
        
        
	//20 Juli 2016 Mananda
	File sfx;
	//====================
	public Parabolic()
	{
            
                Sound.bgm.loop();
		//configure the main canvas
		setExtendedState(MAXIMIZED_BOTH);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);				
		setFocusable(true);
		canvasHeight = getHeight() - getInsets().top;
		canvasStartY = getInsets().top;		
				
		//setting the system's coordinate
		originY = getHeight()/2;
		originX = getWidth()/2;		
		scale = 25;		
				
		//initialize the cannon & bullet
		cannon = new Cannon(-30, -14, 1.5, Color.red);
		v0Bullet = 30;
		incV0 = 1;
		currentT = 0;
		incT = 0.01;
		trigger = 0;
               
		//initialize the target		
		double carWidth = 1, carHeight = 4;
                target = new Target(28,5,carWidth, carHeight, 1,1,Color.blue,5);
                
		//initialize the bullets
		maxBullets = 100;
		bullets =  new ArrayList<Bullet>();
                try
                {
                    img = ImageIO.read(new File("background.png"));	
                    img2 = ImageIO.read(new File("background2.png"));
                }
                catch(IOException e)
                {    
                }
		//add listener to key event, so that the program could respond to keyboard input
		addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e) {				
				// TODO Auto-generated method stub				
				if(e.getKeyCode() == KeyEvent.VK_LEFT)
				{
					//cannon.rotateLeft();
				}
				else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					//cannon.rotateRight();
				}
				else if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{	
                                    if(bulletCounter < maxBullets)
                                    {   
					bullets.add(new Bullet(cannon.getBarrelEndX(), 
                                            cannon.getBarrelEndY() + cannon.getBarrelWidth(),
                                            v0Bullet, currentT, cannon.getAlpha(), cannon.getBarrelWidth()/2, (int) Math.ceil(Math.random() * 10), false));
                                    }
				}

				else if(e.getKeyCode() == KeyEvent.VK_UP)
				{
					v0Bullet += incV0;
				}
				else if(e.getKeyCode() == KeyEvent.VK_DOWN)
				{
					v0Bullet -= incV0;
				}
				else if(e.getKeyCode() == KeyEvent.VK_W)
				{
					cannon.move(0.1, 'u');
				}			
				else if(e.getKeyCode() == KeyEvent.VK_S)
				{
					cannon.move(0.1, 'd');
				}			
				else if(e.getKeyCode() == KeyEvent.VK_A)
				{
					cannon.move(0.1, 'l');
				}			
				else if(e.getKeyCode() == KeyEvent.VK_D)
				{
					cannon.move(0.1, 'r');
				}			
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
			}
			
		});
		
		addMouseMotionListener(this);
		addMouseListener(this);
		state = STATE.MENU;
		
		//20 Juli 2016 Mananda
		sfx = new File("Explosion_02.wav");
		//====================
		
		//create the image where the model is drawn
		dbImage = (BufferedImage)createImage(getWidth(), canvasHeight);
		
		//start the thread to run the animation
		animator = new Thread(this);
		animator.start();	
		
	}
	
	public void mouseMoved(MouseEvent e)
	{
		target.move((e.getY() - originY) / scale);
	}
	
	public void mouseDragged(MouseEvent e)
	{
		
	}

	public void resetGame()
        {
            target.Dead(true);
            bullets = null;
            level = 1;
            Sound.fail.play();
            JOptionPane.showMessageDialog (null, "You Died! Back to Main Menu", "You Lose!", JOptionPane.ERROR_MESSAGE);
            bullets =  new ArrayList<Bullet>();
            bulletCounter = 0;
            dbImage = (BufferedImage)createImage(getWidth(), canvasHeight);
            state = STATE.MENU;
            target.Dead(false);
            target.resetLife();
        }
        
        public void nextLevel()
        {
            level++;
            Sound.win.play();
            JOptionPane.showMessageDialog (null, "Next Level!", "Nice One!", JOptionPane.INFORMATION_MESSAGE);
            bullets =  new ArrayList<Bullet>();	
            bulletCounter = 0;
            maxBullets+=50;
            target.resetLife();
        }
        
        //20 Juli 2016 Mananda
        static void PlaySound(File Sound)
    	{
    		try
    		{
    			Clip clip = AudioSystem.getClip();
    			clip.open(AudioSystem.getAudioInputStream(Sound));
    			clip.start();
    			
    			//Thread.sleep(clip.getMicrosecondLength() / 1000);
    		}
    		catch(Exception e)
    		{
    			
    		}
    	}
        //====================
	public void run() {		
		while(true)
		{
			if(state == STATE.GAME)
				update();
			render();
			printScreen();
			try
			{
				animator.sleep((int)(incT));
			}
			catch(Exception ex)
			{
					
			}
		}		
	}
	
	public void update()
	{
                
		//update the time
		currentT += incT;
                
                //Check if bullets is empty
                if(delay==false)
                {
                    if(maxBullets-bulletCounter==0)
                    {
                        startTime = System.currentTimeMillis();
                        delay=true;
                    }
                }
                
                
                for(int i=0;i<bullets.size();i++)
                {
                     if(bullets.get(i).getHitTarget()==true)
                     {
                        bullets.remove(i);
                     }   
                }
                
                if(turnRandom)
                {
                    Random randomGenerator = new Random();

                    if(bulletCounter < maxBullets)
                    {
                            if(trigger == 0 && target.getLife()!=0)
                            {
                                int randomPower = randomGenerator.nextInt(40)+30+level;
                                v0Bullet = randomPower;
                                double randomAlpha = 0.0 + (0.8 - 0.0) * randomGenerator.nextDouble();
                                cannon.setAlpha(randomAlpha);
                                
                                //initialize the bullet
                                bullets.add(new Bullet(cannon.getBarrelEndX(), 
                                   cannon.getBarrelEndY() + cannon.getBarrelWidth(),
                                   v0Bullet, currentT, cannon.getAlpha(), cannon.getBarrelWidth()/2, (int) Math.ceil(Math.random() * 10), false));
                                ++bulletCounter;
                                //PlaySound(sfx);  
                                Sound.explosion.play();
                            }								
                    }
                    trigger++;
                    if(trigger == (maxLevel+1)-level)
                            trigger = 0;
                 }

		if(bullets!=null)
		{
			for(int i=0; i<bullets.size(); i++)
			{
				//check to see whether the ball has been shot
				if(bullets.get(i).isShot())
				{												
					if(bullets.get(i).isTriggered() == false)
					{
						if(bullets.get(i).getType() > 5)
						{
							if(bullets.get(i).getType() % 2 == 0) //burst
							{
                                                            bullets.get(i).setTrigger(true);
                                                            if(bulletCounter < maxBullets)
	                            {   
	                                bullets.add(new Bullet(cannon.getBarrelEndX(), 
	                                cannon.getBarrelEndY() + cannon.getBarrelWidth(),
	                                v0Bullet, currentT, cannon.getAlpha() * 7/6, cannon.getBarrelWidth()/2, 0, true));
                                        ++bulletCounter;
	                            }
                                                            if(bulletCounter < maxBullets)
	                            {   
	                                bullets.add(new Bullet(cannon.getBarrelEndX(), 
	                                cannon.getBarrelEndY() + cannon.getBarrelWidth(),
	                                v0Bullet, currentT, cannon.getAlpha() * 5/6, cannon.getBarrelWidth()/2, 0, true));
                                        ++bulletCounter;
	                            }
							}
							else if(bullets.get(i).getType() % 2 != 0) //split
							{
								if(target.getX() - bullets.get(i).getX() <= 25)
								{
									bullets.get(i).setTrigger(true);
									if(bulletCounter < maxBullets)
		                            {   
		                                bullets.add(new Bullet(bullets.get(i).getX(), 
		                                bullets.get(i).getY(), v0Bullet, currentT, bullets.get(i).getAlpha() * 3/2, cannon.getBarrelWidth()/2, 0, true));
                                                ++bulletCounter;
		                            }
									if(bulletCounter < maxBullets)
		                            {   
		                                bullets.add(new Bullet(bullets.get(i).getX(), 
		                                bullets.get(i).getY(), v0Bullet, currentT, bullets.get(i).getAlpha() * 1/2, cannon.getBarrelWidth()/2, 0, true));
                                                ++bulletCounter;
		                            }
								}
							}
						}
						
					}
                                        bullets.get(i).move(currentT);	
                                        bullets.get(i).move2(currentT);
                                        if(target.getLife()>0)
                                            bullets.get(i).detectCollision(target);
					if(target.getLife()<=0)
                                        {
                                            resetGame();
					}				
				}
			}
		}
                
            if(maxBullets-bulletCounter==0)
            {
                endTime = System.currentTimeMillis();
            }
            if (endTime - startTime > 4000 && endTime!=0 && startTime!=0) 
            {
                endTime=0;
                startTime=0;
                delay = false;
                nextLevel();
            }
	}
	
	public void render()
	{
		if(dbImage != null)
		{	
			//get graphics of the image where coordinate and function will be drawn
			Graphics g = dbImage.getGraphics();
                        
			
			//draw the numbers in x-axis and y-axis			
			if(originX < getWidth()/2)
				maxNumX = (int)((getWidth()-originX)/(scale));
			else
				maxNumX = (int)(originX/scale);
	                        
			if(originY < canvasHeight/2)
				maxNumY = (int)((canvasHeight-originY)/(scale));
			else
				maxNumY = (int)(originY/scale);	
			
			if(state == STATE.MENU)
			{
                                g.drawImage(img2, 0, 0, getWidth(), getHeight(), this);
				playButton = new Rectangle((int)(originX+(maxNumX-41.5)*scale),(int)(originY-(maxNumY-12)*scale), 100, 50);
				quitButton = new Rectangle((int)(originX+(maxNumX-41.5)*scale),(int)(originY-(maxNumY-15)*scale), 100, 50);
				
				Graphics2D g2d = (Graphics2D) g;
				
				Font fnt0 = new Font("arial", Font.BOLD, 50);
				g.setFont(fnt0);
				g.setColor(Color.BLACK);
				g.drawString("PROJECT: TANK", (int)(originX+(maxNumX-47)*scale),(int)(originY-(maxNumY-10)*scale));
                                
                                Font fnt2 = new Font("arial", Font.BOLD, 22);
                                g.setFont(fnt2);
				g.drawString("Mathematics and Physics for Game", (int)(originX+(maxNumX-46.5)*scale),(int)(originY-(maxNumY-30)*scale));
                                g.drawString("Mananda / 00000005245 / mananda.hutagalung@gmail.com", (int)(originX+(maxNumX-51)*scale),(int)(originY-(maxNumY-31)*scale));
                                g.drawString("Winadi Wiratama / 00000007257 / winadiw@gmail.com", (int)(originX+(maxNumX-50)*scale),(int)(originY-(maxNumY-32)*scale));
                                
				Font fnt1 = new Font("arial", Font.BOLD, 30);
				g.setFont(fnt1);
				
				g.drawString("PLAY", (int)(originX+(maxNumX-41)*scale),(int)(originY-(maxNumY-13.5)*scale));
				g2d.draw(playButton);
				
				g.drawString("QUIT", (int)(originX+(maxNumX-41)*scale),(int)(originY-(maxNumY-16.5)*scale));
				g2d.draw(quitButton);
				//menu.render(g);
			}
			else
			{
				//clear screen
                               g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                               g.setColor(Color.white);
                                g.fillRect(0, 0, getWidth(), canvasHeight);
                                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                                if(drawAxis)
                                {
                                    //draw the x-axis and draw the y-axis
                                    g.setColor(Color.BLACK);
                                    g.drawLine(0, (int)originY, (int)getWidth(), (int)originY);
                                    g.drawLine((int)originX, 0, (int)originX, (int)canvasHeight);

                                    for(int i=0; i<maxNumX; i++)
                                    {
                                            g.drawString(Integer.toString(i), (int)(originX+(i*scale)), (int)originY);
                                            g.drawString(Integer.toString(-1*i), (int)(originX+(-i*scale)), (int)originY);
                                    }
                                    for(int i=0; i<maxNumY; i++)
                                    {
                                            g.drawString(Integer.toString(-1*i), (int)originX, (int)(originY+(i*scale)));
                                            g.drawString(Integer.toString(i), (int)originX, (int)(originY + (-i*scale)));
                                    }
                                }
				//draw the cannon
				cannon.draw(g,originX, originY, scale);
				g.setColor(Color.black);
		        g.setFont(new Font("TimesRoman", Font.PLAIN, 35)); 
		        //g.drawString("Power: " + v0Bullet, getInsets().left,canvasStartY);
		        g.drawString("Power: " + v0Bullet, (int)(originX+(maxNumX-69)*scale),(int)(originY-(maxNumY-5)*scale));
			g.drawString("Life: " + target.getLife(), (int)(originX+(maxNumX-7)*scale),(int)(originY-(maxNumY-5)*scale));
                        g.drawString("Level: " + level, (int)(originX+(maxNumX-7)*scale),(int)(originY-(maxNumY-7)*scale));
		        g.drawString("Bullets: " + (maxBullets-bulletCounter), (int)(originX+(maxNumX-69)*scale),(int)(originY-(maxNumY-3)*scale));
				//draw the target
				if(!target.isDead())
					target.draw(g, originX, originY, scale);
					
				//draw the ball
				if(bulletCounter > 0)
				{
					for(int i=0; i<bullets.size(); i++)
					{
						bullets.get(i).draw(g, originX, originY, scale,line);	
					}
				}
                              
			}
			
		}
	}

	public void printScreen()
	{
		try
		{
			Graphics g = getGraphics();
			if(dbImage != null && g != null)
			{
				g.drawImage(dbImage, 0, canvasStartY, null);
			}
			
			// Sync the display on some systems.
			// (on Linux, this fixes event queue problems)
			Toolkit.getDefaultToolkit().sync();
			g.dispose();
		}
		catch(Exception ex)
		{
			System.out.println("Graphics error: " + ex);  
		}		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		int mx = e.getX();
		int my = e.getY();
		if(state == STATE.MENU)
		{
			if(mx >= (int)(originX+(maxNumX-40)*scale) && mx <= (int)(originX+100+(maxNumX-40)*scale))
			{
				if(my >= (int)(originY + 30 -(maxNumY-12)*scale) && my <= (int)(originY + 80 -(maxNumY-12)*scale))
				{
					state = STATE.GAME;
				}
				
				if(my >= (int)(originY + 30 -(maxNumY-15)*scale) && my <= (int)(originY + 80 -(maxNumY-15)*scale))
				{
					System.exit(0);
				}
			}
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
