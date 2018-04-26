package es.ucm.fdi.events;

import java.util.ArrayList;

import es.ucm.fdi.exceptions.IdException;
import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.sim.Simulator;
import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;
import es.ucm.sim.obj.Vehicle;

/*
 * Clase abstracta que agrupa los eventos de creación de objetos
 */
public abstract class NewObjE extends Event{
	protected String id;
	
	public NewObjE(int time, String name, String id) {
		super(time, name);
		this.id = id;
	}
	
	public abstract void ejecuta(Simulator s, ArrayList<Junction> js, 
			ArrayList<Road> rs, ArrayList<Vehicle> vs) 
					throws MissingObjectExc, IdException;		
}
