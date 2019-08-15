package hw03.ganzj2;

import java.net.*;

import javax.swing.JOptionPane;

import java.awt.Color;
import java.io.*;

public class Client {
	
	 private Socket socket = null; 
	 private static DataInputStream  input = null; 
	 private static DataOutputStream output = null; 
	 private BufferedReader br = null;
	
	/**
	 * Function used to connect client computer to host server 
	 * @param host - String containing host IP address
	 * @param port - Integer port address
	 * @throws UnknownHostException - handled
	 * @throws IOException - handled
	 */
	public Client(String host, int port) throws UnknownHostException, IOException {
		boolean work = false;
		while(work == false) {
			try {
				InetAddress ipaddress = InetAddress.getByName(host);
				socket = new Socket(ipaddress.getHostAddress(), port);
				
				input = new DataInputStream(socket.getInputStream());
				output = new DataOutputStream(socket.getOutputStream());
				work = true;
			}
			catch(UnknownHostException e) {
				System.out.println("Cannot find host with that IP Address");
				host = JOptionPane.showInputDialog("Please enter another IP address");
			}
			catch (IOException e) {
				System.out.println("Client Class IO Exception");
			}
		}
	}

	/**
	 * Function used to send shot coordinates to client
	 * @param s - Integer Array containing coordinates
	 */
	public static void sendShot(int[] s) {
		String message = Integer.toString(s[0]) + "," + Integer.toString(s[1]);
		try {
			output.writeUTF(message);
		} catch (IOException e) {
		}
		
	}
	/**
	 * Function for receiving coordinates of shot from server
	 * @return Integer Array of the host's shot
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
			int [] temp = new int [2];
			temp[0] = -1;
			temp[1] = -1;
			return temp;
		}
	}
	/**
	 * Function used to receive the result of the clients shot from the host
	 * @return - Integer of the hosts result
	 */
	public static int receiveResult() {
		String message = "";
		try {
			message = input.readUTF();
		} catch (IOException e) {
		}
		if(message == "") {
			return -1;
		}
		else {
			return Integer.parseInt(message);	
		}
	}
	
	/**
	 * Function used to send the host's shot result to the host
	 * @param r - Integer result sent to Host
	 */
	public static void sendResult(int r) {
		String temp = Integer.toString(r);
		try {
			output.writeUTF(temp);
		} catch (IOException e) {
		}
	}
	/**
	 * Function used to send the clients name to the host
	 * @param n - String clients name
	 */
	public static void sendName(String n) {
		 try {
			 output.writeUTF(n);
		} catch (IOException e) {
		}
	}
	/**
	 * Functions used to receive the hosts name from the host
	 * @return String name of the host
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
	 * Functions used to receive start values from the host
	 * @return Boolean Array containing start information from the host
	 * @throws IOException - handled internally
	 */
	public static Boolean[] receiveStart() throws IOException {
		String temp = "";
		while(temp == "") {
			temp = input.readUTF();
		}
		Boolean[] bool = new Boolean[2];
		Boolean a = false;
		Boolean b = false;
		String[] tempa = temp.split(",");
		if(Integer.parseInt(tempa[0]) == 1) {
			a = true;
		}
		if(Integer.parseInt(tempa[1]) == 1) {
			b = true;
		}
		bool[0] = a;
		bool[1] = b;
		return bool;
	}
	/**
	 * Function used to send the status of the clients' ship picking to the host
	 * @param s - String true or false of the clients status
	 * @throws IOException - handled in MAIN
	 */
	public static void sendCheck(String s) throws IOException {
		output.writeUTF(s);
	}
	
	/**
	 * Function used to receive the Check status from the host
	 * @return - Boolean status of host
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
	 * Function used to receive preference file data from host
	 * @throws IOException - This exception will not happen
	 */
	public static void receiveFile() throws IOException {
		String temp = "";
		while(temp == "") {
			try {
				temp = input.readUTF();
			} 
			catch (IOException e) {
			}
		}
		if(temp.compareTo("FALSE") == 0) {
			return;
		}
		else {
			temp = "";
			temp = input.readUTF();
			Menubar.blankColor = new Color(Integer.parseInt(temp));
			temp = "";
			temp = input.readUTF();
			Menubar.shipColor = new Color(Integer.parseInt(temp));
			temp = "";
			temp = input.readUTF();
			Menubar.hitColor = new Color(Integer.parseInt(temp));
			temp = "";
			temp = input.readUTF();
			Menubar.missColor = new Color(Integer.parseInt(temp));
			temp = "";
			temp = input.readUTF();
			Menubar.shipHitColor = new Color(Integer.parseInt(temp));
			temp = "";
			temp = input.readUTF();
			Menubar.turnTime = Integer.parseInt(temp);
			temp = "";
			temp = input.readUTF();
			Menubar.defaultPort = Integer.parseInt(temp);
			temp = "";
			temp = input.readUTF();
			Menubar.defaultIP = temp;
			temp = "";
			temp = input.readUTF();
			Menubar.numShips = Integer.parseInt(temp);
		}
		return;
	}
	/**
	 * Function used to receive ship lengths from host
	 * @return - Integer Array of ship lengths
	 * @throws IOException - No Need 
	 */
	public static int[] receiveShips() throws IOException {
		String temp = "";
		temp = input.readUTF();
		int[] a = new int[Integer.parseInt(temp)];
		
		for(int i = 0; i < a.length; i++) {
			temp = "";
			temp = input.readUTF();
			a[i] = Integer.parseInt(temp);
		}
		return a;
	}
}
