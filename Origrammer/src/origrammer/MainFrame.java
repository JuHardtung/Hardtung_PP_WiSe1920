package origrammer;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class MainFrame extends JFrame{
	MainScreen mainScreen;
	public UITopPanel uiTopPanel;
	public UISidePanel uiSidePanel;

	
	MainFrame(){
		mainScreen = new MainScreen();
		
		uiTopPanel = new UITopPanel(mainScreen);
		uiSidePanel = new UISidePanel(mainScreen, uiTopPanel);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(uiTopPanel, BorderLayout.NORTH);
		getContentPane().add(uiSidePanel, BorderLayout.WEST);
		getContentPane().add(mainScreen, BorderLayout.CENTER);
		
		pack();
		setVisible(true);
        setTitle("Origrammer Alpha v0.01");
        this.setIconImage(new ImageIcon(getClass().getResource("/images/origrammer.gif")).getImage());
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
