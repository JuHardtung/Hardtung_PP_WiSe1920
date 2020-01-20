package origrammer.geometry;

import java.awt.Rectangle;

import javax.swing.JLabel;

public class OriLeader {

	
	public OriLine line = new OriLine();
	private JLabel leaderLabel = new JLabel();
	private boolean isSelected;
	
	public OriLeader() {
		
	}
	
	public OriLeader(OriLine leader, JLabel leaderLabel) {
		this.line = leader;
		this.leaderLabel = leaderLabel;
	}
	
	public void moveBy(double x, double y) {
		x = Math.round(x);
		y = Math.round(y);
		line.p0.x += x;
		line.p0.y += y;
		line.p1.x += x;
		line.p1.y += y;
		
		Rectangle oldBounds = leaderLabel.getBounds();
		leaderLabel.setBounds(oldBounds.x + (int) x, oldBounds.y + (int) y, 
				oldBounds.width, oldBounds.height);
	}

	public OriLine getLeader() {
		return line;
	}

	public void setLeader(OriLine leader) {
		this.line = leader;
	}

	public JLabel getLabel() {
		return leaderLabel;
	}

	public void setLabel(JLabel leaderLabel) {
		this.leaderLabel = leaderLabel;
	}
	
	public void setText(String leaderText) {
		leaderLabel.setText(leaderText);
	}
	
	public void setPosition(Rectangle rect) {
		leaderLabel.setBounds(rect);
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
}
