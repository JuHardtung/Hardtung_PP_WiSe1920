package origrammer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class UISidePanel extends JPanel implements ActionListener, PropertyChangeListener, KeyListener {	

	public JRadioButton selectionToolRB = new JRadioButton(Origrammer.res.getString("UI_selectionTool"), false);
	private JRadioButton lineInputToolRB = new JRadioButton(Origrammer.res.getString("UI_lineInputTool"), true);
	private JRadioButton arrowInputToolRB = new JRadioButton(Origrammer.res.getString("UI_arrowInputTool"), false);
	private JRadioButton symbolInputToolRB = new JRadioButton(Origrammer.res.getString("UI_symbolInputTool"), false);

	private JRadioButton measureToolRB = new JRadioButton(Origrammer.res.getString("UI_measureTool"), false);
	private JRadioButton fillToolRB = new JRadioButton(Origrammer.res.getString("UI_fillTool"), false);
	private ButtonGroup toolbarGroup;
	
	//MEASURE PANEL
	private JPanel measureOptionsPanel = new JPanel();
	private JRadioButton measureLengthRB = new JRadioButton(Origrammer.res.getString("UI_measureLength"), true);
	private JRadioButton measureAngleRB = new JRadioButton(Origrammer.res.getString("UI_measureAngle"), true);
	private JButton copyMeasuredLength = new JButton("Copy");
	private JButton copyMeasuredAngle = new JButton("Copy");
	private ButtonGroup measureGroup;
	public JFormattedTextField measureLengthTF;
	public JFormattedTextField measureAngleTF;
	
	//INPUT LINE MODE
	private JRadioButton lineInputTwoVerticesRB = new JRadioButton(Origrammer.res.getString("UI_lineInputTwoVertices"), true);
	private JRadioButton lineInputIncenterRB = new JRadioButton(Origrammer.res.getString("UI_lineInputIncenter"), true);
	private ButtonGroup lineInputGroup;
	
	//Grid
	public JCheckBox dispGridCheckBox = new JCheckBox(Origrammer.res.getString("UI_ShowGrid"), true);
	private JButton gridHalfButton = new JButton(Origrammer.res.getString("UI_gridHalf"));
	private JButton gridDoubleButton = new JButton(Origrammer.res.getString("UI_gridDouble"));
	private JButton gridSetButton = new JButton(Origrammer.res.getString("UI_gridSet"));
	private JFormattedTextField gridTextField;
	
	private JCheckBox dispVerticesCB = new JCheckBox(Origrammer.res.getString("UI_ShowVertices"), true);
	private JCheckBox dispFilledFacedCB = new JCheckBox(Origrammer.res.getString("UI_ShowFilledFaces"), true);

	private MainScreen screen;
	private UITopPanel uiTopPanel;
	
	public UISidePanel(MainScreen __screen, UITopPanel __uiTopPanel) {
		this.screen = __screen;
		this.uiTopPanel = __uiTopPanel;
		setPreferredSize(new Dimension(200, 400));
		setBackground(new Color(230, 230, 230));
		
		//TOOLBAR Button Group
		toolbarGroup = new ButtonGroup();
		toolbarGroup.add(lineInputToolRB);
		toolbarGroup.add(arrowInputToolRB);
		toolbarGroup.add(symbolInputToolRB);
		toolbarGroup.add(fillToolRB);
		toolbarGroup.add(selectionToolRB);
		toolbarGroup.add(measureToolRB);
		
		//LINE INPUT Button Group
		lineInputGroup = new ButtonGroup();
		lineInputGroup.add(lineInputTwoVerticesRB);
		lineInputGroup.add(lineInputIncenterRB);
				
		//MEASURE Button Group
		measureGroup = new ButtonGroup();
		measureGroup.add(measureLengthRB);
		measureGroup.add(measureAngleRB);
		
		//TOOLBAR ActionListener
		selectionToolRB.addActionListener(this);
		lineInputToolRB.addActionListener(this);
		arrowInputToolRB.addActionListener(this);
		symbolInputToolRB.addActionListener(this);
		measureToolRB.addActionListener(this);
		fillToolRB.addActionListener(this);
		
		//LINE INPUT ActionListener
		lineInputTwoVerticesRB.addActionListener(this);
		lineInputIncenterRB.addActionListener(this);
		
		//MEASURE OPTIONS ActionListener
		measureLengthRB.addActionListener(this);
		measureAngleRB.addActionListener(this);
		copyMeasuredLength.addActionListener(this);
		copyMeasuredAngle.addActionListener(this);
		
		//GRID ActionListener
		dispGridCheckBox.addActionListener(this);
		gridHalfButton.addActionListener(this);
		gridDoubleButton.addActionListener(this);
		gridSetButton.addActionListener(this);
		
		//DISPL VERTICES ActionListener
		dispVerticesCB.addActionListener(this);
		dispVerticesCB.setSelected(true);
		Globals.dispVertex = true;
		
		//DISPL FILLED FACES ActionListener
		dispFilledFacedCB.addActionListener(this);
		dispFilledFacedCB.setSelected(true);
		Globals.dispFilledFaces = true;
					
		//TOOLBAR Panel and positioning
		JPanel toolbarPanel = new JPanel();
		toolbarPanel.add(selectionToolRB);
		toolbarPanel.add(lineInputToolRB);
		toolbarPanel.add(arrowInputToolRB);
		toolbarPanel.add(symbolInputToolRB);
		toolbarPanel.add(measureToolRB);
		toolbarPanel.add(fillToolRB);
		toolbarPanel.setLayout(new GridLayout(6, 1, 10, 2));
		add(toolbarPanel);
		
		//LINE INPUT Panel and positioning
		JPanel lineInputPanel = new JPanel();
		lineInputPanel.add(lineInputTwoVerticesRB);
		lineInputPanel.add(lineInputIncenterRB);
		lineInputPanel.setLayout(new GridLayout(2, 1, 10, 2));
		add(lineInputPanel);
		

		
		//MEASURING OPTIONS Panel and positioning
		JLabel measureLabel = new JLabel("Measure", SwingConstants.CENTER);
		
		JPanel measureLengthPanel = new JPanel();
		measureLengthTF = new JFormattedTextField(new DecimalFormat("###.##cm"));
		measureLengthPanel.setMaximumSize(new Dimension(50,30));
		measureLengthPanel.add(measureLengthRB);
		measureLengthPanel.add(measureLengthTF);
		measureLengthPanel.add(copyMeasuredLength);
		measureLengthPanel.setLayout(new GridLayout(1, 3, 2, 2));
		
		measureAngleTF = new JFormattedTextField(new DecimalFormat("##.##°"));
		JPanel measureAnglePanel = new JPanel();
		measureAnglePanel.add(measureAngleRB);
		measureAnglePanel.add(measureAngleTF);
		measureAnglePanel.add(copyMeasuredAngle);
		measureAnglePanel.setLayout(new GridLayout(1, 3, 2, 2));
		
		measureOptionsPanel.add(measureLabel);
		measureOptionsPanel.add(measureLengthPanel);
		measureOptionsPanel.add(measureAnglePanel);
		measureOptionsPanel.setLayout(new GridLayout(3, 1, 10, 2));
		add(measureOptionsPanel);
		measureOptionsPanel.setVisible(false);		

		//GRID Panel and positioning
		JPanel gridPanel = new JPanel();
		gridTextField = new JFormattedTextField(new DecimalFormat("#"));
		gridTextField.setColumns(3);
		gridTextField.setValue(new Integer(Config.DEFAULT_GRID_DIV_NUM));
		gridTextField.setHorizontalAlignment(JTextField.RIGHT);
		JPanel gridCustomPanel = new JPanel();
		gridCustomPanel.add(gridTextField);
		gridCustomPanel.add(gridSetButton);
		
		JPanel gridHalfDoublePanel = new JPanel();
		gridHalfDoublePanel.add(gridHalfButton);
		gridHalfDoublePanel.add(gridDoubleButton);
		
		gridPanel.add(dispGridCheckBox);
		gridPanel.add(gridCustomPanel);
		gridPanel.add(gridHalfDoublePanel);
		
		gridPanel.setLayout(new GridLayout(3, 1, 10, 2));
		gridPanel.setBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()));
		//gridPanel.setBorder(new TitledBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()), "Grid"));
		add(gridPanel);
		
		//Buttons Panel and positioning
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(dispVerticesCB);
		buttonsPanel.add(dispFilledFacedCB);
		buttonsPanel.setLayout(new GridLayout(2, 1, 10, 2));
		add(buttonsPanel);
		
		modeChanged();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == selectionToolRB) {
			Globals.toolbarMode = Constants.ToolbarMode.SELECTION_TOOL;
			modeChanged();
		} else if (e.getSource() == lineInputToolRB) {
			Globals.toolbarMode = Constants.ToolbarMode.INPUT_LINE;
			modeChanged();
		} else if (e.getSource() == arrowInputToolRB) {
			Globals.toolbarMode = Constants.ToolbarMode.INPUT_ARROW;
			modeChanged();
		} else if (e.getSource() == symbolInputToolRB) {
			Globals.toolbarMode = Constants.ToolbarMode.INPUT_SYMBOL;
			modeChanged();
		} else if (e.getSource() == measureToolRB) {
			Globals.toolbarMode = Constants.ToolbarMode.MEASURE_TOOL;
			modeChanged();
		} else if (e.getSource() == fillToolRB) {
			Globals.toolbarMode = Constants.ToolbarMode.FILL_TOOL;
			modeChanged();
		} else if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE 
				&& e.getSource() == lineInputTwoVerticesRB) {
			Globals.lineEditMode = Constants.LineInputMode.INPUT_LINE;
			modeChanged();
		} else if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE 
				&& e.getSource() == lineInputIncenterRB) {
			Globals.lineEditMode = Constants.LineInputMode.TRIANGLE_INSECTOR;
			modeChanged();
		} else if (Globals.toolbarMode == Constants.ToolbarMode.MEASURE_TOOL) {
			if (e.getSource() == measureLengthRB) {
				Globals.measureMode = Constants.MeasureMode.MEASURE_LENGTH;
			} else if (e.getSource() == measureAngleRB) {
				Globals.measureMode = Constants.MeasureMode.MEASURE_ANGLE;
			}
			if (e.getSource() == copyMeasuredLength) {
				if (measureLengthTF.getValue() != null) {
					StringSelection stringSelection = new StringSelection(measureLengthTF.getValue().toString());
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, null);
				} else {
					System.out.println("measureLengthTextField is empty!");
				}

			}
			if (e.getSource() == copyMeasuredAngle) {
				if (measureAngleTF.getValue() != null) {
					StringSelection stringSelection = new StringSelection(measureAngleTF.getValue().toString());
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(stringSelection, null);
				} else {
					System.out.println("measureAngleTextField is empty!");
				}

			}
		} else if (e.getSource() == dispGridCheckBox) {
			screen.setDispGrid(dispGridCheckBox.isSelected());
		} else if (e.getSource() == gridSetButton) {
			int customGrid = new Integer(gridTextField.getText());
			if (customGrid < 65 && customGrid > 1) {
				Globals.gridDivNum = customGrid;
				screen.repaint();	
			} else {
				gridTextField.setValue(Globals.gridDivNum);
			}
		} else if (e.getSource() == gridHalfButton) {
			if (Globals.gridDivNum > 3) {
				Globals.gridDivNum /= 2;
				gridTextField.setValue(Globals.gridDivNum);
				screen.repaint();
			}
		} else if (e.getSource() == gridDoubleButton) {
			if (Globals.gridDivNum < 33) {
				Globals.gridDivNum *= 2;
				gridTextField.setValue(Globals.gridDivNum);
				screen.repaint();
			}
		} else if (e.getSource() == dispVerticesCB) {
			Globals.dispVertex = dispVerticesCB.isSelected();
			screen.repaint();
		} else if (e.getSource() == dispFilledFacedCB) {
			Globals.dispFilledFaces = dispFilledFacedCB.isSelected();
			screen.repaint();
		}
	}
	
	
	public void modeChanged() {
		if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE) {
			lineInputTwoVerticesRB.setEnabled(true);
			lineInputIncenterRB.setEnabled(true);
		} else {
			lineInputTwoVerticesRB.setEnabled(false);
			lineInputIncenterRB.setEnabled(false);
		}
			
		if (Globals.toolbarMode == Constants.ToolbarMode.MEASURE_TOOL) {
			measureOptionsPanel.setVisible(true);		
		} else {
			measureOptionsPanel.setVisible(false);		
		}
		
		uiTopPanel.modeChanged();
		screen.modeChanged();
		repaint();
	}


	@Override
	public void keyPressed(KeyEvent arg0) {		
	}

	@Override
	public void keyReleased(KeyEvent e) {		
	}

	@Override
	public void keyTyped(KeyEvent e) {		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {		
	}
	
}
