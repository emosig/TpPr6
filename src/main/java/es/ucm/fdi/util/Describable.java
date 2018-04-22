package es.ucm.fdi.util;

import java.util.Map;

public interface Describable {
	/**
	* @param out - a map to fill in with key- value pairs
	* @return the passed- in map, with all fields filled out.
	*/
	void describe(Map<String, String> out);
}
