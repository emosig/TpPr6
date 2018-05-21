package es.ucm.model.sim;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import es.ucm.fdi.events.Event;
import es.ucm.fdi.exceptions.IdException;
import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.fdi.exceptions.NegativeArgExc;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.util.MultiTreeMap;
import es.ucm.model.SimulatorListener;
import es.ucm.model.sim.obj.*;

/*
 * 	Clase principal del simulador
 */
public class Simulator {
	
	private int simTime;
	private int limit;
	private MultiTreeMap<Integer, Event> evs;
	private RoadMap m;
	private List<SimulatorListener> listeners;
	private boolean accessRoadMap;
	
	public enum EventType{
		REGISTERED, RESET, NEW_EVENT, ADVANCED, ERROR
	}
	
	/*
	 * Clase interna para los eventos de la práctica 5
	 */
	public class UpdateEvent {
		
		private EventType type;
		
		public UpdateEvent(EventType e) {
			type = e;
		}
		
		public EventType getEvent() {
			return type;
		}
		
		public List<Vehicle> getVehicles() {
			return m.getVehicles();
		}
		
		public List<Road> getRoads() {
			return m.getRoads();
		}
		
		public List<Junction> getJunctions() {
			return m.getJunctions();
		}
		
		public List<Event> getEventQueue() {
			return evs.valuesList();
		}
		
		public int getCurrentTime() {
			return simTime;
		}
	}
	
	public Simulator(int t) throws NegativeArgExc{
		if(t <= 0) {
			throw new NegativeArgExc("Pasos de simulación negativos o 0");
		}
		simTime = 0;
		limit = t;
		m = new RoadMap();
		evs = new MultiTreeMap<>();
		listeners  = new ArrayList<>();
		accessRoadMap = false;
	}
	
	public int getSimTime() {
		return simTime;
	}
	
	public boolean canAccessRM() {
		return !m.isEmpty();
	}
	
	public RoadMap getRoadMap() {
		return m;
	}
	
	public List<Event> getEventQueue() {
		return evs.valuesList();
	}
	
	public boolean insertaEvento(Event e) {
		if(e.getTime() < simTime) {
			return false;
		}
		fireUpdateEvent(EventType.NEW_EVENT, "");
		evs.putValue(e.getTime(), e);
		return true;
	}
	
	public void addSimulatorListener(SimulatorListener l) {
		listeners.add(l);
		UpdateEvent ue = new UpdateEvent(EventType.REGISTERED);
		// evita pseudo-recursividad
		SwingUtilities.invokeLater(()->l.registered(ue));
	}
	
	public void removeListener(SimulatorListener l) {
		listeners.remove(l);
	}
	
	/*
	 * uso interno, evita tener que escribir el mismo bucle muchas veces
	 */
	private void fireUpdateEvent(EventType type, String error) {
		// envia un evento apropiado a todos los listeners
		for(SimulatorListener l: listeners) {
			UpdateEvent ue = new UpdateEvent(type);
			switch(type) {
			case RESET:
				SwingUtilities.invokeLater(()->l.reset(ue));
				break;
			case NEW_EVENT:
				SwingUtilities.invokeLater(()->l.newEvent(ue));
				break;
			case ADVANCED:
				SwingUtilities.invokeLater(()->l.advanced(ue));
				break;
			case ERROR:
				SwingUtilities.invokeLater(()->l.error(ue, error));
				break;
			default:
				break;
			}
		}
	}
	
	public void reset() {
		//reset
		simTime = 0;
		m = new RoadMap();
		evs = new MultiTreeMap<>();
		fireUpdateEvent(EventType.RESET, "");
	}
	
	/*
	 * Ejecuta los pasos del simulador
	 * Usar cuando haya archivo de eventos!!
	 */
	private void executeFurther(OutputStream out) {
		//1 ejecutar eventos
		for(Event e: evs.innerValues()) {
			if(!e.getDone() && e.getTime() == simTime) { 
				//evita repetición de eventos
				ArrayList<Junction> js = new ArrayList<>();
				ArrayList<Road> rs = new ArrayList<>();
				ArrayList<Vehicle> vs = new ArrayList<>();
				try {
					e.ejecuta(this, js, rs, vs);
				} catch (MissingObjectExc e1) {
					fireUpdateEvent(EventType.ERROR, e1.getMessage());
				} catch (IdException e1) {
					fireUpdateEvent(EventType.ERROR, e1.getMessage());
				}	
				//añado objetos al roadmap
				for(Junction j: js) m.addJunction(j);
				for(Road r: rs) m.addRoad(r);
				for(Vehicle v: vs) m.addVehicle(v);
			}
		}
		
		//2 avanzar carreteras
		for(Road r: m.getRoads()) {
			r.avanza();
		}
		
		//3 avanzar cruces
		for(Junction j: m.getJunctions()) {
			try {
				j.avanza();
			} catch (IdException e2) {
				fireUpdateEvent(EventType.ERROR, e2.getMessage());
			}
		}
		
		//4 incrementar t
		++simTime;
			//advanced()
		fireUpdateEvent(EventType.ADVANCED, null);
		
		//5 escribir informe si out != null
		if(out != null) {
			try {
				for(SimObj j: m.getJunctions())
					writeReport(j, out);
				for(SimObj r: m.getRoads())
					writeReport(r, out);
				for(SimObj v: m.getVehicles())
					writeReport(v, out);
			} catch (IOException e1) {
				fireUpdateEvent(EventType.ERROR, "Error en la escritura");
			}
		}
	}
	
	/*
	 * Ejecuta un número de pasos
	 */
	public void ejecutaSteps(int steps, OutputStream out) {
		for(int i = 0; i < steps; ++i) executeFurther(out);
	}
	
	/*
	 * Ejecuta hasta llegar al límite establecido
	 */
	public void ejecuta(int steps, OutputStream out){
		while(simTime < limit) {
			executeFurther(out);
		}
	}
	
	/*
	 * Rellena un mapa con todas las keys
	 */
	private void writeReport(SimObj obj, OutputStream out) 
			throws IOException {
		Map<String, String> map = new HashMap<>();
		obj.generaInforme(simTime, map);
		sortTags(map, out);
	}
	
	/*
	 * Método auxiliar para garantizar el orden de los apartados 
	 * de cada iniSection
	 */
	private void sortTags(Map<String, String> m, OutputStream out) 
			throws IOException {
		//esta es la key del report header
		IniSection is = new IniSection(m.get("")); 
		m.remove("");
		//reference proporciona el orden de los distintos campos a la hora 
		//de hacer reports
		String[] tagOrder = {
				"id", "time", "queues", "type", "speed", 
				"kilometrage", "faulty", "location", "state", 
		};
		Map<Integer, String> reference = new HashMap<>();
		for(int i = 0; i < tagOrder.length; ++i)
			reference.put(i, tagOrder[i]);
		//Ahora recorro reference y meto en out solo los mappings 
		//cuya key existe en m
		for(int j = 0; j < reference.size();++j) {
			String key = m.get(reference.get(j));
			if(key != null) is.setValue(reference.get(j), key);
		}
		is.store(out);
		out.write('\n');
	}
}
