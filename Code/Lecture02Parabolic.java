import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;

import javax.swing.JFrame;

public class Lecture02Parabolic extends JFrame implements Runnable{

	//stage variables (cannonball, hill, ball)
	Lecture02Cannon cannon;				//the cannon who shoot the ball		
	Lecture02Target target;				//the target to be drawn
	ArrayList<Lecture02Bullet> bullets;	//the ball to be drawn
        //BufferedImage tankImage;
	private int maxBullets;
	private int trigger;
	
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
        
	//Image where functions are drawn, which then drawn to the canvas
	BufferedImage dbImage;	
			
	
	public Lecture02Parabolic()
	{
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
		cannon = new Lecture02Cannon(-30, -13, 1.5, Color.red);
		v0Bullet = 30;
		incV0 = 1;
		currentT = 0;
		incT = 0.01;
		trigger = 0;
                /*
                try
                {
                        //tankImage = ImageIO.read(new File("D:/imagetank/Tank_Kanan.png"));
                        //tankImageKanan = ImageIO.read(new File("image/TankTanpaLaras.png"));
                        //barrelKanan = ImageIO.read(new File("D:/imagetank/Laras_Kanan.png"));
                }
                catch(IOException e)
                {

                }
                */
		//initialize the target		
		double carWidth = 1, carHeight = 4;
		/*target = new Lecture02Target(0 + carWidth/2,	(-1*(canvasHeight-originY)/scale ) + carHeight,
							  carWidth, carHeight, 0.1,0,Color.blue);
                */
                target = new Lecture02Target(28,5,carWidth, carHeight, 1,1,Color.blue,5);
		//initialize the bullets
		maxBullets = 2000;
		bullets =  new ArrayList<Lecture02Bullet>();		
							
		//add listener to key event, so that the program could respond to keyboard input
		addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e) {				
				// TODO Auto-generated method stub				
				if(e.getKeyCode() == KeyEvent.VK_LEFT)
				{
					//target.moveLeft();
					cannon.rotateLeft();
				}
				else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					//target.moveRight();
					cannon.rotateRight();
				}
				else if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{	
                                    if(bullets.size() < maxBullets)
                                    {   
                                        bullets.add(new Lecture02Bullet(cannon.getBarrelEndX(), 
					cannon.getBarrelEndY() + cannon.getBarrelWidth(),
					v0Bullet, currentT, cannon.getAlpha(), cannon.getBarrelWidth()/2));
                                    }
				}
                                else if(e.getKeyCode() == KeyEvent.VK_I)
				{	
                                    target.moveUp();
				}
                                else if(e.getKeyCode() == KeyEvent.VK_K)
				{	
                                    target.moveDown();
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
		
		//create the image where the model is drawn
		dbImage = (BufferedImage)createImage(getWidth(), canvasHeight);
		
		//start the thread to run the animation
		animator = new Thread(this);
		animator.start();	
	}
	

	public void run() {		
		while(true)
		{
			update();
			render();
			printScreen();
			try
			{
				animator.sleep((int)(incT*1000));
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
		
		//move the target
		//target.move();
		/*
		if(target.getRightmostX()*scale > getWidth() || target.getLefttmostX()*scale <= 0)
			target.reverseDirection();
			*/		
		//if the bullet has been created	

		trigger++;
		if(trigger == 10)
			trigger = 0;
		
		if(bullets!=null)
		{
			for(int i=0; i<bullets.size(); i++)
			{
				//check to see whether the ball has been shot
				if(bullets.get(i).isShot())
				{												
					bullets.get(i).move(currentT);	
                                        bullets.get(i).move2(currentT);
					bullets.get(i).detectCollision(target);
					if(target.getLife()<=0)
                                        {
						target.Dead();
						//bullets = null;
					}				
				}
			}
		}
		
			
	}
	
	public void render()
	{
		if(dbImage != null)
		{				
			//get graphics of the image where coordinate and function will be drawn
			Graphics g = dbImage.getGraphics();
		
			//clear screen
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), canvasHeight);
			
			//draw the x-axis and draw the y-axis
			g.setColor(Color.BLACK);
			g.drawLine(0, (int)originY, (int)getWidth(), (int)originY);
			g.drawLine((int)originX, 0, (int)originX, (int)canvasHeight);
			
			//draw the numbers in x-axis and y-axis
			int maxNumX;			
			if(originX < getWidth()/2)
				maxNumX = (int)((getWidth()-originX)/(scale));
			else
				maxNumX = (int)(originX/scale);
			int maxNumY;
                        
			if(originY < canvasHeight/2)
				maxNumY = (int)((canvasHeight-originY)/(scale));
			else
				maxNumY = (int)(originY/scale);			
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
			
			//draw the cannon
			cannon.draw(g,originX, originY, scale);
			g.setColor(Color.black);
                        g.setFont(new Font("TimesRoman", Font.PLAIN, 35)); 
			//g.drawString("Power: " + v0Bullet, getInsets().left,canvasStartY);
                        g.drawString("Power: " + v0Bullet, (int)(originX+(maxNumX-69)*scale),(int)(originY-(maxNumY-5)*scale));
			g.drawString("Life: " + target.getLife(), (int)(originX+(maxNumX-7)*scale),(int)(originY-(maxNumY-5)*scale));
			//draw the target
			if(!target.isDead())
				target.draw(g, originX, originY, scale);
			
			//draw the ball
			if(bullets.size() > 0)
			{
				for(int i=0; i<bullets.size(); i++)
				{
					bullets.get(i).draw(g, originX, originY, scale);					
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
}
