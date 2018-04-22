package es.ucm.fdi.exceptions;

public class SimulatorExc extends Exception {
	public SimulatorExc() {
		super("Acceso prematuro al simulador");
	}
}
