package gameClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import gui.MyGUI;
import gui.StdDraw;
import oop_dataStructure.OOP_DGraph;
import oop_dataStructure.oop_edge_data;
import oop_dataStructure.oop_graph;
import oop_utils.OOP_Point3D;

public class MyGameGUI {

	public void start(boolean manual) {
		
		//Start the game and build the graph
		int scenario_num = 2;
		game_service game = Game_Server.getServer(scenario_num); // you have [0,23] games
		String graphjson = game.getGraph();
		OOP_DGraph graph = new OOP_DGraph();
		graph.init(graphjson);
		MyGUI gui = new MyGUI (graph);

		gui.draw();
		// place fruit
		showFruit(game, gui);
		// place robot
		String info = game.toString();
		JSONObject line;
		try {
			line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int robot_size = ttt.getInt("robots");
			for(int a = 0;a<robot_size;a++) {
				game.addRobot(a);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
		game.startGame();
		// should be a Thread!!!
		while(game.isRunning()) {
			StdDraw.enableDoubleBuffering();
			gui.clear();
			gui.draw();
			showFruit(game, gui);
			
			moveRobots(game, graph, gui, manual);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			StdDraw.show();
		}
		String results = game.toString();
		System.out.println("Game Over: "+results);
	}
	
	private void moveRobots(game_service game, oop_graph graph, MyGUI gui, boolean manual) {
		List<String> jrobots = game.move();
		if(jrobots!=null) { //the game isn't finished yet
			long time = game.timeToEnd();
			for(int i=0;i<jrobots.size();i++) {
				String robot_json = jrobots.get(i);
				try {
					JSONObject line = new JSONObject(robot_json);
					JSONObject ttt = line.getJSONObject("Robot");
					int rid = ttt.getInt("id");
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
					OOP_Point3D point = new OOP_Point3D(ttt.getString("pos"));
					gui.drawRobots(point);
					gui.drawTime(time);
					if(dest==-1) {	//Robot arrived to dest
						if (manual) dest = nextNodeManual(graph, src);
						else dest = nextNodeAuto(graph, src);
						game.chooseNextEdge(rid, dest);
					}
				} 
				catch (JSONException e) {e.printStackTrace();}
			}
		}
	}
	
	/**
	 * a very simple random walk implementation!
	 * @param g
	 * @param src
	 * @return
	 */
	private int nextNode(oop_graph g, int src) {
		int ans = -1;
		Collection<oop_edge_data> edges = g.getE(src);
		Iterator<oop_edge_data> itr = edges.iterator();
		int s = edges.size();
		int r = (int)(Math.random()*s);
		int i=0;
		while(i<r) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}
	
	private int nextNodeManual (oop_graph g, int src) {
		int ans = -1;
		Collection<oop_edge_data> edges = g.getE(src);
		Iterator<oop_edge_data> itr = edges.iterator();
		char userInput = 0;
		while (userInput < '0' || userInput > '0'+edges.size()-1) {
            if (StdDraw.hasNextKeyTyped()) { //check for keyboard input
                userInput = StdDraw.nextKeyTyped();
            }
		}
		int num = Character.getNumericValue(userInput);
		int i = 0;
		while(i<num) {itr.next();i++;}
		ans = itr.next().getDest();
		return ans;
	}
	
	private int nextNodeAuto (oop_graph g, int src) {
		int ans = -1;
		Collection<oop_edge_data> edges = g.getE(src);
		Iterator<oop_edge_data> itr = edges.iterator();
		return ans;
	}

	private ArrayList<OOP_Point3D> jFruitsToPoint (List<String> fruits) throws JSONException {
		ArrayList<OOP_Point3D> flist = new ArrayList<>();
		for (String str : fruits) {
			JSONObject obj = new JSONObject(str);
			JSONObject fruitobj = obj.getJSONObject("Fruit");
			String pos = fruitobj.getString("pos");
			String [] points = pos.split(",");
			double x = Double.parseDouble(points[0]);
			double y = Double.parseDouble(points[1]);
			OOP_Point3D fpos = new OOP_Point3D(x,y);
			flist.add(fpos);
		}
		return flist;
	}
	
	private void showFruit (game_service game, MyGUI gui) {
		List<String> fruits = game.getFruits();
		try {
			ArrayList<OOP_Point3D> fpos = jFruitsToPoint(fruits);
			gui.drawFruits(fpos);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MyGameGUI game = new MyGameGUI();
		game.start(true);
	}

}
