package cn.edu.tsinghua.ee.Dreamland.DreamHalma;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.Configure;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.Gui;

//top level class just to trigger the frame class to start the game
public class DreamHalma {
	
	public static void main(String[] args){
		try{
			Gui gui = new Gui();
			Backend backend = new Backend();
			gui.run();
			backend.run();
		}
		catch(Exception e){
			System.out.println("Sorry to exit, Error running DreamHalma: " );
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Thanks to play this game, it is designed by Team Dreamland, Tsinghua University");
		System.exit(0);			
	}
	
}
