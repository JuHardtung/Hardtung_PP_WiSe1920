package origrammer;

import java.util.ListResourceBundle;

public class StringResource extends ListResourceBundle {

	static final Object[][] strings = {
			{"Title", "Origrammer: An origami diagramming tool"},
			{"UI_selectionTool", "Selection Tool"},
			{"UI_lineInputTool", "Line Input Tool"},
			{"UI_arrowInputTool", "Arrow Input Tool"},
			{"UI_measureTool", "Measure Tool"},
			{"UI_fillTool", "Fill Tool"},
			{"UI_measureLength", "Measure Length"},
			{"UI_measureAngle", "Measure Angle"},
			{"UI_lineInputTwoVertices", "Input 2 endpoints"},
			{"UI_lineInputIncenter", "Rabbit Ear (Triangle Incenter)"},
			{"UI_ShowGrid", "Show Grid"},
			{"UI_gridHalf", "x2"},
			{"UI_gridDouble", "x1/2"},
			{"UI_gridSet", "Set"},
			{"UI_ShowVertices", "Show vertices"},
			{"UI_ShowFilledFaces", "Show Filled Faces"},
			{"Pref_applyButton", "Apply"},
			{"Pref_okButton", "Ok"},
			{"Pref_cancelButton", "Cancel"},
			{"MenuLineInput", "Input Lines"},
			{"MenuArrowInput", "Input Arrows"},
			{"MenuLines", "Lines"},
			{"MenuValleyLine", "Valley Fold"},
			{"MenuMountainLine", "Mountain Fold"},
			{"MenuXRayLine", "X-Ray Fold"},
			{"MenuEdgeLine", "Edge Line"},
			{"MenuArrows", "Arrows"},
			{"TT_LineCB", "Choose the type of line you want to place."},
			{"TT_ArrowCB", "Choose the type of arrow you want to place."},
			{"Origrammer_File", "Origrammer file"},
			{"DefaultFileName", "No Title"},
			{"Error_FileLoadFailed", "Error: Loading File failed"},
			{"Error_FileSaveFailed", "Error: Saving File failed"},
			{"Error_EmptyTitle", "Error: Title can't be empty!"},
			{"Error_EmptySideLength", "Error: Side Length can't be empty!"},
			{"Error_EmptyWidth", "Error: Paper Width can't be empty!"},
			{"Error_EmptyHeight", "Error: Paper Height can't be empty!"},
			{"Warning_SameNameFileExists", "Warning: File with the same name already exists!"},
			{"DialogTitle_FileSave", "Save!"}
	};
	
	
	protected Object[][] getContents() {
		return strings;
	}

}
