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
		return ((Vector2d) v1).x > ((Vector2d) v2).x ? 1 : -1;
	}
}

class PointComparatorY implements Comparator<Object> {
	@Override
	public int compare(Object v1, Object v2) {
		return ((Vector2d) v1).x > ((Vector2d) v2).y ? 1 : -1;
	}
}


public class Step {
	
	public ArrayList<OriLine> crossLines = new ArrayList<>();
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
	
	public static final double POINT_EPS = 1.0;
	
	public double size = Constants.DEFAULT_PAPER_SIZE;

	public Step() {
		if (Globals.newStepOptions == Constants.NewStepOptions.PASTE_DEFAULT_PAPER) {
			initFirstStep();
		}
		stepNumber = Globals.currentStep;
	}
	
	public void initFirstStep() {
		
		if (Globals.paperShape == Constants.PaperShape.SQUARE) {
			//TODO: this is setup for default square paper --> todo for different shapes like octagonal etc.
			OriLine l0 = new OriLine(-size/2.0, size/2.0, size/2.0, size/2.0, OriLine.TYPE_EDGE);
			OriLine l1 = new OriLine(size/2.0, size/2.0, size/2.0, -size/2.0, OriLine.TYPE_EDGE);
			OriLine l2 = new OriLine(size/2.0, -size/2.0, -size/2.0, -size/2.0, OriLine.TYPE_EDGE);
			OriLine l3 = new OriLine(-size/2.0, -size/2.0, -size/2.0, size/2.0, OriLine.TYPE_EDGE);
			lines.add(l0);
			lines.add(l1);
			lines.add(l2);
			lines.add(l3);
		} else if (Globals.paperShape == Constants.PaperShape.RECTANGLE) {
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
			lines.add(l0);
			lines.add(l1);
			lines.add(l2);
			lines.add(l3);
			
		}

	}
	
	/**Adds a new OriLine and checks for intersections with others
	 * 
	 * @param inputLine
	 */
	public void addLine(OriLine inputLine) {
		ArrayList<OriLine> crossingLines = new ArrayList<>();
		ArrayList<OriLine> tmpLines = new ArrayList<>();
		tmpLines.addAll(lines);
		
		//check if the line already exists
		for (OriLine line : tmpLines) {
			if (GeometryUtil.isSameLineSegment(line, inputLine)) {
				return;
			}
		}

		//if new line crosses another one, split them up to smaller lines
		for (OriLine line : tmpLines) {
			if (inputLine.getType() == OriLine.TYPE_NONE && line.getType() != OriLine.TYPE_NONE) {
				continue;
			}
			Vector2d crossPoint = GeometryUtil.getCrossPoint(inputLine, line);
			if (crossPoint == null) {
				continue;
			}

			crossingLines.add(line);
			lines.remove(line);

			if (GeometryUtil.Distance(line.getP0(),  crossPoint) > POINT_EPS) {
				lines.add(new OriLine(line.getP0(), crossPoint, line.getType()));
			}
			if (GeometryUtil.Distance(line.getP1(), crossPoint) > POINT_EPS) {
				lines.add(new OriLine(line.getP1(), crossPoint, line.getType()));
			}
		}

		ArrayList<Vector2d> points = new ArrayList<>();
		points.add(inputLine.getP0());
		points.add(inputLine.getP1());

		//if the intersection is really close to the end of line --> do nothing
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
			if (GeometryUtil.DistancePointToSegment(line.getP0(), inputLine.getP0(), inputLine.getP1()) < POINT_EPS) {
				points.add(line.getP1());
			}

			Vector2d crossPoint = GeometryUtil.getCrossPoint(inputLine, line);
			if (crossPoint != null) {
				points.add(crossPoint);
			}
		}

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
			
			lines.add(new OriLine(prePoint, p, inputLine.getType()));
			prePoint = p;
		}
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
	public void deleteSelectedLeaders() {
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
	

	public void addTriangleInsectorLines(Vector2d v0, Vector2d v1, Vector2d v2) {
		Vector2d incenter = GeometryUtil.getIncenter(v0,v1,v2);
		if (incenter == null) {
			System.out.println("Failed to calculate the incenter of the triangle");
		}
		
		addLine(new OriLine(incenter, v0, Globals.inputLineType));
		addLine(new OriLine(incenter, v1, Globals.inputLineType));
		addLine(new OriLine(incenter, v2, Globals.inputLineType));

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
		return "Step [crossLines=" + crossLines + ", lines=" + lines + ", vertices=" + vertices + ", arrows=" + arrows
				+ ", stepDescription=" + stepDescription + ", stepNumber=" + stepNumber + "]";
	}
	
}
