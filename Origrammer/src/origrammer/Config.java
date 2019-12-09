package origrammer;

import java.awt.BasicStroke;
import java.awt.Color;


public class Config {

	
	final public static Color LINE_COLOR_EDGE = Color.BLACK;
	final public static Color LINE_COLOR_VALLEY = Color.BLUE;
	final public static Color LINE_COLOR_MOUNTAIN = Color.RED;
	final public static Color LINE_COLOR_XRAY = Color.LIGHT_GRAY;
	final public static Color LINE_COLOR_SELECTED = Color.GREEN;
	
	final static float dash[] = {3.0f};
	

	final public static BasicStroke STROKE_EDGE = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	final public static BasicStroke STROKE_VALLEY = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
	final public static BasicStroke STROKE_MOUNTAIN = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	final public static BasicStroke STROKE_XRAY = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	final public static BasicStroke STROKE_SELECTED = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
	final public static BasicStroke STROKE_GRID = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

	
	final public static int DEFAULT_GRID_DIV_NUM = 4;

}
