package cn.edu.tsinghua.ee.Dreamland.DreamHalma.model;

import java.util.HashSet;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.Configure;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.Chess;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.DreamHalmaException;

//class to show the states of the game.
public class State {
	private Configure configure;
	private HashSet<Chess> chesses = new HashSet<Chess>();
	private int totalPlayers;
	
	public State() throws Exception{
		configure = new Configure();
		configure.setConfigure();
	}
	
	//initiate points mainly, using the configuration
	public void init() throws Exception{
		totalPlayers = Integer.parseInt(configure.getProperty("total_players"));
		if ((totalPlayers!=2)&&(totalPlayers!=3)&&(totalPlayers!=6)){
			throw new DreamHalmaException("player number no correct, we only allow 2,3 or 6 players");
		}
		
	}
}
