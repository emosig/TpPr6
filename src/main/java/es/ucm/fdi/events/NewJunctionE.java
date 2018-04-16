package es.ucm.fdi.events;

import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;
import es.ucm.sim.Simulator;
import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;
import es.ucm.sim.obj.Vehicle;

/*
 * 		"Sin encontrarse
 * Viajero por su propio torso blanco
 * ¡Así iba el aire!"
 * 
 * 		Federico García Lorca
 */
public class NewJunctionE extends NewObjE {
	private static final String NAME = "new_junction";
	
	public static class NewJunctionBuilder implements EventBuilder{
		public Event parse(IniSection s) {
			if(!NAME.equals(s.getTag())) return null;
			int arg1 = Integer.parseInt(s.getValue("time"));
			String arg2 = s.getValue("id");
			if(s.getKeys().size() == 2)
				return new NewJunctionE(arg1, arg2);
			else if(s.getKeys().contains("type")) {
				if(s.getKeys().size() == 5) { //rr
					if(!s.getValue("type").equals("rr")) return null;
					int arg3 = Integer.parseInt(s.getValue("max_time_slice"));
					int arg4 = Integer.parseInt(s.getValue("min_time_slice"));
					return new NewRRE(arg3, arg4, arg1, arg2);
				}
				else { //mc
					if(!s.getValue("type").equals("mc")) return null;
					else return new NewMCE(arg1, arg2);
				}
				
				
				
					
			}
			else return null;
		}
	}
	
	public NewJunctionE(int time, String id) {
		super(time, NAME, id);
	}
	@Override
	public void ejecuta(Simulator s, ArrayList<Junction> js, ArrayList<Road> rs, ArrayList<Vehicle> vs) {
		if(done) return;
		js.add(new Junction(id));
		done = true;
	}
}
