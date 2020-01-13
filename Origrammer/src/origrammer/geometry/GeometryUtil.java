package origrammer.geometry;

import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.vecmath.Vector2d;

import origrammer.Globals;
import origrammer.OriArrow;
import origrammer.Origrammer;

public class GeometryUtil {

	public final static double POINT_SNAP_VALUE = 0.00001f;
	
	public static double Distance(Vector2d p0, Vector2d p1) {
		return Distance(p0.x, p0.y, p1.x, p1.y);
	}
	
	
	/**   __________________
	 *  \/(x0-x1)²+(y0-y1)²
	 */
	private static double Distance(double x0, double y0, double x1, double y1) {
        return Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));

	}
	
	
	
	public static boolean isSameLineSegment(OriLine l0, OriLine l1) {
		if (Distance(l0.p0, l1.p0) < POINT_SNAP_VALUE && Distance(l0.p1, l1.p1) < POINT_SNAP_VALUE) {
			return true;
		} if (Distance(l0.p0, l1.p1) < POINT_SNAP_VALUE && Distance(l0.p1, l1.p0) < POINT_SNAP_VALUE) {
			return true;
		}
		return false;
	}
	
	
	public static Vector2d getCrossPoint(OriLine l0, OriLine l1) {
		double epsilon = 1.0e-6;
		Vector2d p0 = new Vector2d(l0.p0);
		Vector2d p1 = new Vector2d(l0.p1);
		
		Vector2d d0 = new Vector2d(p1.x - p0.x, p1.y - p0.y);
		Vector2d d1 = new Vector2d(l1.p1.x - l1.p0.x, l1.p1.y - l1.p0.y);
		Vector2d diff = new Vector2d(l1.p0.x - p0.x, l1.p0.y - p0.y);
		double det = d1.x * d0.y - d1.y * d0.x;
		
		if (det * det > epsilon * d0.lengthSquared() * d1.lengthSquared()) {
			double invDet = 1.0 / det;
			double s = (d1.x * diff.y - d1.y * diff.x) * invDet;
			double t = (d0.x * diff.y - d0.y * diff.x) * invDet;
			
			if(t < 0.0 - epsilon || t > 1.0 + epsilon) {
				return null;
			} else if (s < 0.0 - epsilon || s > 1.0 + epsilon) {
				return null;
			} else {
				Vector2d cp = new Vector2d();
				cp.x = (1.0 - t) * l1.p0.x + t * l1.p1.x;
				cp.y = (1.0 - t) * l1.p0.y + t * l1.p1.y;
				return cp;
			}
		}
		return null;
	}
	
	public static double DistancePointToSegment(Vector2d p, Vector2d sp, Vector2d ep) {
		double x0 = sp.x;
		double y0 = sp.y;
		double x1 = ep.x;
		double y1 = ep.y;
		double px = p.x;
		double py = p.y;
		Vector2d sub0, sub, sub0b;
		
		sub0 = new Vector2d(x0 - px, y0 - py);
		sub0b = new Vector2d(-sub0.x, -sub0.y);
		sub = new Vector2d(x1 - x0, y1 - y0);
		
		double t = ((sub.x * sub0b.x) + (sub.y * sub0b.y)) 
				/ ((sub.x * sub.x) + (sub.y * sub.y));
		
		if(t < 0.0) {
			return Distance(px, py, x0, y0);
		} else if (t > 1.0) {
			return Distance(px, py, x1, y1);
		} else {
			return Distance(x0 + t * sub.x, y0 + t * sub.y, px, py);
		}
	}
	
	public static double DistancePointToSegment(Vector2d p, Vector2d sp, Vector2d ep, Vector2d nearestPoint) {
		double x0 = sp.x;
		double y0 = sp.y;
		double x1 = ep.x;
		double y1 = ep.y;
		double px = p.x;
		double py =  p.y;
		Vector2d sub0, sub, sub0b;
		
		sub0 = new Vector2d(x0 - px, y0 - py);
		sub0b = new Vector2d(-sub0.x, -sub0.y);
		sub = new Vector2d(x1 - x0, y1 - y0);
		
		double t = ((sub.x * sub0b.x) + (sub.y * sub0b.y))
				/ ((sub.x * sub.x) + (sub.y * sub.y));
		
		if (t < 0.0) {
			nearestPoint.set(sp);
			return Distance(px, py, x0, y0);
		} else if ( t > 1.0) {
			nearestPoint.set(ep);
			return Distance(px, py, x1, y1);
		} else {
			nearestPoint.set(x0 + t * sub.x, y0 + t * sub.y);
			return Distance(x0 + t * sub.x, y0 + t * sub.y, px, py);
		}
	}
	
	/**
	 * Returns true if mouseCoordinates are within the bounds of the tested OriArrow
	 * @param x Mouse position 
	 * @param y Mouse position
	 * @param a OriArrow that is tested for selection
	 * @return returns true if mouse is over OriArrow and false if it isn't
	 */
	public static boolean isMouseOverArrow(double x, double y, OriArrow a) {
		
		double aX = a.getxPos();
		double aY = a.getyPos();
		double aXEnd = aX + (a.getWidth());
		double aYEnd = aY + (a.getHeight());
		
		if(x>aX && x <aXEnd) {
			if(y>aY && y<aYEnd) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isMouseOverFace(double x, double y, OriFace f) {
		
		System.out.println("X=" + x + "Y=" + y + " | Path=" + f.toString());
		
		return f.path.intersects(x, y, 5, 5);
	}


	/**
	 * [a,b,c] = Distance(
	 * 
	 * 		            (ax1 + bx2 + cx3   ay1 + by2 + cy3)
	 * 	Incenter(x,y) = (--------------- , ---------------)
	 *                  (   a + b + c         a + b + c   )
	 * 
	 * @param v0 Vertex of the triangle
	 * @param v1 Vertex of the triangle
	 * @param v2 Vertex of the triangle
	 * @return returns the triangle incenter as Vector2d
	 */
	public static Vector2d getIncenter(Vector2d v0, Vector2d v1, Vector2d v2) {
		double l0 = Distance(v1, v2);
		double l1 = Distance(v0, v2);
		double l2 = Distance(v0, v1);
		
		Vector2d vc = new Vector2d();
		vc.x = (v0.x * l0 + v1.x * l1 + v2.x * l2) / (l0 + l1 + l2);
		vc.y = (v0.y * l0 + v1.y * l1 + v2.y * l2) / (l0 + l1 + l2);

		return vc;
	}
	
	
	/**
	 * 		    (b² + c² - a²)		a = Distance(C, B)
	 * cos(A) = --------------		b = Distance(A, C)
	 * 			     2bc			c = Distance(A, B)
	 * 
	 * @param v0
	 * @param v1
	 * @param v2
	 * @return returns the angle that v0 is the vertex of
	 */
	public static double measureAngle(Vector2d v0, Vector2d v1, Vector2d v2) {
		double angle = 0;
		double a = Distance(v2, v1);
		double b = Distance(v0, v2);
		double c = Distance(v0, v1);
		double cosA = (Math.pow(b, 2) + Math.pow(c, 2) - Math.pow(a, 2)) / (2*b*c);
		angle = Math.toDegrees(Math.acos(cosA));
		return angle;
	}
	
	
	public static double measureAngle(OriLine l0, OriLine l1) {
		
		Vector2d l0p0 = l0.p0;
		Vector2d l0p1 = l0.p1;
		Vector2d l1p0 = l1.p0;
		Vector2d l1p1 = l1.p1;
		
		Vector2d angleVertex;
		Vector2d v0;
		Vector2d v1;
		
		if (l0p0 == l1p0) {
			angleVertex = l0p0;
			v0 = l0p1;
			v1 = l1p1;
		} else if (l0p0 == l1p1) {
			angleVertex = l0p0;
			v0 = l0p1;
			v1 = l1p0;
		} else if (l0p1 == l1p0) {
			angleVertex = l0p1;
			v0 = l0p0;
			v1 = l1p1;
		} else {
			angleVertex = l0p1;
			v0 = l0p0;
			v1 = l1p0;
		}
		
		
		return measureAngle(angleVertex, v0, v1);
	}

	/**
	 * 
	 * @param v0 vertex 1
	 * @param v1 vertex 2
	 * @return returns the distance between v0 and v1
	 */
	public static double measureLength(Vector2d v0, Vector2d v1) {
		return Distance(v0, v1);
	}
	
	
	public static GeneralPath createFaceFromVertices(ArrayList<Vector2d> vList) {
		
		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, vList.size());
		path.moveTo(vList.get(0).x, vList.get(0).y);
		
		for (int i=0; i<vList.size(); i++) {
			path.lineTo(vList.get(i).x, vList.get(i).y);
		}
		path.closePath();

		return path;
	}
	
	
	public static GeneralPath createFaceFromLines(OriLine l0, OriLine l1, OriLine l2) {
		
		ArrayList<OriLine> lines = new ArrayList<>();
		
		ArrayList<Vector2d> points = new ArrayList<>();
		lines.add(l0);
		lines.add(l1);
		lines.add(l2);

		for(int i=1;i<lines.size(); i++) {
			if (lines.get(0).p0.x == lines.get(i).p0.x && lines.get(0).p0.y == lines.get(i).p0.y) {
			points.add(lines.get(0).p0);
			} else if (lines.get(0).p0.x == lines.get(i).p1.x && lines.get(0).p0.y == lines.get(i).p1.y) {
				points.add(lines.get(0).p0);
			} else if (lines.get(0).p1.x == lines.get(i).p0.x && lines.get(0).p1.y == lines.get(i).p0.y) {
				points.add(lines.get(0).p1);
			} else if (lines.get(0).p1.x == lines.get(i).p1.x && lines.get(0).p1.y == lines.get(i).p1.y) {
				points.add(lines.get(0).p1);
			}
		}
		
		for (int j=2; j<lines.size(); j++) {
			if (lines.get(1).p0.x == lines.get(j).p0.x && lines.get(1).p0.y == lines.get(j).p0.y) {
				points.add(lines.get(1).p0);
			} else if (lines.get(1).p0.x == lines.get(j).p1.x && lines.get(1).p0.y == lines.get(j).p1.y) {
				points.add(lines.get(1).p0);
			} else if (lines.get(1).p1.x == lines.get(j).p0.x && lines.get(1).p1.y == lines.get(j).p0.y) {
				points.add(lines.get(1).p1);
			} else if (lines.get(1).p1.x == lines.get(j).p1.x && lines.get(1).p1.y == lines.get(j).p1.y) {
				points.add(lines.get(1).p1);
			}
		}
		
		
		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, points.size());
		path.moveTo(points.get(0).x, points.get(0).y);
		
		for (int i=0; i<points.size(); i++) {
			path.lineTo(points.get(i).x, points.get(i).y);
		}
		path.closePath();
		
		return path;
	}
	
	
	public static Rectangle2D calcRotatedBox(double x, double y, double width, double height, double degrees) {
		
		Vector2d v0 = new Vector2d(x, y);
		Vector2d v1 = new Vector2d(x+width, y);
		Vector2d v2 = new Vector2d(x, y+height);
		Vector2d v3 = new Vector2d(x+width, y+height);

		Vector2d newV0 = rotVertex(v0, degrees);
		Vector2d newV1 = rotVertex(v1, degrees);
		Vector2d newV2 = rotVertex(v2, degrees);
		Vector2d newV3 = rotVertex(v3, degrees);
		
		ArrayList<Vector2d> vertexList = new ArrayList<>();
		
		ArrayList<Vector2d> newEdgePoints = new ArrayList<>();
		
		vertexList.add(newV0);
		vertexList.add(newV1);
		vertexList.add(newV2);
		vertexList.add(newV3);
		
		double smallestX = 0;
		double biggestX = 0;
		double smallestY = 0;
		double biggestY = 0;
		
		System.out.println("vertexList: " + vertexList.toString());
		
		for (int i=0; i<vertexList.size(); i++) {
			if (i == 0) {
				smallestX = vertexList.get(i).x;
				smallestY = vertexList.get(i).y;
			}
			if (vertexList.get(i).x < smallestX) {
				smallestX = vertexList.get(i).x;
			} else if (vertexList.get(i).x > biggestX) {
				biggestX = vertexList.get(i).x;
			}
			
			if (vertexList.get(i).y < smallestY) {
				smallestY = vertexList.get(i).y;
			} else if (vertexList.get(i).y > biggestY) {
				biggestY = vertexList.get(i).y;
			}
			
		}
		
		System.out.println("smalX: " + smallestX + " | bigX: " + biggestX + " | smalY: " + smallestY + " | bigY: " + biggestY);

		newEdgePoints.add(new Vector2d(smallestX,biggestY));
		newEdgePoints.add(new Vector2d(biggestX, smallestY));
		
		double newWidth = biggestX - smallestX;
		double newHeight = biggestY - smallestY;
		
		Rectangle2D rect = new Rectangle((int) Math.round(smallestX), (int) Math.round(biggestY), (int) Math.round(newWidth), (int) Math.round(newHeight));

		
		return rect;
		
		
	}
	
	
	public static Vector2d rotVertex(Vector2d v, double degrees) {
		double newX = v.x * Math.cos(Math.toRadians(degrees)) - v.y * Math.sin(Math.toRadians(degrees));
		double newY = v.x * Math.sin(Math.toRadians(degrees)) + v.y * Math.cos(Math.toRadians(degrees));
		
		return new Vector2d(newX, newY);
	}
	
	
}
