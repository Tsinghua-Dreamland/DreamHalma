package cn.edu.tsinghua.ee.Dreamland.DreamHalma.model;

import java.util.HashSet;
import java.util.regex.Pattern;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.Configure;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.Chess;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.DreamHalmaException;

//class to show the states of the game.
@SuppressWarnings("serial")
public class State implements Serializable {
	
	private static final Log LOG = LogFactory.getLog(State.class);
	private Configure configure;
	
	// a HashSet of all the chess pieces
	private HashSet<Chess> chesses = new HashSet<Chess>(); 
	//a marker to make sure whether the game is still on going, for the clean up need
	private boolean gameOn = true; 
	// keep record of the total players in the game
	private int totalPlayers; 
	// the next player in the game
	private int nextPlayer = 0;
	//the direction of movement
	private ArrayList <Chess> dirt=new ArrayList<Chess>(6);
	
	public State() throws Exception{
		configure = new Configure();
		configure.setConfigure();
		
		//initiate directions
		dirt.add(new Chess(1,0,1));
		dirt.add(new Chess(0,1,1));
		dirt.add(new Chess(0,1,1));
		dirt.add(new Chess(0,-1,1));
		dirt.add(new Chess(1,-1,1));
		dirt.add(new Chess(-1,1,1));
	}
	
	public HashSet<Chess> getChesses(){
		return this.chesses;
	}
	
	public boolean getGameOn(){
		return this.gameOn;
	}
	
	public int getTotalPlayers(){
		return this.totalPlayers;
	}
	
	public int getNextPlayer(){
		return this.nextPlayer;
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
			
			//for the debug of validSet
			/*
			gameOn = true;
			Chess start=new Chess(4,-6,1);
			Chess end=new Chess(2,-4,1);
			boolean flag=false;
			flag=validMove(start,end);
			LOG.info(flag);
			*/
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
				break;
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
				break;
			}
			case 4:
			{
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
				count=4;temp=-4;begin=0;end=3;
				while(count>0){
					for(int i=begin;i<=end;i++)
						chesses.add(new Chess(i-4, temp, 3));
					temp=temp+1;end--;count--;
				}
				//for player 4
				count=4;temp=5;begin=0;end=3;
				while(count>0){
					for(int i=begin;i<=end;i++)
						chesses.add(new Chess(i-4, temp, 4));
					temp=temp+1;end--;count--;
				}
				break;
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
						chesses.add(new Chess(i+5, temp, 6));
					temp=temp+1;end--;count--;
				}
				break;
			}
		}
		/*
		for(Chess chess: this.chesses){
			String output;
			output="player "+chess.getOwner()+" Vert "+chess.getVert()+" Horiz  "+chess.getHoriz();
			LOG.info(output);
		}
		*/
	}
	
	//the function to move according to gui's command
	public boolean move(String command){
		//parse the start and end point of a move
		String[] strs = Pattern.compile(",").split(command);
		Chess start = new Chess(Integer.parseInt(strs[0]),Integer.parseInt(strs[1]),this.nextPlayer);
		Chess end = new Chess(Integer.parseInt(strs[2]),Integer.parseInt(strs[3]),this.nextPlayer);
		if (this.validMove(start, end)){
			removeChess(start);
			chesses.add(end);
			this.toNextPlayer();
			this.checkIsGameFinished();
			return true;
		}
		else{
			return false;
		}
	}
	
	//remove the chess from chesses
	//this function is because we cannot fix the contains() function in Hashset<chesses>
	private void removeChess(Chess chess){
		for(Chess temp:this.chesses){
			if((chess.getVert()==temp.getVert())
					&&(chess.getHoriz()==temp.getHoriz())
					&&(chess.getOwner()==temp.getOwner()))
				chesses.remove(temp);
		}
	}
	
	//get the "int nextPlayer" to the next player
	private void toNextPlayer(){
		if(this.nextPlayer<this.totalPlayers)
			this.nextPlayer = this.nextPlayer+1;
		else
			this.nextPlayer = 1;
	}
	
	//check if the game is finished, set gameOn if necessary
	//TO DO:we have to fill this blank to stop the game
	private void checkIsGameFinished(){
		this.gameOn = true;
	}
	
	//check whether a move is valid, this should not change chesses structure though
	private boolean validMove(Chess start, Chess end){
		boolean judge;//whether the movement is right
		judge=false;
		HashSet<Chess>avail=new HashSet<Chess>();
		validSet(start,avail);
		if(compare(avail,end))
			judge=true;
		return judge;
	}
	
	//move towards the point of Hexagon
	private Chess makeMove(Chess start,Chess direction){
		return (new Chess(start.getVert()+direction.getVert(),start.getHoriz()+direction.getHoriz(),start.getOwner()));
	}
	
	//recursion to find the avaliable set of movement
	//TO DO:if are the else path useful? maybe we should remove them if unnecessary
	private void validSet(Chess start,HashSet<Chess>avail){
		if(!compare(avail,start)){
			//for six direction
			for(Chess temp:dirt)
			{
				if(compare(chesses,makeMove(start,temp))){
					Chess now=makeMove(makeMove(start,temp),temp);
					if(!compare(chesses,now)){
						avail.add(now);
						validSet(now,avail);
					}
				}
				else{
					if(check(makeMove(start,temp))){//if the point is in the chessboard
						avail.add(makeMove(start,temp));
						validSet(makeMove(start,temp),avail);
					}
					else
						;
				}
			}
		}
		else 
			;
	}
	
	//whether the chess is still in the chessboard
	public boolean check(Chess now){
		boolean flag=false;
		if(now.getVert()>=-4&&now.getVert()<=4&&now.getHoriz()>=-4&&now.getHoriz()<=4)
			flag=true;
		else if(now.getVert()>=-4&&now.getVert()<=4&&now.getHoriz()>4){
			if(now.getHoriz()==5&&now.getVert()>=-4&&now.getHoriz()<=-1)
				flag=true;
			else if(now.getHoriz()==6&&now.getVert()>=-4&&now.getHoriz()<=-2)
				flag=true;
			else if(now.getHoriz()==7&&now.getVert()>=-4&&now.getHoriz()<=-3)
				flag=true;
			else if(now.getHoriz()==8&&now.getVert()==-4)
				flag=true;
		}
		else if(now.getVert()>=-4&&now.getVert()<=4&&now.getHoriz()<-4){
			if(now.getHoriz()==-5&&now.getVert()>=1&&now.getHoriz()<=4)
				flag=true;
			else if(now.getHoriz()==-6&&now.getVert()>=2&&now.getHoriz()<=4)
				flag=true;
			else if(now.getHoriz()==-7&&now.getVert()>=3&&now.getHoriz()<=4)
				flag=true;
			else if(now.getHoriz()==-8&&now.getVert()==4)
				flag=true;
		}
		else if(now.getHoriz()>=-4&&now.getHoriz()<=4&&now.getVert()<-4){
			if(now.getVert()==-5&&now.getHoriz()>=1&&now.getHoriz()<=4)
				flag=true;
			else if(now.getVert()==-6&&now.getHoriz()>=2&&now.getHoriz()<=4)
				flag=true;
			else if(now.getVert()==-7&&now.getHoriz()>=3&&now.getHoriz()<=4)
				flag=true;
			else if(now.getVert()==-8&&now.getHoriz()==4)
				flag=true;
		}
		else if(now.getHoriz()>=-4&&now.getHoriz()<=4&&now.getVert()>4){
			if(now.getVert()==5&&now.getHoriz()>=-4&&now.getHoriz()<=-1)
				flag=true;
			else if(now.getVert()==6&&now.getHoriz()>=-4&&now.getHoriz()<=-2)
				flag=true;
			else if(now.getVert()==7&&now.getHoriz()>=-4&&now.getHoriz()<=-3)
				flag=true;
			else if(now.getVert()==8&&now.getHoriz()==-4)
				flag=true;
		}

		return flag;
	}
	
	//whether one point is contained in the HashSet chesses
	public static boolean compare(HashSet<Chess> now,Chess exp){
		boolean flag=false;
		for(Chess temp:now){
			if((temp.getVert()==exp.getVert())&&(temp.getHoriz()==exp.getHoriz()))
			{
				flag=true;
				break;
			}
		}
		return flag;
	}

	//for test purpose, clean this up for release
	public String printChess(){
		String s = "";
		for (Chess i: chesses){
			s = s + i.printChess()+"   ";
		}
		return s;
	}
	
	//for test purpose
	public void test(){
		try{
			Thread.sleep(10000);
			LOG.info("chesses clear");
			this.chesses.clear();
		}	catch(Exception e){
			;
		}
	}
}

