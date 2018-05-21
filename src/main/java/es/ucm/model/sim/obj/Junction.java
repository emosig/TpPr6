package es.ucm.model.sim.obj;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import es.ucm.fdi.exceptions.IdException;

/*
 * Cruce
 */
public class Junction extends SimObj{
	/*
	 * Clase auxiliar para almacenar una cola de veliculos mas un semáforo
	 * Los métodos son sobrecargas y modificaciones elementales de los 
	 * habituales: getters, setters(changeTrfLight), push, poll, 
	 * toString, isEmpty...
	 */
	protected static class RoadEnd{
		protected ArrayDeque<Vehicle> vqueue;
		protected Boolean TrfLight;
		protected String id;
		
		public RoadEnd(String id) {
			this.id = id;
			vqueue = new ArrayDeque<>();
			TrfLight = false;
		}
		
		public String getId() {
			return id;
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
			if(vqueue.isEmpty()) {
				return "[]";
			}
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
	protected String greenId; //id de la carretera con semáforo verde
	
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
		for(RoadEnd r: incomingRoad.values()) {
			for(Vehicle v: r.getQueue()) {
				inc.add(v);
			}
		}
		return inc;
	}
	
	public void entraVehiculo(Vehicle v) {
		if(getIncomingV().contains(v)) {
			return;
		}
		incomingRoad.get(v.getRoad()).arrive(v);
	}
	
	public Vehicle saleVehiculo(String idroad) {
		return incomingRoad.get(idroad).leave();
	}
	
	/*
	 * Cuando se crea una carretera que tiene este cruce como final
	 */
	public void addRoad(Road r) {
		incomingRoad.put(r.getId(), new RoadEnd(r.getId()));
	}
	
	/*
	 * //saco esto de avanza() para no repetir código en clases heredadas
	 */
	protected void avanzaSem() { 
		// se apaga el semáfoto encendido
		incomingRoad.get(greenId).changeTrfLight(); 
		//si la id de la carretera en verde es la mayor...
		if(incomingRoad.tailMap(greenId).size() == 1) {
			//actualizar greenId
			greenId = incomingRoad.firstKey(); 
			incomingRoad.get(greenId).changeTrfLight();
		}
		else {
			greenId=incomingRoad.higherKey(greenId);
			incomingRoad.get(greenId).changeTrfLight();
		}
	}
	
	public void avanza() throws IdException {
		if(!incomingRoad.isEmpty()) {
			//caso inicial
			if(greenId == null) {	
				incomingRoad.lastEntry().getValue().changeTrfLight();
				greenId = incomingRoad.lastKey();
			}
			//A partir de aquí greenId está siempre forzosamente definido
			if(!incomingRoad.get(greenId).isEmpty()) {
				saleVehiculo(greenId).moverASiguienteCarretera();
			}
			//avanzar los semáforos
			avanzaSem();
		}
	}

	@Override
	protected void fillReportDetails(Map<String, String> out) {
		if(!incomingRoad.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for(String id : incomingRoad.keySet()) { 
				String color;
				if(id.equals(greenId)) {
					color = "green"; //null-safe
				}
				else color  = "red";
				//creo lista de cosas que van a ir separadas por coma para
				//evitar concatenaciones y appends excesivos
				String[] toAppend = {"(" + id, color, 
						incomingRoad.get(id).vqueueToString() + ")"};
				for(String s: toAppend) {
					sb.append(s).append(',');
				}
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
	
	/*
	 * Auxiliar a describeFurther()
	 */
	//perfeccionar (no entiendo dos puntos)
	private void describeCommon(StringBuilder sb, String id, String color) {
		sb.append(id).append(",").append(color).append(",[");
		if(!incomingRoad.get(id).isEmpty()) {
			for(Vehicle v: incomingRoad.get(id).getQueue()) {
				sb.append(v.getId()).append(",");
			}
			sb.setLength(sb.length() - 1);//borra la última coma
		}
		sb.append("],");
	}
	
	@Override
	protected void describeFurther(Map<String, String> out) {
		StringBuilder sbRe = new StringBuilder("[");
		StringBuilder sbGr = new StringBuilder("[");
		if(!incomingRoad.isEmpty()) {
			sbRe.append("(");
			sbGr.append("(");
			for(String id : incomingRoad.keySet()) {
				if(id.equals(greenId)) describeCommon(sbGr, id, "green");
				else describeCommon(sbRe, id, "red");
			}
			sbRe.setLength(sbRe.length() - 1);  //borra última coma
			sbGr.setLength(sbGr.length() - 1);
			if(sbRe.charAt(sbRe.length() - 1) != '[') {
				sbRe.append(")");
			}
			if(sbGr.charAt(sbGr.length() - 1) != '[') {
				sbGr.append(")");
			}
		}
		sbRe.append("]");
		sbGr.append("]");
		out.put("Green", sbGr.toString());
		out.put("Red", sbRe.toString());
	}
}
