package es.ucm.fdi.events;

import java.util.ArrayList;
import java.util.Arrays;

import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.fdi.ini.IniSection;
import es.ucm.sim.Simulator;
import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;
import es.ucm.sim.obj.SimObj;
import es.ucm.sim.obj.Vehicle;

//este evento no tiene id
public class MakeVehicleFaultyE extends Event{
	private int duration;
	private ArrayList<String> vehicles;
	private static final String NAME = "make_vehicle_faulty";
	private class MakeVehicleFaultyBuilder implements EventBuilder{
		public Event parse(IniSection s) {
			if(!s.getTag().equals(NAME)) return null;
			int arg1 = Integer.parseInt(s.getValue("time"));
			int arg2 = Integer.parseInt(s.getValue("duration"));
			String[] aux = s.getValue("vehicles").split(",");
			ArrayList<String> arg3 = new ArrayList<>(Arrays.asList(aux));
			String arg4 = s.getValue("id");
			return new MakeVehicleFaultyE(arg1, arg2, arg3, arg4);
		}
	}
	public MakeVehicleFaultyE(int time, int duration, ArrayList<String> vehicles, String id) {
		super(time, NAME, id);
		this.time = time;
		this.duration = duration;
		this.vehicles = vehicles;
		object = null;
	}

	public void ejecuta(Simulator s, ArrayList<Junction> js, ArrayList<Road> rs, ArrayList<Vehicle> vs) throws MissingObjectExc {
		for(String idf: vehicles) {
			if(s.getRoadMap().getVehicle(idf).equals(null))
				throw new MissingObjectExc("vehicle");
			s.getRoadMap().getVehicle(idf).setTiempoAveria(duration);
		}
	}
}
