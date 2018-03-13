package es.ucm.sim.obj;

import java.util.Map;

public class Lane extends Road{
	private int lanes;
	private int factorReducc(Vehicle v) { //igual para todos los vehiculos?
		int avs = 0;
		for(Vehicle ve : vehiculos.innerValues()) 
			if(ve.getTAveria() > 0) ++avs;
		if(avs < lanes) return 1;
		else return 2;
	}
	public Lane(int lanes, int vMax, int length, Junction iniJ, Junction finalJ, String id) {
		super(vMax, length, iniJ, finalJ, id);
		this.lanes = lanes;
	}
	public int calcVBase() {
		if(vehiculos.size() > 1) return Integer.min(velocidadMax,velocidadMax*lanes/vehiculos.size() + 1);
		else return velocidadMax;
	}
	protected void fillReportDetails(Map<String,String> out) {
		super.fillReportDetails(out);
		out.put("type", "lanes");
		out.put("lanes", String.valueOf(lanes));
	}
}
