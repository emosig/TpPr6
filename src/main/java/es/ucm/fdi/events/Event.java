package es.ucm.fdi.events;

import java.util.ArrayList;
import java.util.Map;

import es.ucm.fdi.exceptions.IdException;
import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.fdi.util.Describable;
import es.ucm.fdi.util.DescribableEntity;
import es.ucm.sim.Simulator;
import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;
import es.ucm.sim.obj.Vehicle;

/*
 * "Tú eres todo lo que no es la vida; lo que de bueno y de hermoso los sueños dejan y no existe"
 * 
 * 		Fernando Pessoa
 */

public abstract class Event extends DescribableEntity{
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
	
	public void describe(Map<String, String> out) {
		//out.put("#", ¿?)
		out.put("Time", Integer.toString(time));
		describeFurther(out);
	}
	
	/*
	 * Los parámetros js, rs, vs son listas de objetos a añadir
	 */
	public abstract void ejecuta(Simulator s, ArrayList<Junction> js, ArrayList<Road> rs, ArrayList<Vehicle> vs) throws MissingObjectExc, IdException;
	protected abstract void describeFurther(Map<String, String> out);
}
