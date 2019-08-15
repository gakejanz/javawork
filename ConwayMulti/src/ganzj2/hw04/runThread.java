package ganzj2.hw04;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class runThread implements Runnable {

    static byte[][] gb;
    static int x;
    static int y;
    static int step;
    static int maxStep;
    static int yGain;
    static int xGain;
    static uiFrame frame;
    static String name;
    ReentrantLock lock = new ReentrantLock();
    Thread t;
    static int time = menuBar.tickTime;

    /**
     * Instance of the runThread Class
     * @param thread - String Name of Thread
     * @param oldg - 2D Byte Array Grid for portion of the game board
     * @param a - Integer A is the X Dimension of oldg
     * @param b - Integer B is the Y Dimension of oldg
     * @param f - uiFrame Instance from the Main that can be used to access variables of that instance
     * @param s - Integer of the current step in the game
     * @param s2 - Integer of the max step of the game
     * @param c - Integer of the Gain in Y needed to update the Main's gameboard correctly
     * @param d - Integer of the Gain in X needed to update the Main's gameboared correctly
     */
    runThread (String thread, byte[][] oldg, int a, int b, uiFrame f, int s, int s2, int c, int d){
        gb = new byte[b][a];
        x = a;
        y = b;
        int i = 0;
        int j = 0;
            for (i = 0; i < y; i++) {
                for (j = 0; j < x; j++) {
                    gb[i][j] = oldg[i][j];
                }
            }
        frame = f;
        name = thread;
        step = s;
        maxStep = s2;
        yGain = c;
        xGain = d;
        t = new Thread(this, name);
        t.start();
    }

    /**
     * Run Function for the instance of the Thread
     * Calculates the next generation and updates the Main Grid with this new information
     */
    public void run(){
            lock.lock();
            try {
                System.out.println("THREAD: " + name + " entered");
                gb = nextGeneration(gb, x, y);
                updateBoard();
                System.out.println("THREAD: " + name + " exited");
            }
            finally {
                lock.unlock();
            }
    }

    /**
     * Function used to the next generation of the given grid
     * @param grid - 2D Byte Array of the given grid
     * @param M - Integer X Dimension of the given grid
     * @param N - Integer Y Dimension of the given grid
     * @return - 2D byte array of the next generation of the given grid
     */
    public static byte[][] nextGeneration(byte grid[][], int M, int N) //Creating nextGen // adjust for full board
    {
        byte[][] future = new byte[N][M];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) //Looping through every cell
            {
                int aliveNeighbours = 0;
                for (int n = -1; n < 2; n++) { //finding alive neighbours
                    for (int r = -1; r < 2; r++) {
                        if (n != 0 || r != 0) {
                            int a = Math.abs((i + n + N) % N);
                            int b = Math.abs((r + j + M) % M);
                            aliveNeighbours += MultiConwayMain.gb[a][b];
                        }
                    }
                }
                // Cell dies
                if ((MultiConwayMain.gb[i][j] == 1) && (aliveNeighbours < 2)) {
                    future[i][j] = 0;
                    //passed++;
                }
                // Cell dies
                else if ((MultiConwayMain.gb[i][j] == 1) && (aliveNeighbours > 3)) {
                    future[i][j] = 0;
                    //passed++;
                }
                // A new cell is born
                else if ((MultiConwayMain.gb[i][j] == 0) && (aliveNeighbours == 3)) {
                    future[i][j] = 1;
                    //birthed++;
                }
                // Remains the same
                else
                    future[i][j] = MultiConwayMain.gb[i][j];
            }
        }
        return future;
    }

    /**
     * Function used to update and interact with the MAIN game board after the next step has been generated on this thread instance
     */
    public static void updateBoard(){


        int i = 0;
        int j = 0;

        System.out.println("THREAD: " + name + " YGAIN: " + yGain + " XGAIN: " + xGain);
            for (i = 0; i < y; i++) {
                for (j = 0; j < x; j++) {

                    if((i + yGain) == MultiConwayMain.y){
                        yGain -= 1;
                    }

                    if((j + xGain) == MultiConwayMain.x){
                        xGain -= 1;
                    }

                    MultiConwayMain.gb[i + yGain][j + xGain] = gb[i][j];
                }
            }
    }

}

