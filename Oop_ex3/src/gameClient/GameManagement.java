package gameClient;

import java.util.ArrayList;

import Server.User;
import Server.Users;
import Server.game_service;
import Server.robot;

public class GameManagement {
	private User u;
	private ArrayList<User> users = new ArrayList<User>();
	public GameManagement (User u) {
		this.u = u;
		boolean listed = false;
		for (User user : users) {
			if (user == u) {
				listed = true;
				break;
			}
		}
		if (!listed)
			users.add(u);
	}
	
	public void autoDrive(User u){
		User auto = new User(null);
		u = auto;
	}
	
	public void serverEvent(game_service event){
		
	}
	
	
}
