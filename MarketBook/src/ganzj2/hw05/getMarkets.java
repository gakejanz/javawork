package ganzj2.hw05;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author ganzj2
 *
 */
public class getMarkets {
	private static Connection con;
	private static String URL = "jdbc:mysql://localhost:3306/markets";
	private static String User = "root";
	private static String Password = "password";
	public static List<EntriesA> listResults;
	public static ResultSet i;
	public static String results;
	public static List<EntriesA> totalEntries;
	
	public getMarkets(ResultSet r) {
		
	}
	
	/**
	 * Function to Connect to SQL Database
	 * @throws SQLException - Handled
	 */
	protected static void connect() throws SQLException {
        if (con == null || con.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            con = DriverManager.getConnection(URL, User, Password);
        }
    }
	
	/**
	 * Function to disconnect from SQL Database
	 * @throws SQLException - Handled
	 */
	protected static void disconnect() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }
	 
	/**
	 * Function to get the results of all markets in database. Default Search
	 * @return - List of Class Entries A, Data to display first 50 results
	 * @throws SQLException - Handled
	 */
	public static List<EntriesA> getTable() throws SQLException {
		listResults = new ArrayList<>();
		
		connect();
		
		Statement stmt = con.createStatement();
		i = stmt.executeQuery("SELECT COUNT(*) FROM m");
		i.next();
		results = i.getString(1);
		ResultSet rs = stmt.executeQuery("SELECT * FROM m");
		
		if(MarketServlet.firstpage) { //if displaying the first page gather data
			while(rs.next()) {
				String n = rs.getString("MarketName"); 
				String s1 = rs.getString("street");
				String c1 = rs.getString("city");
				String c2 = rs.getString("County");
				String s2 = rs.getString("state");
				String z = rs.getString("zip");
				String s1d = rs.getString("Season1Date");
				String s1t = rs.getString("Season1Time");
				String f = rs.getString("FMID");
				
				EntriesA entry = new EntriesA(n,s1,c1,c2,s2,z,s1d,s1t,f);
				totalEntries.add(entry);
			}
			
			rs.close();
			stmt.close();
			disconnect();
		}
		
		int counter= 0; //building list to return
		while((counter < 50) && (!totalEntries.isEmpty())) {
			EntriesA entry = totalEntries.get(counter);
			listResults.add(entry);
			counter++;
		}
		
		
		return listResults;
	}
	
	/**
	 * 
	 * @param zip - String zip code beign searched
	 * @param rad - String radius of search 
	 * @return - List of Class Entries A, Data to display first 50 results
	 * @throws SQLException - Handled
	 */
	public static List<EntriesA> getTable(String zip, String rad) throws SQLException {
		listResults = new ArrayList<>();
		
		connect();
		
		Statement stmt = con.createStatement();
		ResultSet r = stmt.executeQuery("SELECT * FROM z WHERE zip_code = '" + zip + "';" );
		if(r.next() == false) {
			return null;
		}
		String latitude = r.getString("latitude");
		String longitude = r.getString("longitude");
		
		double lat = Double.parseDouble(latitude);
		double lon = Double.parseDouble(longitude);
		
		double[] radius = new double[4];
		radius = Zips.getZips(lat, lon, Integer.parseInt(rad));
		
		double upper = radius[0];
		double lower = radius[1];
		double right = radius[2];
		double left = radius[3];
		i = stmt.executeQuery("SELECT COUNT(*) FROM m WHERE +\r\n" + 
				"										y >= " + lower + "\r\n" + 
				"										 AND y <= " + upper + "\r\n" + 
				"										 AND x >= " + left + "\r\n" + 
				"										 AND x <= " + right); 
		i.next();
		results = i.getString(1);
		
		if(results.compareTo("0") == 0) {
			return null;
		}
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM m WHERE " +
										"y >= " + lower +
										" AND y <= " + upper +
										" AND x >= " + left +
										" AND x <= " + right);
		if(MarketServlet.firstpage) {
			while(rs.next()) {
				String n = rs.getString("MarketName"); 
				String s1 = rs.getString("street");
				String c1 = rs.getString("city");
				String c2 = rs.getString("County");
				String s2 = rs.getString("state");
				String z = rs.getString("zip");
				String s1d = rs.getString("Season1Date");
				String s1t = rs.getString("Season1Time");
				String f = rs.getString("FMID");
				
				EntriesA entry = new EntriesA(n,s1,c1,c2,s2,z,s1d,s1t,f);
				totalEntries.add(entry);
			}
			
			rs.close();
			stmt.close();
			disconnect();
		}
		
		int counter= 0;
		while((counter < 50) && (counter < totalEntries.size())) {
			EntriesA entry = totalEntries.get(counter);
			listResults.add(entry);
			counter++;
		}
		
		return listResults;
	}
	
	/**
	 * 
	 * @param city - String Searched City
	 * @param state - String Searched State
	 * @param rad - String search radius
	 * @return - List of Class Entries A, Data to display first 50 results
	 * @throws SQLException - Handled
	 */
	public static List<EntriesA> getTable(String city, String state, String rad) throws SQLException { //deall with nulls
		listResults = new ArrayList<>();
		
		connect();
		
		Statement stmt = con.createStatement();
		ResultSet r = stmt.executeQuery("SELECT * FROM z WHERE city = '" + city + "' AND state = '" + state +"';" );
		r.next();
		if(r.next() == false) {
			return null;
		}
		String zip = r.getString("zip_code");
		String latitude = r.getString("latitude");
		String longitude = r.getString("longitude");
		
		double lat = Double.parseDouble(latitude);
		double lon = Double.parseDouble(longitude);
		
		double[] radius = new double[4];
		radius = Zips.getZips(lat, lon, Integer.parseInt(rad));
		
		double upper = radius[0];
		double lower = radius[1];
		double right = radius[2];
		double left = radius[3];
		
		i = stmt.executeQuery("SELECT COUNT FROM m WHERE +\r\n" + 
				"										y >= " + lower + "\r\n" + 
				"										 AND y <= " + upper + "\r\n" + 
				"										 AND x >= " + left + "\r\n" + 
				"										 AND x <= " + right);
		i.next();
		results = i.getString(1);
		if(results.compareTo("0") == 0) {
			return null;
		}
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM m WHERE " +
										"y >= " + lower +
										" AND y <= " + upper +
										" AND x >= " + left +
										" AND x <= " + right);
		if(MarketServlet.firstpage) {
			while(rs.next()) {
				String n = rs.getString("MarketName"); 
				String s1 = rs.getString("street");
				String c1 = rs.getString("city");
				String c2 = rs.getString("County");
				String s2 = rs.getString("state");
				String z = rs.getString("zip");
				String s1d = rs.getString("Season1Date");
				String s1t = rs.getString("Season1Time");
				String f = rs.getString("FMID");
				
				EntriesA entry = new EntriesA(n,s1,c1,c2,s2,z,s1d,s1t,f);
				totalEntries.add(entry);
			}
			
			rs.close();
			stmt.close();
			disconnect();
		}
		
		int counter= 0;
		while((counter < 50) && (totalEntries.isEmpty() == false)) {
			EntriesA entry = totalEntries.get(counter);
			listResults.add(entry);
			counter++;
		}
		
		return listResults;
	}
	
	/**
	 * Function to retrieve data after first page
	 * @return * @return - List of Class Entries A, Data to display first 50 results
	 */
	public static List<EntriesA> nextPage(){
		listResults = new ArrayList<>();
		
		int page = Integer.parseInt(MarketServlet.page);
		
		int counter= 0;
		while((counter < 50 * page) && (counter < totalEntries.size())) {
			if((50*page - counter) <= 50 ) {
				EntriesA entry = totalEntries.get(counter);
				listResults.add(entry);
			}
			counter++;
		}
		
		
		return listResults;
	}
	
}
