import java.util.*;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.text.*;

/**
* The RiemannSum program allows a user to select 6 functions and calculate their Riemann Sum in 4 different ways
* @author Jacob Ganz
* @version 05/30/2019
*/
public class RiemannSum{

	/**
	* The main method is where the user is allowed to enter inputs to select from the given options. These inputs are used to calcualate the sum based on the inputs
	* A large decision statement below takes care of all of the different options the user can select and then uses the limit to calculate the sum
	* @param args - String Array containing command line arguments
	*/
	public static void main(String[] args)	{
		
		Scanner input = new Scanner(System.in); //Initializing variables used for I/O
		int function = 0;
		int sumMethod = 0;
		float lowerLimit = -1;
		float upperLimit = 0;
		float intervals = 0;
		int precision = 0;

		System.out.println("Please select one of the six functions below by entering the integer next to the function."); //Function Options for user to choose from
		System.out.println("1) f(x) = x * x");
		System.out.println("2) f(x) = ln(x)");
		System.out.println("3) f(x) = sin(x)");
		System.out.println("4) f(x) = x * x * x");
		System.out.println("5) f(x) = cos(x)");
		System.out.println("6) f(x) = x ^ (1/2)");
		boolean verify = true;

		while(verify){ //while loop to ensure that the user input is valid
			function = input.nextInt();
			if(function <= 6 && function >= 1){
				verify = false;
			}
			else{
				System.out.println("Invalid Input: Please enter an integer between 1 and 6.");
			}
		}

		System.out.println("Please select one of the four ways to calculate the Riemann Sum by entering the integer next to an option."); //Riemann Sum options for the user
		System.out.println("1) Left-Hand Riemann Sum");
		System.out.println("2) Right-Hand Riemann Sum");
		System.out.println("3) Mid-Point Riemann Sum");
		System.out.println("4) Trapezoid Riemann Sum");

		while(!verify){ //while loop to ensure teh user input is valid
			sumMethod = input.nextInt();
			if(sumMethod <= 4 && sumMethod >= 1){
				verify = true;
			}
			else{
				System.out.println("Invalid Input: Please enter a new integer between 1 and 4.");
			}
		}

		System.out.println("Please enter the lower end point for area calculation.");
		lowerLimit = input.nextFloat();
		if(function == 2){
			while(lowerLimit < 0){ //while loop ensuring that the lower limit is positive in the ln function is selected, will not work if negative
				System.out.println("Lower Limit must be greater than or equal to zero for the ln(x) function. Please enter a new integer.");
				lowerLimit = input.nextFloat();
			}
		}

		System.out.println("Please enter the upper end point for area calculation.");
		while(verify){ //while loop ensuring that the upper limit entered is larger than the lower limit 
			upperLimit = input.nextFloat();
			if(upperLimit > lowerLimit){
				verify = false;
			}
			else{
				System.out.println("Invalid Input: Upper Limit must be greater than Lower Limit. Please enter a new integer.");

			}
		}

		System.out.println("Please enter the number of intervals you would like for the calculation.");
		while(!verify){ //while loop ensuring the interval entered is positive
			intervals = input.nextFloat();
			if(intervals > 0){
				verify = true;
			}
			else{
				System.out.println("Invalid Input: There must be atleast 1 interval. Please enter a new integer.");
			}
		}

		System.out.println("Please enter the number of digits after the decimal you would like in your calculation.");
		while(verify){ //ensuring the precision entered is atleast 1 decimal place
			precision = input.nextInt();
			if(precision > 0){
				verify = false;
			}
			else{
				System.out.println("Invalid Input: The preciison must be atleast 1. Please enter a new integer.");
			}
		}

		float sum = 0;
		float increment = (upperLimit - lowerLimit) / intervals; //the increment for each step based off the limits and intervals

		switch (function){
			case 1: //Function 1 was selected
				switch (sumMethod){
					case 1: //Left-Hand was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += (i * i);
						}
						break;
					case 2: //Right-Hand was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += ((i + increment) * (i + increment));
						}
						break;
					case 3: //Mid-Point was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += (((i + (i + increment))/2) * ((i + (i + increment))/2)); 
						}
						break;
					case 4: //Trapezoid was selected
						for(float i = lowerLimit + increment; i < upperLimit; i += increment){
							sum += 2*(i * i);
						}
						sum += (lowerLimit * lowerLimit);
						sum += (upperLimit * upperLimit);
						sum = sum/2;
						break;
				}
				break;
			case 2: //Function 2 was selected
				switch (sumMethod){
					case 1: //Left-Hand was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += Math.log(i);
						}
						break;
					case 2: //Right-Hand was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += (Math.log(i + increment));
						}
						break;
					case 3: //Mid-Point was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += (Math.log((i + (i + increment))/2)); 
						}
						break;
					case 4: //Trapezoid was selected
						for(float i = lowerLimit + increment; i < upperLimit; i += increment){
							sum += 2*Math.log(i);
						}
						sum += Math.log(lowerLimit);
						sum += Math.log(upperLimit);
						sum = sum/2;
						break;
				}
				break;
			case 3: //Function 3 was selected
				switch (sumMethod){
					case 1: //Left-Hand was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += Math.sin(i);
						}
						break;
					case 2: //Right-Hand was selected
						for(float i = lowerLimit + 1; i < upperLimit; i += increment){
							sum += Math.sin(i + increment);
						}
						break;
					case 3: //Mid-Point was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += Math.sin((i + (i + increment))/2); 
						}
						break;
					case 4: //Trapezoid was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += 2*Math.sin(i);
						}
						sum += Math.sin(lowerLimit);
						sum += Math.sin(upperLimit);
						sum = sum/2;
						break;
				}
				break;
			case 4: //Function 4 was selected
				switch (sumMethod){
					case 1: //Left-Hand was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += (i * i * i);
						}
						break;
					case 2: //Right-Hand was selected
						for(float i = lowerLimit + 1; i < upperLimit; i += increment){
							sum += (i + increment * i + increment * i + increment);	
						}
						break;
					case 3: //Mid-Point was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							float temp = (i + (i + increment))/2;
							sum += temp * temp * temp;
						}
						break;
					case 4: //Trapezoid was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += 2*(i * i * i);
						}
						sum += (lowerLimit * lowerLimit * lowerLimit);
						sum += (upperLimit * upperLimit * upperLimit);
						sum = sum / 2;
						break;
				}
				break;
			case 5: //Function 5 was selected
				switch (sumMethod){
					case 1: //Left-Hand was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += Math.cos(i);
						}
						break;
					case 2: //Right-Hand was selected
						for(float i = lowerLimit + 1; i < upperLimit; i += increment){
							sum += Math.cos(i + increment);
						}
						break;
					case 3: //Mid-Point was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += Math.cos((i + (i + increment))/2); 
						}
						break;
					case 4: //Trapezoid was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += 2*Math.cos(i);
						}
						sum += Math.cos(lowerLimit);
						sum += Math.cos(upperLimit);
						sum = sum / 2;
						break;
				}
				break;
			case 6: //Function 6 was selected
				switch (sumMethod){
					case 1: //Left-Hand was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += Math.sqrt(i);
						}
						break;
					case 2: //Right-Hand was selected
						for(float i = lowerLimit + 1; i < upperLimit; i += increment){
							sum += Math.sqrt(i + increment);
						}
						break;
					case 3: //Mid-Point was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += Math.sqrt((i + (i + increment))/2); 
						}
						break;
					case 4: //Trapezoid was selected
						for(float i = lowerLimit; i < upperLimit; i += increment){
							sum += 2*(Math.sqrt(i));
						}
						sum += Math.sqrt(lowerLimit);
						sum += Math.sqrt(upperLimit);
						sum = sum/2;
						break;
				}
				break;
		}
		sum = sum * increment;
		String error = "#.";
		for(int i = 0; i < precision; i++){ //creating a string to format decimal
			error += "0";
		}
		DecimalFormat df = new DecimalFormat(error);
		df.setRoundingMode(RoundingMode.CEILING);
		System.out.println("Riemann Sum Result: " + df.format(sum)); 

	}
}