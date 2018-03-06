package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;
import es.ucm.sim.Simulator;

public abstract class NewObjE extends Event{
	protected String id;
	
	public NewObjE(int time, String name, String id) {
		super(time, name, id);
	}
	
	public abstract void ejecuta(Simulator s);		
}
