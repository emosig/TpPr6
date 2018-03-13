package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;

public interface EventBuilder {
	public Event parse(IniSection s);
	public default boolean isvalid(String id) {
		return id.matches("[a-zA-Z0-9_+]"); //mágico, no tocar
	}
}
