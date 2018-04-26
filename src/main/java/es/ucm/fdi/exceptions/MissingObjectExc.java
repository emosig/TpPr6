package es.ucm.fdi.exceptions;

import es.ucm.fdi.events.Event;

/*
 * Excepción para la ausencia de un cruce o una carretera
 */
public class MissingObjectExc extends Exception{
	public MissingObjectExc(String message) {
		super(message);
		System.out.println(message);
	}
	
	public MissingObjectExc(Event e) {
		super(e.getName().replaceAll("new_", ""));
		StringBuilder sb = new StringBuilder();
		sb.append("Missing object ").append(e.getName());
		System.out.println(sb.toString());
	}
}
