package es.ucm.model.sim.obj;

import java.util.Map;

/*
 * 	Camino de tierra
 */
public class Dirt extends Road{
	private int factorReducc(Vehicle v) {
		int avs = 0;
		for(Vehicle ve : vehiculos.innerValues()) {
			if(ve.getTAveria() > 0) {
				++avs;
			}
		}
		return avs + 1;
	}
	public Dirt(int vMax, int length, Junction iniJ, Junction finalJ, 
			String id) {
		super(vMax, length, iniJ, finalJ, id);
	}
	public int calcVBase() {
		return velocidadMax;
	}
	protected void fillReportDetails(Map<String,String> out) {
		super.fillReportDetails(out);
		out.put("type", "dirt");
	}
}
