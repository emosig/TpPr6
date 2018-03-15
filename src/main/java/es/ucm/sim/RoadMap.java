package es.ucm.sim;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
	
	public RoadMap() {
		simObjects = new HashMap<>();
		junctionsRO = Collections.unmodifiableList(junctions);
		roadsRO = Collections.unmodifiableList(roads);
		vehiclesRO = Collections.unmodifiableList(vehicles);
	}
	
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
	//devuelve la carretera (si existe) entre dos cruces
	public Road getRoadBetween(String id1, String id2) {
		for(Road r: roads)
			if(r.getFinalJ().getId().equals(id2) && r.getIniJ().getId().equals(id1))
				return r;
		return null;
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
	//comprueban que la id no figura en simObjects
	void addJunction(Junction j) {
		if(simObjects.get(j.getId()) == null) {
			simObjects.put(j.getId(), j);
			junctions.add(j);
			junctionsRO = Collections.unmodifiableList(junctions);
		}
	}
	void addRoad(Road r) {
		if(simObjects.get(r.getId()) == null) {
			simObjects.put(r.getId(), r);
			roads.add(r);
			roadsRO = Collections.unmodifiableList(roads);
		}
	}
	void addVehicle(Vehicle v) {
		if(simObjects.get(v.getId()) == null) {
			simObjects.put(v.getId(), v);
			vehicles.add(v);
			vehiclesRO = Collections.unmodifiableList(vehicles);
		}
	}
}
