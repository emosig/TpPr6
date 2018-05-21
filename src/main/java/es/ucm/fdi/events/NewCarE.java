package es.ucm.fdi.events;

import java.util.ArrayList;

import es.ucm.fdi.exceptions.IdException;
import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.model.sim.Simulator;
import es.ucm.model.sim.obj.Car;
import es.ucm.model.sim.obj.Junction;
import es.ucm.model.sim.obj.Road;
import es.ucm.model.sim.obj.Vehicle;

/*
 * 	Evento para nuevo coche
 */
public class NewCarE extends NewVehicleE{
	private int resistance;
	private double probability;
	private int maxFault;
	private long seed;
	
	/*
	 * @param seed - es opcional
	 */
	public NewCarE(int resistance, double probability, int maxFault, long seed, 
			int time, int maxV, ArrayList<String> itinerary, String id) {
		super(time, maxV, itinerary, id);
		commonInit(resistance, probability, maxFault);
		this.seed = seed;
	}
	
	public NewCarE(int resistance, double probability, int maxFault, int time,
			int maxV, ArrayList<String> itinerary, String id) {
		super(time, maxV, itinerary, id);
		commonInit(resistance, probability, maxFault);
		seed = System.currentTimeMillis();
	}
	
	/*
	 * Parte común a las dos constructoras
	 */
	public void commonInit(int resistance, double probability, int maxFault) {
		this.resistance = resistance;
		this.probability = probability;
		this.maxFault = maxFault;
	}
	
	public void ejecuta(Simulator s, ArrayList<Junction> js, 
			ArrayList<Road> rs, ArrayList<Vehicle> vs) 
					throws MissingObjectExc, IdException {
		if(done) {
			return;
		}
		Car c = new Car(resistance, probability, maxFault, seed, maxV, 
				super.createIt(s), id);
		vs.add(c);
		c.getActualRoad().entraVehiculo(c);
		done = true;
	}

}
