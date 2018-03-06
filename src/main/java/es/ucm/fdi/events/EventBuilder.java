package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;

public interface EventBuilder {
	public Event parse(IniSection s);
	public boolean isvalid(String id);
}
