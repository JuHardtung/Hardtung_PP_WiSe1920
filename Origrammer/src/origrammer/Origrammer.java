package origrammer;
import java.awt.EventQueue;
import java.util.ArrayList;

import origrammer.geometry.OriLine;

public class Origrammer {

	
	public static Diagram diagram;
	public static MainFrame mainFrame;
	public static ArrayList<OriLine> lines = new ArrayList<>();
	
	
	
	public static void main(String[] args) {
		
		//int mainFrameWidth = 800;
		//int mainFrameHeight = 800;
		
		diagram = new Diagram(Constants.DEFAULT_PAPER_SIZE);
		mainFrame = new MainFrame();
		
//		EventQueue.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
//                MainFrame mainFrame = new MainFrame();
//                mainFrame.setVisible(true);
//            }
//        });
		
		
	}

}
