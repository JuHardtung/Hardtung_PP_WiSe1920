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

import javax.swing.BoxLayout;
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
import javax.swing.text.PlainDocument;

public class UISidePanel extends JPanel implements ActionListener, PropertyChangeListener, KeyListener {	

	public JRadioButton selectionToolRB = new JRadioButton(Origrammer.res.getString("UI_selectionTool"), false);
	private JRadioButton lineInputToolRB = new JRadioButton(Origrammer.res.getString("UI_lineInputTool"), true);
	private JRadioButton vertexInputToolRB = new JRadioButton(Origrammer.res.getString("UI_vertexInputTool"), false);
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
	public 	JPanel lineInputPanel = new JPanel();
	private JRadioButton lineInputTwoVerticesRB = new JRadioButton(Origrammer.res.getString("UI_lineInputTwoVertices"), true);
	private JRadioButton lineInputIncenterRB = new JRadioButton(Origrammer.res.getString("UI_lineInputIncenter"), true);
	private ButtonGroup lineInputGroup;

	//INPUT VERTEX MODE
	public 	JPanel vertexInputPanel = new JPanel();
	private JRadioButton vertexInputAbsoluteRB = new JRadioButton(Origrammer.res.getString("UI_vertexInputAbsolute"), true);
	private JRadioButton vertexInputFractionOfLineRB = new JRadioButton(Origrammer.res.getString("UI_vertexInputFractionOfLine"), false);
	private ButtonGroup vertexInputGroup;

	//Grid
	public JCheckBox dispGridCheckBox = new JCheckBox(Origrammer.res.getString("UI_ShowGrid"), true);
	private JButton gridHalfButton = new JButton(Origrammer.res.getString("UI_gridHalf"));
	private JButton gridDoubleButton = new JButton(Origrammer.res.getString("UI_gridDouble"));
	private JButton gridSetButton = new JButton(Origrammer.res.getString("UI_gridSet"));
	private JFormattedTextField gridTextField;

	//SCALING
	public JFormattedTextField scalingCustomTF;
	private JLabel percentLabel = new JLabel("%");
	private JButton scalingCustomButton = new JButton("Set");
	private JButton scaling100 = new JButton("100%");
	private JButton scalingMinus = new JButton("-");
	private JButton scalingPlus = new JButton("+");

	private JButton resetViewButton = new JButton("Reset View");
	private JCheckBox dispVerticesCB = new JCheckBox(Origrammer.res.getString("UI_ShowVertices"), true);
	private JCheckBox dispFilledFacedCB = new JCheckBox(Origrammer.res.getString("UI_ShowFilledFaces"), true);

	private MainScreen screen;
	private UITopPanel uiTopPanel;

	public UISidePanel(MainScreen __screen, UITopPanel __uiTopPanel) {
		this.screen = __screen;
		this.uiTopPanel = __uiTopPanel;
		setPreferredSize(new Dimension(200, 400));
		setBackground(new Color(230, 230, 230));

		addToolbarPanel();
		addLineInputPanel();
		addVertexInputPanel();
		addMeasurePanel();
		addGridPanel();
		addScalingPanel();
		addButtonsPanel();

		modeChanged();
	}
	
	private void addToolbarPanel() {
		selectionToolRB.addActionListener(this);
		lineInputToolRB.addActionListener(this);
		vertexInputToolRB.addActionListener(this);
		arrowInputToolRB.addActionListener(this);
		symbolInputToolRB.addActionListener(this);
		measureToolRB.addActionListener(this);
		fillToolRB.addActionListener(this);
		
		toolbarGroup = new ButtonGroup();
		toolbarGroup.add(lineInputToolRB);
		toolbarGroup.add(vertexInputToolRB);
		toolbarGroup.add(arrowInputToolRB);
		toolbarGroup.add(symbolInputToolRB);
		toolbarGroup.add(fillToolRB);
		toolbarGroup.add(selectionToolRB);
		toolbarGroup.add(measureToolRB);
		
		JPanel toolbarPanel = new JPanel();
		toolbarPanel.add(selectionToolRB);
		toolbarPanel.add(lineInputToolRB);
		toolbarPanel.add(vertexInputToolRB);
		toolbarPanel.add(arrowInputToolRB);
		toolbarPanel.add(symbolInputToolRB);
		toolbarPanel.add(measureToolRB);
		toolbarPanel.add(fillToolRB);
		toolbarPanel.setLayout(new GridLayout(7, 1, 10, 2));
		add(toolbarPanel);
	}
	
	private void addLineInputPanel() {
		lineInputTwoVerticesRB.addActionListener(this);
		lineInputIncenterRB.addActionListener(this);
		
		lineInputGroup = new ButtonGroup();
		lineInputGroup.add(lineInputTwoVerticesRB);
		lineInputGroup.add(lineInputIncenterRB);
		
		lineInputPanel.add(lineInputTwoVerticesRB);
		lineInputPanel.add(lineInputIncenterRB);
		lineInputPanel.setLayout(new GridLayout(2, 1, 10, 2));
		add(lineInputPanel);
	}
	
	private void addVertexInputPanel() {
		vertexInputAbsoluteRB.addActionListener(this);
		vertexInputFractionOfLineRB.addActionListener(this);
		
		vertexInputGroup = new ButtonGroup();
		vertexInputGroup.add(vertexInputAbsoluteRB);
		vertexInputGroup.add(vertexInputFractionOfLineRB);
		
		vertexInputPanel.add(vertexInputAbsoluteRB);
		vertexInputPanel.add(vertexInputFractionOfLineRB);
		vertexInputPanel.setLayout(new GridLayout(2, 1, 10, 2));
		add(vertexInputPanel);
	}
	
	private void addMeasurePanel() {
		measureLengthRB.addActionListener(this);
		measureAngleRB.addActionListener(this);
		copyMeasuredLength.addActionListener(this);
		copyMeasuredAngle.addActionListener(this);
		
		measureGroup = new ButtonGroup();
		measureGroup.add(measureLengthRB);
		measureGroup.add(measureAngleRB);
		
		JLabel measureLabel = new JLabel("Measure", SwingConstants.CENTER);

		JPanel measureLengthPanel = new JPanel();
		measureLengthTF = new JFormattedTextField(new DecimalFormat("###.##cm"));
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
	}

	private void addGridPanel() {
		dispGridCheckBox.addActionListener(this);
		gridHalfButton.addActionListener(this);
		gridDoubleButton.addActionListener(this);
		gridSetButton.addActionListener(this);
		
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
	}

	private void addScalingPanel() {
		scalingCustomButton.addActionListener(this);
		scaling100.addActionListener(this);
		scalingMinus.addActionListener(this);
		scalingPlus.addActionListener(this);
		
		scalingCustomTF = new JFormattedTextField(new DecimalFormat("###.#"));
		scalingCustomTF.setColumns(4);
		scalingCustomTF.setValue(100);
		scalingCustomTF.setHorizontalAlignment(JTextField.RIGHT);
		PlainDocument docScalingCustom = (PlainDocument) scalingCustomTF.getDocument();
		docScalingCustom.setDocumentFilter(new IntFilter());

		JPanel scalingCustom = new JPanel();
		scalingCustom.add(scalingCustomTF);
		scalingCustom.add(percentLabel);

		JPanel scalingCustomPanel = new JPanel();
		//scalingCustomPanel.add(scalingCustomTF);
		scalingCustomPanel.add(scalingCustom);
		scalingCustomPanel.add(scalingCustomButton);
		scalingCustomPanel.setLayout(new BoxLayout(scalingCustomPanel, BoxLayout.LINE_AXIS));

		JPanel scalingButtonsPanel = new JPanel();
		scalingButtonsPanel.add(scaling100);
		scalingButtonsPanel.add(scalingMinus);
		scalingButtonsPanel.add(scalingPlus);
		scalingButtonsPanel.setLayout(new BoxLayout(scalingButtonsPanel, BoxLayout.LINE_AXIS));

		JPanel scalingPanel = new JPanel();
		scalingPanel.add(scalingCustomPanel);
		scalingPanel.add(scalingButtonsPanel);
		scalingPanel.setLayout(new BoxLayout(scalingPanel, BoxLayout.PAGE_AXIS));

		//scalingPanel.add(scalingCustom);
		//scalingPanel.add(scalingCustomButton);
		//scalingPanel.add(scaling100);
		//scalingPanel.add(scalingMinus);
		//scalingPanel.add(scalingPlus);
		//scalingPanel.setLayout(new GridLayout(2, 3, 5, 5));
		scalingPanel.setBorder(new TitledBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()), "Scaling"));
		add(scalingPanel);
	}
	
	private void addButtonsPanel(){
		resetViewButton.addActionListener(this);

		dispVerticesCB.addActionListener(this);
		dispVerticesCB.setSelected(true);
		Globals.dispVertex = true;

		dispFilledFacedCB.addActionListener(this);
		dispFilledFacedCB.setSelected(true);
		Globals.dispFilledFaces = true;
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.add(resetViewButton);
		buttonsPanel.add(dispVerticesCB);
		buttonsPanel.add(dispFilledFacedCB);
		buttonsPanel.setLayout(new GridLayout(3, 1, 10, 2));
		add(buttonsPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == selectionToolRB) {
			Globals.toolbarMode = Constants.ToolbarMode.SELECTION_TOOL;
			modeChanged();
		} else if (e.getSource() == lineInputToolRB) {
			Globals.toolbarMode = Constants.ToolbarMode.INPUT_LINE;
			modeChanged();
		} else if (e.getSource() == vertexInputToolRB) {
			Globals.toolbarMode = Constants.ToolbarMode.INPUT_VERTEX;
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
		} else if (e.getSource() == scalingCustomButton) {
			double newScale = Double.parseDouble(scalingCustomTF.getText());
			if (newScale < 1000 && newScale > 0) {
				Globals.SCALE = newScale/100;
				screen.repaint();
			} else {
				scalingCustomTF.setValue(Globals.SCALE*100);
			}

		} else if (e.getSource() == scaling100) {
			Globals.SCALE = 1.0;
			modeChanged();
		} else if (e.getSource() == scalingMinus) {
			Globals.SCALE -= 0.1;
			modeChanged();
		} else if (e.getSource() == scalingPlus) {
			Globals.SCALE += 0.1;
			modeChanged();
		} else if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE) {
			if (e.getSource() == lineInputTwoVerticesRB) {
				Globals.lineEditMode = Constants.LineInputMode.INPUT_LINE;
				modeChanged();
			} else if (e.getSource() == lineInputIncenterRB) {
				Globals.lineEditMode = Constants.LineInputMode.TRIANGLE_INSECTOR;
				modeChanged();
			}
		} else if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_VERTEX) {
			if (e.getSource() == vertexInputAbsoluteRB) {
				Globals.vertexInputMode = Constants.VertexInputMode.ABSOLUTE;
				modeChanged();
			} else if (e.getSource() == vertexInputFractionOfLineRB) {
				Globals.vertexInputMode = Constants.VertexInputMode.FRACTION_OF_LINE;
				modeChanged();
			}
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
		}
		if (e.getSource() == resetViewButton) {
			screen.resetView();
		}
		if (e.getSource() == dispGridCheckBox) {
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
			//			lineInputTwoVerticesRB.setEnabled(true);
			//			lineInputIncenterRB.setEnabled(true);
			lineInputPanel.setVisible(true);
		} else {
			//			lineInputTwoVerticesRB.setEnabled(false);
			//			lineInputIncenterRB.setEnabled(false);
			lineInputPanel.setVisible(false);
		}

		if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_VERTEX) {
			vertexInputPanel.setVisible(true);
		} else {
			vertexInputPanel.setVisible(false);
		}

		if (Globals.toolbarMode == Constants.ToolbarMode.MEASURE_TOOL) {
			measureOptionsPanel.setVisible(true);		
		} else {
			measureOptionsPanel.setVisible(false);		
		}

		scalingCustomTF.setValue(Globals.SCALE*100);

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
