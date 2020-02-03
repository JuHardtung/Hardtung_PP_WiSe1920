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
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.PlainDocument;

import origrammer.geometry.GeometryUtil;
import origrammer.geometry.OriEqualDistSymbol;
import origrammer.geometry.OriFace;
import origrammer.geometry.OriGeomSymbol;
import origrammer.geometry.OriLeader;
import origrammer.geometry.OriLine;
import origrammer.geometry.OriPicSymbol;
import origrammer.geometry.OriRepetitionBox;

public class UITopPanel extends JPanel implements ActionListener, PropertyChangeListener, KeyListener {

	String[] lineInputOptions = {"Valley Fold", "Mountain Fold", "X-Ray Fold", "Edge Line"};
	Object[] arrowInputOptions = {"Valley Fold", "Mountain Fold", "Turn over", 
									new JSeparator(JSeparator.HORIZONTAL),
									"Push here", "Pull out", "Inflate here"};
	Object[] symbolInputOptions = {"Leader", "Repetition Box", "Next View Here", "Rotations", 
			"Hold Here", "Hold Here and Pull", new JSeparator(JSeparator.HORIZONTAL),  
			"X-Ray Circle", "Fold over and over", "Equal Distances", "Equal Angles", 
			new JSeparator(JSeparator.HORIZONTAL),  "Crimping & Pleating", "Sinks"};

	
	//INPUT LINES/ ARROWS/ SYMBOLS
	JPanel inputLinesPanel = new JPanel();
	JLabel linesLabel = new JLabel(Origrammer.res.getString("MenuLines"));
	JPanel inputArrowPanel = new JPanel();
	JLabel arrowsLabel = new JLabel(Origrammer.res.getString("MenuArrows"));
	JPanel inputSymbolsPanel = new JPanel();
	private JComboBox<String> menuLineCB = new JComboBox<>(lineInputOptions);
	private JComboBox<Object> menuArrowCB = new JComboBox<>(arrowInputOptions);
	private JComboBox<Object> symbolInputCB = new JComboBox<>(symbolInputOptions);
	
	//INPUT SYMBOL LEADER
	JPanel inputSymbolLeaderPanel = new JPanel();
	public JTextField inputLeaderText = new JTextField();
	
	//INPUT SYMBOL REPETITION BOX
	JPanel inputSymbolRepetitionPanel = new JPanel();
	public JTextField inputRepetitionText = new JTextField();
	
	//FACE UP/ FACE DOWN COLOR
	JPanel faceDirectionPanel = new JPanel();
	private JRadioButton faceUpInput = new JRadioButton("Face Up", false);
	private JRadioButton faceDownInput = new JRadioButton("Face Down", true);
	
	//CHANGE LINE/ARROW TYPE
	JPanel changeLinePanel = new JPanel();
	JPanel changeArrowPanel = new JPanel();
	private JComboBox<String> changeLineTypeCB = new JComboBox<>(lineInputOptions);
	private JComboBox<Object> changeArrowTypeCB = new JComboBox<>(arrowInputOptions);
	JButton changeLineButton = new JButton("Set");
	JButton changeArrowButton = new JButton("Set");
	
	//EQUAL DISTANCE SETTINGS
	JPanel equalDistPanel = new JPanel();
	public JSlider sliderEqualDist = new JSlider(-50, 50);
	public JTextField equalDistDividerTF = new JTextField();

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
		
		faceDirectionPanel.add(faceUpInput);
		faceDirectionPanel.add(faceDownInput);
		faceDirectionPanel.setLayout(new BoxLayout(faceDirectionPanel, BoxLayout.PAGE_AXIS));
		
		faceUpInput.addActionListener(this);
		faceDownInput.addActionListener(this);
				
		
		//##### LINE INPUT TYPE #####		
		menuLineCB.setRenderer(new IndentedRenderer());
		menuLineCB.setSelectedIndex(0);
		menuLineCB.addActionListener(this);
		
		inputLinesPanel.add(menuLineCB);
		inputLinesPanel.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(), 
												getBackground().brighter()), "Input Line Type"));

		//##### ARROW INPUT TYPE #####
		menuArrowCB.setRenderer(new SeparatorComboBoxRenderer());
		menuArrowCB.setSelectedIndex(0);
		menuArrowCB.addActionListener(new SeparatorComboBoxListener(menuArrowCB));
		
		inputArrowPanel.add(menuArrowCB);
		inputArrowPanel.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(), 
												getBackground().brighter()), "Input Arrow Type"));

		//##### SYMBOL INPUT TYPE #####
		symbolInputCB.setRenderer(new SeparatorComboBoxRenderer());
		symbolInputCB.setSelectedIndex(0);
		symbolInputCB.addActionListener(new SeparatorComboBoxListener(symbolInputCB));
		
		inputSymbolsPanel.add(symbolInputCB);
		inputSymbolsPanel.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(), 
												getBackground().brighter()), "Input Symbol Type"));
		
		//##### INPUT SYMBOL LEADER #####
		inputLeaderText.setPreferredSize(new Dimension(150, 25));
		inputSymbolLeaderPanel.add(inputLeaderText);
		inputSymbolLeaderPanel.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(), 
												getBackground().brighter()), "Input Leader Text"));
		
		//##### INPUT SYMBOL LEADER #####
		inputRepetitionText.setPreferredSize(new Dimension(150, 25));
		inputSymbolRepetitionPanel.add(inputRepetitionText);
		inputSymbolRepetitionPanel.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(), 
												getBackground().brighter()), "Input Repetition Text"));
		
		//##### SLIDERS #####
		sliderScaleIcon.setMajorTickSpacing(10);
		sliderScaleIcon.setPaintTicks(true);
		//sliderScaleIcon.setPaintLabels(true);
		sliderScaleIcon.addChangeListener(e -> sliderScaleChanged());
		sliderScaleIcon.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(), 
												getBackground().brighter()), "Scale Arrow"));

		sliderRotIcon.setMajorTickSpacing(225);
		sliderRotIcon.setPaintTicks(true);
		//sliderRotIcon.setPaintLabels(true);
		sliderRotIcon.setSnapToTicks(true);
		sliderRotIcon.addChangeListener(e -> sliderRotChanged());
		sliderRotIcon.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(), 
												getBackground().brighter()), "Rotate Arrow"));

		sliderPanel.add(sliderScaleIcon);
		sliderPanel.add(sliderRotIcon);
		
		
		//##### CHANGE LINE TYPE #####
		changeLineButton.addActionListener(this);
		changeLinePanel.add(changeLineTypeCB);
		changeLinePanel.add(changeLineButton);
		changeLinePanel.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
											getBackground().darker(), 
											getBackground().brighter()), "Change Line Type"));
		
		
		//##### CHANGE ARROW TYPE #####
		changeArrowButton.addActionListener(this);
		changeArrowTypeCB.setRenderer(new SeparatorComboBoxRenderer());
		changeArrowPanel.add(changeArrowTypeCB);
		changeArrowPanel.add(changeArrowButton);
		changeArrowPanel.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(), 
												getBackground().brighter()), "Change Arrow Type"));
		
		//##### EQUAL DIST SETTINGS
		sliderEqualDist.setMajorTickSpacing(10);
		sliderEqualDist.setPaintTicks(true);
		sliderEqualDist.addChangeListener(e -> sliderEqualDistChanged());
		equalDistDividerTF.setPreferredSize(new Dimension(20,20));
		PlainDocument docEqualDistDivider = (PlainDocument) equalDistDividerTF.getDocument();
		docEqualDistDivider.setDocumentFilter(new IntFilter());
		equalDistDividerTF.setText(Integer.toString(Globals.gridDivNum));
		//TODO: use "SET" button instead of documentListener
		docEqualDistDivider.addDocumentListener(new DocumentListener() { 
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				System.out.println("remove");
				//setDivider();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				System.out.println("insert");
				setEqualDistanceDividerCount();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				System.out.println("change");
				setEqualDistanceDividerCount();
			}
		});
		
		equalDistPanel.add(sliderEqualDist);
		equalDistPanel.add(equalDistDividerTF);
		equalDistPanel.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(),
												getBackground().brighter()), "Equal Distance Settings"));

		//Add Lines and Arrow Panel to UITopPanel
		add(changeLinePanel);
		add(changeArrowPanel);
		add(faceDirectionPanel);
		add(inputLinesPanel);
		add(inputArrowPanel);
		add(inputSymbolsPanel);
		add(inputSymbolLeaderPanel);
		add(inputSymbolRepetitionPanel);
		add(equalDistPanel);
		add(sliderPanel);
	
		modeChanged();
	}
	
	private void setEqualDistanceDividerCount() {
		for (OriEqualDistSymbol eds : Origrammer.diagram.steps.get(Globals.currentStep).equalDistSymbols) {
			if (eds.isSelected()) {
				eds.setDividerCount(Integer.parseInt(equalDistDividerTF.getText()));
			}
			screen.repaint();
		}
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
				arrow.getLabel().setBounds((int) arrow.getxPos(), (int)arrow.getyPos(), 
												(int) Math.round(arrow.getWidth() * arrow.getScale()), 
												(int) Math.round(arrow.getHeight() * arrow.getScale()));
			}
		}
		
		for (OriPicSymbol symbol : Origrammer.diagram.steps.get(Globals.currentStep).picSymbols) {
			if (symbol.isSelected()) {
				symbol.setScale((double) sliderScaleIcon.getValue()/100);
				screen.repaint();

				//TODO: add preview pictures of arrow types
				symbol.getLabel().setBounds((int) symbol.getxPos(), (int)symbol.getyPos(), 
												(int) Math.round(symbol.getWidth() * symbol.getScale()), 
												(int) Math.round(symbol.getHeight() * symbol.getScale()));
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
				
				arrow.getLabel().setBounds((int)arrow.getxPos(), (int)arrow.getyPos(), (int)rect.getWidth(), (int)rect.getHeight());
			}
		}
		
		for (OriPicSymbol symbol : Origrammer.diagram.steps.get(Globals.currentStep).picSymbols) {
			if (symbol.isSelected()) {
				symbol.setDegrees(sliderRotIcon.getValue()/10);
				screen.repaint();
				
				Rectangle2D rect = GeometryUtil.calcRotatedBox(symbol.getxPos(), symbol.getyPos(), symbol.getWidth(), symbol.getHeight(), symbol.getDegrees());
				
				symbol.getLabel().setBounds((int)symbol.getxPos(), (int)symbol.getyPos(), (int)rect.getWidth(), (int)rect.getHeight());
			}
		}
	}
	
	/**
	 * Sets the translation distance of equalDistanceSymbol
	 */
	private void sliderEqualDistChanged() {
		System.out.println("equalDistSliderChanged");
		
		for (OriEqualDistSymbol eds : Origrammer.diagram.steps.get(Globals.currentStep).equalDistSymbols) {
			if (eds.isSelected()) {
				eds.setTranslationDist(sliderEqualDist.getValue());
				screen.repaint();
			}
		}
	}
	
		
	@Override
	public void actionPerformed(ActionEvent e) {
		modeChanged();

		if (e.getSource() == changeLineButton) {
			for (OriLine l : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
				if (l.isSelected()) {
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
			Object selectedLine = menuLineCB.getSelectedItem();
			
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
	
	private int getSelectedTypes() {
		
		//	0000 0001	LINES
		//	0000 0010	ARROWS
		//  0000 0100	FILLED_FACES
		//  0000 1000	LEADER
		//  0001 0000	REPE_BOXES
		//  0010 0000	PIC_SYMBOLS
		//  0100 0000	GEO_SYMBOLS
		//  1000 0000	EQUAL_DIST_SYMBOLS
		
		int selectedTypes = 0b00000000;
		
		
		for (OriLine line : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
			if (line.isSelected()) {
				selectedTypes += 0b00000001;
				break;
			}
		}
		for (OriArrow arrow : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
			if (arrow.isSelected()) {
				selectedTypes += 0b00000010;
				break;
			}
		}
		for (OriFace face : Origrammer.diagram.steps.get(Globals.currentStep).filledFaces) {
			if (face.isSelected()) {
				selectedTypes += 0b00000100;
				break;
			}
		}
		for (OriLeader leader : Origrammer.diagram.steps.get(Globals.currentStep).leader) {
			if (leader.isSelected()) {
				selectedTypes += 0b00001000;
				break;
			}
		}
		for (OriRepetitionBox repeBox : Origrammer.diagram.steps.get(Globals.currentStep).repetitionBoxes) {
			if (repeBox.isSelected()) {
				selectedTypes += 0b00010000;
				break;
			}
		}
		for (OriPicSymbol picS : Origrammer.diagram.steps.get(Globals.currentStep).picSymbols) {
			if (picS.isSelected()) {
				selectedTypes += 0b00100000;
				break;
			}
		}
		for (OriGeomSymbol geoS : Origrammer.diagram.steps.get(Globals.currentStep).geomSymbols) {
			if (geoS.isSelected()) {
				selectedTypes += 0b01000000;
				break;
			}
		}
		for (OriEqualDistSymbol equalDist : Origrammer.diagram.steps.get(Globals.currentStep).equalDistSymbols) {
			if (equalDist.isSelected()) {
				selectedTypes += 0b10000000;
				break;
			}
		}
		return selectedTypes;
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
		
		if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_SYMBOL) {
			inputSymbolsPanel.setVisible(true);
			if (Globals.inputSymbolMode == Constants.InputSymbolMode.LEADER) {
				inputSymbolLeaderPanel.setVisible(true);
			} else {
				inputSymbolLeaderPanel.setVisible(false);
			}
			if (Globals.inputSymbolMode == Constants.InputSymbolMode.REPETITION_BOX) {
				inputSymbolRepetitionPanel.setVisible(true);
			} else {
				inputSymbolRepetitionPanel.setVisible(false);
			}
			if (Globals.inputSymbolMode == Constants.InputSymbolMode.EQUAL_DIST) {
				equalDistPanel.setVisible(true);
			} else {
				equalDistPanel.setVisible(false);
			}
		} else {
			inputSymbolsPanel.setVisible(false);
			inputSymbolLeaderPanel.setVisible(false);
			inputSymbolRepetitionPanel.setVisible(false);
			equalDistPanel.setVisible(false);
		}
		
		if (Globals.toolbarMode == Constants.ToolbarMode.FILL_TOOL) {
			faceDirectionPanel.setVisible(true);
		} else {
			faceDirectionPanel.setVisible(false);
		}
		
		if (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL) {
			
			//	0000 0001	LINES
			//	0000 0010	ARROWS
			//  0000 0100	FILLED_FACES
			//  0000 1000	LEADER
			//  0001 0000	REPE_BOXES
			//  0010 0000	PIC_SYMBOLS
			//  0100 0000	GEO_SYMBOLS
			//  1000 0000	EQUAL_DIST_SYMBOLS
			int selectedTypes = getSelectedTypes();
			
			int lineMask 	= 0b00000001;
			int arrowMask 	= 0b00000010;
			int faceMask	= 0b00000100;
			int leaderMask	= 0b00001000;
			int repeMask	= 0b00010000;
			int picMask		= 0b00100000;
			int geoMask		= 0b01000000;
			int equDistMask = 0b10000000;
			int result = selectedTypes & lineMask;
			
			
			if (result == 0b00000001) {
				System.out.println("lines");
				changeLinePanel.setVisible(true);
			} else {
				changeLinePanel.setVisible(false);
			}
			result = selectedTypes & arrowMask;
			if (result == 0b00000010) {
				System.out.println("arrow");
				changeArrowPanel.setVisible(true);
				sliderPanel.setVisible(true);
			} else {
				changeArrowPanel.setVisible(false);
				sliderPanel.setVisible(false);
			}
			result = selectedTypes & faceMask;
			if (result == 0b00000100) {
				System.out.println("faces");
				faceDirectionPanel.setVisible(true);
			} else {
				faceDirectionPanel.setVisible(false);
			}
			result = selectedTypes & leaderMask;
			if (result == 0b00001000) {
				System.out.println("leader");
				inputSymbolLeaderPanel.setVisible(true);
			} else {
				inputSymbolLeaderPanel.setVisible(false);
			}
			result = selectedTypes & repeMask;
			if (result == 0b00010000) {
				System.out.println("repe");
				inputSymbolRepetitionPanel.setVisible(true);
			} else {
				inputSymbolRepetitionPanel.setVisible(false);
			}
			result = selectedTypes & picMask;
			if (result == 0b00100000) {
				System.out.println("picSymbols");
				sliderPanel.setVisible(true); //TODO: MAYBE OWN SLIDER FOR PIC_SYMBOLS
			} else {
				sliderPanel.setVisible(false);
			}
			result = selectedTypes & geoMask;
			if (result == 0b01000000) {
				System.out.println("geoSymbols");
				//TODO: editing options for geoSymbols
			}
			result = selectedTypes & equDistMask;
			if (result == 0b10000000) {
				System.out.println("equDist");
				equalDistPanel.setVisible(true);
			} else {
				equalDistPanel.setVisible(false);
			}
			
		
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
			
			if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_ARROW) {
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
				modeChanged();
			} else if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_SYMBOL) {
				Object inputSymbol = symbolInputCB.getSelectedItem();
				
				if (inputSymbol == "Leader") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.LEADER;
					Globals.inputSymbolType = OriPicSymbol.TYPE_NONE;
				} else if (inputSymbol == "Repetition Box") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.REPETITION_BOX;
					Globals.inputSymbolType = OriPicSymbol.TYPE_NONE;
				} else if (inputSymbol == "Equal Distances") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.EQUAL_DIST;
					Globals.inputSymbolType = OriPicSymbol.TYPE_NONE;
				} else if (inputSymbol == "Equal Angles") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.EQUAL_ANGL;
					Globals.inputSymbolType = OriPicSymbol.TYPE_NONE;
				} else if (inputSymbol == "Rotations") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.ROTATIONS;
					Globals.inputSymbolType = OriPicSymbol.TYPE_ROTATION;
				} else if (inputSymbol == "X-Ray Circle") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.X_RAY_CIRCLE;
					Globals.inputSymbolType = OriPicSymbol.TYPE_XRAY_CIRCLE;
				} else if (inputSymbol == "Fold over and over") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.FOLD_OVER_AND_OVER;
					Globals.inputSymbolType = OriPicSymbol.TYPE_NONE;
				} else if (inputSymbol == "Next View Here") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.NEXT_VIEW;
					Globals.inputSymbolType = OriPicSymbol.TYPE_NEXT_VIEW_HERE;
				} else if (inputSymbol == "Hold Here") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.HOLD_HERE;
					Globals.inputSymbolType = OriPicSymbol.TYPE_HOLD;
				} else if (inputSymbol == "Hold Here and Pull") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.HOLD_HERE_AND_PULL;
					Globals.inputSymbolType = OriPicSymbol.TYPE_HOLD_AND_PULL;
				} else if (inputSymbol == "Crimping & Pleating") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.CRIMPING_PLEATING;
					Globals.inputSymbolType = OriPicSymbol.TYPE_NONE;
				} else if (inputSymbol == "Sinks") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.SINKS;
					Globals.inputSymbolType = OriPicSymbol.TYPE_NONE;
				}
				modeChanged();
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
