package es.ucm.sim.obj;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.util.MultiTreeMap;

public class Road extends SimObj{
	protected MultiTreeMap<Integer, Vehicle> vehiculos;
	protected int velocidadMax;
	protected int velocidadBase;
	private int longitud;
	private Junction iniJ, finalJ;
	private int factorReducc(Vehicle v) {
		if(vehiculos.ceilingKey(v.getLoc()) == null) return 1; //no hay keys mayores que la del vehiculo
		else return 2;
	}
	
	public Road(int vMax, int length, Junction iniJ, Junction finalJ, String id) {
		super(id);
		velocidadMax = vMax;
		longitud = length;
		this.iniJ = iniJ;
		this.finalJ = finalJ;
		vehiculos = new MultiTreeMap<>();
	}
	
	public Road(String id) {
		super(id);
	}
	
	public int getLong() {
		return longitud;
	}
	
	public Junction getIniJ() {
		return iniJ;
	}
	
	public Junction getFinalJ() {
		return finalJ;
	}
	
	public void entraVehiculo(Vehicle v) {
		vehiculos.putValue(0, v);
	}
	
	public boolean saleVehiculo(Vehicle v) {
		return vehiculos.removeValue(v.getLoc(), v);
	}
	
	public int calcVBase() {
		if(vehiculos.size() > 1) return Integer.min(velocidadMax,velocidadMax/vehiculos.size() + 1);
		else return velocidadMax;
	}
	
	public void avVehicles() {
		for(Vehicle v: vehiculos.innerValues()) {
			v.setVelocidadActual(velocidadBase/factorReducc(v));
			v.avanza();
		}
	}
	
	public void avanza() { //partirla en funciones más pequeñas facilita especificacion clases hijas
		//cosas del simulador
		velocidadBase = calcVBase();
		avVehicles();
	}
	
	/*public String generaInforme() {
		IniSection ini = new IniSection("road_report");
		ini.setValue("id", getId());
		//ini.setValue("time", value); ??
		
		if(!vehiculos.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for(Vehicle v : vehiculos.innerValues()) { //recorrer el multitreemap
				sb.append("(" + v.getLoc() + "," + v.getId() +"), ");
			}
			sb.setLength(sb.length() - 2); //elimino la ultima coma y el ultimo espacio
			ini.setValue("state", String.join(", ", sb));
		}
		return ini.toString();
	}*/

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		if(!vehiculos.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for(Vehicle v : vehiculos.innerValues()) //recorrer el multitreemap
				sb.append("(" + v.getLoc() + "," + v.getId() +"), ");
			sb.setLength(sb.length() - 2); //elimino la ultima coma y el ultimo espacio
			out.put("state", String.join(", ", sb));
		}
	}

	@Override
	protected String getReportHeader() {
		return "road_report";
	}

	
}