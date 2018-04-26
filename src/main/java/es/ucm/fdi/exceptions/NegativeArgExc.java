package es.ucm.fdi.exceptions;

/*
 * Excepción para un valor negativo inesperado
 */
public class NegativeArgExc extends Exception{
	public NegativeArgExc(String message) {
		super(message);
		System.out.println(message);
	}
}
