package es.ucm.fdi.events;

import java.util.ArrayList;

import es.ucm.fdi.exceptions.IdException;
import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.sim.Simulator;
import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;
import es.ucm.sim.obj.Vehicle;

/*
 * "Tú eres todo lo que no es la vida; lo que de bueno y de hermoso los sueños dejan y no existe"
 * 
 * 		Fernando Pessoa
 */

public abstract class Event {
	protected int time;
	protected final String name;
	protected boolean done; //se pone a true al ejecutar y garantiza la no repetición de eventos
	
	public Event(int time, String name) {
		this.time = time;
		this.name = name;
		done = false;
	}
	
	public boolean getDone() {
		return done;
	}
	
	public String getName() {
		return name;
	}
	
	public int getTime() {
		return time;
	}
	/*
	 * Los parámetros js, rs, vs son listas de objetos a añadir
	 */
	public abstract void ejecuta(Simulator s, ArrayList<Junction> js, ArrayList<Road> rs, ArrayList<Vehicle> vs) throws MissingObjectExc, IdException;
}
