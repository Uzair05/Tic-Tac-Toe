import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *<p>This Class Helps in controlling the GUI interface for the client side application. It also completes the task of implementing the functionalities for the client side application.</p>
 *<p>This Class also manages the sending and receiving of messages between client and Server</p>
 * @author Uzair Bin Colangoy Asim; UID: 3035603071
 * @version 1.0
 * @since 22/12/2019 
 */

public class Controller {

	private View view;
	
	private ActionListener submitButton;
	private ActionListener getHelpListener;
	private ActionListener getExitListener;
	private ActionListener[][] boxListener = new ActionListener[3][3];
	
	private boolean gameOver = false; 
	private boolean isTurn = false;
	private String name = "";
	
	private Socket socket;
	private Scanner in;
	private PrintWriter out;

	/**
	 * This is the Constructor of the {@code Controller.java} class.
	 * @param view This parameter refers to the {@code View.java} Class. In effect, it receives <i>view</i> so that it can call its methods and manipulate elements of the GUI.
	 */
	public Controller(View view) {
		this.view = view;
	}

	/**
	 * This is a public method start which implements the setup and functions of the Client GUI.<br>
	 * Certain codes concerning the implementation of sending messages to server is implemented here.
	 */
	public void start() {
		try {
			this.socket = new Socket("127.0.0.1", 6000);
			this.in = new Scanner(socket.getInputStream());
			this.out = new PrintWriter(socket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/**
		 * Inner Class ActionListener submitButton aims in creating an action listener for the submit button.<br>
		 * As in, it aims to enable the button to read the name from the text field and apply appropriate procedures on it and the rest of the GUI. 
		 */
		submitButton = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (view.getSubmitTextField().getText().length() > 0) {
					
					name = view.getSubmitTextField().getText() + Integer.toString(((int)(Math.random()*10000)));
					
					try {
						out.println("PlayerName-" + name);
					}catch(Exception ex) {
						ex.printStackTrace();
					}
					
					
					view.setTitle("Tic Tac Toe-Player: " + view.getSubmitTextField().getText());
					view.setNotifications("WELCOME " + view.getSubmitTextField().getText());
					view.getSubmitTextField().setText("");
					view.getSubmitTextField().setEnabled(false);
					view.getSubmitButton().setEnabled(false);
				}
				
			}
		};
		view.getSubmitButton().addActionListener(submitButton);
		
		/**
		 * Inner Class ActionListener getHelpListener aims in creating an action listener for the help Menu Item.<br>
		 * In effect, it aims to be able to display a dialog box with important information concerning the rules of the game.
		 */
		getHelpListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				view.displayMessage("Some Information about the Game.\nCriteria for a valid move:\n-The move is not occupied by any mark\n-The move is made during a players turn\n-The move is made within a 3x3 board\nThe Game will continue alternatively until one outcome is achived:\n-Player 1 wins\n-Player 2 wins\n-Draw");
			}
		};
		view.getMenuItem(1).addActionListener(getHelpListener);
		
		/** 
		 * Inner Class ActionListener getExitListener aims in creating an action listener for the exit Menu Item.<br>
		 * In effect, it aims to be able to allow the player to exit the game whilst informing the server about its withdrawal from the game.<br>
		 * This ensures that the server can take appropriate actions concerning the withdrawal of a player.
		 */
		getExitListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				
				try {
					out.println("ExitGame");
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
				
				view.exitGame();
				System.exit(0);
			}
		};
		view.getMenuItem(0).addActionListener(getExitListener);
		
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				final String messg = "Mark"+ "-" + i +"-"+j; 
				final int x = i;
				final int y = j;
				/**
				 * Inner Class ActionListener boxListener aims in creating an action listener for the box JButton variables.<br>
				 * In effect, it allows the buttons to send a message to the server that the player wishes to mark a certain box.<br>
				 * Aside from checking for its turn, the action listener leaves it to the server to decide whether or not the action is legal.<br>
				 * Each box JButton is assigned a unique action listener.
				 */
				boxListener[x][y] = new ActionListener() {
					public void actionPerformed(ActionEvent actionEvent) {
						if(isTurn) {
							try {
								out.println(messg);
							}catch(Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				};
				view.getButton(x, y).addActionListener(boxListener[x][y]);
			}
		}
		
		// Creates a new Thread for reading server messages
		Thread handler = new ClientHandlers(socket);
		handler.start();
	}
	/**
	 * This inner class handles the Multi-Threading implementation and communication of client with the server.<br>
	 * Functionalities such as parsing commands from server is implemented here. 
	 * @author Uzair Bin Colangoy Asim; UID: 3035603071
	 * @version 1.0
	 * @since 22/12/2019 
	 */
	class ClientHandlers extends Thread {
		private Socket socket;
		
		/**
		 * Constructor method for this inner class.
		 * @param socket Socket instance to enable connection to server.
		 */
		public ClientHandlers(Socket socket) {
			this.socket = socket;
		}

		
		/**
		 * encapsulates the entire {@code readFromServer()} method in a try catch loop as a safeguard against run time errors.
		 * @Override run() Original method has been overridden.
		 */
		public void run() {
			try {
				readFromServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/**
		 * This method handles the parsing of commands from server and handles each commands respective procedure.
		 * @throws Exception In the event of IO error.
		 */
		public void readFromServer() throws Exception {
			try {
				while (in.hasNextLine()) {
					
					var command = in.nextLine();
					System.out.println("Client Received: " + command);
					out.flush();
					
					
					String consult = String.valueOf(command);
					String[] orders = consult.split("-");
					for(int i=0;i<orders.length;i++) {
						orders[i] = orders[i].trim();
					}
					
					
					
					
					if (command.startsWith("EndGame")) {
						view.displayPlayerLeftMessage();
						gameOver=true;
						view.promptExit();
					}else if(command.startsWith("Cross")) {
						view.setCross(Integer.parseInt(orders[2]), Integer.parseInt(orders[3]));
						if(command.startsWith(name,("Cross-").length()) && !gameOver) {
							isTurn=true;
							view.setNotifications("Your Opponent has moved, it is now your turn");
						}else {
							isTurn=false;
							view.setNotifications("Valid Move, wait for your opponent");
						}
					}else if(command.startsWith("Circle")) {
						view.setCircle(Integer.parseInt(orders[2]), Integer.parseInt(orders[3]));
						if(command.startsWith(name,("Circle-").length()) && !gameOver) {
							isTurn=true;
							view.setNotifications("Your Opponent has moved, it is now your turn");
						}else {
							isTurn=false;
							view.setNotifications("Valid Move, wait for your opponent");
						}
					}else if(command.startsWith("Won")) {
						if(command.startsWith(name,("Won-").length())) {
							view.displayMessage("Congratulations. You Win");
						}else if (command.startsWith("Draw",("Won-").length())){
							view.displayMessage("Draw");
						}else {
							view.displayMessage("You Lose.");
						}
						gameOver = true;
						view.promptExit();
					}else if(command.startsWith("Turn")) {
						if(command.startsWith(name,("Turn-").length())) {
							isTurn=true;
						}else {
							isTurn=false;
						}
					}
					
					
					
					if(name.length()==0) {
						isTurn=false;
					}
					
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				socket.close();
			}
		}
	}

}
