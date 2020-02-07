package origrammer.geometry;

import javax.swing.JLabel;

public class OriPicSymbol {

	
	final public static int TYPE_NONE = 0;
	final public static int TYPE_ROTATION = 1;
	final public static int TYPE_XRAY_CIRCLE = 2;
	final public static int TYPE_HOLD = 3;
	final public static int TYPE_HOLD_AND_PULL = 4;
	final public static int TYPE_NEXT_VIEW_HERE = 5;
	
	public JLabel label = new JLabel();
	private int type = TYPE_NONE;
	public double xPos;
	public double yPos;
	private int width;
	private int height;
	private double scale = 0;
	private boolean isSelected;
	private double degrees = 0;
	
	public OriPicSymbol() {
		
	}
	
	public OriPicSymbol(double x, double y, int type) {
		xPos = x;
		yPos = y;
		this.type = type;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public double getDegrees() {
		return degrees;
	}

	public void setDegrees(double degrees) {
		this.degrees = degrees;
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public double getScale() {
		if (type == TYPE_ROTATION) {
			return scale + 0.5;
		} else if (type == TYPE_NEXT_VIEW_HERE) {
			return scale + 0.25;
		} else if (type == TYPE_HOLD) {
			return scale + 0.25;
		} else if (type == TYPE_HOLD_AND_PULL) {
			return scale + 0.5;
		} else {
			return scale;
		}
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

}
