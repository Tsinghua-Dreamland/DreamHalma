package cn.edu.tsinghua.ee.Dreamland.DreamHalma.model;

import java.util.HashSet;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.Configure;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.Chess;

//class to show the states of the game.
public class State {
	private Configure configure;
	private HashSet<Chess> chesses = new HashSet<Chess>();
	
	public State() throws Exception{
		Configure configure = new Configure();
		configure.setConfigure();
	}
	
	//initiate points mainly, using the configuration
	public void init() throws Exception{
		;
	}
}
