package origrammer.geometry;

import java.awt.geom.GeneralPath;


public class OriFace {
	
	
	public GeneralPath path;
	private boolean selected;
	
	
	public OriFace(GeneralPath path, boolean selected) {
		this.path = path;
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
