package es.ucm.fdi.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import es.ucm.fdi.events.*;
import es.ucm.fdi.exceptions.NegativeArgExc;
import es.ucm.fdi.exceptions.SimulatorExc;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.sim.Simulator;

/*
 * 	Clase principal del modelo, llamada desde el main. Llama al simulador
 * 	en el método run() y lleva la cuenta de pasos de la simulación
 */
public class Controller {
	private Simulator sim;
	private InputStream in;
	private OutputStream out;
	private int ticks;
	private String eventsForDisplay;
	private boolean evsRead;
	//para cuando ejecuto el modo GUI sin pasar un archivo inicial
	private boolean emptySim; 
	
	public static final String OUTFILE = "out.txt";
	
	/*
	 * Constructura para modo batch
	 */
	public Controller(InputStream in, OutputStream out, int ticks) {
		this.in = in;
		this.out = out;
		this.ticks = ticks;
		emptySim = false;
		evsRead = false;
	}
	
	/*
	 * Constructoras para modo GUI
	 */
	public Controller(InputStream in, int initialTicks) throws  IOException {
		if(in != null) this.in = in;
		emptySim = false;
		construct(initialTicks);
	}
	
	public Controller(int initialTicks) throws IOException {
		in = null;
		emptySim = true;
		construct(initialTicks);
	}
	
	private void construct(int initialTicks) throws IOException {
		ticks = initialTicks;
		File outFile = new File(OUTFILE);
		outFile.createNewFile();
		out = new FileOutputStream(outFile);
		evsRead = false;
	}
	
	public boolean isEmpty() {
		return emptySim;
	}
	
	public Simulator getSim() throws SimulatorExc {
		if(sim == null) throw new SimulatorExc();
		else return sim;
	}
	
	/*
	 * Devuelve representación de los eventos leídos.
	 * Controla que ya se ha llamado a readEvs
	 */
	public String getEventsDisplay() throws SimulatorExc {
		if(evsRead)	return eventsForDisplay;
		else throw new SimulatorExc();
	}
	
	/*
	 * Parsea los eventos que recibe en formato ini por el input y
	 * y los añade al simulador
	 */
	public void readEvs() throws IOException {
		EventBuilder[] traducc = {
				new NewVehicleE.NewVehicleBuilder(), 
				new NewRoadE.NewRoadBuilder(),
				new NewJunctionE.NewJunctionBuilder(),
				new MakeVehicleFaultyE.MakeVehicleFaultyBuilder()
		}; 
		Ini ini = new Ini(in);
		eventsForDisplay = ini.toString();
		evsRead = true;
		for(IniSection is: ini.getSections())
			 for(EventBuilder eb: traducc) {
				 Event evt = eb.parse(is);
				 if(evt != null) 
					 //si no coincide con ningún nombre, 
					 //no se inserta en ningún sitio y a correr
					 sim.insertaEvento(evt);
			 }
	}
	/*
	 * Inicia el simulador, lee los eventos y los ejecuta
	 */
	public void run(int t, boolean limit) {
		try {
			initSim(t);
			if(limit) keepRunning(t);
			else keepRunningSteps(t);
		} catch (NegativeArgExc e) {
		} catch (IOException e) {
			System.out.println("Error en la lectura de eventos");
		}
	}
	
	private void initSim(int t) throws NegativeArgExc, IOException {
		sim = new Simulator(t);
		if(!emptySim) readEvs();
	}
	
	/*
	 * Ejecuta hasta llegar al límite establecido
	 */
	public void keepRunning(int t) {
		sim.ejecuta(t, out);
	}
	
	/*
	 * Ejecuta un número de pasos
	 */
	public void keepRunningSteps(int t) {
		sim.ejecutaSteps(t, out);
	}
}
