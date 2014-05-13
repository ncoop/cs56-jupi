package jupi;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Class defining the billiards table and its aggregation of billiards balls.
 */

public class CaromTable extends JPanel 
{
    private double[] dimTable, dimCushion, dimBorder, dimFloor;
    private double borderCorner, borderWidth, cushionWidth, floorWidth, radius;
    private int ppi;//pixels per inch
    private Ball whiteball, redball, yellowball;
    private ArrayList<Ball> balls = new ArrayList<>();
    private Color felt, border, floor, edge, mark;

    /**
     * Default constructor draws all field values from BilliardConstants
     */
    public CaromTable() 
    {
        borderCorner = BilliardsConstants.BORDER_CORNER;
        borderWidth  = BilliardsConstants.BORDER_WIDTH;
        cushionWidth = BilliardsConstants.CUSHION_WIDTH;
        floorWidth   = BilliardsConstants.FLOOR_WIDTH;
        radius       = BilliardsConstants.BALL_DIAMETER / 2;

        dimTable = BilliardsConstants.TABLE_DIMENSION;
        dimCushion = new double[]{dimTable[0]   + 2*cushionWidth, dimTable[1]   + 2*cushionWidth};
        dimBorder  = new double[]{dimCushion[0] + 2*borderWidth , dimCushion[1] + 2*borderWidth};
        dimFloor   = new double[]{dimBorder[0]  + 2*floorWidth  , dimBorder[1]  + 2*floorWidth};

        whiteball  = new Ball(dimTable[0] / 3 , dimTable[1] /2  , BilliardsConstants.WHITE);
        redball    = new Ball(dimTable[0] *2/3, dimTable[1] *2/5, BilliardsConstants.RED);
        yellowball = new Ball(dimTable[0] *2/3, dimTable[1] *3/5, BilliardsConstants.YELLOW);
        
        balls.add(whiteball);
        balls.add(redball);
        balls.add(yellowball);

        felt   = BilliardsConstants.FELT;
        border = BilliardsConstants.BORDER;
        floor  = BilliardsConstants.FLOOR;
        edge   = BilliardsConstants.EDGE;
        mark   = BilliardsConstants.MARK;
    }//CaromTable 

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        int x, y, width, height;
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

        g.setColor(mark);
        int markRadius = (int)(cushionWidth / 2 * ppi);
        for (int i = 0; i <= 9; i++) 
        {
            g.fillOval((int) (floorWidth + borderWidth + cushionWidth + i*dimTable[0]/9 - cushionWidth/2)*ppi,
                              (int)(cushionEdge - borderWidth/2 - markRadius),
                              markRadius, markRadius);
        }//for i
//*
        for (Ball b: balls) 
        {   // draw a circle outline for each ball 
            g.setColor(edge);
            x     = (int)((floorWidth + cushionWidth + b.getPosition().getX()-radius) * ppi);
            y     = (int)((floorWidth + cushionWidth + b.getPosition().getY()-radius) * ppi);
            width = (int)(2*radius*ppi);
            height= (int)(2*radius*ppi);
/*            		
            g.drawOval((int)((floorWidth + cushionWidth + b.getPosition()[0]-radius) * ppi),
                    (int)((floorWidth + cushionWidth + b.getPosition()[1]-radius) * ppi),
                    (int)(2*radius*ppi), (int)(2*radius*ppi));
*/                    
            g.drawOval(x, y, width, height);          
                    
            // draw a filled circle for each ball            
            g.setColor(b.getColor());
/*            
            g.fillOval((int)((floorWidth + cushionWidth + b.getPosition()[0]-radius) * ppi),
                    (int)((floorWidth + cushionWidth + b.getPosition()[1]-radius) * ppi),
                    (int)(2*radius*ppi), (int)(2*radius*ppi));
*/                    
            g.fillOval(x, y, width, height);
        }//for balls
//*/    
    }//paintComponent
    

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

    @Override
    public Dimension getPreferredSize() 
    {
        return new Dimension((int)(dimFloor[0] * ppi), (int)(dimFloor[1] * ppi));
    }
}//CaromTable
