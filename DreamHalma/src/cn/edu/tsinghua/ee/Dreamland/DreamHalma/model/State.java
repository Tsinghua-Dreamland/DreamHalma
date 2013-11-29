package cn.edu.tsinghua.ee.Dreamland.DreamHalma.model;

import java.util.HashSet;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.Configure;

//class to show the states of the game.
public class State {
	private Configure configure;
	private HashSet<Point> points = new HashSet<Point>();
	
	public State() throws Exception{
		configure.setConfigure();
	}
	
	//initiate points mainly, using the configuration
	public void init() throws Exception{
		;
	}
}
