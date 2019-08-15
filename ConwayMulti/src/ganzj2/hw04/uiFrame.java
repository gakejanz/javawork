package ganzj2.hw04;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class uiFrame extends JFrame {
    int screenHeight;
    int screenWidth;
    int x, y;
    gridPanel grid;
    int[][] gb;
    toolBar tb;
    menuBar mb;

    /**
     * Creation of the UI for the program
     */
    public uiFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenHeight = (int) screenSize.getHeight();
        screenWidth = (int) screenSize.getWidth();
        setTitle("Conway Game of Life");
        setSize(screenWidth, screenHeight);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        mb = new menuBar();
        setJMenuBar(mb);
        tb = new toolBar();
        add(tb, BorderLayout.NORTH);
    }

    /**
     * Function used to create and display a default grid
     * @param a - Integer a is the default x dimension for the grid
     * @param b - Integer b is the default y dimension for the grid
     */
    public void addGrid(int a, int b) {
        grid = new gridPanel(a,b, screenWidth, screenHeight);
        add(grid);
        revalidate();
    }

    /**
     * Function used to update/create a grid panel that has been initialized in the main
     * @param a - Integer a is the default x dimension for the grid
     * @param b - Integer b is the default y dimension for the grid
     * @param g - 2D byte Array representing the initialized grid
     */
    public void updateGrid(int a, int b, byte[][] g){
        grid = new gridPanel(a,b,screenWidth, screenHeight, g);
        add(grid);
        revalidate();
    }

    /**
     * Function used to update the states of the Menu and Tool Bars
     */
    public void updateBars(){
        setBackground(mb.backColor);
        setForeground(mb.fontColor);
        mb.setColors();
        tb.setColors();
    }

    /**
     * Function used to give the default X dimension to the Main
     * @return - Integer of the default X dimension
     */
    public int getdefX(){
        return mb.defX;
    }

    /**
     * Function used to give the default Y dimension to the Main
     * @return - Integer of the defualt Y dimension
     */
    public int getdefY(){
        return mb.defY;
    }
}
