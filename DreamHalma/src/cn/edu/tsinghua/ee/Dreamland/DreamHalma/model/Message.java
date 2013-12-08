package cn.edu.tsinghua.ee.Dreamland.DreamHalma.model;

import java.io.Serializable;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.State;

//this is a only readable usage of class State, to transfer message across processes
public class Message implements Serializable{
	
	private State state; 
	
	public Message(State state){
		this.state = state;
	}
	
	public State getState(){
		return this.state;
	}
}
