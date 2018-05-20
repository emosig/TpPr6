package es.ucm.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.control.SimulatorAction;
import es.ucm.fdi.events.Event;
import es.ucm.fdi.exceptions.NegativeArgExc;
import es.ucm.fdi.exceptions.SimulatorExc;
import es.ucm.fdi.util.MyTable;
import es.ucm.fdi.util.MyTextEditor;
import es.ucm.model.SimulatorListener;
import es.ucm.model.sim.Simulator.UpdateEvent;

/*
 * Clase principal de la vista del simulador. La llamada al controlador
 * se hace desde aquí si se ejecuta el modo GUI
 */
public class MainFrame extends JFrame implements SimulatorListener{
	
	private Controller ctrl;

	private MyTextEditor evEditor;
	private JScrollPane reportsArea; 
	private RoadMapGraph map;
	private MyTable evQueueTable;
	private MyTable vehiclesTable;
	private MyTable roadsTable;
	private MyTable junctionsTable;
	private JTextArea reportTa;
	private JLabel status;
	private JTextField timer;
	private JSpinner delay;
	private JSpinner steps;
	private JPanel lowerBar;
	
	private JSplitPane tableSplit1;
	private JSplitPane tableSplit2;
	private JSplitPane bottomSplit;
	private JSplitPane upperSplit1;
	private JSplitPane upperSplit2;
	private JSplitPane mainSplit;

	
	private static final Dimension MINSIZE = new Dimension(100, 60);
	
	public MainFrame(Controller ctrl, String inFileName) 
			throws SimulatorExc, NegativeArgExc, IOException {
		super("Traffic Simulator");
		this.ctrl = ctrl;
		//para cuando no se propociona archivo no arranca el roadmap
		ctrl.run(1, ctrl.isFile());
		ctrl.getSim().addSimulatorListener(this);
		initGUI();
	}
	
	public static void showFriendlyExc(String s) {
		JOptionPane.showMessageDialog(
				null, s, "Error", JOptionPane.ERROR_MESSAGE);
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
		border("Events Queue", evQueueTable);
	}
	
	private void loadEvQueue(List<Event> e) {
		evQueueTable = new MyTable(e);
		border("Events Queue", evQueueTable);
		if(upperSplit1 != null)upperSplit1.setRightComponent(evQueueTable);
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
	
	private void initVTable() throws SimulatorExc {
		vehiclesTable = new MyTable();
		border("Vehicles", vehiclesTable);
	}
	
	private void loadVTable() throws SimulatorExc {
		vehiclesTable = new MyTable(ctrl.getSim().getRoadMap().getVehicles());
		border("Vehicles", vehiclesTable);
		tableSplit1.setTopComponent(vehiclesTable);
	}
	
	private void initRTable() throws SimulatorExc {
		roadsTable = new MyTable();
		border("Roads", roadsTable);
	} 
	
	private void loadRTable() throws SimulatorExc {
		roadsTable = new MyTable(ctrl.getSim().getRoadMap().getRoads());
		border("Roads", roadsTable);
		tableSplit1.setBottomComponent(roadsTable);
	}
	
	private void initJunTable() throws SimulatorExc {
		junctionsTable = new MyTable();
		border("Junctions", junctionsTable);
	}
	
	private void loadJunTable() throws SimulatorExc {
		junctionsTable = new MyTable(
				ctrl.getSim().getRoadMap().getJunctions());
		border("Junctions", junctionsTable);
		tableSplit2.setBottomComponent(junctionsTable);
	}
	
	/*
	 * Reinicia los componentes (reset)
	 */
	
	private void resetAll() {
		reportTa.setText("");
		evEditor.clear();
		map = new RoadMapGraph();
		bottomSplit.setRightComponent(map);
		setSplitPaneSizes();
		resetTables();
	}
	
	/*
	 * Reinicia las tablas (reset)
	 */
	
	private void resetTables() {
		evQueueTable = new MyTable();
		border("Events Queue", evQueueTable);
		vehiclesTable = new MyTable();
		border("Vehicles", vehiclesTable);
		roadsTable = new MyTable();
		border("Roads", roadsTable);
		junctionsTable = new MyTable();
		border("Junctions", junctionsTable);
		upperSplit1.setRightComponent(evQueueTable);
		tableSplit1.setTopComponent(vehiclesTable);
		tableSplit1.setBottomComponent(roadsTable);
		tableSplit2.setBottomComponent(junctionsTable);
	}
	
	/*
	 * Inicializa el roadmap gráfico
	 */
	private void initMap() throws SimulatorExc {
		map = new RoadMapGraph();
		map.setMaximumSize(new Dimension(50, 50));
	}

	private void loadMap() throws SimulatorExc {
		map = new RoadMapGraph(ctrl.getSim().getRoadMap());
		bottomSplit.setRightComponent(map);
		setSplitPaneSizes();
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
						showFriendlyExc("Archivo no encontrado");
					}
				});
		SimulatorAction load = new SimulatorAction(
				"Load Events", "open.png", "Load events from file",
				KeyEvent.VK_L, "control L", 
				()-> {
					String path;
					try {
						path = evEditor.load();
						if(ctrl.isEmpty()) {
							ctrl.readEvs(path);  
						}
					} catch (SimulatorExc e1) {
						showFriendlyExc(e1.getMessage());
					}catch (IOException e) {
						showFriendlyExc("Error al abrir el fichero");
					}
					
				});
		SimulatorAction saveRe = new SimulatorAction(
				"Save Report", "save_report.png", "Save current report",
				KeyEvent.VK_R, "control R", 
				()-> {
					try {
						saveReport();
					} catch (FileNotFoundException e) {
						showFriendlyExc("Archivo no encontrado");
					}
				});
		SimulatorAction run = new SimulatorAction(
				"Run", "play.png", "Start simulation",
				KeyEvent.VK_Q, "control Q", 
				()-> {
					int howMany = (Integer)steps.getValue();
					int howMuchDelay = (Integer)delay.getValue();
					if (howMany <= 0 || howMany >= 100) {
						showFriendlyExc(
								"Seleccione un número de pasos entre 1 y 99");
					}
					if (howMany <= 0 || howMany >= 10000) {
						showFriendlyExc(
								"Seleccione un valor de retardo entre 1 y 9999");
					}
					if(ctrl.isEmpty())
						showFriendlyExc(
								"No hay eventos cargados");
					else ctrl.keepRunningSteps(howMany, howMuchDelay);
				});
		SimulatorAction reset = new SimulatorAction(
				"Reset", "reset.png", "Reset simulation",
				KeyEvent.VK_W, "control W", 
				()-> ctrl.reset());
		SimulatorAction clearR = new SimulatorAction(
				"Clear", "clear.png", "Clear reports area",
				KeyEvent.VK_F, "control F", 
				()-> reportTa.setText(null));
		SimulatorAction clearE = new SimulatorAction(
				"Clear", "clear.png", "Clear events area",
				KeyEvent.VK_E, "control E", 
				()-> evEditor.clear());
		SimulatorAction stop = new SimulatorAction(
				"Stop", "stop.png", "Stops simulation thread",
				KeyEvent.VK_T, "control T", 
				()-> ctrl.forceStop());

		// inicializo los cuadros de time, steps y delay
		delay = new JSpinner();
		delay.setPreferredSize(new Dimension(50, 5));
		delay.setValue(0);
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
		
		JLabel delayLabel = new JLabel("Delay ");
		JLabel stepsLabel = new JLabel("Steps ");
		JLabel timerLabel = new JLabel("Time: ");
		
		// add actions to toolbar, and bar to window
		JToolBar bar = new JToolBar();
		for(SimulatorAction a: new SimulatorAction[] {
				save, load, clearE, saveRe, run, stop, reset, clearR, exit}) {
			bar.add(a);
		}
		bar.add(delayLabel);
		bar.add(delay);
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
		rep.add(clearR);
		
		JMenuBar menu = new JMenuBar();
		menu.add(file);
		menu.add(sim);
		menu.add(rep);
		setJMenuBar(menu);
		
		//lower bar
		lowerBar = new JPanel();
		add(lowerBar, BorderLayout.SOUTH);
		status = new JLabel("Simulador de tráfico");
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
	
	
	private void loadUpperComponents() throws SimulatorExc, IOException{
		loadEvEditor();
		loadEvQueue(ctrl.getSim().getEventQueue());
		loadReports();
	}
	
	private void loadObjComponents() throws SimulatorExc {
		if(!ctrl.isEmpty()) {
			loadVTable();
			loadRTable();
			loadJunTable();
			loadMap();
		}
	}
	
	private void format() {
		tableSplit1 = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT, vehiclesTable, roadsTable);
		upperSplit1 = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, evEditor, evQueueTable);
		tableSplit2 = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT, tableSplit1, junctionsTable);
		upperSplit2 = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, upperSplit1, reportsArea);
		bottomSplit = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, tableSplit2, map);
		mainSplit = new JSplitPane(
				JSplitPane.VERTICAL_SPLIT, upperSplit2, bottomSplit);
		
		//para redimensionamiento automático
		for (JSplitPane p : new JSplitPane[] {
				tableSplit1, tableSplit2, upperSplit1, upperSplit2, 
				bottomSplit, mainSplit}) {
			p.setResizeWeight(.5);
		}
	}
	
	/*
	 * Inicia y configura todos los componentes visibles
	 */
	private void initGUI() throws SimulatorExc, IOException {
		initEverything();
		if(!ctrl.isEmpty()) {
			loadUpperComponents();
		}
		addBars();
		
		/*for(JComponent c: new JComponent[] {
				evEditor, evQueue, reportsArea, vehicles, roads, junctions}){
			c.setMinimumSize(new Dimension(240, 140));
		}*/
		
		format();

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
		if(!ctrl.isFile())
			JOptionPane.showMessageDialog(
				null, "Simulador 'vacío'\nCargue un archivo para comenzar",
				"Popup amistoso", JOptionPane.INFORMATION_MESSAGE);
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
		SwingUtilities.invokeLater(()->{
			try {
				loadObjComponents();
				status.setText("Simulación iniciada con éxito");
			} catch (SimulatorExc e) {
				showFriendlyExc("Error en el acceso al simulador");
			}
		});
	}

	@Override
	public void reset(UpdateEvent ue) {
		SwingUtilities.invokeLater(()->{
			resetAll();
			timer.setText("0");
			status.setText("Simulación reiniciada");
		});
	}

	@Override
	public void newEvent(UpdateEvent ue) {
		SwingUtilities.invokeLater(()->loadEvQueue(ue.getEventQueue()));	
	}

	@Override
	public void advanced(UpdateEvent ue) {
		SwingUtilities.invokeLater(()->{
			try {
				loadObjComponents();
				loadReports();
				timer.setText(String.valueOf(ctrl.getSim().getSimTime()));
			} catch (SimulatorExc e) {
				showFriendlyExc(e.getMessage());
			} catch (IOException e) {
				showFriendlyExc("Ha ocurrido un error en la lectura del archivo");;
			}
		});
	}

	@Override
	public void error(UpdateEvent ue, String error) {
		showFriendlyExc(error);
	}

}