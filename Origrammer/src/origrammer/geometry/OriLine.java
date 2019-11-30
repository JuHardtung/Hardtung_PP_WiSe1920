package origrammer.geometry;

import javax.vecmath.Vector2d;

public class OriLine {
	final public static int TYPE_NONE = 0;
	final public static int TYPE_EDGE = 1;
	final public static int TYPE_MOUNTAIN = 2;
	final public static int TYPE_VALLEY = 3;
	final public static int TYPE_XRAY = 4;

	public int type = TYPE_NONE;
	public Vector2d p0 = new Vector2d();
	public Vector2d p1 = new Vector2d();

	public OriLine() {
	}

	public OriLine(double x0, double y0, double x1, double y1, int type) {
		this.type = type;
		this.p0.set(x0, y0);
		this.p1.set(x1, y1);
	}
}
