package origrammer;

import java.awt.*;
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
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

public class PreferenceDialog extends JDialog implements ActionListener, ComponentListener {

	String[] mountainStyleOptions = {"Dash Dot", "Dash Dot Dot"};
	String[] outsideReverseOptions = {"AoM + AoA", "2 AoM"};
	String[] rabbitEarOptions = {"2 small AoM + 1 big AoM", "3 small AoM"};
	
	private JComboBox mountainStyleCB = new JComboBox(mountainStyleOptions);
	private JComboBox outsideReverseCB = new JComboBox(outsideReverseOptions);
	private JComboBox rabbitEarCB = new JComboBox(rabbitEarOptions);
	
	private JTextField recPaperWidthTF;
	private JTextField recPaperHeightTF;

	private JButton applyButton = new JButton(Origrammer.res.getString("Pref_applyButton"));
	private JButton okButton = new JButton(Origrammer.res.getString("Pref_okButton"));
	
	private JPanel jContentPane = null;
	
	JButton button;
	JColorChooser colorChooser;
	JPanel preferencePanel;
	private boolean toggled = false;
	MainScreen __screen;
	
	public PreferenceDialog(JFrame frame, MainScreen __screen) {
		super(frame);
		this.__screen = __screen;
		init();
	}
	
	private void init() {
		this.addComponentListener(this);
		this.setSize(770, 550);
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

			
			SpringLayout fileLayout = new SpringLayout();
			JPanel filePanel = new JPanel();	
			
			JLabel paperSizeLabel = new JLabel("Recommended Paper Size: ");

			
			recPaperWidthTF = new JTextField();
			recPaperHeightTF = new JTextField();
			
			recPaperWidthTF.setText(Integer.toString(Origrammer.diagram.recPaperWidth));
			recPaperHeightTF.setText(Integer.toString(Origrammer.diagram.recPaperHeight));
			
			PlainDocument docW = (PlainDocument) recPaperWidthTF.getDocument();
			docW.setDocumentFilter(new IntFilter());
			PlainDocument docH = (PlainDocument) recPaperHeightTF.getDocument();
			docH.setDocumentFilter(new IntFilter());
			
			
			//PAPER SIZE panel and positioning
			JPanel recPaperSizePanel = new JPanel();
			
			filePanel.add(paperSizeLabel);
			recPaperWidthTF.setPreferredSize(new Dimension(50, 20));
			recPaperHeightTF.setPreferredSize(new Dimension(50, 20));

			recPaperSizePanel.add(recPaperWidthTF);
			recPaperSizePanel.add(recPaperHeightTF);
			
			filePanel.add(recPaperSizePanel);
						
			JLabel fileLabel = new JLabel("Paper Color: ", JLabel.TRAILING);
			colorChooser = new JColorChooser(Globals.DEFAULT_PAPER_COLOR);
			colorChooser.setBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()));
			
			AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
			
	        for (int i = 0; i < panels.length; i++) {
	            if (!panels[i].getDisplayName().equals("RGB")) {
	                colorChooser.removeChooserPanel(panels[i]);
	            } else {
	                JPanel panel = panels[i];
	            }
	        }
			
			filePanel.add(fileLabel);
			filePanel.add(colorChooser);
			filePanel.setLayout(fileLayout);
			SpringUtilities.makeCompactGrid(filePanel, 2, 2, 6, 6, 6, 6);
			
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
			//jContentPane.add(filePanel);
			jContentPane.add(buttonPanel);
			
			SpringLayout layout = new SpringLayout();
			jContentPane.setLayout(layout);
			SpringUtilities.makeCompactGrid(jContentPane, 3, 1, 6, 6, 6, 6);
		}
		return jContentPane;
	}
	
	private void colorChanged() {
		Globals.DEFAULT_PAPER_COLOR = colorChooser.getSelectionModel().getSelectedColor();
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


