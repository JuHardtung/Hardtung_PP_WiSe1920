package origrammer.geometry;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;
import javax.swing.JTextField;

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
	
	public OriLeaderBox(OriLeaderBox lb) {
		this.line = new OriLine(lb.line);
		this.label = new JLabel();
		label.setBounds(lb.getLabel().getBounds());
		label.setText(lb.getLabel().getText());
		this.type = lb.type;
		this.isSelected = lb.isSelected;
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
	
	public Rectangle getLabelBounds(Graphics2D g2d) {
		//get JLabel size that fits the text
		Rectangle2D labelBounds = g2d.getFontMetrics().getStringBounds(getLabel().getText(), g2d);
		Rectangle tmpRect = new Rectangle();
		double width =  labelBounds.getWidth() + 10;
		double height = labelBounds.getHeight();
		
		//get JLabel placement depending on the line
		if (line.getP0().y < line.getP1().y) {
			if (line.getP0().x < line.getP1().x) {
				//bottom right
				tmpRect.setRect(line.getP1().x, line.getP1().y + 1, width, height + 2);
			} else {
				//bottom left
				tmpRect.setRect(line.getP1().x - width + 1, line.getP1().y + 1, width, height + 2);
			}
		} else {
			if (line.getP0().x < line.getP1().x) {
				//top right
				tmpRect.setRect(line.getP1().x + 1, line.getP1().y-height, width, height + 2);
			} else {
				//top left
				tmpRect.setRect(line.getP1().x - width + 1, line.getP1().y - height, width, height + 2);
			}
		}
		return tmpRect;
	}

	public void setLabelBounds(Rectangle rect) {
		label.setBounds(rect);
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

	@Override
	public String toString() {
		return "OriLeaderBox [line=" + line + ", label=" + label + ", type=" + type + ", isSelected=" + isSelected
				+ "]";
	}	
}
