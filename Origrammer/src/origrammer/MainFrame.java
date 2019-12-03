package origrammer;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class MainFrame extends JFrame{
	MainScreen mainScreen;
	public UIPanel uiPanel;
	
	MainFrame(){
		mainScreen = new MainScreen();
		
		uiPanel = new UIPanel(mainScreen);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(uiPanel, BorderLayout.WEST);
		getContentPane().add(mainScreen, BorderLayout.CENTER);
		
		pack();
		setVisible(true);
        setTitle("Origrammer Alpha v0.01");
        //this.setIconImage(new ImageIcon(getClass().getResource("/images/origrammer.gif")).getImage());
        //setSize(1010, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initMainScreen();
	}

	private void initMainScreen() {

		add(mainScreen);
	}
	
	
	
	/*TODO: Top row buttons 
			(File: New, open, save, save as, export***, close)
			(Edit: Select all, unselect all, undo, redo)
	 */
}
