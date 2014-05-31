package jupi;

import java.awt.Color;
import java.awt.Graphics;

public class BallPath {

	private double angle,dx,dy,distance;
	private VectorDouble startPoint,endPoint;
	private int ppi = BilliardsConstants.PIXELS_PER_INCH;
	
	
	
	public BallPath(VectorDouble ballPosition,double angle,double distance)
	{
		this.angle = angle;
		this.distance = distance;
		this.dx = distance * Math.cos(angle);
		this.dy = distance * Math.sin(angle);
		this.startPoint = ballPosition;
		this.endPoint = new VectorDouble();
		
		
	}
	public void updateProp(VectorDouble ballPosition,double angle)
	{
		this.angle = angle;
		this.dx = -distance * Math.cos(angle + Math.toRadians(90));
		this.dy = -distance * Math.sin(angle + Math.toRadians(90));
		this.startPoint = ballPosition;
	}
	public void draw(Graphics g,double cushion,double floor,double border)
	{
		double firstPath,secondPath;
		double totalWidth = cushion+floor+border;
		
		
		g.setColor(Color.RED);
		//firstPath = Physics.checkPath(startPoint, dx, dy);
		//secondPath = distance - firstPath;
		
		//System.out.println(firstPath);
		this.dx = -distance * Math.cos(angle + Math.toRadians(90));
		this.dy = -distance * Math.sin(angle + Math.toRadians(90));
		//endPoint.x = startPoint.x + dx;
		//endPoint.y = startPoint.y + dy;
		g.drawLine((int)((startPoint.x + totalWidth)*ppi ), (int)((startPoint.y + totalWidth)* ppi), (int)((startPoint.x + totalWidth+ dx)*ppi),(int)((startPoint.y +totalWidth+ dy)*ppi));
		
		//this.dx = -secondPath * Math.cos(-angle + Math.toRadians(90));
		//this.dy = -secondPath * Math.sin(-angle + Math.toRadians(90));
		//g.drawLine((int)((endPoint.x + totalWidth)*ppi ), (int)((endPoint.y + totalWidth)* ppi), (int)(endPoint.x + totalWidth+ dx)*ppi,(int)(endPoint.y +totalWidth+ dy)*ppi);
	}
	
	
}
