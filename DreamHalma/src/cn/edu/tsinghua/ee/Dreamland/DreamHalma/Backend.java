package cn.edu.tsinghua.ee.Dreamland.DreamHalma;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.State;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.Message;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.Configure;

//Backend thread to act as a server
public class Backend implements Runnable{
	
	private State state;
	private Configure configure;
	private HeartBeat heartBeat;
	
	private static final Log LOG = LogFactory.getLog(Backend.class);
	
	Backend() throws Exception{
		configure = new Configure();
		state = new State();
		configure.setConfigure();
	}
	
	public void run(){
		if(configure.getProperty("backend")=="client"){
			LOG.info("Backend Close since not necessarily useful");
			return;
		} 
		else if((configure.getProperty("backend")).equals("server")){
			LOG.info("Backend Initiating");
			try{
				heartBeat = new HeartBeat();
				configure.setConfigure();
				state.init();
				Thread heartBeatThread = new Thread(heartBeat);
				heartBeatThread.start();
			} catch (Exception e){
				LOG.error("failed to initiate data in backend: "+e.getStackTrace());
				System.exit(1);
			}
			LOG.info("Backend Initiated");
			try{
				this.startGame();
			} catch (Exception e){
				LOG.error("game halted in error: "+e.getStackTrace());
				System.exit(1);
			}
			LOG.info("Game finished successfully");
		}
		else {
			LOG.error("failed to find if its client or server");
			System.exit(1);
		}
	}
	
	private void startGame() throws Exception{
		boolean finished = false;
		int serverPortMovement = Integer.parseInt(configure.getProperty("backend_port_movement"));
		ServerSocket server;
		server = new ServerSocket(serverPortMovement);
		while (!finished){
			Socket socket = server.accept();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String Command = br.readLine();
			ObjectOutputStream oos = new ObjectOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));
			Message message = new Message(state);
			oos.writeObject(message);
			oos.flush();
			br.close();
			oos.close();
			socket.close();
		}
		server.close();
	}
	
	//a thread to receive and send out heartbeat information
	private class HeartBeat implements Runnable{
		public void run(){
			LOG.info("Heartbeat Server Initiating");
			int serverPortHeartBeat = Integer.parseInt(configure.getProperty("backend_port_heartbeat"));
			ServerSocket server;
			try{
				server = new ServerSocket(serverPortHeartBeat);	
				while(true){
					Socket socket = server.accept();
					ObjectOutputStream oos = new ObjectOutputStream(
							new BufferedOutputStream(socket.getOutputStream()));
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
