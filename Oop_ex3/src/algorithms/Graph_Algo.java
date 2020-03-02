package algorithms;

import java.util.List;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;

import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import oop_dataStructure.oop_node_data;
import oop_utils.OOP_Point3D;
/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo{
	
	private oop_graph g;
	/*
	 * graph constructor
	 * @param g
	 */
	public Graph_Algo (oop_graph g) {
		init(g);
	}
	/**
	 * get graph
	 * @return
	 */
	public oop_graph getGraph() {
		return g;
	}

	public void init(oop_graph g) {
		this.g = g;
		
	}

	
	public void init(String file_name) {
		try {
			FileInputStream filein = new FileInputStream(file_name);
			ObjectInputStream in = new ObjectInputStream(filein);
			g = (OOP_DGraph) in.readObject();
			in.close();
			filein.close();
		}
		catch (Exception e) {
			System.out.println("Can't load the file.");
			e.printStackTrace();
		}
	}

	
	public void save(String file_name) {
		if (g instanceof OOP_DGraph) {
			try {
				FileOutputStream fileout = new FileOutputStream (file_name);
				ObjectOutputStream out = new ObjectOutputStream (fileout);
				out.writeObject(g);
				out.close();
				fileout.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
			System.out.println("Can only save OOP_DGraph type");
	}

	
	public boolean isConnected() {
		Collection<oop_node_data> n = g.getV();
		dijakstra (n.iterator().next());
		for (oop_node_data v :g.getV()) {
			if (v.getWeight() == Double.POSITIVE_INFINITY) //if a Node's weight is infinite after using dijakstra function, it's mean that it's not connected to the rest
				return false;
		}
		return true;
	}

	
	public double shortestPathDist(int src, int dest) {
		dijakstra(g.getNode(src));
		oop_node_data n = g.getNode(dest);
		return n.getWeight();
	}

	
	public List<oop_node_data> shortestPath(int src, int dest) {
		oop_node_data u = g.getNode(src);
		oop_node_data v = g.getNode(dest);
		dijakstra(u);
		ArrayList<oop_node_data> ans = new ArrayList<>();
		while(v.getKey() != u.getKey()) {
			ans.add(v);
			v = g.getNode(v.getTag());
		}
		ans.add(u);
		return ans;
	}

	
	public List<oop_node_data> TSP(List<Integer> targets) {
		ArrayList<oop_node_data> ans = new ArrayList<>();
		int firstn = targets.get(0);
		for (int i = 1; i < targets.size(); i++) {
			int currentn = targets.get(i);;
			List<oop_node_data> path = shortestPath(firstn, currentn);
			ans.addAll(path);
			firstn = currentn;
		}
		return ans;
	}

	
	/*public oop_graph copy() {
		oop_graph clone = new OOP_DGraph();
		Collection<oop_node_data> v = this.g.getV();
		Collection<oop_node_data> c = new ArrayList<oop_node_data>();
		for(oop_node_data i: v) {
			c.add(((OOP_NodeData) i).clone());
		}
		for(oop_node_data a :c ) {
			clone.addNode(a);
		}
		return clone;
	}*/
	
	/**
	 * Function for Dijkstra's Algorithm 
	 * @param src
	 */
	public void dijakstra (oop_node_data src) {
		ArrayList<oop_node_data> q = new ArrayList<>();
		for (oop_node_data v :g.getV()) {
			v.setWeight(Double.POSITIVE_INFINITY);
			v.setTag(-1);// -1 == UNDEFINED
			q.add(v);
		}
		src.setWeight(0);
		while (!q.isEmpty()) {
			oop_node_data u = minWeight(q);
			q.remove(u);
			for (oop_edge_data e : g.getE(u.getKey()) ) {
				double alt = u.getWeight() + e.getWeight();
				oop_node_data v = g.getNode(e.getDest());
				if (alt < v.getWeight()) {
					v.setWeight(alt);
					v.setTag(u.getKey());
				}
			}
		}
	}
	
	/**
	 * Sub-function for Dijkstra to get minimum weight
	 * @param q
	 * @return
	 */
	public oop_node_data minWeight(ArrayList<oop_node_data> q) {
		oop_node_data ans = q.get(0);
		for (int i = 1; i < q.size(); i++) {
			if (q.get(i).getWeight()<ans.getWeight())
				ans = q.get(i);
		}
		return ans;
	}
	
	public double getLeft() {
		double l = Double.POSITIVE_INFINITY;
		for (oop_node_data v :g.getV()) {
			OOP_Point3D p = v.getLocation();
			if (p != null && p.x() < l) {
				l = p.x();
			}
		}
		return l;
	}
	
	public double getRight() {
		double r = Double.NEGATIVE_INFINITY;
		for (oop_node_data v :g.getV()) {
			OOP_Point3D p = v.getLocation();
			if (p != null && p.x() > r) {
				r = p.x();
			}
		}
		return r;
	}
	
	public double getTop() {
		double t = Double.NEGATIVE_INFINITY;
		for (oop_node_data v :g.getV()) {
			OOP_Point3D p = v.getLocation();
			if (p != null && p.y() > t) {
				t = p.y();
			}
		}
		return t;
	}
	
	public double getBottom() {
		double b = Double.POSITIVE_INFINITY;
		for (oop_node_data v :g.getV()) {
			OOP_Point3D p = v.getLocation();
			if (p != null && p.y() < b) {
				b = p.y();
			}
		}
		return b;
	}
}

