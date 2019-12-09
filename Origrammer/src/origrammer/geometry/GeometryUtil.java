package origrammer.geometry;

import javax.vecmath.Vector2d;

public class GeometryUtil {

	public final static double POINT_SNAP_VALUE = 0.00001f;
	
	public static double Distance(Vector2d p0, Vector2d p1) {
		return Distance(p0.x, p0.y, p1.x, p1.y);
	}
	
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
	
	
}
