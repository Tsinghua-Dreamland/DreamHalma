package cn.edu.tsinghua.ee.Dreamland.DreamHalma.model;

import java.util.HashSet;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.Configure;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.Chess;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.DreamHalmaException;

//class to show the states of the game.
public class State implements Serializable {
	
	private static final Log LOG = LogFactory.getLog(State.class);
	private Configure configure;
	
	private HashSet<Chess> chesses = new HashSet<Chess>(); // a HashSet of all the chess pieces
	private boolean gameOn; //a marker to make sure whether the game is still on going, for the clean up need
	private int totalPlayers; // keep record of the total players in the game
	
	public State() throws Exception{
		configure = new Configure();
		configure.setConfigure();
	}
	
	public HashSet<Chess> getChesses(){
		return this.getChesses();
	}
	
	public boolean getGameOn(){
		return this.gameOn;
	}
	
	//initiate points mainly, using the configuration
	public void init() throws Exception{
		LOG.info("state initiating");
		totalPlayers = Integer.parseInt(configure.getProperty("total_players"));
		if ((totalPlayers!=2)&&(totalPlayers!=3)&&(totalPlayers!=6)){
			throw new DreamHalmaException("player number no correct, we only allow 2,3 or 6 players");
		} else {
			this.initiateChess();
			gameOn = true;
		}
		LOG.info("state initiated");
	}
	
	//function to initiate the chesses according to player number
	private void initiateChess(){
		for ( int player=1; player<totalPlayers; player=player+1){
			for (int i=0;i<10;i++){
				int vert=i;
				int horiz=i;
				chesses.add(new Chess(vert, horiz, player));
			}
		}
	}
	
	//for test purpose, clean this up for release
	public String printChess(){
		String s = "";
		for (Chess i: chesses){
			s = s + i.printChess()+"   ";
		}
		return s;
	}
}
