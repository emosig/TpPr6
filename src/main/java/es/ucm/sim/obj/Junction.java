package es.ucm.sim.obj;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.TreeMap;

import es.ucm.fdi.ini.IniSection;

public class Junction extends SimObj{
	private class RoadEnd{
		private ArrayDeque<Vehicle> queue;
		private Boolean TrfLight;
		
		public Boolean green() {
			return TrfLight;
		}
		
		public void changeTrfLight() {
			if(TrfLight) TrfLight = false;
			else TrfLight = true;
		}
		
		public void arrive(Vehicle v) {
			queue.offer(v);
		}
		
		public Vehicle leave() {
			return queue.poll();
		}
		
		public Boolean isEmpty() {
			return queue.isEmpty();
		}
	}
	private TreeMap<String, RoadEnd> incomingRoad;
	private String greenId; //id de la carretera con semáforo verde

	public Junction(String id) {
		super(id);
	}
	
	public void entraVehiculo(Vehicle v) { //getroad devuelve el id de la carretera
		incomingRoad.get(v.getRoad()).arrive(v);
	}
	
	public Vehicle saleVehiculo(String idroad) {
		return incomingRoad.get(idroad).leave();
	}
	
	public void avanza() {
		if(!incomingRoad.isEmpty()) {
			if(!incomingRoad.get(greenId).isEmpty()) //en qué caso no es posible??
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
	
	public String generaInforme() {
		IniSection ini = new IniSection("junction_report");
		ini.setValue("id", getId());
		//ini.setValue("time", value); ??
		//ini.setValue("queues", vehiculos); ??
		return ini.toString();
	}
}
