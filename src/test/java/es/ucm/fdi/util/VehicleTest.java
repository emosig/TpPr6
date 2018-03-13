package es.ucm.fdi.util;

import org.junit.Test;

import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;
import es.ucm.sim.obj.Vehicle;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;


/*
 * Unit tests for Vehicle
 */

public class VehicleTest {
	@Test
	public void test() throws FileNotFoundException {
		//Pseudo RoadMap
		ArrayList<Road> it = new ArrayList<>();
		Junction j1 = new Junction("j1");
		Road a = new Road(60, 140, null, j1, "a");
		Road b = new Road(60, 40, j1, null, "a");
		it.add(a);
		it.add(b);
		Vehicle v1 = new Vehicle(50, it, "v1");
		v1.setTiempoAveria(1);
		
		v1.avanza();
		assertEquals(0, v1.getLoc()); //no se ha movido
		
		v1.avanza();
		assertEquals(50, v1.getLoc());
		
		v1.avanza();
		assertEquals(a.getLong(), v1.getLoc()); //fin de carretera
		
		v1.moverASiguienteCarretera();
		assertEquals(0, v1.getLoc());
		assertEquals(b.getId(), v1.getRoad()); //se ha movido de carretera
		
		v1.avanza();
		assertTrue(v1.getLlegado());
		
		/*OutputStream out = new FileOutputStream(new File("src/main/resources/writeStr/out.ini"));
		Vehicle v2 = new Vehicle(50, it, "v2");
		v2....tu puta madre*/
	}
}
