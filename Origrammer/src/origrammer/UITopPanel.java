package origrammer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.PlainDocument;

import origrammer.geometry.GeometryUtil;
import origrammer.geometry.OriArrow;
import origrammer.geometry.OriEqualAnglSymbol;
import origrammer.geometry.OriEqualDistSymbol;
import origrammer.geometry.OriFace;
import origrammer.geometry.OriGeomSymbol;
import origrammer.geometry.OriLeaderBox;
import origrammer.geometry.OriLine;
import origrammer.geometry.OriPicSymbol;
import origrammer.geometry.OriPleatCrimpSymbol;

public class UITopPanel extends JPanel implements ActionListener, PropertyChangeListener, KeyListener {

	private String[] lineInputOptions = {"Valley Fold", "Mountain Fold", "X-Ray Fold", "Edge Line", "Existing Crease"};
	private Object[] arrowInputOptions = {"Valley Fold", "Mountain Fold", "Turn over", 
									new JSeparator(JSeparator.HORIZONTAL),
									"Push here", "Pull out", "Inflate here"};
	private Object[] symbolInputOptions = {"Leader", "Repetition Box", "Next View Here", "Rotations", 
			"Hold Here", "Hold Here and Pull", new JSeparator(JSeparator.HORIZONTAL),  
			"X-Ray Circle", "Fold over and over", "Equal Distances", "Equal Angles", 
			new JSeparator(JSeparator.HORIZONTAL),  "Crimping & Pleating", "Sinks"};

	
	//INPUT LINES/ ARROWS/ SYMBOLS
	private JPanel inputLinesPanel = new JPanel();
	private JPanel inputArrowPanel = new JPanel();
	private JPanel inputSymbolsPanel = new JPanel();
	private JComboBox<String> menuLineCB = new JComboBox<>(lineInputOptions);
	private JComboBox<Object> menuArrowCB = new JComboBox<>(arrowInputOptions);
	private JComboBox<Object> symbolInputCB = new JComboBox<>(symbolInputOptions);
	
	//INPUT VERTICES FRACTION_OF_LINE
	private JPanel inputVertexFractionPanel = new JPanel();
	private JSlider inputVertexFractionSlider = new JSlider(0, 100);
	public JTextField inputVertexFractionTF = new JTextField();
	
	//INPUT SYMBOL LEADER
	private JPanel inputSymbolLeaderPanel = new JPanel();
	public JTextField inputLeaderText = new JTextField();
	
	//CHANGE SYMBOL LEADER
	private JPanel changeSymbolLeaderPanel = new JPanel();
	private JTextField changeLeaderText = new JTextField();
	private JButton changeSymbolLeaderButton = new JButton("Set");
	
	//INPUT SYMBOL REPETITION BOX
//	private JPanel inputSymbolRepetitionPanel = new JPanel();
//	public JTextField inputRepetitionText = new JTextField();
	
	//FACE UP/ FACE DOWN COLOR
	private JPanel faceDirectionPanel = new JPanel();
	private JRadioButton faceUpInput = new JRadioButton("Face Up", false);
	private JRadioButton faceDownInput = new JRadioButton("Face Down", true);
	
	//CHANGE LINE/ARROW TYPE
	private JPanel changeLinePanel = new JPanel();
	private JPanel changeArrowPanel = new JPanel();
	private JComboBox<String> changeLineTypeCB = new JComboBox<>(lineInputOptions);
	private JComboBox<Object> changeArrowTypeCB = new JComboBox<>(arrowInputOptions);
	private JButton changeLineButton = new JButton("Set");
	private JButton changeArrowButton = new JButton("Set");
	
	//Change Existing Crease ends
	private JPanel changeCreaseEndsPanel = new JPanel();
	public JCheckBox startCreaseCB = new JCheckBox("Start");
	public JCheckBox endCreaseCB = new JCheckBox("End");
	
	//EQUAL DISTANCE SETTINGS
	private JPanel equalDistPanel = new JPanel();
	public JSlider sliderEqualDist = new JSlider(-50, 50);
	public JTextField equalDistDividerTF = new JTextField();
	public JButton equalDistButton = new JButton("Set");
	
	//EQUAL ANGLE SETTINGS
	private JPanel equalAnglPanel = new JPanel();
	private JSlider sliderEqualAngl = new JSlider(50,600);
	public JTextField equalAnglDividerTF = new JTextField();
	private JButton equalAnglButton = new JButton("Set");
	
	//PLEATNG/CRIMPING SETTINGS
	public JRadioButton pleatRB = new JRadioButton("Pleat", true);
	public JRadioButton crimpRB = new JRadioButton("Crimp");
	
	private JPanel pleatPanel = new JPanel();
	public JCheckBox pleatCB = new JCheckBox("reverseDir");
	public JTextField pleatTF = new JTextField();
	private JButton pleatButton = new JButton("Set");

	//ARROWS SETTINGS
	private JPanel sliderPanel = new JPanel();
	private JSlider arrowScaleSlider = new JSlider(0, 100);
	private JSlider arrowRotSlider = new JSlider(0, 3600);
	
	//ROTATE/SCALE ORI_PIC_SYMBOLS
	private JPanel picSymbolPanel = new JPanel();
	private JSlider picSymbolScaleSlider = new JSlider(0, 100);
	private JSlider picSymbolRotSlider = new JSlider(0, 3600);
	
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
		//##### CHANGE LINE TYPE #####
		changeLineButton.addActionListener(this);
		changeLinePanel.add(changeLineTypeCB);
		changeLinePanel.add(changeLineButton);
		changeLinePanel.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
											getBackground().darker(), 
											getBackground().brighter()), "Change Line Type"));
		
		//##### CHANGE EXISTING CREASE ENDS
		startCreaseCB.addActionListener(this);
		endCreaseCB.addActionListener(this);
		changeCreaseEndsPanel.add(startCreaseCB);
		changeCreaseEndsPanel.add(endCreaseCB);
		changeCreaseEndsPanel.setBorder(new TitledBorder(
										new EtchedBorder(BevelBorder.RAISED, 
															getBackground().darker(), 
															getBackground().brighter()), "Existing Crease Settingss"));
		
		
		//-----------------------------------------------------------------------------------------
		
		//##### VERTEX FRACTION_OF_LINE
		inputVertexFractionSlider.setMajorTickSpacing(10);
		//inputVertexFractionSlider.setSnapToTicks(true);
		inputVertexFractionSlider.setPaintTicks(true);
		inputVertexFractionSlider.addChangeListener(e -> sliderVertexFraction());
		inputVertexFractionTF.setPreferredSize(new Dimension(30, 30));
		PlainDocument docInputVertexFraction = (PlainDocument) inputVertexFractionTF.getDocument();
		docInputVertexFraction.setDocumentFilter(new IntFilter());
		docInputVertexFraction.addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				//setVertexFractionSlider();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				setVertexFractionSlider();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				setVertexFractionSlider();
			}
			
			private void setVertexFractionSlider() {
				Runnable doSetVertexFractionSlider = new Runnable() {
					@Override
					public void run() {
						String text = inputVertexFractionTF.getText();
						int value = Integer.parseInt(text);

						inputVertexFractionSlider.setValue(value);
					}
				};       
				SwingUtilities.invokeLater(doSetVertexFractionSlider);
			}
		});
		inputVertexFractionTF.setText(Integer.toString(inputVertexFractionSlider.getValue()));
		inputVertexFractionPanel.add(inputVertexFractionSlider);
		inputVertexFractionPanel.add(inputVertexFractionTF);
		inputVertexFractionPanel.setBorder(new TitledBorder(
									new EtchedBorder(BevelBorder.RAISED, 
													getBackground().darker(), 
													getBackground().brighter()), "Input Vertex"));
		
		
		//-----------------------------------------------------------------------------------------
		//##### ARROW INPUT TYPE #####
		menuArrowCB.setRenderer(new SeparatorComboBoxRenderer());
		menuArrowCB.setSelectedIndex(0);
		menuArrowCB.addActionListener(new SeparatorComboBoxListener(menuArrowCB));
		
		inputArrowPanel.add(menuArrowCB);
		inputArrowPanel.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(), 
												getBackground().brighter()), "Input Arrow Type"));
		//##### CHANGE ARROW TYPE #####
		changeArrowButton.addActionListener(this);
		changeArrowTypeCB.setRenderer(new SeparatorComboBoxRenderer());
		changeArrowPanel.add(changeArrowTypeCB);
		changeArrowPanel.add(changeArrowButton);
		changeArrowPanel.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(), 
												getBackground().brighter()), "Change Arrow Type"));
		//##### ARROW SETTINGS #####
		arrowScaleSlider.setMajorTickSpacing(20);
		arrowScaleSlider.setMinorTickSpacing(10);
		arrowScaleSlider.setPaintTicks(true);
		arrowScaleSlider.setPaintLabels(true);
		arrowScaleSlider.addChangeListener(e -> sliderArrowScale());
		arrowScaleSlider.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(), 
												getBackground().brighter()), "Scale Arrow"));
		
		Hashtable<Integer, JLabel> labels = new Hashtable<>();
		labels.put(0,  new JLabel("0"));
		labels.put(900, new JLabel("90°"));
		labels.put(1800, new JLabel("180°"));
		labels.put(2700, new JLabel("270°"));
		labels.put(3600, new JLabel("360°"));
		arrowRotSlider.setLabelTable(labels);
		arrowRotSlider.setMajorTickSpacing(900);
		arrowRotSlider.setMinorTickSpacing(225);
		arrowRotSlider.setPaintTicks(true);
		arrowRotSlider.setPaintLabels(true);
		arrowRotSlider.setSnapToTicks(true);
		arrowRotSlider.addChangeListener(e -> sliderArrowRotChanged());
		arrowRotSlider.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(), 
												getBackground().brighter()), "Rotate Arrow"));
		sliderPanel.add(arrowScaleSlider);
		sliderPanel.add(arrowRotSlider);

		//-----------------------------------------------------------------------------------------
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
												getBackground().brighter()), "Input Text"));
		
		//##### CHANGE SYMBOL LEADER #####
		changeLeaderText.setPreferredSize(new Dimension(150, 25));
		changeSymbolLeaderPanel.add(changeLeaderText);
		changeSymbolLeaderPanel.add(changeSymbolLeaderButton);
		changeSymbolLeaderButton.addActionListener(this);
		changeSymbolLeaderPanel.setBorder(new TitledBorder(
										new EtchedBorder(BevelBorder.RAISED, 
														getBackground().darker(), 
														getBackground().brighter()), "Change Text"));
	
		//##### ORI_PIC_SYMBOL SETTINGS #####
		picSymbolScaleSlider.setMajorTickSpacing(10);
		picSymbolScaleSlider.setPaintTicks(true);
		//picSymbolScaleSlider.setPaintLabels(true);
		picSymbolScaleSlider.addChangeListener(e -> sliderPicSymbolScale());
		picSymbolScaleSlider.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(), 
												getBackground().brighter()), "Scale Symbol"));
		picSymbolRotSlider.setMajorTickSpacing(225);
		picSymbolRotSlider.setPaintTicks(true);
		//sliderRotIcon.setPaintLabels(true);
		picSymbolRotSlider.setSnapToTicks(true);
		picSymbolRotSlider.addChangeListener(e -> sliderPicSymbolRot());
		picSymbolRotSlider.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(), 
												getBackground().brighter()), "Rotate Symbol"));
		picSymbolPanel.add(picSymbolScaleSlider);
		picSymbolPanel.add(picSymbolRotSlider);
		
		//##### EQUAL DIST SETTINGS
		sliderEqualDist.setMajorTickSpacing(10);
		sliderEqualDist.setPaintTicks(true);
		sliderEqualDist.addChangeListener(e -> sliderEqualDistChanged());
		equalDistDividerTF.setPreferredSize(new Dimension(20,20));
		PlainDocument docEqualDistDivider = (PlainDocument) equalDistDividerTF.getDocument();
		docEqualDistDivider.setDocumentFilter(new IntFilter());
		equalDistDividerTF.setText(Integer.toString(Globals.gridDivNum));
		equalDistButton.addActionListener(this);
		equalDistPanel.add(sliderEqualDist);
		equalDistPanel.add(equalDistDividerTF);
		equalDistPanel.add(equalDistButton);
		equalDistPanel.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(),
												getBackground().brighter()), "Equal Distance"));

		//##### EQUAL ANGLE SETTINGS
		sliderEqualAngl.setMajorTickSpacing(50);
		sliderEqualAngl.setSnapToTicks(true);
		sliderEqualAngl.setPaintTicks(true);
		sliderEqualAngl.addChangeListener(e -> sliderEqualAnglChanged());
		equalAnglDividerTF.setPreferredSize(new Dimension(20,20));
		PlainDocument docEqualAnglDivider = (PlainDocument) equalDistDividerTF.getDocument();
		docEqualAnglDivider.setDocumentFilter(new IntFilter());
		equalAnglDividerTF.setText(Integer.toString(Globals.gridDivNum));
		equalAnglButton.addActionListener(this);
		equalAnglPanel.add(sliderEqualAngl);
		sliderEqualAngl.setVisible(false);
		equalAnglPanel.add(equalAnglDividerTF);
		equalAnglPanel.add(equalAnglButton);
		equalAnglPanel.setBorder(new TitledBorder(
								new EtchedBorder(BevelBorder.RAISED, 
												getBackground().darker(),
												getBackground().brighter()), "Equal Angle"));
		
		//##### PLEATS/CRIMPS SETTINGS
		ButtonGroup pleatCrimpGroup = new ButtonGroup();
		pleatCrimpGroup.add(pleatRB);
		pleatCrimpGroup.add(crimpRB);
		JPanel crimpPleatPanel = new JPanel();
		crimpPleatPanel.add(pleatRB);
		crimpPleatPanel.add(crimpRB);
		crimpPleatPanel.setLayout(new BoxLayout(crimpPleatPanel, BoxLayout.PAGE_AXIS));
		pleatRB.addActionListener(this);
		crimpRB.addActionListener(this);
		pleatTF.setPreferredSize(new Dimension(20,20));
		pleatTF.setText("2");
		PlainDocument docPleatLayerCount = (PlainDocument) pleatTF.getDocument();
		docPleatLayerCount.setDocumentFilter(new IntFilter());
		pleatButton.addActionListener(this);
		pleatCB.addActionListener(this);
		pleatPanel.add(crimpPleatPanel);
		pleatPanel.add(pleatCB);
		pleatPanel.add(pleatTF);
		pleatPanel.add(pleatButton);
		pleatPanel.setBorder(new TitledBorder(
							new EtchedBorder(BevelBorder.RAISED, 
											getBackground().darker(),
											getBackground().brighter()), "Pleating"));

		//add all panels to UITopPanel
		add(changeLinePanel);
		add(changeCreaseEndsPanel);
		add(changeArrowPanel);
		add(faceDirectionPanel);
		add(inputLinesPanel);
		add(inputVertexFractionPanel);
		add(inputArrowPanel);
		add(inputSymbolsPanel);
		add(picSymbolPanel);
		add(inputSymbolLeaderPanel);
		add(changeSymbolLeaderPanel);
		add(equalDistPanel);
		add(equalAnglPanel);
		add(pleatPanel);
		add(sliderPanel);
	
		modeChanged();
	}
	
	private void changeOriLeaderBoxText() {
		for (OriLeaderBox s : Origrammer.diagram.steps.get(Globals.currentStep).leaderBoxSymbols) {
			if (s.isSelected()) {
				s.getLabel().setText(changeLeaderText.getText());
				s.setLabelBounds(s.getLabelBounds((Graphics2D) screen.getGraphics()));
			}
		}
	}
	
	private void setEqualDistanceDividerCount() {
		for (OriEqualDistSymbol eds : Origrammer.diagram.steps.get(Globals.currentStep).equalDistSymbols) {
			if (eds.isSelected()) {
				eds.setDividerCount(Integer.parseInt(equalDistDividerTF.getText()));
			}
			screen.repaint();
		}
	}
	
	private void setEqualAngleDividerCount() {
		for (OriEqualAnglSymbol eas : Origrammer.diagram.steps.get(Globals.currentStep).equalAnglSymbols) {
			if (eas.isSelected()) {
				eas.setDividerCount(Integer.parseInt(equalAnglDividerTF.getText()));
			}
			screen.repaint();

		}
	}
	
	private void setPleatLayerCount() {
		for (OriPleatCrimpSymbol pleat : Origrammer.diagram.steps.get(Globals.currentStep).pleatCrimpSymbols) {
			if (pleat.isSelected()) {
				pleat.setLayersCount(Integer.parseInt(pleatTF.getText()));
			}
			screen.repaint();
		}
	}
	
	private void setPleatIsSwitchedDir() {
		for (OriPleatCrimpSymbol pleat : Origrammer.diagram.steps.get(Globals.currentStep).pleatCrimpSymbols) {
			if (pleat.isSelected()) {
				pleat.setIsSwitchedDir(pleatCB.isSelected());
			}
			screen.repaint();
		}
	}
	
	private void changeToPleat() {
		for(OriPleatCrimpSymbol pc : Origrammer.diagram.steps.get(Globals.currentStep).pleatCrimpSymbols) {
			if (pc.isSelected()) {
				pc.setType(OriPleatCrimpSymbol.TYPE_PLEAT);
			}
			screen.repaint();
		}
	}
	
	private void changeToCrimp() {
		for(OriPleatCrimpSymbol pc : Origrammer.diagram.steps.get(Globals.currentStep).pleatCrimpSymbols) {
			if (pc.isSelected()) {
				pc.setType(OriPleatCrimpSymbol.TYPE_CRIMP);
			}
			screen.repaint();
		}
	}
	
	
	private void sliderVertexFraction() {
		inputVertexFractionTF.setText(Integer.toString(inputVertexFractionSlider.getValue()));
	}


	/**
	 * Sets the scale for all selected OriArrows
	 */
	private void sliderArrowScale() {
		for (OriArrow arrow : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
			if (arrow.isSelected()) {
				if (arrowScaleSlider.getValue() == 0) {
					arrow.setScale(0.01);
				} else {
					arrow.setScale((double) arrowScaleSlider.getValue()/100);
				}
				screen.repaint();

				//TODO: add preview pictures of arrow types
				arrow.getLabel().setBounds((int) arrow.getPosition().x, (int)arrow.getPosition().y, 
						(int) Math.round(arrow.getWidth() * arrow.getAdjustedScale()), 
						(int) Math.round(arrow.getHeight() * arrow.getAdjustedScale()));
			}
		}
	}

	
	/**
	 * changes the scale for all selected OriPicSymbol
	 */
	private void sliderPicSymbolScale() {
		for (OriPicSymbol symbol : Origrammer.diagram.steps.get(Globals.currentStep).picSymbols) {
			if (symbol.isSelected()) {
				symbol.setScale((double) picSymbolScaleSlider.getValue()/100);
				screen.repaint();

				//TODO: add preview pictures of arrow types
				symbol.getLabel().setBounds((int) symbol.getPosition().x, (int) symbol.getPosition().y, 
												(int) Math.round(symbol.getWidth() * symbol.getAdjustedScale()), 
												(int) Math.round(symbol.getHeight() * symbol.getAdjustedScale()));
			}
		}
	}	
	
	/**
	 * Sets the rotation of all selected OriArrows
	 */
	private void sliderArrowRotChanged() {
		for (OriArrow arrow : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
			if (arrow.isSelected()) {
				arrow.setDegrees(arrowRotSlider.getValue()/10);
				screen.repaint();
				
				Rectangle2D rect = GeometryUtil.calcRotatedBox(arrow.getPosition().x, arrow.getPosition().y, arrow.getWidth(), arrow.getHeight(), arrow.getDegrees());
				
				arrow.getLabel().setBounds((int) arrow.getPosition().x, (int) arrow.getPosition().y, (int) rect.getWidth(), (int) rect.getHeight());
			}
		}
	}
	
	/**
	 * Sets the rotation of all selected OriPicSymbols
	 */
	private void sliderPicSymbolRot() {
		for (OriPicSymbol symbol : Origrammer.diagram.steps.get(Globals.currentStep).picSymbols) {
			if (symbol.isSelected()) {
				symbol.setDegrees(picSymbolRotSlider.getValue()/10);
				screen.repaint();
				
				Rectangle2D rect = GeometryUtil.calcRotatedBox(symbol.getPosition().x, symbol.getPosition().y, symbol.getWidth(), symbol.getHeight(), symbol.getDegrees());
				
				symbol.getLabel().setBounds((int)symbol.getPosition().x, (int)symbol.getPosition().y, (int)rect.getWidth(), (int)rect.getHeight());
			}
		}
	}
	
	/**
	 * Sets the translation distance of equalDistanceSymbol
	 */
	private void sliderEqualDistChanged() {		
		for (OriEqualDistSymbol eds : Origrammer.diagram.steps.get(Globals.currentStep).equalDistSymbols) {
			if (eds.isSelected()) {
				eds.setTranslationDist(sliderEqualDist.getValue());
				screen.repaint();
			}
		}
	}
	
	/**
	 * Sets the translation distance of equalDistanceSymbol
	 */
	private void sliderEqualAnglChanged() {		
		for (OriEqualAnglSymbol eas : Origrammer.diagram.steps.get(Globals.currentStep).equalAnglSymbols) {
			if (eas.isSelected()) {
				eas.setLineLength(sliderEqualAngl.getValue());
				screen.repaint();
			}
		}
	}
	
	private void creaseStartTranslation() {
		for (OriLine l : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
			if (l.isSelected()) {
				if (startCreaseCB.isSelected()) {
					l.setStartOffset(true);
				} else if (!startCreaseCB.isSelected()) {
					l.setStartOffset(false);
				}
			}
		}
	}

	private void creaseEndTranslation() {
		for (OriLine l : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
			if (l.isSelected()) {
				if (endCreaseCB.isSelected()) {
					l.setEndOffset(true);
				} else if (!endCreaseCB.isSelected()) {
					l.setEndOffset(false);
				}
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
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
					} else if (lineType == "Existing Crease") {
						l.setType(OriLine.TYPE_CREASE);
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

		} else if (e.getSource() == changeSymbolLeaderButton) {
			changeOriLeaderBoxText();
		} else if (e.getSource() == faceUpInput) {
			Globals.faceInputDirection = Constants.FaceInputDirection.FACE_UP;
		} else if (e.getSource() == faceDownInput) {
			Globals.faceInputDirection = Constants.FaceInputDirection.FACE_DOWN;
		} else if (e.getSource() == equalDistButton) {
			setEqualDistanceDividerCount();
		} else if (e.getSource() == equalAnglButton) {
			setEqualAngleDividerCount();
		} else if (e.getSource() == pleatButton) {
			setPleatLayerCount();
		} else if (e.getSource() == pleatCB) {
			setPleatIsSwitchedDir();
		} else if (e.getSource() == pleatRB) {
			changeToPleat();
		} else if (e.getSource() == crimpRB) {
			changeToCrimp();
		} else if (e.getSource() == startCreaseCB) {
			creaseStartTranslation();				
		} else if (e.getSource() == endCreaseCB) {
			creaseEndTranslation();
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
			} else if (selectedLine == "Existing Crease") {
				Globals.inputLineType = OriLine.TYPE_CREASE;
			}
		}
		modeChanged();
	}
	
	
	private int getSelectedTypes() {
		
		//	0 0000 0001	LINES
		//	0 0000 0010	ARROWS
		//  0 0000 0100	FILLED_FACES
		//  0 0000 1000	LEADER
		//  0 0001 0000	PIC_SYMBOLS
		//  0 0010 0000	GEO_SYMBOLS
		//  0 0100 0000	EQUAL_DIST_SYMBOLS
		//  0 1000 0000	EQUAL_ANGL_SYMBOLS
		//  1 0000 0000	PLEAT

		
		int selectedTypes = 0b000000000;
		
		for (OriLine line : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
			if (line.isSelected()) {
				selectedTypes += 0b000000001;
				break;
			}
		}
		for (OriArrow arrow : Origrammer.diagram.steps.get(Globals.currentStep).arrows) {
			if (arrow.isSelected()) {
				selectedTypes += 0b000000010;
				break;
			}
		}
		for (OriFace face : Origrammer.diagram.steps.get(Globals.currentStep).filledFaces) {
			if (face.isSelected()) {
				selectedTypes += 0b000000100;
				break;
			}
		}
		for (OriLeaderBox leader : Origrammer.diagram.steps.get(Globals.currentStep).leaderBoxSymbols) {
			if (leader.isSelected()) {
				selectedTypes += 0b000001000;
				break;
			}
		}
		for (OriPicSymbol picS : Origrammer.diagram.steps.get(Globals.currentStep).picSymbols) {
			if (picS.isSelected()) {
				selectedTypes += 0b000010000;
				break;
			}
		}
		for (OriGeomSymbol geoS : Origrammer.diagram.steps.get(Globals.currentStep).geomSymbols) {
			if (geoS.isSelected()) {
				selectedTypes += 0b000100000;
				break;
			}
		}
		for (OriEqualDistSymbol equalDist : Origrammer.diagram.steps.get(Globals.currentStep).equalDistSymbols) {
			if (equalDist.isSelected()) {
				selectedTypes += 0b001000000;
				break;
			}
		}
		for (OriEqualAnglSymbol equalAngl : Origrammer.diagram.steps.get(Globals.currentStep).equalAnglSymbols) {
			if (equalAngl.isSelected()) {
				selectedTypes += 0b010000000;
				break;
			}
		}
		for (OriPleatCrimpSymbol pleat : Origrammer.diagram.steps.get(Globals.currentStep).pleatCrimpSymbols) {
			if (pleat.isSelected()) {
				selectedTypes += 0b100000000;
				break;
			}
		}
		return selectedTypes;
	}
	
	public void modeChanged() {
			
		if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_LINE) {
			inputLinesPanel.setVisible(true);
			if (Globals.inputLineType == OriLine.TYPE_CREASE) {
				changeCreaseEndsPanel.setVisible(true);
			} else {
				changeCreaseEndsPanel.setVisible(false);
			}
		} else {
			inputLinesPanel.setVisible(false);
			changeCreaseEndsPanel.setVisible(false);
		}
		
		if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_VERTEX
				&& Globals.vertexInputMode == Constants.VertexInputMode.FRACTION_OF_LINE) {
			inputVertexFractionPanel.setVisible(true);
		} else {
			inputVertexFractionPanel.setVisible(false);
		}
		
		if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_ARROW) {
			inputArrowPanel.setVisible(true);			
		} else {
			inputArrowPanel.setVisible(false);			
		}
		
		if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_SYMBOL) {
			inputSymbolsPanel.setVisible(true);
			if (Globals.inputSymbolMode == Constants.InputSymbolMode.LEADER 
				|| Globals.inputSymbolMode == Constants.InputSymbolMode.REPETITION_BOX) {
				inputSymbolLeaderPanel.setVisible(true);
			} else {
				inputSymbolLeaderPanel.setVisible(false);
			}
			if (Globals.inputSymbolMode == Constants.InputSymbolMode.EQUAL_DIST) {
				equalDistPanel.setVisible(true);
			} else {
				equalDistPanel.setVisible(false);
			}
			if (Globals.inputSymbolMode == Constants.InputSymbolMode.EQUAL_ANGL) {
				equalAnglPanel.setVisible(true);
			} else {
				equalAnglPanel.setVisible(false);
			}
			if (Globals.inputSymbolMode == Constants.InputSymbolMode.CRIMPING_PLEATING) {
				pleatPanel.setVisible(true);
			} else {
				pleatPanel.setVisible(false);
			}
		} else {
			inputSymbolsPanel.setVisible(false);
			inputSymbolLeaderPanel.setVisible(false);
			changeSymbolLeaderPanel.setVisible(false);
			//inputSymbolRepetitionPanel.setVisible(false);
			equalDistPanel.setVisible(false);
			equalAnglPanel.setVisible(false);
			pleatPanel.setVisible(false);
		}
		
		if (Globals.toolbarMode == Constants.ToolbarMode.FILL_TOOL) {
			faceDirectionPanel.setVisible(true);
		} else {
			faceDirectionPanel.setVisible(false);
		}
		
		if (Globals.toolbarMode == Constants.ToolbarMode.SELECTION_TOOL) {
			
			//	0 0000 0001	LINES
			//  0 0000 0010	ARROWS
			//  0 0000 0100	FILLED_FACES
			//  0 0000 1000	LEADER
			//  0 0001 0000	PIC_SYMBOLS
			//  0 0010 0000	GEO_SYMBOLS
			//  0 0100 0000	EQUAL_DIST_SYMBOLS
			//  0 1000 0000	EQUAL_ANGL_SYMBOLS
			//  1 0000 0000	PLEAT
			int selectedTypes = getSelectedTypes();
			
			int lineMask 	= 0b000000001;
			int arrowMask 	= 0b000000010;
			int faceMask	= 0b000000100;
			int leaderMask	= 0b000001000;
			int picMask		= 0b000010000;
			int geoMask		= 0b000100000;
			int equDistMask = 0b001000000;
			int equAnglMask = 0b010000000;
			int pleatMask 	= 0b100000000;

			int result = selectedTypes & lineMask;
			if (result == 0b000000001) {
				changeLinePanel.setVisible(true);
				for (OriLine l : Origrammer.diagram.steps.get(Globals.currentStep).lines) {
					if (l.isSelected() && l.getType() == OriLine.TYPE_CREASE) {
						changeCreaseEndsPanel.setVisible(true);
						break;
					} else {
						changeCreaseEndsPanel.setVisible(false);
					}
				}
			} else {
				changeLinePanel.setVisible(false);
			}
			result = selectedTypes & arrowMask;
			if (result == 0b000000010) {
				changeArrowPanel.setVisible(true);
				sliderPanel.setVisible(true);
			} else {
				changeArrowPanel.setVisible(false);
				sliderPanel.setVisible(false);
			}
			result = selectedTypes & faceMask;
			if (result == 0b000000100) {
				faceDirectionPanel.setVisible(true);
			} else {
				faceDirectionPanel.setVisible(false);
			}
			result = selectedTypes & leaderMask;
			if (result == 0b000001000) {
				//inputSymbolLeaderPanel.setVisible(true);
				changeSymbolLeaderPanel.setVisible(true);
			} else {
				//inputSymbolLeaderPanel.setVisible(false);
				changeSymbolLeaderPanel.setVisible(false);
			}
			result = selectedTypes & picMask;
			if (result == 0b000010000) {
				picSymbolPanel.setVisible(true);
				//sliderPanel.setVisible(true); //TODO: MAYBE OWN SLIDER FOR PIC_SYMBOLS
			} else {
				picSymbolPanel.setVisible(false);
				//sliderPanel.setVisible(false);
			}
			result = selectedTypes & geoMask;
			if (result == 0b000100000) {
				//TODO: editing options for geoSymbols
			}
			result = selectedTypes & equDistMask;
			if (result == 0b001000000) {
				equalDistPanel.setVisible(true);
			} else {
				equalDistPanel.setVisible(false);
			}
			result = selectedTypes & equAnglMask;
			if (result == 0b010000000) {
				sliderEqualAngl.setVisible(true);
				equalAnglPanel.setVisible(true);
			} else {
				sliderEqualAngl.setVisible(false);
				equalAnglPanel.setVisible(false);
			}
			result = selectedTypes & pleatMask;
			if (result == 0b100000000) {
				pleatPanel.setVisible(true);
			} else {
				pleatPanel.setVisible(false);
			}
			
		} else {
			sliderEqualAngl.setVisible(false);
			sliderPanel.setVisible(false);
			changeLinePanel.setVisible(false);
			//changeCreaseEndsPanel.setVisible(false);
			changeArrowPanel.setVisible(false);
			picSymbolPanel.setVisible(false);
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
			} else if (Globals.toolbarMode == Constants.ToolbarMode.INPUT_SYMBOL) {
				Object inputSymbol = symbolInputCB.getSelectedItem();
				
				if (inputSymbol == "Leader") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.LEADER;
				} else if (inputSymbol == "Repetition Box") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.REPETITION_BOX;
				} else if (inputSymbol == "Equal Distances") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.EQUAL_DIST;
				} else if (inputSymbol == "Equal Angles") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.EQUAL_ANGL;
				} else if (inputSymbol == "Rotations") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.ROTATIONS;
				} else if (inputSymbol == "X-Ray Circle") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.X_RAY_CIRCLE;
				} else if (inputSymbol == "Fold over and over") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.FOLD_OVER_AND_OVER;
				} else if (inputSymbol == "Next View Here") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.NEXT_VIEW;
				} else if (inputSymbol == "Hold Here") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.HOLD_HERE;
				} else if (inputSymbol == "Hold Here and Pull") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.HOLD_HERE_AND_PULL;
				} else if (inputSymbol == "Crimping & Pleating") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.CRIMPING_PLEATING;
				} else if (inputSymbol == "Sinks") {
					Globals.inputSymbolMode = Constants.InputSymbolMode.SINKS;
				}
			}
			modeChanged();

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
