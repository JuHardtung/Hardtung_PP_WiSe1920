package origrammer;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import origrammer.geometry.OriLine;

public class Origrammer {

	public static String TITLE;
	public static Diagram diagram;
	public static MainFrame mainFrame;
	public static ArrayList<OriLine> lines = new ArrayList<>();
	
	public static ResourceBundle res;
	
	
	
	public static void main(String[] args) {
		
		//int mainFrameWidth = 800;
		//int mainFrameHeight = 800;
		
		
		res = ResourceBundle.getBundle("origrammer.StringResource", Locale.ENGLISH);
		
		TITLE = Origrammer.res.getString("Title") + "  v0.01";
		
		diagram = new Diagram(Constants.DEFAULT_PAPER_SIZE);
		mainFrame = new MainFrame();
		
		
	}

}
