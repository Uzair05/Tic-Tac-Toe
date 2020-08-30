import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
/**
 * This is the Main Client Application. This class calls the {@code Server.java} class to create an instance variable {@code myServer}.<br>
 * Executing this class begins the server application. 
 * @author Uzair Bin Colangoy Asim; UID: 3035603071
 * @version 1.0
 * @since 22/12/2019
 */
public class T8Server {
	/**
	 * This is the static method main, and is executed upon running.<br>
	 * The main aim of this method is to connect to the socket and create an instance of Object Server.
	 * @param args External Arguments, if any.
	 * @throws IOException Throws an exception (<i>run time error</i>) if any errors are occurred during communications. 
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Server is Running...");
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				System.out.println("Server Stopped.");
			}
		}));

		try (var listener = new ServerSocket(6000)) {
			Server myServer = new Server(listener);
			myServer.start();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
