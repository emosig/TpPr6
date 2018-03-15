package es.ucm.sim.obj;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.TreeMap;

import es.ucm.fdi.exceptions.IdException;
import es.ucm.fdi.ini.IniSection;

public class Junction extends SimObj{
	private static class RoadEnd{
		private ArrayDeque<Vehicle> vqueue;
		private Boolean TrfLight;
		
		public RoadEnd() {
			vqueue = new ArrayDeque<>();
			TrfLight = false;
		}
		
		public Boolean green() {
			return TrfLight;
		}
		
		public void changeTrfLight() {
			if(TrfLight) TrfLight = false;
			else TrfLight = true;
		}
		
		public void arrive(Vehicle v) {
			vqueue.offer(v);
		}
		
		public Vehicle leave() {
			return vqueue.poll();
		}
		
		public String vqueueToString() {
			if(vqueue.isEmpty()) return "[]";
			StringBuilder sb = new StringBuilder();
			sb.append('[');
			for(Vehicle v: vqueue) {
				sb.append(v.getId() + ",");
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
	
	public String getGreenId() {
		return greenId;
	}
	
	public boolean isEmpty() {
		return incomingRoad.isEmpty();
	}
	
	public Junction(String id) {
		super(id);
		incomingRoad = new TreeMap<>();
	}
	
	public void entraVehiculo(Vehicle v) { //getroad devuelve el id de la carretera
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
				incomingRoad.firstEntry().getValue().changeTrfLight();
				greenId = incomingRoad.firstKey();
			}
			//A partir de aquí greenId está forzosamente definido
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
			for(String id : incomingRoad.keySet()) { //recorrer el treemap
				String color;
				if(id == greenId) color = "green";
				else color  = "red";
				sb.append("(" + id + "," + color + "," + incomingRoad.get(id).vqueueToString() + "), ");
			}
			sb.setLength(sb.length() - 2); //elimino la ultima coma y el ultimo espacio
			out.put("queues", String.join(", ", sb));
		}
		else out.put("queues", "");
	}

	@Override
	protected String getReportHeader() {
		return "junction_report";
	}
}
