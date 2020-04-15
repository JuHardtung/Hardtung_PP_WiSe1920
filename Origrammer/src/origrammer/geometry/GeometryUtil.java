package origrammer.geometry;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.vecmath.Vector2d;

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
		if (Distance(l0.getP0(), l1.getP0()) < POINT_SNAP_VALUE && Distance(l0.getP1(), l1.getP1()) < POINT_SNAP_VALUE) {
			return true;
		} if (Distance(l0.getP0(), l1.getP1()) < POINT_SNAP_VALUE && Distance(l0.getP1(), l1.getP0()) < POINT_SNAP_VALUE) {
			return true;
		}
		return false;
	}
	
	
	public static Vector2d getCrossPoint(OriLine l0, OriLine l1) {
		double epsilon = 1.0e-6;
		Vector2d p0 = new Vector2d(l0.getP0());
		Vector2d p1 = new Vector2d(l0.getP1());

		Vector2d d0 = new Vector2d(p1.x - p0.x, p1.y - p0.y);
		Vector2d d1 = new Vector2d(l1.getP1().x - l1.getP0().x, l1.getP1().y - l1.getP0().y);
		Vector2d diff = new Vector2d(l1.getP0().x - p0.x, l1.getP0().y - p0.y);
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
				cp.x = (1.0 - t) * l1.getP0().x + t * l1.getP1().x;
				cp.y = (1.0 - t) * l1.getP0().y + t * l1.getP1().y;
				return cp;
			}
		}
		return null;
	}
	
	public static double DistancePointToSegment(Vector2d p, Vector2d sp, Vector2d ep) {
		Vector2d sub0, sub, sub0b;

		sub0 = new Vector2d(sp.x - p.x, sp.y - p.y);
		sub0b = new Vector2d(-sub0.x, -sub0.y);
		sub = new Vector2d(ep.x - sp.x, ep.y - sp.y);

		double t = ((sub.x * sub0b.x) + (sub.y * sub0b.y)) / ((sub.x * sub.x) + (sub.y * sub.y));
		
		if (t < 0.0) {
			return Distance(p.x, p.y, sp.x, sp.y);
		} else if (t > 1.0) {
			return Distance(p.x, p.y, ep.x, ep.y);
		} else {
			return Distance(sp.x + t * sub.x, sp.y + t * sub.y, p.x, p.y);
		}
	}
	
	public static double DistancePointToSegment(Vector2d p, Vector2d sp, Vector2d ep, Vector2d nearestPoint) {
		Vector2d sub0, sub, sub0b;

		sub0 = new Vector2d(sp.x - p.x, sp.y - p.y);
		sub0b = new Vector2d(-sub0.x, -sub0.y);
		sub = new Vector2d(ep.x - sp.x, ep.y - sp.y);

		double t = ((sub.x * sub0b.x) + (sub.y * sub0b.y)) / ((sub.x * sub.x) + (sub.y * sub.y));

		if (t < 0.0) {
			nearestPoint.set(sp);
			return Distance(p.x, p.y, sp.x, sp.y);
		} else if (t > 1.0) {
			nearestPoint.set(ep);
			return Distance(p.x, p.y, ep.x, ep.y);
		} else {
			nearestPoint.set(sp.x + t * sub.x, sp.y + t * sub.y);
			return Distance(sp.x + t * sub.x, sp.y + t * sub.y, p.x, p.y);
		}
	}
	
	/**
	 * Returns true if mouseCoordinates are within the bounds of the tested OriArrow
	 * @param p Mouse position
	 * @param a OriArrow that is tested for selection
	 * @return true if mouse is over OriArrow and false if it isn't
	 */
	public static boolean isMouseOverArrow(Point2D.Double p, OriArrow a) {
		double aX = a.getPosition().x;
		double aY = a.getPosition().y;
		double aXEnd = aX + (a.getWidth());
		double aYEnd = aY + (a.getHeight());

		if (p.x > aX && p.x < aXEnd) {
			if (p.y > aY && p.y < aYEnd) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param p Mouse position
	 * @param s OriSymbol that is tested for selection
	 * @return true if mouse is over OriSymbol and false if it isn't
	 */
	public static boolean isMouseOverSymbol(Point2D.Double p, OriPicSymbol s) {
		double sX = s.getPosition().x;
		double sY = s.getPosition().y;
		double sXEnd = sX + (s.getWidth());
		double sYEnd = sY + (s.getHeight());

		if (p.x > sX && p.x < sXEnd) {
			if (p.y > sY && p.y < sYEnd) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param p Mouse Position
	 * @param s OriGeomSymbol that is tested for selection
	 * @return returns true if mouse is over OriGeomSymbol and false if it isn't
	 */
	public static boolean isMouseOverGeomSymbol(Point2D.Double p, OriGeomSymbol s) {
		double sX = s.getPosition().x;
		double sY = s.getPosition().y;
		double sXEnd = sX + (s.getRadius());
		double sYEnd = sY + (s.getRadius());

		if (p.x > sX && p.x < sXEnd) {
			if (p.y > sY && p.y < sYEnd) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param p Mouse Position
	 * @param s OriEqualDistSymbol that is tested for selection
	 * @return returns true if mouse is over OriEqualDistSymbol and false if it isn't
	 */
	public static boolean isMouseOverEqualDistSymbol(Point2D.Double p, OriEqualDistSymbol s) {
		Vector2d nv = GeometryUtil.getNormalVector(GeometryUtil.getUnitVector(s.getP0(), s.getP1()));

		double p0x = s.getP0Pos().x + 15*nv.x;
		double p0y = s.getP0Pos().y + 15*nv.y;

		double p1x = s.getP0Pos().x - 15*nv.x;
		double p1y = s.getP0Pos().y - 15*nv.y;

		double p2x = s.getP1Pos().x - 15*nv.x;
		double p2y = s.getP1Pos().y - 15*nv.y;

		double p3x = s.getP1Pos().x + 15*nv.x;
		double p3y = s.getP1Pos().y + 15*nv.y;

		Path2D.Double path = new Path2D.Double();
		path.moveTo(p0x, p0y);
		path.lineTo(p1x, p1y);
		path.lineTo(p2x, p2y);
		path.lineTo(p3x, p3y);
		path.closePath();

		return isMouseOverPath(p, path);
	}
	

	/**
	 * 
	 * @param p Mouse Position
	 * @param s Shape that is tested for selection
	 * @return True if the mouse is over the Shape and false if it isn't
	 */
	public static boolean isMouseOverShapes(Point2D.Double p, ArrayList<Shape> s) {
		for (Shape shape : s) {
			if (shape.intersects(p.x, p.y, 5, 5)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param p Mouse Position
	 * @param f OriFace that is tested for selection
	 * @return True if mouse is over the OriFace and false if it isn't
	 */
	public static boolean isMouseOverFace(Point2D.Double p, OriFace f) {
		return f.path.intersects(p.x, p.y, 5, 5);
	}
	
	/**
	 * 
	 * @param p Mouse Position
	 * @param path Path that is tested for selection
	 * @return True if mouse is over the Path and false if it isn't
	 */
	public static boolean isMouseOverPath(Point2D.Double p, Path2D.Double path) {
		return path.intersects(p.x, p.y, 5, 5);
	}
	
	/**
	 * 
	 * @param p Mouse Position
	 * @param rect Rectangle that is tested for selection
	 * @return True if mouse is over the Rectangle and false if it isn't
	 */
	public static boolean isMouseOverRectangle(Point2D.Double p, Rectangle rect) {
		return rect.intersects(p.x, p.y, 5, 5);
	}


	/**
	 * [a,b,c] = Distance()
	 * 
	 * 		            (ax1 + bx2 + cx3   ay1 + by2 + cy3)
	 * 	Incenter(x,y) = (--------------- , ---------------)
	 *                  (   a + b + c         a + b + c   )
	 * 
	 * @param v0 Vertex of the triangle
	 * @param v1 Vertex of the triangle
	 * @param v2 Vertex of the triangle
	 * @return The triangle incenter
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
	 * Calculate the angle by three points
	 * 		    (b² + c² - a²)		a = Distance(C, B)
	 * cos(A) = --------------		b = Distance(A, C)
	 * 			     2bc			c = Distance(A, B)
	 * 
	 * @param v0
	 * @param v1
	 * @param v2
	 * @return The angle that v0 is the vertex of
	 */
	public static double measureAngle(Vector2d v0, Vector2d v1, Vector2d v2) {
		double angle = 0;
		double a = Distance(v2, v1);
		double b = Distance(v0, v2);
		double c = Distance(v0, v1);
		
		double cosA = (Math.pow(b, 2) + Math.pow(c, 2) - Math.pow(a, 2)) / (2 * b * c);
		angle = Math.toDegrees(Math.acos(cosA));
		
		return angle;
	}
	
	/**
	 * Calculate the angle between two OriLines
	 * @param l0
	 * @param l1
	 * @return The angle between l0 and l1
	 */
	public static double measureAngle(OriLine l0, OriLine l1) {
		Vector2d l0p0 = l0.getP0();
		Vector2d l0p1 = l0.getP1();
		Vector2d l1p0 = l1.getP0();
		Vector2d l1p1 = l1.getP1();

		Vector2d angleVertex;
		Vector2d v0;
		Vector2d v1;
		
		if (l0p0.x == l1p0.x && l0p0.y == l1p0.y) {
			angleVertex = l0p0;
			v0 = l0p1;
			v1 = l1p1;
		} else if (l0p0.x == l1p1.x && l0p0.y == l1p1.y) {
			angleVertex = l0p0;
			v0 = l0p1;
			v1 = l1p0;
		} else if (l0p1.x == l1p0.x && l0p1.y == l1p0.y) {
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
	 * Calculates the angle between a line(x,y) and the x-axis
	 * @param x
	 * @param y
	 * @return The angle in Radians
	 */
	private static double calcAngleToXAxis(double x, double y) {
		double alpha = 0;
		System.out.println("x: " + x + " y: " + y);
		if (x > 0) {
			alpha = Math.atan(y / x);
			System.out.println("1");
		} else if (y >= 0 && x < 0) {
			alpha = Math.atan(y / x) + Math.PI;
			System.out.println("2");
		} else if (y < 0 && x < 0) {
			alpha = Math.atan(y / x) - Math.PI;
			System.out.println("3");
		} else if (y > 0 && x == 0) {
			System.out.println("4");
			alpha = Math.PI / 2;
		} else if (y < 0 && x == 0) {
			System.out.println("5");
			alpha = - (Math.PI / 2);
		} else if (y == 0 && x == 0) {
			System.out.println("Angle2 is undefined");
		}
		System.out.println("alpha: " + alpha);
		return alpha;
	}
	
	/**
	 * Creates a GeneralPath from an ArrayList<Vector2d> of vertices for the use in OriFilledFace
	 * @param  vList ArrayList<Vector2d>  for the Path
	 * @return GeneralPath
	 */
	public static GeneralPath createPathFromVertices(ArrayList<Vector2d> vList) {
		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, vList.size());
		path.moveTo(vList.get(0).x, vList.get(0).y);

		for (int i = 1; i < vList.size(); i++) {
			path.lineTo(vList.get(i).x, vList.get(i).y);
		}
		path.closePath();

		return path;
	}
	
	/**
	 * Creates a GeneralPath from three lines
	 * @param l0 OriLine 
	 * @param l1 OriLine
	 * @param l2 OriLine
	 * @return GeneralPath
	 * @deprecated Use createPathFromVertices instead and get the vertices from the lines
	 */
	@Deprecated
	public static GeneralPath createPathFromLines(OriLine l0, OriLine l1, OriLine l2) {
		ArrayList<OriLine> lines = new ArrayList<>();
		ArrayList<Vector2d> points = new ArrayList<>();
		lines.add(l0);
		lines.add(l1);
		lines.add(l2);

		for(int i = 1; i < lines.size(); i++) {
			if (lines.get(0).getP0().x == lines.get(i).getP0().x && lines.get(0).getP0().y == lines.get(i).getP0().y) {
				points.add(lines.get(0).getP0());
			} else if (lines.get(0).getP0().x == lines.get(i).getP1().x && lines.get(0).getP0().y == lines.get(i).getP1().y) {
				points.add(lines.get(0).getP0());
			} else if (lines.get(0).getP1().x == lines.get(i).getP0().x && lines.get(0).getP1().y == lines.get(i).getP0().y) {
				points.add(lines.get(0).getP1());
			} else if (lines.get(0).getP1().x == lines.get(i).getP1().x && lines.get(0).getP1().y == lines.get(i).getP1().y) {
				points.add(lines.get(0).getP1());
			}
		}

		for (int j = 2; j < lines.size(); j++) {
			if (lines.get(1).getP0().x == lines.get(j).getP0().x && lines.get(1).getP0().y == lines.get(j).getP0().y) {
				points.add(lines.get(1).getP0());
			} else if (lines.get(1).getP0().x == lines.get(j).getP1().x && lines.get(1).getP0().y == lines.get(j).getP1().y) {
				points.add(lines.get(1).getP0());
			} else if (lines.get(1).getP1().x == lines.get(j).getP0().x && lines.get(1).getP1().y == lines.get(j).getP0().y) {
				points.add(lines.get(1).getP1());
			} else if (lines.get(1).getP1().x == lines.get(j).getP1().x && lines.get(1).getP1().y == lines.get(j).getP1().y) {
				points.add(lines.get(1).getP1());
			}
		}

		GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, points.size());
		path.moveTo(points.get(0).x, points.get(0).y);

		for (int i = 0; i < points.size(); i++) {
			path.lineTo(points.get(i).x, points.get(i).y);
		}
		path.closePath();

		return path;
	}
	
	/**
	 * Calculates the new edge points of a Rectangle after rotating
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param degrees
	 * @return The rotated Rectangle2D
	 */
	public static Rectangle2D calcRotatedBox(double x, double y, double width, double height, double degrees) {
		Vector2d v0 = new Vector2d(x, y);
		Vector2d v1 = new Vector2d(x + width, y);
		Vector2d v2 = new Vector2d(x, y + height);
		Vector2d v3 = new Vector2d(x + width, y + height);

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
				
		for (int i = 0; i<vertexList.size(); i++) {
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
		
		newEdgePoints.add(new Vector2d(smallestX,biggestY));
		newEdgePoints.add(new Vector2d(biggestX, smallestY));
		
		double newWidth = biggestX - smallestX;
		double newHeight = biggestY - smallestY;
		
		Rectangle2D rect = new Rectangle((int) Math.round(smallestX), (int) Math.round(biggestY), (int) Math.round(newWidth), (int) Math.round(newHeight));

		return rect;
	}
	
	/**
	 * Calculates the position of a vertex after rotating
	 * @param v
	 * @param degrees
	 * @return New Vertex position
	 */
	public static Vector2d rotVertex(Vector2d v, double degrees) {
		double newX = v.x * Math.cos(Math.toRadians(degrees)) - v.y * Math.sin(Math.toRadians(degrees));
		double newY = v.x * Math.sin(Math.toRadians(degrees)) + v.y * Math.cos(Math.toRadians(degrees));
		
		return new Vector2d(newX, newY);
	}
	
	

	/**				  
	 * Calculates the unitVector
	 * <p>
	 *    p1 - p0  <br>
	 *   --------- <br>
	 *   |p1 - p0|
	 * @param p0
	 * @param p1
	 * @return unitVector of line(p0, p1)
	 */
	public static Vector2d getUnitVector(Vector2d p0, Vector2d p1) {
		Vector2d normal = new Vector2d();
		normal.set(p1);
		normal.sub(p0);

		Vector2d uv = new Vector2d();
		uv.set(normal);
		uv.normalize();
		
		return uv;
	}
	
	/**
	 * Calculates the normalVector from the unitVector <p>
	 * Rotate unitVector by 90 degrees to get normalVector <br>
	 * 	x' = x * cos(angle) - y * sin(angle) <br>
	 * 	y' = x * sin(angle) + y * cos(angle) <br>
	 * @param uv
	 * @return normalVector of line(p0,p1)
	 */
	public static Vector2d getNormalVector(Vector2d uv) {
		double angleRadian = Math.toRadians(90);
		
		double rx = (uv.x * Math.cos(angleRadian)) - (uv.y * Math.sin(angleRadian));
		double ry = (uv.x * Math.sin(angleRadian)) + (uv.y * Math.cos(angleRadian));
		Vector2d nv = new Vector2d(rx, ry);

		return nv;
	}
	
}
