package origrammer;

import javax.swing.JLabel;

public class OriArrow {
	
	final public static int TYPE_NONE = 0;
	//ARROWS OF MOTION
	final public static int TYPE_VALLEY = 1;
	final public static int TYPE_MOUNTAIN = 2;
	final public static int TYPE_TURN_OVER = 3;
	//ARROWS OF ACTION
	final public static int TYPE_PUSH_HERE = 4;
	final public static int TYPE_PULL_HERE = 5;
	final public static int TYPE_INFLATE_HERE = 6;
	
	public boolean selected;
	public int type = TYPE_NONE;
	public double xPos;
	public double yPos;
	public double width;
	public double height;

	
	public JLabel arrowLabel = new JLabel();
	
	
	
	public OriArrow(OriArrow a) {
		this.xPos = a.xPos;
		this.yPos = a.yPos;
		this.width = a.width;
		this.height = a.height;
		this.type = a.type;
		
	}
	
	public OriArrow(double xPos, double yPos, double width, double height, int type) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.type = type;
	}
	
	
	public OriArrow(double xPos, double yPos, int type) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = 20;
		this.height = 20;
		this.type = type;
	}
	
	
	
	
	

}
