package es.ucm.fdi.events;

import java.util.ArrayList;

import es.ucm.model.sim.Simulator;
import es.ucm.model.sim.obj.Junction;
import es.ucm.model.sim.obj.Road;
import es.ucm.model.sim.obj.RoundRobin;
import es.ucm.model.sim.obj.Vehicle;

/*
 * Evento de nuevo RoundRobin
 */
public class NewRRE extends NewJunctionE{
	private int maxT, minT;
	
	public NewRRE(int maxT, int minT, int time, String id) {
		super(time, id);
		this.minT = minT;
		this.maxT = maxT;
	}
	
	public void ejecuta(Simulator s, ArrayList<Junction> js, 
			ArrayList<Road> rs, ArrayList<Vehicle> vs) {
		if(done) {
			return;
		}
		js.add(new RoundRobin(maxT, minT, id));
		done = true;
	}
}
