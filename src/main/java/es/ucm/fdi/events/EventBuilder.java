package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;

/*
 *	Interfaz para el parseo de eventos
 */

public interface EventBuilder {
	/*
	 * lee los tags del inisection y devuelve el evento 
	 * con los parámetros correspondientes
	 */
	public Event parse(IniSection s);
	
	public default boolean isvalid(String id) {
		return id.matches("[a-zA-Z0-9_+]");
	}
}
