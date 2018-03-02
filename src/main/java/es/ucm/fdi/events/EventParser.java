package es.ucm.fdi.events;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.util.MultiTreeMap;

public class EventParser {
	private static Event[] evs = {
			new MakeVehicleFaultyE(), new NewJunctionE(), new NewRoadE(), new NewVehicleE()
	};
	
	public Event which(IniSection s) {
		for(Event e : evs)
			if(e.getName().equals(s.getTag()))
				return e.read(s);
		return null; //y si le meto una exception por aquí?
	}
	
	public MultiTreeMap<Integer, Event> genEventMap(Path path) throws IOException {
		MultiTreeMap<Integer, Event> map = new MultiTreeMap<Integer, Event>(); //no lo declaro como map porque necesito putValue()
		InputStream s = Files.newInputStream(path); //de esto no estoy muy seguro yo
		Ini ini = new Ini(s);
		for (IniSection sec : ini.getSections()) {
			Event e = which(sec);
			if(!e.equals(null))
				map.putValue(e.getTime(), e);
		}
		return map;
	}
}
