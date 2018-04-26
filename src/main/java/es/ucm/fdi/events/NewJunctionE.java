package es.ucm.fdi.events;

import java.util.ArrayList;
import java.util.Map;

import es.ucm.fdi.ini.IniSection;
import es.ucm.sim.Simulator;
import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;
import es.ucm.sim.obj.Vehicle;

/*
 * 	Evento para nuevo cruce
 */
public class NewJunctionE extends NewObjE {
	private static final String NAME = "new_junction";
	
	public static class NewJunctionBuilder implements EventBuilder{
		public Event parse(IniSection s) {
			if(!NAME.equals(s.getTag())) return null;
			int t = Integer.parseInt(s.getValue("time"));
			String id = s.getValue("id");
			if(s.getKeys().size() == 2)
				return new NewJunctionE(t, id);
			else if(s.getKeys().contains("type")) {
				if(s.getKeys().size() == 5) { //rr
					if(!s.getValue("type").equals("rr")) return null;
					int max = Integer.parseInt(s.getValue("max_time_slice"));
					int min = Integer.parseInt(s.getValue("min_time_slice"));
					return new NewRRE(max, min, t, id);
				}
				else { //mc
					if(!s.getValue("type").equals("mc")) return null;
					else return new NewMCE(t, id);
				}	
			}
			else return null;
		}
	}
	
	public NewJunctionE(int time, String id) {
		super(time, NAME, id);
	}
	@Override
	public void ejecuta(Simulator s, ArrayList<Junction> js, 
			ArrayList<Road> rs, ArrayList<Vehicle> vs) {
		if(done) return;
		js.add(new Junction(id));
		done = true;
	}
	@Override
	protected void describeFurther(Map<String, String> out) {
		out.put("Type", "New Junction " + id);
	}
}
