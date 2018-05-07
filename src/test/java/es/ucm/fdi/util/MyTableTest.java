package es.ucm.fdi.util;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import es.ucm.sim.obj.Junction;

public class MyTableTest {
	private static JFrame frame;
	private static MyTable table;
	private static MyTable anothertable;
	private static MyTable anotheranothertable;
	private static List<Junction> junctions;
	private static List<Junction> junctions2;
	private static List<Junction> junctions3;
	
	public static void main(String[] args){
		frame = new JFrame();
		
		
		//test1();
		//test2();
		//test3();
		//test4();
		test5();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(720, 480);
		frame.setVisible(true);
	}
	
	private static void test1() {
		junctions = new ArrayList<>();
		junctions.add(new Junction("j1"));
		junctions.add(new Junction("j2"));
		junctions.add(new Junction("j3"));
		table = new MyTable(junctions);
		frame.add(table, BorderLayout.CENTER);
	}
	
	private static void test2() {
		table = new MyTable();
		JScrollPane pane = new JScrollPane(table);
		frame.add(pane);
		test1();
	}
	
	private static void test3() {
		table = new MyTable();
		frame.add(table, BorderLayout.CENTER);
		
		/* Esto no se refleja en la tabla!!!
		junctions = new ArrayList<>();
		junctions.add(new Junction("j1"));
		junctions.add(new Junction("j2"));
		junctions.add(new Junction("j3"));
		table.initTable(junctions);
		*/
		
		junctions = new ArrayList<>();
		junctions.add(new Junction("j1"));
		junctions.add(new Junction("j2"));
		junctions.add(new Junction("j3"));
		table = new MyTable(junctions);
		frame.add(table, BorderLayout.CENTER);
	}
	
	private static void test4() {
		junctions = new ArrayList<>();
		junctions.add(new Junction("j1"));
		junctions.add(new Junction("j2"));
		junctions.add(new Junction("j3"));
		table = new MyTable(junctions);
		
		junctions2 = new ArrayList<>();
		junctions2.add(new Junction("k1"));
		junctions2.add(new Junction("k2"));
		junctions2.add(new Junction("k3"));
		anothertable = new MyTable(junctions2);
		
		anotheranothertable = new MyTable();
		
		JPanel panel = new JPanel(new GridLayout(3, 1));
		panel.add(table);
		panel.add(anothertable);
		panel.add(anotheranothertable);
		
		frame.add(panel, BorderLayout.CENTER);
		
		junctions3 = new ArrayList<>();
		junctions3.add(new Junction("l1"));
		junctions3.add(new Junction("l2"));
		junctions3.add(new Junction("l3"));
		anotheranothertable = new MyTable(junctions3);
		
		panel = new JPanel(new GridLayout(3, 1));
		panel.add(table);
		panel.add(anothertable);
		panel.add(anotheranothertable);
		
		frame.add(panel, BorderLayout.CENTER);
	}
	
	private static void test5() {
		table = new MyTable();
		JScrollPane pane = new JScrollPane(table);
		frame.add(pane);
		
		junctions = new ArrayList<>();
		junctions.add(new Junction("j1"));
		junctions.add(new Junction("j2"));
		junctions.add(new Junction("j3"));
		
		table = new MyTable(junctions);
		pane = new JScrollPane(table);
		frame.add(pane);
	}
}