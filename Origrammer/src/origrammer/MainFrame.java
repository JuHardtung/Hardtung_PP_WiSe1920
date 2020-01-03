package origrammer;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import origrammer.geometry.OriLine;

public class MainFrame extends JFrame implements ActionListener, ComponentListener, WindowListener {
	MainScreen mainScreen;
	public UITopPanel uiTopPanel;
	public UISidePanel uiSidePanel;
	public UIBottomPanel uiBottomPanel;
	
	private JMenu menuFile = new JMenu("File");
	private JMenuItem menuItemOpen = new JMenuItem("Open File");
	private JMenuItem menuItemSave = new JMenuItem("Save File");
	private JMenuItem menuItemSaveAs = new JMenuItem("Save File as");
	
	private JMenu menuEdit = new JMenu("Edit");
	private JMenuItem menuItemUndo = new JMenuItem("Undo");
	private JMenuItem menuItemRedo = new JMenuItem("Redo");
	private JMenuItem menuItemCut = new JMenuItem("Cut");
	private JMenuItem menuItemCopy = new JMenuItem("Copy");
	private JMenuItem menuItemPaste = new JMenuItem("Paste");
	private JMenuItem menuItemDeleteSelected = new JMenuItem("Delete selection");
	private JMenuItem menuItemPreferences = new JMenuItem("Preferences");
	
	private JMenu menuObject = new JMenu("Object");
	private JMenu menuType = new JMenu("Type");
	
	private JMenu menuSelect = new JMenu("Select");
	private JMenuItem menuItemSelectAll = new JMenuItem("Select All");
	private JMenuItem menuItemUnselectAll = new JMenuItem("Unselect All");
	
	private JMenu menuHelp = new JMenu("Help");
	private JMenu menuAbout = new JMenu("About");
	
	private XMLEncoderDecoder xmlEncoderDecoder = new XMLEncoderDecoder();
	private String lastPath = "";
	public ArrayList<String> mruFiles = new ArrayList<>(); //MostRecentlyUsedFiles
	private JMenuItem[] mruFilesMenuItem = new JMenuItem[Config.MRUFILE_NUM];

	
	MainFrame(){
		mainScreen = new MainScreen();
		addWindowListener(this);
		
		uiTopPanel = new UITopPanel(mainScreen);
		uiSidePanel = new UISidePanel(mainScreen, uiTopPanel);
		uiBottomPanel = new UIBottomPanel(mainScreen);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(uiTopPanel, BorderLayout.PAGE_START);
		getContentPane().add(uiSidePanel, BorderLayout.LINE_START);
		getContentPane().add(mainScreen, BorderLayout.CENTER);
		getContentPane().add(uiBottomPanel, BorderLayout.PAGE_END);

	
		
		pack();
		setVisible(true);
        setTitle("Origrammer Alpha v0.01");
        //this.setIconImage(new ImageIcon(getClass().getResource("/images/origrammer.gif")).getImage());
        //setSize(1010, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initMainScreen();
        
        menuItemOpen.addActionListener(this);  
        menuItemSave.addActionListener(this);
        menuItemSaveAs.addActionListener(this);
        
        menuItemUndo.addActionListener(this);
        menuItemRedo.addActionListener(this);
        menuItemCut.addActionListener(this);
        menuItemCopy.addActionListener(this);
        menuItemPaste.addActionListener(this);
        menuItemDeleteSelected.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Origrammer.diagram.steps.get(Globals.currentStep).deleteSelectedLines();
				Origrammer.diagram.steps.get(Globals.currentStep).deleteSelectedArrows();
				Origrammer.diagram.steps.get(Globals.currentStep).deleteSelectedFaces();
				mainScreen.repaint();
			}
		});
        menuItemDeleteSelected.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        menuItemPreferences.addActionListener(this);
        
        menuItemSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Origrammer.diagram.steps.get(Globals.currentStep).selectAll();
				Globals.toolbarMode = Constants.ToolbarMode.SELECTION_TOOL;
				uiSidePanel.selectionToolRB.setSelected(true);
				uiSidePanel.modeChanged();
				mainScreen.repaint();
			}
        	
        });
        menuItemSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK));
        
        menuItemUnselectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Origrammer.diagram.steps.get(Globals.currentStep).unselectAll();
				Globals.toolbarMode = Constants.ToolbarMode.SELECTION_TOOL;
				uiSidePanel.selectionToolRB.setSelected(true);
				uiSidePanel.modeChanged();
				mainScreen.repaint();
			}
		});
        menuItemUnselectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK | KeyEvent.SHIFT_MASK));
        
        
        
        //##### MENU BAR #####
        JMenuBar menuBar = new JMenuBar();
        buildMenuFile();
        buildMenuEdit();
        buildMenuSelect();
        
        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuObject);
        menuBar.add(menuType);
        menuBar.add(menuSelect);
        menuBar.add(menuAbout);
        setJMenuBar(menuBar);       
	}
	
	private void buildMenuFile() {
		menuFile.removeAll();
		
		menuFile.add(menuItemOpen);
		menuFile.addSeparator();
		menuFile.add(menuItemSave);
		menuFile.add(menuItemSaveAs);
		
		
//        for (int i=0; i<Config.MRUFILE_NUM; i++) {
//        	int index = mruFiles.size() - 1 - i;
//        	if (index >= 0) {
//        		String path = mruFiles.get(index);
//        		mruFilesMenuItem[i].setText(path);
//        		menuFile.add(mruFilesMenuItem[i]);
//        	} else {
//        		mruFilesMenuItem[i].setText("");
//        	}
//        }
	}
	
	private void buildMenuEdit() {
		menuEdit.removeAll();
		
		menuEdit.add(menuItemUndo);
		menuEdit.add(menuItemRedo);
		menuEdit.addSeparator();
		menuEdit.add(menuItemCut);
		menuEdit.add(menuItemCopy);
		menuEdit.add(menuItemPaste);
		menuEdit.add(menuItemDeleteSelected);
		menuEdit.addSeparator();
		menuEdit.add(menuItemPreferences);
	}
	
	private void buildMenuSelect() {
		menuSelect.removeAll();
		
		menuSelect.add(menuItemSelectAll);
		menuSelect.add(menuItemUnselectAll);
	}

	private void initMainScreen() {

		add(mainScreen);
	}
	
	public void updateMenu(String filePath) {
		if (mruFiles.contains(filePath)) {
			return;
		}
		
		mruFiles.add(filePath);
		buildMenuFile();
	}
	
	private void openFile(String filePath) {
		Origrammer.mainFrame.uiSidePanel.dispGridCheckBox.setSelected(false);
		Origrammer.mainFrame.mainScreen.setDispGrid(false);

		//if (filePath.endsWith(".xml")) {

			DiagramDataSet diaDataSet = xmlEncoderDecoder.deserializeFromXML();


			if (diaDataSet == null) {
				return;
			}
			
			Diagram diagram = new Diagram();
			diaDataSet.recover(diagram);
			Origrammer.diagram = diagram;
		//}
	}
	
	private void saveFile() {
		JFileChooser fileChooser = new JFileChooser(lastPath);
		fileChooser.addChoosableFileFilter(new FileFilterEx(new String[]{".xml"},
				"(*.xml) " + Origrammer.res.getString("Origrammer_File")));
		if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(this)) {
			try {
				String filePath = fileChooser.getSelectedFile().getPath();
				if (!filePath.endsWith(".xml")) {
					filePath += ".xml";
				}
				File file = new File(filePath);
				if (file.exists()) {
					if (JOptionPane.showConfirmDialog(
							null, Origrammer.res.getString("Warning_SameNameFileExists"),
							Origrammer.res.getString("DialogTitle_FileSave"),
							JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
						return;
					}
				}
				saveFile(filePath);
				updateMenu(filePath);
				lastPath = filePath;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this,  e.toString(), Origrammer.res.getString("Error_FileSaveFailed"),
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void saveFile(String filePath) {
		XMLEncoderDecoder exporter = new XMLEncoderDecoder();
		DiagramDataSet diagramData = new DiagramDataSet(Origrammer.diagram);
		exporter.serializeToXML(diagramData, filePath);
		Origrammer.diagram.dataFilePath = filePath;
	}
	
	
	public void updateTitleText() {
		String fileName;
		if ((Origrammer.diagram.dataFilePath).equals("")) {
			fileName = Origrammer.res.getString("DefaultFileName");
		} else {
			File file = new File(Origrammer.diagram.dataFilePath);
			fileName = file.getName();
		}
		setTitle(fileName + " - " + Origrammer.TITLE);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//check the last opened files
		for (int i=0; i<Config.MRUFILE_NUM; i++) {
			if (e.getSource() == mruFilesMenuItem[i]) {
				try {
					String filePath = mruFilesMenuItem[i].getText();
					openFile(filePath);
					updateMenu(filePath);
					updateTitleText();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(
							this, e.toString(), Origrammer.res.getString("Error_FileLoadFailed"),
									JOptionPane.ERROR_MESSAGE);
				}
				mainScreen.repaint();
				return;
			}

		}
		
		if (e.getSource() == menuItemSave) {
			System.out.println("SAVING");
			saveFile();	
		} else if (e.getSource() == menuItemOpen) {
			System.out.println("OPENING");
			openFile("dsfsd");
		} else if (e.getSource() == menuItemPreferences) {
			PreferenceDialog pd = new PreferenceDialog(this, mainScreen);
			Rectangle rec = getBounds();
            pd.setLocation((int) (rec.getCenterX() - pd.getWidth() / 2),
                    (int) (rec.getCenterY() - pd.getHeight() / 2));
			//pd.setModal(true);
			pd.setVisible(true);
			
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
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
	
	
	
	class FileFilterEx extends FileFilter {
		private String extensions[];
		private String msg;
		
		public FileFilterEx(String[] extensions, String msg) {
			this.extensions = extensions;
			this.msg = msg;
		}

		@Override
		public boolean accept(File f) {
			for (int i=0; i < extensions.length; i++) {
				if (f.isDirectory()) {
					return true;
				}
				if (f.getName().endsWith(extensions[i])) {
					return true;
				}
			}
			return false;
		}

		@Override
		public String getDescription() {
			return msg;
		}
	}
}
