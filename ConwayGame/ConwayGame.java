import java.util.*;
import java.io.*;

/**
* The ConwayGame program implements a variation of the Conway Game of Life that proceeds through steps deciding which cells live and die in the next generation
* @author Jacob Ganz
* @version 05/30/2019
*/

public class ConwayGame{
	/**
	* The main method - This is where user inputs are taken and validated to be used in other functions
	* @param args - A string array containing command line arguments
	* @throws IOException - IOException is checked manually via try/catch
	* @throws FileNotFoundException - FileNotFoundException is checked manually via try/catch
	*/
	public static void main(String[] args) throws IOException, FileNotFoundException{

		Scanner input = new Scanner(System.in);
		boolean bError = true;
		boolean stringOverride = true;
		boolean flag = false;
		String inputFile = "";
		String outFile = "";
		int steps = 0;
		/*
			Gathering necessary inputs from user to run program
		*/
		System.out.println("Please enter the initial seed file name (exclude .txt) => ");
		inputFile = input.nextLine() + ".txt";
		System.out.println("Please enter the output file pattern name (exclude .txt) => ");
		outFile = input.nextLine();
		System.out.println("Please enter how many steps the program shall operate for as an integer =>");
		while(!flag){	
			try{	//A try/catch block checking the number input from the user is a integer, covers IOException
				steps = input.nextInt();
				flag = true;
			}
			catch (InputMismatchException ex){
				System.out.println("Invalid Input. Please enter an integer step amount.");
				input.next();
			}
			if(steps <= 0){ //ensuring input is positive
				System.out.println("Invalid Input. Please enter a positive integer.");
				flag = false;
			}
		}

		/*
			Parsing Data from input file to access necessary information to run the program
		*/
		stringOverride = false;
		while(!stringOverride){
			try{ //A try block that tests the input file can be opened, to ensure valid input				
				BufferedReader br = new BufferedReader(new FileReader(inputFile));
				stringOverride = true;
			}
			catch (IOException e){ //if file cannot be opened user must enter a new input file name
				stringOverride = false;
				System.out.println("Not the name of an .txt file. Please enter a new file input name.");
				inputFile = input.nextLine() + ".txt";
			}
		}
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String reader = br.readLine();
		String[] readerSplit = reader.split(", "); //reading first line to gather dimensions of game board
		int xDist = Integer.parseInt(readerSplit[1]);
		int yDist = Integer.parseInt(readerSplit[0]);

		int[][] gameBoard = new int[xDist][yDist]; //creating game board

		for(int i = 0; i < yDist; i++){ //parsing input file lines 
			reader = br.readLine();
			readerSplit = reader.split(", ");

			for(int j = 0; j < xDist; j++){
				gameBoard[j][i] = Integer.parseInt(readerSplit[j]); //filling in game board with input file data
			}
		}

		printOut(gameBoard,0,outFile,xDist,yDist); //printing out first generation

		for(int i = 1; i <= steps; i++){ //for the steps entered
			gameBoard = NextGeneration(gameBoard, xDist, yDist); //next generation is found and returned
			printOut(gameBoard, i, outFile, xDist, yDist); //next generation is printed out
		}

	}

	/**
	 * Function in which the game 'ticks', i.e. the game proceeds to the next generation and returns it to be put in a output file by the main
	 * Loops through each cell in the matrix and determines whether it's position needs to be wrapped to check all neighbors
	 * Determines cells state for next generation based off livecount of its neighbors and the cells current status
	 * Each cells state is updated by being placed in a temp matrix that is returned to the main 
	 * @return This is a 2-Dimensional integer array representing the next generation's game board
	 * @param gB This is a 2-Dimensional integer array representing the current current game board
	 * @param x This is an integer representing the x dimension of the game board
	 * @param y This is an integer representing the y dimension of the game board
	 *  
	*/
	public static int[][] NextGeneration(int[][] gB, int x, int y){
		int[][] tempGB = new int[x][y];
		boolean xCheck = false;
		boolean yCheck = false;
		boolean alive = true;
		int liveCount = 0;

		for (int i = 0; i < y; i++){
			for(int j = 0; j < x; j++){

				if(gB[j][i] == 1){ //determining if current cell is alive or dead
					alive = true;
				}
				else{
					alive = false;
				}

				if((i == 0) || (i == y - 1)){ //if the y dimension must be wrapped
					yCheck = true;
				}
				else{
					yCheck = false;
				}
				if((j == 0) || (j == x - 1)){ //if the x dimension must be wrapped
					xCheck = true;
				}
				else{
					xCheck = false;
				}

				if(xCheck && yCheck){ //if both dimensions must be wrapped  
					if(j == 0){ //finding conditions of the x and y check
						if(i == 0){ //if checking upper left corner
							for(int n = 0; n < 2; n++){
								for(int m = 0; m < 2; m++){
									if(gB[j+n][i+m] == 1){ //if current neighbor alive
										liveCount++;
									}
								}
							}
							for(int n = 0; n < 2; n++){ //checking wrapped neighbors
								if(gB[j+n][y - 1] == 1){
									liveCount++;
								}
								if(gB[x-1][i+n] == 1){
									liveCount++;
								}
							}
							if(gB[x-1][y-1] == 1){ //checking final wrapped neighbor
								liveCount++;
							}
						}
						else{ //if checking lower left corner
							for(int n = 0; n < 2; n++){
								for(int m = 0; m < 2; m++){
									if(gB[j+n][i-m] == 1){
										liveCount++;
									}
								}
							}
							for(int n = 0; n < 2; n++){ //checking wrapped neighbors
								if(gB[j + n][0] == 1){
									liveCount++;
								}
								if(gB[x - 1][i - n] == 1){
									liveCount++;
								}
							}
							if(gB[x - 1][0] == 1){ //checking final wrapped neighbors
								liveCount++;
							}
						}
					}
					else{
						if(i == 0){ //checking upper right corner
							for(int n = -1; n < 1; n++){
								for(int m = 0; m < 2; m++){
									if(gB[j+n][i+m] == 1){
										liveCount++;
									}
								}
							}
							for(int n = 0; n < 2; n++){ //checking wrapped neighbors
								if(gB[0][i+n] == 1){
									liveCount++;
								}
								if(gB[j-n][y - 1] == 1){
									liveCount++;
								}
							}
							if(gB[0][y - 1] == 1){ //checking final wrapped neighbors
								liveCount++;
							}
						}
						else{ //checking lower right corner
							for(int n = -1; n < 1; n++){
								for(int m = -1; m < 1; m++){
									if(gB[j+n][i+m] == 1){
										liveCount++;
									}
								}
							}
							for(int n = -1; n < 1; n++){ //checking wrapped neighbors
								if(gB[0][i+n] == 1){
									liveCount++;
								}
								if(gB[j+n][0] == 1){
									liveCount++;
								}
							}
							if(gB[0][0] == 1){ //checking final wrapped neighbors
								liveCount++;
							}
						}
					}
				}
				else if (xCheck) { //only the x dimension must be wrapped
					if(j == 0){ //finding the condition of the xCheck
						for(int n = -1; n < 2; n++){
							for(int m = 0; m < 2; m++){
								if(gB[j+m][i+n] == 1){ //current neighbor is alive
									liveCount++;
								}
							}
							if(gB[x - 1][i + n] == 1){ //if wrapped neighbor is alive
								liveCount++;
							}
						}
					}
					else{
						for(int n = -1; n < 2; n++){
							for(int m = -1; m < 1; m++){
								if(gB[j+m][i+n] == 1){ //current neighbor is alive
									liveCount++;
								}
							}
							if(gB[0][i + n] == 1){ //if wrapped neighbor is alive
								liveCount++;
							}
						}
					}
				}
				else if (yCheck) { //only the y dimension must be wrapped
					if(i == 0){ //finding the condition of the yCheck
						for(int n = -1; n < 2; n++){
							for(int m = 0; m < 2; m++){
								if(gB[j+n][i+m] == 1){ //current neighbor is alive
									liveCount++;
								}
							}
							if(gB[j+n][y - 1] == 1){ //if wrapped neighbor is alive
								liveCount++;
							}
						}
					}
					else{
						for(int n = -1; n < 2; n++){
							for(int m = -1; m < 1; m++){
								if(gB[j+n][i+m] == 1){ //current neighbor is alive
									liveCount++;
								}
							}
							if(gB[j+n][0] == 1){ //if wrapped neighbor is alive
								liveCount++;
							}
						}
					}
				}
				else{ //neither dimension has to be wrapped
					for(int n = -1; n < 2; n++){ //looping through neigboring cells
						for(int m = -1; m < 2; m++){
							if(gB[j+n][i+m] == 1){ //current neighbor is alive
								liveCount++;
							}
						}
					}
				}
				if(alive){ //if current cell is alive
						liveCount--; //accounting for current cell be counted as alive
						if(liveCount < 2){ //if current cell dies of underpopulation
							tempGB[j][i] = 0;
						}
						else if ((liveCount == 2) || (liveCount == 3)) { //if current cell lives to next generation
							tempGB[j][i] = 1;
						}
						else{ //if current cell dies due to overpopulation
							tempGB[j][i] = 0;	
						}
				}
				else{
						if(liveCount == 3){ //the cell is birthed in the next generation by reproduction
							tempGB[j][i] = 1;
						}
				}
				liveCount = 0;
			}
		}
		return tempGB;
	}

	/**
	* Function that takes in the current gameBoard and step and prints it out to a output file based on the step
	* @param gB This is a 2-Dimensional integer array representing the current game board
	* @param s This is an integer representing the current step or 'tick' the game is on
	* @param out This is a string representing the output file template
	* @param x This is an integer representing the x dimesion of the game board
	* @param y This is an integer representing the y dimension of the game board
	* @throws IOException This is checked manually via a try/catch block
	*/
	public static void printOut(int[][] gB, int s, String out, int x, int y) throws IOException{ 
		/*
			Function in which the current gameBoard is printed to an outFile, whose name is determined by the step the game is on.
		*/
			Scanner input = new Scanner(System.in);
			String stepOut = out + Integer.toString(s) +".txt"; //creating output file name based on step 
			boolean writable = false;
			
			while(!writable){
				try{
					BufferedWriter writer = new BufferedWriter(new FileWriter(stepOut));
					writable = true;
				}
				catch (IOException e){
					System.out.println("Unable to write with output file template. Please enter a new output file template.");
					out = input.nextLine();
				}
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(stepOut));
			for(int i = 0; i < y - 1; i++){	//Looping through game board printing out matrix
				for(int j = 0; j < x - 1; j++){
					writer.write(Integer.toString(gB[j][i])+ ", ");
				}
				writer.write(Integer.toString(gB[x-1][i]) + "\n");
			}
			for(int i = 0; i < x - 1; i ++){
				writer.write(Integer.toString(gB[i][y-1]) + ", ");
			}
			writer.write(Integer.toString(gB[x-1][y-1]));

			writer.close();
	}
}