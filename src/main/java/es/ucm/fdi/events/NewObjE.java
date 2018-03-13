package es.ucm.fdi.events;

import java.util.ArrayList;

import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.fdi.ini.IniSection;
import es.ucm.sim.Simulator;
import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;
import es.ucm.sim.obj.SimObj;
import es.ucm.sim.obj.Vehicle;

public abstract class NewObjE extends Event{
	protected String id;
	
	public NewObjE(int time, String name, String id) {
		super(time, name, id);
	}
	public abstract void ejecuta(Simulator s, ArrayList<Junction> js, ArrayList<Road> rs, ArrayList<Vehicle> vs) throws MissingObjectExc;		
}
