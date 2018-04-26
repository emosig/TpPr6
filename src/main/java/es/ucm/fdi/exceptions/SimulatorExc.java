package es.ucm.fdi.exceptions;

/*
 * Excepción para acceso no autorizado al simulador
 */
public class SimulatorExc extends Exception {
	public SimulatorExc() {
		super("Acceso prematuro al simulador");
	}
}
