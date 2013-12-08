package cn.edu.tsinghua.ee.Dreamland.DreamHalma;

import java.awt.Frame;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.State;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.Action;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.Configure;

//gui of the whole game, which class chess as the main backend

public class Gui implements Runnable {
	public void run(){
		try {
			MyFrame frame = new MyFrame();
		} catch (Exception e){
			System.out.println("Gui has failed to deal with an exception");
			System.out.println("Reason: " + e);	
			e.printStackTrace();
		}
	} 
}

class MyFrame extends Frame {
	
	private State state;
	private Configure configure;
	
	public MyFrame() throws Exception{
		configure = new Configure();
		state = new State();
		try{
			configure.setConfigure();
			state.init();
		} catch (Exception e){
			System.out.println("failed to initiate data");
			throw e;
		}
	}
}
