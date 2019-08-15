# javawork
Some interesting Java projects I made for a class, Application Programming Using Java.

All Folders include a readMe with instructions on how the programs work.

Here is a short description of each program.

Riemann Sum - Simple Program that will prompt the user to enter a range and select one of six functions, 
               and which type of Riemann Sum the user would like to use. The program will then print out
               the result. (Java)
               
Conway Game - Simple implementation of Conway's Game of Life. Allows users to choose an input file, an 
              output file template, and the number of steps the user would like to run. The program
              then runs the algorithm written to compute each step and write the state of all cells
              at each step to a .txt file.
              
Conway GUI - Another implementation on Conway's Game of Life, however this one features a fully implemented
             GUI. This allows users to save files and settings, customize their UI, upload or save matrix
             files, create their own matrix files by clicking the UI, and fully interact with the Game rather
             than just click run. This means the user can start, pause, move forward, backward, go to a specific
             step, etc. This is all laid out in the project's readme.
             
Conway Multi - The final implementation of Conway's Game of Life. Most aspects of the UI are similar to Conway GUI,
               but a toolbar is implemented to clean up the UI. The highlight of this project is that the program
               can handle matrices of 50,000 by 50,000 cells, or 2.5 BILLION cells. That is, if your computer can 
               allocate enough RAM to java to run that. The program then will run through the given steps, and update
               the UI at the final step, of course your computer screen cannot show 2.5 Billion cells, so if the 
               matrix is larger than the number of pixels on the screen, the matrix is split into groups of 4, 9, 16
               , etc cells in order to reduce the matrix enough to show the result. I have tested this program to run 16
               concurrent threads computing the resultant matrices.
               
Battleship - This is an implementation of the game Battleship, again it has a fully-interactive UI, meaning all the UI
             settings can be adjusted, and saved. The game allows two users with the program to connect to each other
             using a ServerSocket connection. The user's then are prompted to enter their names and place their ships.
             After that it's just like Battleship! Users have 30 seconds (adjustable via menu) to take their shot. The
             number of ships and length of each ship is also customizable. The Winner and Loser are notified at the end 
             of their game and the are asked if they would like to play again. The program keeps track of the number of games 
             each player has won. A feature I am particularly proud of is the implementation of the ship placement. There 
             is an algorithm to find the first open spot on the board, and from the there the user can move the ships in any
             direction and also rotate the ships. The server communication works flawlessly as well allowing all the data 
             needed to run the game to be sent/received between both players.
             
MarketBook - This is Java EE 8 Web Application that utilizes Apache Tomcat, and mySQL to allow users to query a database
             on the website server. The database contains all farmer's markets in America. The app allows users to search farmer's
             markets by ZIP, and City/State, and the radius of their search. This will bring them to a search webpage of a
             table of all results, it allows users to click on each market's name to view all more information about each 
             market. 
              
