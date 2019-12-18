package origrammer;

public class Constants {

	public static enum LineInputMode {
		NONE,
		INPUT_LINE,
		TRIANGLE_INSECTOR,
		DELETE_LINE,
		CHANGE_LINE_TYPE
	};
	
	public static enum ToolbarMode {
		NONE,
		SELECTION_TOOL,
		INPUT_LINE,
		INPUT_ARROW,
		ADD_VERTEX,
		DELETE_VERTEX
	}
	
	final public static double DEFAULT_PAPER_SIZE = 400;
}
