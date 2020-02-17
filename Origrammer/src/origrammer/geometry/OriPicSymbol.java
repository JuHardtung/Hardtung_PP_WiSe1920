package origrammer.geometry;

import javax.swing.JLabel;
import javax.vecmath.Vector2d;

public class OriPicSymbol {

	
	final public static int TYPE_NONE = 0;
	final public static int TYPE_ROTATION = 1;
	final public static int TYPE_HOLD = 2;
	final public static int TYPE_HOLD_AND_PULL = 3;
	final public static int TYPE_NEXT_VIEW_HERE = 4;
	
	public JLabel label = new JLabel();
	private int type = TYPE_NONE;
	private Vector2d position;
	private int width;
	private int height;
	private double scale = 0;
	private boolean isSelected;
	private double degrees = 0;
	
	public OriPicSymbol() {
		
	}
	
	public OriPicSymbol(Vector2d position, int type) {
		this.position = position;
		this.type = type;
	}
	
	public OriPicSymbol(Vector2d position, int width, int height, double scale, double degrees) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.scale = scale;
		this.degrees = degrees;
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

	public Vector2d getPosition() {
		return position;
	}

	public void setPosition(Vector2d position) {
		this.position = position;
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
