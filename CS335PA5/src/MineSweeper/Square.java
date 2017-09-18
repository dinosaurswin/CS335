package MineSweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class Square extends JButton{
	public static  ImageIcon iMine, iMark;
	public static boolean firstMine=true,firstMark=true;
	private int nearbyMines;
	private boolean isMine, cover, marked;
	private Color c[] = {new Color(0,0,255), new Color(0,255,0), new Color(255,0,0), new Color(128,0,128), new Color(128,0,0), new Color(0,255,255), new Color(0,0,0), new Color(128,128,128)};
	//according to sporcle the colors are 1-blue, 2-green, 3-red, 4-purple, 5-maroon, 6-turquoise, 7-black, 8-grey
	private Square[][] neighbors ;//set to be 
	//0:[0,	1,		2]
	//1:[0,	this,	2]
	//2:[0,	1,		2]
	//constructors
	public Square(){
		super();//default button
		//this.setEditable(false);
		//set up its new class variables
	//	this.setEnabled(false);
		this.setBackground(Color.lightGray);
		nearbyMines=0;
		isMine=false;
		cover=true;
		marked=false;
		neighbors = new Square[3][3];
	}
	public Square( String s){
		super(s);//default button
		//this.setEditable(false);
		this.setEnabled(false);
		this.setBackground(Color.lightGray);
		//set up its new class variables
		nearbyMines=0;
		isMine=false;
		marked=false;
		cover=true;
		neighbors = new Square[3][3];
	}
	
	public void checkMines(){//checks for the number of nearby mines
		int minecount=0;//number of mines found
		for(int r=0; r<3;r++)
			for(int c=0; c<3; c++)
				if(neighbors[r][c]!=null && neighbors[r][c].hasMine())//checks for a mine
					minecount++;
		nearbyMines=minecount;//sets nearbyMines to be the number of nearbyMines
	}
	public boolean check(){//checks for the number of nearby mines
		for(int r=0; r<3;r++)
			for(int c=0; c<3; c++)
				if(neighbors[r][c]!=null){//checks for a mine
					if(!neighbors[r][c].isMarked())
						neighbors[r][c].flip();
					if(neighbors[r][c].hasMine() && !neighbors[r][c].isMarked())
						return false;
					}
		return true;//hasnt found any unmakred mines
	}
	public void rclicked(){
		marked=!marked;//toggles marked back and forth
		if(marked){
			if(firstMark){
				firstMark=false;
				iMark = new ImageIcon("flag.png");
				Image img = iMark.getImage() ;  
				Image newimg = img.getScaledInstance( this.getWidth(), this.getHeight(),  java.awt.Image.SCALE_SMOOTH ) ;  
				iMark = new ImageIcon( newimg );	
			}
			this.setIcon(iMark);
			//this.setText("Mine?");
		}
		else{
			this.setIcon(null);
			this.setText("");
		}
		
	}
	public void flip(){
		cover = false;
		this.checkMines();
		if(isMine){
			if(firstMine){//only runs once per class
				firstMine=false;
				iMine = new ImageIcon("mine.png");
				Image img = iMine.getImage() ;  
				Image newimg = img.getScaledInstance( this.getWidth(), this.getHeight(),  java.awt.Image.SCALE_SMOOTH ) ;  
				iMine = new ImageIcon( newimg );
			}
			this.setIcon(iMine);
			this.setText("");
			this.setBackground(Color.red);
		}
		else{
			this.setIcon(null);
			//according to sporcle the colors are 1-blue, 2-green, 3-red, 4-purple, 5-maroon, 6-turquoise, 7-black, 8-grey 
			this.setBackground(Color.GRAY);
			if(nearbyMines>0){
				//this.setText("<html><font color=red>5</font></html>");
				//look up setting text color with html text
				this.setText(""+nearbyMines);//shows number of nearby  mines
				this.setForeground(c[nearbyMines-1]);//only works on neabled buttons
			}
			else
				this.setText("");
		}
	}
	public void clicked(){//what happens when this button is clicked
		this.flip();
		//changes the graphic
		if(nearbyMines==0)
		for(int r=0; r<3;r++)
			for(int c=0; c<3; c++){
				if(neighbors[r][c]!=null && !neighbors[r][c].hasMine() && !(r==1&&c==1) && neighbors[r][c].isCovered()){//checks for everyone without a mine
					//System.out.println(this.getText());
					neighbors[r][c].clicked();
					;
				}
			}
		//setEnabled(false);
	}
	
	//Mutators
	public void plantMine(){isMine=true;}//make the square have a mine
	public void reset(){
		isMine=false;
		cover=true;
		firstMine=true;
		firstMark=true;
		this.setEnabled(true);
		this.setText("");
		this.setIcon(null);
		this.setBackground(Color.LIGHT_GRAY);
	}
	
	//sets up the neighbor squares of this square
	public void setNeighbor(Square s, int r, int c){ neighbors[r][c] = s;}
	public void setNeighbor(Square s1,Square s2,Square s3,Square s4,Square s5,Square s6,Square s7,Square s8 ){
		setNeighbor(s1,0,0);	setNeighbor(s2,0,1);	setNeighbor(s3,0,2);
		setNeighbor(s4,1,0);	setNeighbor(this,1,1);	setNeighbor(s5,1,2);
		setNeighbor(s6,2,0);	setNeighbor(s7,2,1);	setNeighbor(s8,2,2);
	}
		
	//Acessors
	public boolean hasMine(){return isMine;}//check is the square has a mine
	public boolean isCovered(){return cover;}//check is the square has a mine
	public Square getNeighbor( int r, int c){ return neighbors[r][c];}//gets a specific neighbor
	public int getMineNum(){return nearbyMines;}
	public boolean isMarked() {return marked;}
	
}
