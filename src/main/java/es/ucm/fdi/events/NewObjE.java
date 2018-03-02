package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;

public abstract class NewObjE extends Event{
	protected String id;
	
	public NewObjE(int time, String name) {
		super(time, name);
	}

	public abstract Event read(IniSection is);
	
	public abstract void ejecuta();		
}
