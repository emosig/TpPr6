package es.ucm.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import es.ucm.fdi.exceptions.SimulatorExc;
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
	private JTextArea reportsArea; // zona de informes 
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
	private JToolBar bar;
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
	
	public void initGUI() {
		
		Dimension minimumSize = new Dimension(80, 60);
		
		this.setVisible(true);
		vehiclesTable = new JTable();
		roadsTable = new JTable();
		junctionsTable = new JTable();
		evQueue = new JTable();
		evEditor = new JTextArea();
		reportsArea = new JTextArea();
		mapPane = new JPanel();
		
		
		
		
		
		//ESTA MIERDA NO FUNCIONA
		tableSplit1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, vehiclesTable, roadsTable);
		tableSplit1.setDividerLocation(0.5);
		upperSplit1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, evEditor, evQueue);
		upperSplit1.setDividerLocation(0.5);
		tableSplit2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableSplit1, junctionsTable);
		tableSplit2.setDividerLocation(0.5);
		upperSplit2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, upperSplit1, reportsArea);
		upperSplit2.setDividerLocation(0.5);
		bottomSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableSplit2, mapPane);
		bottomSplit.setDividerLocation(0.75);
		mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upperSplit2, bottomSplit);
		mainSplit.setDividerLocation(0.75);
		
		
		
		vehiclesTable.setMinimumSize(minimumSize);
		roadsTable.setMinimumSize(minimumSize);
		junctionsTable.setMinimumSize(minimumSize);
		evQueue.setMinimumSize(minimumSize);
		evEditor.setMinimumSize(minimumSize);
		reportsArea.setMinimumSize(minimumSize);
		mapPane.setMinimumSize(minimumSize);
		
		
		
		//solo para ver qué coño se ve
		bottomSplit.setBackground(Color.blue);
		upperSplit2.setBackground(Color.red);
		upperSplit1.setBackground(Color.green);
		tableSplit2.setBackground(Color.black);
		tableSplit1.setBackground(Color.magenta);
		mainSplit.setBackground(Color.yellow);
		//hostias

		chooser = new JFileChooser();
		this.setLayout(new BorderLayout());
		this.setContentPane(mainSplit);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(720, 480);
		
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
