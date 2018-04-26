package es.ucm.fdi.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import es.ucm.fdi.exceptions.IdException;
import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.fdi.ini.IniSection;
import es.ucm.sim.Simulator;
import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;
import es.ucm.sim.obj.Vehicle;

/*
 * Evento de nuevo vehículo
 */
public class NewVehicleE extends NewObjE{
	protected int maxV;
	private ArrayList<String> itinerary;
	private static final String NAME = "new_vehicle";
	
	public static class NewVehicleBuilder implements EventBuilder{
		public Event parse(IniSection s) {
			if(!NAME.equals(s.getTag())) return null;
			int t = Integer.parseInt(s.getValue("time"));
			int max = Integer.parseInt(s.getValue("max_speed"));
			String[] aux = s.getValue("itinerary").split(",");
			ArrayList<String> it = new ArrayList<>(Arrays.asList(aux));
			String id = s.getValue("id");
			
			if(s.getKeys().size() == 4) //es un vehiculo simple
				return new NewVehicleE(t, max, it, id);
			else if(s.getKeys().size() == 5) { //es una bicicleta
				if(!"bike".equals(s.getValue("type"))) return null;
				return new NewBikeE(t, max, it, id);
			}
			else if(s.getKeys().size() == 7){
				//es un coche en el que se ha omitido type
				int resist = Integer.parseInt(s.getValue("resistance"));
				double prob = Double.parseDouble(
						s.getValue("fault_probability"));
				int dur = Integer.parseInt(s.getValue("max_fault_duration"));
				if(s.getKeys().contains("seed")) {
					long seed = Long.parseLong(s.getValue("seed"));
					return new NewCarE(resist, prob, dur, seed, t, max, it, 
							id);
				}
				else return new NewCarE(resist, prob, dur, t, max, it, id);
				
			}
			else{ //es un coche
				if(!"car".equals(s.getValue("type"))) return null;
				int resist = Integer.parseInt(s.getValue("resistance"));
				double prob = Double.parseDouble(
						s.getValue("fault_probability"));
				int dur = Integer.parseInt(s.getValue("max_fault_duration"));
				long seed = Long.parseLong(s.getValue("seed"));
				return new NewCarE(resist, prob, dur, seed, t, max, it, 
						id);
			}
		}
	}
	
	public NewVehicleE(int time, int maxV, ArrayList<String> itinerary, 
			String id) {
		super(time, NAME, id);
		this.maxV = maxV;
		this.itinerary = itinerary; 
	}

	public ArrayList<Road> createIt(Simulator s) throws MissingObjectExc {
		ArrayList<Road> it = new ArrayList<>();
		for(int i = 1; i < itinerary.size(); ++i) {
			if(s.getRoadMap().getRoadBetween(itinerary.get(i-1), 
					itinerary.get(i)) == null) {
				//no existe carretera entre estos dos cruces
				throw new MissingObjectExc(name);
			}
			else {
				it.add(s.getRoadMap().getRoadBetween(itinerary.get(i-1), 
						itinerary.get(i)));
			}
		}
		return it;
	}
	public void ejecuta(Simulator s, ArrayList<Junction> js, 
			ArrayList<Road> rs, ArrayList<Vehicle> vs) 
					throws MissingObjectExc, IdException {
		if(done) return;
		Vehicle v = new Vehicle(maxV, createIt(s), id);
		vs.add(v);
		//meto el vehiculo en la primera carretera de su itinerario 
		v.getActualRoad().entraVehiculo(v);
		done = true;
	}
	@Override
	protected void describeFurther(Map<String, String> out) {
		out.put("Type", "New Vehicle " + id);
	}
}
