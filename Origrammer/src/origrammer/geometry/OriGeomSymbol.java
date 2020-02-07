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
	
	public OriGeomSymbol(Vector2d position, double radius) {
		this.position = position;
		this.radius = radius;
	}
	
//	public OriGeomSymbol(Vector2d position, double width, double height) {
//		this.position = position;
//		this.width = width;
//		this.height = height;
//	}

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

//	public double getWidth() {
//		return width;
//	}
//
//	public void setWidth(double width) {
//		this.width = width;
//	}
//
//	public double getHeight() {
//		return height;
//	}
//
//	public void setHeight(double height) {
//		this.height = height;
//	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
