package es.ucm.sim.obj;

import java.util.Map;

import es.ucm.fdi.util.Describable;
import es.ucm.fdi.util.DescribableEntity;

/*
 * 		"Como si fueran niños en los cuentos
 * de la infancia escuchan a los vientos contarles
 * de los campos quemados
 * pero no son niños
 * ya no hay quien los lleve en brazos"
 * 
 * 		Inger Christensen
 */
public abstract class SimObj extends DescribableEntity{
	protected String id;
	protected abstract void fillReportDetails(Map<String, String> out);
	protected abstract String getReportHeader();
	protected abstract void describeFurther(Map<String, String> out);
	
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
	
	public void describe(Map<String, String> out) {
		out.put("ID", id);
		describeFurther(out);
	}
}
