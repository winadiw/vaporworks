import java.awt.Graphics;
import java.awt.image.BufferedImage;

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
		
	}
	
	public void barrelDown()
	{
		
	}
	
	public void fire()
	{
		
	}
	
	public void draw(Graphics g, BufferedImage image)
	{
		g.drawImage(image, x, y, 680/4, 270/4, null);
	}
	
	public void drawBarrel(Graphics g, BufferedImage image)
	{
		g.drawImage(image, x+(120), y+20, 280/4, 25/4, null);
	}
}
