package ganzj2.hw04;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class gridPanel extends JPanel {
    public static int a, b, c, d;
    public static byte[][] new_board;

    /**
     * Instance of the gridpanel class used to construct a default panel
     * @param x - Integer X Dimension
     * @param y - Integer Y Dimension
     * @param w - Integer ScreenWidth
     * @param h - Integer ScreenHeight
     */
    public gridPanel(int x, int y, int w, int h)

    {
        a = x;
        b = y;
        c = w;
        d = h;

    }

    /**
     * Instance of the gridpanel class used to construct an initialized panel
     * @param x - Integer X Dimension
     * @param y - Integer Y Dimension
     * @param w - Integer ScreenWidth
     * @param h - Integer ScreenHeight
     * @param g - 2D byte Array of the grid
     */
    public gridPanel(int x, int y, int w, int h, byte[][] g){
        removeAll();
        revalidate();
        a = x;
        b = y;
        c = w;
        d = h;
    }

    /**
     * Function used to reduce the grid down to a size that can be displayed of the screen if the grid is initially too large
     * @param n - Integer representing the factor at which to reduce the board
     */
    public void transformGrid(int n) {
        System.out.println("Board Size larger than number of pixels available. Board Size being simplified by a factor of " + n);
        int new_x = (a / n);
        if (a % n != 0) {
            new_x++;
        }
        System.out.println(new_x);
        int new_y = b / n;
        if (b % n != 0) {
            new_y++;
        }
        new_board = new byte[new_y][new_x];
        int alive = 0, new_a = 0, new_b = 0, countera = 0, counterb = 0;
        //Create new board
        for (int i = 0; i < b; i+=n) { //for size of board
            for (int j = 0; j < a; j+=n) {
                for (int k = 0; k < n; k++) { // for 3 neighbors around the cell
                    for (int q = 0; q < n; q++) {
                        new_a = Math.abs((i + b + k)%b);
                        new_b = Math.abs((j + a + q)%a);
                        try {
                            if (MultiConwayMain.gb[new_a][new_b] == 1) {
                                alive++;
                            }
                        }
                        catch(NullPointerException e){

                        }
                    }
                }
                if (alive >= ((n*n) / 2)){ //if majority of cells is alive
                    new_board[i/n][j/n] = 1;
                }
                else{
                    new_board[i/n][j/n] = 0;
                }
                alive = 0;
                counterb++;
            }
            counterb = 0;
            countera++;
        }

    }

    /**
     * Function used ot paint the UI grid
     * Determines whether the Grid needs to be reduced to fit
     * @param g - Graphics
     */
    public void paint(Graphics g){
        double x_gap = c * 1.0 / (a);
        double y_gap = (d - 100) * 1.0 / (b); //Subtraction compensates for menubar
        boolean drawLines = (x_gap < 1) || (y_gap < 1);
        Graphics2D g2 = (Graphics2D) g;

        if(MultiConwayMain.gb != null) {
            if (drawLines) {                  //frame must be resized
                System.out.println("Resizing Frame");
                int counter = 0;
                while ((x_gap < 1) || (y_gap < 1)) { //Finding ratio that will make grid fit
                    x_gap = x_gap * 2;
                    y_gap = y_gap * 2;
                    counter++;
                }
                counter = counter*2;
                transformGrid(counter);
                for (int i = 0; i < b/counter; i++) { //Drawing Lines
                    for (int j = 0; j < a/counter; j++) {
                        if (new_board[i][j] == 1) { //cell is alive
                            g2.setColor(menuBar.colorAlive);
                            g2.fill(new Rectangle2D.Double(x_gap * j, y_gap * i, x_gap, y_gap));
                        } else { //cell is dead
                            g2.setColor(menuBar.colorDead);
                            g2.fill(new Rectangle2D.Double(x_gap * j, y_gap * i, x_gap, y_gap));
                        }
                            g2.setColor(Color.black);
                            g2.draw(new Rectangle2D.Double(x_gap * j, y_gap * i, x_gap, y_gap));

                    }
                }

            }
            else{
                for (int i = 0; i < b; i++) {
                    for (int j = 0; j < a; j++) {
                        if (MultiConwayMain.gb[i][j] == 1) { //cell is alive
                            g2.setColor(menuBar.colorAlive);
                            g2.fill(new Rectangle2D.Double(x_gap * j, y_gap * i, x_gap, y_gap));
                        } else { //cell is dead
                            g2.setColor(menuBar.colorDead);
                            g2.fill(new Rectangle2D.Double(x_gap * j, y_gap * i, x_gap, y_gap));
                        }
                        if (!drawLines) {
                            g2.setColor(Color.black);
                            g2.draw(new Rectangle2D.Double(x_gap * j, y_gap * i, x_gap, y_gap));
                        }

                    }
                }
            }
        }
        else{
            if(!drawLines){
                for (int i = 0; i < b; i++) {
                    for (int j = 0; j < a; j++) {
                        g2.draw(new Rectangle2D.Double(x_gap * j, y_gap * i, x_gap, y_gap));
                    }
                }
            }
            else{
                System.out.println("Grid Resized");
                while ((x_gap < 1) || (y_gap < 1)) {
                    x_gap = x_gap * 4;
                    y_gap = y_gap * 4;
                }
                for (int i = 0; i < b / 4; i++) {
                    for (int j = 0; j < a / 4; j++) {
                        g2.draw(new Rectangle2D.Double(x_gap * j, y_gap * i, x_gap, y_gap));
                    }
                }
            }

        }
        }
    }
