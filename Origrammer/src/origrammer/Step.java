package origrammer;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

import javax.vecmath.Vector2d;

import origrammer.geometry.GeometryUtil;
import origrammer.geometry.OriArrow;
import origrammer.geometry.OriEqualAnglSymbol;
import origrammer.geometry.OriEqualDistSymbol;
import origrammer.geometry.OriFace;
import origrammer.geometry.OriGeomSymbol;
import origrammer.geometry.OriLeaderBox;
import origrammer.geometry.OriLine;
import origrammer.geometry.OriPicSymbol;
import origrammer.geometry.OriPleatCrimpSymbol;
import origrammer.geometry.OriVertex;

class PointComparatorX implements Comparator<Object> {
	@Override
	public int compare(Object v1, Object v2) {
		if (((Vector2d) v1).x > ((Vector2d) v2).x) {
			return 1;
		} else if (((Vector2d) v1).x < ((Vector2d) v2).x) {
			return -1;
		} else {
			return 0;
		}
	}
}

class PointComparatorY implements Comparator<Object> {
	@Override
	public int compare(Object v1, Object v2) {
		if (((Vector2d) v1).y > ((Vector2d) v2).y) {
			return 1;
		} else if (((Vector2d) v1).y < ((Vector2d) v2).y) {
			return -1;
		} else {
			return 0;
		}
	}
}

class UndoInfo {
	public ArrayList<OriLine> lines = new ArrayList<>();
	public ArrayList<OriVertex> vertices = new ArrayList<>();
	public ArrayList<OriArrow> arrows = new ArrayList<>();
	public ArrayList<OriFace> filledFaces = new ArrayList<>();
	public ArrayList<OriLeaderBox> leaderBoxSymbols = new ArrayList<>();
	public ArrayList<OriPicSymbol> picSymbols = new ArrayList<>();
	public ArrayList<OriGeomSymbol> geomSymbols = new ArrayList<>();
	public ArrayList<OriEqualDistSymbol> equalDistSymbols = new ArrayList<>();
	public ArrayList<OriEqualAnglSymbol> equalAnglSymbols = new ArrayList<>();
	public ArrayList<OriPleatCrimpSymbol> pleatCrimpSymbols = new ArrayList<>();
}


public class Step {

	public ArrayList<OriLine> lines = new ArrayList<>();
	public ArrayList<OriVertex> vertices = new ArrayList<>();
	public ArrayList<OriArrow> arrows = new ArrayList<>();
	public ArrayList<OriFace> filledFaces = new ArrayList<>();
	public ArrayList<OriLeaderBox> leaderBoxSymbols = new ArrayList<>();
	public ArrayList<OriPicSymbol> picSymbols = new ArrayList<>();
	public ArrayList<OriGeomSymbol> geomSymbols = new ArrayList<>();
	public ArrayList<OriEqualDistSymbol> equalDistSymbols = new ArrayList<>();
	public ArrayList<OriEqualAnglSymbol> equalAnglSymbols = new ArrayList<>();
	public ArrayList<OriPleatCrimpSymbol> pleatCrimpSymbols = new ArrayList<>();
	private Stack<UndoInfo> undoStack = new Stack<UndoInfo>();
	public CopiedObjects copiedObjects = new CopiedObjects();
	public String stepDescription;
	public int stepNumber;

	public static final double POINT_EPS = 1.0e-6;

	public double size = Constants.DEFAULT_PAPER_SIZE;


	public Step() {
		if (Globals.newStepOptions == Constants.NewStepOptions.PASTE_DEFAULT_PAPER) {
			initFirstStep();
		}
		stepNumber = Globals.currentStep;
	}

	public void initFirstStep() {
		lines.addAll(getEdgeLines());
	}
	
	
	public void copyObjects() {
		copiedObjects.clear();
		
		copiedObjects.lines = getSelectedOriLines();
		copiedObjects.vertices = getSelectedVertices();
		copiedObjects.arrows = getSelectedOriArrows();
		copiedObjects.filledFaces = getSelectedFilledFaces();
		copiedObjects.leaderBoxSymbols = getSelectedLeaderBoxes();
		copiedObjects.picSymbols = getSelectedPicSymbols();
		copiedObjects.geomSymbols = getSelectedGeomSymbols();
		copiedObjects.equalDistSymbols = getSelectedEqualDistSymbols();
		copiedObjects.equalAnglSymbols = getSelectedEqualAnglSymbols();
		copiedObjects.pleatCrimpSymbols = getSelectedPleatSymbols();
		
	}
	
	public void pasteCopiedObjects() {
		for (OriLine l : copiedObjects.lines) {
			OriLine inL = new OriLine(l);
			inL.setP0(new Vector2d(l.getP0().x + 20, l.getP0().y + 20));
			inL.setP1(new Vector2d(l.getP1().x + 20, l.getP1().y + 20));	//TODO: maybe snapping to grid? --> issue with copy/pasting multiple different OriObjects
			addLine(inL);													//TODO: could use line-snapping-to-grid only when lines are copied --> else just move everything by (20, 20)
		}
//		for (OriVertex v : copiedObjects.vertices) {
//			OriVertex inV = new OriVertex(v);			//TODO: doesn't make sense as you can simply add more vertices  --> don't include them
//			addVertex(inV);
//		}
		for (OriArrow a : copiedObjects.arrows) {
			OriArrow inA = new OriArrow(a);
			inA.setPosition(new Vector2d(a.getPosition().x + 20, a.getPosition().y + 20));
			addArrow(inA);
		}
//		for (OriFace f : copiedObjects.filledFaces) {
//			OriFace inF = new OriFace(f);
//															//TODO: doesn't make sense as FilledFaces are not movable?  --> don't include them
//			addFilledFace(inF);
//		}
		for (OriLeaderBox lb : copiedObjects.leaderBoxSymbols) {
			OriLeaderBox inLb = new OriLeaderBox(lb);
			inLb.moveBy(20, 20);
			addLeader(inLb);
		}
		for (OriPicSymbol ps : copiedObjects.picSymbols) {
			OriPicSymbol inPs = new OriPicSymbol(ps);
			Rectangle oldBounds = ps.label.getBounds();
			inPs.label.setBounds(oldBounds.x + 20, oldBounds.y + 20, oldBounds.width + 20, oldBounds.height + 20);
			inPs.setPosition(new Vector2d(inPs.getPosition().x + 20, inPs.getPosition().y + 20));
			addPicSymbol(inPs);
		}
		for (OriGeomSymbol gs : copiedObjects.geomSymbols) {
			OriGeomSymbol inGs = new OriGeomSymbol(gs);
			inGs.setPosition(new Vector2d(gs.getPosition().x + 20, gs.getPosition().y + 20));
			addGeomSymbol(inGs);
		}
//		for (OriEqualDistSymbol eds : copiedObjects.equalDistSymbols) {
//			OriEqualDistSymbol inEds = new OriEqualDistSymbol(eds);
//			inEds.setP0(new Vector2d(eds.getP0().x + 20, eds.getP0().y + 20));
//			inEds.setP1(new Vector2d(eds.getP1().x + 20, eds.getP1().y + 20));
//			addEqualDistSymbol(inEds);
//		}																				TODO: make OriEqualDistSymbol and OriEqualAnglSymbol movable with snapping to grid
//		for (OriEqualAnglSymbol eas : copiedObjects.equalAnglSymbols) {
//			OriEqualAnglSymbol inEas = new OriEqualAnglSymbol(eas);
//			inEas.setV(new Vector2d(eas.getV().x + 20, eas.getV().y + 20));
//			inEas.setA(new Vector2d(eas.getA().x + 20, eas.getA().y + 20));
//			inEas.setB(new Vector2d(eas.getB().x + 20, eas.getB().y + 20));
//
//			addEqualAngleSymbol(inEas);
//		}
		for (OriPleatCrimpSymbol pcs : copiedObjects.pleatCrimpSymbols) {
			OriPleatCrimpSymbol inPcs = new OriPleatCrimpSymbol(pcs);
			inPcs.setPosition(new Vector2d(pcs.getPosition().x + 20, pcs.getPosition().y + 20));
			addPleatSymbol(inPcs);
		}
	}
	
	public void pushUndoInfo() {
		UndoInfo ui = new UndoInfo();
		for (OriLine l : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
			ui.lines.add(new OriLine(l));
		}
		for (OriVertex v : Origrammer.diagram.steps.get(Globals.currentStep).vertices) {
			ui.vertices.add(new OriVertex(v));
		}
		for (OriArrow a : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
			ui.arrows.add(new OriArrow(a));
		}
		for (OriFace f : Origrammer.diagram.steps.get(Globals.currentStep).filledFaces) {
			ui.filledFaces.add(new OriFace(f));
		}
		for (OriLeaderBox lb : Origrammer.diagram.steps.get(Globals.currentStep).leaderBoxSymbols) {
			ui.leaderBoxSymbols.add(new OriLeaderBox(lb));
		}
		for (OriPicSymbol ps : Origrammer.diagram.steps.get(Globals.currentStep).picSymbols) {
			ui.picSymbols.add(new OriPicSymbol(ps));
		}
		for (OriGeomSymbol gs : Origrammer.diagram.steps.get(Globals.currentStep).geomSymbols) {
			ui.geomSymbols.add(new OriGeomSymbol(gs));
		}
		for (OriEqualDistSymbol eds : Origrammer.diagram.steps.get(Globals.currentStep).equalDistSymbols) {
			ui.equalDistSymbols.add(new OriEqualDistSymbol(eds));
		}
		for (OriEqualAnglSymbol eas : Origrammer.diagram.steps.get(Globals.currentStep).equalAnglSymbols) {
			ui.equalAnglSymbols.add(new OriEqualAnglSymbol(eas));
		}
		for (OriPleatCrimpSymbol pcs : Origrammer.diagram.steps.get(Globals.currentStep).pleatCrimpSymbols) {
			ui.pleatCrimpSymbols.add(new OriPleatCrimpSymbol(pcs));
		}
		undoStack.push(ui);
	}
	
	public void popUndoInfo() {
		if (undoStack.isEmpty()) {
			System.out.println("STACK EMPTY");
			return;
		}
		UndoInfo ui = undoStack.pop();
		Origrammer.diagram.steps.get(Globals.currentStep).lines.clear();
		Origrammer.diagram.steps.get(Globals.currentStep).lines.addAll(ui.lines);
		Origrammer.diagram.steps.get(Globals.currentStep).vertices.clear();
		Origrammer.diagram.steps.get(Globals.currentStep).vertices.addAll(ui.vertices);
		Origrammer.diagram.steps.get(Globals.currentStep).arrows.clear();
		Origrammer.diagram.steps.get(Globals.currentStep).arrows.addAll(ui.arrows);
		Origrammer.diagram.steps.get(Globals.currentStep).filledFaces.clear();
		Origrammer.diagram.steps.get(Globals.currentStep).filledFaces.addAll(ui.filledFaces);
		Origrammer.diagram.steps.get(Globals.currentStep).leaderBoxSymbols.clear();
		Origrammer.diagram.steps.get(Globals.currentStep).leaderBoxSymbols.addAll(ui.leaderBoxSymbols);
		Origrammer.diagram.steps.get(Globals.currentStep).picSymbols.clear();
		Origrammer.diagram.steps.get(Globals.currentStep).picSymbols.addAll(ui.picSymbols);
		Origrammer.diagram.steps.get(Globals.currentStep).geomSymbols.clear();
		Origrammer.diagram.steps.get(Globals.currentStep).geomSymbols.addAll(ui.geomSymbols);
		Origrammer.diagram.steps.get(Globals.currentStep).equalDistSymbols.clear();
		Origrammer.diagram.steps.get(Globals.currentStep).equalDistSymbols.addAll(ui.equalDistSymbols);
		Origrammer.diagram.steps.get(Globals.currentStep).equalAnglSymbols.clear();
		Origrammer.diagram.steps.get(Globals.currentStep).equalAnglSymbols.addAll(ui.equalAnglSymbols);
		Origrammer.diagram.steps.get(Globals.currentStep).pleatCrimpSymbols.clear();
		Origrammer.diagram.steps.get(Globals.currentStep).pleatCrimpSymbols.addAll(ui.pleatCrimpSymbols);
	}
	

	public ArrayList<OriLine> getEdgeLines() {
		if (Globals.paperShape == Constants.PaperShape.SQUARE) {
			//TODO: this is setup for default square paper --> todo for different shapes like octagonal etc.
			return getSquareEdgeLines();
		} else if (Globals.paperShape == Constants.PaperShape.RECTANGLE) {
			return getRectEdgeLines();
		} else {
			return null;
		}
	}

	public ArrayList<OriLine> getSquareEdgeLines() {
		ArrayList<OriLine> newLines = new ArrayList<>();

		OriLine l0 = new OriLine(-size/2.0, size/2.0, size/2.0, size/2.0, OriLine.TYPE_EDGE);
		OriLine l1 = new OriLine(size/2.0, size/2.0, size/2.0, -size/2.0, OriLine.TYPE_EDGE);
		OriLine l2 = new OriLine(size/2.0, -size/2.0, -size/2.0, -size/2.0, OriLine.TYPE_EDGE);
		OriLine l3 = new OriLine(-size/2.0, -size/2.0, -size/2.0, size/2.0, OriLine.TYPE_EDGE);
		newLines.add(l0);
		newLines.add(l1);
		newLines.add(l2);
		newLines.add(l3);
		return newLines;
	}


	public ArrayList<OriLine> getRectEdgeLines() {
		ArrayList<OriLine> newLines = new ArrayList<>();

		double width = Origrammer.diagram.recPaperWidth;
		double height = Origrammer.diagram.recPaperHeight;
		double ratio = 0;

		if (width > height) {
			ratio =  height / width;
		} else {
			ratio = width / height;
		}

		OriLine l0 = new OriLine(-size/2.0, size/2.0*ratio, size/2.0, size/2.0*ratio, OriLine.TYPE_EDGE);
		OriLine l1 = new OriLine(size/2.0, size/2.0*ratio, size/2.0, -size/2.0*ratio, OriLine.TYPE_EDGE);
		OriLine l2 = new OriLine(size/2.0, -size/2.0*ratio, -size/2.0, -size/2.0*ratio, OriLine.TYPE_EDGE);
		OriLine l3 = new OriLine(-size/2.0, -size/2.0*ratio, -size/2.0, size/2.0*ratio, OriLine.TYPE_EDGE);
		newLines.add(l0);
		newLines.add(l1);
		newLines.add(l2);
		newLines.add(l3);

		return newLines;
	}



	/**Adds a new OriLine and checks for intersections with others
	 * 
	 * @param inputLine
	 */
	public void addLine(OriLine inputLine) {

		//don't add the line if it already exists
		for (OriLine l : lines) {
			if (GeometryUtil.isSameLineSegment(l, inputLine)) {
				return;
			}
		}

		splitExistingLines(inputLine);

		//points contains p0 and p1 of inputLine
		ArrayList<Vector2d> points = new ArrayList<>();
		points.add(inputLine.getP0());
		points.add(inputLine.getP1());
		points.addAll(splitInputLine(inputLine));

		//sort ArrayList<Vector2d> points
		boolean sortByX = Math.abs(inputLine.getP0().x - inputLine.getP1().x) > Math.abs(inputLine.getP0().y - inputLine.getP1().y);
		if (sortByX) {
			Collections.sort(points, new PointComparatorX());
		} else {
			Collections.sort(points, new PointComparatorY());
		}

		addInputLine(points, inputLine);
	}

	public void addInputLine(ArrayList<Vector2d> points, OriLine inputLine) {
		Vector2d prePoint = points.get(0);

		for (int i = 1; i < points.size(); i++) {
			Vector2d p = points.get(i);
			if (GeometryUtil.Distance(prePoint, p) < POINT_EPS) {
				continue;
			}

			OriLine newLine = new OriLine(prePoint, p, inputLine.getType());

			//update isStartOffset and isEndOffset if p0 and p1 got switched after sorting
			if (prePoint == inputLine.getP0()) {
				newLine.setStartOffset(inputLine.isStartOffset());
			} else if (prePoint == inputLine.getP1()) {
				newLine.setStartOffset(inputLine.isEndOffset());
			}
			if (p == inputLine.getP0()) {
				newLine.setEndOffset(inputLine.isStartOffset());
			} else if (p == inputLine.getP1()) {
				newLine.setEndOffset(inputLine.isEndOffset());
			}
			lines.add(newLine);
			prePoint = p;
		}
	}

	public ArrayList<Vector2d> splitInputLine(OriLine inputLine) {
		ArrayList<Vector2d> points = new ArrayList<>();
		//split up the inputLine where it crosses existing lines
		for (OriLine line : lines) {
			if (GeometryUtil.Distance(inputLine.getP0(),  line.getP0()) < POINT_EPS) {
				continue;
			}
			if (GeometryUtil.Distance(inputLine.getP0(),  line.getP1()) < POINT_EPS) {
				continue;
			}
			if (GeometryUtil.Distance(inputLine.getP1(),  line.getP0()) < POINT_EPS) {
				continue;
			}
			if (GeometryUtil.Distance(inputLine.getP1(),  line.getP1()) < POINT_EPS) {
				continue;
			}
			if (GeometryUtil.DistancePointToSegment(line.getP0(), inputLine.getP0(), inputLine.getP1()) < POINT_EPS) {
				points.add(line.getP0());
			}
			if (GeometryUtil.DistancePointToSegment(line.getP1(), inputLine.getP0(), inputLine.getP1()) < POINT_EPS) {
				points.add(line.getP1());
			}

			Vector2d crossPoint = GeometryUtil.getCrossPoint(inputLine, line);
			if (crossPoint != null) {
				points.add(crossPoint);
			}
		}
		return points;
	}

	/** Checks if the inputLine crosses any existing lines and splits them if necessary.
	 *  Also checks the correct direction in combination with the start and end offset for OriLine TYPE_CREASES
	 * @param inputLine The entered Line
	 */
	public void splitExistingLines(OriLine inputLine) {
		ArrayList<OriLine> tmpLines = new ArrayList<>();
		tmpLines.addAll(lines);

		//if new line crosses another one, split them up to smaller lines
		for (OriLine l : tmpLines) {
			Vector2d crossPoint = GeometryUtil.getCrossPoint(l, inputLine);
			if (crossPoint == null) {
				continue;
			}

			lines.remove(l);

			//when splitting lines, check what direction the existing line is facing --> according to that disregard some of the offsetFlafs for the endPoints
			if (l.getP0().x < l.getP1().x || ((l.getP0().x == l.getP1().x) && (l.getP0().y < l.getP1().y))) {
				if (GeometryUtil.Distance(l.getP0(), crossPoint) > POINT_EPS) {
					//when inputLine only shares an endpoint of an existing line (and not crossing it), don't disregard the offsetFlag for the endPoint
					//the side of a split up line where the crossPoint is should not have an offset, except for when the crossPoint equals one of the inputLine points
					if (crossPoint.equals(inputLine.getP0()) || crossPoint.equals(inputLine.getP1())) {
						lines.add(new OriLine(l.getP0(), crossPoint, l.getType(), l.isStartOffset(), l.isEndOffset()));
					} else {
						lines.add(new OriLine(l.getP0(), crossPoint, l.getType(), l.isStartOffset(), false));
					}
				}
				if (GeometryUtil.Distance(crossPoint, l.getP1()) > POINT_EPS) {
					if (crossPoint.equals(inputLine.getP0()) || crossPoint.equals(inputLine.getP1())) {
						lines.add(new OriLine(crossPoint, l.getP1(), l.getType(), l.isStartOffset(), l.isEndOffset()));
					} else {
						lines.add(new OriLine(crossPoint, l.getP1(), l.getType(), false, l.isEndOffset()));
					}
				}
			} else if (l.getP1().x < l.getP0().x || ((l.getP1().x == l.getP1().x) && (l.getP1().y < l.getP0().y))) {
				if (GeometryUtil.Distance(l.getP1(), crossPoint) > POINT_EPS) {
					if (crossPoint.equals(inputLine.getP0()) || crossPoint.equals(inputLine.getP1())) {
						lines.add(new OriLine(crossPoint, l.getP1(), l.getType(), l.isStartOffset(), l.isEndOffset()));
					} else {
						lines.add(new OriLine(crossPoint, l.getP1(), l.getType(), false, l.isEndOffset()));
					}
				}
				if (GeometryUtil.Distance(crossPoint, l.getP0()) > POINT_EPS) {
					if (crossPoint.equals(inputLine.getP0()) || crossPoint.equals(inputLine.getP1())) {
						lines.add(new OriLine(l.getP0(), crossPoint, l.getType(), l.isStartOffset(), l.isEndOffset()));
					} else {
						lines.add(new OriLine(l.getP0(), crossPoint, l.getType(), l.isStartOffset(), false));
					}
				}
			}
		}
	}


	public void addTriangleInsectorLines(Vector2d v0, Vector2d v1, Vector2d v2) {
		Vector2d incenter = GeometryUtil.getIncenter(v0,v1,v2);
		if (incenter == null) {
			System.out.println("Failed to calculate the incenter of the triangle");
		}
		Origrammer.diagram.steps.get(Globals.currentStep).pushUndoInfo();
		addLine(new OriLine(incenter, v0, Globals.inputLineType));
		addLine(new OriLine(incenter, v1, Globals.inputLineType));
		addLine(new OriLine(incenter, v2, Globals.inputLineType));

	}

	/**
	 * Adds a new Vertex to the current diagram step
	 * @param inputVertex
	 */
	public void addVertex(OriVertex inputVertex) {
		vertices.add(inputVertex);
	}

	/** Adds a new Arrow to the current diagram step
	 * 
	 * @param inputArrow
	 */
	public void addArrow(OriArrow inputArrow) {
		arrows.add(inputArrow);
	}
	
	/** Adds a new FilledFace to the current diagram step
	 * 
	 * @param inputFace
	 */
	public void addFilledFace(OriFace inputFace) {
		filledFaces.add(inputFace);
	}

	/**
	 * Adds a new Leader to the current diagram step
	 * @param inputLeader
	 */
	public void addLeader(OriLeaderBox inputLeader) {
		leaderBoxSymbols.add(inputLeader);
	}

	/**
	 * Adds a new OriPicSymbol to the current diagram step
	 * @param inputSymbol
	 */
	public void addPicSymbol(OriPicSymbol inputSymbol) {
		picSymbols.add(inputSymbol);
	}

	/**
	 * Adds a new OriGeomSymbol to the current diagram step
	 * @param inputSymbol
	 */
	public void addGeomSymbol(OriGeomSymbol inputSymbol) {
		geomSymbols.add(inputSymbol);
	}

	/**
	 * Adds a new OriEqualDistSymbol to the current diagram step
	 * @param inputSymbol
	 */
	public void addEqualDistSymbol(OriEqualDistSymbol inputSymbol) {
		equalDistSymbols.add(inputSymbol);
	}

	/**
	 * Adds a new OriEqualAngleSymbol to the current diagram step
	 * @param inputSymbol
	 */
	public void addEqualAngleSymbol(OriEqualAnglSymbol inputSymbol) {
		equalAnglSymbols.add(inputSymbol);
	}

	/**
	 * Adds a new OriPleatSymbol to the current diagram step
	 * @param inputSymbol
	 */
	public void addPleatSymbol(OriPleatCrimpSymbol inputSymbol) {
		pleatCrimpSymbols.add(inputSymbol);
	}

	
	/**********************
	 ****  SELECT ALL  ****
	 **********************/
	public void selectAll() {
		selectAllLines();
		selectAllVertices();
		selectAllArrows();
		selectAllFaces();
		selectAllLeaders();
		selectAllPicSymbols();
		selectAllGeomSymbols();
		selectAllEqualDistSymbols();
		selectAllEqualAnglSymbols();
		selectAllPleatSymbols();
	}

	public void selectAllLines() {
		for (OriLine l : lines) {
			l.setSelected(true);
		}
	}

	public void selectAllVertices() {
		for (OriVertex v : vertices) {
			v.setSelected(true);
		}
	}

	public void selectAllArrows() {
		for (OriArrow a : arrows) {
			a.setSelected(true);
		}
	}

	public void selectAllFaces() {
		for (OriArrow a : arrows) {
			a.setSelected(true);
		}
	}
	
	public void selectAllLeaders() {
		for (OriLeaderBox l : leaderBoxSymbols) {
			l.setSelected(true);
		}
	}	

	public void selectAllPicSymbols() {
		for (OriPicSymbol s : picSymbols) {
			s.setSelected(true);
		}
	}

	public void selectAllGeomSymbols() {
		for (OriGeomSymbol s : geomSymbols) {
			s.setSelected(true);
		}
	}

	public void selectAllEqualDistSymbols() {
		for (OriEqualDistSymbol eds : equalDistSymbols) {
			eds.setSelected(true);
		}
	}

	public void selectAllEqualAnglSymbols() {
		for (OriEqualAnglSymbol eas : equalAnglSymbols) {
			eas.setSelected(true);
		}
	}

	public void selectAllPleatSymbols() {
		for (OriPleatCrimpSymbol pleat : pleatCrimpSymbols) {
			pleat.setSelected(true);
		}
	}
	
	
	public int getSelectedObjectsCount() {
		int count = 0;
		for (OriLine l : lines) {
			if (l.isSelected() == true) {
				count++;
			}
		}
		for (OriVertex v : vertices) {
			if (v.isSelected() == true) {
				count++;
			}
		}
		for (OriArrow a : arrows) {
			if (a.isSelected() == true) {
				count++;
			}
		}
		for (OriFace f : filledFaces) {
			if (f.isSelected() == true) {
				count++;
			}
		}
		for (OriLeaderBox lb : leaderBoxSymbols) {
			if (lb.isSelected() == true) {
				count++;
			}
		}
		for (OriPicSymbol ps : picSymbols) {
			if (ps.isSelected() == true) {
				count++;
			}
		}
		for (OriGeomSymbol gs : geomSymbols) {
			if (gs.isSelected() == true) {
				count++;
			}
		}
		for (OriEqualDistSymbol eds : equalDistSymbols) {
			if (eds.isSelected() == true) {
				count++;
			}
		}
		for (OriEqualAnglSymbol eas : equalAnglSymbols) {
			if (eas.isSelected() == true) {
				count++;
			}
		}
		for (OriPleatCrimpSymbol pcs : pleatCrimpSymbols) {
			if (pcs.isSelected() == true) {
				count++;
			}
		}

		return count;
	}

	public void unselectAll() {
		unselectAllLines();
		unselectAllVertices();
		unselectAllArrows();
		unselectAllFaces();
		unselectAllLeaders();
		unselectAllPicSymbols();
		unselectAllGeomSymbols();
		unselectAllEqualDistSymbols();
		unselectAllEqualAnglSymbols();
		unselectAllPleatSymbols();
	}

	public void unselectAllLines() {
		for (OriLine l : lines) {
			l.setSelected(false);
		}
	}

	public void unselectAllVertices() {
		for (OriVertex v : vertices) {
			v.setSelected(false);
		}
	}

	public void unselectAllArrows() {
		for (OriArrow a : arrows) {
			a.setSelected(false);
		}
	}

	public void unselectAllFaces() {
		for (OriFace f : filledFaces) {
			f.setSelected(false);
		}
	}

	public void unselectAllLeaders() {
		for (OriLeaderBox l : leaderBoxSymbols) {
			l.setSelected(false);
		}
	}

	public void unselectAllPicSymbols() {
		for (OriPicSymbol s : picSymbols) {
			s.setSelected(false);
		}
	}

	public void unselectAllGeomSymbols() {
		for (OriGeomSymbol s : geomSymbols) {
			s.setSelected(false);
		}
	}

	public void unselectAllEqualDistSymbols() {
		for (OriEqualDistSymbol eds : equalDistSymbols) {
			eds.setSelected(false);
		}
	}

	public void unselectAllEqualAnglSymbols() {
		for (OriEqualAnglSymbol eas : equalAnglSymbols) {
			eas.setSelected(false);
		}
	}

	public void unselectAllPleatSymbols() {
		for (OriPleatCrimpSymbol pleat : pleatCrimpSymbols) {
			pleat.setSelected(false);
		}
	}
	
	
	/********************************
	 ****  GET SELECTED OBJECTS  ****
	 ********************************/

	
	public ArrayList<OriLine> getSelectedOriLines() {
		ArrayList<OriLine> selectedLines = new ArrayList<>();

		for (OriLine line : lines) {
			if (line.isSelected()) {
				selectedLines.add(line);
			}
		}
		return selectedLines;
	}
	
	public ArrayList<OriVertex> getSelectedVertices() {
		ArrayList<OriVertex> selectedVertices = new ArrayList<>();

		for (OriVertex v : vertices) {
			if (v.isSelected()) {
				selectedVertices.add(v);
			}
		}
		return selectedVertices;
	}
	
	public ArrayList<OriArrow> getSelectedOriArrows() {
		ArrayList<OriArrow> selectedArrows = new ArrayList<>();

		for (OriArrow arrow : arrows) {
			if(arrow.isSelected()) {
				selectedArrows.add(arrow);
			}
		}
		return selectedArrows;
	}
	
	public ArrayList<OriFace> getSelectedFilledFaces() {
		ArrayList<OriFace> selectedFaces = new ArrayList<>();

		for (OriFace face : filledFaces) {
			if (face.isSelected()) {
				selectedFaces.add(face);
			}
		}
		return selectedFaces;
	}
	
	public ArrayList<OriLeaderBox> getSelectedLeaderBoxes() {
		ArrayList<OriLeaderBox> selectedLeader = new ArrayList<>();

		for (OriLeaderBox l : leaderBoxSymbols) {
			if (l.isSelected()) {
				selectedLeader.add(l);
			}
		}
		return selectedLeader;
	}
	
	public ArrayList<OriPicSymbol> getSelectedPicSymbols() {
		ArrayList<OriPicSymbol> selectedPicS = new ArrayList<>();

		for (OriPicSymbol ps : picSymbols) {
			if (ps.isSelected()) {
				selectedPicS.add(ps);
			}
		}
		return selectedPicS;
	}
	
	public ArrayList<OriGeomSymbol> getSelectedGeomSymbols() {
		ArrayList<OriGeomSymbol> selectedGeomS = new ArrayList<>();

		for (OriGeomSymbol gs : geomSymbols) {
			if (gs.isSelected()) {
				selectedGeomS.add(gs);
			}
		}
		return selectedGeomS;
	}
	
	public ArrayList<OriEqualDistSymbol> getSelectedEqualDistSymbols() {
		ArrayList<OriEqualDistSymbol> selectedEqualDistS = new ArrayList<>();

		for (OriEqualDistSymbol eds : equalDistSymbols) {
			if (eds.isSelected()) {
				selectedEqualDistS.add(eds);
			}
		}
		return selectedEqualDistS;
	}
	
	public ArrayList<OriEqualAnglSymbol> getSelectedEqualAnglSymbols() {
		ArrayList<OriEqualAnglSymbol> selectedEqualAnglS = new ArrayList<>();

		for (OriEqualAnglSymbol eas : equalAnglSymbols) {
			if (eas.isSelected()) {
				selectedEqualAnglS.add(eas);
			}
		}
		return selectedEqualAnglS;
	}
	
	public ArrayList<OriPleatCrimpSymbol> getSelectedPleatSymbols() {
		ArrayList<OriPleatCrimpSymbol> selectedPleatS = new ArrayList<>();

		for (OriPleatCrimpSymbol pleat : pleatCrimpSymbols) {
			if (pleat.isSelected()) {
				selectedPleatS.add(pleat);
			}
		}
		return selectedPleatS;
	}


	public void deleteAllSelectedObjects() {
		deleteSelectedLines();
		deleteSelectedVertices();
		deleteSelectedArrows();
		deleteSelectedFaces();
		deleteSelectedLeaderBoxes();
		deleteSelectedPicSymbols();
		deleteSelectedGeomSymbols();
		deleteSelectedEqualDistSymbols();
		deleteSelectedEqualAnglSymbols();
		deleteSelectedPleatSymbols();
	}

	/**
	 * Deletes all selected lines of the current diagram step
	 */
	public void deleteSelectedLines() {
		ArrayList<OriLine> selectedLines = getSelectedOriLines();

		if (selectedLines.size() != 0) {
			Origrammer.diagram.steps.get(Globals.currentStep).pushUndoInfo();
			for (OriLine line : selectedLines) {
				lines.remove(line);
			}
		}
	}

	/**
	 * Deletes all selected vertices of the current diagram step
	 */
	public void deleteSelectedVertices() {
		ArrayList<OriVertex> selectedVertices = getSelectedVertices();

		if (selectedVertices.size() != 0) {
			Origrammer.diagram.steps.get(Globals.currentStep).pushUndoInfo();
			for (OriVertex v : selectedVertices) {
				vertices.remove(v);
			}
		}
	}

	/**
	 * Deletes all selected arrows of the current diagram step
	 */
	public void deleteSelectedArrows() {
		ArrayList<OriArrow> selectedArrows = getSelectedOriArrows();

		if (selectedArrows.size() != 0) {
			Origrammer.diagram.steps.get(Globals.currentStep).pushUndoInfo();
			for (OriArrow arrow : selectedArrows) {
				arrows.remove(arrow);
			}
		}
	}

	/**
	 * Deletes all selected faces of the current diagram step
	 */
	public void deleteSelectedFaces() {
		ArrayList<OriFace> selectedFaces = getSelectedFilledFaces();
		
		if (selectedFaces.size() != 0) {
			Origrammer.diagram.steps.get(Globals.currentStep).pushUndoInfo();
			for (OriFace face : selectedFaces)  {
				filledFaces.remove(face);
			}
		}
	}

	/**
	 * Deletes all selected leaders of the current diagram step
	 */
	public void deleteSelectedLeaderBoxes() {
		ArrayList<OriLeaderBox> selectedLeader = getSelectedLeaderBoxes();
		
		if (selectedLeader.size() != 0) {
			Origrammer.diagram.steps.get(Globals.currentStep).pushUndoInfo();
			for (OriLeaderBox l : selectedLeader) {
				leaderBoxSymbols.remove(l);
			}
		}
	}

	/**
	 * Deletes all selected OriPicSymbols of the current diagram step
	 */
	public void deleteSelectedPicSymbols() {
		ArrayList<OriPicSymbol> selectedPicS = getSelectedPicSymbols();

		if (selectedPicS.size() != 0) {
			Origrammer.diagram.steps.get(Globals.currentStep).pushUndoInfo();
			for (OriPicSymbol ps : selectedPicS) {
				picSymbols.remove(ps);
			}
		}
	}

	/**
	 * Deletes all selected OriGeomSymbols of the current diagram step
	 */
	public void deleteSelectedGeomSymbols() {
		ArrayList<OriGeomSymbol> selectedGeomS = getSelectedGeomSymbols();

		if (selectedGeomS.size() != 0) {
			Origrammer.diagram.steps.get(Globals.currentStep).pushUndoInfo();
			for (OriGeomSymbol gs : selectedGeomS) {
				geomSymbols.remove(gs);
			}
		}
	}

	/**
	 * Deletes all selected OriEqualDistSymbols of the current diagram step
	 */
	public void deleteSelectedEqualDistSymbols() {
		ArrayList<OriEqualDistSymbol> selectedEqualDistS = getSelectedEqualDistSymbols();

		if (selectedEqualDistS.size() != 0) {
			Origrammer.diagram.steps.get(Globals.currentStep).pushUndoInfo();
			for (OriEqualDistSymbol eds : selectedEqualDistS) {
				equalDistSymbols.remove(eds);
			}
		}
	}

	/**
	 * Deletes all selected OriEqualAnglSymbols of the current diagram step
	 */
	public void deleteSelectedEqualAnglSymbols() {
		ArrayList<OriEqualAnglSymbol> selectedEqualAnglS = getSelectedEqualAnglSymbols();

		if (selectedEqualAnglS.size() != 0) {
			Origrammer.diagram.steps.get(Globals.currentStep).pushUndoInfo();
			for (OriEqualAnglSymbol eas : selectedEqualAnglS) {
				equalAnglSymbols.remove(eas);
			}
		}
	}

	/**
	 * Deletes all selected OriPleatSymbols of the current diagram step
	 */
	public void deleteSelectedPleatSymbols() {
		ArrayList<OriPleatCrimpSymbol> selectedPleatS = getSelectedPleatSymbols();

		if (selectedPleatS.size() != 0) {
			Origrammer.diagram.steps.get(Globals.currentStep).pushUndoInfo();
			for (OriPleatCrimpSymbol pleat : selectedPleatS) {
				pleatCrimpSymbols.remove(pleat);
			}	
		}
	}

	public String getStepDescription() {
		return stepDescription;
	}

	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}

	public int getStepNumber() {
		return stepNumber;
	}

	public void setStepNumber(int stepNumber) {
		this.stepNumber = stepNumber;
	}

	@Override
	public String toString() {
		return "Step [lines=" + lines + ", vertices=" + vertices + ", arrows=" + arrows
				+ ", stepDescription=" + stepDescription + ", stepNumber=" + stepNumber + "]";
	}

}
