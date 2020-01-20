package origrammer.geometry;

import java.awt.Rectangle;

import javax.swing.JLabel;

public class OriRepetitionBox {

	
	public OriLine line = new OriLine();
	private JLabel repetitionLabel = new JLabel();
	private boolean isSelected;
	
	public OriRepetitionBox() {
		
	}
	
	public OriRepetitionBox(OriLine line, JLabel repetitionLabel) {
		this.line = line;
		this.repetitionLabel = repetitionLabel;
	}
	
	public void moveBy(double x, double y) {
		x = Math.round(x);
		y = Math.round(y);
		line.p0.x += x;
		line.p0.y += y;
		line.p1.x += x;
		line.p1.y += y;
		
		Rectangle oldBounds = repetitionLabel.getBounds();
		repetitionLabel.setBounds(oldBounds.x + (int) x, oldBounds.y + (int) y, 
				oldBounds.width, oldBounds.height);
	}

	public OriLine getLine() {
		return line;
	}

	public void setLine(OriLine line) {
		this.line = line;
	}

	public JLabel getLabel() {
		return repetitionLabel;
	}

	public void setLabel(JLabel repetitionLabel) {
		this.repetitionLabel = repetitionLabel;
	}
	
	public void setText(String leaderText) {
		repetitionLabel.setText(leaderText);
	}
	
	public void setPosition(Rectangle rect) {
		repetitionLabel.setBounds(rect);
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
