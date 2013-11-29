package cn.edu.tsinghua.ee.Dreamland.DreamHalma;

import cn.edu.tsinghua.ee.Dreamland.DreamHalma.utils.Configure;
import cn.edu.tsinghua.ee.Dreamland.DreamHalma.gui.MyFrame;

//top level class just to trigger the frame class to start the game
public class DreamHalma {
	
	public static void main(String[] args){
		try{
			MyFrame frame = new MyFrame();
		}
		catch(Exception e){
			System.out.println("Sorry to exit, Error running DreamHalma: " + e);
			System.exit(1);
		}
		System.out.println("Thanks to play this game, it is designed by Team Dreamland, Tsinghua University");
		System.exit(0);			
	}
	
}
