package hw03.ganzj2;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;

public class GamePanel extends JPanel{
	private static final long serialVersionUID = 1L;

	/**
	 * Function used create a Game Panel instance
	 * @param grid - Integer Array for the current grid
	 * @param buttons - JButton Array for representing the current grid
	 * @param name - String containing name of the Game Panel
	 */
	public GamePanel(int[][] grid, JButton[][] buttons, String name) {
		setPreferredSize(new Dimension(600, 600));
		setBackground(Color.DARK_GRAY);
		setLayout(new GridLayout(11,11));
		Border border = BorderFactory.createTitledBorder(name);
		setBorder(border);

		buttons[0][0].setBackground(Menubar.blankColor);
		add(buttons[0][0]);
		
		for(int i = 0; i < 11; i++) {
			for(int j = 0; j < 11; j++) {
				if(i != 0 && j != 0) {
					if(grid[i-1][j-1] == 0) { //Cell is empty
						buttons[i][j].setBackground(Menubar.blankColor);
					}
					else if(grid[i-1][j-1] == 1) { //Cell holds users ship
						buttons[i][j].setBackground(Menubar.shipColor);
					}
					else if(grid[i-1][j-1] == 2) { //Cell holds hit enemy ship
						buttons[i][j].setBackground(Menubar.hitColor);
					}
					else if(grid[i-1][j-1] == 3) { //Cell holds missed shot
						buttons[i][j].setBackground(Menubar.missColor);
					}
					else { //Cell holds users hit ship
						buttons[i][j].setBackground(Menubar.shipHitColor);
					}
				}
				else {
					buttons[i][j].setBackground(Menubar.blankColor);
				}
				add(buttons[i][j]);
			}
		}
	}
}
