package es.ucm.fdi.events;

import java.util.ArrayList;
import java.util.Arrays;

import es.ucm.fdi.ini.IniSection;
import es.ucm.sim.Simulator;

public class NewVehicleE extends NewObjE{
	private int maxV;
	private ArrayList<String> itinerary;
	private static final String NAME = "new_vehicle";
	private class NewVehicleBuilder implements EventBuilder{
		public Event parse(IniSection s) {
			if(!s.getTag().equals(NAME)) return null;
			int arg1 = Integer.parseInt(s.getValue("time"));
			int arg2 = Integer.parseInt(s.getValue("max_speed"));
			String[] aux = s.getValue("itinerary").split(",");
			ArrayList<String> arg3 = new ArrayList<>(Arrays.asList(aux));
			String arg4 = s.getValue("id");
			return new NewVehicleE(arg1, arg2, arg3, arg4);
		}

		@Override
		public boolean isvalid(String id) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	public NewVehicleE(int time, int maxV, ArrayList<String> itinerary, String id) {
		super(time, NAME, id);
		this.maxV = maxV;
		this.itinerary = itinerary; //solucionar cambio junction road
	}

	public void ejecuta(Simulator s) {
	}
}
