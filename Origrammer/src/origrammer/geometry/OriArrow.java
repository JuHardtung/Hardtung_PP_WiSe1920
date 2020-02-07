package origrammer.geometry;

import javax.swing.JLabel;
import javax.vecmath.Vector2d;

public class OriArrow {
	
	final public static int TYPE_NONE = 0;
	//ARROWS OF MOTION
	final public static int TYPE_VALLEY = 1;
	final public static int TYPE_MOUNTAIN = 2;
	final public static int TYPE_TURN_OVER = 3;
	//ARROWS OF ACTION
	final public static int TYPE_PUSH_HERE = 4;
	final public static int TYPE_PULL_HERE = 5;
	final public static int TYPE_INFLATE_HERE = 6;
	
	final public static int VALLEY_ARROW_WIDTH = 50;
	final public static int VALLEY_ARROW_HEIGHT = 50;

	private int type = TYPE_NONE;
	private Vector2d position;
	private int width;
	private int height;
	private double scale = 0.0;
	private boolean selected;
	private double degrees = 0;

	private JLabel arrowLabel = new JLabel();
	
	
	public OriArrow() {
		
	}
	
	public OriArrow(OriArrow a) {
		this.position = a.position;
		this.width = a.width;
		this.height = a.height;
		this.type = a.type;
		
	}
	
	public OriArrow(Vector2d position, int width, int height, int type) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.type = type;
	}
	
	
	public OriArrow(Vector2d position, int type) {
		this.position = position;
		this.width = VALLEY_ARROW_WIDTH;
		this.height = VALLEY_ARROW_HEIGHT;
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
		if (type == TYPE_VALLEY) {
			return scale + 1.5;
		} else if (type == TYPE_MOUNTAIN) {
			return scale + 0.8;
		} else if (type == TYPE_TURN_OVER) {
			return scale + 1.5;
		} else if (type == TYPE_PUSH_HERE) {
			return scale + 0.8;
		} else if (type == TYPE_PULL_HERE) {
			return scale + 1.0;
		} else if (type == TYPE_INFLATE_HERE) {
			return scale + 1.0;
		} else {
			return scale;
		}
		
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
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

	public JLabel getLabel() {
		return arrowLabel;
	}

	public void setLabel(JLabel arrowLabel) {
		this.arrowLabel = arrowLabel;
	}

	@Override
	public String toString() {
		return "OriArrow [type=" + type + ", xPos=" + position.x + ", yPos=" + position.y + ", width=" + width + ", height="
				+ height + ", scale=" + scale + ", selected=" + selected + ", degrees=" + degrees + "]";
	}
	

}
