package origrammer;

import javax.vecmath.Vector2d;

import origrammer.geometry.OriLine;

public class OriLineProxy {
	
	private double x0;
	private double y0;
	private double x1;
	private double y1;
	private int type;
	
	public OriLineProxy() {
	}
	
	public OriLineProxy(OriLine l) {
		x0 = l.p0.x;
		y0 = l.p0.y;
		x1 = l.p1.x;
		y1 = l.p1.y;
		type = l.type;
	}
	
	public OriLine getLine() {
		return new OriLine(new Vector2d(x0,y0), new Vector2d(x1, y1), type);
	}

	public double getX0() {
		return x0;
	}

	public void setX0(double x0) {
		this.x0 = x0;
	}

	public double getY0() {
		return y0;
	}

	public void setY0(double y0) {
		this.y0 = y0;
	}

	public double getX1() {
		return x1;
	}

	public void setX1(double x1) {
		this.x1 = x1;
	}

	public double getY1() {
		return y1;
	}

	public void setY1(double y1) {
		this.y1 = y1;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "OriLineProxy [x0=" + x0 + ", y0=" + y0 + ", x1=" + x1 + ", y1=" + y1 + ", type=" + type + "]";
	}

}
