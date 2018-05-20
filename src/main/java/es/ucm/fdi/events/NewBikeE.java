package es.ucm.fdi.events;

import java.util.ArrayList;

import es.ucm.fdi.exceptions.IdException;
import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.model.sim.Simulator;
import es.ucm.model.sim.obj.Bike;
import es.ucm.model.sim.obj.Junction;
import es.ucm.model.sim.obj.Road;
import es.ucm.model.sim.obj.Vehicle;

/*
 * Evento para nueva bicicleta
 */

public class NewBikeE extends NewVehicleE{

	public NewBikeE(int time, int maxV, ArrayList<String> itinerary, String id) {
		super(time, maxV, itinerary, id);
	}
	
	public void ejecuta(Simulator s, ArrayList<Junction> js, 
			ArrayList<Road> rs, ArrayList<Vehicle> vs)
					throws MissingObjectExc, IdException {
		if(done) return;
		Bike b = new Bike(maxV, super.createIt(s), id);
		vs.add(b);
		b.getActualRoad().entraVehiculo(b);
		done = true;
	}
}
