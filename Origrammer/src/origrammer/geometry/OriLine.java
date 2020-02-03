package origrammer.geometry;

import javax.vecmath.Vector2d;

public class OriLine {
	final public static int TYPE_NONE = 0;
	final public static int TYPE_EDGE = 1;
	final public static int TYPE_MOUNTAIN = 2;
	final public static int TYPE_VALLEY = 3;
	final public static int TYPE_XRAY = 4;
	
	private boolean isSelected;
	public int type = TYPE_NONE;
	public Vector2d p0 = new Vector2d();
	public Vector2d p1 = new Vector2d();

	public OriLine() {
	}
	
	public OriLine(OriLine l) {
		this.type = l.type;
		this.p0.set(l.p0);
		this.p1.set(l.p1);
	}
	
	public OriLine(Vector2d p0, Vector2d p1, int type) {
		this.type = type;
		this.p0.set(p0);
		this.p1.set(p1);
	}

	public OriLine(double x0, double y0, double x1, double y1, int type) {
		this.type = type;
		this.p0.set(x0, y0);
		this.p1.set(x1, y1);
	}
	
	public Line getLine() {
		return new Line(p0, new Vector2d(p1.x - p0.x, p1.y - p0.y));
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public String toString() {
		return "OriLine [selected=" + isSelected + ", type=" + type + ", p0=" + p0 + ", p1=" + p1 + "]";
	}
	
}
