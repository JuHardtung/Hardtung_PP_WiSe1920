package origrammer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class PreferenceDialog extends JDialog implements ActionListener, ComponentListener {




	//PREFERENCE PANEL
	private JPanel preferencePanel = new JPanel();
	private String[] mountainStyleOptions = {"Dash Dot", "Dash Dot Dot"};
	private String[] outsideReverseOptions = {"AoM + AoA", "2 AoM"};
	private String[] rabbitEarOptions = {"2 small AoM + 1 big AoM", "3 small AoM"};
	private JComboBox<String> mountainStyleCB = new JComboBox<>(mountainStyleOptions);
	private JComboBox<String> outsideReverseCB = new JComboBox<>(outsideReverseOptions);
	private JComboBox<String> rabbitEarCB = new JComboBox<>(rabbitEarOptions);
	private JCheckBox isColoredLines = new JCheckBox();

	//BUTTONS
	private JPanel buttonPanel = new JPanel();
	private JButton applyButton = new JButton(Origrammer.res.getString("Pref_applyButton"));
	private JButton okButton = new JButton(Origrammer.res.getString("Pref_okButton"));

	private JPanel jContentPane = null;
	private MainScreen __screen;


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

		if (Globals.dispColoredLines) {
			isColoredLines.setSelected(true);
		} else {
			isColoredLines.setSelected(false);
		}

		applyButton.addActionListener(this);
		okButton.addActionListener(this);

		if (jContentPane == null) {

			addPreferencePanel();

			JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

			addButtonsPanel();

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


	private void addPreferencePanel() {
		SpringLayout preferenceLayout = new SpringLayout();

		JLabel mountainStyleLabel = new JLabel("Mountain Fold Style: ", JLabel.TRAILING);
		mountainStyleLabel.setLabelFor(mountainStyleCB);

		JLabel outsideReverseLabel = new JLabel("Outside Reverse Fold Style: ", JLabel.TRAILING);
		outsideReverseLabel.setLabelFor(outsideReverseCB);		

		JLabel rabbitEarLabel = new JLabel("Rabbit Ear Style: ", JLabel.TRAILING);
		rabbitEarLabel.setLabelFor(rabbitEarCB);

		JLabel isColoredLinesLabel = new JLabel("Use colored Lines: ", JLabel.TRAILING);
		isColoredLinesLabel.setLabelFor(isColoredLines);

		preferencePanel.setLayout(preferenceLayout);
		preferencePanel.add(mountainStyleLabel);
		preferencePanel.add(mountainStyleCB);
		preferencePanel.add(outsideReverseLabel);
		preferencePanel.add(outsideReverseCB);
		preferencePanel.add(rabbitEarLabel);
		preferencePanel.add(rabbitEarCB);
		preferencePanel.add(isColoredLinesLabel);
		preferencePanel.add(isColoredLines);

		preferencePanel.setLayout(preferenceLayout);
		SpringUtilities.makeCompactGrid(preferencePanel, 4, 2, 6, 6, 6, 6);
	}

	private void addButtonsPanel() {
		SpringLayout buttonLayout = new SpringLayout();
		applyButton.setHorizontalAlignment(SwingConstants.RIGHT);
		okButton.setHorizontalAlignment(SwingConstants.RIGHT);

		buttonPanel.add(applyButton);
		buttonPanel.add(okButton);
		buttonPanel.setLayout(buttonLayout);
		SpringUtilities.makeCompactGrid(buttonPanel, 1, 2, 6, 6, 6, 6);
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

		Globals.dispColoredLines = isColoredLines.isSelected();
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


