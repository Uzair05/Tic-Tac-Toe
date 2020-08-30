import javax.swing.SwingUtilities;
/**
 * This is the Main Client Application. This class calls both the {@code Controller.java} and the {@code View.java} class to create respective instance variables.<br>
 * Executing this class begins the client application. 
 * @author Uzair Bin Colangoy Asim; UID: 3035603071
 * @version 1.0
 * @since 22/12/2019 
 */
public class T8Client {
	/**
	 * This is the static method main, and is executed upon running.
	 * @param args External Arguments, if any.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * This inner method executes {@code run()} which calls the <i>View</i> and <i>Controller</i> class, hands the <i>view</i> to the <i>controller</i> and calls for the {@code controller.start()} method to begin client functions. 
			 * @Override
			 */
			public void run() {
				View view = new View();
				Controller controller = new Controller(view);
				controller.start();
			}
		});
	}
}
