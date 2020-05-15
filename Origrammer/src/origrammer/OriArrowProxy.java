package origrammer;

import javax.vecmath.Vector2d;

import origrammer.geometry.OriArrow;

public class OriArrowProxy {
	
	private Vector2d p0;
	private Vector2d p1;
	private int type;
	private boolean isMirrored;
	private boolean isUnfold;
	private int arrowRadius;

	
	public OriArrowProxy() {
	}
	
	public OriArrowProxy(OriArrow a) {
		p0 = a.getP0();
		p1 = a.getP1();
		type = a.getType();
		isMirrored = a.isMirrored();
		isUnfold = a.isUnfold();
		arrowRadius = a.getArrowRadius();
                                                                                		
	}
	
	public OriArrow getArrow() {
		return new OriArrow(p0, p1, type, isMirrored, isUnfold, arrowRadius);
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

	public boolean isMirrored() {
		return isMirrored;
	}

	public void setMirrored(boolean isMirrored) {
		this.isMirrored = isMirrored;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getArrowRadius() {
		return arrowRadius;
	}

	public void setArrowRadius(int arrowRadius) {
		this.arrowRadius = arrowRadius;
	}

	@Override
	public String toString() {
		return "OriArrowProxy [p0=" + p0 + ", p1=" + p1 + ", type=" + type + ", isMirrored=" + isMirrored + "]";
	}
	
}
