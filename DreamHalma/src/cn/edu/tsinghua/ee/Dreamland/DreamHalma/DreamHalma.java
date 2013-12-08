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
			Gui gui = new Gui();
			Backend backend = new Backend();
			Thread guiThread = new Thread(gui);
			Thread backendThread = new Thread(backend);
			backendThread.start();
			guiThread.start();
			
		}
		catch(Exception e){
			LOG.error("Sorry to exit, Error running DreamHalma: " + e.getStackTrace());
			System.exit(1);
		}		
	}
	
}
