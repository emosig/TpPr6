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
import es.ucm.model.sim.Simulator;

/*
 * 	Clase principal del modelo, llamada desde el main. Llama al simulador
 * 	en el método run() y lleva la cuenta de pasos de la simulación
 */
public class Controller {
	private Simulator sim;
	private Stepper stepper;
	private InputStream in;
	private OutputStream out;
	private String eventsForDisplay;
	private boolean justResetted;
	//true si hay eventos cargados, falso si no
	private boolean emptySim; 
	//para cuando ejecuto el modo GUI sin pasar un archivo inicial
	private boolean isThereFile;
	
	public static final String OUTFILE = "out.txt";
	
	/*
	 * Constructura para modo batch
	 */
	public Controller(InputStream in, OutputStream out, int ticks) {
		this.in = in;
		this.out = out;
		isThereFile = true;
		justResetted = false;
	}
	
	/*
	 * Constructoras para modo GUI
	 */
	public Controller(InputStream in, int initialTicks) throws  IOException {
		if(in != null) {
			 this.in = in;
		}
		isThereFile = true;
		construct(initialTicks);
	}
	
	public Controller(int initialTicks) throws IOException {
		in = null;
		isThereFile = false;
		construct(initialTicks);
	}
	
	private void construct(int initialTicks) throws IOException {
		justResetted = false;
		emptySim = true;
		File outFile = new File(OUTFILE);
		outFile.createNewFile();
		out = new FileOutputStream(outFile);
	}
	
	/*
	 * Comprueba que se puede acceder a los datos de los objetos de simulación
	 */
	public boolean isEmpty() {
		return !sim.canAccessRM();
	}
	
	/*
	 * Comprueba que hay eventos cargados
	 */
	public boolean isEvsEmpty() {
		return emptySim;
	}
	
	public boolean isFile() {
		return isThereFile;
	}
	
	public Simulator getSim() throws SimulatorExc {
		if(sim == null) {
			throw new SimulatorExc();
		}
		else return sim;
	}
	
	/*
	 * Devuelve representación de los eventos leídos.
	 * Controla que ya se ha llamado a readEvs
	 */
	public String getEventsDisplay() throws SimulatorExc {
		if(!emptySim) {
			return eventsForDisplay;
		}
		else throw new SimulatorExc("No hay eventos cargados");
	}
	
	/*
	 * Parsea los eventos que recibe en formato ini por el input y
	 * y los añade al simulador
	 */
	public void readEvs(Ini ini) throws IOException {
		EventBuilder[] traducc = {
				new NewVehicleE.NewVehicleBuilder(), 
				new NewRoadE.NewRoadBuilder(),
				new NewJunctionE.NewJunctionBuilder(),
				new MakeVehicleFaultyE.MakeVehicleFaultyBuilder()
		}; 
		eventsForDisplay = ini.toString();
		emptySim = false;
		for(IniSection is: ini.getSections()) {
			for(EventBuilder eb: traducc) {
				 Event evt = eb.parse(is);
				 if(evt != null) {
					//si no coincide con ningún nombre, 
					 //no se inserta en ningún sitio y a correr
					 sim.insertaEvento(evt);
				 }	 
			 }
		} 
	}
	
	/*
	 * Parsea los eventos de un archivo alojado en path
	 */
	public void readEvs(String path) throws IOException {
		readEvs(new Ini(path));
	}
	/*
	 * Inicia el simulador, lee los eventos y los ejecuta
	 */
	public void run(int t, boolean limit) throws NegativeArgExc, IOException {
		initSim(t);
	}
	
	private void initSim(int t) throws NegativeArgExc, IOException {
		sim = new Simulator(t);
		if(isThereFile) {
			readEvs(new Ini(in));
		}
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
	public void keepRunningSteps(int t, int delay) throws SimulatorExc {
		keepRunningSteps(t, delay, 
				()->System.out.println(""), ()->System.out.println(""));
	}
	
	public void keepRunningSteps(
			int t, int delay, Runnable before, Runnable after) 
					throws SimulatorExc {
		if(justResetted) {
			justResetted = false;
			throw new SimulatorExc("Cargue un archivo de eventos");
		}
		stepper = new Stepper(
				before,	()->sim.ejecutaSteps(1, out), after);
		stepper.run(delay, t);
	}
	
	/*
	 * Detiene el Stepper
	 */
	public void forceStop() {
		stepper.stop();
	}
	
	/*
	 * Si se llama desde load, pone justResetted en false para que 
	 * pueda ejecutar el simulador
	 */
	public void reset(boolean load) throws NegativeArgExc, IOException {
		justResetted = !load;
		sim.reset();
		if(justResetted) {
			isThereFile = false;
			emptySim = true;
			initSim(1);
		}
	}
}
