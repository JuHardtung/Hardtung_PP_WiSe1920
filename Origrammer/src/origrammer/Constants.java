package origrammer;

import java.awt.Color;

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
		MEASURE_TOOL,
		FILL_TOOL,
		ADD_VERTEX,
		DELETE_VERTEX
	}
	
	public static enum MeasureMode {
		NONE,
		MEASURE_LENGTH,
		MEASURE_ANGLE
	}
	
	public static enum MountainFoldStyle {
		DASH_DOT,
		DASH_DOT_DOT
	}
	
	public static enum OutsideReverseStyle {
		AOM_AOA, 		//Arrow of Motion + Arrow of Action
		AOM_AOM			//2 Arrows of Motion
	}
	
	public static enum RabbitEarStyle {
		SAOM_SAOM_BAOM, //2 small Arrows of Motion + 1 big AoM
		BAOM_BAOM_BAOM  //3 small Arrows of Motion
	}
	
	public static enum NewStepOptions {
		EMPTY_STEP,
		COPY_LAST_STEP,
		PASTE_DEFAULT_PAPER
	}
	
	public static enum PaperShape {
		SQUARE,
		RECTANGLE,
		TRIANGLE,
		POLYGON
	}
	
	public static enum FaceInputDirection  {
		FACE_DOWN,
		FACE_UP
	}
	
	final public static double DEFAULT_PAPER_SIZE = 450;

}
