package cn.edu.tsinghua.ee.Dreamland.DreamHalma.model;

//class where store a location and the belonging of a chess piece
public class Chess {
	private int vert; // Vertical parameter
	private int horiz; // Horizontal parameter
	private int owner; // Owner of this chess piece
	Chess(int vert, int horiz, int owner){
		this.vert = vert;
		this.horiz = horiz;
		this.owner = owner;
	}
}
