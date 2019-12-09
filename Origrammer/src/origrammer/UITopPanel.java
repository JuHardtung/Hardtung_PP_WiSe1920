package origrammer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
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

		
		
		//Add Lines and Arrow Panel to UITopPanel
		add(inputMethod);
		add(linesPanel);
		add(arrowsPanel);
		
		
		lineInputRadioButton.addActionListener(this);
		arrowInputRadioButton.addActionListener(this);
		
		modeChanged();
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object selectedLine = menuLineCB.getSelectedItem();
		Object selectedArrow = menuArrowCB.getSelectedItem();
		
		if (e.getSource() == lineInputRadioButton) {
			Globals.editMode = Constants.EditMode.INPUT_LINE;
			modeChanged();
			
		} else if (e.getSource() == arrowInputRadioButton) {
			Globals.editMode = Constants.EditMode.INPUT_ARROW;
			modeChanged();
		}
		
		if(Globals.editMode == Constants.EditMode.INPUT_LINE) {
			
			System.out.println("EDIT MODE: " + Globals.editMode);
			if(selectedLine == "Valley Fold") {
				Globals.inputLineType = OriLine.TYPE_VALLEY;
				System.out.println("LINE Valley Fold");
			} else if(selectedLine == "Mountain Fold") {
				Globals.inputLineType = OriLine.TYPE_MOUNTAIN;
				System.out.println("LINE Mountain Fold");
			} else if(selectedLine == "X-Ray Fold") {				
				Globals.inputLineType = OriLine.TYPE_XRAY;
				System.out.println("LINE X-Ray Fold");
			} else if(selectedLine == "Edge Line") {
				Globals.inputLineType = OriLine.TYPE_EDGE;
				System.out.println("LINE Edge Line");
			}			
		} else if(Globals.editMode == Constants.EditMode.INPUT_ARROW) {
			
			System.out.println("EDIT MODE: " + Globals.editMode);
			if(selectedArrow == "Valley Fold") {
				System.out.println("ARROW Valley Fold");
			} else if(selectedArrow == "Mountain Fold") {
				System.out.println("ARROW Mountain Fold");
			} else if(selectedArrow == "Turn over") {
				System.out.println("ARROW Turn over");
			} else if(selectedArrow == "Push here") {
				System.out.println("ARROW Push here");
			} else if(selectedArrow == "Pull here") {
				System.out.println("ARROW Pull here");
			} else if(selectedArrow == "Inflate here") {
				System.out.println("ARROW Inflate here");
			}	
		}

		
		//if(e.getSource() == )
	}
	
	public void modeChanged() {
		if(Globals.editMode == Constants.EditMode.INPUT_LINE) {
			menuLineCB.setEnabled(true);
			menuArrowCB.setEnabled(false);			
		} else if (Globals.editMode == Constants.EditMode.INPUT_ARROW) {
			menuLineCB.setEnabled(false);
			menuArrowCB.setEnabled(true);			
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
	    	  System.out.println("ARROW Valley Fold");
	      } else if(selectedArrow == "Mountain Fold") {
	    	  System.out.println("ARROW Mountain Fold");
	      } else if(selectedArrow == "Turn over") {
	    	  System.out.println("ARROW Turn over");
	      } else if(selectedArrow == "Push here") {
	    	  System.out.println("ARROW Push here");
	      } else if(selectedArrow == "Pull out") {
	    	  System.out.println("ARROW Pull out");
	      } else if(selectedArrow == "Inflate here") {
	    	  System.out.println("ARROW Inflate here");
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
