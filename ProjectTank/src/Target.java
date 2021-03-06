import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Target {
	private double x, y, width, height;			// the xy position of the target (its center), and its width and height (in points)
	private double vX, vY;					// the target's velocity in x-axis and y-axis
	private Color targetColor;				// the object's color
	private boolean isDead;	
        private int life; //target life's
    
 	public Target(double x, double y, double width,
				  double height, double vX, double vY, Color targetColor,int life)
	{		
		//set the parameters according to the arguments given	
		this.x = x;
		this.y = y;
		this.vX = vX;
		this.vY = vY;
		this.width = width;
		this.height = height;
		this.targetColor = targetColor;
                this.life = life;
		
		//the target hasn't been hit by default 
		isDead = false;
	}

	public void moveRight()
	{
		x += vX;
	}
	
	public void moveLeft()
	{
		x -= vX;
	}
 
	/**
	 * draw the target if it hasn't been hit
	 * @param g
	 * @param originX
	 * @param originY
	 * @param scale
	 */
	public void draw(Graphics g, double originX, double originY, double scale)	
	{
		//set the color of the graphics according to the ball's color
		Color tempColor = g.getColor();
		g.setColor(targetColor);
		
		//draw the rectangle
		g.fillRect((int)(originX + (x - width/2)*scale) , (int)(originY - (y + height/2)* scale) , 
				(int)(width*scale), (int)(height*scale));
		g.setColor(tempColor);

	}
	
	/**
	 * change the status of the target since it's been hit
	 */
	public void hit()
	{
		--life;
	}
	
	/**
	 * get information about whether the target has been hit or not
	 * @return a boolean value indicating whether the target has been hit or not
	 */
	public boolean isDead()
	{
		return isDead;
	}

        public void Dead(boolean isDead)
	{
            this.isDead=isDead;
	}
	
	/**
	 * reverse the direction of the target's movement
	 */
	public void reverseDirection()
	{
            vX = -vX;
            vY = -vY;
	}
	
        public void moveUp()
        {
            y += vY;
        }
    
        public void moveDown()
        {
            y -= vY;
        }
        
        public void resetLife()
        {
            life=5;
        }

	public void move(double dY)
	{
            dY = dY + y;
            y -= dY;
	}
        
	/**
	 * get the rightmost x-coordinate of the target (in points)
	 * @return the rightmost x-coordinate of the target (in points)
	 */
	public double getRightmostX()
	{
            return x + getWidth()/2;
	}
	/**
	 * get the leftmost x-coordinate of the target (in points)
	 * @return the leftmost x-coordinate of the target (in points)
	 */
	public double getLefttmostX()
	{
            return x - getWidth()/2;
	}
	
	/**
	 * get the x position of the target (the center)
	 * @return the x-position of the target (at the center)
	 */
	public double getX()
	{
            return x;
	}
	/**
	 * get the y position of the target (the center)
	 * @return the y-position of the target (at the center)
	 */
	public double getY()
	{
            return y;
	}
	/**
	 * get the width of the target (in points)
	 * @return the width of the target (in points)
	 */
	public double getWidth()
	{
            return width;
	}
	/**get the height of the target (in points)
	 * @return the height of the target (in points)
	 */
	public double getHeight()
	{
            return height;
	}
        public int getLife()
        {
           return life;
        }
}
