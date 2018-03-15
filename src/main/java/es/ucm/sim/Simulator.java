package es.ucm.sim;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.events.Event;
import es.ucm.fdi.exceptions.IdException;
import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.fdi.exceptions.NegativeArgExc;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.util.MultiTreeMap;
import es.ucm.sim.obj.*;

public class Simulator {
	private MultiTreeMap<Integer, Event> evs;
	private RoadMap m;
	private int simTime;
	private int limit;
	
	public Simulator(int t) throws NegativeArgExc{
		if(t <= 0) throw new NegativeArgExc("Pasos de simulación negativos o 0");
		simTime = 0;
		limit = t;
		m = new RoadMap();
		evs = new MultiTreeMap<>();
	}
	
	public int getSimTime() {
		return simTime;
	}
	public RoadMap getRoadMap() {
		return m;
	}
	
	public boolean insertaEvento(Event e) {
		if(e.getTime() < simTime) return false;
		evs.putValue(e.getTime(), e);
		return true;
	}
	public void ejecuta(int steps, OutputStream out){
		boolean escribe = out != null; //no lo voy a comprobar en cada vuelta del bucle
		while(simTime < limit) {
			//1 ejecutar eventos
			for(Event e: evs.innerValues()) {
				if(!e.getDone() && e.getTime() == simTime) { //evita repetición de eventos
					ArrayList<Junction> js = new ArrayList<>();
					ArrayList<Road> rs = new ArrayList<>();
					ArrayList<Vehicle> vs = new ArrayList<>();
					try {
						e.ejecuta(this, js, rs, vs);
					} catch (MissingObjectExc e1) {
						//e1.printStackTrace();
					} catch (IdException e1) {
						//e1.printStackTrace();
					}
					
					//añado objetos al roadmap
					for(Junction j: js) m.addJunction(j);
					for(Road r: rs) m.addRoad(r);
					for(Vehicle v: vs) m.addVehicle(v);
				}
			}
			//2 avanzar carreteras
			for(Road r: m.getRoads()) r.avanza();
			//3 avanzar cruces
			for(Junction j: m.getJunctions()) {
				try {
					j.avanza();
				} catch (IdException e2) {
					//e2.printStackTrace();
				}
			}
			//4 incrementar t
			++simTime;
			//5 escribir informe si out != null
			if(escribe) {
				try {
					for(SimObj j: m.getJunctions())
						writeReport(j, out);
					for(SimObj r: m.getRoads())
						writeReport(r, out);
					for(SimObj v: m.getVehicles())
						writeReport(v, out);
				} catch (IOException e1) {
					System.out.println("Error en la escritura");
				}
			}
		}
	}
	/*
	 * Rellena un mapa con todas las keys
	 */
	private void writeReport(SimObj obj, OutputStream out) throws IOException {
		Map<String, String> map = new HashMap<>();
		obj.generaInforme(simTime, map);
		sortTags(map, out);
	}
	/*
	 * Método auxiliar para garantizar el orden de los apartados de cada iniSection
	 */
	private void sortTags(Map<String, String> m, OutputStream out) throws IOException {
		//Rellena la inisection con el map
		IniSection is = new IniSection(m.get("")); //esta es la key del report header
		m.remove("");
		//reference proporciona el orden de los distintos campos a la hora de hacer reports
		String[] tagOrder = {
				"id", "time", "type", "speed", "kilometrage",
				"faulty", "location", "state", "queues"
		};
		Map<Integer, String> reference = new HashMap<>();
		for(int i = 0; i < tagOrder.length; ++i)
			reference.put(i, tagOrder[i]);
		//Ahora recorro reference y meto en out solo los mappings cuya key existe en m
		for(int j = 0; j < reference.size();++j) {
			String key = m.get(reference.get(j));
			if(key != null) is.setValue(reference.get(j), key);
		}
		is.store(out);
		out.write('\n');
	}
}
