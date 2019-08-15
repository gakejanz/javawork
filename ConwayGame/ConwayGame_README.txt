Jacob Ganz 
661817611

This program was writtenon Ubuntu and should be run on Ubuntu.

ConwayGame allows the user to input an input text file, an output file template, and the number of steps the user would like to run. The progam takes an input matrix of a pre determined size and proceeds to the next generation, in which each cell of the matrix is determined to be alive or dead based on the 4 given rules described by Professor. All of the user inputs are validated to ensure the program does not break.

In order to run this program, one must extract from the zip file to their desktop and open the terminal. From there they must enter four commands:
cd Desktop
cd HW01_JacobGanz
cd ConwayGame
sh ConwayGameRun.sh

This will run the program in the terminal.

The user will be prompted to input some values, and then the program will print the results to output files.

An example of an input file would look similar to below:
4, 4
1, 0, 0, 1
1, 0, 0, 1
0, 1, 0, 1
0, 1, 0, 0

An example of an output file would look similar to below:
0, 1, 0, 0
0, 1, 0, 1
1, 1, 1, 0
0, 0, 1, 1

Attached in this ConwayGame folder are some sample input and output .txt files.
The ConwayGameDocs Folder contains all html javadoc files.


