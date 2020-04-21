package origrammer.geometry;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.vecmath.Vector2d;

public class OriEqualAnglSymbol {
	
	private Vector2d v = new Vector2d();
	private Vector2d a = new Vector2d();
	private Vector2d b = new Vector2d();
	private int dividerCount;
	private boolean isSelected;
	
	
	public OriEqualAnglSymbol() {
	}
	
	public OriEqualAnglSymbol(OriEqualAnglSymbol eas) {
		this.v = eas.v;
		this.a = eas.a;
		this.b = eas.b;
		this.dividerCount = eas.dividerCount;
		this.isSelected = eas.isSelected;
	}
	
	public OriEqualAnglSymbol(Vector2d v, Vector2d a, Vector2d b) {
		this.v = v;
		this.a = a;
		this.b = b;
	}
	
	public OriEqualAnglSymbol(Vector2d v, Vector2d a, Vector2d b, int dividerCount) {
		this.v = v;
		this.a = a;
		this.b = b;
		this.dividerCount = dividerCount;
	}
	
	/**
	 * 
	 * @return The shapes required for rendering the OriEqualAnglSymbol
	 */
	public ArrayList<Shape> getShapesForDrawing() {

		ArrayList<Shape> shapes = new ArrayList<>();
		double angle = GeometryUtil.measureAngle(v, a, b);
		double singleAngle = angle / getDividerCount();		   
		double length = GeometryUtil.Distance(v, a);

		Vector2d arcStart;
		Vector2d arcEnd = new Vector2d(0, 0);

		//big divider line
		Line2D startLine = new Line2D.Double(v.x, v.y, b.x, b.y);
		shapes.add(startLine);
		for (int i = 1; i <= dividerCount; i++) {
			double endX;
			double endY;

			if (a.x < b.x) {
				endX = v.x + length * Math.cos(Math.toRadians(singleAngle * i));
				if (a.y < b.y) {
					endY = v.y + length * Math.sin(-Math.toRadians(singleAngle * i));
				} else {
					endY = v.y + length * Math.sin(Math.toRadians(singleAngle * i));
				}
			} else {
				endX = v.x - length * Math.cos(Math.toRadians(singleAngle * i));

				if(a.y > b.y) {
					endY = v.y + length * Math.sin(Math.toRadians(singleAngle * i));
				} else {
					endY = v.y + length * Math.sin(-Math.toRadians(singleAngle * i));
				}
			}
			Line2D tmpLine = new Line2D.Double(v.x, v.y, endX, endY);
			shapes.add(tmpLine);

			if (i == 1) {
				arcStart = b;
				arcEnd = new Vector2d(endX, endY);
			} else {
				arcStart = arcEnd;
				arcEnd = new Vector2d(endX, endY);
			}

			//small arcs
			//     ____________________
			//	\/(x1-x0)² + (y1-y0)²
			double r = Math.sqrt(Math.pow(arcEnd.x - v.x, 2) + Math.pow(arcEnd.y - v.y, 2));
			double x = v.x - r;
			double y = v.y - r;
			double width = 2 * r;
			double height = 2 * r;
			double startAngle;
			double endAngle;

			startAngle = -Math.toDegrees(Math.atan2(arcStart.y - v.y, arcStart.x - v.x));
			endAngle = -Math.toDegrees(Math.atan2(arcEnd.y - v.y, arcEnd.x - v.x));

			if (arcEnd.x < arcStart.x) {
				if (arcEnd.y < arcStart.y) {
					startAngle += 2;
					endAngle = endAngle - startAngle-2;
				} else {
					startAngle -= 2;
					endAngle = endAngle - startAngle + 2;
				}
			} else {
				if (arcEnd.y > arcStart.y) {
					startAngle += 2;
					endAngle = endAngle - startAngle -2;
				} else {
					if (i == 1) {
						startAngle *= -1;
					}
					startAngle -= 2;
					endAngle = endAngle - startAngle + 2;
				}
			}

			Arc2D.Double arc = new Arc2D.Double(x, y, width, height, startAngle, endAngle, Arc2D.OPEN);
			shapes.add(arc);
		}
		return shapes;
	}
	
	/** Sets the positions of Vector2d a and Vector2d b,<br> 
	 * so that the {@code Distance(v, a)} and {@code Distance(v, b) == lineLength}
	 * @param lineLength
	 */
	public void setLineLength(double lineLength) {
		Vector2d uvA = GeometryUtil.getUnitVector(v, a);
		Vector2d uvB = GeometryUtil.getUnitVector(v, b);
		
		a.x = v.x + uvA.x * lineLength;
		a.y = v.y + uvA.y * lineLength;
		
		b.x = v.x + uvB.x * lineLength;
		b.y = v.y + uvB.y * lineLength;
	}

	public Vector2d getV() {
		return v;
	}

	public void setV(Vector2d v) {
		this.v = v;
	}

	public Vector2d getA() {
		return a;
	}

	public void setA(Vector2d a) {
		this.a = a;
	}

	public Vector2d getB() {
		return b;
	}

	public void setB(Vector2d b) {
		this.b = b;
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
