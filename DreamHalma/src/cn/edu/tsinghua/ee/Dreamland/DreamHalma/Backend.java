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

//Backend thread, this would act as a server for the whole game
//any Gui should make contact with a Backend to make it work all right
public class Backend implements Runnable{
	
	//this keeps record of the state of the whole game
	//where are the chess pieces, whether the game is over
	private State state; 
	private Configure configure;
	//the thread that deals with the heartbeat request from the Gui
	private HeartBeat heartBeat;
	
	private static final Log LOG = LogFactory.getLog(Backend.class);
	
	Backend() throws Exception{
		configure = new Configure();
		state = new State();
		configure.setConfigure();
	}
	
	public void run(){
		//if the backend property is "client"
		//this means that we only run a Gui locally, no backend required
		if(configure.getProperty("backend")=="client"){
			LOG.info("Backend Close since not necessarily useful");
			return;
		} 
		//if the backend property is "server", there's much more to do
		else if((configure.getProperty("backend")).equals("server")){
			LOG.info("Backend Initiating");
			try{
				heartBeat = new HeartBeat();
				configure.setConfigure();
				//initiate the state, according to how many players in total
				state.init();
				Thread heartBeatThread = new Thread(heartBeat);
				heartBeatThread.start();
			} catch (Exception e){
				LOG.error("failed to initiate data in backend: "+e.getStackTrace());
				System.exit(1);
			}
			LOG.info("Backend Initiated");
			try{
				//actually run the function to get movement request from Gui
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
		
		int serverPortMovement = Integer.parseInt(configure.getProperty("backend_port_movement"));
		ServerSocket server;
		server = new ServerSocket(serverPortMovement);
		
		//run this until the game is over
		boolean finished = false;
		while (!finished){
			//read movement from Gui
			Socket socket = server.accept();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String Command = br.readLine();
			//deal with the movement
			;
			//send the result back
			ObjectOutputStream oos = new ObjectOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));
			Message message = new Message(state);
			oos.writeObject(message);
			oos.flush();
			//clean the environment
			br.close();
			oos.close();
			socket.close();
		}
		server.close();
	}
	
	//a thread to deal with the heartbeat request from the Gui
	//we do not need to deal with any incoming data, but just send the information back
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
