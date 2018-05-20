package es.ucm.fdi.events;

import java.util.ArrayList;

import es.ucm.model.sim.Simulator;
import es.ucm.model.sim.obj.CrowdedJunction;
import es.ucm.model.sim.obj.Junction;
import es.ucm.model.sim.obj.Road;
import es.ucm.model.sim.obj.Vehicle;

/*
 * Evento para nuevo cruce congestionado
 */
public class NewMCE extends NewJunctionE{
	public NewMCE(int time, String id) {
		super(time, id);
	}
	
	public void ejecuta(Simulator s, ArrayList<Junction> js, 
			ArrayList<Road> rs, ArrayList<Vehicle> vs) {
		if(done) return;
		js.add(new CrowdedJunction(id));
		done = true;
	}
}
