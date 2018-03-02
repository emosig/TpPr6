package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;

public abstract class Event {
	protected int time;
	protected final String name;
	
	public Event(int time, String name) {
		this.time = time;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getTime() {
		return time;
	}
	
	public abstract Event read(IniSection is);
	
	public abstract void ejecuta();
}
