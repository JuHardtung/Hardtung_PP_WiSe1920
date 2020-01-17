package origrammer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import origrammer.geometry.GeometryUtil;
import origrammer.geometry.OriLine;

public class UITopPanel extends JPanel implements ActionListener, PropertyChangeListener, KeyListener {

	String[] lineInputOptions = {"Valley Fold", "Mountain Fold", "X-Ray Fold", "Edge Line"};
	Object[] arrowInputOptions = {"Valley Fold", "Mountain Fold", "Turn over", 
									new JSeparator(JSeparator.HORIZONTAL),
									"Push here", "Pull out", "Inflate here"};

	
	//INPUT LINES/ARROWS
	JPanel inputLinesPanel = new JPanel();
	JLabel linesLabel = new JLabel(Origrammer.res.getString("MenuLines"));
	JPanel inputArrowPanel = new JPanel();
	JLabel arrowsLabel = new JLabel(Origrammer.res.getString("MenuArrows"));
	private JComboBox<String> menuLineCB = new JComboBox<>(lineInputOptions);
	private JComboBox<Object> menuArrowCB = new JComboBox<>(arrowInputOptions);
	
	//FACE UP/ FACE DOWN COLOR
	private JRadioButton faceUpInput = new JRadioButton("Face Up", false);
	private JRadioButton faceDownInput = new JRadioButton("Face Down", true);
	
	//CHANGE LINE/ARROW TYPE
	JPanel changeLinePanel = new JPanel();
	JPanel changeArrowPanel = new JPanel();
	private JComboBox<String> changeLineTypeCB = new JComboBox<>(lineInputOptions);
	private JComboBox<Object> changeArrowTypeCB = new JComboBox<>(arrowInputOptions);
	JButton changeLineButton = new JButton("Set");
	JButton changeArrowButton = new JButton("Set");



	//ROTATE/SCALE ARROWS
	JPanel sliderPanel = new JPanel();
	private JSlider sliderScaleIcon = new JSlider(100, 200);
	private JSlider sliderRotIcon = new JSlider(0, 3600);
	
	
	MainScreen screen;
	
	public UITopPanel(MainScreen __screen) {
		this.screen = __screen;
		setPreferredSize(new Dimension(1000, 70));
		setBackground(new Color(230, 230, 230));
		
		
		//##### FACE UP / FACE DOWN INPUT
		ButtonGroup faceDirectionInput = new ButtonGroup();
		faceDirectionInput.add(faceUpInput);
		faceDirectionInput.add(faceDownInput);
		
		JPanel faceDirectionPanel = new JPanel();
		faceDirectionPanel.add(faceUpInput);
		faceDirectionPanel.add(faceDownInput);
		faceDirectionPanel.setLayout(new BoxLayout(faceDirectionPanel, BoxLayout.PAGE_AXIS));
		
		faceUpInput.addActionListener(this);
		faceDownInput.addActionListener(this);
				
		
		//##### LINE INPUT TYPE #####		
		menuLineCB.setRenderer(new IndentedRenderer());
		menuLineCB.setSelectedIndex(0);
		menuLineCB.addActionListener(this);
		menuLineCB.setToolTipText(Origrammer.res.getString("TT_LineCB"));
		
		inputLinesPanel.add(menuLineCB);
		inputLinesPanel.setBorder(new TitledBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()), "Input Line Type"));
		inputLinesPanel.setToolTipText(Origrammer.res.getString("TT_LineCB"));

		
		//##### ARROW INPUT TYPE #####
		menuArrowCB.setRenderer(new SeparatorComboBoxRenderer());
		menuArrowCB.setSelectedIndex(0);
		menuArrowCB.addActionListener(new SeparatorComboBoxListener(menuArrowCB));
		menuArrowCB.setToolTipText(Origrammer.res.getString("TT_ArrowCB"));
		
		inputArrowPanel.add(menuArrowCB);
		inputArrowPanel.setBorder(new TitledBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()), "Input Arrow Type"));
		inputArrowPanel.setToolTipText(Origrammer.res.getString("TT_ArrowCB"));

		
		//##### SLIDERS #####
		sliderScaleIcon.setMajorTickSpacing(10);
		sliderScaleIcon.setPaintTicks(true);
		//sliderScaleIcon.setPaintLabels(true);
		sliderScaleIcon.addChangeListener(e -> sliderScaleChanged());
		sliderScaleIcon.setBorder(new TitledBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()), "Scale Arrow"));

		sliderRotIcon.setMajorTickSpacing(225);
		sliderRotIcon.setPaintTicks(true);
		//sliderRotIcon.setPaintLabels(true);
		sliderRotIcon.setSnapToTicks(true);
		sliderRotIcon.addChangeListener(e -> sliderRotChanged());
		sliderRotIcon.setBorder(new TitledBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()), "Rotate Arrow"));

		sliderPanel.add(sliderScaleIcon);
		sliderPanel.add(sliderRotIcon);
		
		
		//##### CHANGE LINE TYPE #####
		changeLineButton.addActionListener(this);
		changeLinePanel.add(changeLineTypeCB);
		changeLinePanel.add(changeLineButton);
		changeLinePanel.setBorder(new TitledBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()), "Change Line Type"));
		
		
		//##### CHANGE ARROW TYPE #####
		changeArrowButton.addActionListener(this);
		changeArrowTypeCB.setRenderer(new SeparatorComboBoxRenderer());
		changeArrowPanel.add(changeArrowTypeCB);
		changeArrowPanel.add(changeArrowButton);
		changeArrowPanel.setBorder(new TitledBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()), "Change Arrow Type"));

		//Add Lines and Arrow Panel to UITopPanel
		add(changeLinePanel);
		add(changeArrowPanel);
		add(faceDirectionPanel);
		add(inputLinesPanel);
		add(inputArrowPanel);
		add(sliderPanel);
	
		modeChanged();
	}
	
	
	/**
	 * Sets the scale for all selected OriArrows
	 */
	private void sliderScaleChanged() {
		for (OriArrow arrow : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
			if (arrow.isSelected()) {
				arrow.setScale((double) sliderScaleIcon.getValue()/100);
				screen.repaint();

				//TODO: add preview pictures of arrow types
				arrow.getArrowLabel().setBounds((int) arrow.getxPos(), (int)arrow.getyPos(), 
												(int) Math.round(arrow.getWidth() * arrow.getScale()), 
												(int) Math.round(arrow.getHeight() * arrow.getScale()));
			}
		}
	}		
	
	/**
	 * Sets the rotation of all selected OriArrows
	 */
	private void sliderRotChanged() {
		for (OriArrow arrow : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
			if (arrow.isSelected()) {
				arrow.setDegrees(sliderRotIcon.getValue()/10);
				screen.repaint();
				
				Rectangle2D rect = GeometryUtil.calcRotatedBox(arrow.getxPos(), arrow.getyPos(), arrow.getWidth(), arrow.getHeight(), arrow.getDegrees());
				
				arrow.getArrowLabel().setBounds((int)arrow.getxPos(), (int)arrow.getyPos(), (int)rect.getWidth(), (int)rect.getHeight());
			}
		}
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		Object selectedLine = menuLineCB.getSelectedItem();

		if (e.getSource() == changeLineButton) {
			for (OriLine l : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
				if (l.isSelected) {
					String lineType = changeLineTypeCB.getSelectedItem().toString();
					
					if (lineType == "Valley Fold") {
						l.setType(OriLine.TYPE_VALLEY);
					} else if (lineType == "Mountain Fold") {
						l.setType(OriLine.TYPE_MOUNTAIN);
					} else if (lineType == "X-Ray Fold") {
						l.setType(OriLine.TYPE_XRAY);
					} else if (lineType == "Edge Line") {
						l.setType(OriLine.TYPE_EDGE);
					}
				}
			}
			screen.repaint();

		} else if (e.getSource() == changeArrowButton) {

			for (OriArrow a : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
				if (a.isSelected()) {
					String arrowType = changeArrowTypeCB.getSelectedItem().toString();
					
					if (arrowType == "Valley Fold") {
						a.setType(OriArrow.TYPE_VALLEY);
					} else if (arrowType == "Mountain Fold") {
						a.setType(OriArrow.TYPE_MOUNTAIN);
					} else if (arrowType == "Turn over") {
						a.setType(OriArrow.TYPE_TURN_OVER);
					} else if (arrowType == "Push here") {
						a.setType(OriArrow.TYPE_PUSH_HERE);
					} else if (arrowType == "Pull out") {
						a.setType(OriArrow.TYPE_PULL_HERE);
					} else if (arrowType == "Inflate here") {
						a.setType(OriArrow.TYPE_INFLATE_HERE);
					}
				}
			}
			screen.repaint();

		} else if (e.getSource() == faceUpInput) {
			Globals.faceInputDirection = Constants.FaceInputDirection.FACE_UP;
		} else if (e.getSource() == faceDownInput) {
			Globals.faceInputDirection = Constants.FaceInputDirection.FACE_DOWN;
		}

		if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE) {

			if (selectedLine == "Valley Fold") {
				Globals.inputLineType = OriLine.TYPE_VALLEY;
			} else if (selectedLine == "Mountain Fold") {
				Globals.inputLineType = OriLine.TYPE_MOUNTAIN;
			} else if (selectedLine == "X-Ray Fold") {				
				Globals.inputLineType = OriLine.TYPE_XRAY;
			} else if (selectedLine == "Edge Line") {
				Globals.inputLineType = OriLine.TYPE_EDGE;
			}			
		}
	}
	
	public void modeChanged() {
		if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE) {
			inputLinesPanel.setVisible(true);
		} else {
			inputLinesPanel.setVisible(false);
		}
		
		if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_ARROW) {
			inputArrowPanel.setVisible(true);			
		} else {
			inputArrowPanel.setVisible(false);			
		}
		
		if (Globals.toolbarMode == Constants.ToolbarMode.FILL_TOOL) {
			faceUpInput.setVisible(true);
			faceDownInput.setVisible(true);
		} else {
			faceUpInput.setVisible(false);
			faceDownInput.setVisible(false);
		}
		
		if (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL) {
			sliderPanel.setVisible(true);
			changeLinePanel.setVisible(true);
			changeArrowPanel.setVisible(true);
		} else {
			sliderPanel.setVisible(false);
			changeLinePanel.setVisible(false);
			changeArrowPanel.setVisible(false);
		}
		screen.modeChanged();
	}
	
	//Indents JComboBox entries
	class IndentedRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value,
                int index,boolean isSelected,boolean cellHasFocus) {
			   JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			   lbl.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
			   return lbl;
		}
	}
	
	//source: http://esus.com/creating-a-jcombobox-with-a-divider-separator-line/
	class SeparatorComboBoxRenderer extends BasicComboBoxRenderer implements ListCellRenderer {
	   public SeparatorComboBoxRenderer() {
	      super();
	   }
	    
	   public Component getListCellRendererComponent(JList list,
	       Object value, int index, boolean isSelected, boolean cellHasFocus) {
		   JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		   lbl.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
	      if (isSelected) {
	          setBackground(list.getSelectionBackground());
	          setForeground(list.getSelectionForeground());
	      }
	      else {
	          setBackground(list.getBackground());
	          setForeground(list.getForeground());
	      }
	  
	      setFont(list.getFont());
	      if (value instanceof Icon) {
	         setIcon((Icon)value);
	      }
	      if (value instanceof JSeparator) {
	         return (Component) value;
	      }
	      else {
	         setText((value == null) ? "" : value.toString());
	      }
	  
	      return lbl;
	  } 
	}
	
	//source: http://esus.com/creating-a-jcombobox-with-a-divider-separator-line/
	class SeparatorComboBoxListener implements ActionListener {
		JComboBox<Object> combobox;
		Object oldItem;

		SeparatorComboBoxListener(JComboBox<Object> combobox) {
			this.combobox = combobox;
			combobox.setSelectedIndex(0);
			oldItem = combobox.getSelectedItem();
		}

		public void actionPerformed(ActionEvent e) {
			Object selectedArrow = menuArrowCB.getSelectedItem();

			if (selectedArrow instanceof JSeparator) {
				combobox.setSelectedItem(oldItem);
			} else {
				oldItem = selectedArrow;
			}
			if (selectedArrow == "Valley Fold") {
				Globals.inputArrowType = OriArrow.TYPE_VALLEY;
			} else if (selectedArrow == "Mountain Fold") {
				Globals.inputArrowType = OriArrow.TYPE_MOUNTAIN;
			} else if (selectedArrow == "Turn over") {
				Globals.inputArrowType = OriArrow.TYPE_TURN_OVER;
			} else if (selectedArrow == "Push here") {
				Globals.inputArrowType = OriArrow.TYPE_PUSH_HERE;
			} else if (selectedArrow == "Pull out") {
				Globals.inputArrowType = OriArrow.TYPE_PULL_HERE;
			} else if (selectedArrow == "Inflate here") {
				Globals.inputArrowType = OriArrow.TYPE_INFLATE_HERE;
			}
		}
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
