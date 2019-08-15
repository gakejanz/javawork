
package ganzj2.hw04;

import javax.swing.*;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.awt.*;
import java.io.*;
import java.nio.Buffer;
import java.util.concurrent.*;

import static java.lang.StrictMath.floor;

public class MultiConwayMain {
    static uiFrame frame;
    static int x; //Default
    static int y;//Default
    static byte[][] gb = null; //Game Board Integer Array
    static boolean game = true;
    static int step = 0;
    static int maxStep = 10;
    static byte[][] ogGB;
    static int time = 500;

    /**
     * Main Function for the program - Sets up UI and game threads
     * @param args - Command Line Arguments
     * @throws IOException - Handled Elsewhere
     * @throws InterruptedException - Handled Elsewhere
     */
    public static void main(String[] args)  throws IOException, InterruptedException{

        SwingUtilities.invokeLater(() ->  { //building UI on EDT Thread
                try {
                    buildFrame();
                } catch (IOException e) {
                    //handled
                }

                x = frame.getdefX();
                y = frame.getdefY();
                maxStep = frame.mb.ticks;
            }

        );
        boolean load = loadChoice(); //Getting loading option from use

        initializeGrid(load); //Initializing Grid based on users choice
        //new runThread("A" , gb, ogGB, x, y);

        runGame(); //Single Thread Testing Only


    }

    /**
     * Function to build the frame UI
     * @throws IOException - Handled Elsewhere
     */
    public static void buildFrame() throws IOException{ //Building Frame Function
        frame = new uiFrame();
        frame.addGrid(x, y);
        readPrefs();
        frame.revalidate();
    }

    /**
     * Function that makes the user decide their load option, with a default of a random generated grid
     * @return Boolean of the users' decision
     */
    public static boolean loadChoice() {
        String[] options = {"Load a File", "Randomize File"};
        int x = JOptionPane.showOptionDialog(null, "Would you like to load a file or randomly generate one?",
                "File Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
        if (x == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Function to initialize the Game Grids
     * @param load - Boolean of the load choice of the user
     * @throws IOException - Handled Elsewhere
     */
    public static void initializeGrid(boolean load) throws IOException {
        if (load == false) { //board is randomly generated
            gb = new byte[y][x];
            ogGB = new byte[y][x];
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    if (Math.random() < 0.5) { //cell randomly generated to be dead
                        gb[i][j] = 0;
                        ogGB[i][j] = 0;
                    } else { //cell is randomly generated to be alive
                        gb[i][j] = 1;
                        ogGB[i][j] = 1;
                    }
                }
            }
            frame.updateGrid(x, y, gb);
            frame.revalidate();
        }
        else{
            JFileChooser j = new JFileChooser();
            int r = j.showSaveDialog(null);
            String file = null;
            try {
                if (r == JFileChooser.APPROVE_OPTION)

                {
                    // set the label to the path of the selected file
                    file = (j.getSelectedFile().getAbsolutePath());
                }
                // if the user cancelled the operation
                else
                    file = (null);
                BufferedReader testRead = new BufferedReader(new FileReader(file)); //validating selection
                testRead.readLine();
                testRead.close();
                ;
            }
            catch (Exception ex){
                System.out.println("Error on File Selection! Try Again!");
            }
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(file));
            }
            catch(Exception e){
                //Already handled
            }

            String reader = br.readLine();
            String[] readerSplit = reader.split(", "); //reading first line to gather dimensions of game board
            try{  //ensuring the file contains the correct information
                x = Integer.parseInt(readerSplit[1]);
            }
            catch(Exception f){
                System.out.println("Invalid File!");
            }
            y = Integer.parseInt(readerSplit[0]);

            step = Integer.parseInt(readerSplit[2]);
            gb = new byte[y][x];
            ogGB = new byte[y][x];

            for(int i = 0; i < y; i++){ //parsing input file lines
                reader = br.readLine();
                readerSplit = reader.split(", ");

                for(int k = 0; k < x; k++){
                    gb[i][k] = (readerSplit[k]).getBytes()[0]; //filling in game board with input file data
                }
            }
            for(int i = 0; i < y; i++){ //parsing input file lines
                reader = br.readLine();
                readerSplit = reader.split(", ");
                for(int k = 0; k < x; k++){
                    ogGB[i][k] = (readerSplit[k]).getBytes()[0]; //filling in game board with input file data
                }
            }
            br.close();
            frame.updateGrid(x, y, gb);

            frame.revalidate();
        }
    }

    /**
     * Function used to calculate the game grids next generation
     *
     * @param grid 2D Integer matrix representing the game grid
     * @param M    Integer representing the x dimension of grid
     * @param N    Integer representing the y dimension of the grid
     * @return 2D Integer matrix representing the game grids next generation
     */

    public static byte[][] nextGeneration(byte grid[][], int M, int N) //Creating nextGen
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
                            aliveNeighbours += grid[a][b];
                        }
                    }
                }
                // Cell dies
                if ((grid[i][j] == 1) && (aliveNeighbours < 2)) {
                    future[i][j] = 0;
                    //passed++;
                }
                // Cell dies
                else if ((grid[i][j] == 1) && (aliveNeighbours > 3)) {
                    future[i][j] = 0;
                    //passed++;
                }
                // A new cell is born
                else if ((grid[i][j] == 0) && (aliveNeighbours == 3)) {
                    future[i][j] = 1;
                    //birthed++;
                }
                // Remains the same
                else
                    future[i][j] = grid[i][j];
            }
        }
        return future;
    }

    /**
     * Function to run the game portion of the program
     * Advances Steps and allows user to interact with the UI
     * @throws InterruptedException - Handled Elsewhere
     * @throws IOException - Handled Elsewhere
     */
    public static void runGame() throws InterruptedException, IOException {
            long measure = 0;
            while(game) {
                TimeUnit.MILLISECONDS.sleep(50);
                while (frame.tb.running) {
                    measure = System.currentTimeMillis();
                    for (step = 0; step < maxStep; step++) { //steps = 5 atm
                        if (frame.mb.threads == 1) {
                            if (frame.tb.running == false) {
                                while (!frame.tb.running) {   //if game is paused
                                    TimeUnit.MILLISECONDS.sleep(50);
                                    checkStates();
                                    frame.tb.stepDisplay.setText("Step: " + step + " ");
                                    frame.tb.maxstepDisplay.setText("Max Step: " + maxStep);
                                }
                            }
                            gb = nextGeneration(gb, x, y);
                        } else {
                            multiNextGen();
                            //TimeUnit.MILLISECONDS.sleep(100);

                        }
                        frame.tb.stepDisplay.setText("Step: " + step + " ");
                        frame.tb.maxstepDisplay.setText("Max Step: " + maxStep);
                    }
                    frame.tb.running = false;
                    frame.updateGrid(x, y, gb);
                    frame.revalidate();
                    System.out.println("TIME: " + (System.currentTimeMillis() - measure));
                }
                checkStates();
                frame.tb.stepDisplay.setText("Step: " + step + " ");
                frame.tb.maxstepDisplay.setText("Max Step: " + maxStep);
            }
        frame.updateGrid(x, y, gb);
        frame.revalidate();
        frame.tb.running = false;
            }

    /**
     * Function used to see if the user has interacted with the UI at all and changed any states of the UI
     * @throws IOException - Handled Manually
     * @throws InterruptedException - Handled Manually
     */
    public static void checkStates() throws IOException, InterruptedException{
        if(frame.tb.forward){
            if(frame.mb.threads == 1) {
                if (step < maxStep) {
                    System.out.println("Forward a Tick!");
                    gb = nextGeneration(gb, x, y);
                    frame.updateGrid(x, y, gb);
                    frame.revalidate();
                    step++;
                    frame.tb.forward = false;
                } else {
                    System.out.println("Cannot Go Forward!");
                    frame.tb.forward = false;
                }
            }
            else{
                if(step < maxStep) {
                    System.out.println("Forward a Tick!");
                    multiNextGen();
                    Thread.sleep(1000);
                    frame.updateGrid(x, y, gb);
                    frame.revalidate();
                    step++;
                    frame.tb.forward = false;
                }
                else{
                    System.out.println("Cannot Go Forward!");
                    frame.tb.forward = false;
                }
            }
        }
        if(frame.tb.backward == true){ //validate
            if(frame.mb.threads == 1) {
                if (step > 0) {
                    System.out.println("Back a Tick!");
                    gb = ogGB;
                    for (int j = 0; j < step - 1; j++) {
                        gb = nextGeneration(gb, x, y);
                    }
                    frame.updateGrid(x, y, gb);
                    frame.revalidate();
                    step--;
                    frame.tb.backward = false;
                } else {
                    System.out.println("Cannot Go Backward!");
                    frame.tb.backward = false;
                }
            }
            else{
                if(step > 0) {
                    System.out.println("Back a Tick!");
                    for(int i = 0; i < y; i++){
                        for(int j = 0; j < x; j++){
                            gb[i][j] = ogGB[i][j];
                        }
                    }
                    for (int j = 0; j < step - 1; j++) {
                        multiNextGen();
                    }
                    frame.updateGrid(x, y, gb);
                    frame.revalidate();
                    step--;
                    frame.tb.backward = false;
                }
                else{
                    System.out.println("Cannot Go Backward!");
                    frame.tb.backward = false;
                }
            }
        }
        if(frame.tb.desiredStep != -1){
            if(frame.mb.threads == 1) {
                if ((frame.tb.desiredStep > 0) && (frame.tb.desiredStep < maxStep)) {
                    for(int i = 0; i < y; i++){
                        for(int j = 0; j < x; j++){
                            gb[i][j] = ogGB[i][j];
                        }
                    }
                    for (int j = 0; j < step - 1; j++) {
                        gb = nextGeneration(gb, x, y);
                    }
                    frame.updateGrid(x, y, gb);
                    frame.revalidate();
                    step = frame.tb.desiredStep;
                    frame.tb.desiredStep = -1;
                } else {
                    System.out.println("Cannot go to that step!");
                    frame.tb.desiredStep = -1;
                }
            }
            else{
                if((frame.tb.desiredStep > 0) && (frame.tb.desiredStep < maxStep)) {
                    gb = ogGB;
                    for (int j = 0; j < step - 1; j++) {
                        multiNextGen();
                    }
                    frame.updateGrid(x, y, gb);
                    frame.revalidate();
                    step = frame.tb.desiredStep;
                    frame.tb.desiredStep = -1;
                }
                else{
                    System.out.println("Cannot go to that step!");
                    frame.tb.desiredStep = -1;
                }
            }
        }
        if(frame.mb.updateFlag){
            frame.updateGrid(x,y,gb);
            frame.updateBars();
            frame.revalidate();
            frame.mb.updateFlag = false;
        }
        if(frame.mb.updateTime){
            time = frame.mb.tickTime;
            System.out.println("Time per Tick updated to: " + time + " milliseconds!");
            frame.mb.updateTime = false;
        }
        if(frame.mb.updateTicks){
            maxStep = frame.mb.ticks;
            frame.mb.updateTicks = false;
        }
        if(frame.mb.loadedFile){
            initializeGrid(true);
            frame.mb.loadedFile = false;
        }
        if(frame.mb.reset){
            boolean temp = loadChoice();
            initializeGrid(temp);
            frame.revalidate();
            frame.mb.reset = false;
        }
        if(frame.mb.saveGrids){
            saveGrids(false);
            frame.mb.saveGrids = false;
        }
        if(frame.mb.saveGame){
            saveGrids(true);
            frame.mb.saveGame = false;
        }
    }

    /**
     * Function used to calculate the next generation when the user has selected to use more than one thread
     * @throws InterruptedException - Handled Manually
     */
    public static void multiNextGen() throws InterruptedException{

        System.out.println(frame.mb.threads);
        Thread[] t = new Thread[frame.mb.threads];
        ExecutorService pool = Executors.newFixedThreadPool(frame.mb.threads);
        Thread tempT = null;
        int counter = 0;

        if((frame.mb.threads > x )|| (frame.mb.threads > y )){
            int temp = Math.min(x,y);
            int newT = 0;
            if(temp > 8){
                newT = 8;
            }
            else if(temp > 4){
                newT = 4;
            }
            else if(temp > 2){
                newT = 2;
            }
            else{
                newT = 1;
            }
            System.out.println("Too Many Threads for size of board! Setting Threads to : " + newT);
            frame.mb.threads = newT;
            System.out.println(frame.mb.threads);
        }

        int divX = 1;
        int divY = 1;


        for(int i = 0; i < frame.mb.threads; i += 2){
            if((i == 0) || (i == 4) || (i == 8) || (i == 10)){
                divY += 1;
            }
            if ( (i == 2) || (i == 10) || (i == 12) || (i == 8)){
                divX += 1;
            }
        }


        byte[][] a = new byte[(int) y/divY][(x/divX)];
        int counterX = 0;
        int counterY = 0;

        for(int k = 0; k < frame.mb.threads; k++) {
           System.out.println("K: " + k + " DIVX: " + divX + " divY: " +divY);
           /* if(((x%divX) == 0) && ((y%divY) == 0)) {
                System.out.println("Hi");
                for (int i = k * divY; i < divY * (k + 1); i++) {
                    for (int j = k * divX; j < divX * (k + 1); j++) {
                        a[i - k * divY][j - k * divX] = gb[i][j];
                    }
                }
                new runThread(Integer.toString(k), a, a[0].length, a.length, frame, step, maxStep, , x/divX);
            }*/
            //else{
                a = new byte[y/divY][(x/divX)];
                if(divY%2 == 1) {
                    if(frame.mb.threads == 8) {
                        if (counterY != divY) {
                            for (int i = counterY * (y / divY); i < (y / divY) * (counterY + 1); i++) {
                                for (int j = counterX * (x / divX); j < (x / divX) * (counterX + 1); j++) {
                                    a[i - (counterY * (y / divY))][j - (counterX * (x / divX))] = gb[i][j];

                                }
                            }
                            tempT = new Thread(new runThread(Integer.toString(k), a, a[0].length, a.length, frame, step, maxStep, (counterY * (y / divY)), counterX * (x - (x / divX))));
                            counterY++;
                        } else {
                            a = new byte[y - counterY * (y / divY)][x / divX];
                            int ax = (x - counterX * (x / divX));
                            int b = (y - counterY * (y / divY));
                            for (int i = counterY * (y / divY); i < y; i++) {
                                for (int j = counterX * (x / divX); j < (x / divX) * (counterX + 1); j++) {
                                    a[i - (counterY * (y / divY))][j - (counterX * (x / divX))] = gb[i][j];
                                }
                            }
                            tempT = new Thread(new runThread(Integer.toString(k), a, a[0].length, a.length, frame, step, maxStep, (counterY * (y / divY)), counterX * (x - (x / divX))));
                            counterY = 0;
                            counterX++;
                        }
                    }
                    else{
                        if (counterY != divY - 1) {
                            for (int i = counterY * (y / divY); i < (y / divY) * (counterY + 1); i++) {
                                for (int j = counterX * (x / divX); j < (x / divX) * (counterX + 1); j++) {
                                    a[i - (counterY * (y / divY))][j - (counterX * (x / divX))] = gb[i][j];

                                }
                            }
                            tempT = new Thread(new runThread(Integer.toString(k), a, a[0].length, a.length, frame, step, maxStep, (counterY * (y / divY)), counterX * ((x / divX))));
                            counterY++;
                        } else {
                            a = new byte[y - counterY * (y / divY)][x / divX];
                            int ax = (x - counterX * (x / divX));
                            int b = (y - counterY * (y / divY));
                            for (int i = counterY * (y / divY); i < y; i++) {
                                for (int j = counterX * (x / divX); j < (x / divX) * (counterX + 1); j++) {
                                    a[i - (counterY * (y / divY))][j - (counterX * (x / divX))] = gb[i][j];
                                }
                            }
                            tempT = new Thread(new runThread(Integer.toString(k), a, a[0].length, a.length, frame, step, maxStep, (counterY * (y / divY)), counterX * ((x / divX))));
                            counterY = 0;
                            counterX++;
                        }
                    }
                }
                else{
                    if (counterY != divY - 1) {
                        for (int i = counterY * (y / divY); i < (y / divY) * (counterY + 1); i++) {
                            for (int j = counterX * (x / divX); j < (x / divX) * (counterX + 1); j++) {
                                a[i - (counterY * (y / divY))][j - (counterX * (x / divX))] = gb[i][j];

                            }
                        }
                        tempT = new Thread(new runThread(Integer.toString(k), a, a[0].length, a.length, frame, step, maxStep, (counterY * (y / divY)), counterX * (x - (x / divX))));
                        counterY++;
                    } else {
                        a = new byte[y - counterY * (y / divY)][x / divX];
                        int ax = (x - counterX * (x / divX));
                        int b = (y - counterY * (y / divY));
                        for (int i = counterY * (y / divY); i < y; i++) {
                            for (int j = counterX * (x / divX); j < (x / divX) * (counterX + 1); j++) {
                                a[i - (counterY * (y / divY))][j - (counterX * (x / divX))] = gb[i][j];
                            }
                        }
                        tempT = new Thread(new runThread(Integer.toString(k), a, a[0].length, a.length, frame, step, maxStep, (counterY * (y / divY)), counterX * (x - (x / divX))));
                        counterY = 0;
                        counterX++;
                    }
                }
                tempT.join();
        }
    }

    /**
     * Function used to save the grid/grid based on the users input
     * @param b - Boolean meaning if the user wants to save current Grid or all Grids
     */
    public static void saveGrids(boolean b){
        if(!b){
            System.out.println("Saved Tick: " + step + "!");
            FileWriter fw = null;
            try {
                fw = new FileWriter(menuBar.outFile + step);
                fw.write(x + ", " + y + ", " + step);
                fw.write("\n");
                for(int i = 0; i < y; i++){
                    for(int j = 0; j < x; j++){
                        fw.write(gb[i][j] + ", ");
                    }
                    fw.write("\n");
                }
                for(int i = 0; i < y; i++){
                    for(int j = 0; j < x; j++){
                        fw.write(ogGB[i][j] + ", ");
                    }
                    fw.write("\n");
                }
                fw.close();
            } catch (IOException e) {

            }
        }
        else{
            for(int k = step; k < maxStep; k++){
                System.out.println("Saved Tick: " + k + "!");
                FileWriter fw = null;
                try {
                    fw = new FileWriter(menuBar.outFile + k);
                    fw.write(x + ", " + y + ", " + k);
                    fw.write("\n");
                    for(int i = 0; i < y; i++){
                        for(int j = 0; j < x; j++){
                            fw.write(gb[i][j] + ", ");
                        }
                        fw.write("\n");
                    }
                    for(int i = 0; i < y; i++){
                        for(int j = 0; j < x; j++){
                            fw.write(ogGB[i][j] + ", ");
                        }
                        fw.write("\n");
                    }
                    fw.close();
                } catch (IOException e) {

                }
                gb = nextGeneration(gb,x,y);
            }
        }
        gb = ogGB;
    }

    /**
     * readPrefs sets all current settings to previously saved settings
     * @throws IOException -  Exception is handled manually
     */
    public static void readPrefs() throws IOException {
        BufferedReader bw = null;
        try {
            bw = new BufferedReader(new FileReader("myPreferences.txt"));
            //Setting up current preferences
            menuBar.colorAlive = findColor(bw.readLine());
            menuBar.colorDead = findColor(bw.readLine());
            menuBar.backColor = findColor(bw.readLine());
            menuBar.fontColor = findColor(bw.readLine());
            menuBar.defX = Integer.parseInt(bw.readLine());
            menuBar.defY = Integer.parseInt(bw.readLine());
            menuBar.outFile = bw.readLine();
            menuBar.l = bw.readLine();
            menuBar.s = bw.readLine();
            menuBar.tickTime = Integer.parseInt(bw.readLine());
            menuBar.ticks = Integer.parseInt(bw.readLine());
            menuBar.threads = Integer.parseInt(bw.readLine());
            menuBar.updateFlag = true;
            menuBar.updateTime = true;
            menuBar.updateTicks = true;
            try{
                checkStates();
            }
            catch(InterruptedException e){
                //
            }
            System.out.println("Preferences File Loaded!");
        } catch (FileNotFoundException e) {
            System.out.println("No Preferences File! Using Default Settings!");
            return;
        }
    }

    /**
     * Parses string data from preference file to form a Color Object
     * @param line String that is being parsed
     * @return Color Object
     */

    public static Color findColor(String line) {
        if(line.startsWith("java")) { //Parsing through split string array to extract R G B int values
            String[] a = new String[4];
            a = line.split("=");
            String r = "";
            String g = "";
            String b = "";
            for(int i = 0; i < a.length; i++) {
                for( char x : a[i].toCharArray()) {
                    if(i != 0) {
                        if (Character.isDigit(x)) {
                            if(i == 1) {
                                r += x;
                            }
                            else if(i == 2) {
                                g += x;
                            }
                            else {
                                b += x;
                            }
                        }
                    }
                }
            }
            return(new Color(Integer.parseInt(r), Integer.parseInt(g), Integer.parseInt(b))); //creating new Color object and returning it
        }
        else {
            return null;
        }
    }
}
