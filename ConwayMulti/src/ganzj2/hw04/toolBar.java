package ganzj2.hw04;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class toolBar extends JToolBar {
    public static boolean running = false;
    public static boolean forward = false;
    public static boolean backward = false;
    public static int desiredStep = -1;

    JButton startButton;
    JButton stopButton;
    JButton forwardButton;
    JButton backButton;
    JTextField goToButton;
    JLabel stepDisplay;
    JLabel maxstepDisplay;

    /**
     * Public Instance of toolbar used to generate the ToolBar for the UI
     * Allows user to interact with the UI similar to the hotKeys panel I had written in HW2
     */
    public toolBar(){
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() { //Setting game state to true if selected, handled in MAIN class
            public void actionPerformed(ActionEvent e) {
                running = true;
            }
        });
        add(startButton);
        stopButton = new JButton("Pause");
        stopButton.addActionListener(new ActionListener() { //Setting game state to true if selected, handled in MAIN class
            public void actionPerformed(ActionEvent e) {
                running = false;
            }
        });
        add(stopButton);
        forwardButton = new JButton("Forward Tick");
        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                forward = true;
            }
        });
        add(forwardButton);
        backButton = new JButton("Backward Tick");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                backward = true;
            }
        });
        add(backButton);
        goToButton = new JTextField("Go To Tick");
        goToButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(running == false) { //if program is not currently running
                    try{
                        desiredStep = Integer.parseInt(goToButton.getText());
                    }
                    catch (Exception f) {
                        System.out.println("Invalid Input!");
                        desiredStep = -1;
                    }
                }
                goToButton.setText("Go To Certain Tick");
            }
        });
        add(goToButton);
        stepDisplay = new JLabel("Step: ");
        maxstepDisplay = new JLabel("Max Step: ");
        add(stepDisplay);
        add(maxstepDisplay);
    }

    /**
     * Function used to set all the UI colors the same when selected
     */
    public void setColors(){
        setBackground(menuBar.backColor);
        startButton.setBackground(menuBar.backColor);
        stopButton.setBackground(menuBar.backColor);
        forwardButton.setBackground(menuBar.backColor);
        backButton.setBackground(menuBar.backColor);
        goToButton.setBackground(menuBar.backColor);
        stepDisplay.setBackground(menuBar.backColor);
        maxstepDisplay.setBackground(menuBar.backColor);

        setForeground(menuBar.fontColor);
        startButton.setForeground(menuBar.fontColor);
        stopButton.setForeground(menuBar.fontColor);
        forwardButton.setForeground(menuBar.fontColor);
        backButton.setForeground(menuBar.fontColor);
        goToButton.setForeground(menuBar.fontColor);
        stepDisplay.setForeground(menuBar.fontColor);
        maxstepDisplay.setForeground(menuBar.fontColor);
    }
}
