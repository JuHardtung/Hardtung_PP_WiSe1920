package origrammer;

import java.util.ListResourceBundle;

public class StringResource_en extends ListResourceBundle {

	static final Object[][] strings = {
			{"Title_long", "Origrammer: An Origami Diagramming Tool"},
			{"Title_short", "Origrammer"},
			{"UI_selectionTool", "Selection Tool"},
			{"UI_lineInputTool", "Line Input Tool"},
			{"UI_vertexInputTool", "Vertex Input Tool"},
			{"UI_arrowInputTool", "Arrow Input Tool"},
			{"UI_symbolInputTool", "Symbol Input Tool"},
			{"UI_measureTool", "Measure Tool"},
			{"UI_fillTool", "Fill Tool"},
			{"UI_measureLength", "Length"},
			{"UI_measureAngle", "Angle"},
			{"UI_lineInputTwoVertices", "Input 2 Endpoints"},
			{"UI_lineInputIncenter", "Rabbit Ear (Triangle Incenter)"},
			{"UI_vertexInputAbsolute", "Absolute Position"},
			{"UI_vertexInputFractionOfLine", "Fraction of Line"},
			{"UI_ShowGrid", "Show Grid"},
			{"UI_gridHalf", "x2"},
			{"UI_gridDouble", "x1/2"},
			{"UI_gridSet", "Set"},
			{"UI_ShowVertices", "Show Vertices"},
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
			{"Origrammer_File", "Origrammer File"},
			{"DefaultFileName", "No Title"},
			{"Error_FileLoadFailed", "Error: Loading File failed"},
			{"Error_FileSaveFailed", "Error: Saving File failed"},
			{"Error_EmptyTitle", "Error: Title can't be empty!"},
			{"Error_EmptySideLength", "Error: Side length can't be empty!"},
			{"Error_EmptyWidth", "Error: Paper width can't be empty!"},
			{"Error_EmptyHeight", "Error: Paper height can't be empty!"},
			{"Error_EmptyLeaderTextField", "Error: The TextField for a leader can't be empty!"},
			{"Error_EmptyRepetitionTextField", "Error: The TextField for a RepetitionBox can't be empty!"},
			{"Warning_SameNameFileExists", "Warning: File with this name already exists! Want to overwrite?"},
			{"DialogTitle_FileSave", "Saving File"}
	};
	
	protected Object[][] getContents() {
		return strings;
	}

}
