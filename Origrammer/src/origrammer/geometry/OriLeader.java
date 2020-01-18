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

	public OriLine getLeader() {
		return line;
	}

	public void setLeader(OriLine leader) {
		this.line = leader;
	}

	public JLabel getLeaderLabel() {
		return leaderLabel;
	}

	public void setLeaderLabel(JLabel leaderLabel) {
		this.leaderLabel = leaderLabel;
	}
	
	public void setLeaderText(String leaderText) {
		leaderLabel.setText(leaderText);
	}
	
	public void setLeaderPosition(Rectangle rect) {
		leaderLabel.setBounds(rect);
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
}
