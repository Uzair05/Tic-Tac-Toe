import java.awt.*;
import javax.swing.*;

/**
 *<p>This Class Helps in creating the GUI interface for the client side application</p>
 * @author Uzair Bin Colangoy Asim; UID: 3035603071
 * @version 1.0
 * @since 22/12/2019 
 */

public class View {
	
	private ImageIcon blank = new ImageIcon("blank.png");
	private ImageIcon circle = new ImageIcon("circle.png");
	private ImageIcon cross = new ImageIcon("cross.png");
	
	private JFrame frame;
	private JPanel panel;
	private JPanel[] compi = new JPanel[3]; 
	private JLabel messg = new JLabel();
	private JTextField input_name = new JTextField(15);
	private JButton submit_name = new JButton("Submit");

	public View() {
		this.setFrame();
	}

	private void setFrame() {
		frame = new JFrame("Tic Tac Toe");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 460);
		frame.setVisible(true);
		frame.add(this.setMainPanel());
		frame.setJMenuBar(this.setMenuBar());
		this.input_name.requestFocus();
		frame.setLocationRelativeTo(null);
	}	
	private JPanel setMainPanel() {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(this.setCompi1());
		panel.add(this.setCompi2());
		panel.add(this.setCompi3());
		
		return panel;
	}
	
	private JPanel setCompi1() {
		compi[0] = new JPanel();
		compi[0].add(messg, BorderLayout.WEST);
		this.setNotifications("Enter Your Player Name: ");
		compi[0].setBackground(Color.pink);
		return compi[0];
	}
	private JPanel setCompi3() {
		compi[2] = new JPanel();
		compi[2].setLayout(new BoxLayout(compi[2], BoxLayout.X_AXIS));
		compi[2].add(this.input_name);
		compi[2].add(this.submit_name);
		return compi[2];
	}
	private JButton[][] box = new JButton[3][3];
	private JPanel setCompi2() {
		compi[1] = new JPanel();
		compi[1].setLayout(new GridLayout(3,3));
		
		for(int n=0;n<3;n++) {
			for(int m=0;m<3;m++) {
				this.box[n][m] = new JButton();
				this.box[n][m].setIcon(this.blank);
			}
		}
		for(int i=0;i<9;i++) {
			compi[1].add(this.box[(int)(i/3)][i%3]);
		}
		
		return compi[1];
	}
	private JMenuBar menuBar = new JMenuBar();
	private JMenu[] menu = new JMenu[2];
	private JMenuItem[] menuItem = new JMenuItem[2];
	private void setMenuItem() {
		this.menuItem[0] = new JMenuItem("Exit");
		this.menuItem[1] = new JMenuItem("Instructions");
	}
	private void setMenu() {
		this.setMenuItem();
		this.menu[0] = new JMenu("Control");
		this.menu[0].add(this.menuItem[0]);
		this.menu[1] = new JMenu("Help");
		this.menu[1].add(this.menuItem[1]);
	}
	private JMenuBar setMenuBar() {
		this.setMenu();
		this.menuBar.add(this.menu[0]);
		this.menuBar.add(this.menu[1]);
		return this.menuBar;
	}

	/**
	 * This method allows the GUI to present an option to quit.<br>
	 * In sense it allows creates an option dialog to confirm if the user wishes to quit and quits if the user agrees. 
	 */
	public void promptExit() {
		int input = JOptionPane.showConfirmDialog(null, "Do you want to exit game?","GameOver",JOptionPane.DEFAULT_OPTION);
		if (input==0) {
			System.exit(0);
		}
	}
	/**
	 * This method allows the GUI to replace the icon of the button by a cross icon.<br>
	 * This is to allow all users to know that it has been marked by player 1. <br>
	 * Ideally, it will only be called in the case of a valid move.
	 * @param x this parameter receives the row number of the box in a 3x3 table.
	 * @param y this parameter receives the column number of the box in a 3x3 table.
	 */
	public void setCross(int x, int y) {
		this.box[x][y].setIcon(this.cross);
	}
	/**
	 * This method allows the GUI to replace the icon of the button by a circle icon.<br>
	 * This is to allow all users to know that it has been marked by player 2. <br>
	 * Ideally, it will only be called in the case of a valid move.
	 * @param x this parameter receives the row number of the box in a 3x3 table.
	 * @param y this parameter receives the column number of the box in a 3x3 table.
	 */
	public void setCircle(int x, int y) {
		this.box[x][y].setIcon(this.circle);
	}
	/**
	 * This method allows for the controller to create a dialog box showing a message.
	 * <i>Game Ends. One of the players left</i><br>
	 * It is ideally used when the server decides that one of the players has exited and the game should end.
	 */
	public void displayPlayerLeftMessage() {
		JOptionPane.showMessageDialog(this.frame,"Game Ends. One of the players left");
	}
	/**
	 * In effect it creates a dialogue box and has it displayed to the user.
	 * @param s This hold the string value with the message being displayed by the controller.<br>
	 */
	public void displayMessage(String s) {
		JOptionPane.showMessageDialog(this.frame,
			    s,
			    "Message",
			    JOptionPane.PLAIN_MESSAGE);
	}
	/**
	 * This Method allows the JButton to be accessed by external classes so that it can modified and manipulated.
	 * @param x this parameter receives the row number of the box JButton in a 3x3 table.
	 * @param y this parameter receives the column number of the box JButton in a 3x3 table.
	 * @return {@code this.box[x][y]} This method returns the JButton so that it can manipulated by controller.
	 */
	public JButton getButton(int x, int y) {
		return this.box[x][y];
	}
	/**
	 * This method allows the controller to call the code {@code System.exit(0)} so that the GUI is exited.
	 * This is ideally used <b>after</b> sending a command to server regarding the exit of the game.
	 */
	public void exitGame(){
		System.exit(0);
	}
	/**
	 * This Method allows the JMenuItems to be accessed by external classes so that it can modified and manipulated.
	 * @param i This parameter holds the index value to either of two JMenuItems
	 * @return {@code menuItem[i]} This method returns the JMenuItem so that it can manipulated by controller.
	 */
	public JMenuItem getMenuItem(int i) {
		if (i==1) {
			return menuItem[1];
		}else {
			return menuItem[0];
		}
	}
	/**
	 * This method returns the JTextField so that it can manipulated by controller.
	 * @return {@code input_name} returns the JTextField {@code input_name}
	 */
	public JTextField getSubmitTextField() {
		return this.input_name;
	}
	/**
	 * This method returns the JButton so that it can manipulated by controller.
	 * @return {@code submit_name} This method returns the JButton {@code submit_name} so that it can manipulated by controller.
	 */
	public JButton getSubmitButton() {
		return this.submit_name;
	}
	/**
	 *In effect, this method uses the command {@code this.messg.setText(s);} to set the text of the JLabel.
	 * @param s This parameter holds the string value which is to be the text displayed in the notifications bar.
	 */
	public void setNotifications(String s) {
		this.messg.setText(s);
	}
	/**
	 *In effect, this method uses the command {@code this.frame.setTitle(s);} to set the title of the JFrame.
	 * @param s This parameter holds the string value which is to be the text displayed in the Frame's title.
	 */
	public void setTitle(String s) {
		this.frame.setTitle(s);
	}
}
