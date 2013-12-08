package cn.edu.tsinghua.ee.Dreamland.DreamHalma;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.State;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.Configure;

public class Backend implements Runnable{
	
	private State state;
	private Configure configure;
	
	Backend() throws Exception{
		configure = new Configure();
		state = new State();
		try{
			configure.setConfigure();
			state.init();
		} catch (Exception e){
			System.out.println("failed to initiate data in backend");
			throw e;
		}
	}
	
	public void run(){
		if(configure.getProperty("backend")=="client"){
			return;
		} 
		else if((configure.getProperty("backend")).equals("server")||
				(configure.getProperty("backend")).equals("client_and_server")){
			//do a lot here
		}
		else {
			System.out.println("failed to find if its client or server");
			System.exit(1);
		}
		
	}
}
