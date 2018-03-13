package es.ucm.fdi.events;

import java.util.ArrayList;

import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.sim.Simulator;
import es.ucm.sim.obj.Car;
import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;
import es.ucm.sim.obj.Vehicle;

public class NewCarE extends NewVehicleE{
	private int resistance;
	private double probability;
	private int maxFault;
	private long seed;
	
	public NewCarE(int resistance, double probability, int maxFault, long seed, int time, int maxV, ArrayList<String> itinerary, String id) {
		//seed es opcional
		super(time, maxV, itinerary, id);
		this.resistance = resistance;
		this.probability = probability;
		this.maxFault = maxFault;
		this.seed = seed;
	}
	
	public void ejecuta(Simulator s, ArrayList<Junction> js, ArrayList<Road> rs, ArrayList<Vehicle> vs) throws MissingObjectExc {
		vs.add(new Car(resistance, probability, maxFault, seed, maxV, super.createIt(s), id));
	}

}
