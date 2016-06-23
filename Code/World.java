import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.Graphics2D;

public class World extends JFrame implements Runnable
{
    Target target;
    Tank tank;
    Tank tank2;
    Bullet bullet;
    private double incT;
    private Thread animator;
    BufferedImage dbImage;
    BufferedImage tankImage;
    BufferedImage tankImageKanan;
    BufferedImage barrelKanan;
    
    private int canvasHeight;
    private int canvasStartY;
    private double v0Bullet;			// the initial velocity of the bullet
    private double incV0;			// the increment of initial velocity
    private double currentT;			// the time of the game 
    private int scale;
    public World()
    {
        setExtendedState(MAXIMIZED_BOTH);
	setBackground(Color.WHITE);
	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setVisible(true);				
	setFocusable(true);
        
	canvasHeight = getHeight() - getInsets().top;
	canvasStartY = getInsets().top;
	dbImage = (BufferedImage)createImage(getWidth(), canvasHeight);
        incT = 0.01;
        v0Bullet = 1200;

        //Initialize Target
        target = new Target(getWidth()*3/4, 100, 20, 50, 5, Color.RED);
        
        //target = new target();
        
        //Initialize Bullet
        
        tank = new Tank(200, 700);
        //tank2 = new Tank(200, 700);
        try
        {
        	tankImage = ImageIO.read(new File("D:/imagetank/Tank_Kanan.png"));
        	//tankImageKanan = ImageIO.read(new File("image/TankTanpaLaras.png"));
        	barrelKanan = ImageIO.read(new File("D:/imagetank/Laras_Kanan.png"));
        }
        catch(IOException e)
        {
        	
        }
        
        //Add KeyListener
        addKeyListener(new KeyListener()
	{
            @Override
            public void keyPressed(KeyEvent e) 
            {							
		if(e.getKeyCode() == KeyEvent.VK_UP)
		{
                    //Naikkan Target
			target.moveUp();

		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN)
		{
                    //Turunkan Target
			target.moveDown();
		}
                else if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    //Keluar dari permainan, kembali ke menu dengan muncul dialog
                }
                
                else if(e.getKeyCode() == KeyEvent.VK_W)
                {
                    tank.barrelUp();
                }
                
                else if(e.getKeyCode() == KeyEvent.VK_S)
                {
                    tank.barrelDown();
                }
                
                else if(e.getKeyCode() == KeyEvent.VK_D)
                {
                    //bullet.shoot();
                    bullet = new Bullet(tank.getBarrelEndX(), 
                    tank.getBarrelEndY(),
                    v0Bullet, currentT, tank.getAlpha(), 10, 100);	
                    //System.out.println(tank.getBarrelEndX()+" "+tank.getBarrelEndY());
                    System.out.println(tank.getAlpha());
                }   
            }
            @Override
            public void keyReleased(KeyEvent arg0) 
            {
            }

            @Override
            public void keyTyped(KeyEvent arg0) 
            {
            }
        
        });
        
        //create the image where the model is drawn
	//dbImage = (BufferedImage)createImage(getWidth(), canvasHeight);
		
	//start the thread to run the animation
	animator = new Thread(this);
	animator.start();	
    }
    
    public void run() 
    {	
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
        currentT += incT;
        if(bullet != null)			
        {
           bullet.move(currentT);  
        }
    }
    
    public void render()
    {
        //Gambar graphics nanti disini
    	Graphics g = dbImage.getGraphics();
        Graphics2D g2d = (Graphics2D)g;
    	
    	g.setColor(Color.white);
	g.fillRect(0, 0, getWidth(), canvasHeight);
    	
 	target.draw(g, this.getHeight(), this.getWidth(), 1);
 	tank.draw(g, this.tankImage);
             
        AffineTransform at = new AffineTransform();
        
        // 4. translate it to the center of the component
        //at.translate(this.getWidth() / 2, this.getHeight() / 2);
        at.setToTranslation(tank.getX()+160, tank.getY()+24);
        
        at.rotate(tank.getAlpha());
        at.scale(0.4, 0.4);

        // 1. translate the object so that you rotate it around the axis
        //at.translate(barrelKanan.getWidth(), barrelKanan.getHeight());

        // draw the image
        tank.drawBarrel2D(g2d, this.barrelKanan, at);
        //tank2.draw(g, this.tankImageKanan);
        if(bullet != null)
        {
            bullet.draw(g, 0, 0);
        }
		
   }
    
    public void printScreen()
    {
        //Tampilkan ke screen
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