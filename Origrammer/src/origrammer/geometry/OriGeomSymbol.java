package origrammer.geometry;

import javax.vecmath.Vector2d;

public class OriGeomSymbol {

	
	final public static int TYPE_NONE = 0;
	final public static int TYPE_XRAY_CIRCLE = 1;
	final public static int TYPE_CLOSED_SINK = 2;
	
	private int type = TYPE_NONE;
	private Vector2d position;
	private double radius;
	private boolean isSelected;
	
	
	public OriGeomSymbol() {
	}
	
	public OriGeomSymbol(OriGeomSymbol gs) {
		this.type = gs.type;
		this.position = gs.position;
		this.radius = gs.radius;
		this.isSelected = gs.isSelected;
	}
	
	public OriGeomSymbol(Vector2d position, double radius) {
		this.position = position;
		this.radius = radius;
	}
	
	public OriGeomSymbol(Vector2d position, double radius, int type) {
		this.position = position;
		this.radius = radius;
		this.type = type;
	}
	
	public void moveBy(double xTrans, double yTrans) {
		position.x += xTrans;
		position.y += yTrans;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Vector2d getPosition() {
		return position;
	}

	public void setPosition(Vector2d position) {
		this.position = position;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public String toString() {
		return "OriGeomSymbol [type=" + type + ", position=" + position + ", radius=" + radius + ", isSelected="
				+ isSelected + "]";
	}
}
