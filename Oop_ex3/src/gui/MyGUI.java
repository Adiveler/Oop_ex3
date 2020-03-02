package gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import algorithms.Graph_Algo;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import oop_dataStructure.oop_node_data;
import oop_utils.OOP_Point3D;

public class MyGUI {
	private oop_graph g;
	private double left, right, top, bottom;
	public MyGUI (oop_graph g) {
		this.g = g;
		Graph_Algo algo = new Graph_Algo(g);
		this.left = algo.getLeft()-0.001;
		this.right = algo.getRight()+0.001;
		this.top = algo.getTop()+0.001;
		this.bottom = algo.getBottom()-0.001;
	}
	
	
	/** convert polar point to screen
	 * @param p - polar point
	 * @return point to show on screen   */
	private OOP_Point3D polarToScreen(OOP_Point3D p) {
		double w = this.right - this.left;
		double x = (p.x()-this.left)/w;
		double h = this.top - this.bottom;
		double y = (p.y()-this.bottom)/h; 
		return new OOP_Point3D(x,y);
	}
	/**
	 * draw the graph, nodes in blue and edges in red
	 */
	public void draw () {
		//draw edges
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.RED);
		for (oop_node_data n : g.getV()) {
			Collection<oop_edge_data> edges = g.getE(n.getKey());
			for (oop_edge_data e : edges) {
				oop_node_data u = g.getNode(e.getDest());
				OOP_Point3D pn = n.getLocation();
				OOP_Point3D pu = u.getLocation();
				pn = polarToScreen(pn);
				pu = polarToScreen(pu);
				StdDraw.line(pn.x(), pn.y(), pu.x(), pu.y());
			}
		}
		//draw nodes
		StdDraw.setPenRadius(0.05);
		StdDraw.setPenColor(StdDraw.BLUE);
		
		for (oop_node_data n : g.getV()) {
			OOP_Point3D p = n.getLocation();
			p = polarToScreen(p);
			StdDraw.point(p.x(), p.y());
		}
	}
	
	public void drawFruits(ArrayList<OOP_Point3D> fruits) {
		for (OOP_Point3D point : fruits) {
			point = polarToScreen(point);
			StdDraw.picture(point.x(), point.y(), "data/fruit.png");
		}
	}
	
	public void drawRobots(OOP_Point3D point) {
		point = polarToScreen(point);
		StdDraw.picture(point.x(), point.y(), "data/robot.png");
	}
	
	public void clear () {
		StdDraw.clear();
	}
	
	public void drawTime(long time) {
		StdDraw.text(0.5, 0.98, ""+time);
	}
	/**
	 * Get an input from user's keyboard: s for save, l for load, q for quit
	 * @throws InterruptedException
	 */
	public void inputkeyboard() throws InterruptedException {
		Graph_Algo ga = new Graph_Algo(g);
		Scanner sc = new Scanner(System.in);
		char c = 0;
		while (c != 'q') {
			
			if (StdDraw.hasNextKeyTyped()) {
				c = StdDraw.nextKeyTyped();
			} 
			
			switch (c) {
			case 's':
			    System.out.println("Enter file name");
			    String file_name = sc.nextLine();
				ga.save(file_name);
				System.out.println("save");
				break;

			case 'l':
			    System.out.println("Enter file name");
			    String fileload = sc.nextLine();
				ga.init(fileload);
				g = ga.getGraph();
				draw();
				System.out.println("load");
				break;
				
			case 'q':
				System.out.println("quit");
				break;
				
			default:
				break;
			}
			
			c = 0;
			Thread.sleep(15);
			
		}
		sc.close();
	}
}
