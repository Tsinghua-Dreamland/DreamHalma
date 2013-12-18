package cn.edu.tsinghua.ee.Dreamland.DreamHalma.model;

import java.io.Serializable;

//class where store a location and the belonging of a chess piece
@SuppressWarnings("serial")
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
		if (this == object) return true;  
        if (object == null) return false;  
        if (getClass() != object.getClass()) return false;  
		final Chess temp=(Chess)object;
		if (vert != temp.vert)
			   return false;
		if (horiz != temp.horiz)
			   return false;
		return true;
	 }

	public int hashcode(){
		 final int prime = 31;  
	        int result = 1;  
	        result = prime * result + this.vert;
	        result = prime*  result + this.horiz;
	        return result;  
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
