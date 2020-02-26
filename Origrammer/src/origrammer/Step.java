package origrammer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
		
		OriLine l0 = new OriLine(-size/2.0, size/2.0*ratio, size/2.0,  size/2.0*ratio, OriLine.TYPE_EDGE);
		OriLine l1 = new OriLine(size/2.0, size/2.0*ratio, size/2.0,-size/2.0*ratio, OriLine.TYPE_EDGE);
		OriLine l2 = new OriLine(size/2.0, -size/2.0*ratio, -size/2.0,-size/2.0*ratio, OriLine.TYPE_EDGE);
		OriLine l3 = new OriLine(-size/2.0, -size/2.0*ratio, -size/2.0,size/2.0*ratio, OriLine.TYPE_EDGE);
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
		System.out.println("PRE: " + inputLine.toString());
		ArrayList<OriLine> crossingLines = new ArrayList<>();
		ArrayList<OriLine> tmpLines = new ArrayList<>();
		tmpLines.addAll(lines);
		
		//check if line already exists
		for (OriLine l : tmpLines) {
			if (GeometryUtil.isSameLineSegment(l, inputLine)) {
				return;
			}
		}
		
		//if new line crosses another one, split them up to smaller lines
		for (OriLine l : tmpLines) {
			Vector2d crossPoint = GeometryUtil.getCrossPoint(l, inputLine);
			if (crossPoint == null) {
				continue;
			}
			crossingLines.add(l);
			lines.remove(l);
			
			//splitting existing line (OriLine l) at crossPoint with new inputLine
			if (l.getP0().x < l.getP1().x || ((l.getP0().x == l.getP1().x) && (l.getP0().y < l.getP1().y))) {				
				if (GeometryUtil.Distance(l.getP0(), crossPoint) > POINT_EPS) {
					lines.add(new OriLine(l.getP0(), crossPoint, l.getType(), l.isStartTransl(), false));
				}
				if (GeometryUtil.Distance(crossPoint, l.getP1()) > POINT_EPS) {
					lines.add(new OriLine(crossPoint, l.getP1(), l.getType(), false, l.isEndTransl()));
				}
				
				
			} else if (l.getP1().x < l.getP0().x || ((l.getP1().x == l.getP1().x) && (l.getP1().y < l.getP0().y))) {				
				if (GeometryUtil.Distance(l.getP1(), crossPoint) > POINT_EPS) {
					lines.add(new OriLine(l.getP1(), crossPoint, l.getType(), l.isStartTransl(), false));
				}
				if (GeometryUtil.Distance(crossPoint, l.getP0()) > POINT_EPS) {
					lines.add(new OriLine(crossPoint, l.getP0(), l.getType(), false, l.isEndTransl()));
				}
			}
		}
			
		//points contains p0 and p1 of inputLine
		ArrayList<Vector2d> points = new ArrayList<>();
		points.add(inputLine.getP0());
		points.add(inputLine.getP1());

		//if inputLine.p0 or inputLine.p1 is really close to either line.p0 or line.p1 --> do nothing
		//if distance between inputLine.p0 and line or between inputLine.p1 and line
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
		
		//how to sort ArrayList<Vector2d> points
		boolean sortByX = Math.abs(inputLine.getP0().x - inputLine.getP1().x) > Math.abs(inputLine.getP0().y - inputLine.getP1().y);
		if (sortByX) {
			Collections.sort(points, new PointComparatorX());
		} else {
			Collections.sort(points, new PointComparatorY());
		}
		

		Vector2d prePoint = points.get(0);

		for (int i = 1; i < points.size(); i++) {
			Vector2d p = points.get(i);
			if (GeometryUtil.Distance(prePoint, p) < POINT_EPS) {
				continue;
			}
			
			if (prePoint.x < p.x || ((prePoint.x == p.x) && (prePoint.y < p.y))) {
				lines.add(new OriLine(prePoint, p, inputLine.getType(), inputLine.isStartTransl(), inputLine.isEndTransl()));

			} else if (p.x < prePoint.x || ((p.x == p.x) && (p.y < prePoint.y))) {
				lines.add(new OriLine(prePoint, p, inputLine.getType(), inputLine.isStartTransl(), inputLine.isEndTransl()));
			}
			//lines.add(new OriLine(prePoint, p, inputLine.getType(), inputLine.isStartTransl(), inputLine.isEndTransl()));
			prePoint = p;
		}
	}
//	public void addLine(OriLine inputLine) {
//		ArrayList<OriLine> crossingLines = new ArrayList<>();
//		ArrayList<OriLine> tmpLines = new ArrayList<>();
//		tmpLines.addAll(lines);
//		
//		//check if the line already exists
//		for (OriLine line : tmpLines) {
//			if (GeometryUtil.isSameLineSegment(line, inputLine)) {
//				return;
//			}
//		}
//
//		//if new line crosses another one, split them up to smaller lines
//		for (OriLine line : tmpLines) {
//			if (inputLine.getType() == OriLine.TYPE_NONE && line.getType() != OriLine.TYPE_NONE) {
//				continue;
//			}
//			Vector2d crossPoint = GeometryUtil.getCrossPoint(inputLine, line);
//			if (crossPoint == null) {
//				continue;
//			}
//
//			crossingLines.add(line);
//			lines.remove(line);
//
//			if (GeometryUtil.Distance(line.getP0(), crossPoint) > POINT_EPS) {
//				lines.add(new OriLine(line.getP0(), crossPoint, line.getType(), line.isStartTransl(), false));
//			}
//			if (GeometryUtil.Distance(line.getP1(), crossPoint) > POINT_EPS) {
//				lines.add(new OriLine(line.getP1(), crossPoint, line.getType(), false, line.isEndTransl()));
//			}
//		}
//
//		ArrayList<Vector2d> points = new ArrayList<>();
//		points.add(inputLine.getP0());
//		points.add(inputLine.getP1());
//
//		//if the intersection is really close to the end of line --> do nothing
//		for (OriLine line : lines) {
//			if (GeometryUtil.Distance(inputLine.getP0(),  line.getP0()) < POINT_EPS) {
//				continue;
//			}
//			if (GeometryUtil.Distance(inputLine.getP0(),  line.getP1()) < POINT_EPS) {
//				continue;
//			}
//			if (GeometryUtil.Distance(inputLine.getP1(),  line.getP0()) < POINT_EPS) {
//				continue;
//			}
//			if (GeometryUtil.Distance(inputLine.getP1(),  line.getP1()) < POINT_EPS) {
//				continue;
//			}
//			if (GeometryUtil.DistancePointToSegment(line.getP0(), inputLine.getP0(), inputLine.getP1()) < POINT_EPS) {
//				points.add(line.getP0());
//			}
//			if (GeometryUtil.DistancePointToSegment(line.getP0(), inputLine.getP0(), inputLine.getP1()) < POINT_EPS) {
//				points.add(line.getP1());
//			}
//
//			Vector2d crossPoint = GeometryUtil.getCrossPoint(inputLine, line);
//			if (crossPoint != null) {
//				points.add(crossPoint);
//			}
//		}
//		boolean sortByX = Math.abs(inputLine.getP0().x - inputLine.getP1().x) > Math.abs(inputLine.getP0().y - inputLine.getP1().y);
//		if (sortByX) {
//			Collections.sort(points, new PointComparatorX());
//		} else {
//			Collections.sort(points, new PointComparatorY());
//		}
//
//		Vector2d prePoint = points.get(0);
//
//		for (int i = 1; i < points.size(); i++) {
//			Vector2d p = points.get(i);
//			if (GeometryUtil.Distance(prePoint, p) < POINT_EPS) {
//				continue;
//			}
//			
//			lines.add(new OriLine(prePoint, p, inputLine.getType(), inputLine.isStartTransl(), inputLine.isEndTransl()));
//			prePoint = p;
//		}
//	}
	
	public void addTriangleInsectorLines(Vector2d v0, Vector2d v1, Vector2d v2) {
		Vector2d incenter = GeometryUtil.getIncenter(v0,v1,v2);
		if (incenter == null) {
			System.out.println("Failed to calculate the incenter of the triangle");
		}
		
		addLine(new OriLine(incenter, v0, Globals.inputLineType));
		addLine(new OriLine(incenter, v1, Globals.inputLineType));
		addLine(new OriLine(incenter, v2, Globals.inputLineType));

	}
	
	/**
	 * Adds a new Vertex to the current diagram step
	 * @param inputVertex
	 */
	public void addVertex(Vector2d inputVertex) {
		vertices.add(new OriVertex(inputVertex));
	}


	/** Adds a new Arrow to the current diagram step
	 * 
	 * @param inputArrow
	 */
	public void addArrow(OriArrow inputArrow) {
		arrows.add(inputArrow);
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
		ArrayList<OriLine> selectedLines = new ArrayList<>();
		
		for (OriLine line : lines) {
			if (line.isSelected()) {
				selectedLines.add(line);
			}
		}
		
		for (OriLine line : selectedLines) {
			lines.remove(line);
		}
	}
	
	/**
	 * Deletes all selected vertices of the current diagram step
	 */
	public void deleteSelectedVertices() {
		ArrayList<OriVertex> selectedVertices = new ArrayList<>();
		
		for (OriVertex v : vertices) {
			if (v.isSelected()) {
				selectedVertices.add(v);
			}
		}
		
		for (OriVertex v : selectedVertices) {
			vertices.remove(v);
		}
	}
 	
	
	/**
	 * Deletes all selected arrows of the current diagram step
	 */
	public void deleteSelectedArrows() {
		ArrayList<OriArrow> selectedArrows = new ArrayList<>();
		
		for (OriArrow arrow : arrows) {
			if(arrow.isSelected()) {
				selectedArrows.add(arrow);
			}
		}
		
		for (OriArrow arrow : selectedArrows) {
			arrows.remove(arrow);
		}
	}
	
	/**
	 * Deletes all selected faces of the current diagram step
	 */
	public void deleteSelectedFaces() {
		ArrayList<OriFace> selectedFaces = new ArrayList<>();
		
		for (OriFace face : filledFaces) {
			if (face.isSelected()) {
				selectedFaces.add(face);
			}
		}
		for (OriFace face : selectedFaces)  {
			filledFaces.remove(face);
		}
	}
	
	/**
	 * Deletes all selected leaders of the current diagram step
	 */
	public void deleteSelectedLeaderBoxes() {
		ArrayList<OriLeaderBox> selectedLeader = new ArrayList<>();
		
		for (OriLeaderBox l : leaderBoxSymbols) {
			if (l.isSelected()) {
				selectedLeader.add(l);
			}
		}
		for (OriLeaderBox l : selectedLeader) {
			leaderBoxSymbols.remove(l);
		}
	}
	
	/**
	 * Deletes all selected OriPicSymbols of the current diagram step
	 */
	public void deleteSelectedPicSymbols() {
		ArrayList<OriPicSymbol> selectedPicS = new ArrayList<>();
		
		for (OriPicSymbol ps : picSymbols) {
			if (ps.isSelected()) {
				selectedPicS.add(ps);
			}
		}
		for (OriPicSymbol ps : selectedPicS) {
			picSymbols.remove(ps);
		}
	}
	
	/**
	 * Deletes all selected OriGeomSymbols of the current diagram step
	 */
	public void deleteSelectedGeomSymbols() {
		ArrayList<OriGeomSymbol> selectedGeomS = new ArrayList<>();
		
		for (OriGeomSymbol gs : geomSymbols) {
			if (gs.isSelected()) {
				selectedGeomS.add(gs);
			}
		}
		for (OriGeomSymbol gs : selectedGeomS) {
			geomSymbols.remove(gs);
		}
	}
	
	/**
	 * Deletes all selected OriEqualDistSymbols of the current diagram step
	 */
	public void deleteSelectedEqualDistSymbols() {
		ArrayList<OriEqualDistSymbol> selectedEqualDistS = new ArrayList<>();
		
		for (OriEqualDistSymbol eds : equalDistSymbols) {
			if (eds.isSelected()) {
				selectedEqualDistS.add(eds);
			}
		}
		for (OriEqualDistSymbol eds : selectedEqualDistS) {
			equalDistSymbols.remove(eds);
		}
	}
	
	/**
	 * Deletes all selected OriEqualAnglSymbols of the current diagram step
	 */
	public void deleteSelectedEqualAnglSymbols() {
		ArrayList<OriEqualAnglSymbol> selectedEqualAnglS = new ArrayList<>();
		
		for (OriEqualAnglSymbol eas : equalAnglSymbols) {
			if (eas.isSelected()) {
				selectedEqualAnglS.add(eas);
			}
		}
		for (OriEqualAnglSymbol eas : selectedEqualAnglS) {
			equalAnglSymbols.remove(eas);
		}
	}
	
	/**
	 * Deletes all selected OriPleatSymbols of the current diagram step
	 */
	public void deleteSelectedPleatSymbols() {
		ArrayList<OriPleatCrimpSymbol> selectedPleatS = new ArrayList<>();
		
		for (OriPleatCrimpSymbol pleat : pleatCrimpSymbols) {
			if (pleat.isSelected()) {
				selectedPleatS.add(pleat);
			}
		}
		for (OriPleatCrimpSymbol pleat : selectedPleatS) {
			pleatCrimpSymbols.remove(pleat);
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
