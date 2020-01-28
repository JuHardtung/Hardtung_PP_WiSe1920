package origrammer.geometry;

public class OriGeomSymbol {

	
	final public static int TYPE_NONE = 0;
	final public static int TYPE_XRAY_CIRCLE = 1;
	final public static int TYPE_CLOSED_SINK = 2;
	
	private int type = TYPE_NONE;
	public double xPos;
	public double yPos;
	public double radius;
	public double width;
	public double height;
	public boolean isSelected;
	
	
	public OriGeomSymbol() {
	}
	
	public OriGeomSymbol(double xPos, double yPos, double radius) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.radius = radius;
	}
	
	public OriGeomSymbol(double xPos, double yPos, double width, double height) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getxPos() {
		return xPos;
	}

	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	public double getyPos() {
		return yPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
