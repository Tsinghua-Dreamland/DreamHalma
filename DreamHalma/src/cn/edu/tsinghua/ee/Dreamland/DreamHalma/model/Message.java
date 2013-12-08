package cn.edu.tsinghua.ee.Dreamland.DreamHalma.model;

import java.io.Serializable;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.State;

//this is a only readable usage of class State, to transfer message across processes
public class Message implements Serializable{
	private boolean gameOn = true; //a marker to make sure whether the game is still on going, for the clean up need
	private State state;
	public Message(State state){
		this.state = state;
	}
	public State getState(){
		return this.state;
	}
	public boolean gameOn(){
		return this.gameOn;
	}
}
