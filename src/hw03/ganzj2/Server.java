package hw03.ganzj2;

import java.net.*;
import java.io.*;

public class Server {
	
	private static Socket socket = null;
	private ServerSocket server = null;
	private static DataInputStream input = null;
	private static DataOutputStream output = null;
	public static boolean exit = false;
	
	/**
	 * Function used to create Server
	 * @param port - Integer for the port used to create the server
	 * @throws IOException - Exception handled elsewhere
	 */
	public Server(int port) throws IOException {
			try {
				server = new ServerSocket(port);
			}
			catch (IOException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("Server Created");
			
			socket = server.accept();

			input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			output = new DataOutputStream(socket.getOutputStream());
			
			if(exit == true) {
				socket.close();
				input.close();
				server.close();
			}
	}
	
	public static void sendShot(int[] s) throws IOException {
		String message = Integer.toString(s[0]) + "," + Integer.toString(s[1]);
		output = new DataOutputStream(socket.getOutputStream());
		try {
			output.writeUTF(message);
		} catch (IOException e) {
		}
		
	}
	
	/**
	 * Function used to receive the shot data from the client
	 * @return - Integer Array returning the shot coordinates
	 */
	public static int[] receiveShot() {
		String message = "";
		try {
			message = input.readUTF();
		} catch (IOException e) {
		}
		if(message != "") {
			int [] temp = new int[2];
			String[] msg = message.split(",");
			temp[0] = Integer.parseInt(msg[0]);
			temp[1] = Integer.parseInt(msg[1]);
			return temp;
		}
		else {
			int[] temp = new int[2];
			temp[0] = -1;
			temp[1] = -1;
			return temp;
		}
	}
	
	/**
	 * Function used to receive shot results from the client
	 * @return Integer containing the result
	 */
	public static int receiveResult() {
		String message = "";
		try {
			message = input.readUTF();
		} catch (IOException e) {
		}
		if(message == null) {
			return -1;
		}
		return Integer.parseInt(message);
	}
	
	/**
	 * Function used to send the result of a shot to the client
	 * @param r - Integer containing the result for the main
	 */
	public static void sendResult(int r) {
		String temp = Integer.toString(r);
		try {
			output.writeUTF(temp);
		} catch (IOException e) {
		}
	}
	
	/**
	 * Function used to send the same of the Host player to the client
	 * @param n - String name of the host player
	 */
	public static void sendName(String n) {
		 try {
			 output.writeUTF(n);
		} catch (IOException e) {
		}
	}
	
	/**
	 * Function used to receive the name of the client player from the client
	 * @return String of the client player's name
	 */
	public static String receiveName() {
		String temp = "";
		while(temp == "") {
			try {
				temp = input.readUTF();
			} catch (IOException e) {
			}
		}
		return temp;
	}
	
	/**
	 * Function used to send start data from host to client
	 * @param c - Boolean connect value from host
	 * @param s - Boolean start value from host
	 * @throws IOException - handled manually
	 */
	public static void sendStart(boolean c, boolean s) throws IOException {
		if(c == true && s == true) {
			output.writeUTF("1,1");
		}
		else if(c == true && s == false) {
			output.writeUTF("1,0");
		}
		else if(c == false && s == true) {
			output.writeUTF("0,1");
		}
		else {
			output.writeUTF("0,0");
		}
	}
	
	/**
	 * Function used to send done placing ships status to client
	 * @param s - String check value
	 * @throws IOException - handled manually
	 */
	public static void sendCheck(String s) throws IOException {
		output.writeUTF(s);
	}
	
	/**
	 * Function used to receive check from the client
	 * @return - Boolean containing check value
	 * @throws IOException - handled manually
	 */
	public static boolean receiveCheck() throws IOException {
		String temp = "";
		while(temp == "") {
			try {
				temp = input.readUTF();
			} 
			catch (IOException e) {
			}
		}
		if(temp != "") {
			return true;
		}
		return false;
	}
	
	/**
	 * Function for sending preference values from host to client
	 * @param a - Boolean containing value whether or not there is a preference file from the host
	 * @throws IOException - handled manually
	 */
	public static void sendFile(boolean a) throws IOException {
		if(a == false) {
			output.writeUTF("FALSE");
		}
		else {
			output.writeUTF("TRUE");
			output.writeUTF(Integer.toString((Menubar.blankColor).getRGB()));
			output.writeUTF(Integer.toString((Menubar.shipColor).getRGB()));
			output.writeUTF(Integer.toString((Menubar.hitColor).getRGB()));
			output.writeUTF(Integer.toString((Menubar.missColor).getRGB()));
			output.writeUTF(Integer.toString((Menubar.shipHitColor).getRGB()));
			output.writeUTF(Integer.toString(Menubar.turnTime));
			output.writeUTF(Integer.toString(Menubar.defaultPort));
			output.writeUTF(Menubar.defaultIP);
			output.writeUTF(Integer.toString(Menubar.numShips));
		}
	}
	/**
	 * Function for sending ship lengths
	 * @param a - Integer array of ship length
	 * @throws IOException - handled manually
	 */
	public static void sendShips(int[] a) throws IOException {
		output.writeUTF(Integer.toString((a.length)));
		for(int i = 0; i < a.length; i++) {
			output.writeUTF(Integer.toString(a[i]));
		}
	}
}
