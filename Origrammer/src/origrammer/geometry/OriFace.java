package origrammer.geometry;

import java.awt.geom.GeneralPath;


public class OriFace {
	
	
	public GeneralPath path;
	private boolean selected;
	private boolean isFaceUp;
	
	
	public OriFace(GeneralPath path, boolean selected, boolean isFaceUp) {
		this.path = path;
		this.selected = selected;
		this.isFaceUp = isFaceUp;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isFaceUp() {
		return isFaceUp;
	}

	public void setFaceUp(boolean isFaceUp) {
		this.isFaceUp = isFaceUp;
	}

}
