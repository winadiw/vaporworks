import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Target 
{
    private int x,y,width,height;
    private double vY;
    private Color targetColor;
    
    public Target(int x,int y, int width, int height,double vY,Color targetColor)
    {
       this.x = x;  
       this.y = y;
       this.width = width;
       this.height = height;
       this.vY = vY;
       this.targetColor = targetColor;
    }
    
    public void moveUp()
    {
        y -= vY;
    }

    public void moveDown()
    {
        y += vY;
    }
    
    public void draw(Graphics g, double originX, double originY, double scale)	
    {
	//set the color of the graphics according to the ball's color
	Color tempColor = g.getColor();
	g.setColor(targetColor);
		
	//draw the rectangle
	//g.fillRect((int)(originX + (x - width/2)*scale) , (int)(originY - (y + height/2)* scale) , 
    //                (int)(width*scale), (int)(height*scale));
	g.fillRect(this.x, this.y, this.width, this.height);
	g.setColor(tempColor);
		
    }

    public double getRightmostX()
    {
        return x + getWidth();
        //Masih perlu dicek apakah sesuai
    }

    public double getLefttmostX()
    {
        return x - getWidth();
        //Masih perlu dicek apakah sesuai
    }
	
    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getWidth()
    {
	return width;
    }

    public double getHeight()
    {
        return height;
    }
}
