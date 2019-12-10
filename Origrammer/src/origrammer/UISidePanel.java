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

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class UISidePanel extends JPanel implements ActionListener, PropertyChangeListener, KeyListener {	
		
	JFormattedTextField gridTextField;
	
	JRadioButton selectionToolRButton = new JRadioButton(Origrammer.res.getString("UI_selectionTool"), false);
	JRadioButton lineInputToolRButton = new JRadioButton(Origrammer.res.getString("UI_lineInputTool"), true);
	JRadioButton arrowInputToolRButton = new JRadioButton(Origrammer.res.getString("UI_arrowInputTool"), false);
	ButtonGroup toolbarGroup;
	
	//Grid
	JCheckBox dispGridCheckBox = new JCheckBox(Origrammer.res.getString("UI_ShowGrid"), true);
	JButton gridHalfButton = new JButton(Origrammer.res.getString("UI_gridHalf"));
	JButton gridDoubleButton = new JButton(Origrammer.res.getString("UI_gridDouble"));
	JButton gridSetButton = new JButton(Origrammer.res.getString("UI_gridSet"));
	
	JCheckBox dispVerticesCheckBox = new JCheckBox(Origrammer.res.getString("UI_ShowVertices"), true);

	MainScreen screen;
	UITopPanel uiTopPanel;

	
	
	public UISidePanel(MainScreen __screen, UITopPanel __uiTopPanel) {
		this.screen = __screen;
		this.uiTopPanel = __uiTopPanel;
		setPreferredSize(new Dimension(200, 400));
		setBackground(new Color(230, 230, 230));
				
		toolbarGroup = new ButtonGroup();
		toolbarGroup.add(selectionToolRButton);
		toolbarGroup.add(lineInputToolRButton);
		toolbarGroup.add(arrowInputToolRButton);
		
		
		dispVerticesCheckBox.addActionListener(this);
		dispVerticesCheckBox.setSelected(true);
		Globals.dispVertex = true;
		
		//TOOLBAR ActionListener
		selectionToolRButton.addActionListener(this);
		lineInputToolRButton.addActionListener(this);
		arrowInputToolRButton.addActionListener(this);
		
		//GRID ActionListener
		dispGridCheckBox.addActionListener(this);
		gridHalfButton.addActionListener(this);
		gridDoubleButton.addActionListener(this);
		gridSetButton.addActionListener(this);
				
				
		//TOOLBAR Panel and positioning
		JPanel toolbarPanel = new JPanel();
		
		toolbarPanel.add(selectionToolRButton);
		toolbarPanel.add(lineInputToolRButton);
		toolbarPanel.add(arrowInputToolRButton);
		toolbarPanel.setLayout(new GridLayout(5, 1, 10, 2));
		add(toolbarPanel);
		
		
		
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
		
		add(gridPanel);
		
		
		
		//Buttons Panel
		JPanel buttonsPanel = new JPanel();
		
		buttonsPanel.add(dispVerticesCheckBox);
		
		buttonsPanel.setLayout(new GridLayout(1, 1, 10, 2));
		add(buttonsPanel);
		
		
		
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == selectionToolRButton) {
			Globals.editMode = Constants.ToolbarMode.SELECTION_TOOL;
			modeChanged();
		} else if (e.getSource() == lineInputToolRButton) {
			Globals.editMode = Constants.ToolbarMode.INPUT_LINE;
			modeChanged();
		} else if (e.getSource() == arrowInputToolRButton) {
			Globals.editMode = Constants.ToolbarMode.INPUT_ARROW;
			modeChanged();
		}
		
		else if (e.getSource() == dispGridCheckBox) {
			screen.setDispGrid(dispGridCheckBox.isSelected());
		} else if (e.getSource() == gridSetButton) {
			int customGrid = new Integer(gridTextField.getText());
			if (customGrid < 65 && customGrid > 1) {
				Globals.gridDivNum = customGrid;
				screen.repaint();	
			} else {
				gridTextField.setValue(Globals.gridDivNum);
			}
		}
		else if (e.getSource() == gridHalfButton) {
			if(Globals.gridDivNum > 3) {
				Globals.gridDivNum /= 2;
				gridTextField.setValue(Globals.gridDivNum);
				screen.repaint();
			}
		} else if (e.getSource() == gridDoubleButton) {
			if(Globals.gridDivNum < 33) {
				Globals.gridDivNum *= 2;
				gridTextField.setValue(Globals.gridDivNum);
				screen.repaint();
			}
		} else if (e.getSource() == dispVerticesCheckBox) {
			Globals.dispVertex = dispVerticesCheckBox.isSelected();
			screen.repaint();
		}
	}
	
	
	public void modeChanged() {
		
		uiTopPanel.modeChanged();
		screen.modeChanged();
		repaint();
	}


	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
	
}
