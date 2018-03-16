package es.ucm.sim.obj;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.ucm.fdi.exceptions.IdException;

/*
 * "¿Qué pasa en la verde alameda?
 * Pues que no es verde
 * y que ni quiera hay una alameda."
 * 
 * 		Alejandra Pizarnik
 */
public class Junction extends SimObj{
	/*
	 * Clase auxiliar para almacenar una cola de veliculos mas un semáforo
	 * Los métodos son sobrecargas y modificaciones elementales de los habituales:
	 * getters, setters(changeTrfLight), push, poll, toString, isEmpty...
	 */
	private static class RoadEnd{
		private ArrayDeque<Vehicle> vqueue;
		private Boolean TrfLight;
		
		public RoadEnd() {
			vqueue = new ArrayDeque<>();
			TrfLight = false;
		}
		
		public ArrayDeque<Vehicle> getQueue(){
			return vqueue;
		}
		
		public void changeTrfLight() {
			if(TrfLight) TrfLight = false;
			else TrfLight = true;
		}
		
		public void arrive(Vehicle v) {
			vqueue.offer(v);
		}
		
		public Vehicle leave() {
			Vehicle out = vqueue.poll();
			out.getActualRoad().saleVehiculo(out);
			return out;
		}
		
		public String vqueueToString() {
			if(vqueue.isEmpty()) return "[]";
			StringBuilder sb = new StringBuilder();
			sb.append('[');
			for(Vehicle v: vqueue) {
				sb.append(v.getId());
				sb.append(',');
			}
			sb.setLength(sb.length() - 1); //eliminar ultima coma
			sb.append(']');
			return sb.toString();
		}
		
		public Boolean isEmpty() {
			return vqueue.isEmpty();
		}
	}
	private TreeMap<String, RoadEnd> incomingRoad;
	private String greenId; //id de la carretera con semáforo verde
	
	public Junction(String id) {
		super(id);
		incomingRoad = new TreeMap<>();
	}
	
	public String getGreenId() {
		return greenId;
	}
	
	public boolean isEmpty() {
		return incomingRoad.isEmpty();
	}
	
	public List<Vehicle> getIncomingV(){
		List<Vehicle> inc = new ArrayList<>();
		for(RoadEnd r: incomingRoad.values())
			for(Vehicle v: r.getQueue())
				inc.add(v);
		return inc;
	}
	
	public void entraVehiculo(Vehicle v) { //getroad devuelve el id de la carretera
		if(getIncomingV().contains(v)) return;
		incomingRoad.get(v.getRoad()).arrive(v);
	}
	
	public Vehicle saleVehiculo(String idroad) {
		return incomingRoad.get(idroad).leave();
	}
	
	public void addRoad(Road r) {	//Cuando se crea una carretera que tiene este cruce como final
		incomingRoad.put(r.getId(), new RoadEnd());
	}
	
	public void avanza() throws IdException {
		if(!incomingRoad.isEmpty()) {
			//caso inicial
			if(greenId == null) {	
				incomingRoad.lastEntry().getValue().changeTrfLight();
				greenId = incomingRoad.lastKey();
			}
			//A partir de aquí greenId está siempre forzosamente definido
			if(!incomingRoad.get(greenId).isEmpty())
				saleVehiculo(greenId).moverASiguienteCarretera();
			//avanzar los semáforos
			incomingRoad.get(greenId).changeTrfLight(); // se apaga el semáfoto encendido
			if(incomingRoad.tailMap(greenId).size() == 1) { //si la id de la carretera en verde es la mayor...
				greenId = incomingRoad.firstKey(); //actualizar greenId
				incomingRoad.get(greenId).changeTrfLight();
			}
			else {
				greenId=incomingRoad.higherKey(greenId);
				incomingRoad.get(greenId).changeTrfLight();
			}
		}
	}

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		if(!incomingRoad.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for(String id : incomingRoad.keySet()) { 
				String color;
				if(id.equals(greenId)) color = "green"; //null-safe
				else color  = "red";
				//creo lista de cosas que van a ir separadas por coma spara evitar concatenaciones y appends excesivos
				String[] toAppend = {"(" + id, color, incomingRoad.get(id).vqueueToString() + ")"};
				for(String s: toAppend)
					sb.append(s).append(',');
			}
			sb.setLength(sb.length() - 1); //elimino la ultima coma
			out.put("queues", String.join(", ", sb));
		}
		else out.put("queues", "");
	}

	@Override
	protected String getReportHeader() {
		return "junction_report";
	}
}
