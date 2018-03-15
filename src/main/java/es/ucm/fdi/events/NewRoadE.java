package es.ucm.fdi.events;

import java.util.ArrayList;

import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.fdi.ini.IniSection;
import es.ucm.sim.Simulator;
import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;
import es.ucm.sim.obj.Vehicle;

public class NewRoadE extends NewObjE{
	protected String iniJ, finalJ;
	protected int vMax, length;
	private static final String NAME = "new_road";
	public static class NewRoadBuilder implements EventBuilder{
		public Event parse(IniSection s) {
			if(!s.getTag().equals(NAME)) return null;
			int arg1 = Integer.parseInt(s.getValue("time"));
			String arg2 = s.getValue("src");
			String arg3 = s.getValue("dest");
			int arg4 = Integer.parseInt(s.getValue("max_speed"));
			int arg5 = Integer.parseInt(s.getValue("length"));
			String arg6 = s.getValue("id");
			
			if(s.getKeys().size() == 6) //carretera simle
				return new NewRoadE(arg1, arg2, arg3, arg4, arg5, arg6);
			else if(s.getKeys().size() == 7) { //camino
				if(!s.getValue("type").equals("dirt")) return null;
				return new NewDirtE(arg1, arg2, arg3, arg4, arg5, arg6);
			}
			else {//autopista
				if(!s.getValue("type").equals("lanes")) return null;
				int arg7 = Integer.parseInt(s.getValue("lanes"));
				return new NewLaneE(arg7, arg1, arg2, arg3, arg4, arg5, arg6);
			}
		}
	}
	public NewRoadE(int time, String src, String dest, int vMax, int length, String id) {
		super(time, NAME, id);
		iniJ = src;
		finalJ = dest;
		this.vMax = vMax;
		this.length = length;
	}

	public void ejecuta(Simulator s, ArrayList<Junction> js, ArrayList<Road> rs, ArrayList<Vehicle> vs) throws MissingObjectExc {
		/*Este código crea nuevos cruces si no existen los dados
		 * 
		 * Junction iniJJ, finalJJ;
		if(!s.getRoadMap().getJunction(iniJ).equals(null)) //si iniJ ya existe en RoadMap
			iniJJ = s.getRoadMap().getJunction(iniJ);
		else iniJJ = new Junction(iniJ);
		if(!s.getRoadMap().getJunction(finalJ).equals(null)) //si la finalJ también
			finalJJ = s.getRoadMap().getJunction(finalJ);
		else finalJJ = new Junction(finalJ);
		Road r = new Road(vMax, length, iniJJ, finalJJ, id);*/
		if(done) return;
		if(s.getRoadMap().getJunction(iniJ) == null || s.getRoadMap().getJunction(finalJ) == null)
			throw new MissingObjectExc(this);
		Junction iniJJ = s.getRoadMap().getJunction(iniJ), finalJJ = s.getRoadMap().getJunction(finalJ);
		Road myRoad = new Road(vMax, length, iniJJ, finalJJ, id);
		finalJJ.addRoad(myRoad);
		rs.add(myRoad);
		done = true;
	}
}
