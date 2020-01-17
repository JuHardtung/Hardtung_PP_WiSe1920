package origrammer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class UIBottomPanel extends JPanel implements ActionListener, PropertyChangeListener, KeyListener {

	JFormattedTextField foldDescr = new JFormattedTextField();
	
	JButton stepBack = new JButton("<");
	JFormattedTextField currentStep = new JFormattedTextField(new DecimalFormat("###"));
	JButton stepForth = new JButton(">");
	Step newStep = new Step();

	MainScreen screen;
	
	public UIBottomPanel(MainScreen __screen) {
		this.screen = __screen;
		setPreferredSize(new Dimension(500, 100));
		setBackground(new Color(230, 230, 230));
		
		//ActionListener
		stepBack.addActionListener(this);
		stepForth.addActionListener(this);
		
		//FOLD DESCRIPTION panel and positioning
		JLabel foldDescrTitel = new JLabel("Fold Description", SwingConstants.CENTER);
		JPanel foldDescrPanel = new JPanel();
		foldDescr.setPreferredSize(new Dimension(300, 20));	
		foldDescrPanel.add(foldDescrTitel);
		foldDescrPanel.add(foldDescr);
		foldDescrPanel.setLayout(new GridLayout(2, 1, 10, 2));
		//foldDescrPanel.setLayout(new BoxLayout(foldDescrPanel, BoxLayout.Y_AXIS));
		
		//STEP NAVIGATION panel and positioning
		JPanel stepNavigation = new JPanel();
		stepBack.setPreferredSize(new Dimension(50, 20));
		stepForth.setPreferredSize(new Dimension(50, 20));
		currentStep.setHorizontalAlignment(JFormattedTextField.CENTER);
		currentStep.setPreferredSize(new Dimension(50, 20));
		currentStep.setValue(Globals.currentStep);
		stepNavigation.add(stepBack);
		stepNavigation.add(currentStep);
		stepNavigation.add(stepForth);
		stepNavigation.setLayout(new GridLayout(1, 3, 70, 2));
		
		JPanel stepDescrAndNavPanel = new JPanel();
		stepDescrAndNavPanel.add(foldDescrPanel);
		stepDescrAndNavPanel.add(stepNavigation);
		stepDescrAndNavPanel.setLayout(new GridLayout(2, 1, 10, 2));
		
		add(stepDescrAndNavPanel);
		
		foldDescr.getDocument().addDocumentListener(new DocumentListener() {

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
				Origrammer.diagram.steps.get(Globals.currentStep).stepDescription = foldDescr.getText();
			}
		});
		
		stepChanged();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == stepBack) {
			if (Globals.currentStep >= 1) {
				Globals.currentStep -= 1;
			}
			Origrammer.diagram.steps.get(Globals.currentStep).unselectAll();
			stepChanged();
		} else if (e.getSource() == stepForth) {
			//if currentStep == last step in diagram --> create new step
			int prevStep = Globals.currentStep;
			Globals.currentStep += 1;

			if ((int) currentStep.getValue() == Origrammer.diagram.steps.size()-1) {			
				Step newStep = new Step();
				
				for (int i=0; i<Origrammer.diagram.steps.get(prevStep).crossLines.size(); i++) {
					newStep.crossLines.add(Origrammer.diagram.steps.get(prevStep).crossLines.get(i));
				}
				
				for (int i=0; i<Origrammer.diagram.steps.get(prevStep).lines.size(); i++) {
					newStep.lines.add(Origrammer.diagram.steps.get(prevStep).lines.get(i));
				}
				
				for (int i=0; i<Origrammer.diagram.steps.get(prevStep).vertices.size(); i++) {
					newStep.vertices.add(Origrammer.diagram.steps.get(prevStep).vertices.get(i));
				}		
				
				for (int i=0; i<Origrammer.diagram.steps.get(prevStep).arrows.size(); i++) {
					OriArrow tmpArrow = new OriArrow();
					tmpArrow.setxPos(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getxPos());
					tmpArrow.setyPos(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getyPos());
					tmpArrow.setWidth(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getWidth());
					tmpArrow.setHeight(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getHeight());
					tmpArrow.setType(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getType());
					tmpArrow.setScale(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getScale());
					tmpArrow.setDegrees(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getDegrees());
					tmpArrow.setSelected(Origrammer.diagram.steps.get(prevStep).arrows.get(i).isSelected());
					tmpArrow.setArrowLabel(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getArrowLabel());

					newStep.arrows.add(tmpArrow);
				}
				
				newStep.stepNumber = Globals.currentStep;
				Origrammer.diagram.steps.add(newStep);
			}
			Origrammer.diagram.steps.get(Globals.currentStep).unselectAll();
			stepChanged();
		}
	}
	
	
	public void stepChanged() {
		if (Origrammer.diagram.steps.get(Globals.currentStep).stepDescription == null) {
			foldDescr.setValue("");
		} else {
			foldDescr.setValue(Origrammer.diagram.steps.get(Globals.currentStep).stepDescription);
		}
		currentStep.setValue(Globals.currentStep);
		screen.modeChanged();
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {		
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {		
	}

}
