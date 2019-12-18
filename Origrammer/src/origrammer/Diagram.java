package origrammer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.ImageIcon;
import javax.vecmath.Vector2d;

import origrammer.geometry.GeometryUtil;
import origrammer.geometry.OriLine;
import origrammer.geometry.OriVertex;

class PointComparatorX implements Comparator {
	@Override
	public int compare(Object v1, Object v2) {
		return ((Vector2d) v1).x > ((Vector2d) v2).x ? 1 : -1;
	}
}

class PointComparatorY implements Comparator {
	@Override
	public int compare(Object v1, Object v2) {
		return ((Vector2d) v1).x > ((Vector2d) v2).y ? 1 : -1;
	}
}




public class Diagram {
	
	public ArrayList<OriLine> crossLines = new ArrayList<>();
	public ArrayList<OriLine> lines = new ArrayList<>();
	public ArrayList<OriVertex> vertices = new ArrayList<>();
	public ArrayList<OriArrow> arrows = new ArrayList<>();
	
	public static final double POINT_EPS = 1.0;
	
	public double size;
	
	public Diagram(double size) {
		this.size = size;
		
		OriLine l0 = new OriLine(-size/2.0, size/2.0, size/2.0,size/2.0, OriLine.TYPE_EDGE);
		OriLine l1 = new OriLine(size/2.0, size/2.0, size/2.0,-size/2.0, OriLine.TYPE_EDGE);
		OriLine l2 = new OriLine(size/2.0, -size/2.0, -size/2.0,-size/2.0, OriLine.TYPE_EDGE);
		OriLine l3 = new OriLine(-size/2.0, -size/2.0, -size/2.0,size/2.0, OriLine.TYPE_EDGE);
		lines.add(l0);
		lines.add(l1);
		lines.add(l2);
		lines.add(l3);
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
		for(OriLine line : tmpLines) {
			if(GeometryUtil.isSameLineSegment(line, inputLine)) {
				return;
			}
		}

		//if new line crosses another one, split them up to smaller lines
		for (OriLine line : tmpLines) {
			if (inputLine.getType() == OriLine.TYPE_NONE && line.getType() != OriLine.TYPE_NONE) {
				continue;
			}
			Vector2d crossPoint = GeometryUtil.getCrossPoint(inputLine, line);
			if(crossPoint == null) {
				continue;
			}

			crossingLines.add(line);
			lines.remove(line);

			if(GeometryUtil.Distance(line.p0,  crossPoint) > POINT_EPS) {
				lines.add(new OriLine(line.p0, crossPoint, line.getType()));
			}
			if(GeometryUtil.Distance(line.p1, crossPoint) > POINT_EPS) {
				lines.add(new OriLine(line.p1, crossPoint, line.getType()));
			}
		}

		ArrayList<Vector2d> points = new ArrayList<>();
		points.add(inputLine.p0);
		points.add(inputLine.p1);

		//if the intersection is really close to the end of line --> do nothing
		for(OriLine line : lines) {
			if(GeometryUtil.Distance(inputLine.p0,  line.p0) < POINT_EPS) {
				continue;
			}
			if(GeometryUtil.Distance(inputLine.p0,  line.p1) < POINT_EPS) {
				continue;
			}
			if(GeometryUtil.Distance(inputLine.p1,  line.p0) < POINT_EPS) {
				continue;
			}
			if(GeometryUtil.Distance(inputLine.p1,  line.p1) < POINT_EPS) {
				continue;
			}
			if(GeometryUtil.DistancePointToSegment(line.p0, inputLine.p0, inputLine.p1) < POINT_EPS) {
				points.add(line.p0);
			}
			if(GeometryUtil.DistancePointToSegment(line.p0, inputLine.p0, inputLine.p1) < POINT_EPS) {
				points.add(line.p1);
			}

			Vector2d crossPoint = GeometryUtil.getCrossPoint(inputLine, line);
			if(crossPoint != null) {
				points.add(crossPoint);
			}
		}

		boolean sortByX = Math.abs(inputLine.p0.x - inputLine.p1.x) > Math.abs(inputLine.p0.y - inputLine.p1.y);
		if (sortByX) {
			Collections.sort(points, new PointComparatorX());
		} else {
			Collections.sort(points, new PointComparatorY());
		}

		Vector2d prePoint = points.get(0);

		for (int i = 1; i < points.size(); i++) {
			Vector2d p = points.get(i);
			if(GeometryUtil.Distance(prePoint, p) < POINT_EPS) {
				continue;
			}
			
			lines.add(new OriLine(prePoint, p, inputLine.getType()));
			prePoint = p;
		}
	}


	/** Adds a new Arrow to the diagram
	 * 
	 * @param inputArrow
	 */
	public void addArrow(OriArrow inputArrow) {
		arrows.add(new OriArrow(inputArrow));
	}
	
	public void selectAll() {
		selectAllLines();
		selectAllArrows();
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

	public void unselectAll() {
		unselectAllLines();
		unselectAllArrows();
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

	/**
	 * Deletes all selected lines of the current diagram step
	 */
	public void deleteSelectedLines() {
		ArrayList<OriLine> selectedLines = new ArrayList<>();
		
		for (OriLine line : lines) {
			if(line.isSelected()) {
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

	public void addTriangleInsectorLines(Vector2d v0, Vector2d v1, Vector2d v2) {
		Vector2d incenter = GeometryUtil.getIncenter(v0,v1,v2);
		if(incenter == null) {
			System.out.println("Failed to calculate the incenter of the triangle");
		}
		
		addLine(new OriLine(incenter, v0, Globals.inputLineType));
		addLine(new OriLine(incenter, v1, Globals.inputLineType));
		addLine(new OriLine(incenter, v2, Globals.inputLineType));

	}

}
