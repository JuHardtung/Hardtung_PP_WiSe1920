package origrammer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import java.awt.geom.Line2D;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.vecmath.Vector2d;

import javafx.geometry.Bounds;
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
	private OriLine firstSelectedLine = null;
	private OriLine selectedCandidateL = null;
	private ArrayList<Vector2d> tmpOutline = new ArrayList<>();
	
	private boolean displayGrid = true;
	//Affine transformation info
	private Dimension preSize;
	private AffineTransform affineTransform = new AffineTransform();
	private ArrayList<Vector2d> crossPoints = new ArrayList<>();
	
	private Graphics2D g2d;
	private JLabel arrowLabel;
	
	
	JLabel tmpArrowLabel = new JLabel();
	ImageIcon arrowImage = new ImageIcon("./images/origrammer.jpg");


	
	
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
	
    private void doDrawing(Graphics2D g2d) {

//        g2d.drawString("Java 2D", 50, 50);
//        g2d.drawRect(0, 0, 800, 800);
//        g2d.setColor(Color.red);
        
    	arrowLabel = new JLabel(new ImageIcon("./images/origrammer.jpg"));
    	arrowLabel.setSize(20, 20);
    	arrowLabel.setBounds(-200, -200, 20, 20);
        
        add(arrowLabel);
        
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

       g2d = (Graphics2D) g;        
       updateAffineTransform(g2d);      
       
       if (displayGrid) {
    	   drawGrid(g2d);
    	   }
       
       g2d.setStroke(Config.STROKE_VALLEY);
       g2d.setColor(Color.BLACK);
       for(OriLine line : Origrammer.diagram.lines) {
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
    	   if ((Globals.editMode == Constants.ToolbarMode.INPUT_LINE
    			   && line.selected)
    			   || (Globals.editMode == Constants.ToolbarMode.SELECTION_TOOL
    			   && line.selected)) {
    		   g2d.setColor(Config.LINE_COLOR_SELECTED);
    		   g2d.setStroke(Config.STROKE_SELECTED);
    		   }
    	   
    	   if (line == firstSelectedLine) {
    		   g2d.setColor(Color.RED);
    		   g2d.setStroke(Config.STROKE_SELECTED);
    	   } else if (line == selectedCandidateL) {
    		   g2d.setColor(Config.LINE_COLOR_SELECTED);
    		   g2d.setStroke(Config.STROKE_SELECTED);
    	   }
    	   
    	   g2d.draw(new Line2D.Double(line.p0.x, line.p0.y, line.p1.x, line.p1.y));
       }
       
       //render ARROWS according to their ARROW_TYPE
       for (OriArrow arrow : Origrammer.diagram.arrows) {
    	   ImageIcon arrowImage = new ImageIcon("./images/origrammer.jpg");
    	   switch (arrow.type) {
    	   		case OriArrow.TYPE_VALLEY:
    	   			arrowImage = new ImageIcon("./images/origrammer.jpg");
    	   			break;
    	   		case OriArrow.TYPE_MOUNTAIN:
    	   			arrowImage = new ImageIcon("./images/origrammer.jpg");
    	   			break;
    	   		case OriArrow.TYPE_TURN_OVER:
    	   			arrowImage = new ImageIcon("./images/origrammer.jpg");
    	   			break;
    	   		case OriArrow.TYPE_PUSH_HERE:
    	   			arrowImage = new ImageIcon("./images/origrammer.jpg");
    	   			break;
    	   		case OriArrow.TYPE_PULL_HERE:
    	   			arrowImage = new ImageIcon("./images/origrammer.jpg");
    	   			break;
    	   		case OriArrow.TYPE_INFLATE_HERE:
    	   			arrowImage = new ImageIcon("./images/origrammer.jpg");
    	   			break;
    	   }
    	   
    	   arrowLabel = new JLabel(arrowImage);
    	   arrowLabel.setBounds((int)arrow.xPos, (int)arrow.yPos, (int)arrow.width, (int)arrow.height);
    	   
    	   add(arrowLabel);
    	   
    	   
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
       
       
       //show all vertices if in EditMode ADD_VERTEX or DELETE_VERTEX mode or dispVertex is true
       if(Globals.editMode == Constants.ToolbarMode.ADD_VERTEX 
    		   || Globals.editMode == Constants.ToolbarMode.DELETE_VERTEX
    		   || Globals.dispVertex) {
    	   g2d.setColor(Color.BLACK);
    	   double vertexDrawSize = 4.0;

    	   for (OriLine line : Origrammer.diagram.lines) {
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
    	   
    	   if (Globals.lineEditMode == Constants.LineEditMode.INPUT_LINE) {
    		   Vector2d cv = selectedCandidateV == null 
    				   ? new Vector2d(currentMousePointLogic.getX(), currentMousePointLogic.getY()) 
    					: selectedCandidateV;
    			g2d.draw(new Line2D.Double(firstSelectedV.x, firstSelectedV.y, cv.x, cv.y));
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
    	   g2d.setColor(Color.RED);
    	   g2d.fill(new Rectangle2D.Double(v.x - 5.0 / scale, v.y - 5.0 / scale, 10.0 / scale,
    			   10.0 / scale));
       }
       
       if (selectedCandidateV != null) {
    	   g2d.setColor(Color.GREEN);
    	   g2d.fill( new Rectangle2D.Double(selectedCandidateV.x - 5.0 / scale,
    			   selectedCandidateV.y - 5.0 / scale, 10.0 / scale, 10.0 / scale));
       }
       
       if (Globals.bDispCrossLine) {
    	   if(!Origrammer.diagram.crossLines.isEmpty()) {
    		   g2d.setStroke(Config.STROKE_EDGE);
    		   g2d.setColor(Color.MAGENTA);
    		   
    		   for (OriLine line : Origrammer.diagram.crossLines) {
    			   Vector2d v0 = line.p0;
    			   Vector2d v1 = line.p1;
    			   
    			   g2d.draw(new Line2D.Double(v0.x, v0.y, v1.x, v1.y));
    		   }
    	   }
       }
       
       if (currentMouseDraggingPoint != null
    		   && (Globals.editMode == Constants.ToolbarMode.SELECT_LINE
    		   || Globals.editMode == Constants.ToolbarMode.CHANGE_LINE_TYPE)) {
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
    
    public void setDispGrid(boolean dispGrid) {
    	this.displayGrid = dispGrid;
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
    	firstSelectedLine = null;
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
    
    
    private Vector2d pickVertex(Point2D.Double p) {
    	double minDistance = Double.MAX_VALUE;
    	Vector2d minPosition = new Vector2d();
    	
    	for (OriLine line : Origrammer.diagram.lines) {
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
    	
    	if(displayGrid) {
    		double step = Origrammer.diagram.size / Globals.gridDivNum;
    		for (int ix = 0; ix < Globals.gridDivNum +1; ix++) {
    			for (int iy = 0; iy < Globals.gridDivNum + 1; iy++) {
    				double x = -Origrammer.diagram.size / 2 + step * ix;
    				double y = -Origrammer.diagram.size / 2 + step * iy;
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
    	
    	for(OriLine line : Origrammer.diagram.lines) {
    		if (Globals.editMode == Constants.ToolbarMode.DELETE_LINE) {	
    		}
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
    
    
	@Override
	public void mouseClicked(MouseEvent e) {
		//if right clicked, remove any selected vertices
		if(SwingUtilities.isRightMouseButton(e)) {
			if(firstSelectedV != null) {
				System.out.println("firstSelected reset");
				firstSelectedV = null;
				repaint();
			} else if (secondSelectedV != null) {
				System.out.println("secondSelected reset");

				secondSelectedV = null;
				repaint();
			} else if (thirdSelectedV != null) {
				System.out.println("thirdSelected reset");

				thirdSelectedV = null;
				repaint();
			}
			return;
		}
		
		if(Globals.editMode == Constants.ToolbarMode.NONE) {
			return;
		}
		
		//get mouse click coordinates
		Point2D.Double clickPoint = new Point2D.Double();
		try {
			affineTransform.inverseTransform(e.getPoint(), clickPoint);
		} catch (Exception ex) {
			return;
		}
		//System.out.println("Mouse Position: " + clickPoint.toString());
		
		if (Globals.editMode == Constants.ToolbarMode.INPUT_LINE) {
			if (Globals.lineEditMode == Constants.LineEditMode.INPUT_LINE) {
				Vector2d v = pickVertex(clickPoint);
				
				if(v == null) {
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
						Origrammer.diagram.addLine(line);
						firstSelectedV = null;
					}
				}
				System.out.println("Nr. of Lines: " + Origrammer.diagram.lines.size());
			}
		} else if (Globals.editMode == Constants.ToolbarMode.INPUT_ARROW){
			
//			double xPos = currentMousePointLogic.x;
//			double yPos = currentMousePointLogic.y;
//			int type = Globals.inputArrowType;
//			Origrammer.diagram.addArrow(new OriArrow(xPos, yPos, type));
//			repaint();
			
		} else if (Globals.editMode == Constants.ToolbarMode.SELECTION_TOOL) {
			OriLine l = pickLine(clickPoint);
			
			if (l != null) {
				if (!l.isSelected()) {
					l.setSelected(true);
				} else {
					l.setSelected(false);
				}
			}
		
			repaint();
			return;
		}
		

		//TODO: CHANGE LINE TYPE
		//TODO: DELETE LINE
		//TODO: PICK LINE
		
		repaint();
		
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
		} else if ((Globals.editMode == Constants.ToolbarMode.INPUT_ARROW) 
				&& (e.getModifiers() & MouseEvent.BUTTON1_MASK) != 0) {
			

			double xPos = 0;
			double yPos = 0;
			xPos += (double) (preMousePoint.getX()-400) / scale;
			yPos += (double) (preMousePoint.getY()-400) / scale;
			preMousePoint = e.getPoint();
			
			tmpArrowLabel.setIcon(arrowImage);
			tmpArrowLabel.setBounds((int) xPos, (int) yPos, 20, 20);

			add(tmpArrowLabel);

			
			//System.out.println("MousePos: " + currentMousePointLogic.x + " | " + currentMousePointLogic.y);
			repaint();
		} else if (Globals.editMode == Constants.ToolbarMode.SELECT_LINE
				|| Globals.editMode == Constants.ToolbarMode.CHANGE_LINE_TYPE) {
			currentMouseDraggingPoint = e.getPoint();
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
		
		if(Globals.editMode == Constants.ToolbarMode.INPUT_LINE) {
			if (Globals.lineEditMode == Constants.LineEditMode.INPUT_LINE) {
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
		} else if (Globals.editMode == Constants.ToolbarMode.SELECTION_TOOL) {
			OriLine preLine = selectedCandidateL;
			selectedCandidateL = pickLine(currentMousePointLogic);
			if (preLine != selectedCandidateL) {
				repaint();

			}
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
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(Globals.editMode == Constants.ToolbarMode.INPUT_ARROW) {

			int type = Globals.inputArrowType;
			double xPos = 0;
			double yPos = 0;
			xPos += (double) (preMousePoint.getX()-400) / scale;
			yPos += (double) (preMousePoint.getY()-400) / scale;
			Origrammer.diagram.addArrow(new OriArrow(xPos, yPos, type));
			remove(tmpArrowLabel);
			repaint();
		}
		
		
	}
}
