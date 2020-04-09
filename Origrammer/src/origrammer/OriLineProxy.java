package origrammer;

import javax.vecmath.Vector2d;

import origrammer.geometry.OriLine;

public class OriLineProxy {
	
	private Vector2d p0;
	private Vector2d p1;
	private int type;
	private boolean isStartTransl;
	private boolean isEndTransl;
	
	
	public OriLineProxy() {
	}
	
	public OriLineProxy(OriLine l) {
		this.p0 = l.getP0();
		this.p1 = l.getP1();
		this.type = l.getType();
		this.isStartTransl = l.isStartOffset();
		this.isEndTransl = l.isEndOffset();
	}
	
	public OriLine getLine() {
		return new OriLine(p0, p1, type, isStartTransl, isEndTransl);
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
		return "OriLineProxy [p0=" + p0 + ", p1=" + p1 + ", type=" + type + "]";
	}

}
