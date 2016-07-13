import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class Lecture02Bullet {
	private double currentX;	// the x-coordinate of the ball
	private double currentY;	// the y-corrdinate of the ball
        private double currentX2;	// the x-coordinate of the ball
	private double currentY2;	// the y-corrdinate of the ball
	private double r;			// the radius of the ball (in points)
	private boolean isShot;		// boolean value to check whether the bullet has been shot or not
	private double x0, y0;		// the original xy-coordinate at the beginning
	private double alpha;		// the angle at which the ball is first thrown
        private double alpha2;		// the angle at which the ball is first thrown
	private double v0;			// the initial speed of the ball
	private double t0;			// the time when the bullet first shot
	private double m;
	private double F;
	private double beta;
	/**
	 * @param x0 the initial x-coordinate where the bullet is
	 * @param y0 the initial y-coordinate where the bullet is
	 * @param v0 the initial velocity of the bullet
	 * @param t0 the time when the bullet first shot
	 * @param alpha the angle where the bullet first shot
	 * @param r the radius of the ball (in points)
	 */
	public Lecture02Bullet(double x0, double y0, double v0, double t0, double alpha, double r)
	{
		this.x0 = x0;
		this.y0 = y0;
		this.t0 = t0;
		this.v0 = v0;
		this.r = r;
		this.m=1;
		this.F=0;
		this.beta=0;
		this.alpha = alpha;
		currentX = x0;
		currentY = y0;
		isShot = true;	
	}
	
	/**
	 * function to move the ball's position according to parabolic motion law and the time since the bullet has been shot
	 * @param t		: the real-time used as a reference to update the bullet position 
	 */
	public void move(double t)
	{
		double currentT = t - t0;
                //System.out.println(currentT);
		currentX = x0 + (getV0x()*currentT + getAx()*currentT*currentT/2);
		currentY = y0 + (getV0y()*currentT + getAy()*currentT*currentT/2);
	}
        
        public void move2(double t)
	{
                t+=0.05;
		double currentT = t - t0;
                //System.out.println(currentT);
		currentX2 = x0 + (getV0x()*currentT + getAx()*currentT*currentT/2);
		currentY2 = y0 + (getV0y()*currentT + getAy()*currentT*currentT/2);
                alpha2 = Math.atan((currentY2-currentY)/(currentX2-currentX));
	}
	
	/**
	 * function to draw the bullet
	 * @param g	the graphics at which the bullet will be drawn
	 * @param originX the location of 0 point (x-axis) the graphics where the bullet will be drawn
	 * @param originY the location of 0 point (y-axis) the graphics where the bullet will be drawn
	 * @param scale the scale used in the graphics to signify how many pixels are for each interval between two point in the coordinate of the graphic 
	 */
	
        public void draw(Graphics g, double originX, double originY, double scale) 
        {
           //set the color of the graphics according to the ball's color
           Graphics2D g2d = (Graphics2D)g;
           Color tempColor = g.getColor();
           g.setColor(Color.RED);
           /*
           double x1, y1, x2, y2;
            x1 = currentX;
            y1 = currentY;
            x2 = currentX2;
            y2 = currentY2;
            g2d.setStroke(new BasicStroke((int)(1*scale)));
            g2d.drawLine((int)(originX + x1*scale), (int)(originY - y1*scale), (int)(originX + x2*scale), (int)(originY - y2*scale));									
            g2d.setColor(tempColor);
           */
           //draw the ball
           //g.fillOval((int)(originX + (currentX - r)*scale) , (int)(originY - (currentY - r)*scale) , (int)(r*2*scale), (int)(r*2*scale));
           //g.setColor(tempColor);
           
           g2d.setColor(Color.RED);
           
           Rectangle rect = new Rectangle((int)(originX + (currentX)*scale), (int)(originY - (currentY)*scale), 40, 20);
           AffineTransform transform = new AffineTransform();
           transform.rotate(-alpha2, (int)(originX + (currentX)*scale),(int)(originY - (currentY)*scale));
           Shape transformed = transform.createTransformedShape(rect);
           
           //g2d.rotate(alpha);
           //g2d.draw(rect);
           g2d.fill(transformed);
           
        }
	
	/**
	 * shoot the bullet
	 */
	public void shoot()
	{
		isShot = true;
	}
	/**
	 * get information whether the bullet has been shot already or not
	 * @return whether the bullet is shot already (true) or not (false)
	 */
	public boolean isShot()
	{
		return isShot;
	}

	 /** detect collision with target (when it enters the target's region)
	  * @param target
	  */
	public void detectCollision(Lecture02Target target)
	{
		if(currentY>= (target.getY() - target.getHeight()*0.5) &&
				currentY<= (target.getY() + target.getHeight()*0.5))		
		{
			if(currentX+r >= (target.getX() - target.getWidth()*0.5) &&
					currentX+r<= (target.getX() ))
			{		
				target.hit();
			}
		}
	}
	
	public double getFx()
	{
		return F*Math.cos(beta);
	}
	
	public double getFy()
	{
		return F*Math.sin(beta);
	}
	/**
	 * function to calculate the ball's velocity in x-axis based on the time it's calculated
	 * @param t		: the time at which the velocity is calculated
	 * @return a double value indicating the ball's velocity in x-axis
	 */
	public double getVx(double t)
	{
		return getV0x() + getAx()*t;
	}
	/**
	 * function to calculate the ball's velocity in y-axis based on the time it's calculated
	 * @param t		: the time at which the velocity is calculated
	 * @return a double value indicating the ball's velocity in y-axis
	 */
	public double getVy(double t)
	{
		return getV0y() + getAy()*t;
	}

	/**
	 * function to calculate the ball's acceleration in x-axis 
	 * @return a double value indicating the ball's acceleration in x-axis
	 */
	public double getAx()
	{
		
		return getFx()/m; //Karena tidak ada arah angin maka Ax = 0;
	}
	/**
	 * function to calculate the ball's acceleration in y-axis 
	 * @return a double value indicating the ball's acceleration in y-axis
	 */
	public double getAy()
	{		
		return (getFy()/m) + (-9.87);
	}

	/**
	 * function to calculate the ball's initial velocity in x-axis 
	 * @return a double value indicating the ball's initial velocity in x-axis
	 */
	public double getV0x()
	{
		return v0*Math.cos(alpha);
	}
	/**
	 * function to calculate the ball's initial velocity in y-axis 
	 * @return a double value indicating the ball's initial velocity in y-axis
	 */
	public double getV0y()
	{
		return v0*Math.sin(alpha);
	}

}
