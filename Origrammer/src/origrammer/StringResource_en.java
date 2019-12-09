package origrammer;

import java.util.ListResourceBundle;

public class StringResource_en extends ListResourceBundle {

	static final Object[][] strings = {
			{"Title", "Origrammer: An origami diagramming tool"},
			
			{"UI_ShowGrid", "Show Grid"},
			{"UI_gridHalf", "x2"},
			{"UI_gridDouble", "x1/2"},
			{"UI_gridSet", "Set"},
			{"UI_ShowVertices", "Show vertices"},
			{"MenuLineInput", "Input Lines"},
			{"MenuArrowInput", "Input Arrows"},
			{"MenuLines", "Lines"},
			{"MenuValleyLine", "Valley Fold"},
			{"MenuMountainLine", "Mountain Fold"},
			{"MenuXRayLine", "X-Ray Fold"},
			{"MenuEdgeLine", "Edge Line"},
			{"MenuArrows", "Arrows"},
			{"TT_LineCB", "Choose the type of line you want to place."},
			{"TT_ArrowCB", "Choose the type of arrow you want to place."}
	};
	
	
	protected Object[][] getContents() {
		return strings;
	}

}