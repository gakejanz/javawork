package hw03.ganzj2;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GameFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	static int screenSize;
	
	/**
	 * Function used to build the Game Frame
	 */
	public GameFrame() {
		setTitle("Battleship");
		setSize(new Dimension(1200,800));
		setJMenuBar(new Menubar());
	}
}
