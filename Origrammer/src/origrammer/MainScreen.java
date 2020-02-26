package origrammer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
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
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.vecmath.Vector2d;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.util.SVGConstants;
import org.apache.commons.io.FileUtils;

import origrammer.geometry.*;

public class MainScreen extends JPanel 
		implements MouseListener, MouseMotionListener, MouseWheelListener, ActionListener, ComponentListener {
	
	
	private Point2D preMousePoint;
	private Point2D currentMouseDraggingPoint = null;
	private Point2D.Double currentMousePointLogic = new Point2D.Double();
	//private double Globals.SCALE;
	private double transX;
	private double transY;
	
	//temp info when editing
	private Vector2d firstSelectedV = null;
	private Vector2d secondSelectedV = null;
	private Vector2d thirdSelectedV = null;
	private Vector2d selectedCandidateV = null;
	private OriLine firstSelectedL = null;
	private OriLine secondSelectedL = null;
	private OriLine thirdSelectedL = null;
	private OriLine selectedCandidateL = null;
	private OriVertex selectedCandidateVertex = null;
	private OriArrow selectedCandidateA = null;
	private OriFace selectedCandidateF = null;
	private OriPicSymbol selectedCandidatePS = null;
	private OriGeomSymbol selectedCandidateGS = null;
	private OriEqualDistSymbol selectedCandidateEDS = null;
	private OriEqualAnglSymbol selectedCandidateEAS = null;
	private OriPleatCrimpSymbol selectedCandidatePleat = null;
	private OriLeaderBox selectedCandidateLeader = null;
	private OriLeaderBox tmpLeader = new OriLeaderBox();
	
	private boolean dispGrid = true;
	//Affine transformation info
	private Dimension preSize;
	private AffineTransform affineTransform = new AffineTransform();
	//private ArrayList<Vector2d> crossPoints = new ArrayList<>();
	
	private Graphics2D g2d;
	
	public ArrayList<JLabel> arrowLabelList = new ArrayList<>();
	private JLabel tmpArrowLabel = new JLabel();
	private OriArrow tmpOriArrow = new OriArrow();
	private JLabel tmpSymbolLabel = new JLabel();
	private OriPicSymbol tmpOriSymbol = new OriPicSymbol();
	//private JLabel arrowLabel = new JLabel();
	private boolean isReleased = false;
	private boolean isMovingSymbols = false;
	private boolean isPressedOverSymbol = false;
	int tmpArrowWidth;
	int tmpArrowHeight;
	
	public MainScreen() {
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addComponentListener(this);
		
		Globals.SCALE = 1.0;
		setBackground(Color.white);
		setPreferredSize(new Dimension(800, 800));
		setLayout(null);
		
		preSize = getSize();
	}
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        removeAll();
        g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        updateAffineTransform(g2d);
        
        if (dispGrid) {
        	drawGrid(g2d);
        }
        
    	//RENDER ALL FILLED FACES
    	renderAllFilledFaces();
    	
    	//RENDER ALL LINES
    	renderAllLines();
       
    	//RENDER ALL ARROWS
    	tmpArrowLabel.setBorder(new EtchedBorder(BevelBorder.RAISED, Color.RED, getBackground().brighter()));
    	add(tmpArrowLabel);
    	for (OriArrow arrow : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
    		BufferedImage bimg = getBufImgByTypeAndRot(arrow);
    		int newArrowLabelWidth = (int) Math.round(bimg.getWidth()/2*arrow.getAdjustedScale());
    		int newArrowLabelHeight = (int) Math.round(bimg.getHeight()/2*arrow.getAdjustedScale());
    		arrow.setWidth(newArrowLabelWidth);
    		arrow.setHeight(newArrowLabelHeight);
    		arrow.getLabel().setBounds((int) arrow.getPosition().x, (int) arrow.getPosition().y, arrow.getWidth(), arrow.getHeight());

    		Image dimg = bimg.getScaledInstance(arrow.getLabel().getWidth(), arrow.getLabel().getHeight(), Image.SCALE_SMOOTH);
    		ImageIcon arrowImageIcon = new ImageIcon(dimg);
    		//arrow.setWidth(newArrowLabelWidth);
    		//arrow.setHeight(newArrowLabelHeight);
    		arrow.getLabel().setIcon(arrowImageIcon);
    		//set Border to indicate a selected arrow or when hovering over one
    		if (arrow.isSelected()) {
    			arrow.getLabel().setBorder(new EtchedBorder(BevelBorder.RAISED, Color.GREEN, getBackground().brighter()));
    		} else if (selectedCandidateA == arrow) {
    			arrow.getLabel().setBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()));
    		} else {
    			arrow.getLabel().setBorder(BorderFactory.createEmptyBorder());
    		}
    		add(arrow.getLabel());
    	}
    	
    	if (isReleased) {
    		remove(tmpArrowLabel);
    	}
	   
       //RENDER ALL SYMBOLS
       tmpSymbolLabel.setBorder(new EtchedBorder(BevelBorder.RAISED, Color.RED, getBackground().brighter()));
       add(tmpSymbolLabel);
       for (OriPicSymbol s : Origrammer.diagram.steps.get(Globals.currentStep).picSymbols) {    

    	   BufferedImage bimg = getBufImgByTypeAndRot(s);
    	   int newSymbolLabelWidth = (int) Math.round(bimg.getWidth()/2*s.getScale());
    	   int newSymbolLabelHeight = (int) Math.round(bimg.getHeight()/2*s.getScale());
    	   s.setWidth(newSymbolLabelWidth);
    	   s.setHeight(newSymbolLabelHeight);
    	   s.getLabel().setBounds((int) s.getPosition().x, (int) s.getPosition().y, s.getWidth(), s.getHeight());
    	   
    	   Image dimg = bimg.getScaledInstance(s.getLabel().getWidth(), s.getLabel().getHeight(), Image.SCALE_SMOOTH);
    	   ImageIcon symbolImageIcon = new ImageIcon(dimg);

    	   s.getLabel().setIcon(symbolImageIcon);
    	   //set Border to indicate a selected symbol or when hovering over one
    	   if (s.isSelected()) {
    		   s.getLabel().setBorder(new EtchedBorder(BevelBorder.RAISED, Color.GREEN, getBackground().brighter()));
    	   } else if (selectedCandidatePS == s) {
    		   s.getLabel().setBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()));
    	   } else {
    		   s.getLabel().setBorder(BorderFactory.createEmptyBorder());
    	   }
    	   add(s.getLabel());
       }
       
	   if (isReleased) {
		   remove(tmpSymbolLabel);
	   }
	   
	   //RENDER ALL LEADERS
	   renderAllOriLeaderBoxes();
	   
	   //RENDER ALL ORI_GEOM_SYMBOLS
	   renderAllOriGeomSymbols();
	   
	   //RENDER ALL EQUAL_DISTANCE_SYMBOLS
	   renderAllEquDistSymbols();
	   
	   //RENDER ALL EQUAL_ANGLE_SYMBOLS
	   renderAllEquAnglSymbols();
	   
	   //RENDER ALL PLEATS
	   renderAllCrimpsPleats();
	   
       //render all vertices if in ToolbarMode ADD_VERTEX or DELETE_VERTEX mode or if dispVertex is true
	   renderAllVertices();

       //draw temp LINE on firstSelectedV and currentMousePoint
	   renderAllTempLines();
       
	   if (firstSelectedV != null) {
    	   g2d.setColor(Color.RED);
    	   g2d.fill(new Rectangle2D.Double(firstSelectedV.x - 5.0 / Globals.SCALE,
    			   firstSelectedV.y - 5.0 / Globals.SCALE, 10.0 / Globals.SCALE, 10.0 / Globals.SCALE));
       }
	   
       if (secondSelectedV != null) {
    	   g2d.setColor(Color.RED);
    	   g2d.fill(new Rectangle2D.Double(secondSelectedV.x - 5.0 / Globals.SCALE,
    			   secondSelectedV.y - 5.0 / Globals.SCALE, 10.0 / Globals.SCALE, 10.0 / Globals.SCALE));
       }
       
       if (thirdSelectedV != null) {
    	   g2d.setColor(Color.RED);
    	   g2d.fill(new Rectangle2D.Double(thirdSelectedV.x - 5.0 / Globals.SCALE,
    			   thirdSelectedV.y - 5.0 / Globals.SCALE, 10.0 / Globals.SCALE, 10.0 / Globals.SCALE));
       }
       
       if (selectedCandidateV != null) {
    	   g2d.setColor(Color.GREEN);
    	   g2d.fill( new Rectangle2D.Double(selectedCandidateV.x - 5.0 / Globals.SCALE,
    			   selectedCandidateV.y - 5.0 / Globals.SCALE, 10.0 / Globals.SCALE, 10.0 / Globals.SCALE));
       }
       
       //draw RECTANGULAR selection
       renderRectSelection();

       //draw tmp OriGeomSymbol
       renderTempOriGeomSymbol();

       //show coordinates of selected Vertex
       if (selectedCandidateV != null ) {
    	   g.setColor(Color.BLACK);
    	   g.drawString("(" + selectedCandidateV.x + ", " + selectedCandidateV.y + ")", -325, -325);
       }
    }
    
    private void renderAllFilledFaces() {
    	if (Globals.dispFilledFaces) {
    		for (OriFace f : Origrammer.diagram.steps.get(Globals.currentStep).filledFaces) {
    			if (f.isSelected() || selectedCandidateF == f) {
    				g2d.setPaint(new Color(200, 100, 100));
    				g2d.draw(f.path);
    				g2d.fill(f.path);
    			} else {
    				if (f.isFaceUp()) {
        				g2d.setPaint(Origrammer.diagram.getFaceUpColor());
    				} else {
        				g2d.setPaint(Origrammer.diagram.getFaceDownColor());
    				}
    				g2d.draw(f.path);
    				g2d.fill(f.path);
    			}
    		}
    	}
    }
    
    private void renderAllLines() {
    	for (OriLine line : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
    		//render lines according to their LINE_TYPE
    		switch(line.getType()) {
    		case OriLine.TYPE_VALLEY:
    			g2d.setColor(Config.LINE_COLOR_VALLEY);
    			g2d.setStroke(Config.STROKE_VALLEY);
    			break;
    		case OriLine.TYPE_MOUNTAIN:
    			g2d.setColor(Config.LINE_COLOR_MOUNTAIN);
    			if (Globals.mountainFoldStyle == Constants.MountainFoldStyle.DASH_DOT) {
    				g2d.setStroke(Config.STROKE_MOUNTAIN_DASH_DOT);
    			} else if (Globals.mountainFoldStyle == Constants.MountainFoldStyle.DASH_DOT_DOT) {
    				g2d.setStroke(Config.STROKE_MOUNTAIN_DASH_DOT_DOT);
    			}    			
    			break;
    		case OriLine.TYPE_XRAY:
    			g2d.setColor(Config.LINE_COLOR_XRAY);
    			g2d.setStroke(Config.STROKE_XRAY);
    			break; 
    		case OriLine.TYPE_EDGE:
    			g2d.setColor(Config.LINE_COLOR_EDGE);
    			g2d.setStroke(Config.STROKE_EDGE);
    			break;
    		case OriLine.TYPE_CREASE:
    			g2d.setColor(Config.LINE_COLOR_CREASE);
    			g2d.setStroke(Config.STROKE_CREASE);
    		}

    		if (!Globals.dispColoredLines) {
    			g2d.setColor(Config.LINE_COLOR_EDGE);
    		}

    		//if line is selected during INPUT_LINE/SELECTION_TOOL mode, render GREEN
    		if ((Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE
    				&& line.isSelected())
    				|| (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL
    				&& line.isSelected())) {
    			g2d.setColor(Config.LINE_COLOR_SELECTED);
    			g2d.setStroke(Config.STROKE_SELECTED);
    		}
    		
    		if (line == firstSelectedL || line == secondSelectedL || line == thirdSelectedL) {
    			g2d.setColor(Color.RED);
    			g2d.setStroke(Config.STROKE_SELECTED);
    		} else if (line == selectedCandidateL) {
    			g2d.setColor(Config.LINE_COLOR_SELECTED);
    			g2d.setStroke(Config.STROKE_SELECTED);
    		}
    		Vector2d p0 = line.getP0();
    		Vector2d p1 = line.getP1();

    		if (line.getType() == OriLine.TYPE_CREASE) {
    			if (line.isStartTransl()) {
    				p0 = line.getTranslatedP0();
    			} else {
    				p0 = line.getP0();
    			}
    			if (line.isEndTransl()) {
    				p1 = line.getTranslatedP1();
    			} else {
    				p1 = line.getP1();
    			}
    		}
    		g2d.draw(new Line2D.Double(p0.x, p0.y, p1.x, p1.y));
    	}
    }
    
    private void renderAllTempLines() {
        if (firstSelectedV != null) {
     	   switch (Globals.inputLineType) {
     	   		case OriLine.TYPE_NONE:
     	   			g2d.setColor(Config.LINE_COLOR_SELECTED);//TODO: TYPE_NONE COLOR
     	   			g2d.setStroke(Config.STROKE_SELECTED);
     	   			break;
     	   		case OriLine.TYPE_VALLEY:
     	   			g2d.setColor(Config.LINE_COLOR_VALLEY);
     	   			g2d.setStroke(Config.STROKE_VALLEY);
     	   			break;
     	   		case OriLine.TYPE_MOUNTAIN:
     	   			g2d.setColor(Config.LINE_COLOR_MOUNTAIN);
     	   			if (Globals.mountainFoldStyle == Constants.MountainFoldStyle.DASH_DOT) {
         	   			g2d.setStroke(Config.STROKE_MOUNTAIN_DASH_DOT);
     	   			} else if (Globals.mountainFoldStyle == Constants.MountainFoldStyle.DASH_DOT_DOT) {
         	   			g2d.setStroke(Config.STROKE_MOUNTAIN_DASH_DOT_DOT);
     	   			}
     	   			break;
     	   		case OriLine.TYPE_XRAY:
     	   			g2d.setColor(Config.LINE_COLOR_XRAY);
     	   			g2d.setStroke(Config.STROKE_XRAY);
     	   			break;
     	   		case OriLine.TYPE_EDGE:
     	   			g2d.setColor(Config.LINE_COLOR_EDGE);
     	   			g2d.setStroke(Config.STROKE_EDGE);
     	   			break;
     	   		case OriLine.TYPE_CREASE:
     	   			g2d.setColor(Config.LINE_COLOR_CREASE);
     	   			g2d.setStroke(Config.STROKE_CREASE);
     	   }
     	   if (!Globals.dispColoredLines) {
     		   g2d.setColor(Config.LINE_COLOR_EDGE);
     	   }
     	   
     	   g2d.fill(new Rectangle2D.Double(firstSelectedV.x - 5.0 / Globals.SCALE,
     			   firstSelectedV.y - 5.0 / Globals.SCALE, 10.0 / Globals.SCALE, 10.0 / Globals.SCALE));
     	   
     	   if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE) {
         	   if (Globals.lineEditMode == Constants.LineInputMode.INPUT_LINE) {
         		   Vector2d cv = selectedCandidateV == null 
         				   ? new Vector2d(currentMousePointLogic.getX(), currentMousePointLogic.getY()) 
         					: selectedCandidateV;
         			g2d.draw(new Line2D.Double(firstSelectedV.x, firstSelectedV.y, cv.x, cv.y));
         	   }
     	   }
        }
    }
    
    private void renderTempOriGeomSymbol() {
        if (currentMouseDraggingPoint != null && (Globals.toolbarMode == Constants.ToolbarMode.INPUT_SYMBOL 
     		   && (Globals.inputSymbolMode == Constants.InputSymbolMode.X_RAY_CIRCLE 
     		   || Globals.inputSymbolMode == Constants.InputSymbolMode.CRIMPING_PLEATING))) {
     	   Point2D.Double sp = new Point2D.Double();
     	   Point2D.Double ep = new Point2D.Double();
     	   try {
     		   affineTransform.inverseTransform(preMousePoint, sp);
     		   affineTransform.inverseTransform(currentMouseDraggingPoint, ep);
     	   } catch (NoninvertibleTransformException e) {
     		   e.printStackTrace();
     	   }
     	   
     	   g2d.setStroke(Config.STROKE_SELECTED);
     	   //Vector2d pre = new Vector2d(preMousePoint.getX(), preMousePoint.getY());
     	   //Vector2d after = new Vector2d(currentMouseDraggingPoint.getX(), currentMouseDraggingPoint.getY());

     	   //double radius = GeometryUtil.Distance(pre, after);
     	   double width = ep.x - sp.x;
     	   double height = ep.y - sp.y;
 			if (width > height) {
 		    	   g2d.draw(new Ellipse2D.Double(sp.x, sp.y, width, width));
 			} else if (height > width) {
 		    	   g2d.draw(new Ellipse2D.Double(sp.x, sp.y, height, height));
 			}
        }
    }
    
    private void renderAllOriLeaderBoxes() {
 	   for (OriLeaderBox l : Origrammer.diagram.steps.get(Globals.currentStep).leaderBoxSymbols) {
 		   
 		   if (l.isSelected() || selectedCandidateLeader == l) {
     		   g2d.setColor(Config.LINE_COLOR_SELECTED);
     		   g2d.setStroke(Config.STROKE_EDGE);
     		   l.getLabel().setBorder(new EtchedBorder(BevelBorder.RAISED, Color.GREEN, getBackground().brighter()));
 		   } else {
 			   g2d.setColor(Config.LINE_COLOR_EDGE);
     		   g2d.setStroke(Config.STROKE_EDGE);
 			   l.getLabel().setBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()));
 		   }
 		   
 		   if (l.getType() == OriLeaderBox.TYPE_LEADER) {
 			   l.getLabel().setBorder(BorderFactory.createEmptyBorder());
 		   }
 		   
 		   add(l.getLabel());
     	   g2d.draw(new Line2D.Double(l.line.getP0().x, l.line.getP0().y, l.line.getP1().x, l.line.getP1().y));
 	   }
    }
    
    private void renderAllOriGeomSymbols() {
	   for (OriGeomSymbol s : Origrammer.diagram.steps.get(Globals.currentStep).geomSymbols) {
    	   
		   if (s.isSelected() || selectedCandidateGS == s) {
    		   g2d.setColor(Config.LINE_COLOR_SELECTED);
    		   g2d.setStroke(Config.STROKE_EDGE);
		   } else {
			   g2d.setColor(Config.LINE_COLOR_EDGE);
    		   g2d.setStroke(Config.STROKE_EDGE);
		   }
		   Shape circle = new Ellipse2D.Double(s.getPosition().x, s.getPosition().y, s.getRadius(), s.getRadius());

		   if (s.getType() == OriGeomSymbol.TYPE_CLOSED_SINK) {
			   g2d.draw(circle);
			   g2d.fill(circle);
		   } else {
			   g2d.draw(circle);
		   }
	   }
    }
    
    private void renderAllEquDistSymbols() {
	   for (OriEqualDistSymbol eds : Origrammer.diagram.steps.get(Globals.currentStep).equalDistSymbols) {
		   
		   if (eds.isSelected() || selectedCandidateEDS == eds) {
			   g2d.setStroke(Config.STROKE_EDGE);
			   g2d.setColor(Color.GREEN);
		   } else {
			   g2d.setStroke(Config.STROKE_EDGE);
			   g2d.setColor(Color.BLACK);
		   }
		   ArrayList<Shape> shapes = eds.getShapesForDrawing();
		   for (Shape s : shapes) {
			   g2d.draw(s);
		   }
	   }
    }
    
    private void renderAllEquAnglSymbols() {
 	   for (OriEqualAnglSymbol eas : Origrammer.diagram.steps.get(Globals.currentStep).equalAnglSymbols) {
		   if (eas.isSelected() || selectedCandidateEAS == eas) {
			   g2d.setStroke(Config.STROKE_EDGE);
			   g2d.setColor(Color.GREEN);
		   } else {
			   g2d.setStroke(Config.STROKE_EDGE);
			   g2d.setColor(Color.BLACK);
		   }
		   
		   ArrayList<Shape> shapes = eas.getShapesForDrawing();
		   for (Shape s : shapes) {
			   g2d.draw(s);
		   }
	   }
    }
    
    private void renderAllCrimpsPleats() {
 	   for (OriPleatCrimpSymbol pleat : Origrammer.diagram.steps.get(Globals.currentStep).pleatCrimpSymbols) {
		   if (pleat.isSelected() || selectedCandidatePleat == pleat) {
			   g2d.setStroke(Config.STROKE_EDGE);
			   g2d.setColor(Color.GREEN);
		   } else {
			   g2d.setStroke(Config.STROKE_EDGE);
			   g2d.setColor(Color.BLACK);
		   }
		   
		   ArrayList<Shape> shapes = pleat.getShapesForDrawing();
		   for (Shape s : shapes) {
			   g2d.draw(s);
		   }
	   }
    }
    
    private void renderAllVertices() {
        if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_VERTEX || Globals.dispVertex) {
     	   g2d.setColor(Color.BLACK);
     	   double vertexDrawSize = 3.0;
     	   for (OriLine line : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
     		   Vector2d v0 = line.getP0();
     		   Vector2d v1 = line.getP1();
     		   g2d.fill(new Rectangle2D.Double(v0.x - vertexDrawSize / Globals.SCALE,
     				   v0.y - vertexDrawSize / Globals.SCALE, vertexDrawSize * 2 / Globals.SCALE,
     				   vertexDrawSize * 2 / Globals.SCALE));
     		   g2d.fill(new Rectangle2D.Double(v1.x - vertexDrawSize / Globals.SCALE,
     				   v1.y - vertexDrawSize / Globals.SCALE, vertexDrawSize * 2 / Globals.SCALE,
     				   vertexDrawSize * 2 / Globals.SCALE));
     	   }
     	   
     	   for (OriVertex v : Origrammer.diagram.steps.get(Globals.currentStep).vertices) {
     		   if (v.isSelected() || v.p == selectedCandidateV) {
     			   g2d.setColor(Config.LINE_COLOR_SELECTED);
     			   vertexDrawSize = 5.0;
     		   } else {
     			   g2d.setColor(Config.LINE_COLOR_EDGE);
     		   }
     		  g2d.fill(new Rectangle2D.Double(v.p.x - vertexDrawSize / Globals.SCALE,
    				   v.p.y - vertexDrawSize / Globals.SCALE, vertexDrawSize * 2 / Globals.SCALE,
    				   vertexDrawSize * 2 / Globals.SCALE));
     	   }
        }
    }
    
    private void renderRectSelection() {
        if (currentMouseDraggingPoint != null && (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL)) {
     	   Point2D.Double sp = new Point2D.Double();
     	   Point2D.Double ep = new Point2D.Double();
     	   try {
     		   affineTransform.inverseTransform(preMousePoint, sp);
     		   affineTransform.inverseTransform(currentMouseDraggingPoint, ep);
     	   } catch (NoninvertibleTransformException e) {
     		   e.printStackTrace();
     	   }
     	   
     	   g2d.setStroke(Config.STROKE_EDGE);
     	   g2d.setColor(Color.BLACK);
     	   double sx = Math.min(sp.x, ep.x);
     	   double sy = Math.min(sp.y, ep.y);
     	   double w = Math.abs(sp.x - ep.x);
     	   double h = Math.abs(sp.y - ep.y);
     	   g2d.draw(new Rectangle2D.Double(sx, sy, w, h));
        }
    }
    
    //#######################################################################################
    //#######################################################################################

    public static BufferedImage rasterize(File svgFile) throws IOException {
        final BufferedImage[] imagePointer = new BufferedImage[1];
        // Rendering hints can't be set programatically, so
        // we override defaults with a temporary stylesheet.
        // These defaults emphasize quality and precision, and
        // are more similar to the defaults of other SVG viewers.
        // SVG documents can still override these defaults.
        String css = "svg {" +
                "shape-rendering: geometricPrecision;" +
                "text-rendering:  geometricPrecision;" +
                "color-rendering: optimizeQuality;" +
                "image-rendering: optimizeQuality;" +
                "}";
        File cssFile = File.createTempFile("batik-default-override-", ".css");
        FileUtils.writeStringToFile(cssFile, css, "ISO-8859-1");
        
        TranscodingHints transcoderHints = new TranscodingHints();
        transcoderHints.put(ImageTranscoder.KEY_XML_PARSER_VALIDATING, Boolean.FALSE);
        transcoderHints.put(ImageTranscoder.KEY_DOM_IMPLEMENTATION,
                SVGDOMImplementation.getDOMImplementation());
        transcoderHints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI,
                SVGConstants.SVG_NAMESPACE_URI);
        transcoderHints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT, "svg");
        transcoderHints.put(ImageTranscoder.KEY_USER_STYLESHEET_URI, cssFile.toURI().toString());
        try {
            TranscoderInput input = new TranscoderInput(new FileInputStream(svgFile));
            ImageTranscoder t = new ImageTranscoder() {
                @Override
                public BufferedImage createImage(int w, int h) {
                    return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                }
                @Override
                public void writeImage(BufferedImage image, TranscoderOutput out)
                        throws TranscoderException {
                    imagePointer[0] = image;
                }
            };
            t.setTranscodingHints(transcoderHints);
            t.transcode(input, null);
        }
        catch (TranscoderException ex) {
            // Requires Java 6
            ex.printStackTrace();
            throw new IOException("Couldn't convert " + svgFile);
        }
        finally {
            cssFile.delete();
        }
        return imagePointer[0];
    }
    
    
    
    private void drawGrid(Graphics2D g2d) {
    	g2d.setColor(Color.LIGHT_GRAY);
    	g2d.setStroke(Config.STROKE_GRID);
    	
    	int lineNum = Globals.gridDivNum;
    	double step = Origrammer.diagram.steps.get(Globals.currentStep).size / lineNum;
    	for (int i=1; i<lineNum; i++) {
    		g2d.draw(new Line2D.Double(step * i - Origrammer.diagram.paperSize / 2.0, -Origrammer.diagram.paperSize / 2.0, 
    									step * i - Origrammer.diagram.paperSize / 2.0, Origrammer.diagram.paperSize / 2.0));
    		g2d.draw(new Line2D.Double(-Origrammer.diagram.paperSize / 2.0, step * i - Origrammer.diagram.paperSize / 2.0,
    									Origrammer.diagram.paperSize / 2.0, step * i - Origrammer.diagram.paperSize / 2.0));
    	}    	
    }
    
    public void setDispGrid(boolean dispGrid) {
    	this.dispGrid = dispGrid;
    	resetPickedElements();
    	repaint();
    }
    
    public void modeChanged() {
    	resetPickedElements();
    	repaint();
    }
    
    public void resetPickedElements() {
    	firstSelectedV = null;
    	secondSelectedV = null;
    	thirdSelectedV = null;
    	selectedCandidateV = null;
    	firstSelectedL = null;
    	secondSelectedL = null;
    	thirdSelectedL = null;
    	selectedCandidateL = null;
    	selectedCandidateA = null;
    	selectedCandidateLeader = null;
    	selectedCandidatePS = null;
    	tmpLeader = new OriLeaderBox();
    }
    
    //update the AffineTransform
    private void updateAffineTransform(Graphics2D g2d) {
    	affineTransform.setToTranslation(getWidth()*0.5+transX, getHeight()*0.5+transY);
    	affineTransform.scale(Globals.SCALE, Globals.SCALE);
        //affineTransform.setToTranslation(Constants.DEFAULT_PAPER_SIZE, Constants.DEFAULT_PAPER_SIZE);
    	g2d.transform(affineTransform);
    }
    public BufferedImage getBufImgByTypeAndRot(OriPicSymbol s) {
    	BufferedImage img = null;
    	String fileName = null;
    	double rotation = s.getDegrees();
    	String rot = "";
    	switch (s.getType()) {
    	case OriPicSymbol.TYPE_ROTATION:
    		fileName = "rotation";
    		break;
    	case OriPicSymbol.TYPE_NEXT_VIEW_HERE:
    		fileName = "nextViewHere";
    		break;
    	case OriPicSymbol.TYPE_HOLD:
    		fileName = "hold";
    		break;
    	case OriPicSymbol.TYPE_HOLD_AND_PULL:
    		fileName = "holdAndPull";
    		break;
    	default:
    		fileName = "rotation";
    		break;
    	}
    	
    	//add degrees to fileName 
    	if (rotation < 22.5) {
    		rot = "0";
    	} else if(rotation > 0 && rotation < 45) {
    		rot = "22,5";
    	} else if (rotation > 22.5 && rotation < 67.5) {
    		rot = "45";
    	} else if (rotation > 45 && rotation < 90) {
    		rot = "67,5";
    	} else if (rotation > 67.5 && rotation < 112.5) {
    		rot = "90";
    	} else if (rotation > 90 && rotation < 135) {
    		rot = "112,5";
    	} else if (rotation > 112.5 && rotation < 157.5) {
    		rot = "135";
    	} else if (rotation > 135 && rotation < 180) {
    		rot = "157,5";
    	} else if (rotation > 157.5 && rotation < 202.5) {
    		rot = "180";
    	} else if (rotation > 180 && rotation < 225) {
     		rot = "202,5";
     	} else if (rotation > 202.5 && rotation < 247.5) {
     		rot = "225";
     	} else if (rotation > 225 && rotation < 270) {
    		rot = "247,5";
    	} else if (rotation > 247.5 && rotation < 292.5) {
    		rot = "270";
    	} else if (rotation > 270 && rotation < 315) {
    		rot = "292,5";
    	} else if (rotation > 292.5 && rotation < 337.5) {
    		rot = "315";
    	} else if (rotation > 315 && rotation < 360) {
    		rot = "337,5";
    	} else if (rotation > 337.5) {
    		rot = "0";
    	}

    	File file = new File("./images/" + fileName +"/" + rot + ".svg");
    	//BufferedImage img = null;
    	try {
    		img = rasterize(file);
    	} catch (IOException e1) {
    		e1.printStackTrace();
    	}
    	    	
    	return img;
    }
    
    public BufferedImage getBufImgByTypeAndRot(OriArrow a) {
    	BufferedImage img = null;
    	String fileName = null;
    	double rotation = a.getDegrees();
    	String rot = "";
    	switch (a.getType()) {
    	case OriArrow.TYPE_VALLEY:
    		fileName = "valleyFold";
    		break;
    	case OriArrow.TYPE_MOUNTAIN:
    		fileName = "mountainFold";
    		break;
    	case OriArrow.TYPE_TURN_OVER:
    		fileName = "turnOver";
    		break;
    	case OriArrow.TYPE_PUSH_HERE:
    		fileName = "pushHere";
    		break;
    	case OriArrow.TYPE_PULL_HERE:
    		fileName = "pullHere";
    		break;
    	case OriArrow.TYPE_INFLATE_HERE:
    		fileName = "inflateHere";
    		break;
    	default:
    		fileName = "origrammer";
    		break;
    	}
    	
    	//add degrees to fileName 
    	if (rotation < 22.5) {
    		rot = "0";
    	} else if(rotation > 0 && rotation < 45) {
    		rot = "22,5";
    	} else if (rotation > 22.5 && rotation < 67.5) {
    		rot = "45";
    	} else if (rotation > 45 && rotation < 90) {
    		rot = "67,5";
    	} else if (rotation > 67.5 && rotation < 112.5) {
    		rot = "90";
    	} else if (rotation > 90 && rotation < 135) {
    		rot = "112,5";
    	} else if (rotation > 112.5 && rotation < 157.5) {
    		rot = "135";
    	} else if (rotation > 135 && rotation < 180) {
    		rot = "157,5";
    	} else if (rotation > 157.5 && rotation < 202.5) {
    		rot = "180";
    	} else if (rotation > 180 && rotation < 225) {
     		rot = "202,5";
     	} else if (rotation > 202.5 && rotation < 247.5) {
     		rot = "225";
     	} else if (rotation > 225 && rotation < 270) {
    		rot = "247,5";
    	} else if (rotation > 247.5 && rotation < 292.5) {
    		rot = "270";
    	} else if (rotation > 270 && rotation < 315) {
    		rot = "292,5";
    	} else if (rotation > 292.5 && rotation < 337.5) {
    		rot = "315";
    	} else if (rotation > 315 && rotation < 360) {
    		rot = "337,5";
    	} else if (rotation > 337.5) {
    		rot = "0";
    	}

    	File file = new File("./images/" + fileName +"/" + rot + ".svg");
    	//BufferedImage img = null;
    	try {
    		img = rasterize(file);
    	} catch (IOException e1) {
    		e1.printStackTrace();
    	}
    	    	
    	return img;
    }
    
    //#######################################################################################
    //#######################################################################################

    private Vector2d pickVertex(Point2D.Double p) {
    	double minDistance = Double.MAX_VALUE;
    	Vector2d minPosition = new Vector2d();
    	
    	for (OriLine line : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
    		double dist0 = p.distance(line.getP0().x, line.getP0().y);
    		if(dist0 < minDistance) {
    			minDistance = dist0;
    			minPosition.set(line.getP0());
    		}
    		double dist1 = p.distance(line.getP1().x, line.getP1().y);
    		if (dist1 < minDistance) {
    			minDistance = dist1;
    			minPosition.set(line.getP1());
    		}
    	}
    	
    	for (OriVertex v : Origrammer.diagram.steps.get(Globals.currentStep).vertices) {
    		double dist = p.distance(v.p.x, v.p.y);
    		if (dist < minDistance) {
    			minDistance = dist;
    			minPosition.set(v.p);
    		}
    	}
    	
    	if (dispGrid) {
    		double step = Origrammer.diagram.paperSize / Globals.gridDivNum;
    		for (int ix = 0; ix < Globals.gridDivNum +1; ix++) {
    			for (int iy = 0; iy < Globals.gridDivNum + 1; iy++) {
    				double x = -Origrammer.diagram.paperSize / 2 + step * ix;
    				double y = -Origrammer.diagram.paperSize / 2 + step * iy;
    				double dist = p.distance(x, y);
    				if (dist < minDistance) {
    					minDistance = dist;
    					minPosition.set(x, y);
    				}
    			}	
    		}
    	}
    	
    	if (minDistance < 10.0 / Globals.SCALE) {
    		return minPosition;
    	} else {
    		return null;
    	}
    }
    
    private OriLine pickLine(Point2D.Double p) {
    	double minDistance = Double.MAX_VALUE;
    	OriLine bestLine = null;
    	
    	for(OriLine line : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
    		double dist = GeometryUtil.DistancePointToSegment(new Vector2d(p.x, p.y), line.getP0(), line.getP1());
    		if (dist < minDistance) {
    			minDistance = dist;
    			bestLine = line;
    		}
    	}
    	
    	if (minDistance / Globals.SCALE < 10) {
    		return bestLine;
    	} else {
    		return null;
    	}
    }
    
    private OriArrow  pickArrow(Point2D.Double p) {
    	//double minDistance = Double.MAX_VALUE;
    	OriArrow bestArrow = null;
    	
    	for(OriArrow arrow : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
    		boolean pickedA = GeometryUtil.isMouseOverArrow(p, arrow);
    		if (pickedA) {
    			bestArrow = arrow;
    		}
    	}
    	if (bestArrow != null) {
    		return bestArrow;
    	} else {
    	  		return null;
    	}
    }
    
    private OriFace pickFace(Point2D.Double p) {
    	OriFace bestFace = null;
    	
    	for (OriFace face : Origrammer.diagram.steps.get(Globals.currentStep).filledFaces) {
    		boolean pickedF = GeometryUtil.isMouseOverFace(p, face);
    		if (pickedF) {
    			bestFace = face;
    		}
    	}
    	
    	if (bestFace != null) {
    		return bestFace;
    	} else {
    		return null;
    	}
    }
    
    private OriLeaderBox pickLeader(Point2D.Double p) {
    	double minDistance = Double.MAX_VALUE;
    	OriLeaderBox bestLeader = null;
    	for (OriLeaderBox leader : Origrammer.diagram.steps.get(Globals.currentStep).leaderBoxSymbols) {
    		
    		if (GeometryUtil.isMouseOverRectangle(p, leader.getLabel().getBounds())) {
    			return leader;
    		} else {
        		double dist = GeometryUtil.DistancePointToSegment(new Vector2d(p.x, p.y), leader.line.getP0(), leader.line.getP1());
        		if (dist < minDistance) {
        			minDistance = dist;
        			bestLeader = leader;
        		}
    		}
    	}
    	
    	if (minDistance / Globals.SCALE < 10) {
    		return bestLeader;
    	} else {
    		return null;
    	}
    }
    
    private OriPicSymbol  pickPicSymbol(Point2D.Double p) {
    	//double minDistance = Double.MAX_VALUE;
    	OriPicSymbol bestSymbol = null;
    	
    	for(OriPicSymbol symbol : Origrammer.diagram.steps.get(Globals.currentStep).picSymbols) {
    		boolean pickedS = GeometryUtil.isMouseOverSymbol(p, symbol);
    		if (pickedS) {
    			bestSymbol = symbol;
    		}
    	}
    	if (bestSymbol != null) {
    		return bestSymbol;
    	} else {
    	  		return null;
    	}
    }
    
    private OriGeomSymbol  pickGeomSymbol(Point2D.Double p) {
    	//double minDistance = Double.MAX_VALUE;
    	OriGeomSymbol bestSymbol = null;
    	
    	for (OriGeomSymbol symbol : Origrammer.diagram.steps.get(Globals.currentStep).geomSymbols) {
    		boolean pickedS = GeometryUtil.isMouseOverGeomSymbol(p, symbol);
    		if (pickedS) {
    			bestSymbol = symbol;
    		}
    	}
    	if (bestSymbol != null) {
    		return bestSymbol;
    	} else {
    	  		return null;
    	}
    }
    
    private OriEqualDistSymbol pickEqualDistSymbol(Point2D.Double p) {
    	OriEqualDistSymbol bestSymbol = null;
    	
    	for (OriEqualDistSymbol symbol : Origrammer.diagram.steps.get(Globals.currentStep).equalDistSymbols) {
    		boolean pickedS = GeometryUtil.isMouseOverEqualDistSymbol(p, symbol);
    		if (pickedS) {
    			bestSymbol = symbol;
    		}
    	}
    	if (bestSymbol != null) {
    		return bestSymbol;
    	} else {
    	  		return null;
    	}
    }
    
    private OriEqualAnglSymbol pickEqualAnglSymbol(Point2D.Double p) {
    	OriEqualAnglSymbol bestSymbol = null;
    	
    	for (OriEqualAnglSymbol symbol : Origrammer.diagram.steps.get(Globals.currentStep).equalAnglSymbols) {
    		boolean pickedS = GeometryUtil.isMouseOverShapes(p, symbol.getShapesForDrawing());
    		if (pickedS) {
    			bestSymbol = symbol;
    		}
    	}
    	if (bestSymbol != null) {
    		return bestSymbol;
    	} else {
    		return null;
    	}
    }
    
    private OriPleatCrimpSymbol pickPleatSymbol(Point2D.Double p) {
    	OriPleatCrimpSymbol bestSymbol = null;
    	
    	for (OriPleatCrimpSymbol symbol : Origrammer.diagram.steps.get(Globals.currentStep).pleatCrimpSymbols) {
    		boolean pickedS = GeometryUtil.isMouseOverShapes(p, symbol.getShapesForDrawing());
    		if (pickedS) {
    			bestSymbol = symbol;
    		}
    	}
    	if (bestSymbol != null) {
    		return bestSymbol;
    	} else {
    		return null;
    	}
    }
    //#######################################################################################
    //#######################################################################################

	/**
	 * Creates a filled face once 3 vertices are selected
	 * @param clickPoint
	 */
	private void createFilledFace(Point2D.Double clickPoint) {
		//creates OriFace that is to be filled with DEFAULT_PAPER_COLOR --> OriFace is a triangle with 3 OriLines as sides			
		Vector2d v = pickVertex(clickPoint);
		if (v != null) {
			if (firstSelectedV == null) {
				firstSelectedV = v;
			} else if (secondSelectedV == null) {
				secondSelectedV = v;
			} else {
				ArrayList<Vector2d> vList = new ArrayList<>();
				
				vList.add(v);
				vList.add(firstSelectedV);
				vList.add(secondSelectedV);
				OriFace newFace;
				GeneralPath filledFace = GeometryUtil.createFaceFromVertices(vList);
				if (Globals.faceInputDirection == Constants.FaceInputDirection.FACE_UP) {
					newFace = new OriFace(filledFace, false, true);
				} else {
					newFace = new OriFace(filledFace, false, false);
				}
				Origrammer.diagram.steps.get(Globals.currentStep).filledFaces.add(newFace);
				firstSelectedV = null;
				secondSelectedV = null;
				thirdSelectedV = null;
			}
		}		
	}
	
	private void createLeaderBox(Point2D.Double clickPoint) {
		Vector2d tmp = new Vector2d(clickPoint.x, clickPoint.y);
		if (firstSelectedV == null) {
			firstSelectedV = tmp;
		} else if (secondSelectedV == null) {
			Rectangle tmpRect = new Rectangle();
			secondSelectedV = tmp;
			tmpLeader.line.setP0(firstSelectedV);
			tmpLeader.line.setP1(secondSelectedV);
			tmpLeader.setText(Origrammer.mainFrame.uiTopPanel.inputLeaderText.getText());
			tmpLeader.setSelected(false);
			
			if (tmpLeader.getLabel().getText().length() == 0) {
				JOptionPane.showMessageDialog(this,  Origrammer.res.getString("Error_EmptyLeaderTextField"),
						"Error_EmptyLeaderTextField", 
						JOptionPane.ERROR_MESSAGE);
			} else {
				Rectangle2D labelBounds = g2d.getFontMetrics().getStringBounds(tmpLeader.getLabel().getText(), g2d);
				double width =  labelBounds.getWidth()+10;
				double height = labelBounds.getHeight();
				
				if (tmpLeader.line.getP0().y < tmpLeader.line.getP1().y) {
					if (tmpLeader.line.getP0().x < tmpLeader.line.getP1().x) {
						//bottom right
						tmpRect.setRect(tmpLeader.line.getP1().x, tmpLeader.line.getP1().y+1, width, height+2);
					} else {
						//bottom left
						tmpRect.setRect(tmpLeader.line.getP1().x-width+1, tmpLeader.line.getP1().y+1, width, height+2);
					}
				} else {
					if (tmpLeader.line.getP0().x < tmpLeader.line.getP1().x) {
						//top right
						tmpRect.setRect(tmpLeader.line.getP1().x+1, tmpLeader.line.getP1().y-height, width, height+2);
					} else {
						//top left
						tmpRect.setRect(tmpLeader.line.getP1().x-width+1, tmpLeader.line.getP1().y-height, width, height+2);
					}
				}
				tmpLeader.setPosition(tmpRect);
				if (Globals.inputSymbolMode == Constants.InputSymbolMode.LEADER) {
					tmpLeader.setType(OriLeaderBox.TYPE_LEADER);
				} else if (Globals.inputSymbolMode == Constants.InputSymbolMode.REPETITION_BOX) {
					tmpLeader.setType(OriLeaderBox.TYPE_REPETITION);
				}
				Origrammer.diagram.steps.get(Globals.currentStep).addLeader(tmpLeader);
			}
			
			firstSelectedV = null;
			secondSelectedV = null;
		}
	}
	
	private void createEqualDistSymbol(Point2D.Double clickPoint) {
		Vector2d v = pickVertex(clickPoint);
		
		if (firstSelectedV == null) {
			firstSelectedV = v;
		} else if (secondSelectedV == null) {
			secondSelectedV = v;
			
			OriEqualDistSymbol tmpEquDistSymbol = new OriEqualDistSymbol();
			
			tmpEquDistSymbol.setTranslationDist(Origrammer.mainFrame.uiTopPanel.sliderEqualDist.getValue());
			tmpEquDistSymbol.setDividerCount(Integer.parseInt(Origrammer.mainFrame.uiTopPanel.equalDistDividerTF.getText()));
			tmpEquDistSymbol.setP0(firstSelectedV);
			tmpEquDistSymbol.setP1(secondSelectedV);
	
			Origrammer.diagram.steps.get(Globals.currentStep).addEqualDistSymbol(tmpEquDistSymbol);
			
			firstSelectedV = null;
			secondSelectedV = null;
		}
	}
	
	private void createEqualAnglSymbol(Point2D.Double clickPoint) {
		Vector2d v = pickVertex(clickPoint);
		
		if (firstSelectedV == null) {
			firstSelectedV = v;
		} else if (secondSelectedV == null) {
			secondSelectedV = v;
		} else if (thirdSelectedV == null) {
			thirdSelectedV = v;
			
			Vector2d uv1 = GeometryUtil.getUnitVector(firstSelectedV, secondSelectedV);
			Vector2d uv2 = GeometryUtil.getUnitVector(firstSelectedV, thirdSelectedV);

			double dist1 = GeometryUtil.Distance(firstSelectedV, secondSelectedV);
			double dist2 = GeometryUtil.Distance(firstSelectedV, thirdSelectedV);
			
			//if Distance(v,a) and Distance(v,b) are not equal --> set both to the lower value
			if (dist1 < dist2) {
				thirdSelectedV.x = firstSelectedV.x + uv2.x*dist1;
				thirdSelectedV.y = firstSelectedV.y + uv2.y*dist1;

			} else if (dist2 < dist1) {
				secondSelectedV.x = firstSelectedV.x + uv1.x*dist2;
				secondSelectedV.y = firstSelectedV.y + uv1.y*dist2;
			}

			
			OriEqualAnglSymbol tmpEquAnglSymbol = new OriEqualAnglSymbol(firstSelectedV, secondSelectedV, thirdSelectedV);
			tmpEquAnglSymbol.setDividerCount(Integer.parseInt(Origrammer.mainFrame.uiTopPanel.equalAnglDividerTF.getText()));
			
			Origrammer.diagram.steps.get(Globals.currentStep).addEqualAngleSymbol(tmpEquAnglSymbol);
			
			firstSelectedV = null;
			secondSelectedV = null;
			thirdSelectedV = null;
		}
	}
	
	private void createPleatCrimpSymbol(Point2D.Double clickPoint) {
		Vector2d tmp = new Vector2d(clickPoint.x, clickPoint.y);

		if (firstSelectedV == null) {
			firstSelectedV = tmp;
		
			OriPleatCrimpSymbol tmpPCSymbol = new OriPleatCrimpSymbol(firstSelectedV, 
										Origrammer.mainFrame.uiTopPanel.pleatCB.isSelected(),
										Integer.parseInt(Origrammer.mainFrame.uiTopPanel.pleatTF.getText()));
			if (Origrammer.mainFrame.uiTopPanel.pleatRB.isSelected()) {
				tmpPCSymbol.setType(OriPleatCrimpSymbol.TYPE_PLEAT);
			} else {
				tmpPCSymbol.setType(OriPleatCrimpSymbol.TYPE_CRIMP);
			}
			Origrammer.diagram.steps.get(Globals.currentStep).addPleatSymbol(tmpPCSymbol);

			
			firstSelectedV = null;
		}
	}
	
	private void createClosedSinkDot(Point2D.Double clickPoint) {
		Vector2d tmp = new Vector2d(clickPoint.x, clickPoint.y);
	
		if (firstSelectedV == null) {
			firstSelectedV = tmp;
			
			OriGeomSymbol tmpGeoSymbol = new OriGeomSymbol(firstSelectedV, 10);
			tmpGeoSymbol.setType(OriGeomSymbol.TYPE_CLOSED_SINK);
			Origrammer.diagram.steps.get(Globals.currentStep).addGeomSymbol(tmpGeoSymbol);
			
			firstSelectedV = null;
		}
	}
	
	private void createTriangleInsector(Point2D.Double clickPoint) {
		//creates Insector of the triangle with edge points {firstSelectedV, secondSelectedV, v}
		Vector2d v = pickVertex(clickPoint);
		if (v != null) {
			if (firstSelectedV == null) {
				firstSelectedV = v;
			} else if (secondSelectedV == null) {
				secondSelectedV = v;
			} else {
				Origrammer.diagram.steps.get(Globals.currentStep).addTriangleInsectorLines(firstSelectedV, secondSelectedV, v);
				firstSelectedV = null;
				secondSelectedV = null;
			}
		}
	}
	
    private void createVertexAbsolutePos(Point2D.Double clickPoint) {
    	Vector2d p = new Vector2d(clickPoint.x, clickPoint.y);
    	Origrammer.diagram.steps.get(Globals.currentStep).addVertex(p);
    }
    
    private void createVertexFractionOfLine(Point2D.Double clickPoint) {
    	OriLine l = pickLine(clickPoint);
    	
    	if (firstSelectedL == null && (l != null)) {
    		firstSelectedL = l;
    		
    		double fraction = Double.parseDouble(Origrammer.mainFrame.uiTopPanel.inputVertexFractionTF.getText());
    		double dist = GeometryUtil.Distance(l.getP0(), l.getP1());
    		Vector2d uv = GeometryUtil.getUnitVector(l.getP0(), l.getP1());
    		
    		double newX = l.getP0().x + uv.x * (dist*(fraction/100)); 
    		double newY = l.getP0().y + uv.y * (dist*(fraction/100));
    		
    		Vector2d newVertex = new Vector2d(newX, newY);
    		
	   		OriLine first = new OriLine(l.getP0(), newVertex, l.getType());
    		OriLine second = new OriLine(newVertex, l.getP1(), l.getType());
    		Origrammer.diagram.steps.get(Globals.currentStep).addLine(first);
    		Origrammer.diagram.steps.get(Globals.currentStep).addLine(second);
    		Origrammer.diagram.steps.get(Globals.currentStep).lines.remove(l);
 
    	}
    	firstSelectedL = null;
    }
	
    //#######################################################################################
    //#######################################################################################

	
	private void selectOnClickPoint(Point2D.Double clickPoint) {
		//select OriLine or unselect all OriLines if clicked on nothing
		OriLine l = pickLine(clickPoint);
		if (l != null) {
			if (!l.isSelected()) {
				l.setSelected(true);
			} else {
				l.setSelected(false);
			}
		} else {
			Origrammer.diagram.steps.get(Globals.currentStep).unselectAllLines();
		}
		
		//select OriVertex or unselect all OriVertices if clicked on nothing
		Vector2d p = pickVertex(clickPoint);
		if (p != null) {
			for (OriVertex v : Origrammer.diagram.steps.get(Globals.currentStep).vertices) {
				if (v.p.x == p.x && v.p.y == p.y) {
					if (v != null) {
						if (!v.isSelected()) {
							v.setSelected(true);
						} else {
							v.setSelected(false);
						}
					} 
				} 
			}
		} else {
			Origrammer.diagram.steps.get(Globals.currentStep).unselectAllVertices();
		}
		
		//select OriArrow or unselect all OriArrows if clicked on nothing
		OriArrow a = pickArrow(clickPoint);
		if (a != null) {
			if (!a.isSelected()) {
				a.setSelected(true);
			} else if (!isPressedOverSymbol) {
				a.setSelected(false);
			}
		} else {
			Origrammer.diagram.steps.get(Globals.currentStep).unselectAllArrows();
		}
		
		//select OriFace or unselect all OriFaces if clicked on nothing
		OriFace f = pickFace(clickPoint);
		if (f != null) {
			if (!f.isSelected()) {
				f.setSelected(true);
			} else {
				f.setSelected(false);
			}
		} else {
			Origrammer.diagram.steps.get(Globals.currentStep).unselectAllFaces();
		}
		
		//select OriLeader or unselect all OriLeader if clicked on nothing
		OriLeaderBox leader = pickLeader(clickPoint);
		if (leader != null) {
			if (!leader.isSelected()) {
				leader.setSelected(true);
			} else if (!isPressedOverSymbol) {
				leader.setSelected(false);
			}
		} else {
			Origrammer.diagram.steps.get(Globals.currentStep).unselectAllLeaders();
		}
		
		//select OriPicSymbol or unselect all OriPicSymbols if clicked on nothing
		OriPicSymbol s = pickPicSymbol(clickPoint);
		if (s != null) {
			if (!s.isSelected()) {
				s.setSelected(true);
			} else if (!isPressedOverSymbol) {
				s.setSelected(false);
			}
		} else {
			Origrammer.diagram.steps.get(Globals.currentStep).unselectAllPicSymbols();
		}
		
		//select OriGeomSymbol or unselect all OriGeomSymbols if clicked on nothing
		OriGeomSymbol gs = pickGeomSymbol(clickPoint);
		if (gs != null) {
			if (!gs.isSelected()) {
				gs.setSelected(true);
			} else if (!isPressedOverSymbol) {
				gs.setSelected(false);
			}
		} else {
			Origrammer.diagram.steps.get(Globals.currentStep).unselectAllGeomSymbols();
		}
		
		//select OriEqualDistSymbol or unselect all OriEqualDistSymbols if clicked on nothing
		OriEqualDistSymbol eds = pickEqualDistSymbol(clickPoint);
		if (eds != null) {
			if (!eds.isSelected()) {
				eds.setSelected(true);
			} else if (!isPressedOverSymbol) {
				eds.setSelected(false);
			}
		} else {
			Origrammer.diagram.steps.get(Globals.currentStep).unselectAllEqualDistSymbols();
		}
		
		//select OriEqualAnglSymbol or unselect all OriEqualAnglSymbols if clicked on nothing
		OriEqualAnglSymbol eas = pickEqualAnglSymbol(clickPoint);
		if (eas != null) {
			if (!eas.isSelected()) {
				eas.setSelected(true);
			} else if (!isPressedOverSymbol) {
				eas.setSelected(false);
			}
		} else {
			Origrammer.diagram.steps.get(Globals.currentStep).unselectAllEqualAnglSymbols();
		}
		
		//select OriPleatSymbol or unselect all OriPleatSymbols if clicked on nothing
		OriPleatCrimpSymbol pleat = pickPleatSymbol(clickPoint);
		if (pleat != null) {
			if (!pleat.isSelected()) {
				pleat.setSelected(true);
			} else if (!isPressedOverSymbol) {
				pleat.setSelected(false);
			}
		} else {
			Origrammer.diagram.steps.get(Globals.currentStep).unselectAllPleatSymbols();
		}
	
		Origrammer.mainFrame.uiTopPanel.modeChanged();
	}
	
	
	private void measureLength(Point2D.Double clickPoint) {
		//MEASURE LENGTH (vertex - vertex)
		Vector2d v = pickVertex(clickPoint);
		if (v != null) {
			if (firstSelectedV == null) {
				firstSelectedV = v;
			} else {
				double length = GeometryUtil.measureLength(firstSelectedV, v);
				Origrammer.mainFrame.uiSidePanel.measureLengthTF.setValue(length);;
				firstSelectedV = null;
			}
		}
		//MEASURE LENGTH (OriLine.v0 - OriLine.v1)
		OriLine l = pickLine(clickPoint);
		if (v == null && l != null) {
			double length = GeometryUtil.measureLength(l.getP0(), l.getP1());
			Origrammer.mainFrame.uiSidePanel.measureLengthTF.setValue(length);;
			firstSelectedL = null;
		}
	}
	private void measureAngle(Point2D.Double clickPoint) {
		//MEASURE ANGLE (vertex - vertex - vertex) --> measures angle of first selected vertex
		Vector2d v = pickVertex(clickPoint);
		if (v != null) {
			if (firstSelectedV == null) {
				firstSelectedV = v;
			} else if (secondSelectedV == null) {
				secondSelectedV = v;
			} else {
				double angle = GeometryUtil.measureAngle(firstSelectedV, secondSelectedV, v);
				Origrammer.mainFrame.uiSidePanel.measureAngleTF.setValue(angle);;
				firstSelectedV = null;
				secondSelectedV = null;
			}
		}
		//MEASURE ANGLE (line - line) --> measures angle between two OriLines that share one point
		OriLine l = pickLine(clickPoint);
		if(v == null && l != null) {
			if (firstSelectedL == null) {
				firstSelectedL = l;
			} else {
				double angle = GeometryUtil.measureAngle(firstSelectedL, l);
				Origrammer.mainFrame.uiSidePanel.measureAngleTF.setValue(angle);
				firstSelectedL = null;
			}
		}
	}
	
	
	//############################################
	//############## MOUSE LISTENER ##############
	//############################################
	

	@Override
	public void mouseClicked(MouseEvent e) {
		//if right clicked, remove any selected vertices
		if (SwingUtilities.isRightMouseButton(e)) {
			if (firstSelectedV != null) {
				firstSelectedV = null;
				repaint();
			} else if (secondSelectedV != null) {
				secondSelectedV = null;
				repaint();
			} else if (thirdSelectedV != null) {
				thirdSelectedV = null;
				repaint();
			}

			Origrammer.diagram.steps.get(Globals.currentStep).unselectAll();
			return;
		}

		if (Globals.toolbarMode == Constants.ToolbarMode.NONE) {
			return;
		}

		//get mouse click coordinates
		Point2D.Double clickPoint = new Point2D.Double();
		try {
			affineTransform.inverseTransform(e.getPoint(), clickPoint);
		} catch (Exception ex) {
			return;
		}

		if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE) {
			if (Globals.lineEditMode == Constants.LineInputMode.INPUT_LINE) {
				//create line on the two picked vertices (or on custom point on a OriLine while pressing CTRL)
				Vector2d v = pickVertex(clickPoint);
				if (v == null) {
					if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK) {
						OriLine l = pickLine(clickPoint);
						if (l != null) {
							v = new Vector2d();
							Vector2d cp = new Vector2d(clickPoint.x, clickPoint.y);
							GeometryUtil.DistancePointToSegment(cp, l.getP0(), l.getP1(), v);
						}
					}
				}
				if (v != null) {
					if (firstSelectedV == null) {
						firstSelectedV = v;
					} else {
						OriLine line = new OriLine(firstSelectedV, v, Globals.inputLineType);
						if (Globals.inputLineType == OriLine.TYPE_CREASE) {
							if (Origrammer.mainFrame.uiTopPanel.startCreaseCB.isSelected()) {
							} else {
								line.setStartTransl(false);
							}
							if (Origrammer.mainFrame.uiTopPanel.endCreaseCB.isSelected()) {
								line.setEndTransl(true);
							} else {
								line.setEndTransl(false);
							}
						}
						Origrammer.diagram.steps.get(Globals.currentStep).addLine(line);

						firstSelectedV = null;
					}
				}
			}  else if (Globals.lineEditMode == Constants.LineInputMode.TRIANGLE_INSECTOR) {
				createTriangleInsector(clickPoint);
			}
		} else if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_VERTEX) {
			if (Globals.vertexInputMode == Constants.VertexInputMode.ABSOLUTE) {
				createVertexAbsolutePos(clickPoint);
			} else if (Globals.vertexInputMode == Constants.VertexInputMode.FRACTION_OF_LINE) {
				createVertexFractionOfLine(clickPoint);
			}
		} else if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_SYMBOL) {
			if (Globals.inputSymbolMode == Constants.InputSymbolMode.LEADER ||
					Globals.inputSymbolMode == Constants.InputSymbolMode.REPETITION_BOX) {
				createLeaderBox(clickPoint);
			}else if (Globals.inputSymbolMode == Constants.InputSymbolMode.EQUAL_DIST) {
				createEqualDistSymbol(clickPoint);
			} else if (Globals.inputSymbolMode == Constants.InputSymbolMode.EQUAL_ANGL) {
				createEqualAnglSymbol(clickPoint);
			} else if (Globals.inputSymbolMode == Constants.InputSymbolMode.CRIMPING_PLEATING) {
				createPleatCrimpSymbol(clickPoint);
			} else if (Globals.inputSymbolMode == Constants.InputSymbolMode.SINKS) {
				createClosedSinkDot(clickPoint);
			}
		} else if (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL) {
			selectOnClickPoint(clickPoint);
		} else if (Globals.toolbarMode == Constants.ToolbarMode.MEASURE_TOOL) {
			if (Globals.measureMode == Constants.MeasureMode.MEASURE_LENGTH) {
				measureLength(clickPoint);
			} else if (Globals.measureMode == Constants.MeasureMode.MEASURE_ANGLE) {
				measureAngle(clickPoint);
			} 
		} else if (Globals.toolbarMode == Constants.ToolbarMode.FILL_TOOL) {
			createFilledFace(clickPoint);
		}
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0 &&
				(e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK) {
			//scale diagram with CTRL + dragging mouse
			double moved = e.getX() - preMousePoint.getX() + e.getY() - preMousePoint.getY();
			Globals.SCALE += moved / 150.0;
			if (Globals.SCALE < 0.01) {
				Globals.SCALE = 0.01;
			}
			preMousePoint = e.getPoint();
			updateAffineTransform(g2d);
			repaint();
		} else if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
			//translate diagram with right click + dragging mouse
			transX += (double) (e.getX() - preMousePoint.getX()) / Globals.SCALE;
			transY += (double) (e.getY() - preMousePoint.getY()) / Globals.SCALE;
			preMousePoint = e.getPoint();
			updateAffineTransform(g2d);
			repaint();
		} else if ((Globals.toolbarMode == Constants.ToolbarMode.INPUT_ARROW) 
				&& (e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
			//ADD new OriArrow on MousePoint
			isReleased = false;
			int type = Globals.inputArrowType;
			double xPos = (double) (preMousePoint.getX()-400) / Globals.SCALE;
			double yPos = (double) (preMousePoint.getY()-400) / Globals.SCALE;
			preMousePoint = e.getPoint();
			tmpOriArrow = new OriArrow(new Vector2d(xPos, yPos), type);

			//get correct Arrow Image
			BufferedImage img = getBufImgByTypeAndRot(tmpOriArrow);

			//get updated label width & height
			int newArrowLabelWidth = (int) Math.round(img.getWidth()/2*tmpOriArrow.getAdjustedScale());
			int newArrowLabelHeight = (int) Math.round(img.getHeight()/2*tmpOriArrow.getAdjustedScale());

			//set width & height of arrowLabel and OriArrow
			tmpArrowLabel = new JLabel();
			tmpArrowLabel.setSize(newArrowLabelWidth, newArrowLabelHeight);
			tmpOriArrow.setWidth(newArrowLabelWidth);
			tmpOriArrow.setHeight(newArrowLabelHeight);
			//scale the Image and set it as ImageIcon of the arrowLabel
			Image scaledImg = img.getScaledInstance(tmpArrowLabel.getWidth(), tmpArrowLabel.getHeight(), Image.SCALE_SMOOTH);
			ImageIcon arrowImageIcon = new ImageIcon(scaledImg);
			tmpArrowLabel.setIcon(arrowImageIcon);
			//set bounds of tmpArowLabel
			tmpOriArrow.setLabel(tmpArrowLabel);
			tmpOriArrow.getLabel().setBounds((int) tmpOriArrow.getPosition().x, 
					(int) tmpOriArrow.getPosition().y, 
					(int) tmpOriArrow.getWidth(),
					(int) tmpOriArrow.getHeight());
			repaint();
		} else if ((Globals.toolbarMode == Constants.ToolbarMode.INPUT_SYMBOL 
				&& (Globals.inputSymbolMode == Constants.InputSymbolMode.ROTATIONS //TODO: shouldn't it be OR ||?
				|| Globals.inputSymbolMode == Constants.InputSymbolMode.NEXT_VIEW
				|| Globals.inputSymbolMode == Constants.InputSymbolMode.HOLD_HERE
				|| Globals.inputSymbolMode == Constants.InputSymbolMode.HOLD_HERE_AND_PULL))
				&& (e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
			//ADD new OriSymbol on MousePoint
			isReleased = false;
			int type = OriPicSymbol.TYPE_NONE;

			if (Globals.inputSymbolMode == Constants.InputSymbolMode.ROTATIONS) {
				type = OriPicSymbol.TYPE_ROTATION;
			} else if (Globals.inputSymbolMode == Constants.InputSymbolMode.HOLD_HERE) {
				type = OriPicSymbol.TYPE_HOLD;
			} else if (Globals.inputSymbolMode == Constants.InputSymbolMode.HOLD_HERE_AND_PULL) {
				type = OriPicSymbol.TYPE_HOLD_AND_PULL;
			} else if (Globals.inputSymbolMode == Constants.InputSymbolMode.NEXT_VIEW) {
				type = OriPicSymbol.TYPE_NEXT_VIEW_HERE;
			}
			double xPos = (double) (preMousePoint.getX()-400) / Globals.SCALE;
			double yPos = (double) (preMousePoint.getY()-400) / Globals.SCALE;
			Vector2d pos = new Vector2d(xPos, yPos);
			preMousePoint = e.getPoint();
			tmpOriSymbol = new OriPicSymbol(pos, type);

			//get correct Symbol Image
			BufferedImage img = getBufImgByTypeAndRot(tmpOriSymbol);

			//get updated label width & height
			int newSymbolLabelWidth = (int) Math.round(img.getWidth()/2*tmpOriSymbol.getScale());
			int newSymbolLabelHeight = (int) Math.round(img.getHeight()/2*tmpOriSymbol.getScale());

			//set width & height of symbolLabel and OriSymbol
			tmpSymbolLabel = new JLabel();
			tmpSymbolLabel.setSize(newSymbolLabelWidth, newSymbolLabelHeight);
			tmpOriSymbol.setWidth(newSymbolLabelWidth);
			tmpOriSymbol.setHeight(newSymbolLabelHeight);
			//scale the Image and set it as ImageIcon of the symbolLabel
			Image scaledImg = img.getScaledInstance(tmpSymbolLabel.getWidth(), tmpSymbolLabel.getHeight(), Image.SCALE_SMOOTH);
			ImageIcon symbolImageIcon = new ImageIcon(scaledImg);
			tmpSymbolLabel.setIcon(symbolImageIcon);
			//set bounds of tmpArowLabel
			tmpOriSymbol.setLabel(tmpSymbolLabel);
			tmpOriSymbol.getLabel().setBounds((int) tmpOriSymbol.getPosition().x, 
					(int) tmpOriSymbol.getPosition().y, 
					(int) tmpOriSymbol.getWidth(),
					(int) tmpOriSymbol.getHeight());
			repaint();

		} else if (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL 
				|| (Globals.toolbarMode == Constants.ToolbarMode.INPUT_SYMBOL
				&& Globals.inputSymbolMode == Constants.InputSymbolMode.X_RAY_CIRCLE)) {
			//moving symbols by dragging (all selected Objects are moved)
			currentMouseDraggingPoint = e.getPoint();
			Point2D.Double affineMouseDraggingPoint = new Point2D.Double();
			try {
				affineTransform.inverseTransform(currentMouseDraggingPoint, affineMouseDraggingPoint);
			} catch (NoninvertibleTransformException e1) {
				e1.printStackTrace();
			}

			OriArrow pickedArrow = pickArrow(affineMouseDraggingPoint);
			if (pickArrow(currentMousePointLogic) != null && isPressedOverSymbol || isMovingSymbols) {
				isMovingSymbols = true;
				if (pickedArrow != null) {

					double xTrans = (e.getX() - preMousePoint.getX()) / Globals.SCALE;
					double yTrans = (e.getY() - preMousePoint.getY()) / Globals.SCALE;
					preMousePoint = e.getPoint();
					for (OriArrow arrow : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
						//if selected, move to new position
						if (arrow.isSelected()) {
							int newX = (int) Math.round(arrow.getPosition().x + xTrans);
							int newY = (int) Math.round(arrow.getPosition().y + yTrans);
							arrow.setPosition(new Vector2d(newX, newY));
							arrow.getLabel().setBounds(newX, newY, 
									(int) Math.round(arrow.getWidth() * arrow.getAdjustedScale()), 
									(int) Math.round(arrow.getHeight() * arrow.getAdjustedScale()));
						}
					}
				}
			}

			OriLeaderBox pickedLeader = pickLeader(affineMouseDraggingPoint);
			if (pickLeader(currentMousePointLogic) != null && isPressedOverSymbol || isMovingSymbols) {
				isMovingSymbols = true;

				if (pickedLeader != null) {

					double xTrans = (e.getX() - preMousePoint.getX()) / Globals.SCALE;
					double yTrans = (e.getY() - preMousePoint.getY()) / Globals.SCALE;
					preMousePoint = e.getPoint();

					for (OriLeaderBox leader : Origrammer.diagram.steps.get(Globals.currentStep).leaderBoxSymbols) {
						//if selected, move to new position
						if (leader.isSelected()) {
							leader.moveBy(xTrans, yTrans);
						}
					}
				}
			}

			OriPicSymbol pickedSymbol = pickPicSymbol(affineMouseDraggingPoint);
			if (pickPicSymbol(currentMousePointLogic) != null && isPressedOverSymbol || isMovingSymbols) {
				isMovingSymbols = true;
				if (pickedSymbol != null) {

					double xTrans = (e.getX() - preMousePoint.getX()) / Globals.SCALE;
					double yTrans = (e.getY() - preMousePoint.getY()) / Globals.SCALE;
					preMousePoint = e.getPoint();
					for (OriPicSymbol symbol : Origrammer.diagram.steps.get(Globals.currentStep).picSymbols) {
						//if selected, move to new position
						if (symbol.isSelected()) {
							int newX = (int) Math.round(symbol.getPosition().x + xTrans);
							int newY = (int) Math.round(symbol.getPosition().y + yTrans);
							
							Vector2d newPos = new Vector2d(newX, newY);
							symbol.setPosition(newPos);
							symbol.getLabel().setBounds(newX, newY, 
									(int) Math.round(symbol.getWidth() * symbol.getScale()), 
									(int) Math.round(symbol.getHeight() * symbol.getScale()));
						}
					}
				}
			}

			OriGeomSymbol pickedGeomSymbol = pickGeomSymbol(affineMouseDraggingPoint);
			if (pickGeomSymbol(currentMousePointLogic) != null && isPressedOverSymbol || isMovingSymbols) {
				isMovingSymbols = true;
				if (pickedGeomSymbol != null) {

					double xTrans = (e.getX() - preMousePoint.getX()) / Globals.SCALE;
					double yTrans = (e.getY() - preMousePoint.getY()) / Globals.SCALE;
					preMousePoint = e.getPoint();
					for (OriGeomSymbol s : Origrammer.diagram.steps.get(Globals.currentStep).geomSymbols) {
						//if selected, move to new position
						if (s.isSelected()) {
							int newX = (int) Math.round(s.getPosition().x + xTrans);
							int newY = (int) Math.round(s.getPosition().y + yTrans);
							s.setPosition(new Vector2d(newX, newY));
						}
					}
				}
			}
			
			OriPleatCrimpSymbol pickedPleatSymbol = pickPleatSymbol(affineMouseDraggingPoint);
			if (pickPleatSymbol(currentMousePointLogic) != null && isPressedOverSymbol || isMovingSymbols) {
				isMovingSymbols = true;
				if (pickedPleatSymbol != null) {

					double xTrans = (e.getX() - preMousePoint.getX()) / Globals.SCALE;
					double yTrans = (e.getY() - preMousePoint.getY()) / Globals.SCALE;
					preMousePoint = e.getPoint();
					for (OriPleatCrimpSymbol p : Origrammer.diagram.steps.get(Globals.currentStep).pleatCrimpSymbols) {
						//if selected, move to new position
						if (p.isSelected()) {
							int newX = (int) Math.round(p.getPosition().x + xTrans);
							int newY = (int) Math.round(p.getPosition().y + yTrans);
							p.setPosition(new Vector2d(newX, newY));
						}
					}
				}
			}

			repaint();
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		try {
			affineTransform.inverseTransform(e.getPoint(), currentMousePointLogic);
		} catch (Exception ex) {
			return;
		}

		if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE) {
			if (Globals.lineEditMode == Constants.LineInputMode.INPUT_LINE
					|| Globals.lineEditMode == Constants.LineInputMode.TRIANGLE_INSECTOR
					|| Globals.toolbarMode == Constants.ToolbarMode.FILL_TOOL) {
				Vector2d firstV = selectedCandidateV;
				selectedCandidateV = this.pickVertex(currentMousePointLogic);

				if (selectedCandidateV == null) {
					if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK) {
						OriLine l = pickLine(currentMousePointLogic);
						if (l != null) {
							selectedCandidateV = new Vector2d();
							Vector2d cp = new Vector2d(currentMousePointLogic.x, currentMousePointLogic.y);
							GeometryUtil.DistancePointToSegment(cp,  l.getP0(), l.getP1(), selectedCandidateV);
						}
					}
				}
				if (selectedCandidateV != firstV || firstSelectedV != null) {
					repaint();
				}
			}
		} else if (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL) {
			OriLine preLine = selectedCandidateL;
			selectedCandidateL = pickLine(currentMousePointLogic);
			if (preLine != selectedCandidateL) {
				repaint();
			}

			Vector2d preVertex = selectedCandidateV;
			selectedCandidateV = pickVertex(currentMousePointLogic);
			if (preVertex != selectedCandidateV) {
				repaint();
			}
			

			OriArrow preArrow = selectedCandidateA;
			selectedCandidateA = pickArrow(currentMousePointLogic);
			if (preArrow != selectedCandidateA) {
				repaint();
			}

			OriFace preFace = selectedCandidateF;
			selectedCandidateF = pickFace(currentMousePointLogic);
			if (preFace != selectedCandidateF) {
				repaint();
			}

			OriLeaderBox preLeader = selectedCandidateLeader;
			selectedCandidateLeader = pickLeader(currentMousePointLogic);
			if (preLeader != selectedCandidateLeader) {
				repaint();
			}

			OriPicSymbol prePicSymbol = selectedCandidatePS;
			selectedCandidatePS = pickPicSymbol(currentMousePointLogic);
			if (prePicSymbol != selectedCandidatePS) {
				repaint();
			}

			OriGeomSymbol preGeomSymbol = selectedCandidateGS;
			selectedCandidateGS = pickGeomSymbol(currentMousePointLogic);
			if (preGeomSymbol != selectedCandidateGS) {
				repaint();
			}

			OriEqualDistSymbol preEqualDistSymbol = selectedCandidateEDS;
			selectedCandidateEDS = pickEqualDistSymbol(currentMousePointLogic);
			if (preEqualDistSymbol != selectedCandidateEDS) {
				repaint();
			}
			
			OriEqualAnglSymbol preEqualAnglSymbol = selectedCandidateEAS;
			selectedCandidateEAS = pickEqualAnglSymbol(currentMousePointLogic);
			if (preEqualAnglSymbol != selectedCandidateEAS) {
				repaint();
			}
			
			OriPleatCrimpSymbol prePleatSymbol = selectedCandidatePleat;
			selectedCandidatePleat = pickPleatSymbol(currentMousePointLogic);
			if (prePleatSymbol != selectedCandidatePleat) {
				repaint();
			}

		} else if (Globals.toolbarMode == Constants.ToolbarMode.MEASURE_TOOL) {
			Vector2d firstV = selectedCandidateV;
			selectedCandidateV = this.pickVertex(currentMousePointLogic);
			if (selectedCandidateV != firstV || firstSelectedV != null) {
				firstSelectedL = null;
				selectedCandidateL = null;
				repaint();
			} else {
				OriLine preLine = selectedCandidateL;
				selectedCandidateL = pickLine(currentMousePointLogic);
				if (preLine != selectedCandidateL) {
					repaint();
				}
			}
		} else if (Globals.toolbarMode == Constants.ToolbarMode.FILL_TOOL ||
				(Globals.toolbarMode == Constants.ToolbarMode.INPUT_SYMBOL 
				&& (Globals.inputSymbolMode == Constants.InputSymbolMode.EQUAL_DIST 
					|| Globals.inputSymbolMode == Constants.InputSymbolMode.EQUAL_ANGL))) {
			selectedCandidateV = this.pickVertex(currentMousePointLogic);
			repaint();
		} else if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_VERTEX 
				&& Globals.vertexInputMode == Constants.VertexInputMode.FRACTION_OF_LINE) {
			selectedCandidateL = this.pickLine(currentMousePointLogic);
			repaint();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		preMousePoint = e.getPoint();
		
		//mark OriArrow as selected if in SELECTION_TOOL mode and you have pressed left mouse button while over OriArrow -->
		//only needed for freely movable objects
		if (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL) {
			OriArrow pickedArrow = pickArrow(currentMousePointLogic);
			if (pickedArrow != null) {
				pickedArrow.setSelected(true);
				isPressedOverSymbol = true;
				repaint();
			}
			
			OriLeaderBox pickedLeader = pickLeader(currentMousePointLogic);
			if (pickedLeader != null) {
				pickedLeader.setSelected(true);
				isPressedOverSymbol = true;
				repaint();
			}
			
			OriPicSymbol pickedPicSymbol = pickPicSymbol(currentMousePointLogic);
			if (pickedPicSymbol != null) {
				pickedPicSymbol.setSelected(true);
				isPressedOverSymbol = true;
				repaint();
			}
			
			OriGeomSymbol pickedGeomSymbol = pickGeomSymbol(currentMousePointLogic);
			if (pickedGeomSymbol != null) {
				pickedGeomSymbol.setSelected(true);
				isPressedOverSymbol = true;
				repaint();
			}
			
			OriPleatCrimpSymbol pickedPleatSymbol = pickPleatSymbol(currentMousePointLogic);
			if (pickedPleatSymbol != null) {
				pickedPleatSymbol.setSelected(true);
				isPressedOverSymbol = true;
				repaint();
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0 &&
				Globals.toolbarMode == Constants.ToolbarMode.INPUT_ARROW) {
			isReleased = true;
			int type = Globals.inputArrowType;
			double xPos = 0;
			double yPos = 0;
			xPos += (double) (preMousePoint.getX()-400) / Globals.SCALE;
			yPos += (double) (preMousePoint.getY()-400) / Globals.SCALE;
			Origrammer.diagram.steps.get(Globals.currentStep).addArrow(new OriArrow(new Vector2d(xPos, yPos), type));
			//get last added OriArrow
			OriArrow arrow = Origrammer.diagram.steps.get(Globals.currentStep).arrows.get(Origrammer.diagram.steps.get(Globals.currentStep).arrows.size()-1);
			//get correct Arrow Image
			BufferedImage img = null;
			img = getBufImgByTypeAndRot(arrow);
			//get updated label width & height
			int newArrowLabelWidth = (int) Math.round(img.getWidth()/2*arrow.getAdjustedScale());
			int newArrowLabelHeight = (int) Math.round(img.getHeight()/2*arrow.getAdjustedScale());
			//set width & height of arrowLabel and OriArrow
			JLabel arrowLabel = new JLabel();
			arrowLabel.setSize(newArrowLabelWidth, newArrowLabelHeight);
			arrow.setWidth(newArrowLabelWidth);
			arrow.setHeight(newArrowLabelHeight);
			//scale the Image and set it as ImageIcon of the arrowLabel
			Image scaledImg = img.getScaledInstance(arrowLabel.getWidth(), arrowLabel.getHeight(), Image.SCALE_SMOOTH);
			ImageIcon arrowImageIcon = new ImageIcon(scaledImg);
			arrowLabel.setIcon(arrowImageIcon);
			//set bounds of arrowLabel
			arrow.setLabel(arrowLabel);
			arrow.getLabel().setBounds((int)arrow.getPosition().x, 
					(int)arrow.getPosition().y, 
					(int) Math.round(arrow.getWidth()*arrow.getAdjustedScale()), 
					(int) Math.round(arrow.getHeight()*arrow.getAdjustedScale()));
			repaint();		
		} else if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0 
				&& (Globals.inputSymbolMode == Constants.InputSymbolMode.ROTATIONS
				|| Globals.inputSymbolMode == Constants.InputSymbolMode.NEXT_VIEW
				|| Globals.inputSymbolMode == Constants.InputSymbolMode.HOLD_HERE
				|| Globals.inputSymbolMode == Constants.InputSymbolMode.HOLD_HERE_AND_PULL)) {
			
			isReleased = true;
			int type = OriPicSymbol.TYPE_NONE;

			if (Globals.inputSymbolMode == Constants.InputSymbolMode.ROTATIONS) {
				type = OriPicSymbol.TYPE_ROTATION;
			} else if (Globals.inputSymbolMode == Constants.InputSymbolMode.HOLD_HERE) {
				type = OriPicSymbol.TYPE_HOLD;
			} else if (Globals.inputSymbolMode == Constants.InputSymbolMode.HOLD_HERE_AND_PULL) {
				type = OriPicSymbol.TYPE_HOLD_AND_PULL;
			} else if (Globals.inputSymbolMode == Constants.InputSymbolMode.NEXT_VIEW) {
				type = OriPicSymbol.TYPE_NEXT_VIEW_HERE;
			}
			double xPos = 0;
			double yPos = 0;
			xPos += (double) (preMousePoint.getX()-400) / Globals.SCALE;
			yPos += (double) (preMousePoint.getY()-400) / Globals.SCALE;
			Vector2d pos = new Vector2d(xPos, yPos);
			Origrammer.diagram.steps.get(Globals.currentStep).addPicSymbol(new OriPicSymbol(pos, type));
			//get last added OriSymbol
			OriPicSymbol symbol = Origrammer.diagram.steps.get(Globals.currentStep).picSymbols.get(Origrammer.diagram.steps.get(Globals.currentStep).picSymbols.size()-1);
			//get correct Symbol Image
			BufferedImage img = null;
			img = getBufImgByTypeAndRot(symbol);
			//get updated label width & height
			int newSymbolLabelWidth = (int) Math.round(img.getWidth()/2*symbol.getScale());
			int newSymbolLabelHeight = (int) Math.round(img.getHeight()/2*symbol.getScale());
			//set width & height of symbolLabel and OriSymbol
			JLabel symbolLabel = new JLabel();
			symbolLabel.setSize(newSymbolLabelWidth, newSymbolLabelHeight);
			symbol.setWidth(newSymbolLabelWidth);
			symbol.setHeight(newSymbolLabelHeight);
			//scale the Image and set it as ImageIcon of the symbolLabel
			Image scaledImg = img.getScaledInstance(symbolLabel.getWidth(), symbolLabel.getHeight(), Image.SCALE_SMOOTH);
			ImageIcon symbolImageIcon = new ImageIcon(scaledImg);
			symbolLabel.setIcon(symbolImageIcon);
			//set bounds of symbolLabel
			symbol.setLabel(symbolLabel);
			symbol.getLabel().setBounds((int)symbol.getPosition().x, 
					(int)symbol.getPosition().y, 
					(int) Math.round(symbol.getWidth()*symbol.getScale()), 
					(int) Math.round(symbol.getHeight()*symbol.getScale()));
			repaint();		
		} else if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_SYMBOL
				&& Globals.inputSymbolMode == Constants.InputSymbolMode.X_RAY_CIRCLE
				&& currentMouseDraggingPoint != null) {
			//X-RAY CIRCLE
			Point2D.Double sp = new Point2D.Double();
			Point2D.Double ep = new Point2D.Double();
			try {
				affineTransform.inverseTransform(preMousePoint, sp);
				affineTransform.inverseTransform(currentMouseDraggingPoint, ep);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

//			Vector2d v1  = new Vector2d(sp.x, sp.y);
//			Vector2d v2 = new Vector2d(ep.x, ep.y);
			//double diameter = GeometryUtil.Distance(v1, v2);
			double width = ep.x - sp.x;
			double height = ep.y - sp.y;
			OriGeomSymbol tmpGeomS = new OriGeomSymbol();
			if (width > height) {
				tmpGeomS = new OriGeomSymbol(new Vector2d(sp.x, sp.y), width);
			} else if (height > width) {
				tmpGeomS = new OriGeomSymbol(new Vector2d(sp.x, sp.y), height);
			}

			Origrammer.diagram.steps.get(Globals.currentStep).addGeomSymbol(tmpGeomS);

			
		} else if (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL
				&& currentMouseDraggingPoint != null) {
			//Rectangular Selection
			Point2D.Double sp = new Point2D.Double();
			Point2D.Double ep = new Point2D.Double();
			try {
				affineTransform.inverseTransform(preMousePoint, sp);
				affineTransform.inverseTransform(currentMouseDraggingPoint, ep);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			int minX = (int) Math.round(Math.min(sp.x,  ep.x));
			int minY = (int) Math.round(Math.min(sp.y, ep.y));
			int maxX = (int) Math.round(Math.max(sp.x, ep.x));
			int maxY = (int) Math.round(Math.max(sp.y, ep.y));
			Rectangle selectRect = new Rectangle(minX, minY, maxX-minX, maxY-minY);
			
		
			//Check if there is a line in the selection rectangle
			for (OriLine l : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
				Line2D tmpL = new Line2D.Double(l.getP0().x, l.getP0().y, l.getP1().x, l.getP1().y);
				if (tmpL.intersects(selectRect)) {
					l.setSelected(true);
				} else {
					l.setSelected(false);
				}
			}
			
			//Check if there is an arrow in the selection rectangle
			for (OriArrow a : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
				Rectangle tmpR2 = new Rectangle((int) Math.round(a.getPosition().x), 
						(int) Math.round(a.getPosition().y), a.getWidth(), a.getHeight());
				if (tmpR2.intersects(selectRect)) {
					a.setSelected(true);
				} else {
					a.setSelected(false);
				}
			}
			
			//Check if there is a OriFace in the selection rectangle
			for (OriFace f : Origrammer.diagram.steps.get(Globals.currentStep).filledFaces) {
				if (f.path.intersects(selectRect)) {
					f.setSelected(true);
				} else {
					f.setSelected(false);
				}
			}
			
			//Check if there is a symbol in the selection rectangle
			for (OriPicSymbol s : Origrammer.diagram.steps.get(Globals.currentStep).picSymbols) {
				Rectangle tmpR2 = new Rectangle((int) Math.round(s.getPosition().x), 
						(int) Math.round(s.getPosition().y), s.getWidth(), s.getHeight());
				if (tmpR2.intersects(selectRect)) {
					s.setSelected(true);
				} else {
					s.setSelected(false);
				}
			}
			
			//Check if there is a symbol in the selection rectangle
			for (OriGeomSymbol gs : Origrammer.diagram.steps.get(Globals.currentStep).geomSymbols) {
				Shape tmpC2 = new Ellipse2D.Double(gs.getPosition().x, gs.getPosition().y, gs.getRadius(), gs.getRadius());
				if (tmpC2.intersects(selectRect)) {
					gs.setSelected(true);
				} else {
					gs.setSelected(false);
				}
			}
			
			//Check if there is a leader in the selection rectangle
			for (OriLeaderBox leader : Origrammer.diagram.steps.get(Globals.currentStep).leaderBoxSymbols) {

				Rectangle tmpR3 = leader.getLabel().getBounds();
				Line2D tmpL2 = new Line2D.Double(leader.line.getP0().x, leader.line.getP0().y, 
												leader.line.getP1().x, leader.line.getP1().y);

				if (tmpR3.intersects(selectRect)) {
					leader.setSelected(true);
				} else if (tmpL2.intersects(selectRect)) {
					leader.setSelected(true);
				} else {
					leader.setSelected(false);
				}
			}
			
			//Check if there is a equalDistSymbol in the selection rectangle
			for (OriEqualDistSymbol eds : Origrammer.diagram.steps.get(Globals.currentStep).equalDistSymbols) {

				Vector2d nv = GeometryUtil.getNormalVector(GeometryUtil.getUnitVector(eds.getP0(), eds.getP1()));
				double p0x = eds.getP0().x + 15*nv.x;
				double p0y = eds.getP0().y + 15*nv.y;
								
				double p1x = eds.getP0().x - 15*nv.x;
				double p1y = eds.getP0().y - 15*nv.y;
				
				double p2x = eds.getP1().x - 15*nv.x;
				double p2y = eds.getP1().y - 15*nv.y;
				
				double p3x = eds.getP1().x + 15*nv.x;
				double p3y = eds.getP1().y + 15*nv.y;
				
				Path2D.Double p = new Path2D.Double();
				p.moveTo(p0x, p0y);
				p.lineTo(p1x, p1y);
				p.lineTo(p2x, p2y);
				p.lineTo(p3x, p3y);
				p.closePath();

				if (p.intersects(selectRect)) {
					eds.setSelected(true);
				} else {
					eds.setSelected(false);
				}
			}
			
			//Check if there is a equalAnglSymbol in the selection rectangle
			for (OriEqualAnglSymbol eas : Origrammer.diagram.steps.get(Globals.currentStep).equalAnglSymbols) {
				ArrayList<Shape> shapes = eas.getShapesForDrawing();
				
				for(Shape s : shapes) {
					if(s.intersects(selectRect)) {
						eas.setSelected(true);
						break;
					} else {
						eas.setSelected(false);
					}
				}
			}
			
			//Check if there is a equalAnglSymbol in the selection rectangle
			for (OriPleatCrimpSymbol p : Origrammer.diagram.steps.get(Globals.currentStep).pleatCrimpSymbols) {
				ArrayList<Shape> shapes = p.getShapesForDrawing();
				
				for(Shape s : shapes) {
					if(s.intersects(selectRect)) {
						p.setSelected(true);
						break;
					} else {
						p.setSelected(false);
					}
				}
			}
			
			Origrammer.mainFrame.uiTopPanel.modeChanged();
		}
		currentMouseDraggingPoint = null;
		isMovingSymbols = false;
		repaint();
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		if (getWidth() <= 0 || getHeight() <= 0) {
			return;
		}
		preSize = getSize();
		
		transX = transX - preSize.width * 0.5 + getWidth() * 0.5;
		transY = transY - preSize.height * 0.5 + getHeight() * 0.5;
		
		//updateAffineTransform(g2d);
		repaint();
		
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		//zoom on diagram with mouseWheel
		double scale_ = (100.0 - e.getWheelRotation() * 5) / 100.0;
		Globals.SCALE *= scale_;
		
		
//		double newScale = Double.parseDouble(Origrammer.mainFrame.uiSidePanel.scalingCustomTF.set());
//		if (newScale < 1000 && newScale > 0) {
//			Globals.SCALE = newScale/100;
//			screen.repaint();
//		} else {
//			scalingCustomTF.setValue(Globals.SCALE*100);
//		}
		
		
		Origrammer.mainFrame.uiSidePanel.scalingCustomTF.setValue(Globals.SCALE*100);
		
		
		
		
		//updateAffineTransform(g2d);
		repaint();
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {		
	}
	
	@Override
	public void componentShown(ComponentEvent arg0) {		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {		
	}
	
	@Override
	public void componentHidden(ComponentEvent arg0) {		
	}
	@Override
	public void componentMoved(ComponentEvent arg0) {		
	}
}