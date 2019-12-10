package origrammer;

import origrammer.geometry.OriLine;

public class Globals {
	
	public static Constants.LineEditMode lineEditMode = Constants.LineEditMode.INPUT_LINE;
	public static Constants.ToolbarMode editMode = Constants.ToolbarMode.INPUT_LINE;

	public static int gridDivNum = Config.DEFAULT_GRID_DIV_NUM;
	public static boolean dispVertex = true;
	public static boolean bDispCrossLine = false;
	public static int inputLineType = OriLine.TYPE_VALLEY;
	public static int inputArrowType = OriArrow.TYPE_VALLEY;

}
