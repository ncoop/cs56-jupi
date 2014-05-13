package jupi;
import java.awt.*;

/**
 * Class defining a billiards ball.
 */

public class Ball 
{
    private Color color;
    private VectorDouble position, velocity;
    private double mass, radius;

    /**
     * Default constructor makes an odd-colored (blue) ball at position 0,0.
     * Should not be used.
     */
    public Ball() 
    {
        this(0, 0, 20, 10, Color.blue);
    }

    public Ball(double x, double y, double radius, double mass, Color c) 
    {        
        position    = new VectorDouble(x,y);
        velocity    = new VectorDouble(0,0);
        this.radius = radius;
        this.mass   = mass;
        color       = c;
    }

    public Color getColor() 
    {
        return color;
    }

    public void setColor(Color color) 
    {
        this.color = color;
    }

    public VectorDouble getPosition() 
    {
        return position;
    }

    public void setPosition(VectorDouble position) 
    {
        this.position = position;
    }
    
    public void setPosition(double x, double y) 
    {
        this.setPosition(new VectorDouble(x,y));
    }
    
    public VectorDouble getVelocity()
    {
    	return velocity;
    }
    
    public void setVelocity(VectorDouble velocity)
    {
    	this.velocity = velocity;
    }
    
    public void setVelocity(double xVel,double yVel)
    {
    	this.setVelocity(new VectorDouble(xVel,yVel));
    }
    
    public void reflect(boolean x, boolean y)
    {
    	if(x)
    	{    		
    		this.setVelocity((this.getVelocity().getX()*-1), this.getVelocity().getY());
    	}
    	if(y)
    	{    	
    		this.setVelocity((this.getVelocity().getX()), (this.getVelocity().getY() * -1));
    	}
    }
          
    public double getRadius()
    {
    	return radius;
    }
    
    public double getMass()
    {
    	return mass;
    }

}//Ball