package es.ucm.fdi.util;

import java.io.IOException;

import org.junit.Test;

import es.ucm.fdi.launcher.ExampleMain;

public class MainTest {
	@Test
	public void test() {
		try {
			ExampleMain.test("D:\\EclipseJava\\Programas\\practicas\\p4\\src\\resources\\examples\\basic");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
