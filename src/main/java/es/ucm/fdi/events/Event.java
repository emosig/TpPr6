package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;
import es.ucm.sim.Simulator;

public abstract class Event {
	protected int time;
	protected final String name;
	protected final String id;
	
	public Event(int time, String name, String id) {
		this.time = time;
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getTime() {
		return time;
	}
	
	public abstract void ejecuta(Simulator s);
}
