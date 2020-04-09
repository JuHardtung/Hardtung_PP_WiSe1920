package origrammer;

import javax.swing.JLabel;

import origrammer.geometry.OriLeaderBox;
import origrammer.geometry.OriLine;

public class OriLeaderBoxProxy {

	private OriLineProxy line;
	private JLabel label;
	private int type;
	
	
	public OriLeaderBoxProxy() {
	}
	
	public OriLeaderBoxProxy(OriLeaderBox s) {
		
		this.line = new OriLineProxy(s.getLine());
		this.label = s.getLabel();
		this.type = s.getType();
	}
	
	public OriLeaderBox getSymbol() {
		return new OriLeaderBox(new OriLine(line.getP0(), line.getP1(), type), label, type);
	}

	public OriLineProxy getLine() {
		return line;
	}

	public void setLine(OriLineProxy line) {
		this.line = line;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
