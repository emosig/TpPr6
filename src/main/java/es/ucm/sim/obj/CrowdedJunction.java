package es.ucm.sim.obj;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.ucm.fdi.exceptions.IdException;
import es.ucm.sim.obj.Junction.RoadEnd;

public class CrowdedJunction extends Junction {
	
	/*
	 * 		"Nunca me ha gustado que me comprendiesen.
	 * Ser comprendido es prostituirse. Prefiero ser tomado en serio
	 * como lo que no soy, ignorado humanamente con decencia
	 * y naturalidad.
	 * 
	 * 		Fernando Pessoa
	 */

	private static class MCRoadEnd extends RoadEnd{
		private int intervaloDeTiempo, uTiempoUsadas;
		
		public MCRoadEnd(String id) {
			super(id);
			intervaloDeTiempo = 0;
			uTiempoUsadas = 0;
		}
		
		public int getIntervalo() {
			return intervaloDeTiempo;
		}
		
		public int getUTiempo() {
			return uTiempoUsadas;
		}
		
		public void increaseT() {
			++uTiempoUsadas;
		}
		
		public void setIntervalo(int t) {
			intervaloDeTiempo = t;
		}
		
		public void resetUTiempo() {
			uTiempoUsadas = 0;
		}
	}
	
	private TreeMap<String, MCRoadEnd> myIncomingRoad;
	
	public CrowdedJunction(String id) {
		super(id);
		myIncomingRoad = new TreeMap<>();
	}
	
	public boolean isEmpty() {
		return myIncomingRoad.isEmpty();
	}
	
	public List<Vehicle> getIncomingV(){
		List<Vehicle> inc = new ArrayList<>();
		for(RoadEnd r: myIncomingRoad.values())
			for(Vehicle v: r.getQueue())
				inc.add(v);
		return inc;
	}
	
	public void entraVehiculo(Vehicle v) { //getroad devuelve el id de la carretera
		if(getIncomingV().contains(v)) return;
		myIncomingRoad.get(v.getRoad()).arrive(v);
	}
	
	public Vehicle saleVehiculo(String idroad) {
		return myIncomingRoad.get(idroad).leave();
	}
	
	public void addRoad(Road r) {	//Cuando se crea una carretera que tiene este cruce como final
		myIncomingRoad.put(r.getId(), new MCRoadEnd(r.getId()));
	}
	
	protected void avanzaSem() { //saco esto de avanza() para no repetir código en clases heredadas
		MCRoadEnd prioridad = null;
		int max = -1; //si todas están vacías se queda con la primera
		for(MCRoadEnd e: myIncomingRoad.values()) {
			if(e.getQueue().size() > max && !greenId.equals(e.getId())) {
				max = e.getQueue().size();
				prioridad = e;
			}
		}
		if (max > 0) { //prioridad no es null
			myIncomingRoad.get(greenId).changeTrfLight();
			greenId = prioridad.getId();
			prioridad.setIntervalo(Integer.max(max / 2, 1));
			prioridad.changeTrfLight();
			prioridad.resetUTiempo();
		}
	}
	
	
	public void avanza() throws IdException {
 		if(!myIncomingRoad.isEmpty()) {
			//caso inicial
			if(greenId == null) {	
				myIncomingRoad.lastEntry().getValue().changeTrfLight();
				myIncomingRoad.lastEntry().getValue().setIntervalo(Integer.max(myIncomingRoad.lastEntry().getValue().getQueue().size() / 2, 1));
				greenId = myIncomingRoad.lastKey();
			}
			//A partir de aquí greenId está siempre forzosamente definido
			if(!myIncomingRoad.get(greenId).isEmpty())
				saleVehiculo(greenId).moverASiguienteCarretera();
			//avanzar los semáforos si ha pasado el intervalo de tiempo
			if(myIncomingRoad.get(greenId).getIntervalo() == myIncomingRoad.get(greenId).getUTiempo())
				avanzaSem();
			else myIncomingRoad.get(greenId).increaseT();
		}
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		if(!myIncomingRoad.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for(String id : myIncomingRoad.keySet()) { 
				String color;
				if(id.equals(greenId)) color = "green:" + myIncomingRoad.get(id).getUTiempo(); //null-safe
				else color  = "red";
				//creo lista de cosas que van a ir separadas por coma spara evitar concatenaciones y appends excesivos
				String[] toAppend = {"(" + id, color, myIncomingRoad.get(id).vqueueToString() + ")"};
				for(String s: toAppend)
					sb.append(s).append(',');
			}
			sb.setLength(sb.length() - 1); //elimino la ultima coma
			out.put("queues", String.join(", ", sb));
		}
		else out.put("queues", "");
		out.put("type", "mc");
	}
}
