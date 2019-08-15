package HW02.ganzj2;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * This hw2Main program is the main driver for the Rich GUI Conway Game of Life Implementation for homework 2
 * @author Jacob Ganz
 * @version 06/18/2019
 */

public class hw2Main {
	static int passed = 0; //public int used for counting passed cells
	static int birthed = 0; //public int used for counting birthed cells
	
	/**
	 * The Main Method - This is where game is ran and the GUI is formed, using inputs from other functions
	 * Ran in an infinite loop, must be exited manually
	 * Checks for Preferences file containing past settings, if there is the new frame is built upon those preferences
	 * Allows user full functionality of GUI
	 * @param args - A String array containing any command line arguments
	 * @throws InterruptedException - Handles Exception used to waiting time 
	 * @throws IOException - IOException is dealt with manually via try/catch
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		
		BufferedReader br = null;
		int steps = -1;
		try { //Checking to see if there is a myPreferences folder saved in the package
			br = new BufferedReader(new FileReader("myPreferences.txt"));
			steps = readPrefs();
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("No Preferences File Found! Using Default Settings.");
			steps = 5;
		}

		
		int defaultX = menuBar.defX; //setting default variables to initial defaults from menuBar class
		int defaultY = menuBar.defY;
		String inFile = menuBar.s;
		int[][] defaultGrid = new int[defaultX][defaultY]; //Creating Default Grid using Default Variables
		int curStep = 0;
		for(int i = 0; i < defaultX; i++) {
			for(int j = 0; j < defaultY; j++) {
				defaultGrid[i][j] = 0;
			}
		}
		
		var frame = new MyFrame(defaultX, defaultY, defaultGrid); //creating initial grid gui based off default values and initializing default variables
		int xDist = defaultX;
		int yDist = defaultY;
		int[][] test = new int[defaultX][defaultY];
		frame.ticks = steps;
		frame.setTitle("Conway Game of Life");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.curMax.setText("Max Step: " + steps);
		frame.setVisible(true);
		
		boolean fileLoaded = false;

		while(0 < 1) {        //Infinite Loop - Program must be exited Manually
			boolean first = true;
			boolean error = false;
			while(!frame.running) {       //While the start button has not been hit
				frame.step.setText("Current Step: " + curStep);   //Outputting current step to a stats label
				if(menuBar.loadedFile) { //If there is a file to be loaded from preferences or a file manually selected
					inFile = menuBar.l;
					menuBar.loadedFile = false;
					error = false;
				}
				if(inFile != null) { //if the file loaded can be used to build the grid
					br = null;
					try {        //ensuring file can be read
						br = new BufferedReader(new FileReader(inFile));
					} catch (Exception e) {
						System.out.println("Invalid File!");
						menuBar.loadedFile = false;
						error = true;
						first = false;
					}
					String reader = "";
					try {            //ensuring the file can be read
						reader = br.readLine();
					} catch (IOException e1) {
						inFile = null;
						System.out.println("Invalid File!");
						menuBar.loadedFile = false;
						error = true;
						first = false;
					}
					catch (NullPointerException g){
						inFile = null;
						System.out.println("Invalid File!");
						menuBar.loadedFile = false;
						error = true;
						first = false;
					}
					String[] readerSplit = reader.split(", "); //reading first line to gather dimensions of game board
					try{  //ensuring the file contains the correct information
						xDist = Integer.parseInt(readerSplit[1]);
					}
					catch (Exception f) { 
						inFile = null;
						System.out.println("Invalid File!");
						menuBar.loadedFile = false;
						error = true;
						first = false;
					}
					if(!error) {          //if there is no problem reading the input file
						yDist = Integer.parseInt(readerSplit[0]);
						test = new int[xDist][yDist];
						try {
							test = readFile(inFile);
						} catch (IOException e) {    //ensuring file read again
							inFile = null;
							System.out.println("Invalid File!");
							menuBar.loadedFile = false;
							error = true;
							first = false;
						}
						frame.setVisible(true);
						frame.gameGrid = test;
						frame.ogGrid = test;
						System.out.println("RESIZE DIMS " + xDist + " " + yDist);
						frame.resize(xDist, yDist, test);
						first = false;
						fileLoaded = true;
						inFile = null;
					}
				}
				else{                                 //else game is created based off current gridItem
					if(first){
						if((xDist != frame.buttons.length) || (yDist != frame.buttons[0].length)){ //if the dimensions are not equivalent to the displayed dimensions
							frame.resize(defaultX, defaultY, defaultGrid);
						}
					}
					if(!fileLoaded){
						frame.gameGrid = MyFrame.createGrid(defaultX, defaultY, frame.buttons);  //creating gamegrid based off state of buttons
						test = frame.gameGrid;
							xDist = defaultX;
							yDist = defaultY;
					}
					else{
						frame.gameGrid = MyFrame.createGrid(xDist, yDist, frame.buttons);
						test = frame.gameGrid;
					}
					
				}
				TimeUnit.MILLISECONDS.sleep(50); //wait 
				if(menuBar.reset) { //if the NEW button is selected from MenuBar - everything is reset
					System.out.println("NEW BOARD");
					menuBar.reset = false;
					frame.running = false;
					first = true;
					menuBar.l = null;
					steps = frame.ticks;
					defaultX = menuBar.defX;
					defaultY = menuBar.defY;
					defaultGrid = new int[defaultX][defaultY];
					frame.gameGrid = defaultGrid;
					frame.ogGrid = defaultGrid;
					frame.resize(defaultX, defaultY, defaultGrid);
					xDist = defaultX;
					yDist = defaultY;
					test = new int[xDist][yDist];
					test = defaultGrid;
					frame.setVisible(true);
				}
				if(frame.desiredStep != -1) { //if the Tick Number text field is used
					if(frame.desiredStep >= 0 && frame.desiredStep <= steps) {
						test = gotoStep(xDist, yDist, test, frame, curStep);
						curStep = frame.desiredStep;
						frame.desiredStep = -1;
					}
					else {
						System.out.println("Error in tick selection! Remaining at current step");
						frame.desiredStep = curStep;
					}
					updateUI(frame, xDist, yDist, test);
					frame.desiredStep = -1;
				}
				
				if(frame.forward == true) { //if the forward button is clicked
					System.out.println("Forward One Step");
					if(curStep < steps) {
						test = forward(xDist, yDist, test, frame);
						curStep++;
					}
					else {
						System.out.println("Unable to go forward! Exceeds Current Max Step!");
						frame.forward = false;
					}
				}
				
				if(frame.backward == true) { //if the backward button is clicked
					System.out.println("Backward One Step");
					if(curStep != 0) {
						test = backward(xDist, yDist, test, frame, curStep);
						curStep--;
					}
					else {
						System.out.println("Unable to go Backwards! The Current Step is 0!");
						frame.backward = false;
						frame.ogGrid = test;
					}
				}
				if(menuBar.savePrefs == true) { //if the save preferences button is clicked
					savePrefs(frame.ticks);
					System.out.println(steps);
					menuBar.savePrefs = false;
				}
				if(menuBar.saveGame == true) { //if the save game button is clicked
					saveGrids(curStep, steps, test, xDist, yDist);
				}
				if(menuBar.updateFlag == true) { //if the UI needs to be updated, based off various button clicks from menuBar
					updateUI(frame, xDist, yDist, test);
					menuBar.reset = true;
					frame.setVisible(true);
					menuBar.updateFlag = false;
				}
				if(curStep == 0) { //if the current step is 0 place current grid in placeholder variable within frame class
					frame.ogGrid = test;
				}
				if(menuBar.saveGrids == true) { //if the current grid saved as txt file button was selected
					saveGrid(xDist, yDist, test);
				}
				steps = frame.ticks; //syncing main's max steps with frame's max steps
				
				//Updating UIs JLables Below
				frame.alive.setText("Alive Cells: " + liveCount(xDist, yDist, test));
				frame.dead.setText("Dead Cells: " + ((xDist * yDist) - liveCount(xDist, yDist, test)));
				frame.birthed.setText("Birthed Cells: " + birthed);
				frame.passed.setText("Passed Cells: " + passed);
				frame.curMax.setText("Max Step: " + steps);
				frame.step.setText("Current Step: " + curStep);   //Outputting current step to a stats label
			}
			//Syncing frame objects with Main objects
			steps = frame.ticks;
			frame.ogGrid = test;
			
			for(int i = 1; i <= steps; i++) { //Running Game for given max steps
				curStep = i;
				if(menuBar.reset) { //if the NEW Button is selected, exit loop and reset
					break;
				}
				else { 
					test = nextGeneration(test, xDist, yDist); //updating test matrix to next generation
					//Updating UI
					frame.step.setText("Current Step: " + curStep);
					frame.alive.setText("Alive Cells: " + liveCount(xDist, yDist, test));
					frame.dead.setText("Dead Cells: " + ((xDist * yDist) - liveCount(xDist, yDist, test)));
					frame.birthed.setText("Birthed Cells: " + birthed);
					frame.passed.setText("Passed Cells: " + passed);
					try {
						TimeUnit.MILLISECONDS.sleep(500); //waiting 0.5 seconds between each generation
					} catch (InterruptedException e) {
					}
					
					frame.setButtons(xDist,yDist,test);//Updating UI buttons to resemble game grid
					frame.setVisible(true);
					while(!frame.running) {   //if game is paused
						TimeUnit.MILLISECONDS.sleep(50); //wait between each checking of status of buttons
						steps = frame.ticks;
						if(menuBar.reset) { //if the NEW button is selected break and reset
							break;
						}
						if(frame.desiredStep != -1) { //if user wants to jump to different step
							if(frame.desiredStep >= 0 && frame.desiredStep <= steps) {
								test = gotoStep(xDist, yDist, test, frame, curStep);
								curStep = frame.desiredStep;
								//updateUI(frame, xDist, yDist, test);
								i = curStep;
							}
							else {
								System.out.println("Error in tick selection! Remaining at current step");
								frame.desiredStep = curStep;
							}
							frame.desiredStep = -1;
						}
						if(frame.forward == true) { //If User wants to move forward a step
							if(curStep < steps) {
								test = forward(xDist, yDist, test, frame);
								i++;
								curStep = i;
							}
							else {
								System.out.println("Unable to go forward! Exceeds Current Max Step!");
								frame.forward = false;
							}
						}
						
						if(frame.backward == true) { //if user wants to move back a step
							if(curStep != 0) {
								test = backward(xDist, yDist, test, frame, curStep);
								i--;
								curStep = i;
							}
							else {
								System.out.println("Unable to go Backwards! The Current Step is 0!");
								frame.ogGrid = test;
								frame.backward = false;
							}
						}
						if(curStep == 0) {
							frame.ogGrid = test;
						}
						if(menuBar.updateFlag == true) { //if the UI needs to be updated, based off various button clicks from menuBar
							updateUI(frame, xDist, yDist, test);
							frame.setVisible(true);
							menuBar.reset = true;
							menuBar.updateFlag = false;
						}
					}
				}
			}
			frame.running = false; //Game is no longer running
			if(menuBar.reset) { //If New BUtton is selected - reset all values to default
				System.out.println("RESET");
				menuBar.reset = false;
				frame.running = false;
				first = true;
				menuBar.l = null;
				frame.gameGrid = defaultGrid;
				frame.resize(defaultX, defaultY, defaultGrid);
				steps = frame.ticks;
				frame.numTicks.setText("Number of Ticks");
				xDist = defaultX;
				yDist = defaultY;
				test = new int[xDist][yDist];
				test = defaultGrid;
				frame.setVisible(true);
				curStep = 0;
			}
			if(menuBar.savePrefs == true) { //if save preferences is selected
				savePrefs(steps);
				menuBar.savePrefs = false;
			}
			if(menuBar.saveGrids == true) { //if save the current grid is selected
				saveGrid(xDist, yDist, test);
				menuBar.saveGrids = false;
			}
			if(menuBar.saveGame == true) { //if the save game button is clicked
				saveGrids(curStep, steps, test, xDist, yDist);
			}
			if(menuBar.updateFlag == true) { //if the UI needs to be updated, based off various button clicks from menuBar
					updateUI(frame, xDist, yDist, test);
					frame.setVisible(true);
					menuBar.reset = true;
					menuBar.updateFlag = false;
				}
		}
	}
	
	/**
	 * Function used to calculate the game grids next generation 
	 * @param grid 2D Integer matrix representing the game grid
	 * @param M Integer representing the x dimension of grid
	 * @param N Integer representing the y dimension of the grid
	 * @return 2D Integer matrix representing the game grids next generation
	 */
	public static int[][] nextGeneration(int grid[][], int M, int N) //Creating nextGen
    { 
        int[][] future = new int[N][M]; 
  
        for(int i = 0; i < N; i++)
        {
        	for(int j = 0; j < M; j++) //Looping through every cell
        	{
        		int aliveNeighbours = 0;
        		for (int n = -1; n < 2; n++) { //finding alive neighbours
        			   for (int r = -1; r < 2; r++) {
        			      if(n != 0 || r != 0) {
        			    	  int a = Math.abs((i + n + N)%N);
        			    	  int b = Math.abs((r + j + M)%M);
        			          aliveNeighbours += grid[a][b];
        			      }
        			   }
        			}
        		 // Cell dies 
                if ((grid[i][j] == 1) && (aliveNeighbours < 2)) { 
                    future[i][j] = 0; 
                	passed++;
                }
                // Cell dies
                else if ((grid[i][j] == 1) && (aliveNeighbours > 3)) {
                    future[i][j] = 0; 
                	passed++;
                }
                // A new cell is born 
                else if ((grid[i][j] == 0) && (aliveNeighbours == 3)) {
                    future[i][j] = 1; 
                    birthed++;
                }
                // Remains the same 
                else
                    future[i][j] = grid[i][j]; 
        	}
        }
        return future;
    }
	
	/**
	 * readFile reads and parses any input game grid files 
	 * @param file String containing the file location
	 * @return 2D Integer matrix representing a game grid
	 * @throws IOException - IOException is handled in the MAIN
	 */
	public static int[][] readFile(String file) throws IOException{
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(file));
		String reader = br.readLine();
		String[] readerSplit = reader.split(", "); //reading first line to gather dimensions of game board
		int xDist = Integer.parseInt(readerSplit[1]);
		int yDist = Integer.parseInt(readerSplit[0]);

		int[][] gameBoard = new int[yDist][xDist]; //creating game board

		for(int i = 0; i < yDist; i++){ //parsing input file lines 
			reader = br.readLine();
			readerSplit = reader.split(", ");

			for(int j = 0; j < xDist; j++){
				gameBoard[i][j] = Integer.parseInt(readerSplit[j]); //filling in game board with input file data
			}
		}
		br.close();
		return gameBoard;
	}
	
	/**
	 * savePrefs saves all current states of Game Settings to a .txt file that can be used later
	 * @param steps Integer represting the current games max steps
	 * @throws IOException - IOException is handled 
	 */
	public static void savePrefs(int steps) throws IOException {
		System.out.println("Saved Prefs!");
		FileWriter fw = null;
		try {
			fw = new FileWriter(menuBar.pref);
		} catch (IOException e) {
		}
		//Writing all preference settings to txt file
		fw.write(menuBar.colorAlive + "\n");
		fw.write(menuBar.colorDead + "\n");
		fw.write(menuBar.backColor + "\n");
		fw.write(menuBar.hotColor + "\n");
		fw.write(menuBar.statColor + "\n");
		fw.write(menuBar.fontColor + "\n");
		fw.write(menuBar.defX + "\n");
		fw.write(menuBar.defY + "\n");
		fw.write(menuBar.outFile + "\n");
		fw.write(menuBar.l + "\n");
		fw.write(menuBar.s + "\n");
		fw.write(Integer.toString(steps));
		fw.close();
	}
	
	/**
	 * readPrefs sets all current settings to previously saved settings
	 * @return Integer representing games max step
	 * @throws IOException Exception is handled manually
	 */
	public static int readPrefs() throws IOException {
		BufferedReader bw = null;
		try {
			bw = new BufferedReader(new FileReader("myPreferences.txt"));
		} catch (FileNotFoundException e) {
			//handled elsewhere
		}
		finally {
			//Setting up current preferences
			menuBar.colorAlive = findColor(bw.readLine());
			menuBar.colorDead = findColor(bw.readLine());
			menuBar.backColor = findColor(bw.readLine());
			menuBar.hotColor = findColor(bw.readLine());
			menuBar.statColor = findColor(bw.readLine());
			menuBar.fontColor = findColor(bw.readLine());
			menuBar.defX = Integer.parseInt(bw.readLine());
			menuBar.defY = Integer.parseInt(bw.readLine());
			menuBar.outFile = bw.readLine();
			menuBar.l = bw.readLine();
			menuBar.s = bw.readLine();
		}
		return Integer.parseInt(bw.readLine());
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
	
	/**
	 * Moving the game grid forward one step
	 * @param x Integer X Dimension of grid
	 * @param y Integer Y dimension of grid
	 * @param t 2D Integer array representing grid
	 * @param f MyFrame class object used in MAIN
	 * @return 2D Integer array containing game grid moved forward one step
	 */
	public static int[][] forward(int x, int y, int[][] t, MyFrame f){
		t = nextGeneration(t, x, y);
		f.setButtons(x, y, t);
		f.forward = false;
		return t;
	}
	
	/**
	 * Moving the game grid backward one step
	 * @param x Integer X Dimension of grid
	 * @param y Integer Y dimension of grid
	 * @param t 2D Integer array representing grid
	 * @param f MyFrame class object used in MAIN
	 * @param step Integer representing current step
	 * @return 2D Integer array containing game grid moved forward one step
	 */
	public static int[][] backward(int x, int y, int[][] t, MyFrame f, int step){ //FIX SHADING
		int temp = step - 1;
		t = f.ogGrid;
			for (int i = 0; i < y; i++) { //For every cell in Matrix
				for(int j = 0; j < x; j++) {
					f.buttons[i][j].setBackground(menuBar.colorDead);
				}
			}
		if(temp != 0) { //if backward one step is not the 0th step
			f.setButtons(x, y, f.ogGrid);
			for(int p = 1; p <= temp; p++) {
				t = nextGeneration(t, x, y);
				f.setButtons(x, y, t);
			}
			//f.setButtons(x, y, t);
		}
		else { //else set the grid back to 0th step
			f.setButtons(x,y,f.ogGrid);
		}
		f.backward = false;
		return t;
	}
	
	/**
	 * Moving game grid to specified step
	 * @param x Integer X Dimension of grid
	 * @param y Integer Y Dimension of grid
	 * @param t 2D integer array representing grid
	 * @param f MyFrame class object used in MAIN
	 * @param step Integer step representing current step
	 * @return 2D Integer array containing the game grid moved to selected step
	 */
	public static int[][] gotoStep(int x, int y, int[][] t, MyFrame f, int step){
		// = f.ogGrid;
		if(f.desiredStep == 0) { //if the desiredStep is ZERO
			t = f.ogGrid;
			f.setButtons(x, y, t);
		}
		else { //else rebuild grid until specified step
			//f.gameGrid = t;
			t = f.ogGrid;
			f.setButtons(x,y,f.ogGrid);
			System.out.println("DESIRED STEP: " + f.desiredStep);
			for(int i = 1; i <= f.desiredStep; i++) {
				
				t = nextGeneration(t, x, y);
				f.setButtons(x,y,t);
			}
		}
		return t;
	}
	
	/**
	 * Saving all Grids up to specified Max Step to templated output files
	 * @param cur Integer representing games current step
	 * @param end Integer representing games final step
	 * @param matrix 2D Integer array representing game grid
	 * @param a Integer X Dimension 
	 * @param b Integer Y Dimension
	 * @throws IOException handled elsewhere
	 */
	public static void saveGrids(int cur, int end, int[][] matrix, int a, int b) throws IOException {
		System.out.println("Saved Current Board up to Generation " + end + " with outfile template " + menuBar.outFile);
		FileWriter fw = null;
		for(int i = cur; i <= end; i++) { //Looping through steps
			String output = menuBar.outFile + "_Gen" + Integer.toString(i) + ".txt"; //creating new templated file
			try {
				fw = new FileWriter(output);
			} catch (IOException e) {
			}
			for(int j = 0; j < b - 1; j++) {  //Printing out current grid
				for(int k = 0; k < a - 1; k++) {
					fw.write(Integer.toString(matrix[j][k]) + ", ");
				}
				fw.write(Integer.toString(matrix[j][a - 1]) + "\n");
			}
			for(int j = 0; j < a - 1; j++) {
				fw.write(Integer.toString(matrix[b - 1][j]) + ", ");
			}
			fw.write(Integer.toString(matrix[b-1][a-1]));
			matrix = nextGeneration(matrix, a, b); //finding grids next generation
			fw.close();
		}
		menuBar.saveGame = false; //resetting save game
	}
	
	/**
	 * Function used to count Live Cells in current grid
	 * @param x Integer X Dimension of grid
	 * @param y Integer Y Dimension of grid
	 * @param matrix 2D Integer Array of game grid
	 * @return Integer value of number of live cells in current grid
	 */
	public static int liveCount(int x, int y, int[][] matrix) {
		int temp = 0;
		for(int i = 0; i < y; i++) { //Loops through all game grid
			for(int j = 0; j < x; j++) {
				if(matrix[i][j] == 1) { //if current cell is alive
					temp++; 
				}
			} 
		}
		return temp;
	}
	
	/**
	 * Updating Current User Interface
	 * @param f MyFrame Class Object used in MAIN
	 * @param x Integer X Dimension
	 * @param y Integer Y Dimension
	 * @param m 2D Integer array of game grid
	 */
	public static void updateUI(MyFrame f, int x, int y, int[][] m) {
		//Removing and resetting all UI pieces
		f.statP.removeAll();
		f.statP.add(f.statistics());
		f.hotP.removeAll();
		f.hotP.add(f.hotKeys());
	}
	
	/**
	 * Saving Current Game Grid in text file to be accessed later
	 * @param a Integer X Dimension
	 * @param b Integer Y Dimesnion
	 * @param matrix 2D integer array of game grid
	 * @throws IOException handled elsewhere
	 */
	public static void saveGrid(int a, int b, int[][] matrix) throws IOException {
		System.out.println("Saved Current Grid to " + menuBar.s);
		FileWriter fw = null;
		try {
			fw = new FileWriter(menuBar.s);
		} catch (IOException e) {
		}
		fw.write(Integer.toString(a) + ", " + Integer.toString(b) + "\n"); //Printing out current game grid to file
		for(int j = 0; j < b - 1; j++) {
			for(int k = 0; k < a - 1; k++) {
				fw.write(Integer.toString(matrix[j][k]) + ", ");
			}
			fw.write(Integer.toString(matrix[j][a - 1]) + "\n");
		}
		for(int j = 0; j < a - 1; j++) {
			fw.write(Integer.toString(matrix[b - 1][j]) + ", ");
		}
		fw.write(Integer.toString(matrix[b-1][b-1]));
		fw.close();
		menuBar.saveGrids = false;
	}
}









