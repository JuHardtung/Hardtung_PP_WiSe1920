package origrammer;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import origrammer.geometry.OriLine;

public class Origrammer {

	public static String TITLE;
	public static Diagram diagram;
	private static Step step;
	public static MainFrame mainFrame;
	public static ArrayList<OriLine> lines = new ArrayList<>();
	
	public static ResourceBundle res;
	private static double size = Constants.DEFAULT_PAPER_SIZE;
	
	
	public static void main(String[] args) {
		
		//int mainFrameWidth = 800;
		//int mainFrameHeight = 800;
		
		
		res = ResourceBundle.getBundle("origrammer.StringResource", Locale.ENGLISH);
		
		TITLE = Origrammer.res.getString("Title") + "  v0.01";
		
		diagram = new Diagram(size, Globals.DEFAULT_PAPER_COLOR);
		
		step = new Step();
		
//		OriLine l0 = new OriLine(-size/2.0, size/2.0, size/2.0,size/2.0, OriLine.TYPE_EDGE);
//		OriLine l1 = new OriLine(size/2.0, size/2.0, size/2.0,-size/2.0, OriLine.TYPE_EDGE);
//		OriLine l2 = new OriLine(size/2.0, -size/2.0, -size/2.0,-size/2.0, OriLine.TYPE_EDGE);
//		OriLine l3 = new OriLine(-size/2.0, -size/2.0, -size/2.0,size/2.0, OriLine.TYPE_EDGE);
//		step.lines.add(l0);
//		step.lines.add(l1);
//		step.lines.add(l2);
//		step.lines.add(l3);

		diagram.addStep(step);
		
		mainFrame = new MainFrame();
	}
	
	
	
	private void initFirstStep() {
		
	}

}
