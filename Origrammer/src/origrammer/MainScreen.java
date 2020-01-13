package origrammer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
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
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
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

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;

import jdk.nashorn.internal.codegen.CompilerConstants;
import origrammer.geometry.*;

public class MainScreen extends JPanel 
		implements MouseListener, MouseMotionListener, MouseWheelListener, ActionListener, ComponentListener {
	
	
	private BufferedImage bufferImage;
	private Graphics2D bufferGraphic;
	private Point2D preMousePoint;
	private Point2D currentMouseDraggingPoint = null;
	private Point2D.Double currentMousePointLogic = new Point2D.Double();
	private double scale;
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
	private OriArrow selectedCandidateA = null;
	private ArrayList<Vector2d> tmpOutline = new ArrayList<>();
	
	private boolean dispGrid = true;
	//Affine transformation info
	private Dimension preSize;
	private AffineTransform affineTransform = new AffineTransform();
	private ArrayList<Vector2d> crossPoints = new ArrayList<>();
	
	private Graphics2D g2d;
	
	public ArrayList<JLabel> arrowLabelList = new ArrayList<>();
	private JLabel tmpArrowLabel = new JLabel();
	private OriArrow tmpOriArrow = new OriArrow();
	//private JLabel arrowLabel = new JLabel();
	private boolean isReleased = false;
	private boolean isMovingArrows = false;
	private boolean isPressedOverArrow = false;
	int tmpArrowWidth;
	int tmpArrowHeight;


	
	public MainScreen() {
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addComponentListener(this);
		
		scale = 1.5;
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
        updateAffineTransform(g2d);

        if (dispGrid) {
        	drawGrid(g2d);
        }
        
        
        
    	File file = new File("./images/test1.svg");
//    	SVGUniverse svgUniverse = new SVGUniverse();	
//    	
//    	SVGDiagram diagram = null;
//		try {
//			diagram = svgUniverse.getDiagram(svgUniverse.loadSVG(file.toURI().toURL()));
//		} catch (MalformedURLException e1) {
//			e1.printStackTrace();
//		}
//    	
//    	try {
//			diagram.render(g2d);
    	//		} catch (SVGException e1) {
    	//			e1.printStackTrace();
    	//		}

//    	BufferedImage testbimg = null;
//    	try {
//    		testbimg = rasterize(file);
//    	} catch (IOException e1) {
//    		// TODO Auto-generated catch block
//    		e1.printStackTrace();
//    	}
//
//    	JLabel label = new JLabel();
//
//    	Image testdimg = testbimg.getScaledInstance(400, 400, Image.SCALE_SMOOTH);
//    	ImageIcon testImageIcon = new ImageIcon(testdimg);
//
//
//    	label.setIcon(testImageIcon);
//    	add(label);



    	//show all FILLED FACES
    	if (Globals.dispFilledFaces) {
    		for (OriFace f : Origrammer.diagram.steps.get(Globals.currentStep).filledFaces) {
    			if (f.isSelected()) {
    				g2d.draw(f.path);
    				g2d.setPaint(new Color(200, 100, 100));
    				g2d.fill(f.path);
    			} else {
    				g2d.draw(f.path);
    				if (f.isFaceUp()) {
        				g2d.setPaint(Origrammer.diagram.getFaceUpColor());
    				} else {
        				g2d.setPaint(Origrammer.diagram.getFaceDownColor());
    				}
    				g2d.fill(f.path);
    			}
    		}
    	}

       g2d.setStroke(Config.STROKE_VALLEY);
       g2d.setColor(Color.BLACK);
       for(OriLine line : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
    	   //render lines according to their LINE_TYPE
    	   switch(line.type) {
    	   		case OriLine.TYPE_VALLEY:
    	   			g2d.setColor(Config.LINE_COLOR_VALLEY);
    	   			g2d.setStroke(Config.STROKE_VALLEY);
    	   			break;
    	   		case OriLine.TYPE_MOUNTAIN:
    	   			g2d.setColor(Config.LINE_COLOR_MOUNTAIN);
    	   			g2d.setStroke(Config.STROKE_MOUNTAIN);
    	   			break;
    	   		case OriLine.TYPE_XRAY:
    	   			g2d.setColor(Config.LINE_COLOR_XRAY);
    	   			g2d.setStroke(Config.STROKE_XRAY);
    	   			break; 
    	   		case OriLine.TYPE_EDGE:
    	   			g2d.setColor(Config.LINE_COLOR_EDGE);
    	   			g2d.setStroke(Config.STROKE_EDGE);
    	   			break; 
    	   }
    	   
    	   //if line is selected during INPUT_LINE/SELECTION_TOOL mode, render GREEN
    	   if ((Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE
    			   && line.isSelected)
    			   || (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL
    			   && line.isSelected)) {
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
    	   
    	   g2d.draw(new Line2D.Double(line.p0.x, line.p0.y, line.p1.x, line.p1.y));
       }
    
       tmpArrowLabel.setBorder(new EtchedBorder(BevelBorder.RAISED, Color.RED, getBackground().brighter()));
       add(tmpArrowLabel);

       //render all arrows
       for (OriArrow arrow : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {    	   
    	   
    	   BufferedImage bimg = getBufImgByTypeAndRot(arrow);

    	   int newArrowLabelWidth = (int) Math.round(bimg.getWidth()/2*arrow.getScale());
    	   int newArrowLabelHeight = (int) Math.round(bimg.getHeight()/2*arrow.getScale());
    	   arrow.setWidth(newArrowLabelWidth);
    	   arrow.setHeight(newArrowLabelHeight);
    	   arrow.getArrowLabel().setSize(arrow.getWidth(), arrow.getHeight());
    	   
    	   Image dimg = bimg.getScaledInstance(arrow.getArrowLabel().getWidth(), arrow.getArrowLabel().getHeight(), Image.SCALE_SMOOTH);
    	   ImageIcon arrowImageIcon = new ImageIcon(dimg);

//    	   arrow.setWidth(newArrowLabelWidth);
//    	   arrow.setHeight(newArrowLabelHeight);
    	   arrow.getArrowLabel().setIcon(arrowImageIcon);

    	   //set Border to indicate a selected arrow or when hovering over one
    	   if (arrow.isSelected()) {
    		   arrow.getArrowLabel().setBorder(new EtchedBorder(BevelBorder.RAISED, Color.GREEN, getBackground().brighter()));
    	   } else if(selectedCandidateA == arrow) {
    		   arrow.getArrowLabel().setBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()));
    	   } else {
    		   arrow.getArrowLabel().setBorder(BorderFactory.createEmptyBorder());
    	   }

    	   add(arrow.getArrowLabel());
       }
       
	   if(isReleased) {
		   remove(tmpArrowLabel);
	   }

       int outlineVnum = tmpOutline.size();
       if (outlineVnum != 0) {
    	   g2d.setColor(Color.GREEN);
    	   g2d.setStroke(Config.STROKE_SELECTED);
    	   for (int i = 0; i < outlineVnum - 1; i++) {
    		   Vector2d p0 = tmpOutline.get(i);
    		   Vector2d p1 = tmpOutline.get((i+1) % outlineVnum);
    		   g2d.draw(new Line2D.Double(p0.x, p0.y, p1.x, p1.y));
    	   }
    	   
    	   Vector2d cv = selectedCandidateV == null 
    			   ? new Vector2d(currentMousePointLogic.getX(), currentMousePointLogic.getY()) 
    				: selectedCandidateV;
    	   
    	   g2d.draw(new Line2D.Double(tmpOutline.get(0).x, 
    			   tmpOutline.get(0).y, cv.x, cv.y));
    	   g2d.draw(new Line2D.Double(tmpOutline.get(outlineVnum - 1).x, 
    			   tmpOutline.get(outlineVnum - 1).y, cv.x, cv.y));
       }
       
       //show all vertices if in ToolbarMode ADD_VERTEX or DELETE_VERTEX mode or if dispVertex is true
       if (Globals.toolbarMode == Constants.ToolbarMode.ADD_VERTEX 
    		   || Globals.toolbarMode == Constants.ToolbarMode.DELETE_VERTEX
    		   || Globals.dispVertex) {
    	   g2d.setColor(Color.BLACK);
    	   double vertexDrawSize = 4.0;

    	   for (OriLine line : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
    		   Vector2d v0 = line.p0;
    		   Vector2d v1 = line.p1;

    		   g2d.fill(new Rectangle2D.Double(v0.x - vertexDrawSize / scale,
    				   v0.y - vertexDrawSize / scale, vertexDrawSize * 2 / scale,
    				   vertexDrawSize * 2 / scale));
    		   g2d.fill(new Rectangle2D.Double(v1.x - vertexDrawSize / scale,
    				   v1.y - vertexDrawSize / scale, vertexDrawSize * 2 / scale,
    				   vertexDrawSize * 2 / scale));
    	   }
    	   
       }
       
       //draw LINE on firstSelectedV and currentMousePoint
       if (firstSelectedV != null) {
    	   switch (Globals.inputLineType) {
    	   		case OriLine.TYPE_NONE:
    	   			g2d.setColor(Config.LINE_COLOR_SELECTED);//TODO TYPE_NONE COLOR
    	   			g2d.setStroke(Config.STROKE_SELECTED);
    	   			break;
    	   		case OriLine.TYPE_VALLEY:
    	   			g2d.setColor(Config.LINE_COLOR_VALLEY);
    	   			g2d.setStroke(Config.STROKE_VALLEY);
    	   			break;
    	   		case OriLine.TYPE_MOUNTAIN:
    	   			g2d.setColor(Config.LINE_COLOR_MOUNTAIN);
    	   			g2d.setStroke(Config.STROKE_MOUNTAIN);
    	   			break;
    	   		case OriLine.TYPE_XRAY:
    	   			g2d.setColor(Config.LINE_COLOR_XRAY);
    	   			g2d.setStroke(Config.STROKE_XRAY);
    	   			break;
    	   		case OriLine.TYPE_EDGE:
    	   			g2d.setColor(Config.LINE_COLOR_EDGE);
    	   			g2d.setStroke(Config.STROKE_EDGE);
    	   			break;
    	   }
    	   
    	   g2d.fill(new Rectangle2D.Double(firstSelectedV.x - 5.0 / scale,
    			   firstSelectedV.y - 5.0 / scale, 10.0 / scale, 10.0 / scale));
    	   
    	   if(Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE) {
        	   if (Globals.lineEditMode == Constants.LineInputMode.INPUT_LINE) {
        		   Vector2d cv = selectedCandidateV == null 
        				   ? new Vector2d(currentMousePointLogic.getX(), currentMousePointLogic.getY()) 
        					: selectedCandidateV;
        			g2d.draw(new Line2D.Double(firstSelectedV.x, firstSelectedV.y, cv.x, cv.y));
        	   }
    	   }

    	   
       }
       
       if (secondSelectedV != null) {
    	   g2d.setColor(Color.RED);
    	   g2d.fill(new Rectangle2D.Double(secondSelectedV.x - 5.0 / scale,
    			   secondSelectedV.y - 5.0 / scale, 10.0 / scale, 10.0 / scale));
       }
       
       if (thirdSelectedV != null) {
    	   g2d.setColor(Color.RED);
    	   g2d.fill(new Rectangle2D.Double(thirdSelectedV.x - 5.0 / scale,
    			   thirdSelectedV.y - 5.0 / scale, 10.0 / scale, 10.0 / scale));
       }
       
       
       for (Vector2d v : crossPoints) {
    	   g2d.setColor(Color.YELLOW);
    	   g2d.fill(new Rectangle2D.Double(v.x - 5.0 / scale, v.y - 5.0 / scale, 10.0 / scale,
    			   10.0 / scale));
       }
       
       if (selectedCandidateV != null) {
    	   g2d.setColor(Color.GREEN);
    	   g2d.fill( new Rectangle2D.Double(selectedCandidateV.x - 5.0 / scale,
    			   selectedCandidateV.y - 5.0 / scale, 10.0 / scale, 10.0 / scale));
       }
       
       if (Globals.bDispCrossLine) {
    	   if(!Origrammer.diagram.steps.get(Globals.currentStep).crossLines.isEmpty()) {
    		   g2d.setStroke(Config.STROKE_EDGE);
    		   g2d.setColor(Color.MAGENTA);
    		   
    		   for (OriLine line : Origrammer.diagram.steps.get(Globals.currentStep).crossLines) {
    			   Vector2d v0 = line.p0;
    			   Vector2d v1 = line.p1;
    			   
    			   g2d.draw(new Line2D.Double(v0.x, v0.y, v1.x, v1.y));
    		   }
    	   }
       }

       //draw RECTANGULAR selection 
       if (currentMouseDraggingPoint != null && (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL)) {
    	   Point2D.Double sp = new Point2D.Double();
    	   Point2D.Double ep = new Point2D.Double();

    	   try {
    		   affineTransform.inverseTransform(preMousePoint, sp);
    		   affineTransform.inverseTransform(currentMouseDraggingPoint, ep);
    		   g2d.setStroke(Config.STROKE_SELECTED);
    		   g2d.setColor(Color.BLACK);
    		   double sx = Math.min(sp.x, ep.x);
    		   double sy = Math.min(sp.y, ep.y);
    		   double w = Math.abs(sp.x - ep.x);
    		   double h = Math.abs(sp.y - ep.y);
    		   g2d.draw(new Rectangle2D.Double(sx, sy, w, h));
    	   } catch (NoninvertibleTransformException e) {
    		   e.printStackTrace();
    	   }
       }
       
       //show coordinates of selected Vertex
       if (selectedCandidateV != null ) {
    	   g.setColor(Color.BLACK);
    	   g.drawString("(" + selectedCandidateV.x + ", " + selectedCandidateV.y + ")", -250, -250);
       }   
    }
    
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
    	crossPoints.clear();
    	tmpOutline.clear();
    }
    
    //update the AffineTransform
    private void updateAffineTransform(Graphics2D g2d) {
    	affineTransform.setToTranslation(getWidth()*0.5+transX, getHeight()*0.5+transY);
    	affineTransform.scale(scale, scale);
        //affineTransform.setToTranslation(Constants.DEFAULT_PAPER_SIZE, Constants.DEFAULT_PAPER_SIZE);
    	g2d.transform(affineTransform);

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

//    	try {
//    		img = ImageIO.read(new File("./images/" + fileName + rot +".gif"));
//    	} catch (IOException e) {
//    		e.printStackTrace();
//    	}
    	    	
    	return img;

    }
    
    
    private Vector2d pickVertex(Point2D.Double p) {
    	double minDistance = Double.MAX_VALUE;
    	Vector2d minPosition = new Vector2d();
    	
    	for (OriLine line : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
    		double dist0 = p.distance(line.p0.x, line.p0.y);
    		if(dist0 < minDistance) {
    			minDistance = dist0;
    			minPosition.set(line.p0);
    		}
    		double dist1 = p.distance(line.p1.x, line.p1.y);
    		if (dist1 < minDistance) {
    			minDistance = dist1;
    			minPosition.set(line.p1);
    		}
    	}
    	
    	if(dispGrid) {
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
    	
    	if (minDistance < 10.0 / scale) {
    		return minPosition;
    	} else {
    		return null;
    	}
    }
    
    private OriLine pickLine(Point2D.Double p) {
    	double minDistance = Double.MAX_VALUE;
    	OriLine bestLine = null;
    	
    	for(OriLine line : Origrammer.diagram.steps.get(Globals.currentStep).lines) {

    		double dist = GeometryUtil.DistancePointToSegment(new Vector2d(p.x, p.y), line.p0, line.p1);
    		if (dist < minDistance) {
    			minDistance = dist;
    			bestLine = line;
    		}
    	}
    	
    	if (minDistance / scale < 10) {
    		return bestLine;
    	} else {
    		return null;
    	}
    }
    
    private OriArrow  pickArrow(Point2D.Double p) {
    	//double minDistance = Double.MAX_VALUE;
    	OriArrow bestArrow = null;
    	
    	for(OriArrow arrow : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
    		boolean pickedA = GeometryUtil.isMouseOverArrow(p.getX(), p.getY(), arrow);
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
    		boolean pickedF = GeometryUtil.isMouseOverFace(p.x, p.y, face);
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
		
		if(Globals.toolbarMode == Constants.ToolbarMode.NONE) {
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
							GeometryUtil.DistancePointToSegment(cp, l.p0, l.p1, v);
						}
					}
				}
				if (v != null) {
					if(firstSelectedV == null) {
						firstSelectedV = v;
					} else {
						OriLine line = new OriLine(firstSelectedV, v, Globals.inputLineType);
						Origrammer.diagram.steps.get(Globals.currentStep).addLine(line);
						firstSelectedV = null;
					}
				}
			}  else if (Globals.lineEditMode == Constants.LineInputMode.TRIANGLE_INSECTOR) {
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
		} else if (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL) {
			//select OriLine or unselect all OriLines if clicked on nothing
			OriLine l = pickLine(clickPoint);
			if (l != null) {
				if (!l.isSelected) {
					l.isSelected = true;
				} else {
					l.isSelected = false;
				}
			} else {
				Origrammer.diagram.steps.get(Globals.currentStep).unselectAllLines();
			}
			
			//select OriArrow or unselect all OriArrows if clicked on nothing
			OriArrow a = pickArrow(clickPoint);
			if(a != null) {
				if (!a.isSelected()) {
					a.setSelected(true);
				} else if (!isPressedOverArrow){
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
		
			repaint();
			return;
		} else if (Globals.toolbarMode == Constants.ToolbarMode.MEASURE_TOOL) {
			if (Globals.measureMode == Constants.MeasureMode.MEASURE_LENGTH) {
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
						double length = GeometryUtil.measureLength(l.p0, l.p1);
						Origrammer.mainFrame.uiSidePanel.measureLengthTF.setValue(length);;
						firstSelectedL = null;
					}
				
			} else if (Globals.measureMode == Constants.MeasureMode.MEASURE_ANGLE) {
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
		} else if (Globals.toolbarMode == Constants.ToolbarMode.FILL_TOOL) {
			//create OriFace that is to be filled with DEFAULT_PAPER_COLOR --> OriFace is a triangle with 3 OriLines as sides
//			OriLine l = pickLine(clickPoint);
//			if (l != null) {
//				if (firstSelectedL == null) {
//					firstSelectedL = l;
//				} else if (secondSelectedL == null) {
//					secondSelectedL = l;
//				} else {
//					GeneralPath filledFace = GeometryUtil.createFaceFromLines(firstSelectedL, secondSelectedL, l);
//					OriFace newFace = new OriFace(filledFace, false);
//					Origrammer.diagram.steps.get(Globals.currentStep).filledFaces.add(newFace);
//					firstSelectedL = null;
//					secondSelectedL = null;
//					thirdSelectedL = null;
//				}
//			}
			
			
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
		//TODO: CHANGE LINE TYPE		
		repaint();
	}
	
	
	private void closeTmpOutline() {
		ArrayList<OriLine> outlines = new ArrayList<>();
		
		
		int outlineVnum = tmpOutline.size();
		for (int i=0; i<outlineVnum; i++) {
			OriLine line = new OriLine(tmpOutline.get(i), tmpOutline.get((i+1)% outlineVnum), OriLine.TYPE_EDGE);
		}
		
		while (true) {
			boolean bDeleteLine = false;
			for (OriLine lines : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
				
			}
		}
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
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		//zoom on diagram with mouseWheel
		double scale_ = (100.0 - e.getWheelRotation() * 5) / 100.0;
		scale *= scale_;
		//updateAffineTransform(g2d);
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if ((e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0 &&
				(e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK) {
			//scale diagram with CTRL + dragging mouse
			double moved = e.getX() - preMousePoint.getX() + e.getY() - preMousePoint.getY();
			scale += moved / 150.0;
			if (scale < 0.01) {
				scale = 0.01;
			}
			preMousePoint = e.getPoint();
			updateAffineTransform(g2d);
			repaint();
		} else if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
			//translate diagram with right click + dragging mouse
			transX += (double) (e.getX() - preMousePoint.getX()) / scale;
			transY += (double) (e.getY() - preMousePoint.getY()) / scale;
			preMousePoint = e.getPoint();
			updateAffineTransform(g2d);
			repaint();
		} else if ((Globals.toolbarMode == Constants.ToolbarMode.INPUT_ARROW) 
				&& (e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
			//ADD new OriArrow on MousePoint
			isReleased = false;
			int type = Globals.inputArrowType;
			double xPos = (double) (preMousePoint.getX()-400) / scale;
			double yPos = (double) (preMousePoint.getY()-400) / scale;
			preMousePoint = e.getPoint();
			tmpOriArrow = new OriArrow(xPos, yPos, type);
			
			//get correct Arrow Image
			BufferedImage img = getBufImgByTypeAndRot(tmpOriArrow);
			
			//get updated label width & height
			int newArrowLabelWidth = (int) Math.round(img.getWidth()/2*tmpOriArrow.getScale());
			int newArrowLabelHeight = (int) Math.round(img.getHeight()/2*tmpOriArrow.getScale());
			
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
			tmpOriArrow.setArrowLabel(tmpArrowLabel);
			tmpOriArrow.getArrowLabel().setBounds((int) tmpOriArrow.getxPos(), 
												(int) tmpOriArrow.getyPos(), 
												(int) tmpOriArrow.getWidth(),
												(int) tmpOriArrow.getHeight());
			repaint();

		} else if (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL) {
			currentMouseDraggingPoint = e.getPoint();

			Point2D.Double affineMouseDraggingPoint = new Point2D.Double();
			try {
				affineTransform.inverseTransform(currentMouseDraggingPoint, affineMouseDraggingPoint);

			} catch (NoninvertibleTransformException e1) {
				e1.printStackTrace();
			}
			OriArrow pickedArrow = pickArrow(affineMouseDraggingPoint);
			
			if (pickArrow(currentMousePointLogic) != null && isPressedOverArrow || isMovingArrows) {
				isMovingArrows = true;

				if (pickedArrow != null) {
					
					double xTrans = (e.getX() - preMousePoint.getX()) / scale;
					double yTrans = (e.getY() - preMousePoint.getY()) / scale;
					preMousePoint = e.getPoint();

					for (OriArrow arrow : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
						//if selected, move to new position
						if(arrow.isSelected()) {

							int newX = (int) Math.round(arrow.getxPos() + xTrans);
							int newY = (int) Math.round(arrow.getyPos() + yTrans);
							arrow.setxPos(newX);
							arrow.setyPos(newY);
							arrow.getArrowLabel().setBounds(newX, newY, 
									(int) Math.round(arrow.getWidth() * arrow.getScale()), 
									(int) Math.round(arrow.getHeight() * arrow.getScale()));
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
		
		if(Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE) {
			if (Globals.lineEditMode == Constants.LineInputMode.INPUT_LINE
					|| Globals.lineEditMode == Constants.LineInputMode.TRIANGLE_INSECTOR
					|| Globals.toolbarMode == Constants.ToolbarMode.FILL_TOOL) {
				Vector2d firstV = selectedCandidateV;
				selectedCandidateV = this.pickVertex(currentMousePointLogic);
				
				if(selectedCandidateV == null) {
					if ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK) {
						OriLine l = pickLine(currentMousePointLogic);
						if(l != null) {
							selectedCandidateV = new Vector2d();
							Vector2d cp = new Vector2d(currentMousePointLogic.x, currentMousePointLogic.y);
							GeometryUtil.DistancePointToSegment(cp,  l.p0, l.p1, selectedCandidateV);
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
			
			OriArrow preArrow = selectedCandidateA;
			selectedCandidateA = pickArrow(currentMousePointLogic);
			if (preArrow != selectedCandidateA) {
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
		} else if (Globals.toolbarMode == Constants.ToolbarMode.FILL_TOOL) {
			selectedCandidateV = this.pickVertex(currentMousePointLogic);
			repaint();
		}
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
	public void mousePressed(MouseEvent e) {
		preMousePoint = e.getPoint();
		
		//mark OriArrow as selected if in SELECTION_TOOL mode and you have pressed left mouse button while over OriArrow -->
		if (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL) {
			OriArrow pickedArrow = pickArrow(currentMousePointLogic);
			if (pickedArrow != null) {
				pickedArrow.setSelected(true);
				isPressedOverArrow = true;
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
			xPos += (double) (preMousePoint.getX()-400) / scale;
			yPos += (double) (preMousePoint.getY()-400) / scale;
			Origrammer.diagram.steps.get(Globals.currentStep).addArrow(new OriArrow(xPos, yPos, type));

			//get last added OriArrow
			OriArrow arrow = Origrammer.diagram.steps.get(Globals.currentStep).arrows.get(Origrammer.diagram.steps.get(Globals.currentStep).arrows.size()-1);

			//get correct Arrow Image
			BufferedImage img = null;
			img = getBufImgByTypeAndRot(arrow);

			//get updated label width & height
			int newArrowLabelWidth = (int) Math.round(img.getWidth()/2*arrow.getScale());
			int newArrowLabelHeight = (int) Math.round(img.getHeight()/2*arrow.getScale());

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
			arrow.setArrowLabel(arrowLabel);
			arrow.getArrowLabel().setBounds((int)arrow.getxPos(), 
											(int)arrow.getyPos(), 
											(int) Math.round(arrow.getWidth()*arrow.getScale()), 
											(int) Math.round(arrow.getHeight()*arrow.getScale()));
			repaint();		
		} else if (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL
				&& currentMouseDraggingPoint != null) {
			//Rectangular Selection
			ArrayList<OriLine> selectedLines = new ArrayList<>();
			Point2D.Double sp = new Point2D.Double();
			Point2D.Double ep = new Point2D.Double();
			try {
				affineTransform.inverseTransform(preMousePoint, sp);
				affineTransform.inverseTransform(currentMouseDraggingPoint, ep);

				int minX = (int) Math.round(Math.min(sp.x,  ep.x));
				int minY = (int) Math.round(Math.min(sp.y, ep.y));
				int maxX = (int) Math.round(Math.max(sp.x, ep.x));
				int maxY = (int) Math.round(Math.max(sp.y, ep.y));
				Rectangle tmpR = new Rectangle(minX, minY, maxX-minX, maxY-minY);

				//Check if there is a line in the selection rectangle
				for(OriLine l : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
					Line2D tmpL = new Line2D.Double(l.p0.x, l.p0.y, l.p1.x, l.p1.y);
					if(tmpL.intersects(tmpR)) {
						l.isSelected = true;
					} else {
						l.isSelected = false;
					}
				}

				//Check if there is an arrow in the selection rectangle
				for (OriArrow a : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
					Rectangle tmpR2 = new Rectangle((int) Math.round(a.getxPos()), 
													(int) Math.round(a.getyPos()), 
													a.getWidth(),
													a.getHeight());

					if (tmpR2.intersects(tmpR)) {
						a.setSelected(true);
					} else {
						a.setSelected(false);
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		currentMouseDraggingPoint = null;
		isMovingArrows = false;
		repaint();
	}
}
