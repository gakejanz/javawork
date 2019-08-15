package ganzj2.hw05;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;

/**
 * Servlet implementation class displayInfo
 */
@WebServlet("/info")
public class displayInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Connection con;
	private static String URL = "jdbc:mysql://localhost:3306/markets";
	private static String User = "root";
	private static String Password = "password";
	ResultSet rs;
	ResultSetMetaData rsmd;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public displayInfo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * If A specific market link was clicked this servlet prodoces more information about that market.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String FMID = request.getParameter("id").toString();
		PrintWriter out = response.getWriter();
		
		try {
			connect();
			
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM m WHERE FMID = '" + FMID + "';");
			rsmd = rs.getMetaData();
			rs.next();
			
			out.print("<html><style>\r\n" + 
		    		"table, th, td {\r\n" + 
		    		"  border: 1px solid black;\r\n" + 
		    		"  border-collapse: collapse;\r\n" + 
		    		"}\r\n" + 
		    		"th, td {\r\n" + 
		    		"  padding: 15px;\r\n" + 
		    		"}\r\n" +
		    		"body {\r\n" + 
		    		"	margin: 0;\r\n" + 
		    		"	font-family: Arial, Helvetica, sans-serif;\r\n" + 
		    		"	color: green;\r\n" + 
		    		"	background-color: #e9e9e9\r\n" + 
		    		"}\r\n" + 
		    		"h1 {\r\n" + 
		    		"	font-family: Arial, Helvetica, sans-serif;\r\n" + 
		    		"	color: green;\r\n" + 
		    		"	margin-left: 8px;\r\n" + 
		    		"}\r\n" + 
		    		"</style></html>");
			
			out.print("<html><body>");
			
			out.print("<h1>Market -  " + rs.getString("MarketName") + "</h1> <table style = \"width:100%\" id = \"table\">\r\n" + 
					"            <tr>\r\n");
			for(int i = 1; i < rsmd.getColumnCount(); i++) {
				out.print("<th>" + rsmd.getColumnName(i) + "</th>");
			}
			
			out.print("</tr><tr>");
			
			for(int i = 1; i < rsmd.getColumnCount(); i++) {
				out.print("<td>" + rs.getString(i) + "</td>");
			}
			
			out.print("</tr></table></html>");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
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
	
	protected static void disconnect() throws SQLException {
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }

}
