package origrammer.geometry;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.vecmath.Vector2d;

public class OriEqualDistSymbol {
	
	private Vector2d p0 = new Vector2d();
	private Vector2d p1 = new Vector2d();
	private double translationDist = 50;
	private int dividerCount;
	private boolean isSelected;

	
	public OriEqualDistSymbol() {
	}
	
	public OriEqualDistSymbol(OriEqualDistSymbol eds) {
		this.p0 = eds.p0;
		this.p1 = eds.p1;
		this.translationDist = eds.translationDist;
		this.dividerCount = eds.dividerCount;
		this.isSelected = eds.isSelected;
	}
	
	public OriEqualDistSymbol(Vector2d p0, Vector2d p1, int dividerCount) {
		this.p0 = p0;
		this.p1 = p1;
		this.dividerCount = dividerCount;
	}
	
	public OriEqualDistSymbol(Vector2d p0, Vector2d p1, double translationDist, int dividerCount) {
		this.p0 = p0;
		this.p1 = p1;
		this.translationDist = translationDist;
		this.dividerCount = dividerCount;
	}
	
	/**
	 * 
	 * @return The Shapes required for rendering the OriEqualDistSymbol
	 */
	public ArrayList<Shape> getShapesForDrawing() {
		ArrayList<Shape> shapes = new ArrayList<>();
		Vector2d uv = GeometryUtil.getUnitVector(p0, p1);
		Vector2d nv = GeometryUtil.getNormalVector(uv);
		Vector2d p0Pos = getP0Pos();
		double dist = GeometryUtil.Distance(p0, p1);
		double trans = dist / dividerCount;

		//main line 
		for (int j = 1; j <= dividerCount; j++) {
			double slp0x = p0Pos.x + trans * (j - 1) * uv.x + 10 * uv.x;
			double slp0y = p0Pos.y + trans * (j - 1) * uv.y + 10 * uv.y;
			double slp1x = p0Pos.x + trans * j * uv.x - 10 * uv.x;
			double slp1y = p0Pos.y + trans * j * uv.y - 10 * uv.y;

			shapes.add(new Line2D.Double(slp0x, slp0y, slp1x, slp1y));
		}

		//small divider lines
		for (int i = 0; i <= dividerCount; i++) {
			double tmpX = p0Pos.x + ((trans * i) * uv.x);
			double tmpY = p0Pos.y + ((trans * i) * uv.y);

			double x5 = tmpX + (15 * nv.x);
			double y5 = tmpY + (15 * nv.y);

			double x6 = tmpX - (15 * nv.x);
			double y6 = tmpY - (15 * nv.y);

			shapes.add(new Line2D.Double(x5, y5, x6, y6));
		}
		return shapes;
	}
	
	public void moveBy(double xTrans, double yTrans) {
		p0.x += xTrans;
		p0.y += yTrans;
		p1.x += xTrans;
		p1.y += yTrans;
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
