package cn.edu.tsinghua.ee.Dreamland.DreamHalma;

import java.awt.Frame;
import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.State;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.Message;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.Configure;

//gui of the whole game, which class chess as the main backend
public class Gui implements Runnable {
	
	private static final Log LOG = LogFactory.getLog(Gui.class);

	public void run(){
		try {
			//wait until the backend is fully initiated
			Thread.sleep(1000);
			LOG.info("Gui Initiating");
			MyFrame frame = new MyFrame();
		} catch (Exception e){
			LOG.error("Gui has failed to deal with an exception: "+e.getStackTrace());
			System.exit(1);
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
				//get a heart beat message the refresh the chess board the first time
				state = this.getHeartBeatMessage().getState();
				//this is a debug info, delete it for release
				LOG.info("chess pieces: " + state.printChess());
			} catch (Exception e){
				LOG.error("failed to initiate data");
				throw e;
			}
		}
		
		//the function to get heartbeat message
		//will communicate with the heartbeat server thread in the backend
		//and returns a class Message as updated information
		private Message getHeartBeatMessage() throws Exception{
			int serverPort = Integer.parseInt(configure.getProperty("backend_port_heartbeat"));
			String serverAddress = configure.getProperty("backend_address");
			LOG.info("connecting backend with: "+serverAddress+":"+serverPort);
			try{
				Socket socket = new Socket(serverAddress, serverPort);
				ObjectInputStream ois=new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				Message message = (Message)ois.readObject();
				return message;
			} catch(Exception e){
				LOG.error("failed to connect with server");
				throw e;
			}
		}
	}
}


