package hw03.ganzj2;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

//ADD CHANGE SHIP LENGTHS

public class BattleshipMain {
	
	static int[][] playerGrid;
	static JButton[][] playerGridButtons;
	static int[][] playerShots;
	static JButton[][] playerShotsButtons;
	static String opponentsMove;
	static String playersMove;
	static Server server;
	static Client client;
	static String hostName = "battleshipgame";
	static boolean exit = false;
	static boolean game = true; //boolean for current game status
	static JPanel playerPanel;
	static JPanel shotsPanel;
	static JPanel movePanel;
	static JFrame gframe;
	static int start_x;
	static int start_y;
	static int direction;
	static boolean connect;
	static int last_x = 1;
	static int last_y = 1;
	static int x = 1;
	static int y = 1;
	static boolean curHost = false;
	static String playerName = "player";
	static String opponentName = "player";
	static int playerWins = 0;
	static int opponentWins = 0;
	static int[] ships = new int[Menubar.numShips];
	
	
	/**
	 * 
	 * @param args - String array of command line arguments
	 * @throws InterruptedException - Exception handled manually
	 * @throws IOException - Exception handled manually
	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		
		boolean connect = false; //By Default
		connect = getLoadChoice(); //Boolean deciding whether user wants to connect or wait to be connected to
		if(connect == false) { //Attempt to connect to another user
			try {
				if(Menubar.defaultIP == null) {
					String ip = JOptionPane.showInputDialog("Please enter the IP you would like to connect to.");
					Menubar.defaultIP = ip;
				}
				client = new Client(Menubar.defaultIP, Menubar.defaultPort); //Attempting to connect to another users server
			}
			catch (IOException e) {
			}
		}
		else { //Wait for another user to connect to you
			server = new Server(Menubar.defaultPort); //creating new server for other users to connect to
			System.out.println("TEST");
		}
		
		playerName = JOptionPane.showInputDialog("Please enter a name", "Player");
		if(connect == true) { //if current player is host
			Server.sendName(playerName);
			opponentName = Server.receiveName();
		}
		else {
			Client.sendName(playerName);
			opponentName = Client.receiveName();
		}

		playerGrid = new int[10][10]; //Initializing basic grids and ship data
		playerGridButtons = new JButton[11][11];
		playerShots = new int[10][10];
		playerShotsButtons = new JButton[11][11];
		ships[0] = 5;
		ships[1] = 4;
		ships[2] = 3;
		ships[3] = 3;
		ships[4] = 2;
		Menubar.shipLengths = ships;
		
		if(connect == true) { //if the user is the host, checking to see if there is a Preferences file to load if yes, send data to client
			BufferedReader bw = null;
			try {
				bw = new BufferedReader(new FileReader("myPreferences.txt"));
				readPrefs();
				if(connect == true) {
					Server.sendFile(true);
				}
			} catch (FileNotFoundException e) {
				Server.sendFile(false);
			}
		}
		else {
			Client.receiveFile();
		}
		
		initializeGrids(); 
		
		gframe = buildUI(); //building game frame
		
		boolean starter = false;
		
		System.out.println("Generating Random Number between 0 and 1 to decide who starts the game!");
		if(connect) {
			int turn = new Random().nextInt(2);
			
			if(turn == 1) {
				System.out.println("A '1' was Generated! The Host will start the game.");
				Server.sendStart(false, false);
				starter = true;
			}
			else {
				System.out.println("A '0' was Generated! The Client will start the game.");
				Server.sendStart(false, true);
			}
		}
		else {
			Boolean[] a = Client.receiveStart();
			connect = a[0];
			starter = a[1];
			if(starter == true) {
				System.out.println("A '0' was Generated! The Client will start the game.");
			}
			else {
				System.out.println("A '1' was Generated! The Host will start the game.");
			}
		}
			
		System.out.println("But first both players must place their ships!");
		
		boolean ready = false;
		if(Menubar.updateLengths == true) {
			Menubar.updateLengths = false;
				ships = new int[Menubar.numShips];
				for(int i = 0; i < ships.length; i++) { //If number of ships is off from default, make user enter ship lengths
					int temp = 3;
					int length = temp;
					try {
						 length = Integer.parseInt(JOptionPane.showInputDialog("Please enter an integer ship length greater than 0 and less than 10."));
						if((length <= 0) || (length > 10)) {
							System.out.println("Invalid Input. Using Saved Default: " + temp);
							length = temp;
						}
					}
					catch (Exception f) {
						System.out.println("Invalid Input. Using Saved Default: " + length);
					}
					ships[i] = length;
				}
		}
		if(connect == true) { //if user is host, send set ship length parameters to client
			Server.sendShips(ships);
		}
		else {
			ships = Client.receiveShips();
		}
		placeShips(ships);  //placing the ships on the board
		while(ready == false) {
			if(connect == true) { //if current player is host, checking to see if both players are ready to advance
				Server.sendCheck("true");
				ready = Server.receiveCheck();
			}
			else {
				Client.sendCheck("true");
				ready = Client.receiveCheck();
			}
		}
		exit = false;
		
		while(!exit) { //while a user has not decided to leave
			System.out.println("ENTERING GAME");
			int temp = runGame(connect,starter);
			if(temp == 1) {
				playerWins++;
				System.out.println("CONGRATS YOU WON!");
			}
			else {
				opponentWins++;
				System.out.println("Tough loss! Better Try Again!");
			}
			playerGrid = new int[10][10]; //resetting game
			playerGridButtons = new JButton[11][11];
			playerShots = new int[10][10];
			playerShotsButtons = new JButton[11][11];
			initializeGrids();
			gframe = buildUI();
			updateUI();
			placeShips(ships);
			while(ready == false) {
				if(connect == true) { //if current player is host
					Server.sendCheck("true");
					ready = Server.receiveCheck();
				}
				else {
					Client.sendCheck("true");
					ready = Client.receiveCheck();
				}
			}
			updateUI();
			if(Menubar.savePrefs == true) {
				savePrefs();
			}
		}
	}
	
	/**
	 * This function creates the UI for the game
	 * @return JFrame object containing the UI for the entire battleship game
	 */
	public static JFrame buildUI() {
		var frame = new GameFrame(); //creating new GameFrame instance
		frame.setLayout(new BorderLayout());
		
		playerPanel = new GamePanel(playerGrid, playerGridButtons, playerName); //Creating Game Grids
		playerPanel.setBackground(Color.green);
		
		shotsPanel = new GamePanel(playerShots, playerShotsButtons, opponentName);

		movePanel = new ControlPanel();
		ControlPanel.time.setText("Time: 0");
		ControlPanel.playerScore.setText("Score: " + playerWins);
		ControlPanel.opponentScore.setText("Score: " + opponentWins);

		final JPanel gamePanels = new JPanel(new GridLayout(0,2));
		gamePanels.add(playerPanel);
		gamePanels.add(shotsPanel);
		frame.add(gamePanels, BorderLayout.CENTER);
		frame.add(movePanel, BorderLayout.SOUTH);
		
		frame.pack();
		frame.setVisible(true);
		
		return frame;
	}
	
	/**
	 * This function utilizes global variables to update the UI at any time, similar to a tick in a game
	 */
	public static void updateUI() {
		for(int i = 1; i < 11; i++) {
			for(int j = 1; j < 11; j++) {
				//System.out.print(playerGrid[i][j] + " ");
				if(playerGrid[i-1][j-1] == 0) { //Cell is empty
					playerGridButtons[i][j].setBackground(Menubar.blankColor);
				}
				else if(playerGrid[i-1][j-1] == 1) { //Cell holds users ship
					playerGridButtons[i][j].setBackground(Menubar.shipColor);
				}
				else if(playerGrid[i-1][j-1] == 2) { //Cell holds hit enemy ship
					playerGridButtons[i][j].setBackground(Menubar.hitColor);
				}
				else if(playerGrid[i-1][j-1] == 3) { //Cell holds missed shot
					playerGridButtons[i][j].setBackground(Menubar.missColor);
				}
				else { //Cell holds users hit ship
					playerGridButtons[i][j].setBackground(Menubar.shipHitColor);
				}
				
				if(playerShots[i-1][j-1] == 1) {
					playerShotsButtons[i][j].setBackground(Menubar.hitColor);
				}
				else if (playerShots[i-1][j-1] == 2){
					playerShotsButtons[i][j].setBackground(Menubar.missColor);
				}
				else {
					playerShotsButtons[i][j].setBackground(Menubar.blankColor);
				}
			}
		}
	}
	
	/**
	 * Function that gets network choice from user.
	 * @return Boolean that contains the decision of the user's network choice
	 */
	public static boolean getLoadChoice() {
		String[] options = {"Connect to user", "Wait for user to connect to you"};
		int x = JOptionPane.showOptionDialog(null, "Would you like to connect to a user or wait for a user to connect to you?",
                "Connection Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
		if(x == 0) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * Function that initializes values for all grid variables
	 */
	public static void initializeGrids() {
		
		String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};;
		
		for(int i = 0; i < 11; i++) {
			for(int j = 0; j < 11; j++) {
				if(i != 10 && j != 10) {
					playerGrid[i][j] = 0;
					playerShots[i][j] = 0;
				}
				playerGridButtons[i][j] = new JButton();
				playerShotsButtons[i][j] = new JButton();
			}
		}
		for(int i = 1; i < 11; i++) {
			playerGridButtons[i][0].setText(Integer.toString(i));
			playerGridButtons[0][i].setText(letters[i-1]);
			playerShotsButtons[i][0].setText(Integer.toString(i));
			playerShotsButtons[0][i].setText(letters[i-1]);
		}
	}
	
	/**
	 * Function each individual round of the game
	 * @param curHost - Boolean containing information of who the current host is
	 * @param start - Boolean containing information of who is starting the game 
	 * @return - Integer containing information on who won the game
	 * @throws InterruptedException - Exception is handled manually
	 * @throws IOException - Exception is handled manually
	 */
	public static int runGame(boolean curHost, boolean start) throws InterruptedException, IOException {
		int[] shot = new int[2];
		int result = 0;
		int winner = 0;
		
		System.out.println("GAME STARTED");
		game = true;
		while(game == true) { //while the game is running 
			if(Menubar.savePrefs == true) {
				savePrefs();
			}
				if(start == true && curHost == true) { //current player starts game and is host
					JOptionPane.showMessageDialog(gframe, "It is now your turn!");
					shot = selectShot();
					if(shot[0] != -1) {
						Server.sendShot(shot);
						result = Server.receiveResult();
						if(result == 1) {
							playerShots[shot[0]][shot[1]] = 1; //if player shot was a hit
						}
						else {
							playerShots[shot[0]][shot[1]] = 2; //if player shot was a miss
						}
					}
					else {
						System.out.println("Time's Up! Turn Skipped!");
						Server.sendShot(shot);
						result = Server.receiveResult();
					}
					updateUI();
					winner = determineWinner(); //check if -1
					if(winner == 1) { //game has been won.
						System.out.println("Congrats! You have won!");
						return 1;
					}
					if(winner == 2) { //game has been won by opponent
						System.out.println("Better luck next time!");
						return 2;
					}
					shot = Server.receiveShot();
					if(shot[0] != -1) {
						result = determineShot(shot);
						if(result == 1) {
							playerGrid[shot[0]][shot[1]] = 4; //if opponent shot was a hit
						}
						else {
							playerGrid[shot[0]][shot[1]] = 3;
						}
						Server.sendResult(result);
					}
					else {
						Server.sendResult(0);
					}
					updateUI();
					winner = determineWinner(); //check if -1
					if(winner == 1) { //game has been won.
						System.out.println("Congrats! You have won!");
						return 1;
					}
					if(winner == 2) { //game has been won by opponent
						System.out.println("Better luck next time!");
						return 2;
					}
				}
				else if(start == false && curHost == true) { //other player starts first but current player is host
					shot = Server.receiveShot();
					System.out.println("RECEIVED SHOT: " + shot[0] + ", " + shot[1]);
					if(shot[0] != -1) {
						result = determineShot(shot);
						if(result == 1) {
							playerGrid[shot[0]][shot[1]] = 4; //if opponent shot was a hit
						}
						else {
							playerGrid[shot[0]][shot[1]] = 3;
						}
						Server.sendResult(result);
					}
					else {
						Server.sendResult(0);
					}
					updateUI();
					winner = determineWinner(); //check if -1
					if(winner == 1) { //game has been won.
						System.out.println("Congrats! You have won!");
						return 1;
					}
					if(winner == 2) { //game has been won by opponent
						System.out.println("Better luck next time!");
						return 2;
					}
					JOptionPane.showMessageDialog(gframe, "It is now your turn!");
					shot = selectShot();
					if(shot[0] != -1) {
						Server.sendShot(shot);
						result = Server.receiveResult();
						if(result == 1) {
							playerShots[shot[0]][shot[1]] = 1; //if player shot was a hit
						}
						else {
							playerShots[shot[0]][shot[1]] = 2; //if player shot was a miss
						}
					}
					else {
						System.out.println("Time's Up! Turn Skipped!");
						Server.sendShot(shot);
						result = Server.receiveResult();
					}
					updateUI();
					winner = determineWinner(); //check if -1
					if(winner == 1) { //game has been won.
						System.out.println("Congrats! You have won!");
						return 1;
					}
					if(winner == 2) { //game has been won by opponent
						System.out.println("Better luck next time!");
						return 2;
					}
				}
				else if(start == false && curHost == false) { //other player starts and other player is host
					shot = Client.receiveShot();
					if(shot[0] != -1) {
						result = determineShot(shot);
						if(result == 1) {
							playerGrid[shot[0]][shot[1]] = 4; //if opponent shot was a hit
						}
						else {
							playerGrid[shot[0]][shot[1]] = 3;
						}
						Client.sendResult(result);
					}
					else {
						Client.sendResult(0);
					}
					updateUI();
					winner = determineWinner(); //check if -1
					if(winner == 1) { //game has been won.
						System.out.println("Congrats! You have won!");
						return 1;
					}
					if(winner == 2) { //game has been won by opponent
						System.out.println("Better luck next time!");
						return 2;
					}
					JOptionPane.showMessageDialog(gframe, "It is now your turn!");
					shot = selectShot();
					if(shot[0] != -1) {
						Client.sendShot(shot);
						result = Client.receiveResult();
						if(result == 1) {
							playerShots[shot[0]][shot[1]] = 1; //if player shot was a hit
						}
						else {
							playerShots[shot[0]][shot[1]] = 2; //if player shot was a miss
						}
					}
					else {
						System.out.println("Time's Up! Turn Skipped!");
						Client.sendShot(shot);
						result = Client.receiveResult();
					}
					updateUI();
					winner = determineWinner(); //check if -1
					if(winner == 1) { //game has been won.
						System.out.println("Congrats! You have won!");
						return 1;
					}
					if(winner == 2) { //game has been won by opponent
						System.out.println("Better luck next time!");
						return 2;
					}
				}
				else { //other player is host but current player starts
					JOptionPane.showMessageDialog(gframe, "It is now your turn!");
					shot = selectShot();
					if(shot[0] != -1) {
						Client.sendShot(shot);
						result = Client.receiveResult();
						if(result == 1) {
							playerShots[shot[0]][shot[1]] = 1; //if player shot was a hit
						}
						else {
							playerShots[shot[0]][shot[1]] = 2; //if player shot was a miss
						}
					}
					else {
						System.out.println("Time's Up! Turn Skipped!");
						Client.sendShot(shot);
						result = Client.receiveResult();
					}
					updateUI();
					winner = determineWinner(); //check if -1
					if(winner == 1) { //game has been won.
						System.out.println("Congrats! You have won!");
						return 1;
					}
					if(winner == 2) { //game has been won by opponent
						System.out.println("Better luck next time!");
						return 2;
					}
					shot = Client.receiveShot();
					if(shot[0] != -1) {
						result = determineShot(shot);
						if(result == 1) {
							playerGrid[shot[0]][shot[1]] = 4; //if opponent shot was a hit
						}
						else {
							playerGrid[shot[0]][shot[1]] = 3;
						}
						Client.sendResult(result);
					}
					else {
						Client.sendResult(0);
					}
					updateUI();
					winner = determineWinner(); //check if -1
					if(winner == 1) { //game has been won.
						System.out.println("Congrats! You have won!");
						return 1;
					}
					if(winner == 2) { //game has been won by opponent
						System.out.println("Better luck next time!");
						return 2;
					}
				}
		}
		return 0;
	}
	
	/**
	 * Function used to place the ships on the user's grid
	 * @param a - Integer array containing the information of all ships to be placed
	 */
	public static void placeShips(int[] a){
		
		ArrayList<String> ships = new ArrayList<String>(a.length); //creating an Arraylist of all ships
		for(int i = 0; i < a.length; i++) {
			ships.add("Ship of " + a[i] + " length");
		}
		
		int choice = -1;
		int shipchoice = -1;
		
		while(!ships.isEmpty()) {
			choice = JOptionPane.showOptionDialog(null, "Select Ship to Place", "Ships", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, ships.toArray(), null);
			
			switch(choice) { //The users' choice on which ship to place
			case 0:
				shipchoice = findShip((String) ships.toArray()[0]);
				placeShip(shipchoice);
				ships.remove(ships.toArray()[0]);
				break;
			case 1:
				shipchoice = findShip((String) ships.toArray()[1]);
				placeShip(shipchoice);
				ships.remove(ships.toArray()[1]);
				break;
			case 2:
				shipchoice = findShip((String) ships.toArray()[2]);
				placeShip(shipchoice);
				ships.remove(ships.toArray()[2]);
				break;
			case 3:
				shipchoice = findShip((String) ships.toArray()[3]);
				placeShip(shipchoice);
				ships.remove(ships.toArray()[3]);
				break;
			case 4:
				shipchoice = findShip((String) ships.toArray()[4]);
				placeShip(shipchoice);
				ships.remove(ships.toArray()[4]);
				break;
			}
		}
	}
	
	/**
	 * Function to send the integer value of the ship length selected
	 * @param s - String of the users choice
	 * @return - Integer of the users choice
	 */
	public static int findShip(String s) {
		
		String[] temp = s.split(" ");
		return Integer.parseInt(temp[2]);

	}
	
	/**
	 * Function to place the users current ship, uses multiple other functions to do this
	 * @param s - Integer of the current ships length
	 */
	public static void placeShip(int s) {
		start_x = -1;
		start_y = -1;
		direction = 1;
		int counter = 0;
		int flag = 0;
		boolean free = false;
			while(ControlPanel.exit == false) {
				while(free == false) {
					free = defaultSpace(s);
				}
				
				for(int i = 0; i < s; i++) {
					switch(direction) {
					case 0:
						playerGrid[start_y - i][start_x] = 1;
						break;
					case 1:
						playerGrid[start_y][start_x + i] = 1;
						break;
					case 2:
						playerGrid[start_y + i][start_x] = 1;
						break;
					case 3:
						playerGrid[start_y][start_x - 1] = 1;
						break;
					}
				}
				
				updateUI();
				if(ControlPanel.rot == true) {  //if the user wants to rotate the ship
					direction = rotateShip(s, direction);
					ControlPanel.rot = false;
				}
				
				if(ControlPanel.d == true) { //if the user wants to move the ship down
					System.out.println("Ship Moved Down!");
					moveDown(s, direction);
					ControlPanel.d = false;
				}
				if(ControlPanel.r == true) { //if the user wants to move the ship right
					System.out.println("Ship Moved Right!");
					moveRight(s, direction);
					ControlPanel.r = false;
				}
				if(ControlPanel.u == true) { //if the user wants to move the ship up
					System.out.println("Ship Moved Up!");
					moveUp(s, direction);
					ControlPanel.u = false;
				}
				if(ControlPanel.l == true) { //if the user wants to move the ship left
					System.out.println("Ship Moved Left!");
					moveLeft(s, direction);
					ControlPanel.l = false;
				}
			}
			ControlPanel.exit = false;
		}
	
	/**
	 * Function used to rotate the current ship, using current ships location, length, and direction, it is determined whether the ship can rotate
	 * @param s - Integer of current ships length
	 * @param d - Integer of the current ship direction
	 * @return Integer returning new direction
	 */
	public static int rotateShip(int s, int d) { 
		boolean can_rotate = true;
		switch(d) {
		case 0: //pointing upwards
			for(int i = 1; i < s; i++) {
				try {
					if(playerGrid[start_y][start_x + i] == 1) {
						can_rotate = false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Ship cannot be rotated!");
					can_rotate = false;
					break;
				}
			}
			if(can_rotate == true) {
				for(int i = 1; i < s; i++) {
					playerGrid[start_y - i][start_x] = 0;
					playerGrid[start_y][start_x + i] = 1;
				}
				d = 1;
				System.out.println("Ship rotated!");
			}
			updateUI();
			can_rotate = true;
			break;
		case 1: //pointing right
			for(int i = 1; i < s; i++) {
				try {
					if(playerGrid[start_y + i][start_x] == 1) {
						can_rotate = false;
						System.out.println("Cannot Rotate!");
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Ship cannot be rotated!");
					can_rotate = false;
					break;
				}
			}
			if(can_rotate == true) {
				for(int i = 1; i < s; i++) {
					playerGrid[start_y][start_x + i] = 0;
					playerGrid[start_y + i][start_x] = 1;
				}
				d = 2;
				System.out.println("Ship rotated!");
			}
			updateUI();
			can_rotate = true;
			break;
		case 2: //pointing down
			for(int i = 1; i < s; i++) {
				try {
					if(playerGrid[start_y][start_x - i] == 1) {
						can_rotate = false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Ship cannot be rotated!");
					can_rotate = false;
					break;
				}
			}
			if(can_rotate == true) {
				for(int i = 1; i < s; i++) {
					playerGrid[start_y + i][start_x] = 0;
					playerGrid[start_y][start_x - i] = 1;
				}
				d = 3;
				System.out.println("Ship rotated!");
			}
			updateUI();
			can_rotate = true;
			break;
		case 3: //pointing left
			for(int i = 1; i < s; i++) {
				try {
					if(playerGrid[start_y - i][start_x] == 1) {
						can_rotate = false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Ship cannot be rotated!");
					can_rotate = false;
					break;
				}
			}
			if(can_rotate == true) {
				for(int i = 1; i < s; i++) {
					playerGrid[start_y][start_x - i] = 0;
					playerGrid[start_y - i][start_x] = 1;
				}
				d = 0;
				System.out.println("Ship rotated!");
			}
			updateUI();
			can_rotate = true;
			break;
		}
		
		return d;
	}
	
	/**
	 * Function used to move the current ship down, using the ships current position it is determined whether or not the ship can move down
	 * @param s - Integer of current ships length
	 * @param d - Integer of current ships direction
	 */
	public static void moveDown(int s, int d) { 
		boolean can_move = true;
		boolean error = false;
		switch(d) {
		case 0: //facing upward
			try {
				if(playerGrid[start_y + 1][start_x] == 0) {
					playerGrid[start_y - s + 1][start_x] = 0;
					playerGrid[start_y + 1][start_x] = 1;
					System.out.println("Ship moved down!");
					start_y = start_y + 1;
				}
				else {
					System.out.println("Ship cannot move down!");
				}
			}
			catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Ship cannot move down!");
				break;
			}
			updateUI();
			break;
		case 1: //facing right
			for(int i = 0; i < s; i++) {
				try {
					if(playerGrid[start_y+1][start_x + i] == 1) {
						can_move = false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Ship cannot move down!");
					can_move = false;
					error = true;
					break;
				}
			}
			if(can_move == true) {
				for(int i = 0; i < s; i++) {
					playerGrid[start_y][start_x + i] = 0;
					playerGrid[start_y + 1][start_x + i] = 1;
				}
				System.out.println("Ship moved down!");
				start_y = start_y + 1;
			}
			else {
				if(error == false) {
					System.out.println("Ship cannot move down!");
				}
			}
			updateUI();
			break;
		case 2: //facing downward
			try {
				if(playerGrid[start_y + s][start_x] == 0) {
					playerGrid[start_y][start_x] = 0;
					playerGrid[start_y + s][start_x] = 1;
					System.out.println("Ship moved down!");
					start_y = start_y + 1;
				}
				else {
					System.out.println("Ship cannot move down!");
				}
			}
			catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Ship cannot move down!");
				break;
			}
			updateUI();
			break;
		case 3: //facing left
			for(int i = 0; i < s; i++) {
				try {
					if(playerGrid[start_y+1][start_x - i] == 1) {
						can_move = false;
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Ship cannot move down!");
					can_move = false;
					error = true;
					break;
				}
			}
			if(can_move == true) {
				for(int i = 0; i < s; i++) {
					playerGrid[start_y][start_x - i] = 0;
					playerGrid[start_y + 1][start_x - i] = 1;
				}
				System.out.println("Ship moved down!");
				start_y = start_y + 1;
			}
			else {
				if(error == false) {
					System.out.println("Ship cannot move down!");
				}
			}
			updateUI();
			break;
		}
	}
	
	/**
	 * Function for moving the current ship right, using the ships current position to determine whether the ship can move right
	 * @param s - Integer for the current ships length
	 * @param d - Integer for the current ships direction
	 */
	public static void moveRight(int s, int d) {
		boolean can_move = true;
		boolean error = false;
		switch(d) {
		case 0: //facing upward
			for(int i = 0; i < s; i ++) {
				try {
					if(playerGrid[start_y - i][start_x + 1] == 1) {
						can_move = false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Ship cannot move right!");
					error = true;
					can_move = false;
					break;
				}
			}
			if(can_move == true) {
				for(int i = 0; i < s; i++) {
					playerGrid[start_y - i][start_x] = 0;
					playerGrid[start_y - i][start_x + 1] = 1;
				}
				System.out.println("Ship moved right!");
				start_x = start_x + 1;
			}
			else {
				if(error == false) {
					System.out.println("Ship cannot move right!");
				}
			}
			updateUI();
			break;
		case 1:
			try {
				if(playerGrid[start_y][start_x + s] != 1) { //if ship can move right
					playerGrid[start_y][start_x] = 0;
					playerGrid[start_y][start_x + s] = 1;
					System.out.println("Ship moved right!");
					start_x = start_x + 1;
				}
				else {
					System.out.println("Ship cannot move right!");
				}
			}
			catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Ship cannot move right!");
				break;
			}
			updateUI();
			break;
		case 2: // facing downward
			for(int i = 0; i < s; i ++) {
				try {
					if(playerGrid[start_y + i][start_x + 1] == 1) {
						can_move = false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Ship cannot move right!");
					can_move = false;
					error = true;
					break;
				}
			}
			if(can_move == true) {
				for(int i = 0; i < s; i++) {
					playerGrid[start_y + i][start_x] = 0;
					playerGrid[start_y + i][start_x + 1] = 1;
				}
				System.out.println("Ship moved right!");
				start_x = start_x + 1;
			}
			else {
				if(error == false) {
					System.out.println("Ship cannot move right!");
				}
			}
			updateUI();
			break;
		case 3: //ship pointed left
			try {
				if(playerGrid[start_y][start_x + 1] != 1) { //if ship can move right
					playerGrid[start_y][start_x - s + 1] = 0;
					playerGrid[start_y][start_x + 1] = 1;
					start_x = start_x + 1;
					System.out.println("Ship moved right!");
				}
				else {
					System.out.println("Ship cannot move right!");
				}
			}
			catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Ship cannot move right!");
			}
			updateUI();
			break;
		}
	}
	
	/**
	 * Function for moving the current ship down, using the positions of the ship to determine if the ship can move up
	 * @param s - Integer of current ships length
	 * @param d - Integer of current ships direction
	 */
	public static void moveUp(int s, int d) {
		boolean can_move = true;
		boolean error = false;
		switch(d) {
		case 0: //facing upward
			try {
				if(playerGrid[start_y - s][start_x] != 1) {
					playerGrid[start_y][start_x] = 0;
					playerGrid[start_y - s][start_x] = 1;
					System.out.println("Ship moved up!");
					start_y = start_y - 1;
				}
				else {
					System.out.println("Ship cannot move up!");
				}
			}
			catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Ship cannot move up!");
			}
			updateUI();
			break;
		case 1: //facing right
			for(int i = 0; i < s; i++) {
				try {
					if(playerGrid[start_y - 1][start_x + i] == 1) {
						can_move = false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Ship cannot move up!");
					can_move = false;
					error = true;
					break;
				}
			}
			if(can_move == true) {
				for(int i = 0; i < s; i++) {
					playerGrid[start_y][start_x + i] = 0;
					playerGrid[start_y - 1][start_x + i] = 1;
				}
				start_y  = start_y - 1;
				System.out.println("Ship moved up!");
			}
			else {
				if(error == true) {
					System.out.println("Ship cannot move up!");
				}
			}
			updateUI();
			break;
		case 2: //facing down
			try {
				if(playerGrid[start_y - 1][start_x] != 1) {
					playerGrid[start_y + s - 1][start_x] = 0;
					playerGrid[start_y - 1][start_x] = 1;
					start_y = start_y - 1;
					System.out.println("Ship moved up!");
				}
				else {
					System.out.println("Ship cannot move up!");
				}
			}
			catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Ship cannot move up!");
			}
			updateUI();
			break;
		case 3: //facing left
			for(int i = 0; i < s; i++) {
				try {
					if(playerGrid[start_y - 1][start_x - i] == 1) {
						can_move = false;
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Ship cannot move up!");
					can_move = false;
					error = true;
					break;
				}
			}
			if(can_move == true) {
				for(int i = 0; i < s; i++) {
					playerGrid[start_y][start_x - i] = 0;
					playerGrid[start_y - 1][start_x - i] = 1;
				}
				start_y = start_y - 1;
				System.out.println("Ship moved up!");
			}
			else {
				if(error == false) {
					System.out.println("Ship cannot move up!");
				}
			}
			updateUI();
			break;
		}
	}
	
	/**
	 * Function for moving the current ship left, using the ships position to determine if the ship can move left
	 * @param s - Integer for the current ships length
	 * @param d - Integer for the current ships direction
	 */
	public static void moveLeft(int s, int d) {
		boolean can_move = true;
		boolean error = false;
		switch(d) {
		case 0: //facing upward
			for(int i = 0; i < s; i ++) {
				try {
					if(playerGrid[start_y - i][start_x - 1] == 1) {
						can_move = false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Ship cannot move left!");
					can_move = false;
					error = true;
					break;
				}
			}
			if(can_move == true) {
				for(int i = 0; i < s; i++) {
					playerGrid[start_y - i][start_x] = 0;
					playerGrid[start_y - i][start_x - 1] = 1;
				}
				start_x = start_x - 1;
				System.out.println("Ship moved left!");
			}
			else {
				if(error == false) {
					System.out.println("Ship cannot move left!");
				}
			}
			updateUI();
			break;
		case 1: //facing right
			try {
				if(playerGrid[start_y][start_x - 1] != 1) { //if ship can move right
					playerGrid[start_y][start_x + s - 1] = 0;
					playerGrid[start_y][start_x - 1] = 1;
					start_x = start_x - 1;
					System.out.println("Ship moved left!");
				}
				else {
					System.out.println("Ship cannot move left!");
				}
			}
			catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Ship cannot move left!");
				break;
			}
			updateUI();
			break;
		case 2: // facing downward
			for(int i = 0; i < s; i++) {
				try {
					if(playerGrid[start_y + i][start_x - 1] == 1) {
						can_move = false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Ship cannot move left!");
					can_move = false;
					error = true;
					break;
				}
			}
			if(can_move == true) {
				for(int i = 0; i < s; i++) {
					playerGrid[start_y + i][start_x] = 0;
					playerGrid[start_y + i][start_x - 1] = 1;
				}
				start_x = start_x - 1;
				System.out.println("Ship moved left!");
			}
			else {
				if(error == false) {
					System.out.println("Ship cannot move left!");
				}
			}
			updateUI();
			break;
		case 3:
			try {
				if(playerGrid[start_y][start_x - s] != 1) { //if ship can move right
					playerGrid[start_y][start_x] = 0;
					playerGrid[start_y][start_x - s] = 1;
					start_x = start_x - 1;
					System.out.println("Ship moved left!");
				}
				else {
					System.out.println("Ship cannot move left!");
				}
			}
			catch(ArrayIndexOutOfBoundsException e) {
				System.out.println("Ship cannot move left!");
			}
			updateUI();
			break;
		}
	}
	
	/**
	 * Function that finds a free space the ship selected to be first placed at
	 * @param s - Integer containing ship length
	 * @return = Boolean stating whether or not the ship was placed
	 */
	public static boolean defaultSpace(int s) {
		int counter = 0;
		
		switch(direction) {
		case 0: //facing upward
			for(int i = s; i < 11; i++) { //Looping through board to see if the ship can be placed
				for(int j = 1; j < 11; j++) {
					for(int k = 0; k < s; k++) {
						if(playerGrid[i - k][j] == 0) {
							counter++;
						}
					}
					if(counter == s) {
						start_y = i;
						start_x = j;
						return true;
					}
					counter = 0;
				}
			}
			direction++; //if ship cannot be placed change direction and try again
			defaultSpace(s);
			break;
		case 1: //facing right 
			for(int i = 1; i < 11; i++) {
				for(int j = 1; j < 11 - s; j++) {
					for(int k = 0; k < s; k++) {
						if(playerGrid[i][j + k] == 0) {
							counter++;
						}
					}
					if(counter == s) {
						start_y = i;
						start_x = j;
						return true;
					}
					counter = 0;
				}
			}
			direction++;
			defaultSpace(s);
			break;
		case 2: //facing down
			for(int i = 1; i < 11 - s; i++) {
				for(int j = 1; j < 11; j++) {
					for(int k = 0; k < s; k++) {
						if(playerGrid[i + k][j] == 0) {
							counter++;
						}
					}
					if(counter == s) {
						start_y = i;
						start_x = j;
						return true;
					}
					counter = 0;
				}
			}
			direction++;
			defaultSpace(s);
			break;
		case 3: //facing left
			for(int i = 1; i < 11; i++) {
				for(int j = s; j < 11; j++) {
					for(int k = 0; k < s; k++) {
						if(playerGrid[i][j - k] == 0) {
							counter++;
						}
					}
					if(counter == s) {
						start_y = i;
						start_x = j;
						return true;
					}
					counter = 0;
				}
			}
			direction = 1;
			defaultSpace(s);
			break;
		}
		return false;
	}
	
	/**
	 * Function used to determine the user's shot for their turn
	 * @return - Integer Array containing the coordinates for the shot
	 * @throws InterruptedException - Exception handled manually
	 */
	public static int[] selectShot() throws InterruptedException {
		System.out.println("It is your turn to select a shot! Press 'End Turn' when finished."); //if shot 1,1 place default shot elsewhere
		
		int[] temp = new int[2];
		
		int counter = Menubar.turnTime*10;
		
		defaultShot();
		playerShots[y][x] = 1;
		temp[0] = y;
		temp[1] = x;
		System.out.println("SELECTING SHOT");
		updateUI();
		ControlPanel.exit = false;
		ControlPanel.r = false;
		ControlPanel.u = false;
		ControlPanel.l = false;
		ControlPanel.d = false;
		
		while(ControlPanel.exit == false && counter > 0) { //while the user has not ended the turn and the time limit has not been exceeded
			if(ControlPanel.u == true) { // if the shot should move up
				try {
					if(playerShots[y-1][x] == 0) {
						playerShots[y][x] = 0;
						y = y - 1;
						ControlPanel.u = false;
						playerShots[y][x] = 1;
						updateUI();
						temp[0] = y;
						temp[1] = x;
					}
					else {
						System.out.println("Cannot move up!");
						ControlPanel.u = false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Cannot move up!");
					ControlPanel.u = false;
				}
			}
			if(ControlPanel.r == true) { //if the shot should move right
				try {
					if(playerShots[y][x+1] == 0) {
						playerShots[y][x] = 0;
						x = x + 1;
						ControlPanel.r = false;
						playerShots[y][x] = 1;
						updateUI();
						temp[0] = y;
						temp[1] = x;
					}
					else {
						System.out.println("Cannot move right!");
						ControlPanel.r = false;
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Cannot move right!");
					ControlPanel.r = false;
				}
			}
			if(ControlPanel.d == true) { //if the shot should move down
				try {
					if(playerShots[y+1][x] == 0) {
						playerShots[y][x] = 0;
						y = y + 1;
						ControlPanel.d = false;
						playerShots[y][x] = 1;
						updateUI();
						temp[0] = y;
						temp[1] = x;
					}
					else {
						System.out.println("Cannot move down!");
						ControlPanel.d = false;
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Cannot move down!");
					ControlPanel.d = false;
				}
			}
			if(ControlPanel.l == true) { //if the shot should move left
				try {
					if(playerShots[y][x-1] == 0) {
						playerShots[y][x] = 0;
						x = x - 1;
						ControlPanel.l = false;
						playerShots[y][x] = 1;
						updateUI();
						temp[0] = y;
						temp[1] = x;
					}
					else {
						System.out.println("Cannot move left!");
						ControlPanel.l = false;
					}
				}
				catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Cannot move left!");
					ControlPanel.l = false;
				}
			}
			
			TimeUnit.MILLISECONDS.sleep(100);
			if(counter%10 == 0) {
				ControlPanel.time.setText("Time: " + counter/10);
			}
			counter -= 1;
		}
		
		System.out.println("Turn Ended!");
		ControlPanel.exit = false;
		return temp;
	}
	
	/**
	 * Function similar to defaultSpace, finds the first open spot for the user's shot to default to
	 */
	public static void defaultShot() {
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				if(playerShots[i][j] == 0) {
					y = i;
					x = j;
					return;
				}
			}
		}
	}
	
	/**
	 * Function to determine result of the shot received from other player
	 * @param s - Integer Array containing shots coordinates
	 * @return - Integer value containing whether the shot was a hit or not
	 */
	public static int determineShot(int[] s) {

		if((playerGrid[s[0]][s[1]]) == 1){ //if hit
			playerGrid[s[0]][s[1]] = 4;
			updateUI();
			return 1;
		}
		return 0;  //if miss
	}
	
	/**
	 * Function determining if there was a winner after the most current shot
	 * @return - Integer containing data whether who is the winner if there is one
	 */
	public static int determineWinner() {
		int countera = 0;
		int counterb = 0;
		
		int limit = 0;
		
		for(int i = 0; i < ships.length; i++) {
			limit += ships[i];
		}
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				if(playerShots[i][j] == 1) { //if enemy ship was hit
					countera ++;
				}
				if(playerGrid[i][j] == 1) { //if users ship is still alive
					counterb ++;
				}
			}
		}
		
		if(countera == limit) {
			return 1;
		}
		if(counterb == 0) {
			return 2;
		}
		return 0;
	}
	
	/**
	 * Function used to save preferences file
	 * @throws IOException - handled manually
	 */
	public static void savePrefs() throws IOException {
		System.out.println("Saved Prefs!");
		FileWriter fw = null;
		try {
			fw = new FileWriter(Menubar.pref);
		} catch (IOException e) {
		}
		//Writing all preference settings to txt file
		fw.write(Menubar.blankColor + "\n");
		fw.write(Menubar.shipColor + "\n");
		fw.write(Menubar.hitColor + "\n");
		fw.write(Menubar.missColor + "\n");
		fw.write(Menubar.shipHitColor + "\n");
		fw.write(Menubar.turnTime + "\n");
		fw.write(Menubar.defaultPort + "\n");
		fw.write(Menubar.defaultIP + "\n");
		fw.write(Menubar.numShips + "\n");
		fw.write("true \n");
		fw.close();
	}
	
	/**
	 * Function used to read preferences from file
	 * @throws IOException - handled manually in MAIN
	 */
	public static void readPrefs() throws IOException{
		BufferedReader bw = null;
		try {
			bw = new BufferedReader(new FileReader("myPreferences.txt"));
		} catch (FileNotFoundException e) {
			//handled elsewhere
		}
		finally {
			//Setting up current preferences
			Menubar.blankColor = findColor(bw.readLine());
			Menubar.shipColor = findColor(bw.readLine());
			Menubar.hitColor = findColor(bw.readLine());
			Menubar.missColor = findColor(bw.readLine());
			Menubar.shipHitColor = findColor(bw.readLine());
			Menubar.turnTime = Integer.parseInt(bw.readLine());
			Menubar.defaultPort = Integer.parseInt(bw.readLine());
			Menubar.defaultIP = bw.readLine();
			Menubar.numShips = Integer.parseInt(bw.readLine());
			Menubar.updateLengths = true;
			bw.close();
		}
	}
/**
 * Parses string data from preference file to form a Color Object
 * @param line String that is being parsed
 * @return Color Object
 */
public static Color findColor(String line) {
	if(line.startsWith("java")) { //Parsing through split string array to extract R G B int values 
		String[] a = new String[4];
		a = line.split("=");
		String r = "";
		String g = "";
		String b = "";
		for(int i = 0; i < a.length; i++) {
			for( char x : a[i].toCharArray()) {
				if(i != 0) {
					if (Character.isDigit(x)) {
						if(i == 1) {
							r += x;
						}
						else if(i == 2) {
							g += x;
						}
						else {
							b += x;
						}
					}
				}
			}
		}
		return(new Color(Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b))); //creating new Color object and returning it
	}
	else {
		return null;
	}
}
}
