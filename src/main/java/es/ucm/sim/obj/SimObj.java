package es.ucm.sim.obj;

public abstract class SimObj {
	private String id;
	
	public SimObj() {
		id = "";
	}
	
	public SimObj(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public abstract void avanza();
	public abstract String generaInforme();
	
	public boolean equals(SimObj that) {
		return id.equals(that.getId());
	}
	//public algo hashcode???
}
