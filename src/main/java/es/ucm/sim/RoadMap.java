package es.ucm.sim;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import es.ucm.sim.obj.*;

public class RoadMap {
	// búsqueda por ids, unicidad
	private Map<String, SimObj> simObjects;
	// listados reales
	private List<Junction> junctions = new ArrayList<>();
	private List<Road> roads = new ArrayList<>();
	private List<Vehicle> vehicles = new ArrayList<>();
	// listados read-only, via Collections.unmodifiableList();
	private List<Junction> junctionsRO;
	private List<Road> roadsRO;
	private List<Vehicle> vehiclesRO; 
	// búsqueda por ids, unicidad
	public SimObj getSimObj(String id) {
		return simObjects.get(id);
	}
	public Junction getJunction(String id) {
		for(Junction j:junctions) {
			if(j.getId().equals(id)) return j;
		}
		return null; //throw algo
	}
	public Road getRoad(String id) {
		for(Road r:roads) {
			if(r.getId().equals(id)) return r;
		}
		return null; //throw algo
	}
	public Vehicle getVehicle(String id) {
		for(Vehicle v:vehicles) {
			if(v.getId().equals(id)) return v;
		}
		return null; //throw algo
	}
	// listado (sólo lectura)
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(junctionsRO);
	}
	public List<Road> getRoads(){
		return Collections.unmodifiableList(roadsRO);
	}
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(vehiclesRO);
	}
	// inserción de objetos (package-protected)
	void addJunction(Junction j) {
		
	}
}
