package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;

/*
 * 		"La aurora de Nueva York tiene
 * cuatro columnas de cieno
 * y un huracán de negras palomas
 * que chapotean en las aguas podridas"
 * 
 * 		Federico García Lorca
 */

public interface EventBuilder {
	public Event parse(IniSection s); //lee los tags del inisection y devuelve el evento con los parámetros correspondientes
	
	public default boolean isvalid(String id) {
		return id.matches("[a-zA-Z0-9_+]"); //mágico, no tocar
	}
}
