package es.ucm.fdi.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.fdi.ini.IniSection;
import es.ucm.model.sim.Simulator;
import es.ucm.model.sim.obj.Junction;
import es.ucm.model.sim.obj.Road;
import es.ucm.model.sim.obj.Vehicle;


/*
 * Evento para la avería de un vehículo
 */
public class MakeVehicleFaultyE extends Event{
	private int duration;
	private ArrayList<String> vehicles;
	private static final String NAME = "make_vehicle_faulty";
	
	public static class MakeVehicleFaultyBuilder implements EventBuilder{
		public Event parse(IniSection s) {
			if(!NAME.equals(s.getTag())) {
				return null;
			}
			int arg1 = Integer.parseInt(s.getValue("time"));
			int arg2 = Integer.parseInt(s.getValue("duration"));
			String[] aux = s.getValue("vehicles").split(",");
			ArrayList<String> arg3 = new ArrayList<>(Arrays.asList(aux));
			return new MakeVehicleFaultyE(arg1, arg2, arg3);
		}
	}
	
	public MakeVehicleFaultyE(int time, int duration, 
			ArrayList<String> vehicles) {
		super(time, NAME);
		this.time = time;
		this.duration = duration;
		this.vehicles = vehicles;
	}

	public void ejecuta(Simulator s, ArrayList<Junction> js, 
			ArrayList<Road> rs, ArrayList<Vehicle> vs) 
					throws MissingObjectExc {
		if(done) {
			return;
		}
		for(String idf: vehicles) {
			if(s.getRoadMap().getVehicle(idf) == null)
				throw new MissingObjectExc("vehicle");
			s.getRoadMap().getVehicle(idf).setTiempoAveria(duration);
		}
		done = true;
	}

	@Override
	protected void describeFurther(Map<String, String> out) {
		StringBuilder sb = new StringBuilder("Break Vehicles [");
		for(String v: vehicles) {
			sb.append(v).append(",");
		}
		sb.setLength(sb.length() - 1);
		sb.append("]");
		out.put("Type", sb.toString());
	}
}
