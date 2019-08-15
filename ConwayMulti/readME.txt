Jacob Ganz 661817611

This program was written on windows and should be ran on windows.

This program will run Conway Game of Life Simulations using a choice of 1,2,4,8, or 16 threads, based on the users selection.
Any Preferences the user will like to carry over to the next time the application is open can be saved using the save preferences button.
The user interface for the most part is fully customizable.

The multithreading for the most part works correctly. It works perfectly if a sleep of around 1 second is thrown in between the creation
of each thread but that would butcher the execution time. However, it still works most the of the time but when a lot of threads are
thrown in it can get messy and the synchronization of the threads does not execute as intended.

All of the test cases I had used for this homework were randomly generated from my program and I did this countless times for each
threading amount as well as ticks and other variables. Please understand this is why I do not include a test cases file.

In order to run this program simply extract the zip file to the desktop
Open up the Homework04 Folder and Double click the batch file in the scripts folder.

