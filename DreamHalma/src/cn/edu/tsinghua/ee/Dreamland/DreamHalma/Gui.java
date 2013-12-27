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
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;
import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.swing.JOptionPane;
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
		
		public State state;
		private Configure configure;
		public Point points;             //record select click position
		public int whichClick = 0;       //is this a select click or a move click
		public int wrongClick = 0;		 // is the select click is a weong click
		public Chess firstClick;			
		public Message FailMessage;		 // is the move is a fail
		public boolean FrontPage = true;
		public boolean InstructionPage = false;
		//public boolean init = true;
		
		public MyFrame(String s) throws Exception{
			super(s);
			configure = new Configure();
			state = new State();
			points = new Point();
			FailMessage = new Message(state);
			//points = new ArrayList<Point>();
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
				//this.test();
			} catch (Exception e){
				LOG.error("failed to initiate data");
				throw e;
			}
		}
		public void testImpl(Chess start, Chess end) throws Exception{
			Message message = this.sendMove(start, end);
			LOG.info("GUI returned info: isValid = "+message.getIsValid());
			LOG.info("GUI returned info: nextPlayer = "+message.getState().getNextPlayer());
			//LOG.info(message.getState().getChesses().size()+" chesses: "+message.getState().printChess());
		}
		
		public void paint(Graphics g)
		{
			if (FrontPage == true){
				final Image imageBg = Toolkit.getDefaultToolkit()
						.getImage("images/FrontPage.jpg");
				g.drawImage(imageBg,0,0, this);
			}
			else{
				final Image imageBg = Toolkit.getDefaultToolkit()
						.getImage("images/background.jpg");
				g.drawImage(imageBg,0,25, this);
				final Image imageNextPlayer = Toolkit.getDefaultToolkit()
						.getImage("images/next player.png");
				g.drawImage(imageNextPlayer,0,10, this);
				if (state.getNextPlayer() == 1)
				{
					final Image img1 = Toolkit.getDefaultToolkit()
							.getImage("images/marbles/p1.png");
					g.drawImage(img1,150,44, this);
				}
				if (state.getNextPlayer() == 2)
				{
					final Image img1 = Toolkit.getDefaultToolkit()
							.getImage("images/marbles/p2.png");
					g.drawImage(img1,150,44, this);
				}
				if (state.getNextPlayer() == 3)
				{
					final Image img1 = Toolkit.getDefaultToolkit()
							.getImage("images/marbles/p3.png");
					g.drawImage(img1,150,44, this);
				}
				if (state.getNextPlayer() == 4)
				{
					final Image img1 = Toolkit.getDefaultToolkit()
							.getImage("images/marbles/p4.png");
					g.drawImage(img1,150,44, this);
				}
				if (state.getNextPlayer() == 5)
				{
					final Image img1 = Toolkit.getDefaultToolkit()
							.getImage("images/marbles/p5.png");
					g.drawImage(img1,150,44, this);
				}
				if (state.getNextPlayer() == 6)
				{
					final Image img1 = Toolkit.getDefaultToolkit()
							.getImage("images/marbles/p6.png");
					g.drawImage(img1,150,44, this);
				}
				if (state.getGameOn() == false)
				{
					if (state.getNextPlayer() == 2) {
						JOptionPane.showMessageDialog(
							    null,"Player 1 wins!!");
						System.exit(0);
					}
					if (state.getNextPlayer() == 3) {
						JOptionPane.showMessageDialog(
							    null,"Player 2 wins!!");
						System.exit(0);
					}
					if (state.getNextPlayer() == 4) {
						JOptionPane.showMessageDialog(
							    null,"Player 3 wins!!");
						System.exit(0);
					}
					if (state.getNextPlayer() == 5) {
						JOptionPane.showMessageDialog(
							    null,"Player 4 wins!!");
						System.exit(0);
					}
					if (state.getNextPlayer() == 6) {
						JOptionPane.showMessageDialog(
							    null,"Player 5 wins!!");
						System.exit(0);
					}
					if (state.getNextPlayer() == 1) {
						JOptionPane.showMessageDialog(
							    null,"Player " + state.getTotalPlayers() + " wins!!");
						System.exit(0);
					}
				}
				for(Chess chess: state.getChesses()){
					int x = chess.getHoriz();
					int y = chess.getVert();
					int xx = 286 + x*48 + y*24;
					int yy = 370 + y*43;
					//System.out.println(x+","+y);
					//LOG.info("Chess marked");
					if (chess.getOwner() == 1){
						final Image img1 = Toolkit.getDefaultToolkit()
								.getImage("images/marbles/p1.png");
						g.drawImage(img1,xx,yy, this); 	
					}
					if (chess.getOwner() == 2){
						final Image img1 = Toolkit.getDefaultToolkit()
								.getImage("images/marbles/p2.png");
						g.drawImage(img1,xx,yy, this); 
					}
					if (chess.getOwner() == 3){
						final Image img1 = Toolkit.getDefaultToolkit()
								.getImage("images/marbles/p3.png");
						g.drawImage(img1,xx,yy, this); 	
					}
					if (chess.getOwner() == 4){
						final Image img1 = Toolkit.getDefaultToolkit()
								.getImage("images/marbles/p4.png");
						g.drawImage(img1,xx,yy, this); 
					}
					if (chess.getOwner() == 5){
						final Image img1 = Toolkit.getDefaultToolkit()
								.getImage("images/marbles/p5.png");
						g.drawImage(img1,xx,yy, this); 	
					}
					if (chess.getOwner() == 6){
						final Image img1 = Toolkit.getDefaultToolkit()
								.getImage("images/marbles/p6.png");
						g.drawImage(img1,xx,yy, this); 
					}
					if (whichClick == 1){
						   final Image circle = Toolkit.getDefaultToolkit()
						           .getImage("images/marbles/cc.png");
						   g.drawImage(circle,points.x,points.y, this); 
						   //g.fillOval(p.x-6,p.y-8,25,25);
					}
					//if (init == false){
						if (wrongClick == 1){
							   final Image cwrong = Toolkit.getDefaultToolkit()
							           .getImage("images/choosefail.png");
							   g.drawImage(cwrong,-5,45, this); 
							   //g.fillOval(p.x-6,p.y-8,25,25);
						}
						else{
							if (FailMessage.getIsValid() == false){
								final Image pwrong = Toolkit.getDefaultToolkit()
										.getImage("images/playfail.png");
									g.drawImage(pwrong,-5,45, this); 
							}
						}
					//}	
				}
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
			Message message = this.sendMessage(str);
			LOG.info("GUI returned info: isValid = "+message.getIsValid());
			LOG.info("GUI returned info: nextPlayer = "+message.getState().getNextPlayer());
			//LOG.info(message.getState().getChesses().size()+" chesses: "+message.getState().printChess());
			return message;
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
			if (mf.FrontPage == true)
			{
				mf.FrontPage = false;
			}
			else{
				for (int i = 0; i <= 16; i++){
					if (i%2 == 0){
						for (int k = -2; k <= 574; k+=48){
							if ((k-x)*(k-x)+(43*i+26-y)*(43*i+26-y)<=169){
								this.dealwithClick(mf, k, i);
							}
						}
					}
					else{
						for (int k = 22; k <= 552; k+=48){
							if ((k-x)*(k-x)+(43*i+26-y)*(43*i+26-y)<=169){
								//here send the coordinates
								this.dealwithClick(mf, k, i);
							}
						}
					}
				}
			}
		}
		public void dealwithClick(MyFrame mf, int k,int i)
		{
			//here send the coordinates
			int xx = (k+2)/48 - 6 - (i - i%2 -8)/2;
			int yy = i - 8;
			if (mf.whichClick == 0)
			{
				mf.wrongClick = 1;
				for(Chess chess: mf.state.getChesses()){
					int cx = chess.getHoriz();
					int cy = chess.getVert();
					int owner = chess.getOwner();
					if (cx == xx && cy == yy && owner == mf.state.getNextPlayer()){
						mf.firstClick = new Chess(xx,yy,0);
						mf.points.x = k;
						mf.points.y = 43*i+26;
						mf.whichClick = 1;
						mf.wrongClick = 0;
						break;
					}
				}
				mf.repaint();
			}
			else
			{
				mf.wrongClick = 0;
				Chess secondClick = new Chess(xx,yy,0);
				try {
					mf.FailMessage = mf.sendMove(mf.firstClick, secondClick);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (secondClick.getHoriz() == mf.firstClick.getHoriz() && secondClick.getVert() == mf.firstClick.getVert()){
					mf.FailMessage.setIsValid(true);
				}
				mf.whichClick = 0;
				mf.repaint();
			}
		}
	}
}


