package es.ucm.fdi.exceptions;

public class NegativeArgExc extends Exception{
	public NegativeArgExc(String message) {
		super(message);
		System.out.println(message);
	}
}
