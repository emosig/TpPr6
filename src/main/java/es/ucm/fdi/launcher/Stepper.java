package es.ucm.fdi.launcher;

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
					
					//debug
					System.out.println(currentSteps);
					
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
