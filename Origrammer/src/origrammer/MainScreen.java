package origrammer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import origrammer.geometry.*;

public class MainScreen extends JPanel 
		implements MouseListener, MouseMotionListener, MouseWheelListener, ActionListener, ComponentListener {
	
	
	private Image bufferImage;
	private Graphics2D bufferGraphic;
	private double scale;
	private double transX;
	private double transY;
	
	private boolean displayGrid = true;
	//Affine transformation info
	private Dimension preSize;
	private AffineTransform affineTransform = new AffineTransform();
	
	
	public MainScreen() {
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addComponentListener(this);
		
		scale = 1.5;
		setBackground(Color.white);
		setPreferredSize(new Dimension(800, 800));
		
		preSize = getSize();
	}
	
    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawString("Java 2D", 50, 50);
        g2d.drawRect(0, 0, 800, 800);
        g2d.setColor(Color.red);
        g2d.drawLine(getWidth()/2, getHeight()/2, getWidth(), getHeight());
        
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

       Graphics2D g2d = (Graphics2D)g;        
       updateAffineTransform(g2d);
       
       if (displayGrid) {
    	   drawGrid(g2d);
    	   }
       
       Diagram diagram = new Diagram(Constants.DEFAULT_PAPER_SIZE);
       
       g2d.setColor(Color.BLACK);
       
       for(int j=0; j<diagram.lines.size(); j++) {
    	   double x1 = diagram.lines.get(j).p0.x * (Constants.DEFAULT_PAPER_SIZE/2);
    	   double y1 = diagram.lines.get(j).p0.y * (Constants.DEFAULT_PAPER_SIZE/2);
    	   double x2 = diagram.lines.get(j).p1.x * (Constants.DEFAULT_PAPER_SIZE/2);
    	   double y2 = diagram.lines.get(j).p1.y * (Constants.DEFAULT_PAPER_SIZE/2);

    	   //System.out.println("SIZE: " + diagram.lines.size());
    	   System.out.println("X1: " + x1 + "Y1: " + y1 + " | X2: " + x2 + "Y2: " + y2);
    	   g2d.draw(new Line2D.Double(x1, y1, x2, y2));
       }

        
        
//        if(bufferImage == null) {
//        	bufferImage = createImage(getWidth(), getHeight());
//        	bufferGraphic = (Graphics2D) bufferImage.getGraphics();
//        	updateAffineTransform();
//        	preSize = getSize();
//        }
//        
//        //init the AffineTransform of bufferGraphic
//        bufferGraphic.setTransform(new AffineTransform());
//        
//        //Clear the image buffer
//        bufferGraphic.setColor(Color.WHITE);
//        bufferGraphic.fillRect(0, 0, getWidth(), getHeight());
//        
//        //set the AffineTransform of buffer
//        bufferGraphic.setTransform(affineTransform);
//        
//        Graphics2D g2d = bufferGraphic;
//        //affineTransform.setToTranslation(400, 400);
//        //Graphics2D g2d = (Graphics2D) g;
//        

        
        //g2d.transform(affineTransform);

        
        //doDrawing(g);
    }
    
    private void drawGrid(Graphics2D g2d) {
    	g2d.setColor(Color.LIGHT_GRAY);
    	g2d.setStroke(Config.STROKE_GRID);
    	
    	int lineNum = Globals.gridDivNum;
    	double step = Origrammer.diagram.size / lineNum;
    	for(int i = 1; i<lineNum; i++) {
    		g2d.draw(new Line2D.Double(step * i - Origrammer.diagram.size / 2.0, -Origrammer.diagram.size / 2.0, 
    									step * i - Origrammer.diagram.size / 2.0, Origrammer.diagram.size / 2.0));
    		g2d.draw(new Line2D.Double(-Origrammer.diagram.size / 2.0, step * i - Origrammer.diagram.size / 2.0,
    									Origrammer.diagram.size / 2.0, step * i - Origrammer.diagram.size / 2.0));
    	}    	
    }
    
    //update the AffineTransform
    private void updateAffineTransform(Graphics2D g2d) {
    	affineTransform.setToTranslation(getWidth()*0.5, getHeight()*0.5);
    	System.out.println("width: " + getWidth() + "Height: " + getHeight());
        //affineTransform.setToTranslation(Constants.DEFAULT_PAPER_SIZE, Constants.DEFAULT_PAPER_SIZE);
        g2d.transform(affineTransform);
    	
//    	affineTransform.setToIdentity();
//    	affineTransform.translate(getWidth()*0.5, getHeight()*0.5);
//    	affineTransform.scale(scale, scale);
//    	affineTransform.translate(400, 400);
    }

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
