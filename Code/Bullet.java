import java.awt.Color;
import java.awt.Graphics;

public class Bullet 
{
    private double currentX;            // the x-coordinate of the ball
    private double currentY;            // the y-corrdinate of the ball
    private double r;			// the radius of the ball (in points)
    //private boolean isShot;		// boolean value to check whether the bullet has been shot or not
    private double x0, y0;		// the original xy-coordinate at the beginning
    private double alpha;		// the angle at which the ball is first thrown
    private double v0;			// the initial speed of the ball
    private double t0;			// the time when the bullet first shot
    private double m;
    
    private int bulletType;
    //private double m; //Massa Peluru????
    
    public Bullet(double x0, double y0, double v0, double t0, double alpha,double r,double m)
    {
        this.x0 = x0;
        this.y0 = y0;
        this.v0 = v0;
        this.t0 = t0;
        this.alpha = alpha;
        this.r = r;
        this.m = m;
        //this.bulletType = bulletType;
        
        currentX = x0;
	currentY = y0;
    }
    public void move(double t)
    {
        double currentT = t - t0;
        currentX = x0 + (getV0x()*currentT+ getAx()*currentT*currentT/2);
        currentY = y0 + (getV0y()*currentT+ getAy()*currentT*currentT/2);
    }
    public void draw(Graphics g, double originX, double originY)	
    {
        //set the color of the graphics according to the ball's color
        Color tempColor = g.getColor();
        g.setColor(Color.RED);
		
        //draw the ball
        g.fillOval((int)(originX + (currentX - r)) , (int)(currentY - r) , (int)(r*2), (int)(r*2));
        g.setColor(tempColor);	
    }
        
    public double getVx(double t)
    {
        return getV0x() + getAx()*t;
    }
    
    public double getVy(double t)
    {
        return getV0y()+getAy()*t;
    }
    
    public double getAx()
    {
	return 0;
    }
    
    public double getAy()
    {		
	return m*9.87;
    }
  
    public double getV0x()
    {
	return v0*Math.cos(alpha);
    }
    
    public double getV0y()
    {
	return v0*Math.sin(alpha);
    }
}
