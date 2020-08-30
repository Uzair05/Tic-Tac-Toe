/**
 * The main purpose of this class is to hold and store the playing table of the Tic Tac Toe game.<br>
 * Ideally, the entire process is synchronized and all necessary variables are within reach of the server.<br>
 * The server can use data from this table stored in this class to determine the winner and check if any moves are valid.
 * @author Uzair Bin Colangoy Asim; UID: 3035603071
 * @version 1.0
 * @since 22/12/2019
 */

public class SharedNumber {
	private final Object lock = new Object();
	private char[][] n = new char[3][3];
	private int counter;
	/**
	 * Constructor method.
	 */
	public SharedNumber() {
		this.counter=0;
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				this.n[i][j] = 'm';
			}
		}
	}
	/**
	 * This Particular method aids in clearing the table in the event of having the game reset.
	 */
	public void resetTable() {
		this.counter=0;
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				this.n[i][j] = 'm';
			}
		}
	}
	/**
	 * This method aims in storing the character x in its appropriate space in the table.
	 * @param x The Row in which the <b>x</b> should be placed.
	 * @param y The Column in which the <b>x</b> should be placed.
	 * @return {@code boolean} This method returns true on successful operation i.e. <i>The Mark was placed in an empty square.</i>
	 */
	public boolean setCross(int x, int y) {
		synchronized (lock) {
			if(this.n[x][y] == 'm') {
				this.n[x][y] = 'x';
				this.counter++;
				return true;
			}else {
				return false;
			}
		}
	}
	/**
	 * This method aims in storing the character o in its appropriate space in the table.
	 * @param x The Row in which the <b>o</b> should be placed.
	 * @param y The Column in which the <b>o</b> should be placed.
	 * @return {@code boolean} This method returns true on successful operation i.e. <i>The Mark was placed in an empty square.</i>
	 */
	public boolean setCircle(int x, int y) {
		synchronized (lock) {
			if(this.n[x][y] == 'm') {
				this.n[x][y] = 'o';
				this.counter++;	
				return true;
			}else {
				return false;
			}
		}
	}
	/**
	 * This method allows external classes to access and read the {@code counter} variable.
	 * @return {@code this.counter} It is returned so that it can be read by external classes.
	 */
	public int getCounter() {
		synchronized (lock) {
			return this.counter;
		}
	}
	
	/**
	 * This method aims in creating a numeral identification to declare the winner of the game, if any. 
	 * @return [0,1,2,3] <ul><li>0 is returned if no one won.</li><li>1 is return if player 1 won</li><li>2 is returned if player 2 won</li><li>3 is returned in the event of a draw.</li></ul> 
	 */
	public int someoneWon() {
		synchronized (lock) {
			if (this.xWon()) {
				return 1;
			}else if (this.oWon()) {
				return 2;
			}else if (counter==9) {
				return 3;
			}else {
				return 0;
			}
		}
	}
	/**
	 * A private method which allows easier modulation to check if player 1 won.
	 * @return true if player 1 (representing the mark 'x') won.
	 */
	private boolean xWon() {
		synchronized (lock) {
			boolean won;
			
			boolean grade = false;
			
			//Horizontal win
			for(int i=0;i<3;i++) {
				won = true;
				for(int j=0;j<3;j++) {
					won = won && (this.n[i][j] == 'x');
				}
				if (won == true) {
					grade = true;
				}
			}
			
			//Vertical win
			for(int i=0;i<3;i++) {
				won = true;
				for(int j=0;j<3;j++) {
					won = won && (this.n[j][i] == 'x');
				}
				if (won == true) {
					grade = true;
				}
			}
			
			//Diagonal Win
			won = (this.n[0][0] == 'x')&&(this.n[1][1] == 'x')&&(this.n[2][2] == 'x');
			if (won == true) {
				grade = true;
			}
			won = (this.n[0][2] == 'x')&&(this.n[1][1] == 'x')&&(this.n[2][0] == 'x');
			if (won == true) {
				grade = true;
			}
			
			
			return grade;
		}
	}
	/**
	 * A private method which allows easier modulation to check if player 2 won.
	 * @return true if player 2 (representing the mark 'o') won.
	 */
	private boolean oWon() {
		synchronized (lock) {
			boolean won;
			
			boolean grade = false;
			
			//Horizontal win
			for(int i=0;i<3;i++) {
				won = true;
				for(int j=0;j<3;j++) {
					won = won && (this.n[i][j] == 'o');
				}
				if (won == true) {
					grade = true;
				}
			}
			
			//Vertical win
			for(int i=0;i<3;i++) {
				won = true;
				for(int j=0;j<3;j++) {
					won = won && (this.n[j][i] == 'o');
				}
				if (won == true) {
					grade = true;
				}
			}
			
			//Diagonal Win
			won = (this.n[0][0] == 'o')&&(this.n[1][1] == 'o')&&(this.n[2][2] == 'o');
			if (won == true) {
				grade = true;
			}
			won = (this.n[0][2] == 'o')&&(this.n[1][1] == 'o')&&(this.n[2][0] == 'o');
			if (won == true) {
				grade = true;
			}
			
			
			return grade;
		}
	}
	

}
