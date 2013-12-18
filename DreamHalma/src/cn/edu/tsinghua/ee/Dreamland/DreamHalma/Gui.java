package cn.edu.tsinghua.ee.Dreamland.DreamHalma;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.State;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.Message;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.Chess;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.Configure;

//gui of the whole game, which class chess as the main backend
public class Gui implements Runnable {
	
	private static final Log LOG = LogFactory.getLog(Gui.class);

	public void run(){
		try {
			//wait until the backend is fully initiated
			Thread.sleep(1000);
			LOG.info("Gui Initiating");
			new MyFrame("A New Game");
		} catch (Exception e){
			LOG.error("Gui has failed to deal with an exception: ");
			e.printStackTrace();
			System.exit(1);
		}
	} 
	
	@SuppressWarnings("serial")
	class MyFrame extends Frame {
		
		private State state;
		private Configure configure;
		ArrayList<Point> points = null;
		
		public MyFrame(String s) throws Exception{
			super(s);
			configure = new Configure();
			state = new State();
			points = new ArrayList<Point>();
			try{
				configure.setConfigure();
				//register the gui and refresh the chess board the first time
				state = this.registerChess().getState();
				//initiate the chess board
				Dimension dimension = new Dimension(602, 750);
				final Point origin = new Point(0, 0);
				final Rectangle rectangle = new Rectangle(origin, dimension);
				this.setBounds(rectangle);
				this.setVisible(true);
				final JPanel panelContent = new JPanel();   
				panelContent.setBounds(rectangle);
				panelContent.setOpaque(false);
				this.addMouseListener(new MyMouseListener()); 
				this.addWindowListener(new WindowAdapter(){
					public void windowClosing(WindowEvent e){
						setVisible(false);
						System.exit(0);
						}
					}
				);
				//to start the thread to renew information from server side
				StatusUpdater statusUpdater = new StatusUpdater();
				Thread statusUpdaterThread = new Thread(statusUpdater);
				statusUpdaterThread.start();
				//test function, delete for release
				this.test();
			} catch (Exception e){
				LOG.error("failed to initiate data");
				throw e;
			}
		}
		
		private void test() throws Exception{
			LOG.info("Backend test started");
			LOG.info("inital "+state.getChesses().size()+" chesses: "+state.printChess());
			Chess start = new Chess(-5,1,1);
			Chess end = new Chess(-4,0,1);
			this.testImpl(start, end);
			Thread.sleep(1000);
			start = new Chess(5,-1,2);
			end = new Chess(4,-2,2);
			this.testImpl(start, end);
			Thread.sleep(1000);
			start = new Chess(5,-1,2);
			end = new Chess(4,0,2);
			this.testImpl(start, end);
			Thread.sleep(1000);
			start = new Chess(-7,3,1);
			end = new Chess(-3,-1,1);
			this.testImpl(start, end);
		}
		
		private void testImpl(Chess start, Chess end) throws Exception{
			Message message = this.sendMove(start, end);
			LOG.info("GUI returned info: isValid = "+message.getIsValid());
			LOG.info("GUI returned info: nextPlayer = "+message.getState().getNextPlayer());
			LOG.info(message.getState().getChesses().size()+" chesses: "+message.getState().printChess());
		}
		
		public void paint(Graphics g)
		{
			final Image imageBg = Toolkit.getDefaultToolkit()
					.getImage("images/background.jpg");
			g.drawImage(imageBg,0,25, this);
			  
			for(Chess chess: state.getChesses()){
				int x = chess.getHoriz();
				int y = chess.getVert();
				int xx = 286 + x*48 + y*24;
				int yy = 370 + y*43;
				//System.out.println(x+","+y);
				//LOG.info("Chess marked");
				final Image img1 = Toolkit.getDefaultToolkit()
						.getImage("images/marbles/yellow.png");
				g.drawImage(img1,xx,yy, this); 
			 }
		}
		
		//added a double buffer to this Frame, which will eliminate the blinking while updating the frame
		private Image iBuffer;
		private Graphics gBuffer;
		public void update(Graphics g){
			if(iBuffer == null){
				iBuffer = createImage(this.getSize().width, this.getSize().height);
				gBuffer = iBuffer.getGraphics();
			}
			gBuffer.setColor(getBackground());
			gBuffer.fillRect(0, 0, this.getSize().width, this.getSize().height);
			paint(gBuffer);
			g.drawImage(iBuffer, 0, 0, this);
		}

		public void addPoint(Point p)
		{
			points.add(p);
		}
		
		//the function to register to the server as a client
		//returns the initial state of the game
		private Message registerChess() throws Exception{
			LOG.info("registering the gui to server");
			String str = configure.getProperty("local_players_detail");
			return this.sendMessage(str);
		}
		
		//send the information to the server and wait for returned info
		private Message sendMove(Chess start, Chess end) throws Exception{
			LOG.info("sending movement message to the server");
			String str = start.getHoriz()+","+start.getVert()
						+","+end.getHoriz()+","+end.getVert();
			return this.sendMessage(str);
		}
		
		//send message to server and get result
		private Message sendMessage(String str) throws Exception{
			int serverPort = Integer.parseInt(configure.getProperty("backend_port_movement"));
			String serverAddress = configure.getProperty("backend_address");
			try{
				Socket socket = new Socket(serverAddress, serverPort);
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(
						new BufferedOutputStream(socket.getOutputStream())));
				//send out the local players at this gui
				pw.println(str);
				pw.flush();
				pw.close();
				socket.close();
				socket = new Socket(serverAddress, serverPort);
				ObjectInputStream ois = new ObjectInputStream(
						new BufferedInputStream(socket.getInputStream()));
				Message message = (Message)ois.readObject();
				ois.close();
				socket.close();
				return message;
			} catch(Exception e){
				LOG.error("failed to connect with server");
				throw e;
			}
		}
		
		
		//this is a class to update the panal from the server every other second
		class StatusUpdater implements Runnable{
			public void run(){
				while (state.getGameOn()){
					try{
						Thread.sleep(1000);
						state = getHeartBeatMessage().getState();
						repaint();
					} catch (Exception e){
						LOG.error("failed to update data");
						System.exit(1);
					}
				}
			}
			
			//the function to get heartbeat message
			//will communicate with the heartbeat server thread in the backend
			//and returns a class Message as updated information
			private Message getHeartBeatMessage() throws Exception{
				int serverPort = Integer.parseInt(configure.getProperty("backend_port_heartbeat"));
				String serverAddress = configure.getProperty("backend_address");
				try{
					Socket socket = new Socket(serverAddress, serverPort);
					ObjectInputStream ois=new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
					Message message = (Message)ois.readObject();
					ois.close();
					socket.close();
					return message;
				} catch(Exception e){
					LOG.error("failed to connect with server");
					throw e;
				}
			}
		}
	}
	
	class MyMouseListener extends MouseAdapter
	{
		
		public void mousePressed(MouseEvent e){
			MyFrame mf = (MyFrame)e.getSource();
			mf.repaint();
			int x = e.getX()-12;
			int y = e.getY()-12;
			for (int i = 0; i <= 2; i++){
				if (i%2 == 0){
					for (int k = -2; k <= 574; k+=48){
						if ((k-x)*(k-x)+(43*i+24-y)*(43*i+24-y)<=169){
							//here send the coordinates
							mf.addPoint((new Point(k,43*i+24)));
							mf.repaint();
						}
					}
				}
				else{
					for (int k = 22; k <= 552; k+=48){
						if ((k-x)*(k-x)+(43*i+24-y)*(43*i+24-y)<=169){
							//here send the coordinates
							mf.addPoint((new Point(k,43*i+24)));
							mf.repaint();
						}
					}
				}
			}
			for (int i = 3; i <= 16; i++){
				if (i%2 == 0){
					for (int k = -2; k <= 574; k+=48){
						if ((k-x)*(k-x)+(43*i+26-y)*(43*i+26-y)<=169){
							//here send the coordinates
							mf.addPoint((new Point(k,43*i+26)));
							mf.repaint();
						}
					}
				}
				else{
					for (int k = 22; k <= 552; k+=48){
						if ((k-x)*(k-x)+(43*i+26-y)*(43*i+26-y)<=169){
							//here send the coordinates
							mf.addPoint((new Point(k,43*i+26)));
							mf.repaint();
						}
					}
				}
			}
		}
	}
}


