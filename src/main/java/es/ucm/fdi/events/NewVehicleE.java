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

public class NewVehicleE extends NewObjE{
	protected int maxV;
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
			
			if(s.getKeys().size() == 4) // es un vehiculo simple
				return new NewVehicleE(arg1, arg2, arg3, arg4);
			else if(s.getKeys().size() == 4) { //es una bicicleta
				if(!s.getValue("type").equals("bike")) return null;
				return new NewBikeE(arg1, arg2, arg3, arg4);
			}
			else { //es un coche
				if(!s.getValue("type").equals("car")) return null;
				int arg5 = Integer.parseInt(s.getValue("resistance"));
				double arg6 = Double.parseDouble(s.getValue("fault_probability"));
				int arg7 = Integer.parseInt(s.getValue("max_fault_duration"));
				long arg8 = Long.parseLong(s.getValue("seed"));
				return new NewCarE(arg5, arg6, arg7, arg8, arg1, arg2, arg3, arg4);
			}
		}
	}
	public NewVehicleE(int time, int maxV, ArrayList<String> itinerary, String id) {
		super(time, NAME, id);
		this.maxV = maxV;
		this.itinerary = itinerary; //solucionar cambio junction road
	}

	public ArrayList<Road> createIt(Simulator s) throws MissingObjectExc {
		ArrayList<Road> it = new ArrayList<>();
		for(int i = 1; i < itinerary.size(); ++i) {
			if(s.getRoadMap().getRoadBetween(itinerary.get(i-1), itinerary.get(i)).equals(null))
				//no existe carretera entre estos dos cruces
				throw new MissingObjectExc(name);
			else it.add(s.getRoadMap().getRoadBetween(itinerary.get(i-1), itinerary.get(i)));
		}
		return it;
	}
	public void ejecuta(Simulator s, ArrayList<Junction> js, ArrayList<Road> rs, ArrayList<Vehicle> vs) throws MissingObjectExc {
		vs.add(new Vehicle(maxV, createIt(s), id));
	}
}
