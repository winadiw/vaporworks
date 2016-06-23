import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.Graphics2D;

public class Tank 
{
	private int x;
	private int y;
	private double barrelX;
	private double barrelY;
	private double barrelEndX;
	private double barrelEndY;
	private double alpha;
	private double barrelRotationSpeed;
	
	public Tank(int x, int y)
	{
		this.x = x;
		this.y = y;
                alpha = 0;//Math.PI/4;
                barrelX = x + 160;
                barrelY = y + 24;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public void barrelUp()
	{
            alpha-=0.2;	
	}
	
	public void barrelDown()
	{
            alpha+=0.2;
	}
	
	public void fire()
	{
		
	}
        
        public double getAlpha()
        {
		return alpha;
	}
	
        
	public void draw(Graphics g, BufferedImage image)
	{
		g.drawImage(image, x, y, 680/3, 270/3, null);
	}
	
	public void drawBarrel(Graphics g, BufferedImage image)
	{
		g.drawImage(image, x+(120), y+20, 280/4, 25/4, null);
	}
        
        public void drawBarrel2D(Graphics2D g2d,BufferedImage image, AffineTransform at)
        {
             g2d.drawImage(image, at, null);
        }
        
        public double getBarrelX()
        {
            return barrelX;
        }
        
         public double getBarrelY()
        {
            return barrelY;
        }
         
        public double getBarrelEndX()
        {
            return barrelX+100*Math.cos(alpha);
        }
        
        public double getBarrelEndY()
        {
            return barrelY+12*Math.sin(alpha);
        }
}
