import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.awt.geom.*;


public class Lecture02Cannon {
	private double x,y,r;					// the xy position of the cannon (center of the cannon), and its radius (in points, not pixel)
	private double barrelLength;			// the length of the barrel
	private double barrelWidth;				// the width of the barrel
	private double alpha;					// the angle of the barrel	
	private Color cannonColor;				// the color of the cannon
	
	private final double incAlpha = 0.1;	// the increment of alpha every time the cannon is rotated in radian
	
	/**
	 * @param x				: the x position of the cannon on the map (in points)	
	 * @param y				: the y position of the cannon on the map (in points)
	 * @param r				: the radius of the cannon (in points)
	 * @param cannonColor	: the cannon color (which is used to determine the barrel color)
	 */
	public Lecture02Cannon(double x, double y, double r, Color cannonColor)
	{
		//the angle of the barrel is initialized to 0
		this.alpha = Math.PI/4;
		
		//parameters given as arguments are set
		this.x = x;
		this.y = y;
		this.r = r;
		this.cannonColor = cannonColor;

		//other parameters are initialized based on the arguments given (and some assumption)
		this.barrelLength = 3*r;
		this.barrelWidth = r/2;
	}
	
	/**
	 * function to rotate the barrel to the left
	 */
	public void rotateLeft()
	{
		alpha += incAlpha;
	}
	/**
	 * function to rotate the barrel to the right
	 */
	public void rotateRight()
	{
		alpha -= incAlpha;
	}
	
	/**
	 * move the cannon according to the distance and direction
	 * @param distance the distance (in points) of the move
	 * @param direction the direction of the move('u', 'd', 'l', 'r')
	 */
	public void move(double distance, char direction)
	{
		switch(direction)
		{
			case 'u': y += distance;
				break;
			case 'd': y -= distance;
				break;
			case 'l': x -= distance;
				break;
			case 'r': x += distance;
				break;
		}
	}
	
	/**
	 * function to get the width of the barrel
	 * @return a double value indicating the width of the barrel
	 */
	public double getBarrelWidth()
	{
		return barrelWidth;
	}
	/**
	 * function to get the alpha (elevation angle) of the barrel)
	 * @return a double value indicating the alpha of the barrel
	 */
	public double getAlpha()
	{
		return alpha;
	}
	/**
	 * function to get the x position of the barrel's end (centered)
	 * @return a double value indicating the x position of the barrel's end
	 */
	public double getBarrelEndX()
	{
		return x + barrelLength*Math.cos(alpha);
	}
	/**
	 * function to get the y position of the barrel's end (centered)
	 * @return a double value indicating the y position of the barrel's end
	 */
	public double getBarrelEndY()
	{
		return y + barrelLength*Math.sin(alpha);
	}
	
	/**
	 * function to draw the cannon
	 * @param g			: the graphics where the cannon is drawn
	 * @param originX	: the x coordinate of the origin point in the graphics (in points)
	 * @param originY	: the y coordinate of the origin point in the graphics (in points)
	 * @param scale		: the scale used in the graphics, showing how many pixels there are between 2 points
	 */
	public void draw(Graphics g,double originX, double originY, double scale)
	{
		Graphics2D g2 = (Graphics2D) g; 
		Color tempColor = g2.getColor();
                g2.setColor(Color.lightGray);
		double x1, y1, x2, y2;
		x1 = x;
		y1 = y;
		x2 = getBarrelEndX();
		y2 = getBarrelEndY();
		g2.setStroke(new BasicStroke((int)(getBarrelWidth()*scale)));
		g2.drawLine((int)(originX + x1*scale), (int)(originY - y1*scale), (int)(originX + x2*scale), (int)(originY - y2*scale));									
		g2.setColor(tempColor);
                /*
                AffineTransform at = new AffineTransform();
                at.translate((int)(originX+(x*scale)),(int)(originY-(y*scale)));
                g2.drawImage(image, at, null);
                */
		//g.drawImage(image, (int) (originX+(x*scale)), (int)(originY-(y*scale)), 680/3, 270/3, null);
                
		//draw the circle of the cannon
                g2.setColor(cannonColor);
		g.fillOval((int)(originX + (x - r)*scale),(int)(originY - (y + r)*scale), (int)((r*2)*scale), (int)((r*2)*scale));
                g2.setColor(Color.ORANGE);
		g.fillRect((int)(originX + (x-2*r)*scale),(int)(originY - (y)*scale), (int)((r*4)*scale), (int)((r)*scale));
                //g.fillRect(int x, int y, int width, int height)
		//draw the center of the barrel
                
		
                
	}


}
