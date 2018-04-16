package es.ucm.fdi.events;

import java.util.ArrayList;

import es.ucm.sim.Simulator;
import es.ucm.sim.obj.CrowdedJunction;
import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;
import es.ucm.sim.obj.Vehicle;

public class NewMCE extends NewJunctionE{
	public NewMCE(int time, String id) {
		super(time, id);
	}
	
	public void ejecuta(Simulator s, ArrayList<Junction> js, ArrayList<Road> rs, ArrayList<Vehicle> vs) {
		if(done) return;
		js.add(new CrowdedJunction(id));
		done = true;
	}
}
