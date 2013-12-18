package cn.edu.tsinghua.ee.Dreamland.DreamHalma;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.Gui;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.Backend;

//top level class just to trigger the frame class to start the game
public class DreamHalma {
	
	private static final Log LOG = LogFactory.getLog(DreamHalma.class);
	
	public static void main(String[] args){
		try{
			//start the backend thread
			Backend backend = new Backend();
			Thread backendThread = new Thread(backend);
			backendThread.start();
			//start the gui thread
			Gui gui = new Gui();
			Thread guiThread = new Thread(gui);
			guiThread.start();
		}
		catch(Exception e){
			LOG.error("Sorry to exit, Error running DreamHalma: ");
			e.printStackTrace();
			System.exit(1);
		}		
	}
}
