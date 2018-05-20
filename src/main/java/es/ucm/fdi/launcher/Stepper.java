package es.ucm.fdi.launcher;

public class Stepper {
	private Runnable before;
	private Runnable during;
	private Runnable after;
	private int delay;
	
	public Stepper(Runnable before, Runnable during, Runnable after) {
		this.before = before;
		this.during = during;
		this.after = after;
	}
	
	public void execute(int delay, int steps) {
		new Thread(()->{
			
		});
		before.run();
		while(condition) {
			during.run();
			Thread.sleep(delay);
		}
		after.run();
	}
}
