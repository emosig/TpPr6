package es.ucm.sim;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import es.ucm.fdi.events.Event;
import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.fdi.util.MultiTreeMap;
import es.ucm.sim.obj.*;

public class Simulator {
	private MultiTreeMap<Integer, Event> evs;
	private RoadMap m;
	private int simTime;
	
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
		int tlimit = simTime + steps;
		while(simTime < tlimit) {
			//1 ejecutar eventos
			for(Event e: evs.innerValues()) {
				//cómo coño añado los objetos?????
				//colas para cada tipo de objetos?
				ArrayList<Junction> js = new ArrayList<>();
				ArrayList<Road> rs = new ArrayList<>();
				ArrayList<Vehicle> vs = new ArrayList<>();
				try {
					e.ejecuta(this, js, rs, vs);
				} catch (MissingObjectExc e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//añado objetos al roadmap
				for(Junction j: js) m.addJunction(j);
				for(Road r: rs) m.addRoad(r);
				for(Vehicle v: vs) m.addVehicle(v);
			}
			//2 avanzar carreteras
			for(Road r: m.getRoads()) r.avanza();
			//3 avanzar cruces
			for(Junction j: m.getJunctions()) j.avanza();
			//4 incrementar t
			++simTime;
			//5 escribir informe si out != null
			if(escribe) {
				Map<String, String> map = new TreeMap<>();
				writeReport(map, out);
				//implementar traductor inisection - mapa
			}
		}
	}
	private void writeReport(Map<String, String> report, OutputStream out) {
		for(SimObj j: m.getJunctions()) j.generaInforme(simTime, report);
		for(SimObj r: m.getRoads()) r.generaInforme(simTime, report);
		for(SimObj v: m.getVehicles()) v.generaInforme(simTime, report);
	}
}
