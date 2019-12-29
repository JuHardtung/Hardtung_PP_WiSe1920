package origrammer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import origrammer.geometry.OriLine;

public class UITopPanel extends JPanel implements ActionListener, PropertyChangeListener, KeyListener {

	String[] lineInputOptions = {"Valley Fold", "Mountain Fold", "X-Ray Fold", "Edge Line"};
	Object[] arrowInputOptions = {"Valley Fold", "Mountain Fold", "Turn over", 
									new JSeparator(JSeparator.HORIZONTAL),
									"Push here", "Pull out", "Inflate here"};
	JLabel linesLabel = new JLabel(Origrammer.res.getString("MenuLines"));
	JLabel arrowsLabel = new JLabel(Origrammer.res.getString("MenuArrows"));

	private JRadioButton lineInputRadioButton = new JRadioButton(Origrammer.res.getString("MenuLineInput"));
	private JRadioButton arrowInputRadioButton = new JRadioButton(Origrammer.res.getString("MenuArrowInput"));
	
	private JComboBox menuLineCB = new JComboBox(lineInputOptions);
	private JComboBox menuArrowCB = new JComboBox(arrowInputOptions);
	
	
	JPanel sliderPanel = new JPanel();
	private JSlider sliderScaleIcon = new JSlider(0, 100);
	private JSlider sliderRotIcon = new JSlider(0, 360);
	
	
	MainScreen screen;
	
	public UITopPanel(MainScreen __screen) {
		this.screen = __screen;
		setPreferredSize(new Dimension(1000, 70));
		setBackground(new Color(230, 230, 230));
		
		//InputMethod
		ButtonGroup inputMethodGroup = new ButtonGroup();
		inputMethodGroup.add(lineInputRadioButton);
		inputMethodGroup.add(arrowInputRadioButton);
		inputMethodGroup.setSelected(lineInputRadioButton.getModel(), true);

		JPanel inputMethod = new JPanel();
		inputMethod.add(lineInputRadioButton);
		inputMethod.add(arrowInputRadioButton);
		inputMethod.setToolTipText("Activate this to enable line input.");
		
		inputMethod.setLayout(new GridLayout(2, 1, 0, 0));
				
		//##### LINES #####
		linesLabel.setBorder(BorderFactory.createEmptyBorder(-10,0,-10,0));
		linesLabel.setHorizontalAlignment(JLabel.CENTER);
		
		menuLineCB.setRenderer(new IndentedRenderer());
		menuLineCB.setSelectedIndex(0);
		menuLineCB.addActionListener(this);
		menuLineCB.setToolTipText(Origrammer.res.getString("TT_LineCB"));


		
		JPanel linesSubPanel = new JPanel();
		linesSubPanel.add(menuLineCB);
		linesSubPanel.setBorder(BorderFactory.createEmptyBorder(-5,0,0,0));
		
		JPanel linesPanel = new JPanel();
		linesPanel.add(linesLabel);
		linesPanel.add(linesSubPanel);
		linesPanel.setLayout(new GridLayout(2, 1, 0, 0));
		linesPanel.setBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()));
		linesPanel.setToolTipText(Origrammer.res.getString("TT_LineCB"));

		
		//##### ARROWS #####
		arrowsLabel.setBorder(BorderFactory.createEmptyBorder(-10,0,-10,0));
		arrowsLabel.setHorizontalAlignment(JLabel.CENTER);
		
		menuArrowCB.setRenderer(new SeparatorComboBoxRenderer());
		menuArrowCB.setSelectedIndex(0);
		menuArrowCB.addActionListener(new SeparatorComboBoxListener(menuArrowCB));
		menuArrowCB.setToolTipText(Origrammer.res.getString("TT_ArrowCB"));

		
		JPanel arrowsSubPanel = new JPanel();
		arrowsSubPanel.add(menuArrowCB);
		arrowsSubPanel.setBorder(BorderFactory.createEmptyBorder(-5,0,0,0));
		
		JPanel arrowsPanel = new JPanel();
		arrowsPanel.add(arrowsLabel);
		arrowsPanel.add(arrowsSubPanel);
		arrowsPanel.setLayout(new GridLayout(2, 1, 0, 0));
		arrowsPanel.setBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()));
		arrowsPanel.setToolTipText(Origrammer.res.getString("TT_ArrowCB"));

		
		//##### SLIDERS #####
		
		sliderScaleIcon.setMajorTickSpacing(10);
		//menuIconSizeSlider.setMinorTickSpacing(1);
		sliderScaleIcon.setPaintTicks(true);
		sliderScaleIcon.setPaintLabels(true);
		//sliderScaleIcon.setSnapToTicks(true);
		sliderScaleIcon.addChangeListener(e -> sliderScaleChanged());

		//TODO: allow for float input (for the 22.5° rotations)
		sliderRotIcon.setMajorTickSpacing(45);
		//menuIconRotSlider.setMinorTickSpacing(1);
		sliderRotIcon.setPaintTicks(true);
		sliderRotIcon.setPaintLabels(true);
		//sliderRotIcon.setSnapToTicks(true);
		sliderRotIcon.addChangeListener(e -> sliderRotChanged());

		
		sliderPanel.add(sliderScaleIcon);
		sliderPanel.add(sliderRotIcon);
		sliderPanel.setLayout(new GridLayout(1,2,10,10));
		sliderPanel.setBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()));
		sliderPanel.setVisible(false);
		
		
		//Add Lines and Arrow Panel to UITopPanel
		//add(inputMethod);
		add(linesPanel);
		add(arrowsPanel);
		add(sliderPanel);
		
		
		
//		lineInputRadioButton.addActionListener(this);
//		arrowInputRadioButton.addActionListener(this);
		
		modeChanged();
		
		
	}
	
	private void sliderScaleChanged() {

		BufferedImage img = null;

		for(OriArrow arrow : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {

			if(arrow.isSelected()) {
				
				//sliderScale can never be 0, otherwise the updated size failes
				if(sliderScaleIcon.getValue() == 0) {
					arrow.setScale(0.01);
				} else {
					arrow.setScale((double) sliderScaleIcon.getValue()/100);
				}
				screen.repaint();

				//arrow.getArrowLabel().setIcon(arrowImageIcon);
				arrow.getArrowLabel().setBounds((int)arrow.getxPos(), (int)arrow.getyPos(), 
												(int) Math.round(arrow.getWidth()*arrow.getScale()), 
												(int) Math.round(arrow.getHeight()*arrow.getScale()));

			}
		}
	}		
	
	private void sliderRotChanged() {
		
		for(OriArrow arrow : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {			
			if(arrow.isSelected()) {
				
				arrow.setDegrees(sliderRotIcon.getValue());
				screen.repaint();
			}
		}
	}
	


	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object selectedLine = menuLineCB.getSelectedItem();
		Object selectedArrow = menuArrowCB.getSelectedItem();
		
//		if (e.getSource() == lineInputRadioButton) {
//			Globals.editMode = Constants.ToolbarMode.INPUT_LINE;
//			modeChanged();
//			
//		} else if (e.getSource() == arrowInputRadioButton) {
//			Globals.editMode = Constants.ToolbarMode.INPUT_ARROW;
//			modeChanged();
//		}
		if(Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE) {
			
			if(selectedLine == "Valley Fold") {
				Globals.inputLineType = OriLine.TYPE_VALLEY;
			} else if(selectedLine == "Mountain Fold") {
				Globals.inputLineType = OriLine.TYPE_MOUNTAIN;
			} else if(selectedLine == "X-Ray Fold") {				
				Globals.inputLineType = OriLine.TYPE_XRAY;
			} else if(selectedLine == "Edge Line") {
				Globals.inputLineType = OriLine.TYPE_EDGE;
			}			
		} else if(Globals.toolbarMode == Constants.ToolbarMode.INPUT_ARROW) {
			
			if(selectedArrow == "Valley Fold") {


			} else if(selectedArrow == "Mountain Fold") {


			} else if(selectedArrow == "Turn over") {


			} else if(selectedArrow == "Push here") {

			} else if(selectedArrow == "Pull here") {

			} else if(selectedArrow == "Inflate here") {

			}	
		}
	}
	
	public void modeChanged() {
		if(Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE) {
			menuLineCB.setEnabled(true);
			menuArrowCB.setEnabled(false);			
		} else if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_ARROW) {
			menuLineCB.setEnabled(false);
			menuArrowCB.setEnabled(true);			
		}
		
		//TODO arrow slider only visible when an arrow is selected
		if (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL) {
			sliderPanel.setVisible(true);
		} else {
			sliderPanel.setVisible(false);
		}
		screen.modeChanged();
	}
	
	//Indents JComboBox entries
	class IndentedRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList list,Object value,
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
	   JComboBox combobox;
	   Object oldItem;
	     
	   SeparatorComboBoxListener(JComboBox combobox) {
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
	      if(selectedArrow == "Valley Fold") {
				Globals.inputArrowType = OriArrow.TYPE_VALLEY;
	      } else if(selectedArrow == "Mountain Fold") {
				Globals.inputArrowType = OriArrow.TYPE_MOUNTAIN;
	      } else if(selectedArrow == "Turn over") {
				Globals.inputArrowType = OriArrow.TYPE_TURN_OVER;
	      } else if(selectedArrow == "Push here") {
				Globals.inputArrowType = OriArrow.TYPE_PUSH_HERE;
	      } else if(selectedArrow == "Pull out") {
				Globals.inputArrowType = OriArrow.TYPE_PULL_HERE;
	      } else if(selectedArrow == "Inflate here") {
				Globals.inputArrowType = OriArrow.TYPE_INFLATE_HERE;
			}
	   }
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
