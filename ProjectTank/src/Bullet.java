import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class Bullet {
	private double currentX;	// the x-coordinate of the ball
	private double currentY;	// the y-corrdinate of the ball
        private double currentX2;	// the x2-coordinate of the ball
	private double currentY2;	// the y2-corrdinate of the ball
	private double r;		// the radius of the bullet, used in collision detection
	private boolean isShot;		// boolean value to check whether the bullet has been shot or not
	private double x0, y0;		// the original xy-coordinate at the beginning
	private double alpha;		// the angle at which the ball is first thrown
        private double alpha2;		// angle after certain t, to count the rotation
	private double v0;		// the initial speed of the ball
	private double t0;		// the time when the bullet first shot
	private double m;               // bullet mass
	private double F;               
	private double beta;               
        private double t;
        private boolean hitTarget; //to check whether the bullet have hit the target or not
        private int type;   //the bullet chance appearing
        private boolean triggered;

	public Bullet(double x0, double y0, double v0, double t0, double alpha, double r, int type, boolean triggered)
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
                
                this.type = type;
		this.triggered = triggered;
                hitTarget = false;
	}
	
	/**
	 * function to move the ball's position according to parabolic motion law and the time since the bullet has been shot
	 * @param t: the real-time used as a reference to update the bullet position 
	 */
	public void move(double t)
	{
                this.t = t;
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
	
        public void draw(Graphics g, double originX, double originY, double scale,boolean line) 
        {
           Graphics2D g2d = (Graphics2D)g;
           
           //Create the bullet
           Rectangle rect = new Rectangle((int)(originX + (currentX)*scale), (int)(originY - (currentY)*scale), 30, 15);
           AffineTransform transform = new AffineTransform();
           transform.rotate(-alpha2, (int)(originX + (currentX)*scale),(int)(originY - (currentY)*scale));
           Shape transformed = transform.createTransformedShape(rect);
           Shape ellipse = transform.createTransformedShape(new Ellipse2D.Double((int)(originX + (currentX)*scale), 
                   (int)(originY - (currentY)*scale), 40, 15));
           g2d.setColor(Color.RED);
           g2d.fill(transformed);
           g2d.fill(ellipse);
           
           //if create parabolic line, then render this
           if(line)
           {
               double tempT = t;
               g2d.setColor(Color.GREEN);
               //draw the line
               double[] xList = new double[100];
               double[] yList = new double[100];
               for(int i=0;i<100;i++)
               {
                   tempT+=0.4;
                   double currentT = tempT - t0;
                   xList[i] = x0 + (getV0x()*currentT + getAx()*currentT*currentT/2);
                   yList[i] = y0 + (getV0y()*currentT + getAy()*currentT*currentT/2);

               }
               for(int i=0;i<100;i++)
               {
                g2d.fillOval((int)(originX + (xList[i])*scale),(int)(originY - (yList[i])*scale),10,10);
               }
           }
        }
	

	//shoot the bullet
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
	public void detectCollision(Target target)
	{
            if(currentY>= (target.getY() - target.getHeight()*0.5) &&
		currentY<= (target.getY() + target.getHeight()*0.5))		
            {
		if(currentX+r >= (target.getX() - target.getWidth()*0.5) &&
                    currentX+r<= (target.getX()))
		{		
                    if(hitTarget==false)
                    {
                        target.hit();
                        hitTarget();
                    }
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
	 * @param t: the time at which the velocity is calculated
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
        
        public void hitTarget()
        {
            hitTarget = true;
        }
        public boolean getHitTarget()
        {
            return hitTarget;
        }
        
        public int getType()
        {
            return type;
        }
        
        public boolean isTriggered()
        {
            return triggered;
        }
        
        public void setTrigger(boolean trigger)
        {
            triggered = trigger;
        }
        
        public double getX()
        {
            return currentX2;
        }
        
        public double getY()
        {
            return currentY2;
        }
        
        public double getAlpha()
        {
            return alpha2;
        }

}
