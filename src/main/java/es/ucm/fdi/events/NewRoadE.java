package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;
import es.ucm.sim.Simulator;

public class NewRoadE extends NewObjE{
	private String iniJ, finalJ;
	private int vMax, length;
	private static final String NAME = "new_road";
	private class NewRoadBuilder implements EventBuilder{
		public Event parse(IniSection s) {
			if(!s.getTag().equals(NAME)) return null;
			int arg1 = Integer.parseInt(s.getValue("time"));
			String arg2 = s.getValue("src");
			String arg3 = s.getValue("dest");
			int arg4 = Integer.parseInt(s.getValue("max_speed"));
			int arg5 = Integer.parseInt(s.getValue("length"));
			String arg6 = s.getValue("id");
			return new NewRoadE(arg1, arg2, arg3, arg4, arg5, arg6);
		}

		@Override
		public boolean isvalid(String id) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	public NewRoadE(int time, String src, String dest, int vMax, int length, String id) {
		super(time, NAME, id);
		iniJ = src;
		finalJ = dest;
		this.vMax = vMax;
		this.length = length;
	}

	public void ejecuta(Simulator s) {	
	}
}
