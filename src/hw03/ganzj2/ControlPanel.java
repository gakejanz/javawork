package hw03.ganzj2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	static boolean exit = false;
	static boolean rot = false;
	static boolean l = false;
	static boolean r = false;
	static boolean u = false;
	static boolean d = false;
	static JButton playerScore;
	static JButton opponentScore;
	static JButton time;
	
	/**
	 * Function that builds bottom part of UI
	 * Contains ALL movement buttons
	 * Contains Scores display
	 * Contains timer display
	 */
	public ControlPanel() {
		
		JButton rotate = new JButton("Rotate");
		rotate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rot = true;
			}
		});
		
		JButton end = new JButton("End Turn");
		end.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exit = true;
			}
		});
		
		JButton left = new JButton("Move Left");
		left.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				l = true;
			}
		});
		
		JButton right = new JButton("Move Right");
		right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				r = true;
			}
		});
		
		JButton up = new JButton("Move Up");
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				u = true;
			}
		});
		
		JButton down = new JButton("Move Down");
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				d = true;
			}
		});
		
		JPanel moves = new JPanel();
		moves.setLayout(new BorderLayout());
		moves.add(up, BorderLayout.NORTH);
		moves.add(down, BorderLayout.SOUTH);
		moves.add(left, BorderLayout.WEST);
		moves.add(right, BorderLayout.EAST);
		
		playerScore = new JButton();
		playerScore.setPreferredSize(new Dimension(100,50));
		
		opponentScore = new JButton();
		opponentScore.setPreferredSize(new Dimension(100,50));
		
		time = new JButton();
		time.setPreferredSize(new Dimension(150, 50));
		
		JPanel Holder = new JPanel();
		Holder.setLayout(new GridLayout(0,5));
		Holder.add(playerScore);
		Holder.add(rotate);
		Holder.add(moves);
		Holder.add(end);
		Holder.add(opponentScore);
		
		JPanel smallHolder = new JPanel();
		smallHolder.add(time);
		
		setLayout(new GridLayout(2,0));
		
		
		add(Holder);
		add(smallHolder);
		
		setPreferredSize(new Dimension(1200, 200));
		setBackground(Color.blue);
	}
}
