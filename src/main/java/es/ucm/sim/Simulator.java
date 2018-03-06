package es.ucm.sim;

import java.io.IOException;
import java.io.OutputStream;

import es.ucm.fdi.events.Event;
import es.ucm.fdi.util.MultiTreeMap;
import es.ucm.sim.obj.*;

public class Simulator {
	private MultiTreeMap<Integer, Event> evs;
	private RoadMap m;
	private int simTime;
	
	public int getSimTime() {
		return simTime;
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
			for(Event e: evs.innerValues())	e.ejecuta(this);
			//2 avanzar carreteras
			for(Road r: m.getRoads()) r.avanza();
			//3 avanzar cruces
			for(Junction j: m.getJunctions()) j.avanza();
			//4 incrementar t
			++simTime;
			//5 escribir informe si out != null
			if(escribe) {
				try {
					for(Junction j: m.getJunctions()) out.write(j.generaInforme().getBytes());
					for(Road r: m.getRoads()) out.write(r.generaInforme().getBytes());
					for(Vehicle v: m.getVehicles()) out.write(v.generaInforme().getBytes());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		}
	}
}
