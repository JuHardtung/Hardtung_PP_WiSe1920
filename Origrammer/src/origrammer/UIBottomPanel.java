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
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import origrammer.geometry.OriArrow;
import origrammer.geometry.OriLine;

public class UIBottomPanel extends JPanel implements ActionListener, PropertyChangeListener, KeyListener {

	private JFormattedTextField foldDescr = new JFormattedTextField();
	
	private JButton stepBack = new JButton("<");
	private JFormattedTextField currentStepTF = new JFormattedTextField(new DecimalFormat("###"));
	private JButton stepForth = new JButton(">");	
	
	private JPanel newStepPanel = new JPanel();
	private JRadioButton newEmptyStep = new JRadioButton("Empty Step", false);
	private JRadioButton newCopiedStep = new JRadioButton("Copy last Step", true);
	private JRadioButton newBaseShapeStep = new JRadioButton("Basic Paper Shape", false);
	private ButtonGroup newStepGroup;

	private MainScreen screen;
	
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
		foldDescr.setPreferredSize(new Dimension(400, 20));	
		foldDescrPanel.add(foldDescrTitel);
		foldDescrPanel.add(foldDescr);
		foldDescrPanel.setLayout(new GridLayout(2, 1, 10, 2));
		//foldDescrPanel.setLayout(new BoxLayout(foldDescrPanel, BoxLayout.Y_AXIS));
		
		//STEP NAVIGATION panel and positioning
		JPanel stepNavigation = new JPanel();
		stepBack.setPreferredSize(new Dimension(30, 10));
		stepForth.setPreferredSize(new Dimension(30, 10));
		currentStepTF.setHorizontalAlignment(JFormattedTextField.CENTER);
		currentStepTF.setPreferredSize(new Dimension(30, 10));
		currentStepTF.setMaximumSize(new Dimension(30, 10));
		currentStepTF.setValue(Globals.currentStep);
		stepNavigation.add(stepBack);
		stepNavigation.add(currentStepTF);
		stepNavigation.add(stepForth);
		stepNavigation.setLayout(new GridLayout(1, 3, 100, 2));
		
		JPanel stepDescrAndNavPanel = new JPanel();
		stepDescrAndNavPanel.add(foldDescrPanel);
		stepDescrAndNavPanel.add(stepNavigation);
		stepDescrAndNavPanel.setLayout(new GridLayout(2, 1, 10, 2));
		
		add(Box.createRigidArea(new Dimension(190,70)));
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
		
		newStepGroup = new ButtonGroup();
		newStepGroup.add(newEmptyStep);
		newStepGroup.add(newBaseShapeStep);
		newStepGroup.add(newCopiedStep);
		
		newStepPanel.add(newEmptyStep);
		newStepPanel.add(newCopiedStep);
		newStepPanel.add(newBaseShapeStep);
		newStepPanel.setLayout(new BoxLayout(newStepPanel, BoxLayout.PAGE_AXIS));
		
		add(newStepPanel);
		
		
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

			//copy last step
			if ((int) currentStepTF.getValue() == Origrammer.diagram.steps.size()-1) {
				Step newStep = new Step();

				if (newCopiedStep.isSelected()) {
					for (int i=0; i<Origrammer.diagram.steps.get(prevStep).lines.size(); i++) {
						newStep.lines.add(Origrammer.diagram.steps.get(prevStep).lines.get(i));
					}

					for (int i=0; i<Origrammer.diagram.steps.get(prevStep).vertices.size(); i++) {
						newStep.vertices.add(Origrammer.diagram.steps.get(prevStep).vertices.get(i));
					}		

					for (int i=0; i<Origrammer.diagram.steps.get(prevStep).arrows.size(); i++) {
						OriArrow tmpArrow = new OriArrow();
						tmpArrow.setPosition(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getPosition());
						tmpArrow.setWidth(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getWidth());
						tmpArrow.setHeight(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getHeight());
						tmpArrow.setType(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getType());
						tmpArrow.setScale(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getScale());
						tmpArrow.setDegrees(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getDegrees());
						tmpArrow.setSelected(Origrammer.diagram.steps.get(prevStep).arrows.get(i).isSelected());
						tmpArrow.setLabel(Origrammer.diagram.steps.get(prevStep).arrows.get(i).getLabel());

						newStep.arrows.add(tmpArrow);
					}
					
					for (int i=0; i<Origrammer.diagram.steps.get(prevStep).filledFaces.size(); i++) {
						newStep.filledFaces.add(Origrammer.diagram.steps.get(prevStep).filledFaces.get(i));
					}
					
					for (int i=0; i<Origrammer.diagram.steps.get(prevStep).leaderBoxSymbols.size(); i++) {
						newStep.leaderBoxSymbols.add(Origrammer.diagram.steps.get(prevStep).leaderBoxSymbols.get(i));
					}
					
					for (int i=0; i<Origrammer.diagram.steps.get(prevStep).picSymbols.size(); i++) {
						newStep.picSymbols.add(Origrammer.diagram.steps.get(prevStep).picSymbols.get(i));
					}
					
					for (int i=0; i<Origrammer.diagram.steps.get(prevStep).geomSymbols.size(); i++) {
						newStep.geomSymbols.add(Origrammer.diagram.steps.get(prevStep).geomSymbols.get(i));
					}
					
					for (int i=0; i<Origrammer.diagram.steps.get(prevStep).equalDistSymbols.size(); i++) {
						newStep.equalDistSymbols.add(Origrammer.diagram.steps.get(prevStep).equalDistSymbols.get(i));
					}
					
					for (int i=0; i<Origrammer.diagram.steps.get(prevStep).equalAnglSymbols.size(); i++) {
						newStep.equalAnglSymbols.add(Origrammer.diagram.steps.get(prevStep).equalAnglSymbols.get(i));
					}
					
					for (int i=0; i<Origrammer.diagram.steps.get(prevStep).pleatCrimpSymbols.size(); i++) {
						newStep.pleatCrimpSymbols.add(Origrammer.diagram.steps.get(prevStep).pleatCrimpSymbols.get(i));
					}
					
					
				} else if (newBaseShapeStep.isSelected()) {
					//new step with previously chosen paper shape
					ArrayList<OriLine> newLines = Origrammer.diagram.steps.get(Globals.currentStep-1).getEdgeLines();
					newStep.lines.addAll(newLines);
				} else if (newEmptyStep.isSelected()) {
					//new empty step
					newStep.lines.clear();
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
		
		if (Origrammer.diagram.steps.size() == Globals.currentStep+1) {
			//newStepPanel.setVisible(true);
			newEmptyStep.setEnabled(true);
			newCopiedStep.setEnabled(true);
			newBaseShapeStep.setEnabled(true);
			
		} else {
			//newStepPanel.setVisible(false);
			newEmptyStep.setEnabled(false);
			newCopiedStep.setEnabled(false);
			newBaseShapeStep.setEnabled(false);
		}
		currentStepTF.setValue(Globals.currentStep);
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
