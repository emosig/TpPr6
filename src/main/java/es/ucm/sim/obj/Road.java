package es.ucm.sim.obj;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.util.MultiTreeMap;

public class Road extends SimObj{
	private int longitud;
	private MultiTreeMap<Integer, Vehicle> vehiculos;
	private int velocidadMax;
	private int velocidadBase;
	private Junction iniJ, finalJ;
	private int factorReducc(Vehicle v) {
		if(vehiculos.ceilingKey(v.getLoc()) == null) return 1; //no hay keys mayores que la del vehiculo
		else return 2;
	}
	
	public Road(String id) {
		super(id);
	}
	
	public int getLong() {
		return longitud;
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
	
	public void avanza() {
		//cosas del simulador
		//cálculo Vbase
		if(vehiculos.size() > 1) velocidadBase = Integer.min(velocidadMax,velocidadMax/vehiculos.size() + 1);
		else velocidadBase = velocidadMax;
		//Para cada vehiculo
		for(Vehicle v: vehiculos.innerValues()) {
			v.setVelocidadActual(velocidadBase/factorReducc(v));
			v.avanza();
		}
	}
	
	public String generaInforme() {
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
	}

	
}