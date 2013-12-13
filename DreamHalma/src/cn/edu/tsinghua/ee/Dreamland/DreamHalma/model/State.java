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
	
	// a HashSet of all the chess pieces
	private HashSet<Chess> chesses = new HashSet<Chess>(); 
	//a marker to make sure whether the game is still on going, for the clean up need
	private boolean gameOn; 
	// keep record of the total players in the game
	private int totalPlayers; 
	
	public State() throws Exception{
		configure = new Configure();
		configure.setConfigure();
	}
	
	public HashSet<Chess> getChesses(){
		return this.chesses;
	}
	
	public boolean getGameOn(){
		return this.gameOn;
	}
	
	//initiate points mainly, using the configuration
	public void init() throws Exception{
		LOG.info("state initiating");
		totalPlayers = Integer.parseInt(configure.getProperty("total_players"));
		//according to the rule, only 2,3,4,or 6 players are accepted
		if ((totalPlayers!=2)&&(totalPlayers!=3)&&(totalPlayers!=4)&&(totalPlayers!=6)){
			throw new DreamHalmaException("player number no correct, we only allow 2,3 or 6 players");
		} else {
			//initiate all the chess pieces according to how many players
			this.initiateChess();
			gameOn = true;
		}
		LOG.info("state initiated");
	}
	
	//function to initiate the chesses according to player number
	private void initiateChess(){
		switch(this.totalPlayers){
		case 2:{
			int count=4;//the total layer of chesses is 4
			//for player 1
			int temp=-5;
			int begin=1;int end=4;
			while(count>0){
				for(int i=begin;i<=end;i++)
					chesses.add(new Chess(i, temp, 1));
				temp=temp-1;begin++;count--;
			}
			//for player 2
			count=4;temp=5;begin=0;end=3;
			while(count>0){
				for(int i=begin;i<=end;i++)
					chesses.add(new Chess(i-4, temp, 2));
				temp=temp+1;end--;count--;
			}
		}
		case 3:{
			int count=4;//the total layer of chesses is 4
			//for player 1
			int temp=-5;
			int begin=1;int end=4;
			while(count>0){
				for(int i=begin;i<=end;i++)
					chesses.add(new Chess(i, temp, 1));
				temp=temp-1;begin++;count--;
			}
			//for player 2
			count=4;temp=4;begin=0;end=3;
			while(count>0){
				for(int i=begin;i<=end;i++)
					chesses.add(new Chess(i+1, temp, 2));
				temp=temp-1;begin++;count--;
			}
			//for player 3
			count=4;temp=4;begin=0;end=3;
			while(count>0){
				for(int i=begin;i<=end;i++)
					chesses.add(new Chess(i-8, temp, 3));
				temp=temp-1;begin++;count--;
			}
		}
		case 6:{
			int count=4;//the total layer of chesses is 4
			//for player 1
			int temp=-5;
			int begin=1;int end=4;
			while(count>0){
				for(int i=begin;i<=end;i++)
					chesses.add(new Chess(i, temp, 1));
				temp=temp-1;begin++;count--;
			}
			//for player 2
			count=4;temp=4;begin=0;end=3;
			while(count>0){
				for(int i=begin;i<=end;i++)
					chesses.add(new Chess(i+1, temp, 2));
				temp=temp-1;begin++;count--;
			}
			//for player 3
			count=4;temp=4;begin=0;end=3;
			while(count>0){
				for(int i=begin;i<=end;i++)
					chesses.add(new Chess(i-8, temp, 3));
				temp=temp-1;begin++;count--;
			}
			//for player 4
			count=4;temp=5;begin=0;end=3;
			while(count>0){
				for(int i=begin;i<=end;i++)
					chesses.add(new Chess(i-4, temp, 4));
				temp=temp+1;end--;count--;
			}
			//for player 5
			count=4;temp=-4;begin=0;end=3;
			while(count>0){
				for(int i=begin;i<=end;i++)
					chesses.add(new Chess(i-4, temp, 5));
				temp=temp+1;end--;count--;
			}
			//for player 6
			count=4;temp=-4;begin=0;end=3;
			while(count>0){
				for(int i=begin;i<=end;i++)
					chesses.add(new Chess(i+5, temp, 5));
				temp=temp+1;end--;count--;
			}
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
