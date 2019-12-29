package origrammer;

import java.awt.*;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.*;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.ColorPicker;

public class PreferenceDialog extends JDialog implements ActionListener, ComponentListener {

	String[] mountainStyleOptions = {"Dash Dot", "Dash Dot Dot"};
	String[] outsideReverseOptions = {"AoM + AoA", "2 AoM"};
	String[] rabbitEarOptions = {"2 small AoM + 1 big AoM", "3 small AoM"};
	
	private JComboBox mountainStyleCB = new JComboBox(mountainStyleOptions);
	private JComboBox outsideReverseCB = new JComboBox(outsideReverseOptions);
	private JComboBox rabbitEarCB = new JComboBox(rabbitEarOptions);
	
	private JFormattedTextField paperSizeTextField;

	
	private JPanel jContentPane = null;
	
	JButton button;
	JColorChooser colorChooser;
	JPanel preferencePanel;
	private boolean toggled = false;
	
	public PreferenceDialog(JFrame frame) {
		super(frame);
		init();
	}
	
	private void init() {
		this.addComponentListener(this);
		this.setSize(750, 550);
		this.setContentPane(getJContentPane());
		this.setTitle("Preferences");	
	}
	
	private JPanel getJContentPane() {
		System.out.println("MountainStyle: " + Globals.mountainFoldStyle);
		System.out.println("OutsideReversse: " + Globals.outsideReverseStyle);
		
			if(Globals.mountainFoldStyle == Constants.MountainFoldStyle.DASH_DOT) {
				mountainStyleCB.setSelectedIndex(0);
			} else if (Globals.mountainFoldStyle == Constants.MountainFoldStyle.DASH_DOT_DOT) {
				mountainStyleCB.setSelectedIndex(1);

			}
			
			if(Globals.outsideReverseStyle == Constants.OutsideReverseStyle.AOM_AOA) {
				outsideReverseCB.setSelectedIndex(0);
			} else if (Globals.outsideReverseStyle == Constants.OutsideReverseStyle.AOM_AOM) {
				outsideReverseCB.setSelectedIndex(1);

			}
			
			if(Globals.rabbitEarStyle == Constants.RabbitEarStyle.SAOM_SAOM_BAOM) {
				rabbitEarCB.setSelectedIndex(0);
			} else if (Globals.rabbitEarStyle == Constants.RabbitEarStyle.BAOM_BAOM_BAOM) {
				rabbitEarCB.setSelectedIndex(1);

			}
		
			mountainStyleCB.addActionListener(this);
			outsideReverseCB.addActionListener(this);
			rabbitEarCB.addActionListener(this);
			
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

			
			SpringLayout fileLayout = new SpringLayout();
			JPanel filePanel = new JPanel();	
			
			JLabel paperSizeLabel = new JLabel("Paper Size: ");
			paperSizeTextField = new JFormattedTextField(new DecimalFormat("####"));
			
			paperSizeTextField.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void changedUpdate(DocumentEvent e) {
					changed();
				}
				@Override
				public void insertUpdate(DocumentEvent e) {
					changed();
				}
				@Override
				public void removeUpdate(DocumentEvent e) {
					changed();
				}
				
				public void changed() {

				}
			});
			filePanel.add(paperSizeLabel);
			filePanel.add(paperSizeTextField);
			
			
			JLabel fileLabel = new JLabel("Paper Color: ", JLabel.TRAILING);
			colorChooser = new JColorChooser(Color.LIGHT_GRAY);
			colorChooser.setBorder(null);
			colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					colorChanged();
				}
			});
			
			filePanel.add(fileLabel);
			filePanel.add(colorChooser);
			filePanel.setLayout(fileLayout);
			SpringUtilities.makeCompactGrid(filePanel, 2, 2, 6, 6, 6, 6);

			
			jContentPane = new JPanel();
			jContentPane.add(preferencePanel);
			jContentPane.add(separator);
			jContentPane.add(filePanel);
			
			SpringLayout layout = new SpringLayout();
			jContentPane.setLayout(layout);
			SpringUtilities.makeCompactGrid(jContentPane, 3, 1, 6, 6, 6, 6);
			
		}
		return jContentPane;
	}
	
	private void colorChanged() {
		//button.setBackground(colorChooser.getSelectionModel().getSelectedColor());
		Globals.DEFAULT_PAPER_COLOR = colorChooser.getSelectionModel().getSelectedColor();
		System.out.println("DEFAULT COLOR: " + Globals.DEFAULT_PAPER_COLOR);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
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
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

}


