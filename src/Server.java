import java.util.*;
import java.util.concurrent.Executors;
import java.io.*;
import java.net.*;

/**
 * This Class aims in creating the abilities of the server.<br>
 * The server can connect to various clients and allow communication between clients in a game of <i>Tic Tac Toe</i>.
 * <br>The server also has authority to act as a referee of the game.
 * @author Uzair Bin Colangoy Asim; UID: 3035603071
 * @version 1.0
 * @since 22/12/2019
 */

public class Server {
	private ServerSocket serverSocket;
	private SharedNumber number;
	
	private boolean turn = true;//I used boolean as a flag to control whose turn is it

	// The set of all the print writers for all the clients, used for broadcast.
	private Set<PrintWriter> writers = new HashSet<>();
	/**
	 * Constructor method.
	 * @param serverSocket a ServerSocket instance variable aiming in allowing server-client communication.
	 */
	public Server(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
		this.number = new SharedNumber();
	}
	/**
	 * This method aims in simplifying the implementation of server abilities.<br>
	 * It is called by external classes.
	 */
	public void start() {
		var pool = Executors.newFixedThreadPool(200);
		int clientCounters = 0;
		while (true) {
			try {
				Socket socketz = serverSocket.accept();
				pool.execute(new Handler(socketz));
				System.out.println("Connected to client " + clientCounters++);
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	private String player1="";
	private String player2="";
	/**
	 * Internal class, Handles the parsing of messages from the clients and broadcasting general commands to all the clients.<br>
	 * This class, in a nutshell, is the referee of the game.<br>
	 * The game implements the Runnable instance allowing it to multithread.
	 * @author Uzair Bin Colangoy Asim; UID: 3035603071
	 * @version 1.0
	 * @since 22/12/2019
	 */
	public class Handler implements Runnable {
		private Socket socket;
		private Scanner input;
		private PrintWriter output;

		
		/**
		 * Constructor method.
		 * @param socket Instance variable of Socket.<br>Allows for communication between server and clients.
		 */
		public Handler(Socket socket) {
			this.socket = socket;
		}

		/**
		 * This method contains all the actions of the class and parses the communication between the clients an itself.
		 * <br> This method also contains code to store and check marks of various players in attempt to see if moves are legal. It also prevents any illegal moves from being made.
		 * <br> This method also handles the event where a winner is declared (<i>or a Draw</i>) and takes appropriate action.<br>
		 * Ultimately, this method is responsible for handling the event where a player decides to withdraw from the game.
		 * @Override run() the original run() method has been overridden.
		 */
		public void run() {
			System.out.println("Connected: " + socket);
			try {
	
				input = new Scanner(socket.getInputStream());
				output = new PrintWriter(socket.getOutputStream(), true);

				// add this client to the broadcast list
				writers.add(output);

				while (input.hasNextLine()) {
					
					var command = input.nextLine();
					var messg = "";
			
					
					System.out.println("Server Received: " + command);
					
					
					String consult = String.valueOf(command);
					String[] orders = consult.split("-");
					for(int i=0;i<orders.length;i++) {
						orders[i] = orders[i].trim();
						//System.out.println(i +" "+ orders[i]);
					}
					
					
					if(command.startsWith("PlayerName")) {
						if (player1 == "") {
							System.out.println("nameAssigned1");
							player1 = orders[1];
						}else if (player2 == "") {
							System.out.println("nameAssigned2");
							player2 = orders[1];
							messg = "Turn-" + player1;
							turn = true;
						}
					}else if(command.startsWith("ExitGame")) {
						messg = "EndGame";
						player1 = "";
						player2 = "";
						number.resetTable();
					}else if(command.startsWith("Mark")) {
						if(turn) {
							if(number.setCross(Integer.parseInt(orders[1]), Integer.parseInt(orders[2]))){
								messg = "Cross-"+player2+"-"+orders[1]+"-"+orders[2];
								turn = !turn;	
							}
						}else {
							if(number.setCircle(Integer.parseInt(orders[1]), Integer.parseInt(orders[2]))){
								messg = "Circle-"+player1+"-"+orders[1]+"-"+orders[2];
								turn = !turn;	
							}
						}
							
					}
					
					


					// broadcast updated number to all clients
					for (PrintWriter writer : writers) {
						writer.println(messg);
					}
					System.out.println("Server Broadcasted: " + messg);

					
					if(number.someoneWon() != 0) {
						if (number.someoneWon() == 1) {
							messg = "Won-" + player1;
						}else if (number.someoneWon() == 2) {
							messg = "Won-"+player2;
						}else {
							messg = "Won-Draw";
						}
						
						player1 = "";
						player2 = "";
						number.resetTable();
						
						for (PrintWriter writer : writers) {
							writer.println(messg);
						}
						System.out.println("Server Broadcasted: " + messg);
					}
					
					
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				// client disconnected
				if (output != null) {
					writers.remove(output);
				}
			}
		}
	}
}
