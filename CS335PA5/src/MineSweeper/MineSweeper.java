//By Jacob Richardson Fall 2016
package MineSweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;






public class MineSweeper extends Frame implements ActionListener, MouseListener{
	Square[][] squares;
	JButton reset;
	JLabel timer, mineNum;
	Timer t;
	int counter, numBombs;
	final int GameTime=1000;
	JMenuBar menu;
	JRadioButton ten, twenty, thirty, other;
	JRadioButton start, pause;
	Container butts;
	
	public void setupButtons(){
		//top panel of buttons
		Container toptop = new Container();//container for the menu bar and the top bar w/timer mine count and reset button
		Container top = new Container();//container for the mine count reset and timer
		top.setLayout(new GridLayout(1,3));//layout for those 3 buttons
		toptop.setLayout(new GridLayout(2,1));//layout for the menu bar and top container
		
		reset = new JButton("Reset");//reset button
		reset.addActionListener(this);//listen to reset
		
		menu = new JMenuBar();//menu bar
		JMenu szMen = new JMenu("Size");//size menu
		JMenu gMen = new JMenu("Game");//game menu
		
		//set up the size menu options
		ten = new JRadioButton("Ten");
		ten.addActionListener(this);
		ten.setSelected(true);
		szMen.add(ten);
		
		twenty = new JRadioButton("Twenty");
		twenty.addActionListener(this);
		szMen.add(twenty);
		
		thirty = new JRadioButton("Thirty");
		thirty.addActionListener(this);
		szMen.add(thirty);
		
		other = new JRadioButton("Other");
		other.addActionListener(this);
		szMen.add(other);
		
		//set up the game menu options
		start = new JRadioButton("Play");
		start.addActionListener(this);
		start.setSelected(true);
		gMen.add(start);
		
		pause = new JRadioButton("Pause");
		pause.addActionListener(this);
		gMen.add(pause);
		
		
		menu.add(gMen);
		menu.add(szMen);
		
		toptop.add(menu);
		
		counter =GameTime;
		
		t=new Timer(1000, this);
		//t.setDelay(1);
		t.start();
		
		timer = new JLabel();
		timer.setForeground(new Color(255,0,0));
		timer.setOpaque(true);
		timer.setBackground(new Color(189,0,0));
		timer.setText(""+counter);
		timer.setHorizontalAlignment(JLabel.CENTER);
		//timer.setEnabled(false);
		//timer.setHorizontalAligment();
		
		mineNum= new JLabel();
		mineNum.setForeground(new Color(255,0,0));
		mineNum.setOpaque(true);
		mineNum.setBackground(new Color(189,0,0));
		mineNum.setText("0");
		mineNum.setHorizontalAlignment(JLabel.CENTER);
		//mineNum.setEnabled(false);
		
		
		
		top.add(mineNum);
		top.add(reset);
		top.add(timer, BorderLayout.EAST);
		//top.setPreferredSize(new Dimension(this.getWidth(),this.getHeight()/6));
		this.setLayout(new BorderLayout());
		toptop.add(top);
		this.add(toptop, BorderLayout.NORTH);
		
		addSquares(10);
		plantMines();
		//mineNum.setText(""+plantMines());
	}
	public void addSquares(int size){
		squares = new Square[size][size];//allocate space for the array
		butts = new Container();//makes a new container for butts
		butts.setLayout(new GridLayout(size,size));//gives it a square grid
		//make the squares
		for(int r=0; r<size; r++)
			for(int c=0; c<size; c++){
				squares[r][c] = new Square();// new Square(r+"+"+c);//makes a new square
				butts.add(squares[r][c]);//adds square to butts
				//squares[r][c].addActionListener(this);//adds action listeners to each button
				squares[r][c].addMouseListener(this);
			}
		//set up neighbors
		for(int r=0; r<size; r++)
			for(int c=0; c<size; c++){
				//set up its neighbors
				if(r-1 >= 0 && c-1 >= 0)//up left
					squares[r][c].setNeighbor(squares[r-1][c-1], 0, 0);
				if(r-1 >= 0)//neighbor to the up
					squares[r][c].setNeighbor(squares[r-1][c], 0, 1);
				if(r-1 >= 0 && c+1 < size)//up right
					squares[r][c].setNeighbor(squares[r-1][c+1], 0, 2);
				
				if(c-1 >= 0)//neighbor to the left
					squares[r][c].setNeighbor(squares[r][c-1], 1, 0);
				if(c+1 < size)//neighbor to the right
					squares[r][c].setNeighbor(squares[r][c+1], 1, 2);
				
				if(r+1 < size && c-1 >= 0)//down left
					squares[r][c].setNeighbor(squares[r+1][c-1], 2, 0);
				if(r+1 < size)//neighbor to the down
					squares[r][c].setNeighbor(squares[r+1][c], 2, 1);
				if(r+1 < size && c+1 < size)//down right
					squares[r][c].setNeighbor(squares[r+1][c+1], 2, 2);
			}
		this.add(butts);//adds squares to the game board
	} 
	public void resetTimer(){
		t.restart();//restarts the clock
		counter=GameTime;//resets the counter variable
	}
	public void plantMines(){
		int size = squares.length;//dimension of the square play field
		//plant mines
		numBombs = (int)(Math.random()*size*size/3)+1;//number of bombs (at least a single bomb)
		mineNum.setText(""+numBombs);
		for(int i=0; i<numBombs; i++){//plants the bombs
			int r=(int)(Math.random()*size),c=(int)(Math.random()*size);//random position
			if(!squares[r][c].hasMine())//without a mine
				squares[r][c].plantMine();//plants the mine
			else//with a mine
				i--;//try again
		}
		mineNum.setText(""+ numBombs);
	}
	public void resetMines(){
		int size = squares.length;//size of the square field
		for(int r=0; r<size; r++)
			for(int c=0; c<size; c++)
				squares[r][c].reset();//resets each square
	}
	public void showMines(){
		int size = squares.length;//size of the square field
		for(int r=0; r<size; r++)
			for(int c=0; c<size; c++)
				if(squares[r][c].hasMine())
					squares[r][c].flip();//flips each square
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == reset){ //reset button is clicked 
	    	resetMines();//resets all the mines
	    	plantMines();//plants new mines
	    	resetTimer();//resets the timer
	    	return;//stop checking what's clicked
	    }
		if(e.getSource() == start){//play
			t.restart();
			pause.setSelected(false);
			start.setSelected(true);
		}
		if( e.getSource() == pause ){//play
			t.stop();
			pause.setSelected(true);
			start.setSelected(false);
		}
	    if(e.getSource() == ten || e.getSource() == twenty || e.getSource() == thirty || e.getSource() == other){//new size selected
	    	//remove all the squares
	    	this.remove(butts);
	    	butts.removeAll();
	    	//set all selected to be false
	    	ten.setSelected(false);
	    	twenty.setSelected(false);
	    	thirty.setSelected(false);
	    	other.setSelected(false);
	    	
	    	if(e.getSource() == ten){//10x10
	    		 setSize(500, 500);//size
	    		ten.setSelected(true);
	    		addSquares(10);//makes the squares
	    	}
	    	if(e.getSource() == twenty){
	    		 setSize(500*2, 500*2);//size
	    		twenty.setSelected(true);
	    		addSquares(20);//makes the squares
	    	}
	    	if(e.getSource() == thirty){
	    		 setSize(500*3, 500*3);//size
	    		thirty.setSelected(true);
	    		addSquares(30);//makes the squares
	    	}
	    	if(e.getSource() == other){
	    		String size = JOptionPane.showInputDialog(this,"Pleas input other size:");//reads in user input
	    		int sz = Integer.valueOf(size);//converts from string to integer
	    		int x = (int)Math.max(1, Math.min(sz/10,3));//uses size between 1 and 3
	    		setSize(500*x, 500*x);//size
	    		other.setSelected(true);
	    		addSquares(sz);//makes the squares
	    	}
	    	reset.doClick();//resets the game
	    	return;//stops checking 
	    }
	    if(t.isRunning()){//timer is running
			counter--;
			timer.setText(""+counter);
			if(counter==0){//end of game
				JOptionPane.showMessageDialog(this,"Time ran out!!");
				this.showMines();
				t.stop();
			}
			else{//game in progress and playing
			//clicked on a square 
			/*	for(int i=0; i<squares.length; i++)
					for(int j=0; j<squares[i].length; j++)
						if(e.getSource() == squares[i][j]){//checks which square is clicked
							squares[i][j].clicked();//tells the square it was clicked
							break;//stops checking the squares
						}	
			*/
			}
	    }
	    
	}
	public void win(){
		for(int i=0; i<squares.length; i++)
			for(int j=0; j<squares[i].length; j++)
				if(!squares[i][j].hasMine()&&squares[i][j].isCovered())
					return;
		this.showMines();
		JOptionPane.showMessageDialog(this,"You WIN!");
		t.stop();
	}
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("hmmm"+e.getButton());
		if(e.getButton() == 1)
			left=true;
		if(e.getButton() == 3)
			right=true;
		
		if(t.isRunning())
		for(int i=0; i<squares.length; i++)
			for(int j=0; j<squares[i].length; j++)
				if(e.getSource() == squares[i][j]){//checks which square is clicked
					if(squares[i][j].isCovered()){
						if(e.getButton() ==1){
							squares[i][j].clicked();//tells the square it was clicked
							if(squares[i][j].hasMine()){
								this.showMines();
								JOptionPane.showMessageDialog(this,"You hit a mine!");
								t.stop();
							}
							else{//check if they win
								win();					
							}
						}
						else if(e.getButton() == 3){
							squares[i][j].rclicked();//it was right clicked
							if(squares[i][j].isMarked())
								numBombs--;
							else
								numBombs++;
							mineNum.setText(""+numBombs);
						}
					}
					else{//if it is uncovered
						if( left && right){
							left=false;//reset variables so it only runs once
							right=false;
							if( !squares[i][j].check() ){//failed the check
								this.showMines();
								JOptionPane.showMessageDialog(this,"You missed the mine!");
								t.stop();
							}
							else{
								JOptionPane.showMessageDialog(this,"good job!");
								win();
							}
						}
					}
					break;//stops checking the squares
				}	
		
	}
	public MineSweeper() {
        super("CS335 - Mine Sweeper");

        setLayout(new BorderLayout());
        
        
        
     
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        
        setSize(600, 600);//size
        setLocation(40, 40);//default position on the screen
        this.setupButtons();
        setVisible(true);//makes it visible 
    }

    public static void main(String[] args) {
        MineSweeper demo = new MineSweeper();
        

        demo.setVisible(true);
    }
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	boolean left = false, right = false;
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1)
			left=true;
		if(e.getButton() == 3)
			right=true;
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == 1)
			left=false;
		if(e.getButton() == 3)
			right=false;
	}
   
    
}//end of minesweeper class


class MyWin extends WindowAdapter {
	 public void windowClosing(WindowEvent e)
   {
       System.exit(0);
   }
}
