package es.ucm.model.sim.obj;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.exceptions.IdException;

/*
 * 	Vehículo
 */
public class Vehicle extends SimObj{
	
	/*
	 * Me creo un comparador para que se ordenen los vehículos como yo quiero
	 */
	public static class VehicleComparator implements Comparator<Vehicle>{

		@Override
		public int compare(Vehicle o1, Vehicle o2) {
			if(o1.getLoc() == o2.getLoc())
				return o1.getId().compareToIgnoreCase(o2.getId());
			else return o2.getLoc() - o1.getLoc();
		}
	} 
	
	private int localizacion;
	private ArrayList<Road> itinerario;
	private int posItinerario; //número de carretera
	private Boolean haLlegado;
	protected int velMaxima;
	protected int velActual;
	protected int tAveria;
	protected int kilometrage() {
		int k = 0;
		for(int i = 0; i < posItinerario; ++i) 
			k+= itinerario.get(i).getLong();
		k += localizacion;
		return k;
	}
	
	public Vehicle(String id) {
		super(id);
		velMaxima = 0;
		localizacion = 0;
		itinerario = new ArrayList<>();
		posItinerario = 0;
		tAveria = 0;
		haLlegado = false;
	}
	public Vehicle(int vMax, ArrayList<Road> it, String id) {
		super(id);
		velMaxima = vMax;
		localizacion = 0;
		itinerario = it;
		posItinerario = 0;
		tAveria = 0;
		haLlegado = false;
	}
	public int getLoc() {
		return localizacion;
	}
	public String getRoad() {
		return itinerario.get(posItinerario).getId();
	}
	public Road getActualRoad() {
		return itinerario.get(posItinerario);
	}
	public boolean getLlegado() {
		return haLlegado;
	}
	public int getTAveria() {
		return tAveria;
	}
	public void setTiempoAveria(int t){
		if(tAveria < 1) velActual = 0; //Si no estaba parado lo para
		tAveria += t;
	}
	public void setVelocidadActual(int v) {
		if(tAveria > 0) velActual = 0;
		else if(v < velMaxima) velActual = v;
		else velActual = velMaxima;
	}
	/*
	 * La cola que incluyo como parámetro es para hacer salir los 
	 * vehiculos sin romper el iterador innerValues en la carretera
	 */
	public void avanza(List<Vehicle> queue) {
		if(tAveria > 0) {
			//está averiado
			--tAveria;
			velActual = 0;
		}	
		else {
			localizacion += velActual;
			if(itinerario.get(posItinerario).getLong() <= localizacion) {
				//localizacion al final de la carretera
				localizacion = itinerario.get(posItinerario).getLong(); 
				//se apunta en la cola para salir de la caretera
				queue.add(this); 
				//entra en cruce
				itinerario.get(posItinerario).getFinalJ().entraVehiculo(this); 
				velActual = 0;
			}
		}
	}
	/*
	 * Comprueba si al salir de la carretera acaba su recorrido, y si no pasa 
	 * a la siguiente carretera
	 */
	public void moverASiguienteCarretera() throws IdException {
		if(posItinerario + 1 == itinerario.size()) {
			haLlegado = true;
			itinerario.get(posItinerario).saleVehiculo(this);
			return;
		}
		++posItinerario;
		itinerario.get(posItinerario).entraVehiculo(this);
		localizacion = 0;
	}
	@Override
	protected void fillReportDetails(Map<String, String> out) {
		if(haLlegado) out.put("location", "arrived");
		else out.put("location", new StringBuilder().append('(')
				.append(itinerario.get(posItinerario).getId()).append(',')
				.append(localizacion).append(')').toString());
 		out.put("speed", String.valueOf(velActual));
		out.put("faulty", String.valueOf(tAveria));
		out.put("kilometrage", String.valueOf(kilometrage()));
		
	}

	@Override
	protected String getReportHeader() {
		return "vehicle_report";
	}
	
	@Override
	protected void describeFurther(Map<String, String> out) {
		out.put("Road", itinerario.get(posItinerario).getId());
		out.put("Location", Integer.toString(localizacion));
		out.put("Speed", Integer.toString(velActual));
		out.put("Km", Integer.toString(kilometrage()));
		out.put("Faulty Units", Integer.toString(tAveria));
		StringBuilder sb = new StringBuilder("[");
		if(!itinerario.isEmpty()) {
		sb.append(itinerario.get(0).getIniJ().getId());
			for(Road r: itinerario)
				sb.append(r.getFinalJ().getId()).append(",");
			sb.setLength(sb.length() - 1);
		}
		sb.append("]");
		out.put("Itinerary", sb.toString());
	}
}
