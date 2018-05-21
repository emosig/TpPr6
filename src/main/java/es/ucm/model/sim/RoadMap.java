package es.ucm.model.sim;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.model.sim.obj.*;

/*
 * 	Clase que conecta todos los objetos en simulación
 */
public class RoadMap {
	/*
	 * búsqueda por ids, unicidad
	 */
	private Map<String, SimObj> simObjects;
	/*
	 * listados reales
	 */
	private List<Junction> junctions = new ArrayList<>();
	private List<Road> roads = new ArrayList<>();
	private List<Vehicle> vehicles = new ArrayList<>();
	/*
	 * listados read-only, via Collections.unmodifiableList();
	 */
	private List<Junction> junctionsRO;
	private List<Road> roadsRO;
	private List<Vehicle> vehiclesRO; 
	
	public RoadMap() {
		simObjects = new HashMap<>();
		junctionsRO = Collections.unmodifiableList(junctions);
		roadsRO = Collections.unmodifiableList(roads);
		vehiclesRO = Collections.unmodifiableList(vehicles);
	}
	
	/*
	 * búsqueda por ids, unicidad
	 */
	public SimObj getSimObj(String id) {
		return simObjects.get(id);
	}
	
	public Junction getJunction(String id) throws MissingObjectExc {
		for(Junction j:junctions) {
			if(id.equals(j.getId())) {
				return j;
			}
		}
		throw new MissingObjectExc("Can't find a junction with id " + id);
	}
	
	public Road getRoad(String id) throws MissingObjectExc {
		for(Road r:roads) {
			if(id.equals(r.getId())) {
				return r;
			}
		}
		throw new MissingObjectExc("Can't find a road with id " + id);
	}
	
	public Vehicle getVehicle(String id) throws MissingObjectExc {
		for(Vehicle v:vehicles) {
			if(id.equals(v.getId())) {
				return v;
			}
		}
		throw new MissingObjectExc("Can't find a vehicle with id " + id);
	}
	
	/*
	 * Auxiliar para encontrar la carretera que une dos cruces
	 */
	public Road getRoadBetween(String id1, String id2) 
			throws MissingObjectExc {
		for(Road r: roads)
			if(id2.equals(r.getFinalJ().getId()) 
					&& id1.equals(r.getIniJ().getId())) {
				return r;
			}
		StringBuilder sb = new StringBuilder();
		sb.append("Can't find the road between ")
			.append(id1).append(" and ").append(id2);
		throw new MissingObjectExc(sb.toString());
	}
	
	/*
	 * Listado (solo lectura)
	 */
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(junctionsRO);
	}
	
	public List<Road> getRoads(){
		return Collections.unmodifiableList(roadsRO);
	}
	
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(vehiclesRO);
	}
	
	/*
	 * inserción de objetos (package-protected)
	 * /comprueban que la id no figura en simObjects
	 */
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
	
	public boolean isEmpty() {
		return simObjects.size() == 0;
	}
}
