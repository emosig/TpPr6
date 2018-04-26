package es.ucm.model;

import es.ucm.sim.Simulator.UpdateEvent;

/*
 * Lo implementan las clases que se "suscriben" al simulador
 */
public interface SimulatorListener {

	void registered(UpdateEvent ue);
	void reset(UpdateEvent ue);
	void newEvent(UpdateEvent ue);
	void advanced(UpdateEvent ue);
	void error(UpdateEvent ue, String error);
}
