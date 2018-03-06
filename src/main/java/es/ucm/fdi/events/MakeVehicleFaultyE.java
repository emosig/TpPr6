package es.ucm.fdi.events;

import java.util.ArrayList;
import java.util.Arrays;

import es.ucm.fdi.ini.IniSection;
import es.ucm.sim.Simulator;

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

		@Override
		public boolean isvalid(String id) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	public MakeVehicleFaultyE(int time, int duration, ArrayList<String> vehicles, String id) {
		super(time, NAME, id);
		this.time = time;
		this.duration = duration;
		this.vehicles = vehicles;
		
	}

	public Event read(IniSection is) {
		if(is.getTag().equals(this.name)) {
			time = Integer.parseInt(is.getValue("time"));
			duration = Integer.parseInt(is.getValue("duration"));
			String[] aux = is.getValue("vehicles").split(",");
			vehicles = new ArrayList<>(Arrays.asList(aux));
			return this;
		}
		else return null; //exception?
	}

	public void ejecuta(Simulator s) {
		
	}

}
