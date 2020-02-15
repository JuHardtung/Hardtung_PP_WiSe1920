package origrammer.geometry;

import java.awt.Rectangle;

import javax.swing.JLabel;

public class OriLeaderBox {

	final public static int TYPE_NONE = 0;
	final public static int TYPE_LEADER = 1;
	final public static int TYPE_REPETITION = 2;
	
	public OriLine line = new OriLine();
	private JLabel label = new JLabel();
	private int type = TYPE_NONE;
	private boolean isSelected;
	
	public OriLeaderBox() {
		
	}
	
	public OriLeaderBox(OriLine line, JLabel label) {
		this.line = line;
		this.label = label;
	}
	
	public OriLeaderBox(OriLine line, JLabel label, int type) {
		this.line = line;
		this.label = label;
		this.type = type;
	}
	
	public void moveBy(double x, double y) {
		x = Math.round(x);
		y = Math.round(y);
		line.getP0().x += x;
		line.getP0().y += y;
		line.getP1().x += x;
		line.getP1().y += y;
		
		Rectangle oldBounds = label.getBounds();
		label.setBounds(oldBounds.x + (int) x, oldBounds.y + (int) y, 
				oldBounds.width, oldBounds.height);
	}

	public OriLine getLine() {
		return line;
	}

	public void setLine(OriLine line) {
		this.line = line;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel leaderLabel) {
		this.label = leaderLabel;
	}
	
	public void setText(String leaderText) {
		label.setText(leaderText);
	}
	
	public void setPosition(Rectangle rect) {
		label.setBounds(rect);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
}
