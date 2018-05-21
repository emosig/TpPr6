package es.ucm.fdi.control;

/*
 * Clase genérica que uso para ejecutar las "vueltas" del 
 * simulador de una en una
 */
public class Stepper {
	private Runnable before;
	private Runnable during;
	private Runnable after;
	private boolean forceStop;
	
	public Stepper(Runnable before, Runnable during, Runnable after) {
		this.before = before;
		this.during = during;
		this.after = after;
		forceStop = false;
	}
	
	public void run(int delay, int steps) {
		new Thread(()->{
			before.run();
			int currentSteps = 0;
			try {
				while(!forceStop && currentSteps < steps) {
					during.run();
					Thread.sleep(delay);
					++currentSteps;					
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				after.run();
			}
		}).start();
	}
	
	public void stop() {
		forceStop = true;
	}
}
