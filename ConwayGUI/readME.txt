Jacob Ganz 
661817611

This program was written on Ubuntu and should be run on Ubuntu.

Running Main Class of this package gives the user an Interface in which to interact with the program. Here are the UI features that I would like to describe to you:
	-Load: Within the file MenuBar item. Allows the user to load a .txt file with the same formatting as in HW1
	-New:  Within the file MenuBar item. Creates a new Grid using the set default variables, and displays this to the User Interface
	-Save Preferences: Within the file MenuBar item. Saves the user's current set preferences to a hard coded .txt file called "myPreferences.txt"
	-Save Current Game Through Max Step: Within the file MenuBar item. Saves all grids from current step to final step in outfiles
	-Save Current Grid as .txt: Within the file MenuBar item. Self-Explanatory
	-The Settings Menu includes several different customizable settings, as well a reset button to set all settings back to their defaults
	-Hotkey Buttons are self-explanatory
	-In order for the textfields to actually change anything, one must first completely delete the text in the box and enter a valid integer, otherwise this will not change anything.
	

In order to run this program, one must extract from the zip file to their desktop and open the terminal. From there they must enter four commands:
cd Desktop
cd HW02_JacobGanz
sh ConwayGUI_Run.sh

The user will not be prompted to put in any inputs unless they select this.

After closing the application, if the user has a saved myPreferences.txt file in the file directory (where it will automatically be saved) these settings will load on start up.

In order for the user to actually understand and see what is going on, Forward, Backward, and specified step actions are suppressed unless the game status is paused.

An example of an input file would look similar to below: This can be from an input text file, or simply clicked on by the user. This would also be an output for Save Current Grid
4, 4
1, 0, 0, 1
1, 0, 0, 1
0, 1, 0, 1
0, 1, 0, 0

An example of an output file would look similar to below: This is what would output when Save Current Game is selected
0, 1, 0, 0
0, 1, 0, 1
1, 1, 1, 0
0, 0, 1, 1

Attached in this folder are some sample inputs and outputs.
This ConwayGUI_Docs folder holds all html javadoc files.