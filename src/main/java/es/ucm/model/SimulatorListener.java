package es.ucm.model;

import es.ucm.sim.Simulator.UpdateEvent;

public interface SimulatorListener {
	
	//void update(UpdateEvent ue, String error);

	void registered(UpdateEvent ue);
	void reset(UpdateEvent ue);
	void newEvent(UpdateEvent ue);
	void advanced(UpdateEvent ue);
	void error(UpdateEvent ue, String error);
}
