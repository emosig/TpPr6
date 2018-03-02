package es.ucm.fdi.events;

import java.util.ArrayList;
import java.util.Arrays;

import es.ucm.fdi.ini.IniSection;

public class MakeVehicleFaultyE extends Event{
	private int duration;
	private ArrayList<String> vehicles;
	
	
	public MakeVehicleFaultyE() {
		super(-1, "make_vehicle_faulty");
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

	public void ejecuta() {
		// TODO Auto-generated method stub
		
	}

}
