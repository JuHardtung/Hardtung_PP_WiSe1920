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
	private Vector2d position; //TODO: redundant because of label.getBounds()???
	private double scale = 0;
	private boolean isSelected;
	private double degrees = 0;
	
	
	public OriPicSymbol() {
	}
	
	public OriPicSymbol(OriPicSymbol ps) {
		this.label = new JLabel();
		this.label.setBounds(ps.label.getBounds());
		this.label.setIcon(ps.label.getIcon());
		this.type = ps.type;
		this.position = ps.position;
		this.scale = ps.scale;
		this.degrees = ps.degrees;
	}
	
	public OriPicSymbol(Vector2d position, int type) {
		this.position = position;
		this.type = type;
	}
	
	public OriPicSymbol(Vector2d position, double scale, double degrees, int type) {
		this.position = position;
		this.scale = scale;
		this.degrees = degrees;
		this.type = type;
	}
	
	public void moveBy(double xTrans, double yTrans) {
		position.x += xTrans;
		position.y += yTrans;
		label.setBounds((int) Math.round(position.x), (int) Math.round(position.y),
						label.getWidth(), label.getHeight());
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

	/**
	 * 
	 * @return The adjusted base scale for each different Type of OriPicSymbol
	 */
	public double getAdjustedScale() {
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
	
	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

}
