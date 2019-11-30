package origrammer;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

public class UIPanel extends JPanel {

	JButton buttonTest = new JButton("Test");
	MainScreen screen;
	
	public UIPanel(MainScreen __screen) {
		this.screen = __screen;
		setPreferredSize(new Dimension(200, 400));
		setBackground(Color.blue);
	}
	
}
