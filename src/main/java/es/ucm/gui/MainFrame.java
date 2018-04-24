package es.ucm.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
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
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.exceptions.SimulatorExc;
import es.ucm.fdi.launcher.Controller;
import es.ucm.fdi.util.MyTable;
import es.ucm.model.SimulatorListener;
import es.ucm.sim.Simulator.UpdateEvent;

public class MainFrame extends JFrame {
	
	private Controller ctrl;
	private OutputStream reportsOutputStream;
	
	private JScrollPane evEditor;
	private JScrollPane evQueue; 
	private JScrollPane reportsArea; 
	private JScrollPane vehicles;
	private JScrollPane roads;
	private JScrollPane junctions;
	private RoadMapGraph map;
	
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
	private JFileChooser chooser; 
	private File file;
	
	private static final Dimension MINSIZE = new Dimension(100, 60);
	
	public MainFrame(Controller ctrl, String inFileName) throws SimulatorExc {
		super("Traffic Simulator");
		this.ctrl = ctrl;
		ctrl.run();
		currentFile = inFileName != null ? new File(inFileName) : null;
		/*
		reportsOutputStream = new JTextAreaOutputStream(reportsArea,null);
		ctrl.setOutputStream(reportsOutputStream); 
		*/
		//sim.addSimulatorListener(this);
		initGUI();
	}
	
	//muchos métodos de uso interno en initGUI
	
	private void border(String title, JComponent comp) {
		TitledBorder border = new TitledBorder(title);
	    border.setTitleJustification(TitledBorder.LEFT);
	    border.setTitlePosition(TitledBorder.TOP);
	    comp.setBorder(border);
	}
	
	private void scroll(JScrollPane sp) {
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	private void initEvEditor() {
		JTextArea evEditorAux = new JTextArea();
		border("Events Editor", evEditorAux);
		evEditorAux.setMinimumSize(MINSIZE);
		evEditor = new JScrollPane(evEditorAux);
		scroll(evEditor);
	}
	
	private void initEvQueue() throws SimulatorExc {
		MyTable evQueueTable = new MyTable(ctrl.getSim().getEventQueue());
		evQueue = new JScrollPane(evQueueTable);
		border("Events Queue", evQueue);
		scroll(evQueue);
	}
	
	private void initReportsArea() {
		JPanel reportsAreaAux = new JPanel();
		border("Reports Area", reportsAreaAux);
		reportsAreaAux.setLayout(new BorderLayout());
		JTextArea ta = new JTextArea();
		ta.setEditable(false);
		ta.append("test");///
		reportsAreaAux.add(ta);
		reportsAreaAux.setMinimumSize(MINSIZE);
		reportsArea = new JScrollPane(reportsAreaAux);
		scroll(reportsArea);
		
	}
	
	//He intentado hacer una función para no repetir código
	//en los procedimientos que init(algo)Table, pero por alguna
	//razón falla al instanciar JScrollPane en un método aparte
	
	private void initVTable() throws SimulatorExc {
		MyTable vehiclesTable = new MyTable(ctrl.getSim().getRoadMap().getVehicles());
		vehicles = new JScrollPane(vehiclesTable);
		border("Vehicles", vehicles);
		scroll(vehicles);
	}
	
	private void initRTable() throws SimulatorExc {
		MyTable roadsTable = new MyTable(ctrl.getSim().getRoadMap().getRoads());
		roads = new JScrollPane(roadsTable);
		border("Roads", roads);
		scroll(roads);
	} 
	
	private void initJunTable() throws SimulatorExc {
		MyTable junctionsTable = new MyTable(ctrl.getSim().getRoadMap().getJunctions());
		junctions = new JScrollPane(junctionsTable);
		border("Junctions", junctions);
		scroll(junctions);
		
	}
	
	private void initMap() throws SimulatorExc {
		map = new RoadMapGraph(ctrl.getSim().getRoadMap());
	}
	
	private void initMenu() {
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
	
	/*
	 * Tomado de SimWindow
	 */
	private void addBars() {
		// instantiate actions
		SimulatorAction exit = new SimulatorAction(
				"Exit", "exit.png", "Exit the application",
				KeyEvent.VK_A, "control shift X", 
				()-> System.exit(0));
		SimulatorAction save = new SimulatorAction(
				"Save Events", "save.png", "Save changes",
				KeyEvent.VK_S, "control S", 
				()-> System.err.println("saving..."));
		SimulatorAction load = new SimulatorAction(
				"Load Events", "open.png", "Load events from file",
				KeyEvent.VK_L, "control L", 
				()-> System.err.println("loading..."));
		SimulatorAction saveRe = new SimulatorAction(
				"Save Report", "save_report.png", "Save current report",
				KeyEvent.VK_R, "control R", 
				()-> System.err.println("saving..."));
		SimulatorAction run = new SimulatorAction(
				"Run", "play.png", "Start simulation",
				KeyEvent.VK_Q, "control Q", 
				()-> System.err.println("simulating..."));
		SimulatorAction reset = new SimulatorAction(
				"Reset", "reset.png", "Reset simulation",
				KeyEvent.VK_W, "control W", 
				()-> System.err.println("reseting..."));
		SimulatorAction redirect = new SimulatorAction(
				"Redirect Output", "report.png", "Save current report",
				KeyEvent.VK_D, "control D", 
				()-> System.err.println(""));
		SimulatorAction generate = new SimulatorAction(
				"Generate", "report.png", "Generate report",
				KeyEvent.VK_G, "control G", 
				()-> System.err.println("generating..."));
		SimulatorAction clear = new SimulatorAction(
				"Clear", "clear.png", "Clear reports area",
				KeyEvent.VK_F, "control F", 
				()-> System.err.println("clearing..."));

		// add actions to toolbar, and bar to window
		JToolBar bar = new JToolBar();
		for(SimulatorAction a: new SimulatorAction[] {
				exit, save, load, saveRe, run, reset, redirect,
				generate, clear})
			bar.add(a);
		getContentPane().add(bar, BorderLayout.NORTH);

		// add actions to menubar, and bar to window
		JMenu file = new JMenu("File");
		file.add(load);		
		file.add(save);		
		file.addSeparator();
		file.add(saveRe);
		file.addSeparator();
		file.add(exit);
		
		JMenu sim = new JMenu("Simulator");
		sim.add(run);		
		sim.add(reset);		
		sim.add(redirect);
		
		JMenu rep = new JMenu("Reports");
		rep.add(generate);	
		rep.add(clear);
		
		JMenuBar menu = new JMenuBar();
		menu.add(file);
		menu.add(sim);
		menu.add(rep);
		setJMenuBar(menu);
	}
	
	/*
	 * Inicia y configura todos los componentes visibles
	 */
	private void initGUI() throws SimulatorExc {
		initEvEditor();
		initEvQueue();
		initReportsArea();
		initVTable();
		initRTable();
		initJunTable();
		initMap();
		addBars();
		
		tableSplit1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, vehicles, roads); //implementar splitpane ternarios?
		upperSplit1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, evEditor, evQueue);
		tableSplit2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableSplit1, junctions);
		upperSplit2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, upperSplit1, reportsArea);
		bottomSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableSplit2, map);
		mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperSplit2, bottomSplit);
		//para redimensionamiento automático
		for (JSplitPane p : new JSplitPane[] {
				tableSplit1, tableSplit2, upperSplit1, upperSplit2, bottomSplit, mainSplit}) {
			p.setResizeWeight(.5);
		}
		
		//meter esto en otro método fuera de initgui formatear()?
		SwingUtilities.invokeLater(() -> {
			mainSplit.setDividerLocation(0.4);
			bottomSplit.setDividerLocation(0.7);
			SwingUtilities.invokeLater(() -> {
				upperSplit2.setDividerLocation(0.7);
				tableSplit2.setDividerLocation(0.7);
				SwingUtilities.invokeLater(() -> {
					upperSplit1.setDividerLocation(0.5);
					tableSplit1.setDividerLocation(0.5);
				});
			});
		});
		
		
		/*
		bottomSplit.setBackground(Color.blue);
		upperSplit2.setBackground(Color.red);
		upperSplit1.setBackground(Color.green);
		tableSplit2.setBackground(Color.black);
		tableSplit1.setBackground(Color.magenta);
		mainSplit.setBackground(Color.yellow);
		
		this.add(chooser = new JFileChooser());
		*/
		
		this.setLayout(new BorderLayout());
		this.setContentPane(mainSplit);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		this.setSize(720, 480);
		this.setVisible(true);
	}

	private SimulatorListener listener = new SimulatorListener() {
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
	};
}