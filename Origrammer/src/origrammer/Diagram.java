package origrammer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
		
		for(OriLine line : tmpLines) {
			if(GeometryUtil.isSameLineSegment(line, inputLine)) {
				return;
			}
		}

		for (OriLine line : tmpLines) {
			if (inputLine.type == OriLine.TYPE_NONE && line.type != OriLine.TYPE_NONE) {
				continue;
			}
			Vector2d crossPoint = GeometryUtil.getCrossPoint(inputLine, line);
			if(crossPoint == null) {
				continue;
			}

			crossingLines.add(line);
			lines.remove(line);

			if(GeometryUtil.Distance(line.p0,  crossPoint) > POINT_EPS) {
				lines.add(new OriLine(line.p0, crossPoint, line.type));
			}
			if(GeometryUtil.Distance(line.p1, crossPoint) > POINT_EPS) {
				lines.add(new OriLine(line.p1, crossPoint, line.type));
			}
		}

		ArrayList<Vector2d> points = new ArrayList<>();
		points.add(inputLine.p0);
		points.add(inputLine.p1);

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
			
			lines.add(new OriLine(prePoint, p, inputLine.type));
			prePoint = p;
		}
		System.out.println("Vertex size: " + vertices.size());
		for(int i = 0; i<vertices.size(); i++) {
			System.out.println("VERTICES: " + vertices.get(i).toString());
		}
	}




}
