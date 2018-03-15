package es.ucm.sim.obj;

import java.util.Map;

public abstract class SimObj {
	private String id;
	protected abstract void fillReportDetails(Map<String, String> out);
	protected abstract String getReportHeader(); 
	//public abstract void avanza();
	
	public SimObj() {
		id = "";
	}
	
	public SimObj(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void generaInforme(int time, Map<String, String> out) {
		out.put("", getReportHeader());
		out.put ("id", id);
		out.put ("time", String.valueOf(time));
		fillReportDetails(out);
	}
	
	public boolean equals(SimObj that) {
		return id.equals(that.getId());
	}
}
