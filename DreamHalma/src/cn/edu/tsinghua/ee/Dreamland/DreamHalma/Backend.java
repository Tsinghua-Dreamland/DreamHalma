package cn.edu.tsinghua.ee.Dreamland.DreamHalma;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.State;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.Configure;

public class Backend implements Runnable{
	
	private State state;
	private Configure configure;
	private HeartBeat heartBeat;
	
	Backend() throws Exception{
		configure = new Configure();
		heartBeat = new HeartBeat();
		state = new State();
		try{
			configure.setConfigure();
			state.init();
			heartBeat.run();
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
	
	//this is a only readable usage of class State
	private class Message implements Serializable{
		State state;
		public Message(State state){
			this.state = state;
		}
	}
	
	private class HeartBeat implements Runnable{
		public void run(){
			int serverPort = Integer.parseInt(configure.getProperty("server_port"));
			try{
				ServerSocket server = new ServerSocket(serverPort);	
				while(true){
					Socket socket = server.accept();
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					Message message = new Message(state);
					oos.writeObject(message);
					oos.flush();
					oos.close();
					socket.close();
				}
			} catch(Exception e){
				System.out.println("failed to run socket server"+e);
				System.exit(1);
			}
		}
	}
}
