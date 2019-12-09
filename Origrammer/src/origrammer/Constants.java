package origrammer;

public class Constants {

	public static enum LineEditMode {
		NONE,
		INPUT_LINE,
		DELETE_LINE,
		CHANGE_LINE_TYPE
	};
	
	public static enum EditMode {
		NONE,
		INPUT_LINE,
		CHANGE_LINE_TYPE,
		INPUT_ARROW,
		ADD_VERTEX,
		DELETE_VERTEX,
		SELECT_LINE,
		DELETE_LINE
	}
	
	final public static double DEFAULT_PAPER_SIZE = 400;
}
