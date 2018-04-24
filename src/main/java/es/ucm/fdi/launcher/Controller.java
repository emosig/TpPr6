package es.ucm.fdi.launcher;

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
 * 		"Fue entonces
 * Y la tierra despertó arrojando tembolorosos ríos de polilla"
 * 
 * 		Federico García Lorca
 */
public class Controller {
	private Simulator sim;
	private InputStream in;
	private OutputStream out;
	private int ticks;
	
	//Constructura para modo batch
	public Controller(InputStream in, OutputStream out, int ticks) {
		this.in = in;
		this.out = out;
		this.ticks = ticks;
	}
	
	//Constructora para modo GUI
	public Controller(InputStream in, int initialTicks) throws NegativeArgExc {
		if(in != null) this.in = in;
		ticks = initialTicks;
		
		//sim = new Simulator(ticks);
		//cargar eventos en la zona de eventos!!
	}
	
	public Simulator getSim() throws SimulatorExc {
		if(sim == null) throw new SimulatorExc();
		else return sim;
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
		for(IniSection is: ini.getSections())
			 for(EventBuilder eb: traducc) {
				 Event evt = eb.parse(is);
				 if(evt != null) 
					 //si no coincide con ningún nombre, no se inserta en ningún sitio y a correr
					 sim.insertaEvento(evt);
			 }
	}
	/*
	 * Inicia el simulador, lee los eventos y los ejecuta
	 */
	public void run() {
		try {
			sim = new Simulator(ticks);
			readEvs();
			sim.ejecuta(ticks, out);
		} catch (NegativeArgExc e) {
		} catch (IOException e) {
			System.out.println("Error en la lectura de eventos");
		}
	}
}
