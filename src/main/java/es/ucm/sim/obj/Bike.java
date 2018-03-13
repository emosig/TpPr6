package es.ucm.sim.obj;

import java.util.ArrayList;
import java.util.Map;

public class Bike extends Vehicle{

	public Bike(int vMax, ArrayList<Road> it, String id) {
		super(vMax, it, id);
	}
	
	public void setTiempoAveria(int t) {
		if(2*velActual <= velMaxima) super.setTiempoAveria(t);
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		super.fillReportDetails(out);
		out.put("type", "bike");
	}
}
