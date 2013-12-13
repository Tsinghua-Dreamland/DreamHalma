package cn.edu.tsinghua.ee.Dreamland.DreamHalma;

import java.awt.Color;
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
import java.util.Iterator;
import java.io.BufferedInputStream;
import java.io.ObjectInputStream;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.State;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.Message;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.Chess;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.Configure;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.model.Chess;

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
			LOG.error("Gui has failed to deal with an exception: "+e.getStackTrace());
			System.exit(1);
		}
	} 
	
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
				//get a heart beat message the refresh the chess board the first time
				state = this.getHeartBeatMessage().getState();
				//this is a debug info, delete it for release
				LOG.info("chess pieces: " + state.printChess());
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
			} catch (Exception e){
				LOG.error("failed to initiate data");
				throw e;
			}
		}
		
		public void paint(Graphics g)
		{
			final Image imageBg = Toolkit.getDefaultToolkit()
					.getImage("images/background.jpg");
			g.drawImage(imageBg,0,25, this);
			LOG.info("background set");
			  
			for(Chess chess: state.getChesses()){
				int x = chess.getHoriz();
				int y = chess.getVert();
				int xx = 286 + x*48 + y*24;
				int yy = 370 + y*43;
				//System.out.println(x+","+y);
				LOG.info("Chess marked");
				final Image img1 = Toolkit.getDefaultToolkit()
						.getImage("images/marbles/yellow.png");
				g.drawImage(img1,xx,yy, this); 
			 }
		}

		public void addPoint(Point p)
		{
			points.add(p);
		}
		
		public void addState(State s)
		{		  
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


