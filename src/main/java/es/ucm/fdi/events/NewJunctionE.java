package es.ucm.fdi.events;

import java.util.ArrayList;
import java.util.Arrays;

import es.ucm.fdi.ini.IniSection;
import es.ucm.sim.Simulator;
import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;
import es.ucm.sim.obj.SimObj;
import es.ucm.sim.obj.Vehicle;

public class NewJunctionE extends NewObjE {
	private static final String NAME = "new_junction";
	private class NewJunctionBuilder implements EventBuilder{
		public Event parse(IniSection s) {
			if(!s.getTag().equals(NAME)) return null;
			int arg1 = Integer.parseInt(s.getValue("time"));
			String arg2 = s.getValue("id");
			return new NewJunctionE(arg1, arg2);
		}
	}
	public NewJunctionE(int time, String id) {
		super(time, NAME, id);
	}

	@Override
	public void ejecuta(Simulator s, ArrayList<Junction> js, ArrayList<Road> rs, ArrayList<Vehicle> vs) {
		js.add(new Junction(id));
	}
}
