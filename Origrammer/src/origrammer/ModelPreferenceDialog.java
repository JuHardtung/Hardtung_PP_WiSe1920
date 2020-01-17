package origrammer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.PlainDocument;

public class ModelPreferenceDialog extends JDialog implements ActionListener, ComponentListener {
	
	//MODEL
	JLabel titleLabel = new JLabel("Title:");
	JTextField titleTF = new JTextField();
	
	JLabel authorLabel = new JLabel("Author:");
	JTextField authorTF = new JTextField();
	
	JLabel commentsLabel = new JLabel("Comments:");
	JTextArea commentsTF = new JTextArea();
	
	//PAPER SHAPE
	JRadioButton squareRB = new JRadioButton("Square", true);
	JRadioButton rectangleRB = new JRadioButton("Rectangle", false);
	JRadioButton triangleRB = new JRadioButton("Triangle", false);
	JRadioButton polygonRB = new JRadioButton("Polygon", false);
	ButtonGroup paperShapeBG = new ButtonGroup();
	JCheckBox rotatedCB = new JCheckBox("Rotated");
	
	//PAPER SIZE
	JPanel paperSizePanel = new JPanel();
	JLabel paperSideSizeLabel = new JLabel("Side:");
	JTextField paperSizeTF = new JTextField();

	
	//PAPER SIZE RECTANGLE
	JPanel paperSizeRectPanel = new JPanel();
	JLabel paperWidthLabel = new JLabel("Width:");
	JTextField paperWidthTF = new JTextField();
	JLabel paperHeightLabel = new JLabel("Height:");
	JTextField paperHeightTF = new JTextField();
	
	//PAPER COLOR
	JLabel faceUpLabel = new JLabel("Face Up:");
	JLabel faceDownLabel = new JLabel("Face Down:");
	
	//JLabel faceUpColor = new JLabel();
	JButton faceUpColor = new JButton();
	JButton switchColors = new JButton("Switch");
	//JLabel faceDownColor = new JLabel();
	JButton faceDownColor = new JButton();

	private JButton okButton = new JButton(Origrammer.res.getString("Pref_okButton"));
	private JButton cancelButton = new JButton(Origrammer.res.getString("Pref_cancelButton"));
	
	Color tmpColor = null;
	
	private JPanel jContentPane = null;
	MainScreen __screen;
	
	public ModelPreferenceDialog(JFrame frame, MainScreen __screen) {
		super(frame);
		this.__screen = __screen;
		init();
	}
	
	private void init() {
		this.addComponentListener(this);
		this.setSize(700, 425);
		this.setContentPane(getJContentPane());
		this.setTitle("Model Preferences");
	}
	
	
	private JPanel getJContentPane() {
		
		squareRB.addActionListener(this);
		rectangleRB.addActionListener(this);
		triangleRB.addActionListener(this);
		polygonRB.addActionListener(this);
		
		rotatedCB.addActionListener(this);
		
		switchColors.addActionListener(this);
		faceUpColor.addActionListener(this);
		faceDownColor.addActionListener(this);
		
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		if (jContentPane == null) {
			
			//##### MODEL PANEL #####
			titleTF.setPreferredSize(new Dimension(300, 25));
			titleTF.setMaximumSize(new Dimension(500, 25));
			
			titleLabel.setAlignmentX(LEFT_ALIGNMENT);
			titleTF.setAlignmentX(LEFT_ALIGNMENT);
			
			authorTF.setPreferredSize(new Dimension(300, 25));
			authorTF.setMaximumSize(new Dimension(500, 25));
			
			authorLabel.setAlignmentX(LEFT_ALIGNMENT);
			authorTF.setAlignmentX(LEFT_ALIGNMENT);

			commentsTF.setLineWrap(true);
			commentsLabel.setAlignmentX(LEFT_ALIGNMENT);
			commentsTF.setAlignmentX(LEFT_ALIGNMENT);
			JScrollPane scroll = new JScrollPane(commentsTF);
			scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scroll.setAlignmentX(LEFT_ALIGNMENT);
			scroll.setPreferredSize(new Dimension(300, 200));
			scroll.setMaximumSize(new Dimension(500, 200));
			
			JPanel modelPanel = new JPanel();
			modelPanel.add(titleLabel);
			modelPanel.add(titleTF);
			modelPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			modelPanel.add(authorLabel);
			modelPanel.add(authorTF);
			modelPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			modelPanel.add(commentsLabel);
			modelPanel.add(scroll);
			
			modelPanel.setLayout(new BoxLayout(modelPanel, BoxLayout.PAGE_AXIS));
			modelPanel.setBorder(new TitledBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()), "Model"));
			
			
			//##### Paper Shape #####
			paperShapeBG.add(squareRB);
			paperShapeBG.add(rectangleRB);
			paperShapeBG.add(triangleRB);
			paperShapeBG.add(polygonRB);
		
			JPanel paperShapePanel = new JPanel();
		
			rotatedCB.setAlignmentX(0.9f);

			paperShapePanel.add(squareRB);
			paperShapePanel.add(rectangleRB);
			paperShapePanel.add(triangleRB);
			paperShapePanel.add(polygonRB);
			paperShapePanel.add(rotatedCB);
			
			paperShapePanel.setLayout(new GridLayout(5, 1, 1, 0));
			paperShapePanel.setBorder(new TitledBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()), "Paper Shape"));

			
			//##### Paper Size SQUARE #####

			PlainDocument docPaperSize = (PlainDocument) paperSizeTF.getDocument();
			docPaperSize.setDocumentFilter(new IntFilter());
			paperSizeTF.setHorizontalAlignment(JTextField.RIGHT);
			paperSizeTF.setPreferredSize(new Dimension(150, 25));
			paperSizeTF.setMaximumSize(new Dimension(200, 25));
			JLabel paperSizeUnit = new JLabel("cm");
						
			paperSizePanel.add(paperSideSizeLabel);
			paperSizePanel.add(paperSizeTF);
			paperSizePanel.add(paperSizeUnit);
			paperSizePanel.setBorder(new TitledBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()), "Paper Size"));
			
			
			//##### Paper Size RECTANGLE #####
			PlainDocument docPaperWidth = (PlainDocument) paperWidthTF.getDocument();
			JLabel paperWidthRectUnit = new JLabel("cm");
			docPaperWidth.setDocumentFilter(new IntFilter());
			paperWidthTF.setHorizontalAlignment(JTextField.RIGHT);
			paperWidthTF.setPreferredSize(new Dimension(100, 25));
			//paperWidthTF.setMaximumSize(new Dimension(200, 25));
			JPanel paperWidthRect = new JPanel();
			
			PlainDocument docPaperHeight = (PlainDocument) paperHeightTF.getDocument();
			JLabel paperHeightRectUnit = new JLabel("cm");
			docPaperHeight.setDocumentFilter(new IntFilter());
			paperHeightTF.setHorizontalAlignment(JTextField.RIGHT);
			paperHeightTF.setPreferredSize(new Dimension(100, 25));
			//paperHeightTF.setMaximumSize(new Dimension(200, 25));
			JPanel paperHeightRect = new JPanel();
			
			paperWidthRect.add(paperWidthLabel);
			paperWidthRect.add(paperWidthTF);
			paperWidthRect.add(paperWidthRectUnit);
			paperHeightRect.add(paperHeightLabel);
			paperHeightRect.add(paperHeightTF);
			paperHeightRect.add(paperHeightRectUnit);
			
			paperWidthRect.setAlignmentX(RIGHT_ALIGNMENT);
			paperHeightRect.setAlignmentX(RIGHT_ALIGNMENT);

			paperSizeRectPanel.add(paperWidthRect);
			paperSizeRectPanel.add(paperHeightRect);
			paperSizeRectPanel.setBorder(new TitledBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()), "Paper Size"));
			paperSizeRectPanel.setLayout(new BoxLayout(paperSizeRectPanel, BoxLayout.PAGE_AXIS));

			
			//##### Paper Color #####
			faceUpColor.setPreferredSize(new Dimension(100, 50));
			faceUpColor.setOpaque(true);
			faceUpColor.setBackground(new Color(255, 255, 255, 255));
		
			
			switchColors.setPreferredSize(new Dimension(25, 25));

			faceDownColor.setPreferredSize(new Dimension(100, 50));

			faceDownColor.setBackground(new Color(133, 133, 133, 255));
			faceDownColor.setOpaque(true);
			
			JPanel paperColorPanel = new JPanel();
			paperColorPanel.add(faceUpLabel);
			paperColorPanel.add(Box.createRigidArea(new Dimension(25,25)));
			paperColorPanel.add(faceDownLabel);
			paperColorPanel.add(faceUpColor);
			paperColorPanel.add(switchColors);
			paperColorPanel.add(faceDownColor);

			paperColorPanel.setLayout(new GridLayout(2, 3, 0, 0));
			paperColorPanel.setBorder(new TitledBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()), "Paper Color"));
			
			//##### Instructions #####
			JTextArea instructTF = new JTextArea();
			JPanel instructPanel = new JPanel();
			
			instructTF.setLineWrap(true);
			
			instructTF.setAlignmentX(LEFT_ALIGNMENT);
			JScrollPane scroll2 = new JScrollPane(instructTF);
			scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scroll2.setAlignmentX(LEFT_ALIGNMENT);
			scroll2.setPreferredSize(new Dimension(300, 75));
			scroll2.setMaximumSize(new Dimension(500, 100));
			
			instructPanel.add(scroll2);
			instructPanel.setBorder(new TitledBorder(new EtchedBorder(BevelBorder.RAISED, getBackground().darker(), getBackground().brighter()), "Instructions"));
			
			
			//##### BUTTONS PANEL #####
			SpringLayout buttonLayout = new SpringLayout();
			JPanel buttonPanel = new JPanel();
			okButton.setHorizontalAlignment(SwingConstants.RIGHT);
			cancelButton.setHorizontalAlignment(SwingConstants.RIGHT);

			buttonPanel.add(okButton);
			buttonPanel.add(cancelButton);
			buttonPanel.setLayout(buttonLayout);
			SpringUtilities.makeCompactGrid(buttonPanel, 1, 2, 6, 6, 6, 6);
			
			/***************************
			 *****  Paper Options  *****  
			 ***************************/
			
			JPanel paperOptionsPanel = new JPanel();
			//paperOptionsPanel.add(paperShapePanel);
			paperOptionsPanel.add(paperSizePanel);
			paperOptionsPanel.add(paperSizeRectPanel);
			paperOptionsPanel.add(paperColorPanel);
			paperOptionsPanel.add(buttonPanel);
			//paperOptionsPanel.add(instructPanel);
			paperOptionsPanel.setLayout(new BoxLayout(paperOptionsPanel, BoxLayout.PAGE_AXIS));			

			jContentPane = new JPanel();
			jContentPane.add(modelPanel);
			jContentPane.add(paperOptionsPanel);
			//jContentPane.add(buttonPanel);
			
			SpringLayout layout = new SpringLayout();
			jContentPane.setLayout(layout);
			SpringUtilities.makeCompactGrid(jContentPane, 1, 2, 6, 6, 6, 6);
		}
		
		titleTF.setText(Origrammer.diagram.title);
		authorTF.setText(Origrammer.diagram.author);
		commentsTF.setText(Origrammer.diagram.comments);
		faceUpColor.setBackground(Origrammer.diagram.faceUpColor);
		faceDownColor.setBackground(Origrammer.diagram.faceDownColor);
		
		
		if (Globals.paperShape == Constants.PaperShape.SQUARE) {
			paperSizePanel.setVisible(true);
			paperSizeRectPanel.setVisible(false);
			paperSizeTF.setText(Integer.toString(Origrammer.diagram.recPaperWidth));
		} else if (Globals.paperShape == Constants.PaperShape.RECTANGLE) {
			paperSizePanel.setVisible(false);
			paperSizeRectPanel.setVisible(true);
			paperWidthTF.setText(Integer.toString(Origrammer.diagram.recPaperWidth));
			paperHeightTF.setText(Integer.toString(Origrammer.diagram.recPaperHeight));

		}
		


		return jContentPane;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		 if (e.getSource() == switchColors) {
			tmpColor = faceUpColor.getBackground();
			faceUpColor.setBackground(faceDownColor.getBackground());
			faceDownColor.setBackground(tmpColor);
		} else if (e.getSource() == faceUpColor) {
			Color newColor = JColorChooser.showDialog(null, "Choose a Color", faceUpColor.getBackground());
			if (newColor == null) {
				faceUpColor.setBackground(new Color(255, 255, 255, 255));
			} else {
				faceUpColor.setBackground(newColor);
			}
		} else if (e.getSource() == faceDownColor) {
			Color newColor = JColorChooser.showDialog(null, "Choose a Color", faceDownColor.getBackground());
			if (newColor == null) {
				faceDownColor.setBackground(new Color(255, 255, 255, 255));
			} else {
				faceDownColor.setBackground(newColor);
			}
		}
		
		if (e.getSource() == okButton) {
			updateModelPreferences();
			dispose();

		} else if (e.getSource() == cancelButton) {
			dispose();		
		}
	}
	
	private void updateModelPreferences() {

		Origrammer.diagram.setTitle(titleTF.getText());
		Origrammer.diagram.setAuthor(authorTF.getText());
		Origrammer.diagram.setComments(commentsTF.getText());
		Origrammer.diagram.setPaperWidth(Integer.parseInt(paperSizeTF.getText()));
		Origrammer.diagram.setPaperHeight(Integer.parseInt(paperSizeTF.getText()));

		Origrammer.diagram.setFaceUpColor(faceUpColor.getBackground());
		Origrammer.diagram.setFaceDownColor(faceDownColor.getBackground());
		
		__screen.modeChanged();
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
	}



}
