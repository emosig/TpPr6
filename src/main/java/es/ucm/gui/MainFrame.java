package es.ucm.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import com.sun.javafx.event.EventQueue;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.events.Event;
import es.ucm.fdi.exceptions.NegativeArgExc;
import es.ucm.fdi.exceptions.SimulatorExc;
import es.ucm.fdi.util.MyTable;
import es.ucm.fdi.util.MyTextEditor;
import es.ucm.model.SimulatorListener;
import es.ucm.sim.Simulator.UpdateEvent;

/*
 * Clase principal de la vista del simulador. La llamada al controlador
 * se hace desde aquí si se ejecuta el modo GUI
 */
public class MainFrame extends JFrame implements SimulatorListener{
	
	private Controller ctrl;
	private OutputStream reportsOutputStream;

	private MyTextEditor evEditor;
	private JScrollPane evQueue; 
	private JScrollPane reportsArea; 
	private JScrollPane vehicles;
	private JScrollPane roads;
	private JScrollPane junctions;
	private RoadMapGraph map;
	
	private MyTable evQueueTable;
	private MyTable vehiclesTable;
	private MyTable roadsTable;
	private MyTable junctionsTable;
	private JTextArea reportTa;
	private JLabel status;
	private JTextField timer;
	private JSpinner steps;
	
	private JSplitPane tableSplit1;
	private JSplitPane tableSplit2;
	private JSplitPane bottomSplit;
	private JSplitPane upperSplit1;
	private JSplitPane upperSplit2;
	private JSplitPane mainSplit;
	
	private File currentFile;

	
	private static final Dimension MINSIZE = new Dimension(100, 60);
	
	public MainFrame(Controller ctrl, String inFileName) 
			throws SimulatorExc, NegativeArgExc, IOException {
		super("Traffic Simulator");
		this.ctrl = ctrl;
		ctrl.run(1, false); //cuidadín
		currentFile = inFileName != null ? new File(inFileName) : null;
		/*
		reportsOutputStream = new JTextAreaOutputStream(reportsArea,null);
		ctrl.setOutputStream(reportsOutputStream); 
		*/
		ctrl.getSim().addSimulatorListener(this);
		initGUI();
		
		//ctrl.keepRunningSteps(1);
	}
	
	/*
	 * MÉTODOS DE USO INTERNO EN initGUI()
	 */
	
	private void saveReport() throws FileNotFoundException {
		JFileChooser fc = new JFileChooser();
		int r = fc.showSaveDialog(null);
		if(r == JFileChooser.APPROVE_OPTION) {
			PrintWriter out = new PrintWriter(fc.getSelectedFile());
			out.print(reportTa.getText());
			out.close();
		}
	}
	
	/*
	 * Configuración de borde
	 */
	private void border(String title, JComponent comp) {
		TitledBorder border = new TitledBorder(title);
	    border.setTitleJustification(TitledBorder.LEFT);
	    border.setTitlePosition(TitledBorder.TOP);
	    comp.setBorder(border);
	}
	
	/*
	 * Configura los componentes scrollables
	 */
	private void scroll(JScrollPane sp) {
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
	
	/*
	 * En lo que sigue, los métodos initX inicializan el componente X vacío
	 * y los métodos loadX cargan la información de estos componentes.
	 */
	
	private void initEvEditor() {
		evEditor = new MyTextEditor();
		border(new StringBuilder("Events: ").append(evEditor.getName())
				.toString(), evEditor);
	}
	
	private void loadEvEditor() throws SimulatorExc {
		evEditor.append(ctrl.getEventsDisplay());
	}
	
	private void initEvQueue(){
		evQueueTable = new MyTable();
		evQueue = new JScrollPane(evQueueTable);
		border("Events Queue", evQueue);
		scroll(evQueue);
	}
	
	private void loadEvQueue(List<Event> e) {
		evQueueTable.initTable(e);
	}
	
	private void initReportsArea() throws IOException {
		JPanel reportsAreaAux = new JPanel();
		border("Reports Area", reportsAreaAux);
		reportsAreaAux.setLayout(new BorderLayout());
		reportTa = new JTextArea();
		reportTa.setEditable(false);
		reportsAreaAux.add(reportTa);
		reportsAreaAux.setMinimumSize(MINSIZE);
		reportsArea = new JScrollPane(reportsAreaAux);
		scroll(reportsArea);
		loadReports();
	}
	
	private void loadReports() throws IOException {
		FileReader reader = new FileReader(ctrl.OUTFILE);
        BufferedReader br = new BufferedReader(reader);
        reportTa.read( br, null );
        br.close();
        reportTa.requestFocus();
	}
	
	/*
	 * He intentado hacer una función para no repetir código
	 * en los procedimientos que init(algo)Table, pero por alguna
	 * razón falla al instanciar JScrollPane en un método aparte
	 */
	
	/*
	 * Inicializa las tablas de objetos
	 */
	private void initVTable() throws SimulatorExc {
		vehiclesTable = new MyTable();
		vehicles = new JScrollPane(vehiclesTable);
		border("Vehicles", vehicles);
		scroll(vehicles);
	}
	
	private void loadVTable() throws SimulatorExc {
		vehiclesTable.initTable(ctrl.getSim().getRoadMap().getVehicles());
	}
	
	private void initRTable() throws SimulatorExc {
		roadsTable = new MyTable();
		roads = new JScrollPane(roadsTable);
		border("Roads", roads);
		scroll(roads);
	} 
	
	private void loadRTable() throws SimulatorExc {
		roadsTable.initTable(ctrl.getSim().getRoadMap().getRoads());
	}
	
	private void initJunTable() throws SimulatorExc {
		junctionsTable = new MyTable();
		junctions = new JScrollPane(junctionsTable);
		border("Junctions", junctions);
		scroll(junctions);
		
	}
	
	private void loadJunTable() throws SimulatorExc {
		junctionsTable.initTable(ctrl.getSim().getRoadMap().getJunctions());
	}
	
	/*
	 * Inicializa el roadmap gráfico
	 */
	private void initMap() throws SimulatorExc {
		map = new RoadMapGraph();
		map.setMaximumSize(new Dimension(50, 50));
	}

	private void loadMap() throws SimulatorExc {
		map.loadRoadMapGraph(ctrl.getSim().getRoadMap());
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
				()-> {
					try {
						evEditor.save();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
		SimulatorAction load = new SimulatorAction(
				"Load Events", "open.png", "Load events from file",
				KeyEvent.VK_L, "control L", 
				()-> {
					String path = evEditor.load();
					if(ctrl.isEmpty()) {
						try {
							ctrl.readEvs(path); 
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		SimulatorAction saveRe = new SimulatorAction(
				"Save Report", "save_report.png", "Save current report",
				KeyEvent.VK_R, "control R", 
				()-> {
					try {
						saveReport();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
		SimulatorAction run = new SimulatorAction(
				"Run", "play.png", "Start simulation",
				KeyEvent.VK_Q, "control Q", 
				()-> {
					int howMany = (Integer)steps.getValue();
					if (howMany <= 0 || howMany >= 100) {
						JOptionPane.showMessageDialog(
								null, "Seleccione un número de pasos entre 1 y 99",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
					else ctrl.keepRunningSteps(howMany);
				});
		SimulatorAction reset = new SimulatorAction(
				"Reset", "reset.png", "Reset simulation",
				KeyEvent.VK_W, "control W", 
				()-> ctrl.reset());
		SimulatorAction generate = new SimulatorAction(
				"Generate", "report.png", "",
				KeyEvent.VK_G, "control G", 
				()-> System.err.println(""));
		SimulatorAction clearR = new SimulatorAction(
				"Clear", "clear.png", "Clear reports area",
				KeyEvent.VK_F, "control F", 
				()-> reportTa.setText(null));
		SimulatorAction clearE = new SimulatorAction(
				"Clear", "clear.png", "Clear events area",
				KeyEvent.VK_E, "control E", 
				()-> evEditor.clear());

		// inicializo los cuadros de time y steps
		steps = new JSpinner();
		steps.setPreferredSize(new Dimension(50, 5));
		try {
			steps.setValue(ctrl.getSim().getSimTime());
		} catch (SimulatorExc e1) {
			System.out.println("Steps por defecto\n");
		}
		timer = new JTextField();	
		timer.setText("0");
		timer.setPreferredSize(new Dimension(50, 5));
		timer.setEnabled(false);
		
		JLabel stepsLabel = new JLabel("Steps ");
		JLabel timerLabel = new JLabel("Time: ");
		
		// add actions to toolbar, and bar to window
		JToolBar bar = new JToolBar();
		for(SimulatorAction a: new SimulatorAction[] {
				save, load, clearE, saveRe, run, reset,
				generate, clearR, exit}) {
			bar.add(a);
		}
		bar.add(stepsLabel);
		bar.add(steps);
		bar.add(timerLabel);
		bar.add(timer);
		add(bar, BorderLayout.NORTH);

		// add actions to menubar, and bar to window
		JMenu file = new JMenu("File");
		file.add(load);		
		file.add(save);	
		file.add(clearE);
		file.addSeparator();
		file.add(saveRe);
		file.addSeparator();
		file.add(exit);
		
		JMenu sim = new JMenu("Simulator");
		sim.add(run);		
		sim.add(reset);		
		
		JMenu rep = new JMenu("Reports");
		rep.add(generate);	
		rep.add(clearR);
		
		JMenuBar menu = new JMenuBar();
		menu.add(file);
		menu.add(sim);
		menu.add(rep);
		setJMenuBar(menu);
		
		//lower bar
		JPanel lowerBar = new JPanel();
		add(lowerBar, BorderLayout.SOUTH);
		status = new JLabel("test"/*, SwingConstants.LEFT*/);
		lowerBar.add(status);

	}
	
	private void initEverything() throws IOException, SimulatorExc{
		initEvEditor();
		initEvQueue();
		initReportsArea();
		initVTable();
		initRTable();
		initJunTable();
		initMap();
	}
	
	/*
	 * Esto lo voy a quitar yo
	 */
	private void loadEverything() throws SimulatorExc, IOException {
		loadEvEditor();
		loadEvQueue(ctrl.getSim().getEventQueue());
		loadReports();
		loadVTable();
		loadRTable();
		loadJunTable();
		loadMap();
	}
	
	/*
	 * Inicia y configura todos los componentes visibles
	 */
	private void initGUI() throws SimulatorExc, IOException {
		initEverything();
		if(!ctrl.isEmpty()) loadEverything();
		addBars();
		
		for(JComponent c: new JComponent[] {
				evEditor, evQueue, reportsArea, vehicles, roads, junctions}){
			c.setMinimumSize(new Dimension(240, 140));
		}
		
	
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

		add(mainSplit, BorderLayout.CENTER);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(720, 480);
		setVisible(true);
		//cada vez que cambie de tamaño llama a lo de los scrollpane
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				setSplitPaneSizes();
			}
		});
	}
	
	private void setSplitPaneSizes() {
		mainSplit.setDividerLocation(0.4);
		bottomSplit.setDividerLocation(0.7);
		upperSplit2.setDividerLocation(0.7);
		tableSplit2.setDividerLocation(0.7);
		upperSplit1.setDividerLocation(0.5);
		tableSplit1.setDividerLocation(0.5);		
	}
	
	/*
	 * MÉTODOS DE SIMULATOR LISTENER
	 */
	
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
		loadEvQueue(ue.getEventQueue());	
	}

	@Override
	public void advanced(UpdateEvent ue) {
		try {
			loadEverything();
		} catch (SimulatorExc e) {
			JOptionPane.showMessageDialog(
					null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int t = Integer.valueOf(timer.getText());
		timer.setText(String.valueOf(t));
	}

	@Override
	public void error(UpdateEvent ue, String error) {
		JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
	}
}