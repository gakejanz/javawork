package HW02.ganzj2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class MyFrame extends JFrame{	
	
	private static final long serialVersionUID = 1L;
	int screenHeight = 0; //public variable of frames height
	int screenWidth = 0; //public variable of frames width
	int x_dim; //public variable of grids x dimension
	int y_dim; //public variable of grids y dimension
	public boolean running = false; //public variable of game state
	public int ticks = 5; //public variable of max steps
	public JButton[][] buttons; //button grid corresponding to game grid
	public int[][] gameGrid; //game grid
	public String loadFile = null; //Load File String
	public JPanel gridP; //Game Grid Panel
	public JPanel statP; //Stats Panel
	public JPanel hotP; //Hotkey panel
	public int[][] ogGrid; //Initial Grid
	public int desiredStep = -1; //Desired Step if selected
	public JTextField tickNum; //textField for desired step
	public JTextField numTicks; //textfield for max steps
	public boolean forward = false; //boolean for moving forward
	public boolean backward = false; //boolean for moving backward
	public JLabel step; //step JLabel
	public JLabel alive; //alive counter JLabel
	public JLabel dead; //dead counter JLabel
	public JLabel birthed; //birthed counter JLabel
	public JLabel passed; //passed counter JLabel
	public JLabel curMax; //Current Max Step JLabe;
	
	/**
	 * Creating a Frame Object
	 * @param a integer X Dimension
	 * @param b integer y Dimension
	 * @param matrix 2D Integer array of game grid
	 */
	public MyFrame(int a, int b, int[][] matrix) { 
		x_dim = a; //initializing public variables
		y_dim = b;
		gridP = gridPanel(matrix);
		statP = statistics();
		hotP = hotKeys();
		ogGrid = matrix;
		//adding public variable panels to frame and setting size
		setLayout(new BorderLayout());
		add(gridP, BorderLayout.CENTER);
		add(statP, BorderLayout.EAST);
		add(hotP, BorderLayout.SOUTH);
		setJMenuBar(new menuBar()); //new Object of MenuBar class
		setSize(screenWidth, screenHeight);
		
	}
	
	/**
	 * Setting Up States of Button Array to represent the game grid matrix
	 * @param a Integer X Dimension
	 * @param b Integer Y Dimension
	 * @param matrix 2D integer array matrix of game grid
	 */
	public void setButtons(int a, int b,int[][]matrix) {	
		for (int i = 0; i < b; i++) { //For every cell in Matrix
			for(int j = 0; j < a; j++) {
				if(matrix[i][j] == 1) { //if current cell is alive
					if(buttons[i][j].getBackground() != menuBar.colorDead) { //If current button is not represented as dead
						Color red = buttons[i][j].getBackground().darker();
						buttons[i][j].setBackground(red);
					}
					else { //cell is dead
						buttons[i][j].setBackground(menuBar.colorAlive); //set button to alive
					}
				}
				else {
					buttons[i][j].setBackground(menuBar.colorDead); //set button to dead
				}
			}
		}
	}
	
	/**
	 * Function Creating Grid Panel of game grid
	 * @param matrix 2D Integer array of game grid
	 * @return JPanel object reresenting the game grid
	 */
	public JPanel gridPanel(int[][] matrix) {
		buttons = new JButton[y_dim][x_dim]; //creating new 2D array of buttons
		Toolkit kit = Toolkit.getDefaultToolkit(); //Factory Method for getting instance of toolkit class
		Dimension screenSize = kit.getScreenSize();
		screenHeight = screenSize.height / 2;
		screenWidth = screenHeight; 
		JPanel grid = new JPanel(); //Initializing grid panel
		grid.setBackground(menuBar.backColor); //setting background color
		setLayout(new FlowLayout());
		add(grid); //adding grid to Frame
		grid.setPreferredSize(new Dimension(screenWidth - 150, screenHeight - 140)); //setting grid size
		grid.setLayout(new GridLayout(x_dim, y_dim));
		for (int i = 0; i < y_dim; i++) { //for every cell in game grid
			for(int j = 0; j < x_dim; j++) {
				JButton button = new JButton(); //create new JButton and Add ActionListener, Initialize Color
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						if(button.getBackground() != menuBar.colorAlive) {
							button.setBackground(menuBar.colorAlive);
						}
						else{
							button.setBackground(menuBar.colorDead);
						}
					}
				});
				if(matrix[i][j] == 1) {
					button.setBackground(menuBar.colorAlive);
				}
				else {
					button.setBackground(menuBar.colorDead);
				}
				grid.add(button); //add button to grid and 2D array of buttons
				buttons[i][j] = button;
			}
		}
		return grid;
	}
	
	/**
	 * Creating a hotkeys Panel object
	 * @return JPanel object to be placed on the frame holding hotkey buttons and textfields for the user
	 */
	public JPanel hotKeys() {
		JPanel hotkeys = new JPanel(); //Creating new master JPanel
		hotkeys.setPreferredSize(new Dimension(screenWidth, 160));
		hotkeys.setBackground(menuBar.hotColor);
		
		hotkeys.setLayout(new FlowLayout());
		
		JButton start = new JButton("Start"); //Creating new Start button
		start.setForeground(menuBar.fontColor);
		start.addActionListener(new ActionListener() { //Setting game state to true if selected, handled in MAIN class
			public void actionPerformed(ActionEvent e) {
				running = true;
			}
		});
		hotkeys.add(start);
	
		JButton backTick = new JButton("Go Back a Tick"); //Creating new back Tick button
		backTick.setForeground(menuBar.fontColor);
		backTick.addActionListener(new ActionListener() { //Setting backward to true if selected, handled in MAIN class
			public void actionPerformed(ActionEvent e) {
				if(!forward) {
					backward = true;
				}
			}
		});
		hotkeys.add(backTick);
		
		JButton pause = new JButton("Pause"); //Creating new pause button
		pause.setForeground(menuBar.fontColor);
		pause.addActionListener(new ActionListener() { //Setting game state to false if selected, handled in MAIN class
			public void actionPerformed(ActionEvent e) {
				running = false;
			}
		});
		hotkeys.add(pause);
		
		JButton forwardTick= new JButton("Go Forward a Tick"); //creating new forward tick button 
		forwardTick.setForeground(menuBar.fontColor);
		forwardTick.addActionListener(new ActionListener() { //setting forward to true if selected, handled in MAIN class
			public void actionPerformed(ActionEvent e) {
				if(!backward) {
					forward = true;
				}
			}
		});
		hotkeys.add(forwardTick);
		
		tickNum = new JTextField("Go To Certain Tick"); //Creating new tickNum textField
		tickNum.setForeground(menuBar.fontColor);
		tickNum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				if(running == false) { //if program is not currently running
					try{
						desiredStep = Integer.parseInt(tickNum.getText());
					}
					catch (Exception f) {
						System.out.println("Invalid Input!");
						desiredStep = -1;
					}
				}
				tickNum.setText("Go To Certain Tick");
			}
		});
		hotkeys.add(tickNum);
		
		numTicks = new JTextField("Number of Ticks"); //creatung new NUmber of Ticks text Field
		numTicks.setForeground(menuBar.fontColor);
		numTicks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					ticks = Integer.parseInt(numTicks.getText());
				}
				catch (Exception f) {
					ticks = 5;
					System.out.println("Invalid Tick Input! Ticks set to 5");
				}
				if(ticks < 0) {
					ticks = 5;
					System.out.println("Invalid Tick Input! Ticks set to 5");
				}
				numTicks.setText("Number of Ticks");
			}
		});
		hotkeys.add(numTicks);
		
		
		return hotkeys;
	}
	
	/**
	 * Creating new JPanel holding game stats
	 * @return JPanel object containing game stats
	 */
	public JPanel statistics() {
		JPanel stats = new JPanel(); //creating new master panel
		stats.setPreferredSize(new Dimension(140, screenHeight - 150)); //setting size of panel
		stats.setBackground(menuBar.statColor);
		JLabel statHeading = new JLabel("Stats"); //new Heading Label
		statHeading.setForeground(menuBar.fontColor); //Setting font color of label
		stats.add(statHeading); //adding label
		step = new JLabel("Current Step: "); //new label
		step.setForeground(menuBar.fontColor); //setting font color of label
		alive = new JLabel("Alive Cells: "); //new label
		alive.setForeground(menuBar.fontColor); //setting font color of label
		dead = new JLabel("Dead Cells: "); //new label
		dead.setForeground(menuBar.fontColor); //setting font color of label
		birthed = new JLabel("Birthed Cells: "); //new label
		birthed.setForeground(menuBar.fontColor); //setting font color of label
		passed = new JLabel("Passed Cells: "); //new label
		passed.setForeground(menuBar.fontColor); //setting font color of label
		curMax = new JLabel("Max Step: "); //new label
		curMax.setForeground(menuBar.fontColor); //setting font color of label
		
		stats.add(step); //adding labels
		stats.add(alive);
		stats.add(dead);
		stats.add(birthed);
		stats.add(passed);
		stats.add(curMax);
		return stats;
	}
	
	/**
	 * Creating game grid based of button states
	 * @param a Integer X Dimension
	 * @param b Integer Y Dimension
	 * @param btns 2D JButton array of Buttons 
	 * @return 2D integer array of game grid
	 */
	public static int[][] createGrid(int a, int b, JButton[][] btns) {
		int[][] grid = new int[b][a]; //new game grid
		for(int i = 0; i < b; i++) { //Looping through all buttons and initializing values of game grid based off button values
			for(int j = 0; j < a; j++) {
				if(btns[i][j].getBackground() != menuBar.colorDead) { //if the cell is alive
					grid[i][j] = 1;
				}
				else {
					grid[i][j] = 0;
				}
			}
		}
		
		return grid;
	}
	
	/**
	 * Resizing the game grid array to new dimensions
	 * @param a Integer X Dimension
	 * @param b Integer Y Dimension
	 * @param matrix 2D Integer Array of game grid
	 */
	public void resize(int a, int b, int[][] matrix) {
		gridP.removeAll(); //removing old grid from Panel
		x_dim = a;
		y_dim = b;
		buttons = new JButton[y_dim][x_dim]; //Creating new JButton array
		gridP.setLayout(new GridLayout(x_dim, y_dim));
		for (int i = 0; i < y_dim; i++) { //For each cell in grid create new button and initialize their states
			for(int j = 0; j < x_dim; j++) {
				JButton button = new JButton();
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e)
					{
						if(button.getBackground() != menuBar.colorAlive) {
							button.setBackground(menuBar.colorAlive);
						}
						else{
							button.setBackground(menuBar.colorDead);
						}
					}
				});
				if(matrix[i][j] == 1) {
					button.setBackground(menuBar.colorAlive);
				}
				else {
					button.setBackground(menuBar.colorDead);
				}
				gridP.add(button);
				buttons[i][j] = button;
			}
		}
		gridP.validate(); //revalidating UI
		gridP.repaint();
	}

}