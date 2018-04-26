package es.ucm.fdi.exceptions;

/*
 * Excepción en la comprobación de una ID
 */
public class IdException extends Exception {
	public IdException(String message, String type) {
		super(message);
		StringBuilder sb = new StringBuilder();
		sb.append("Fallo en el identificador de un ")
			.append(type).append(": ").append(message);
		System.out.println(sb.toString());
	}
}
