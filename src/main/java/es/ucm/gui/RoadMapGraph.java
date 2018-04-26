package es.ucm.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import es.ucm.fdi.extra.graphlayout.Dot;
import es.ucm.fdi.extra.graphlayout.Edge;
import es.ucm.fdi.extra.graphlayout.Graph;
import es.ucm.fdi.extra.graphlayout.GraphComponent;
import es.ucm.fdi.extra.graphlayout.Node;
import es.ucm.sim.RoadMap;
import es.ucm.sim.obj.Junction;
import es.ucm.sim.obj.Road;

/*
 * Componente de swing para el roadmap gráfico
 */
public class RoadMapGraph extends JPanel{
	private GraphComponent _graphComp;
	private RoadMap rm;
	
	public RoadMapGraph(RoadMap r) {
		super(new BorderLayout());
		_graphComp = new GraphComponent();
		rm = r;
		generateGraph();
		add(_graphComp);
	}
	
	private void generateGraph() {
		Graph g = new Graph();
		Map<Junction, Node> js = new HashMap<>();
		for (Junction j : rm.getJunctions()) {
			Node n = new Node(j.getId());
			js.put(j, n); // <-- para convertir Junction a Node en aristas
			g.addNode(n);
		}
		for (Road r : rm.getRoads()) {
			Edge e = new Edge(r.getId(), js.get(r.getIniJ()), 
					js.get(r.getFinalJ()), r.getLong() );
			g.addEdge(e);
		}
		_graphComp.setGraph(g);
	}
}
