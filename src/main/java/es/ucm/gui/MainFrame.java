package es.ucm.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.OutputStream;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import es.ucm.fdi.extra.graphlayout.Graph;
import es.ucm.fdi.launcher.Controller;
import es.ucm.model.SimulatorListener;
import es.ucm.sim.Simulator;
import es.ucm.sim.Simulator.UpdateEvent;

public class MainFrame extends JFrame implements SimulatorListener{
	
	private Controller ctrl;
	private OutputStream reportsOutputStream;
	
	private JTextArea evEditor; // editor de eventos 
	private JTable evQueue; // cola de eventos 
	private JPanel reportsArea; // zona de informes 
	private JTable vehiclesTable; // tabla de vehiculos 
	private JTable roadsTable; // tabla de carreteras 
	private JTable junctionsTable; // tabla de cruces 
	private Graph map;
	private JPanel mapPane;
	
	private JSplitPane tableSplit1;
	private JSplitPane tableSplit2;
	private JSplitPane bottomSplit;
	private JSplitPane upperSplit1;
	private JSplitPane upperSplit2;
	private JSplitPane mainSplit;
	
	private File currentFile;
	private JMenu fileMenu; 
	private JMenu simMenu; 
	private JMenu reportsMenu; 
	//private JToolBar bar;
	private JFileChooser chooser; 
	private File file;
	
	public MainFrame(Controller ctrl, Simulator sim, String inFileName) {
		super("Traffic Simulator");
		this.ctrl = ctrl; 
		currentFile = inFileName != null ? new File(inFileName) : null;
		/*
		reportsOutputStream = new JTextAreaOutputStream(reportsArea,null);
		ctrl.setOutputStream(reportsOutputStream); // ver sección 8 
		*/
		sim.addSimulatorListener(this);
		initGUI();
	}
	
	private void border(String title, JComponent comp) {
		//para uso interno de cada componente visible
		TitledBorder border = new TitledBorder(title);
	    border.setTitleJustification(TitledBorder.LEFT);
	    border.setTitlePosition(TitledBorder.TOP);
	    comp.setBorder(border);
	}
	
	public void initEvEditor() {
		border("Events Editor", evEditor);
	}
	
	public void initEvQueue() {
		border("Events Queue", evQueue);
		
        /*Nada funciona.
         * 
         * JTable evQueueTable = new JTable(new DefaultTableModel());
		add(evQueueTable);
		JScrollPane sc = new JScrollPane(evQueue);
		sc.setVisible(true);
		add(sc);*/
	}
	
	public void initReportsArea() {
		border("Reports Area", reportsArea);
		reportsArea.setLayout(new BorderLayout());
		JTextArea ta = new JTextArea();
		ta.setEditable(false);
		ta.append("test");
		reportsArea.add(ta);
	}
	
	public void initVTable() {
		border("Vehicles", vehiclesTable);
	}
	
	public void initRTable() {
		border("Roads", roadsTable);
	} 
	
	public void initJTable() {
		border("Junctions", junctionsTable);
	}
	
	public void initMap() {
		
	}
	
	public void initMenu() {
		//Creación menú
		JMenuBar menu = new JMenuBar();
		menu.add(fileMenu = new JMenu("File"));
		menu.add(simMenu = new JMenu("Simulator"));
		menu.add(reportsMenu = new JMenu("Reports"));
		
		JMenuItem fileItem1 = new JMenuItem("Load Events");
		fileMenu.add(fileItem1);
		JMenuItem fileItem2 = new JMenuItem("Save Events");
		fileMenu.add(fileItem2);
		fileMenu.addSeparator();
		JMenuItem fileItem3 = new JMenuItem("Save Report");
		fileMenu.add(fileItem3);
		fileMenu.addSeparator();
		JMenuItem fileItem4 = new JMenuItem("Exit");
		fileMenu.add(fileItem4);
		JCheckBoxMenuItem simItem1 = new JCheckBoxMenuItem("Run");
		simMenu.add(simItem1);
		JCheckBoxMenuItem simItem2 = new JCheckBoxMenuItem("Reset");
		simMenu.add(simItem2);
		JCheckBoxMenuItem simItem3 = new JCheckBoxMenuItem("Redirect Output");
		simMenu.add(simItem3);
		JMenuItem reportsItem1 = new JMenuItem("Generate");
		reportsMenu.add(reportsItem1);
		JMenuItem reportsItem2 = new JMenuItem("Clear");
		reportsMenu.add(reportsItem2);
		
		this.getRootPane().setJMenuBar(menu);
	}
	
	public void initGUI() {
	
		
		//Agrupo los componentes visibles
		JComponent visibleComp[] = {
				vehiclesTable = new JTable(), roadsTable = new JTable(), 
				junctionsTable = new JTable(), evQueue = new JTable(), 
				evEditor = new JTextArea(), reportsArea = new JPanel(), mapPane = new JPanel()
		};
		
		//Fijo una dimensión mínima para todos los elementos
		Dimension minimumSize = new Dimension(100, 60);
		for(JComponent c: visibleComp)
			c.setMinimumSize(minimumSize);
		
		tableSplit1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, vehiclesTable, roadsTable);
		upperSplit1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, evEditor, evQueue);
		tableSplit2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableSplit1, junctionsTable);
		upperSplit2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, upperSplit1, reportsArea);
		bottomSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableSplit2, mapPane);
		mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperSplit2, bottomSplit);
		
		//Un guarrada tremenda, pero no encuentro una forma más limpia de
		//dividir al inicio los paneles de manera coherente
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainSplit.setDividerLocation(0.4);
				bottomSplit.setDividerLocation(0.6);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						upperSplit2.setDividerLocation(0.7);
						tableSplit2.setDividerLocation(0.7);
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								upperSplit1.setDividerLocation(0.5);
								tableSplit1.setDividerLocation(0.5);
							}
						});
					}
				});
			}
		});
		
		
		/*
		bottomSplit.setBackground(Color.blue);
		upperSplit2.setBackground(Color.red);
		upperSplit1.setBackground(Color.green);
		tableSplit2.setBackground(Color.black);
		tableSplit1.setBackground(Color.magenta);
		mainSplit.setBackground(Color.yellow);
		
		this.add(chooser = new JFileChooser());
		this.add(bar = new JToolBar());*/
		
		
		
		initEvEditor();
		initEvQueue();
		initReportsArea();
		initVTable();
		initRTable();
		initJTable();
		initMap();
		initMenu();
		
		this.setLayout(new BorderLayout());
		this.setContentPane(mainSplit);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(720, 480);
		
		this.setVisible(true);
	}

	@Override
	public void registered(UpdateEvent ue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset(UpdateEvent ue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newEvent(UpdateEvent ue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void advanced(UpdateEvent ue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(UpdateEvent ue, String error) {
		// TODO Auto-generated method stub
		
	}
}
