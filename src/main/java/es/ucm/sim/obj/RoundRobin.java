package es.ucm.sim.obj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.ucm.fdi.exceptions.IdException;
import es.ucm.sim.obj.Junction.RoadEnd;

public class RoundRobin extends Junction{
	
	/*
	 * 		"Votar por lo correcto no significa hacer algo.
	 *  Un hombre sabio no dejará el bien a la clemencia del
	 *  azar, ni deseará que este prevalezca al poder de la mayoría"
	 *  
	 *  	David Thoreau
	 */
	
	private static class RRRoadEnd extends RoadEnd{
		private int intervaloDeTiempo, uTiempoUsadas, vehiclesPassed;
		
		public RRRoadEnd(String id) {
			super(id);
			uTiempoUsadas = 0;
			vehiclesPassed = 0;
		}
		
		public int getIntervalo() {
			return intervaloDeTiempo;
		}
		
		public int getUTiempo() {
			return intervaloDeTiempo - uTiempoUsadas;
		}
		
		public int getVehiclesPassed() {
			return vehiclesPassed;
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
		
		public void passVehicle() {
			++vehiclesPassed;
		}
	}
	
	private int maxTimeSlice, minTimeSlice;
	private TreeMap<String, RRRoadEnd> myIncomingRoad;
	
	public RoundRobin(int maxts, int mints, String id) {
		super(id);
		myIncomingRoad = new TreeMap<>();
		maxTimeSlice = maxts;
		minTimeSlice = mints;
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
		myIncomingRoad.put(r.getId(), new RRRoadEnd(r.getId()));
	}
	
	protected void avanzaSem() { //saco esto de avanza() para no repetir código en clases heredadas
		myIncomingRoad.get(greenId).changeTrfLight(); // se apaga el semáfoto encendido
		if(myIncomingRoad.tailMap(greenId).size() == 1) { //si la id de la carretera en verde es la mayor...
			greenId = myIncomingRoad.firstKey(); //actualizar greenId
			myIncomingRoad.get(greenId).changeTrfLight();
		}
		else {
			greenId=myIncomingRoad.higherKey(greenId);
			myIncomingRoad.get(greenId).changeTrfLight();
		}
	}
	
	public void avanza() throws IdException {
		if(!myIncomingRoad.isEmpty()) {
			//caso inicial
			if(greenId == null) {	
				for(RRRoadEnd rrr: myIncomingRoad.values())
					rrr.setIntervalo(maxTimeSlice);
				myIncomingRoad.lastEntry().getValue().changeTrfLight();
				myIncomingRoad.lastEntry().getValue().setIntervalo(maxTimeSlice);
				greenId = myIncomingRoad.lastKey();
			}
			//A partir de aquí greenId está siempre forzosamente definido
			if(!myIncomingRoad.get(greenId).isEmpty()) {
				saleVehiculo(greenId).moverASiguienteCarretera();
				myIncomingRoad.get(greenId).passVehicle(); //ha pasado un vehículo en este turno
			}	
			//avanzar los semáforos si ha pasado el intervalo de tiempo
			if(myIncomingRoad.get(greenId).getUTiempo() == 0) {
				if(myIncomingRoad.get(greenId).getVehiclesPassed() == myIncomingRoad.get(greenId).getIntervalo()) //han pasado todos los vehiculos
					myIncomingRoad.get(greenId).setIntervalo(Integer.min(maxTimeSlice, myIncomingRoad.get(greenId).getIntervalo() + 1));
				else if(myIncomingRoad.get(greenId).getVehiclesPassed() == 0) //no ha pasado ninguno
					myIncomingRoad.get(greenId).setIntervalo(Integer.max(minTimeSlice, myIncomingRoad.get(greenId).getIntervalo() - 1));
				myIncomingRoad.get(greenId).resetUTiempo();
				avanzaSem();
			}
			myIncomingRoad.get(greenId).increaseT();
		}
	}
	
	protected void fillReportDetails(Map<String, String> out) {
		if(!myIncomingRoad.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for(String id : myIncomingRoad.keySet()) { 
				String color;
				if(id.equals(greenId)) color = "green:" + (myIncomingRoad.get(id).getUTiempo() + 1); //null-safe
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
		out.put("type", "rr");
	}
}
