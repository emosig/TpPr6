package es.ucm.fdi.events;

import java.util.ArrayList;

import es.ucm.fdi.exceptions.MissingObjectExc;
import es.ucm.model.sim.Simulator;
import es.ucm.model.sim.obj.Junction;
import es.ucm.model.sim.obj.Lane;
import es.ucm.model.sim.obj.Road;
import es.ucm.model.sim.obj.Vehicle;

/*
 * 	Evento para nueva autopista
 */
public class NewLaneE extends NewRoadE{
	private int lanes;

	public NewLaneE(int lanes, int time, String src, String dest, int vMax, 
			int length, String id) {
		super(time, src, dest, vMax, length, id);
		this.lanes = lanes;
	}
	
	public void ejecuta(Simulator s, ArrayList<Junction> js, 
			ArrayList<Road> rs, ArrayList<Vehicle> vs) 
					throws MissingObjectExc {
		if(done) {
			return;
		}
		if(s.getRoadMap().getJunction(iniJ) == null 
				|| s.getRoadMap().getJunction(finalJ) == null) {
			throw new MissingObjectExc(this);
		}	
		Junction iniJJ = s.getRoadMap().getJunction(iniJ), 
				finalJJ = s.getRoadMap().getJunction(finalJ);
		Lane myLane = new Lane(lanes, vMax, length, iniJJ, finalJJ, id);
		finalJJ.addRoad(myLane);
		rs.add(myLane);
		done = true;
	}
}
