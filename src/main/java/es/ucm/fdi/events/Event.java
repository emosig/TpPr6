package es.ucm.fdi.events;

import java.util.ArrayList;
import java.util.Map;

import es.ucm.fdi.exceptions.IdException;
import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.fdi.util.DescribableEntity;
import es.ucm.sim.Simulator;
import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;
import es.ucm.sim.obj.Vehicle;

/*
 * Clase abstracta con métodos y atributos comunes a todos los eventos
 */

public abstract class Event extends DescribableEntity{
	protected int time;
	protected final String name;
	/*
	 *se pone a true al ejecutar y garantiza la no repetición de eventos
	 */
	protected boolean done;
	
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
	 * Ejecución del evento
	 * 
	 * @param js  - lista de cruces que hay que añadir
	 * @param rs  - lista de cruces que hay que añadir
	 * @param vs  - lista de cruces que hay que añadir
	 */
	public abstract void ejecuta(Simulator s, ArrayList<Junction> js,
			ArrayList<Road> rs, ArrayList<Vehicle> vs) 
					throws MissingObjectExc, IdException;
	/*
	 * Continua el método describe() para cada Describable Entity en concreto
	 */
	protected abstract void describeFurther(Map<String, String> out);
}
