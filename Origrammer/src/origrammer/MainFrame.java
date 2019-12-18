package origrammer;
import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import origrammer.geometry.OriLine;

public class MainFrame extends JFrame implements ActionListener, ComponentListener, WindowListener {
	MainScreen mainScreen;
	public UITopPanel uiTopPanel;
	public UISidePanel uiSidePanel;
	
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
	
	MainFrame(){
		mainScreen = new MainScreen();
		addWindowListener(this);
		
		uiTopPanel = new UITopPanel(mainScreen);
		uiSidePanel = new UISidePanel(mainScreen, uiTopPanel);
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(uiTopPanel, BorderLayout.NORTH);
		getContentPane().add(uiSidePanel, BorderLayout.WEST);
		getContentPane().add(mainScreen, BorderLayout.CENTER);
		
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
				Origrammer.diagram.deleteSelectedLines();
				Origrammer.diagram.deleteSelectedArrows();
				mainScreen.repaint();
			}
		});
        menuItemDeleteSelected.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        menuItemPreferences.addActionListener(this);
        
        menuItemSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Origrammer.diagram.selectAll();
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
				Origrammer.diagram.unselectAll();
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

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	/*TODO: Top row buttons 
			(File: New, open, save, save as, export***, close)
			(Edit: Select all, unselect all, undo, redo)
	 */
}
