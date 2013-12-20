package cn.edu.tsinghua.ee.Dreamland.DreamHalma.model;

import java.io.Serializable;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.State;

//this is a only readable usage of class State, to transfer message across processes
@SuppressWarnings("serial")
public class Message implements Serializable{
	
	private State state;  // the state of the game
	private boolean registered = false; // the marker whether the player has registered as a player
	private boolean isValid = true; //the marker whether a certain move is valid
	
	public Message(State state){
		this.state = state;
	}
	
	public State getState(){
		return this.state;
	}
	
	public boolean getRegistered(){
		return this.registered;
	}
	
	public void setRegistered(boolean registered){
		this.registered = registered;
	}
	
	public boolean getIsValid(){
		return this.isValid;
	}
	
	public void setIsValid(boolean isValid){
		this.isValid = isValid;
	}
}
