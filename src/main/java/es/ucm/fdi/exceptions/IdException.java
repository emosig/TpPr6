package es.ucm.fdi.exceptions;

public class IdException extends Exception {
	public IdException(String message, String type) {
		super(message);
		System.out.println("Fallo en el identificador de un " + type + ": " + message);
	}
}
