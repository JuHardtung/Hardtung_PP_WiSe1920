package origrammer.geometry;

import javax.vecmath.Vector2d;

public class Line {

	public Vector2d p;
	public Vector2d dir;

	public Line(Vector2d p, Vector2d dir) {
		this.p = p;
		this.dir = dir;
		dir.normalize();
	}
}
