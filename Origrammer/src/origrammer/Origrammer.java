package origrammer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import origrammer.geometry.OriLine;

public class Origrammer {

	public static String VERSION;
	public static Diagram diagram;
	private static Step step;
	public static MainFrame mainFrame;
	public static ArrayList<OriLine> lines = new ArrayList<>();
	
	public static ResourceBundle res;
	private static double size = Constants.DEFAULT_PAPER_SIZE;
	
	
	public static void main(String[] args) {
		
		res = ResourceBundle.getBundle("origrammer.StringResource", Locale.ENGLISH);
		
		VERSION = "  v1.2.0";

		Globals.newStepOptions = Constants.NewStepOptions.PASTE_DEFAULT_PAPER;
		//TODO: get the real faceUp and faceDown Colors
		diagram = new Diagram(size, Color.WHITE, Globals.DEFAULT_PAPER_COLOR); 
		step = new Step();
		diagram.steps.add(step);
		Globals.newStepOptions = Constants.NewStepOptions.COPY_LAST_STEP;
		
		mainFrame = new MainFrame();

	}

}
