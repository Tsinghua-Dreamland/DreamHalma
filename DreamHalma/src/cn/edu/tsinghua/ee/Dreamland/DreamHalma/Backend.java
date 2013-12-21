package cn.edu.tsinghua.ee.Dreamland.DreamHalma;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

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
	//the list to keep record of all the players registerd
	ArrayList<InetAddress> players = new ArrayList<InetAddress>();
	
	private static final Log LOG = LogFactory.getLog(Backend.class);
	
	Backend() throws Exception{
		configure = new Configure();
		state = new State();
		configure.setConfigure();
	}
	
	public void run(){
		//if the backend property is "client"
		//this means that we only run a Gui locally, no backend required
		if((configure.getProperty("backend")).equals("client")){
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
				LOG.error("failed to initiate data in backend: ");
				e.printStackTrace();
				System.exit(1);
			}
			LOG.info("Backend Initiated");
			try{
				//actually run the function to get movement request from Gui
				this.startGame();
			} catch (Exception e){
				LOG.error("game halted in error: ");
				e.printStackTrace();
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
		
		LOG.info("Game Controller Server Initiating");
		int serverPortMovement = Integer.parseInt(configure.getProperty("backend_port_movement"));
		ServerSocket server;
		server = new ServerSocket(serverPortMovement);
		
		//register the players, until all the players are ready
		LOG.info("Begin registration");
		boolean success=true;
		ArrayList<Integer> registered = new ArrayList<Integer>();
		this.initiateRegister(server);
		while(success && (registered.size()<state.getTotalPlayers())){
			LOG.info("waiting for client side to register");
			success = this.register(server, registered);
		}
		LOG.info("Registration over, start the game");
		
		//run this until the game is over
		while (state.getGameOn()){
			LOG.info("waiting for the move from player "+state.getNextPlayer());
			this.act(server);
		}
		server.close();
	}
	
	private void initiateRegister(ServerSocket server){
		for(int i=0;i<state.getTotalPlayers();i++){
			players.add(server.getInetAddress());
		}
	}
	
	//this function is designed to receive registration info from gui and return the initial state
	private boolean register(ServerSocket server, ArrayList<Integer> registered) throws Exception{
		boolean success = true;
		Socket socket = server.accept();
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		LOG.info("server built succesfully");
		String command = br.readLine();
		br.close();
		socket.close();
		//deal with the registration
		String[] strs = Pattern.compile(",").split(command);
		for(int i=0;i<strs.length;i++){
			if(registered.contains(Integer.parseInt(strs[i]))){
				success = false;
				break;
			}
			else{
				players.set(Integer.parseInt(strs[i])-1, socket.getInetAddress());
				registered.add(Integer.parseInt(strs[i]));
				LOG.info("player "+Integer.parseInt(strs[i])+" is from "+socket.getInetAddress());
			}
		}
		socket = server.accept();
		ObjectOutputStream oos = new ObjectOutputStream(
				new BufferedOutputStream(socket.getOutputStream()));
		//send the result back
		Message message = new Message(state);
		message.setRegistered(success);
		oos.writeObject(message);
		oos.flush();
		//clean the environment
		oos.close();
		socket.close();
		return success;
	}
	
	//the function to deal with movement from the player
	//transfer the information, deal with it, and return the result back
	private boolean act(ServerSocket server) throws Exception{
		boolean success = true;
		//read movement from Gui
		Socket socket = server.accept();
		//if the connection is not established by the next player, close the socket and set if fail
		//attention:gui has to deal with this exception accordingly
		if(!socket.getInetAddress().equals(players.get(state.getNextPlayer()-1))){
			socket.close();
			return false;
		} else{
			//receive command from gui
			BufferedReader br = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			String command = br.readLine();
			br.close();
			socket.close();
			//deal with the movement
			success = state.move(command);
			//send the result back
			socket = server.accept();
			ObjectOutputStream oos = new ObjectOutputStream(
					new BufferedOutputStream(socket.getOutputStream()));
			Message message = new Message(state);
			message.setIsValid(success);
			oos.writeObject(message);
			oos.flush();
			//clean the environment
			br.close();
			oos.close();
			socket.close();
			return success;
		}
	}
	
	//a thread to deal with the heartbeat request from the Gui
	//we do not need to deal with any incoming data, but just send the information back
	private class HeartBeat implements Runnable{
		public void run(){
			LOG.info("Heartbeat Server Initiating");
			int serverPortHeartBeat = Integer.parseInt(
					configure.getProperty("backend_port_heartbeat"));
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
				System.out.println("failed to run socket server");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
