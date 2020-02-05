package origrammer.geometry;

import javax.vecmath.Vector2d;

public class OriEqualDistSymbol {
	
	private Vector2d p0 = new Vector2d();
	private Vector2d p1 = new Vector2d();
	private double translationDist = 50;
	private int dividerCount;
	private boolean isSelected;

	
	public OriEqualDistSymbol() {
		
	}
	
	public OriEqualDistSymbol(Vector2d p0, Vector2d p1, int dividerCount) {
		this.p0 = p0;
		this.p1 = p1;
		this.dividerCount = dividerCount;
	}
	
	public Vector2d getP0Pos() {
		Vector2d nv = GeometryUtil.getNormalVector(GeometryUtil.getUnitVector(p0, p1));
		double p0x = p0.x + (-translationDist * nv.x);
		double p0y = p0.y + (-translationDist * nv.y);
		return new Vector2d(p0x, p0y);
	}
	
	public Vector2d getP1Pos() {
		Vector2d nv = GeometryUtil.getNormalVector(GeometryUtil.getUnitVector(p0, p1));
		double p1x = p1.x + (-translationDist * nv.x);
		double p1y = p1.y + (-translationDist * nv.y);
		return new Vector2d(p1x, p1y);
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

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
