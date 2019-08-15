package ganzj2.hw04;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

class menuBar extends JMenuBar{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static String l = ""; //public variable Load File string
	static boolean reset = false; //public reset game variable
	static Color colorAlive = Color.red; //Alive cell color
	static Color colorDead = Color.white; //dead cell color
	static Color backColor = Color.white; //background of Bars color
	static Color fontColor = Color.black; //Font color
	static int defX = 100; //Default X Dimension
	static int defY = 100; //default Y Dimension
	static String outFile = "out"; //default output pattern
	static String pref = "myPreferences.txt"; //preferences file name
	static boolean savePrefs = false; //save Preferences Flag
	static boolean saveGame = false; //save game flag
	static boolean saveGrids = false; //save grids flag
	static boolean updateFlag = false; //update UI flag
	static String s = null; //save file string
	static boolean loadedFile = false; //loadedfile boolean
	static int tickTime = 500;
	static int ticks = 10;
	static boolean updateTime = false;
	static boolean updateTicks = false;
	static int threads = 1;

	JMenuItem newItem;
	JMenuItem loadItem;
	JMenuItem savePref;
	JMenuItem save;
	JMenuItem saveGrid;
	JMenu fileMenu;
	JMenu settingsMenu;
	JMenuItem colorAliveItem;
	JMenuItem colorDeadItem;
	JMenuItem backColorItem;
	JMenuItem fontColorItem;
	JMenuItem outfileItem;
	JMenuItem gridItem;
	JMenuItem timeItem;
	JMenuItem numTicks;
	JMenuItem resetPrefItem;
	JMenuItem threadItem;


	/**
	 * Public MenuBar class creates a new JMenuBar 
	 */
	public menuBar() {
		fileMenu = new JMenu("File"); //Creating MenuBar Items
		loadItem = new JMenuItem("Load");
		loadItem.addActionListener(new ActionListener() { //Using File Chooser to select file to load 
			public void actionPerformed(ActionEvent e) {
				loadedFile = true;
	        } 
		});

		newItem = new JMenuItem("New"); //New game Menu Item
		newItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //Sets Reset Flag to true
				reset = true;
				System.out.println("RESET!");
			}
		});
		
		savePref = new JMenuItem("Save Preferences"); //Save Prefs item
		savePref.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //sets save Prefs flag to true
				writeFile();
			}
		});
		
		save = new JMenuItem("Save Current Game Through Max Step"); //Save trhough max step
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //sets saveGame flag to true
				saveGame = true;
			}
		});
		
		saveGrid = new JMenuItem("Save Current Grid as .txt"); //save current grid item
		saveGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //
				saveGrids = true;
			}
		});
		
		fileMenu.add(loadItem); //Adding all file Menu items
		fileMenu.add(newItem);
		fileMenu.add(savePref);
		fileMenu.add(save);
		fileMenu.add(saveGrid);
		add(fileMenu);
		
		settingsMenu = new JMenu("Settings"); //Creating New Menu and its items
		colorAliveItem = new JMenuItem("Alive Cell Color");
		colorDeadItem = new JMenuItem("Dead Cell Color");
		backColorItem = new JMenuItem("Background of Menus Color");
		fontColorItem = new JMenuItem("Font Color");
		
		 colorAliveItem.addActionListener(new ActionListener() { //new Color Chooser for Menuitem
	            public void actionPerformed(ActionEvent e) {
	            	colorAlive = JColorChooser.showDialog(null,
	                         "Choose Cell Alive color", Color.white);
	            	 updateFlag = true;
	               }
	    });
		
		colorDeadItem.addActionListener(new ActionListener() { //new Color Chooser for Menuitem
			public void actionPerformed(ActionEvent e) {
				colorDead = JColorChooser.showDialog(null,
                        "Choose Cell Dead color", Color.white);
				updateFlag = true;
			}
		});
		
		backColorItem.addActionListener(new ActionListener() { //new Color Chooser for Menuitem  //VLAIDATE THAT FONT AND THIS ARE NOT SAME COLOR
			public void actionPerformed(ActionEvent e) {
				backColor = JColorChooser.showDialog(null,
                        "Choose Background color", Color.white);
				updateFlag = true;
			}
		});
		
		fontColorItem.addActionListener(new ActionListener() { //new Color Chooser for Menuitem
            public void actionPerformed(ActionEvent e) {
            	fontColor = JColorChooser.showDialog(null,
                         "Choose Cell Alive color", Color.white);
            	 updateFlag = true;
               }
		});
		
		gridItem = new JMenuItem("Default Grid Size"); //Getting New Default Grid Size variables validated inputs
		gridItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int temp = defX;
					defX = Integer.parseInt(JOptionPane.showInputDialog("Please enter an integer x dimension"));
					if(defX <= 0) {
						System.out.println("Invalid Input. Using Saved Default: " + defX);
						defX = temp;
					}
				}
				catch (Exception f) {
					System.out.println("Invalid Input. Using Saved Default: " + defX);
				}
				if(defX <= 0) {
					System.out.println("Invalid Input. Using Saved Default: " + defX);
				}
				try {
					int temp = defY;
					defY = Integer.parseInt(JOptionPane.showInputDialog("Please enter an integer y dimension"));
					if(defY <= 0) {
						System.out.println("Invalid Input. Using Saved Default: " + defX);
						defY = temp;
					}
				}
				catch (Exception f) {
					System.out.println("Invalid Input. Using Saved Default: " + defY);
				}
				updateFlag = true;
			}
		});
		
		outfileItem = new JMenuItem("Output Pattern"); //getting new outFile template, validated input
		outfileItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outFile = JOptionPane.showInputDialog("Please enter the output file template you would like to use.");
				if(outFile.indexOf(".txt") != -1) {
					System.out.println("Removing '.txt' from outfile pattern");
					outFile.replace(".txt","");
				}
				updateFlag = true;
			}
		});
		
		resetPrefItem = new JMenuItem("Reset Preferences"); //Reset Preferences to originals
		resetPrefItem.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				colorAlive = Color.red;
				colorDead = new JButton().getBackground();
				backColor = new JButton().getBackground();
				fontColor = Color.black;
				defX = 5;
				defY = 5;
				outFile = "out.txt";
				savePrefs = true;
				updateFlag = true;
			}
		});

		timeItem = new JMenuItem("Time per Tick");
		timeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int temp = tickTime;
					tickTime = Integer.parseInt(JOptionPane.showInputDialog("Please enter an integer x dimension"));
					if(tickTime <= 0) {
						System.out.println("Invalid Input. Using Saved Default: " + temp);
						tickTime = temp;
					}
				}
				catch (Exception f) {
					System.out.println("Invalid Input. Using Saved Default: " + tickTime);
				}
				if(tickTime <= 0) {
					System.out.println("Invalid Input. Using Saved Default: " + tickTime);
				}
				updateTime = true;
			}
		});

		numTicks = new JMenuItem("Max Number of Ticks");
		numTicks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int temp = ticks;
					ticks = Integer.parseInt(JOptionPane.showInputDialog("Please enter an integer x dimension"));
					if(ticks <= 0) {
						System.out.println("Invalid Input. Using Saved Default: " + temp);
						ticks = temp;
					}
				}
				catch (Exception f) {
					System.out.println("Invalid Input. Using Saved Default: " + ticks);
				}
				if(ticks <= 0) {
					System.out.println("Invalid Input. Using Saved Default: " + ticks);
				}
				updateTicks = true;
			}
		});

		threadItem = new JMenuItem("Number of Threads");
		threadItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int temp = threads;
					threads = Integer.parseInt(JOptionPane.showInputDialog("Please enter one of the given number of threads (1,2,4,8,16)"));
					if((threads != 1) && (threads != 2) && (threads != 4) && (threads != 8) && (threads != 16)){
						System.out.println("Invalid Input. Using Saved Default: " + temp);
						threads = temp;
					}
					System.out.println("THREADS: " + threads);

				}
				catch (Exception f) {
					System.out.println("Invalid Input. Using Saved Default: " + threads);
				}
				if(threads <= 0) {
					System.out.println("Invalid Input. Using Saved Default: " + threads);
				}
			}
		});

		settingsMenu.add(colorAliveItem); //adding settings items
		settingsMenu.add(colorDeadItem);
		settingsMenu.add(backColorItem);
		settingsMenu.add(fontColorItem);
		settingsMenu.add(outfileItem);
		settingsMenu.add(gridItem);
		settingsMenu.add(timeItem);
		settingsMenu.add(numTicks);
		settingsMenu.add(threadItem);
		settingsMenu.add(resetPrefItem);
		add(settingsMenu);
	}

	/**
	 * Syncing Colors of all of the UI pieces
	 */
	public void setColors(){
		setBackground(backColor);
		settingsMenu.setBackground(backColor);
		colorAliveItem.setBackground(backColor);
		colorDeadItem.setBackground(backColor);
		backColorItem.setBackground(backColor);
		fontColorItem.setBackground(backColor);
		outfileItem.setBackground(backColor);
		gridItem.setBackground(backColor);
		resetPrefItem.setBackground(backColor);
		fileMenu.setBackground(backColor);
		loadItem.setBackground(backColor);
		newItem.setBackground(backColor);
		savePref.setBackground(backColor);
		save.setBackground(backColor);
		saveGrid.setBackground(backColor);
		timeItem.setBackground(backColor);
		numTicks.setBackground(backColor);
		threadItem.setBackground(backColor);

		setForeground(fontColor);
		settingsMenu.setForeground(fontColor);
		fileMenu.setForeground(fontColor);
		colorAliveItem.setForeground(fontColor);
		colorDeadItem.setForeground(fontColor);
		backColorItem.setForeground(fontColor);
		fontColorItem.setForeground(fontColor);
		outfileItem.setForeground(fontColor);
		gridItem.setForeground(fontColor);
		resetPrefItem.setForeground(fontColor);
		loadItem.setForeground(fontColor);
		newItem.setForeground(fontColor);
		savePref.setForeground(fontColor);
		save.setForeground(fontColor);
		saveGrid.setForeground(fontColor);
		timeItem.setForeground(fontColor);
		numTicks.setForeground(fontColor);
		threadItem.setForeground(fontColor);
	}

	/**
	 * Function used to write the MyPreferences File
	 */
	public static void writeFile(){
		System.out.println("Saved Prefs!");
		FileWriter fw = null;
		try {
			fw = new FileWriter(menuBar.pref);
			//Writing all preference settings to txt file
			fw.write(menuBar.colorAlive + "\n");
			fw.write(menuBar.colorDead + "\n");
			fw.write(menuBar.backColor + "\n");
			fw.write(menuBar.fontColor + "\n");
			fw.write(menuBar.defX + "\n");
			fw.write(menuBar.defY + "\n");
			fw.write(menuBar.outFile + "\n");
			fw.write(menuBar.l + "\n");
			fw.write(menuBar.s + "\n");
			fw.write(tickTime + "\n");
			fw.write(ticks + "\n");
			fw.write(Integer.toString(threads));
			fw.close();
		} catch (IOException e) {

		}
	}
}
