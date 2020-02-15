package origrammer;

import javax.vecmath.Vector2d;

import origrammer.geometry.OriEqualDistSymbol;

public class OriEqualDistProxy {
	
	private Vector2d p0;
	private Vector2d p1;
	private double translationDist;
	private int dividerCount;
	
	public OriEqualDistProxy() {
		
	}
	
	public OriEqualDistProxy(OriEqualDistSymbol s) {
		this.p0 = s.getP0();
		this.p1 = s.getP1();
		this.translationDist = s.getTranslationDist();
		this.dividerCount = s.getDividerCount();
	}
	
	public OriEqualDistSymbol getSymbol() {
		return new OriEqualDistSymbol(p0, p1, translationDist, dividerCount);
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

	public double getTranslationDist() {
		return translationDist;
	}

	public void setTranslationDist(double translationDist) {
		this.translationDist = translationDist;
	}

	public int getDividerCount() {
		return dividerCount;
	}

	public void setDividerCount(int dividerCount) {
		this.dividerCount = dividerCount;
	}

}
