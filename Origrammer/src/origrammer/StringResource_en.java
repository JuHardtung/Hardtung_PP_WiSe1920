package origrammer;

import java.util.ListResourceBundle;

public class StringResource_en extends ListResourceBundle {

	static final Object[][] strings = {
			{"Title", "Origrammer: An origami diagramming tool"},
			
			{"UI_ShowGrid", "Show Grid"},
			{"UI_gridHalf", "x2"},
			{"UI_gridDouble", "x1/2"},
			{"UI_gridSet", "Set"}
	};
	
	
	protected Object[][] getContents() {
		return strings;
	}

}
