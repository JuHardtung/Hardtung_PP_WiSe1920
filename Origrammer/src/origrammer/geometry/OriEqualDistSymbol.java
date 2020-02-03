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
	
	/**				 p2 - p1
	 * unitVector = ---------
	 * 				|p2 - p1|	
	 * @return unitVector of line(p0,p1)
	 */
	public Vector2d getUnitVector() {
		Vector2d normal = new Vector2d();
		normal.set(p1);
		normal.sub(p0);

		Vector2d uv = new Vector2d();
		uv.set(normal);
		uv.normalize();
		
		return uv;
	}
	
	
	/** rotate unitVector by 90 degrees to get normalVector
	 * 	x' = x*cos(angle) - y*sin(angle)
	 * 	y' = x*sin(angle) + y*cos(angle)
	 * 
	 * @return normalVector of line(p0,p1)
	 */
	public Vector2d getNormalVector() {
		double angleRadian = Math.toRadians(90);
		
		Vector2d uv = getUnitVector();
		double rx = (uv.x * Math.cos(angleRadian)) - (uv.y * Math.sin(angleRadian));
		double ry = (uv.x * Math.sin(angleRadian)) + (uv.y * Math.cos(angleRadian));
		Vector2d nv = new Vector2d(rx, ry);

		return nv;
	}
	
	public Vector2d getP0Pos() {
		double p0x = p0.x + (-translationDist * getNormalVector().x);
		double p0y = p0.y + (-translationDist * getNormalVector().y);
		return new Vector2d(p0x, p0y);
	}
	
	public Vector2d getP1Pos() {
		double p1x = p1.x + (-translationDist * getNormalVector().x);
		double p1y = p1.y + (-translationDist * getNormalVector().y);
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
