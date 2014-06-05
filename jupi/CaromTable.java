package jupi;
import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.EventListener;

/**
 * Class defining the billiards table and its aggregation of billiards balls.
 */

public class CaromTable extends JPanel implements EventListener
{
    private double[] dimTable, dimCushion, dimBorder, dimFloor;
    private double borderCorner, borderWidth, cushionWidth, floorWidth, radius, mass,gap,cueStickLength,pullDistance;
    private int ppi;//pixels per inch
    private Ball whiteball, redball, yellowball;
    private Ball currentBall, // current ball being played, can be white or yellow
    			 otherBall;
    boolean isWhitePlayer; //determines player's turn - True: white, False: Yellow
    private BallPath path;
    private Cue cueStick;    
    
    private ArrayList<Ball> balls = new ArrayList<>();
    private Color felt, border, floor, edge, mark;
    int mouseX,mouseY;
    boolean mouseDown,showCue;
    boolean isRedHit = false; //checks if current ball has hit red ball
    boolean isOtherBallHit = false; //checks if current ball has hit the other (not red) ball (i.e. Yellow or White)   
    boolean isSwitchPlayers = false;
    Score wScore = new Score();
    int score = 0;//players score
    
    /**
     * Default constructor draws all field values from BilliardConstants
     */

    public CaromTable() 
    {
        borderCorner   = BilliardsConstants.BORDER_CORNER;
        borderWidth    = BilliardsConstants.BORDER_WIDTH;
        cushionWidth   = BilliardsConstants.CUSHION_WIDTH;
        floorWidth     = BilliardsConstants.FLOOR_WIDTH;
        radius         = BilliardsConstants.BALL_DIAMETER / 2;
        mass		   = BilliardsConstants.BALL_MASS;
        gap			   = BilliardsConstants.GAP;
        cueStickLength = BilliardsConstants.CUE_LENGTH;
        pullDistance   = 0;
        mouseDown      = false;
        showCue 	   = false;

        dimTable = BilliardsConstants.TABLE_DIMENSION;
        dimCushion = new double[]{dimTable[0]   + 2*cushionWidth, dimTable[1]   + 2*cushionWidth};
        dimBorder  = new double[]{dimCushion[0] + 2*borderWidth , dimCushion[1] + 2*borderWidth};
        dimFloor   = new double[]{dimBorder[0]  + 2*floorWidth  , dimBorder[1]  + 2*floorWidth};
        
        cueStick = new Cue();
        whiteball  = new Ball(dimTable[0] / 3 , dimTable[1] /2  ,radius,mass, BilliardsConstants.WHITE);
        redball    = new Ball(dimTable[0] *2/3, dimTable[1] *2/5,radius,mass, BilliardsConstants.RED);
        yellowball = new Ball(dimTable[0] *2/3, dimTable[1] *3/5,radius,mass, BilliardsConstants.YELLOW);
        
        currentBall = whiteball; //white starts first
        //isWhitePlayer = true;
        currentBall.setCurrentBall(true);
        otherBall = yellowball;
       
        path = new BallPath(currentBall.getPosition(),0,30);
        
        balls.add(whiteball);
        balls.add(redball);
        balls.add(yellowball);              
        
        whiteball.setVelocity(0,0);
        redball.setVelocity(0,0);
        yellowball.setVelocity(0,0);
        
        felt   = BilliardsConstants.FELT;
        border = BilliardsConstants.BORDER;
        floor  = BilliardsConstants.FLOOR;
        edge   = BilliardsConstants.EDGE;
        mark   = BilliardsConstants.MARK;
        
        
        this.addMouseListener(new MouseListener()
        {	@Override
			public void mouseClicked(MouseEvent e) 	{}
        
			@Override
			public void mousePressed(MouseEvent e) 
			{
				mouseDown = true;
			}

			@Override
			public void mouseReleased(MouseEvent e) 
			{
				mouseDown = false;				
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
        	
        });
        
        this.addMouseMotionListener(new MouseMotionListener()
        {
        	public void mouseMoved(MouseEvent e)
        	{
        		mouseX = e.getX();
        		mouseY = e.getY();
        	}
        	
        	public void mouseDragged(MouseEvent e) {}
        });//MouseMotionListener
        
    }//CaromTable 


    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);        
        drawTable(g);
        drawBalls(g);   
        //Drawing cue stick         
        if(showCue)
        {
        	// -----------------------------------------------------------------------------
        	//updateScore();
        	System.out.printf("Score = %d\n", score);
        	//System.out.printf("SwitchPlayers = " + isSwitchPlayers + "\n");//testing
        	
        	drawCue(g);
        }//if        
    }//paintComponent


    //--------------------------------------------------------------------------------
    /* Drawing Table */
    //--------------------------------------------------------------------------------
    public void drawTable(Graphics g)
    {    	
        int cushionEdge = (int) ((floorWidth + borderWidth) * ppi);
        int tableEdge   = (int) ((floorWidth + borderWidth + cushionWidth) * ppi);
        
        /* draw a filled rectangle for the floor surrounding the table border */
        g.setColor(floor);
        g.fillRect(0, 0, (int)(dimFloor[0] * ppi), (int)(dimFloor[1] * ppi));
        
        /* draw a filled round rect for the rail border surrounding the cushions */
        g.setColor(border);
        g.fillRoundRect((int)(floorWidth * ppi)  , (int)(floorWidth * ppi),
                		(int)(dimBorder[0] * ppi), (int)(dimBorder[1] * ppi),
                		(int)(borderCorner * ppi), (int)(borderCorner * ppi));

        /* draw a filled rectangle for the play area plus the cushions */
        g.setColor(felt);
        g.fillRect(cushionEdge, cushionEdge,
                   (int)(dimCushion[0] * ppi),
                   (int)(dimCushion[1] * ppi));

        /* draw a rectangle outline for the play area */
        g.setColor(edge);
        g.drawRect(tableEdge, tableEdge,
                   (int)(dimTable[0] * ppi),
                   (int)(dimTable[1] * ppi));
               
        /* draw a line segment connecting the play field corners to each border corner */
        g.drawLine(cushionEdge, cushionEdge, tableEdge, tableEdge);
        g.drawLine(cushionEdge + (int)dimCushion[0] * ppi, cushionEdge,
                   tableEdge + (int)dimTable[0] * ppi, tableEdge);
        g.drawLine(cushionEdge, cushionEdge + (int)dimCushion[1] * ppi,
                   tableEdge, tableEdge + (int) dimTable[1] * ppi);
        g.drawLine(cushionEdge + (int)dimCushion[0] * ppi, cushionEdge + (int)dimCushion[1] * ppi,
                   tableEdge + (int)dimTable[0] * ppi, tableEdge + (int)dimTable[1] * ppi);

        /* draw filled circles to mark the edges */
        g.setColor(mark);
        int markRadius = (int)(cushionWidth / 2 * ppi); 
        int marks = 8;
        int sideMarks = 3;
        
        for (int i = 0; i <= marks; i++) 
        {   
        	//Top border markers:
        	g.fillOval((int)(cushionEdge + (cushionWidth/2 + i*dimTable[0]/marks )*ppi),        			   
        			   (int)(cushionEdge - markRadius - borderWidth/2),
        			   markRadius, markRadius);
            //Bottom border markers:                                 
            g.fillOval((int)(cushionEdge + (cushionWidth/2 + i*dimTable[0]/marks )*ppi),
            		   (int)(cushionEdge + (dimTable[1] + 2*cushionWidth)* ppi + borderWidth/2),                      
            		   markRadius, markRadius);
        }
        
        for (int i = 1; i <= sideMarks; i++) 
        {
        	//Left border markers:
        	g.fillOval((int)(cushionEdge - markRadius - borderWidth/2),        			   
     			       (int)(cushionEdge + (i*dimTable[1]/(sideMarks+1))*ppi),
     			       markRadius, markRadius);
        	//Right border markers:
        	g.fillOval((int)(cushionEdge + (2*cushionWidth + dimTable[0])*ppi + borderWidth/2),        			   
  			       	   (int)(cushionEdge + (i*dimTable[1]/(sideMarks+1))*ppi),
  			           markRadius, markRadius);        	
        }
    }//drawTable
    
    
    //--------------------------------------------------------------------------------
    /* Drawing Balls */
    //--------------------------------------------------------------------------------
    public void drawBalls(Graphics g)
    {        
    	int x, y, width, height;    	
    	width  = (int)(2*radius*ppi);
    	height = (int)(2*radius*ppi);
    	
        for (Ball b: balls) 
        {   
        	x = (int)((floorWidth + borderWidth + cushionWidth + b.getPosition().x-radius) * ppi);
        	y = (int)((floorWidth + borderWidth + cushionWidth + b.getPosition().y-radius) * ppi);        	
        	
        	// draw a filled circle for each ball 
            g.setColor(b.getColor());
            g.fillOval(x, y, width, height);            
            // draw a circle outline for each ball 
            g.setColor(edge);
            g.drawOval(x, y, width, height);
        }    	
    }//drawBalls
    
    
    //--------------------------------------------------------------------------------
    /* Drawing Cue Stick */
    //--------------------------------------------------------------------------------
    public void drawCue(Graphics g)
    {
    	double dx = mouseX- ((floorWidth + borderWidth + cushionWidth+currentBall.getPosition().x) * ppi);
    	double dy = mouseY - ((floorWidth + borderWidth + cushionWidth+currentBall.getPosition().y) * ppi);
    
    	double angle = Math.atan2(dy, dx) - Math.toRadians(90);
    	cueStick.setAngle(angle);
            	
		cueStick.setPosition(( floorWidth + borderWidth + cushionWidth + currentBall.getPosition().x - BilliardsConstants.CUE_DIAMETER*.5 ) ,
			(pullDistance + floorWidth + borderWidth +cushionWidth + currentBall.getPosition().y - radius + gap));	
            
    	int[] cuesX = new int[]{(int)(cueStick.getX()*ppi),
    					(int)((cueStick.getX() + BilliardsConstants.CUE_DIAMETER )*ppi),
    					(int)((cueStick.getX() +  1.5*BilliardsConstants.CUE_DIAMETER )*ppi),
    					(int)((cueStick.getX() -  BilliardsConstants.CUE_DIAMETER*.5 )*ppi)};
    	int[]cuesY = new int[]{(int)((cueStick.getY() + BilliardsConstants.CUE_DIAMETER*.5)*ppi),
    					(int)((cueStick.getY() + BilliardsConstants.CUE_DIAMETER*.5)*ppi),
    					(int)((cueStick.getY() + BilliardsConstants.CUE_DIAMETER*.5 + cueStickLength)*ppi),
    					(int)((cueStick.getY() + BilliardsConstants.CUE_DIAMETER*.5 + cueStickLength)*ppi)};
    
    	path.updateProp(currentBall.getPosition(), angle);
    	 path.draw(g,cushionWidth,floorWidth,borderWidth);
                 
    	AffineTransform transform = new AffineTransform();
    
    	transform.rotate(angle,(floorWidth + borderWidth + cushionWidth + currentBall.getPosition().x )*ppi,(floorWidth + borderWidth + cushionWidth + currentBall.getPosition().y  )*ppi);
    	Graphics2D g2d = (Graphics2D)g;
    	g2d.transform(transform);
		g2d.setColor(Color.BLUE);
		g2d.fillArc((int)(cueStick.getX()*ppi) , 
			   (int)(cueStick.getY()*ppi),
			   (int)(BilliardsConstants.CUE_DIAMETER*ppi) ,(int)(BilliardsConstants.CUE_DIAMETER*ppi) ,0 ,180);
	
		g2d.setColor(new Color(0xDBB84D));
		g2d.fillPolygon(cuesX, cuesY, 4);    	
    }//drawCue
    
        

    public void update()
    {
    	//Mouse down even to hit ball    
		if(mouseDown && pullDistance < BilliardsConstants.MAX_PULL && showCue)
		{
			pullDistance += BilliardsConstants.PULL_RATE;
			cueStick.setPower(pullDistance);
			
		}else if(!mouseDown && pullDistance > 0)
		{
			pullDistance -= 3; 
		}
		if(pullDistance < 0)
		{//cue will hit ball
			Physics.hitBall(cueStick,currentBall);
			pullDistance = 0;
			
			isSwitchPlayers = true; //reset 
			wScore.setScoreChanged(false);//reset
			redball.setIsHit(false);//reset
			otherBall.setIsHit(false);//reset
		}
		
		//Check if balls are at rest
    	if (isBallsStopped())		
		{
    		if(currentBall == whiteball)
    		{
    			otherBall = yellowball;
    		}else{
    			otherBall = whiteball;
    		}
    		
    		if(redball.isHit() && otherBall.isHit())
    		{
    			wScore.increment();
    			score++;//will be replaced by Score class
    			isSwitchPlayers = false; //reset
    			redball.setIsHit(false);//reset
    			otherBall.setIsHit(false);//reset
    		}else
    		{    	
    			wScore.setScoreChanged(false);    			
    		}
			showCue = true;			

//-------------------------------------------------------------------			
		    if (isSwitchPlayers && !wScore.isScoreChanged())			
		    {
		    	switchPlayers();	
		    	isSwitchPlayers = false; //reset		    	
    		}//if
//-------------------------------------------------------------------
		}
		else
		{
			showCue = false;
		}
		
    	//Update each ball
    	for(int i = 0; i < balls.size(); i++)
    	{
    		Physics.checkCusionCollision(balls.get(i), this);
    		
    		Ball ball1, ball2;
    		for(int j = i + 1; j < balls.size(); j++)
    		{    			
    			ball1 = balls.get(i);
    			ball2 = balls.get(j);
    			
    			if (Physics.checkBallCollision(ball1, ball2))
    			{//had a collision
    				if (ball1.isCurrentBall() == true)
    				{ 
    					//checkIfScored(ball2); 
    					ball2.setIsHit(true);
    				}
    				else if (ball2.isCurrentBall() == true)
    				{    
    					//checkIfScored(ball1);
    					ball1.setIsHit(true);
    				}    				
    			}
    		}//for j
    		balls.get(i).update();
    	}//for i
    	
    	repaint();
    }//update
    
    
    public boolean isBallsStopped()
    {
    	return ( whiteball.getVelocity().x  == 0 && whiteball.getVelocity().y   == 0 &&
				 yellowball.getVelocity().x == 0 && yellowball.getVelocity().y  == 0 &&
				 redball.getVelocity().x    == 0 && redball.getVelocity().y     == 0);    	
    }
    
    
    /*public void checkIfScored(Ball ball)
    {
    	if (ball == redball)
		{
			isRedHit = true;
			System.out.print("Red Ball Hit!!!");
		}    				
		else if (ball == whiteball || ball == yellowball)
		{//other ball hit (white or yellow, depending on which current ball is)
			isOtherBallHit = true;
			System.out.print("OtherBall Hit!!!");
		}			    	
    }*/
    
    
  //==================================================================  
/*    
    public void updateScore()
    {
    	if (isRedHit && isOtherBallHit)
    	{//same player keeps on playing
    		score++;
    		isSwitchPlayers = false;
    		System.out.printf("\nScore = %d\n", score);
    	}
    	else //No score
    	{    		
    		//isSwitchPlayers= true;
    		//if (isSwitchPlayers)
    			//switchPlayers();
    	}//else
    	
    	//reset:
    	isRedHit = false;
    	isOtherBallHit = false;
    }//updateScore
  */  
    
    public void switchPlayers()
    {    	
	    	if (whiteball.isCurrentBall())
				System.out.print(" Current Bal = WHITE ");
			else if (yellowball.isCurrentBall())
				System.out.print(" Current Bal = yellowball ");
	    	
			//switch players (balls)
			if (whiteball.isCurrentBall())
			{
				whiteball.setCurrentBall(false);
				yellowball.setCurrentBall(true);	    				
				//currentBall.setCurrentBall(false);//change current ball	    				    				
				currentBall = yellowball;
				//currentBall.setCurrentBall(true);//new current ball	    				
				//isWhitePlayer = false;    				
			}
			else//ball is yellow
			{
				whiteball.setCurrentBall(true);
				yellowball.setCurrentBall(false);
				//currentBall.setCurrentBall(false);//change current ball
				currentBall = whiteball;
				//currentBall.setCurrentBall(true);//new current ball
				//isWhitePlayer = true;
			}	
			
			//isSwitchPlayers = false; //reset
    }//switchPlayers
    
    
    public int getPixelsPerInch() 
    {
        return ppi;
    }

    public void setPixelsPerInch(int pixelsPerInch) 
    {
        if (pixelsPerInch < 1)
            throw new IllegalArgumentException("Pixels per inch must be positive.");
        this.ppi = pixelsPerInch;
    }
    
    //Get Table Dimensions
    public double getTableWidth()
    {
    	return dimTable[0];
    }
    
    public double getTableHeight()
    {
    	return dimTable[1];
    }

    @Override
    public Dimension getPreferredSize() 
    {
        return new Dimension((int)(dimFloor[0] * ppi), (int)(dimFloor[1] * ppi));
    }    
}