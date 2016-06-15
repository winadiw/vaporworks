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

public class World extends JFrame implements Runnable
{
    Target target;
    Tank tank;
    Tank tank2;
    private double incT;
    private Thread animator;
    BufferedImage dbImage;
    BufferedImage tankImage;
    BufferedImage tankImageKanan;
    BufferedImage barrelKanan;
    
    private int canvasHeight;
    private int canvasStartY;
    
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
        
        //Initialize Target
        target = new Target(getWidth()/2, 100, 20, 50, 5, Color.RED);
        
        //target = new target();
        
        //Initialize Bullet
        
        //tank = new Tank(getWidth()-200, 700);
        tank2 = new Tank(200, 700);
        try
        {
        	tankImage = ImageIO.read(new File("C:/Users/ASUS N550JV/Desktop/Tank/TankTanpaLaras.png"));
        	tankImageKanan = ImageIO.read(new File("C:/Users/ASUS N550JV/Desktop/Tank/TankTanpaLaras_Kanan.png"));
        	barrelKanan = ImageIO.read(new File("C:/Users/ASUS N550JV/Desktop/Tank/Laras_Kanan.png"));
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
        //Update mechanics disini semua
    }
    
    public void render()
    {
        //Gambar graphics nanti disini
    	Graphics g = dbImage.getGraphics();
    	
    	g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, getWidth(), canvasHeight);
    	
 		target.draw(g, this.getHeight()/2, this.getWidth()/2, 1);
 		
 		//tank.draw(g, this.tankImage);
 		tank2.drawBarrel(g, this.barrelKanan);
 		tank2.draw(g, this.tankImageKanan);
 		//g.drawImage(this.tankImage, tank.getX(), tank.getY(), 680/4, 270/4, null);
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