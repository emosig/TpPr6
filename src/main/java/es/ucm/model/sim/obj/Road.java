package es.ucm.model.sim.obj;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.lang.Math.toIntExact;

import es.ucm.fdi.exceptions.IdException;
import es.ucm.fdi.util.MultiTreeMap;

/*
 * 	Carretera
 */
public class Road extends SimObj{
	protected MultiTreeMap<Integer, Vehicle> vehiculos;
	protected int velocidadMax;
	protected int velocidadBase;
	//cola auxiliar para sacar coches de la carretera sin romper el 
	//iterador de multitreemap
	protected List<Vehicle> abandonQueue = new ArrayList<>();
	private int longitud;
	private Junction iniJ, finalJ;
	private int factorReducc(Vehicle v) {
		for(Vehicle veh:vehiculos.innerValues()) {
			if(veh.getTAveria() > 0 && !veh.equals(v)) {
				if(veh.getLoc() >= v.getLoc()) {
					return 2;
				}
			}
		}
		return 1;
	}
	
	public Road(int vMax, int length, Junction iniJ, Junction finalJ, 
			String id) {
		super(id);
		velocidadMax = vMax;
		longitud = length;
		this.iniJ = iniJ;
		this.finalJ = finalJ;
		vehiculos = new MultiTreeMap<>();
	}
	
	public Road(String id) {
		super(id);
	}
	
	public int getLong() {
		return longitud;
	}
	
	public Junction getIniJ() {
		return iniJ;
	}
	
	public Junction getFinalJ() {
		return finalJ;
	}
	/*
	 * Antes de meter el vehiculo comprueba que no está repetido
	 */
	public void entraVehiculo(Vehicle v) throws IdException {
		boolean yaDentro = false;
		for(Vehicle veh: vehiculos.innerValues()) {
			if(veh.getId().equals(v.getId())){
				yaDentro = true;
			}
		}
		if(yaDentro) {
			throw new IdException("Ya existe el vehiculo en la carretera", "vehiculo");
		}
		vehiculos.putValue(0, v);
	}
	public boolean saleVehiculo(Vehicle v) {

		/*
		 * "Es la hora de partir. Oh abandonado!"
		 * 		
		 * 		Pablo Neruda
		 */
		
		return vehiculos.removeValue(0, v);
	}
	/*
	 * Auxiliar para escribir el report
	 */
	protected List<Vehicle> sortVehicles(){
		List<Vehicle> list = vehiculos.valuesList();
		return list;
	}
	
	public int calcVBase() {
		int l = toIntExact(vehiculos.sizeOfValues());
		if(l > 1) {
			return Integer.min(velocidadMax,velocidadMax/l + 1);
		}
		else return velocidadMax;
	}
	
	public void avVehicles() {
		//empleo la cola auxiliar abandonQueue para no "romper" vehiculos 
		//mientras está iterando
		for(Vehicle v: vehiculos.innerValues()) {
			if(v.getTAveria() == 0) {
				v.setVelocidadActual(velocidadBase/factorReducc(v));
			}
			v.avanza(abandonQueue);
		}
		for(Vehicle gone: abandonQueue) {
			if(gone.getLlegado()) {
				saleVehiculo(gone);
			}
		}
	}
	
	public void avanza() {
		velocidadBase = calcVBase();
		avVehicles();
		
	}
	@Override
	protected void fillReportDetails(Map<String, String> out) {
		if(!vehiculos.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for(Vehicle v : sortVehicles()) {
				sb.append('(').append(v.getId()).append(',')
				.append(v.getLoc()).append("),");	
			}
			sb.setLength(sb.length() - 1); //elimino la ultima coma
			out.put("state", String.join(",", sb));
		}
		else out.put("state", "");
	}

	@Override
	protected String getReportHeader() {
		return "road_report";
	}

	@Override
	protected void describeFurther(Map<String, String> out) {
		out.put("Source", iniJ.getId());
		out.put("Target", finalJ.getId());
		out.put("Length", Integer.toString(longitud));
		out.put("Max Speed", Integer.toString(velocidadMax));
		StringBuilder sb = new StringBuilder("[");
		if(!vehiculos.valuesList().isEmpty()) {
			for(Vehicle v: vehiculos.valuesList()) {
				sb.append(v.getId()).append(",");
			}
			sb.setLength(sb.length() - 1);
		}
		sb.append("]");
		out.put("Vehicles", sb.toString());
	}

	
}