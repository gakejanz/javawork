package HW02.ganzj2;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

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
	public static String l = ""; //public variable Load File string
	public static boolean reset = false; //public reset game variable
	public static Color colorAlive = Color.red; //Alive cell color
	public static Color colorDead = new JButton().getBackground(); //dead cell color
	public static Color backColor = new JButton().getBackground(); //background of grid color
	public static Color hotColor = Color.gray; //hotkey background color
	public static Color statColor = Color.gray; //stats background color
	public static Color fontColor = Color.black; //Font color
	public static int defX = 5; //Default X Dimension
	public static int defY = 5; //default Y Dimension
	public static String outFile = "out"; //default output pattern
	public static String pref = "myPreferences.txt"; //preferences file name
	public static boolean savePrefs = false; //save Preferences Flag
	public static boolean saveGame = false; //save game flag
	public static boolean saveGrids = false; //save grids flag
	public static boolean updateFlag = false; //update UI flag
	public static String s = null; //save file string
	public static boolean loadedFile = false; //loadedfile boolean
	
	/**
	 * Public MenuBar class creates a new JMenuBar 
	 */
	public menuBar() {
		var fileMenu = new JMenu("File"); //Creating MenuBar Items
		var loadItem = new JMenuItem("Load");
		loadItem.addActionListener(new ActionListener() { //Using File Chooser to select file to load 
			public void actionPerformed(ActionEvent e) {
				JFileChooser j = new JFileChooser();
				int r = j.showSaveDialog(null);
				l = null;
				try {	
					if (r == JFileChooser.APPROVE_OPTION) 
						  
		            { 
		                // set the label to the path of the selected file 
		                l = (j.getSelectedFile().getAbsolutePath()); 
		            } 
		            // if the user cancelled the operation 
		            else
		                l = (null); 
					BufferedReader testRead = new BufferedReader(new FileReader(l)); //validating selection
					testRead.readLine();
					testRead.close();
;					loadedFile = true;
				}
				catch (Exception ex){
					System.out.println("Error on File Selection! Try Again!");
				}
	        } 
		});
		var newItem = new JMenuItem("New"); //New game Menu Item
		newItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //Sets Reset Flag to true
				reset = true;
				System.out.println("RESET!");
			}
		});
		
		var savePref = new JMenuItem("Save Preferences"); //Save Prefs item
		savePref.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //sets save Prefs flag to true
				savePrefs = true;
			}
		});
		
		var save = new JMenuItem("Save Current Game Through Max Step"); //Save trhough max step 
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //sets saveGame flag to true
				saveGame = true;
			}
		});
		
		var saveGrid = new JMenuItem("Save Current Grid as .txt"); //save current grid item
		saveGrid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //
				do {
					s = JOptionPane.showInputDialog("Please enter a file name to write into."); //validating input and setting flag to true
				}while(s.indexOf(".txt") != -1);
				saveGrids = true;
			}
		});
		
		fileMenu.add(loadItem); //Adding all file Menu items
		fileMenu.add(newItem);
		fileMenu.add(savePref);
		fileMenu.add(save);
		fileMenu.add(saveGrid);
		add(fileMenu);
		
		var settingsMenu = new JMenu("Settings"); //Creating New Menu and its items
		var colorAliveItem = new JMenuItem("Alive Cell Color");
		var colorDeadItem = new JMenuItem("Dead Cell Color");
		var backColorItem = new JMenuItem("Background of Board Color");
		var hotColorItem = new JMenuItem("Background Color of Hotkeys");
		var statColorItem = new JMenuItem("Background Color of Stats");
		var fontColorItem = new JMenuItem("Font Color");
		
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
		
		backColorItem.addActionListener(new ActionListener() { //new Color Chooser for Menuitem
			public void actionPerformed(ActionEvent e) {
				colorDead = JColorChooser.showDialog(null,
                        "Choose Background color", Color.white);
				updateFlag = true;
			}
		});
		
		hotColorItem.addActionListener(new ActionListener() { //new Color Chooser for Menuitem
            public void actionPerformed(ActionEvent e) {
            	hotColor = JColorChooser.showDialog(null,
                         "Choose Cell Alive color", Color.white);
            	 updateFlag = true;
               }
		});
		
		statColorItem.addActionListener(new ActionListener() { //new Color Chooser for Menuitem
            public void actionPerformed(ActionEvent e) {
            	statColor = JColorChooser.showDialog(null,
                         "Choose Cell Alive color", Color.white);
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
		
		var gridItem = new JMenuItem("Default Grid Size"); //Getting New Default Grid Size variables validated inputs
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
		
		var outfileItem = new JMenuItem("Output Pattern"); //getting new outFile template, validated input
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
		
		var resetPrefItem = new JMenuItem("Reset Preferences"); //Reset Preferences to originals
		resetPrefItem.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				colorAlive = Color.red;
				colorDead = new JButton().getBackground();
				backColor = new JButton().getBackground();
				hotColor = Color.gray;
				statColor = Color.gray;
				fontColor = Color.black;
				defX = 5;
				defY = 5;
				outFile = "out.txt";
				savePrefs = true;
				updateFlag = true;
			}
		});
		
		settingsMenu.add(colorAliveItem); //adding settings items
		settingsMenu.add(colorDeadItem);
		settingsMenu.add(backColorItem);
		settingsMenu.add(hotColorItem);
		settingsMenu.add(statColorItem);
		settingsMenu.add(fontColorItem);
		settingsMenu.add(outfileItem);
		settingsMenu.add(gridItem);
		settingsMenu.add(resetPrefItem);
		add(settingsMenu);
	}
	
}
