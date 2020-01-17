package origrammer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class PreferenceDialog extends JDialog implements ActionListener, ComponentListener {

	String[] mountainStyleOptions = {"Dash Dot", "Dash Dot Dot"};
	String[] outsideReverseOptions = {"AoM + AoA", "2 AoM"};
	String[] rabbitEarOptions = {"2 small AoM + 1 big AoM", "3 small AoM"};
	
	private JComboBox<String> mountainStyleCB = new JComboBox<>(mountainStyleOptions);
	private JComboBox<String> outsideReverseCB = new JComboBox<>(outsideReverseOptions);
	private JComboBox<String> rabbitEarCB = new JComboBox<>(rabbitEarOptions);
	
	private JTextField recPaperWidthTF;
	private JTextField recPaperHeightTF;

	private JButton applyButton = new JButton(Origrammer.res.getString("Pref_applyButton"));
	private JButton okButton = new JButton(Origrammer.res.getString("Pref_okButton"));
	
	private JPanel jContentPane = null;
	
	JButton button;
	JColorChooser colorChooser;
	JPanel preferencePanel;
	MainScreen __screen;
	
	public PreferenceDialog(JFrame frame, MainScreen __screen) {
		super(frame);
		this.__screen = __screen;
		init();
	}
	
	private void init() {
		this.addComponentListener(this);
		this.setSize(375, 210);
		this.setContentPane(getJContentPane());
		this.setTitle("Origrammer Preferences");	
	}
	
	private JPanel getJContentPane() {
		
			if (Globals.mountainFoldStyle == Constants.MountainFoldStyle.DASH_DOT) {
				mountainStyleCB.setSelectedIndex(0);
			} else if (Globals.mountainFoldStyle == Constants.MountainFoldStyle.DASH_DOT_DOT) {
				mountainStyleCB.setSelectedIndex(1);
			}
			
			if (Globals.outsideReverseStyle == Constants.OutsideReverseStyle.AOM_AOA) {
				outsideReverseCB.setSelectedIndex(0);
			} else if (Globals.outsideReverseStyle == Constants.OutsideReverseStyle.AOM_AOM) {
				outsideReverseCB.setSelectedIndex(1);
			}
			
			if (Globals.rabbitEarStyle == Constants.RabbitEarStyle.SAOM_SAOM_BAOM) {
				rabbitEarCB.setSelectedIndex(0);
			} else if (Globals.rabbitEarStyle == Constants.RabbitEarStyle.BAOM_BAOM_BAOM) {
				rabbitEarCB.setSelectedIndex(1);
			}			

			applyButton.addActionListener(this);
			okButton.addActionListener(this);
			
		if (jContentPane == null) {
			
			JLabel mountainStyleLabel = new JLabel("Mountain Fold Style: ", JLabel.TRAILING);
			mountainStyleLabel.setLabelFor(mountainStyleCB);

			JLabel outsideReverseLabel = new JLabel("Outside Reverse Fold Style: ", JLabel.TRAILING);
			outsideReverseLabel.setLabelFor(outsideReverseCB);		
			
			JLabel rabbitEarLabel = new JLabel("Rabbit Ear Style: ", JLabel.TRAILING);
			rabbitEarLabel.setLabelFor(rabbitEarCB);
			
			preferencePanel = new JPanel();
			SpringLayout preferenceLayout = new SpringLayout();

			preferencePanel.setLayout(preferenceLayout);
			preferencePanel.add(mountainStyleLabel);
			preferencePanel.add(mountainStyleCB);
			preferencePanel.add(outsideReverseLabel);
			preferencePanel.add(outsideReverseCB);
			preferencePanel.add(rabbitEarLabel);
			preferencePanel.add(rabbitEarCB);

			preferencePanel.setLayout(preferenceLayout);
			SpringUtilities.makeCompactGrid(preferencePanel, 3, 2, 6, 6, 6, 6);
			
		
			JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

			
			//BUTTONS APPLY/OK
			SpringLayout buttonLayout = new SpringLayout();
			JPanel buttonPanel = new JPanel();
			applyButton.setHorizontalAlignment(SwingConstants.RIGHT);
			okButton.setHorizontalAlignment(SwingConstants.RIGHT);

			buttonPanel.add(applyButton);
			buttonPanel.add(okButton);
			buttonPanel.setLayout(buttonLayout);
			SpringUtilities.makeCompactGrid(buttonPanel, 1, 2, 6, 6, 6, 6);
			
			
			jContentPane = new JPanel();
			jContentPane.add(preferencePanel);
			jContentPane.add(separator);
			jContentPane.add(buttonPanel);
			
			SpringLayout layout = new SpringLayout();
			jContentPane.setLayout(layout);
			SpringUtilities.makeCompactGrid(jContentPane, 3, 1, 6, 6, 6, 6);
		}
		return jContentPane;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
	
		if (e.getSource() == applyButton) {
			applyChanges();
		} else if (e.getSource() == okButton) {
			applyChanges();
			dispose();
		}
	}
	
	private void applyChanges() {
		Object selectedMountainStyle = mountainStyleCB.getSelectedItem();
		Object selectedOutsideReverseStyle = outsideReverseCB.getSelectedItem();
		Object selectedRabbitEarStyle = rabbitEarCB.getSelectedItem();
		
		if (selectedMountainStyle == "Dash Dot") {
			Globals.mountainFoldStyle = Constants.MountainFoldStyle.DASH_DOT;
		} else if (selectedMountainStyle == "Dash Dot Dot") {
			Globals.mountainFoldStyle = Constants.MountainFoldStyle.DASH_DOT_DOT;
		}
		if (selectedOutsideReverseStyle == "AoM + AoA") {
			Globals.outsideReverseStyle = Constants.OutsideReverseStyle.AOM_AOA;
		} else if (selectedOutsideReverseStyle == "2 AoM") {
			Globals.outsideReverseStyle = Constants.OutsideReverseStyle.AOM_AOM;
		}
		if (selectedRabbitEarStyle == "2 small AoM + 1 big AoM") {
			Globals.rabbitEarStyle = Constants.RabbitEarStyle.SAOM_SAOM_BAOM;
		} else if (selectedRabbitEarStyle == "3 small AoM") {
			Globals.rabbitEarStyle = Constants.RabbitEarStyle.BAOM_BAOM_BAOM;
		}
		
		Globals.DEFAULT_PAPER_COLOR = colorChooser.getSelectionModel().getSelectedColor();
		Origrammer.diagram.recPaperWidth = Integer.parseInt(recPaperWidthTF.getText());
		Origrammer.diagram.recPaperHeight = Integer.parseInt(recPaperHeightTF.getText());
		__screen.repaint();
	}
	

	@Override
	public void componentHidden(ComponentEvent arg0) {		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {		
	}

	@Override
	public void componentShown(ComponentEvent e) {		
	}

}


