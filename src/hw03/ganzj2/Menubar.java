package hw03.ganzj2;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Menubar extends JMenuBar{

	private static final long serialVersionUID = 1L;
	static Color blankColor = Color.gray;
	static Color shipColor = Color.blue;
	static Color hitColor = Color.red;
	static Color missColor = Color.white;
	static Color shipHitColor = Color.orange;
	static int turnTime = 30;
	static int defaultPort = 9586;
	static int numShips = 5;
	static int[] shipLengths = new int[numShips];
	static boolean updateLengths = false;
	static String defaultIP = null;
	static boolean updateFlag = false;
	public static boolean savePrefs = false; //save Preferences Flag
	public static String pref = "myPreferences.txt"; //preferences file name
	
	/**
	 * Function used to create the menubar of the UI
	 */
	public Menubar(){
		var settingsMenu = new JMenu("Settings"); //Creating New Menu and its items
		var blankColorItem = new JMenuItem("Default Color");
		var shipColorItem = new JMenuItem("Ship Color");
		var hitColorItem = new JMenuItem("Hit Ship Color");
		var missColorItem = new JMenuItem("User's Missed Ship Color");
		var shipHitColorItem = new JMenuItem("User's  Hit Ship Color");
		var turnTimeItem = new JMenuItem("Turn Time");
		var defaultPortItem = new JMenuItem("Default Port");
		var numShipsItem = new JMenuItem("Number of Ships");
		var shipLengthItem = new JMenuItem("Length of Ships");
		
		blankColorItem.addActionListener(new ActionListener() { //new Color Chooser for Menuitem
	            public void actionPerformed(ActionEvent e) {
	            	blankColor = JColorChooser.showDialog(null,
	                         "Choose Cell Alive color", Color.white);
	            	 updateFlag = true;
	               }
	    });
		 
		shipColorItem.addActionListener(new ActionListener() { //new Color Chooser for Menuitem
            public void actionPerformed(ActionEvent e) {
            	shipColor = JColorChooser.showDialog(null,
                         "Choose Cell Alive color", Color.white);
            	 updateFlag = true;
               }
		});
		 
		hitColorItem.addActionListener(new ActionListener() { //new Color Chooser for Menuitem
            public void actionPerformed(ActionEvent e) {
            	 hitColor = JColorChooser.showDialog(null,
                         "Choose Cell Alive color", Color.white);
            	 updateFlag = true;
               }
		});
		
		missColorItem.addActionListener(new ActionListener() { //new Color Chooser for Menuitem
            public void actionPerformed(ActionEvent e) {
            	 missColor = JColorChooser.showDialog(null,
                         "Choose Cell Alive color", Color.white);
            	 updateFlag = true;
               }
		});
		
		shipHitColorItem.addActionListener(new ActionListener() { //new Color Chooser for Menuitem
            public void actionPerformed(ActionEvent e) {
            	shipHitColor = JColorChooser.showDialog(null,
                         "Choose Cell Alive color", Color.white);
            	 updateFlag = true;
               }
		});
		
		turnTimeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int temp = turnTime;
					turnTime = Integer.parseInt(JOptionPane.showInputDialog("Please enter an integer turn length"));
					if(turnTime <= 0) {
						System.out.println("Invalid Input. Using Saved Default: " + temp);
						turnTime = temp;
					}
				}
				catch (Exception f) {
					System.out.println("Invalid Input. Using Saved Default: " + turnTime);
				}
				if(turnTime <= 0) {
					System.out.println("Invalid Input. Using Saved Default: " + turnTime);
				}
				updateFlag = true;
			}
		});

		defaultPortItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int temp = defaultPort;
					defaultPort = Integer.parseInt(JOptionPane.showInputDialog("Please enter an integer Port"));
					if(defaultPort <= 0) {
						System.out.println("Invalid Input. Using Saved Default: " + temp);
						defaultPort = temp;
					}
				}
				catch (Exception f) {
					System.out.println("Invalid Input. Using Saved Default: " + defaultPort);
				}
				if(defaultPort <= 0) {
					System.out.println("Invalid Input. Using Saved Default: " + defaultPort);
				}
				updateFlag = true;
			}
		});
		
		numShipsItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int temp = numShips;
					numShips = Integer.parseInt(JOptionPane.showInputDialog("Please enter an integer number of ships up to 10 ships"));
					if((numShips <= 0) || (numShips > 10)) {
						System.out.println("Invalid Input. Using Saved Default: " + temp);
						numShips = temp;
					}
				}
				catch (Exception f) {
					System.out.println("Invalid Input. Using Saved Default: " + numShips);
				}
				if((numShips <= 0) || (numShips > 10)) {
					System.out.println("Invalid Input. Using Saved Default: " + numShips);
				}
				updateFlag = true;
				updateLengths = true;
			}
		});
		
		
		var savePref = new JMenuItem("Save Preferences"); //Save Prefs item
		savePref.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //sets save Prefs flag to true
				savePrefs = true;
			}
		});
		
		var resetPrefItem = new JMenuItem("Reset Preferences"); //Reset Preferences to originals
		resetPrefItem.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				System.out.println("Preferences Reset!");
				blankColor = Color.gray;
				shipColor = Color.blue;
				hitColor = Color.red;
				missColor = Color.white;
				shipHitColor = Color.orange;
				turnTime = 30;
				defaultPort = 9586;
				defaultIP = null;
				savePrefs = true;
				updateFlag = true;
			}
		});

		settingsMenu.add(blankColorItem);
		settingsMenu.add(shipColorItem);
		settingsMenu.add(hitColorItem);
		settingsMenu.add(shipHitColorItem);
		settingsMenu.add(turnTimeItem);
		settingsMenu.add(defaultPortItem);
		settingsMenu.add(numShipsItem);
		settingsMenu.add(savePref);
		settingsMenu.add(resetPrefItem);
		
		add(settingsMenu);
	}
}
