package es.ucm.fdi.exceptions;

import es.ucm.fdi.events.Event;

public class MissingObjectExc extends Exception{
	public MissingObjectExc(String message) {
		super(message);
		System.out.println(message);
	}
	public MissingObjectExc(Event e) {
		super(e.getName().replaceAll("new_", ""));
		System.out.println("Missing object " + e.getName());
	}
}
