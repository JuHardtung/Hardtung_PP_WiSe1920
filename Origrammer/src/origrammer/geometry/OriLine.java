package origrammer.geometry;

import javax.vecmath.Vector2d;

public class OriLine {
	final public static int TYPE_NONE = 0;
	final public static int TYPE_EDGE = 1;
	final public static int TYPE_MOUNTAIN = 2;
	final public static int TYPE_VALLEY = 3;
	final public static int TYPE_XRAY = 4;
	final public static int TYPE_CREASE = 5;
	
	private boolean isSelected;
	private int type = TYPE_NONE;
	private Vector2d p0 = new Vector2d();
	private Vector2d p1 = new Vector2d();
	private boolean isStartTransl;
	private boolean isEndTransl;

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
	
	public OriLine(Vector2d p0, Vector2d p1, int type, boolean isStartTrans, boolean isEndTrans) {
		this.type = type;
		this.p0 = p0;
		this.p1 = p1;
		this.isStartTransl = isStartTrans;
		this.isEndTransl = isEndTrans;
	}
	
	
	public Vector2d getTranslatedP0() {
		Vector2d uv = GeometryUtil.getUnitVector(p0, p1);
		
		double newX = p0.x + uv.x*25;
		double newY = p0.y + uv.y*25;
		return new Vector2d(newX, newY);
	}
	
	public Vector2d getTranslatedP1() {
		Vector2d uv = GeometryUtil.getUnitVector(p0, p1);
		
		double newX = p1.x - uv.x*25;
		double newY = p1.y - uv.y*25;
		return new Vector2d(newX, newY);
	}

	public Vector2d getP0() {
		return p0;
	}

	public void setP0(Vector2d p0) {
		this.p0 = p0;
	}

	public Vector2d getP1() {
		return p1;
	}

	public void setP1(Vector2d p1) {
		this.p1 = p1;
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

	public boolean isStartTransl() {
		return isStartTransl;
	}

	public void setStartTransl(boolean isStartTransl) {
		this.isStartTransl = isStartTransl;
	}

	public boolean isEndTransl() {
		return isEndTransl;
	}

	public void setEndTransl(boolean isEndTransl) {
		this.isEndTransl = isEndTransl;
	}

	@Override
	public String toString() {
		return "OriLine [isSelected=" + isSelected + ", type=" + type + ", p0=" + p0 + ", p1=" + p1 + ", isStartTransl="
				+ isStartTransl + ", isEndTransl=" + isEndTransl + "]";
	}
	
}
