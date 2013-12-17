package cn.edu.tsinghua.ee.Dreamland.DreamHalma.model;

import java.io.Serializable;

//class where store a location and the belonging of a chess piece
public class Chess implements Serializable{
	private int vert; // Vertical parameter
	private int horiz; // Horizontal parameter
	private int owner; // Owner of this chess piece
	
	Chess(int vert, int horiz, int owner){
		this.vert = vert;
		this.horiz = horiz;
		this.owner = owner;
	}
	
	@Override
	 public boolean equals(Object object){
		return ((((Chess)object).getVert()==this.getVert())&&(((Chess)object).getHoriz()==this.getHoriz())); 
	 }

	 public int hashcode(){
		 return this.getHoriz();
	 }
	public int getVert(){
		return this.vert;
	}
	
	public int getHoriz(){
		return this.horiz;
	}
	
	public int getOwner(){
		return this.owner;
	}
	
	//for test purpose, clean this up for release
	public String printChess(){
		return "vert:"+this.vert+"  horiz:"+this.horiz+"  owner:"+this.owner;
	}
	
}
