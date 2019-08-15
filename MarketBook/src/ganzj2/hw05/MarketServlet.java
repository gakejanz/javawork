package ganzj2.hw05;

import java.io.*;
import java.util.*;
import java.util.Date;
import java.sql.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MarketServlet
 */
@WebServlet("/search")
public class MarketServlet extends HttpServlet {
	Connection c;
	Statement stmt;
	ResultSet rs;
	static String zipSearch;
	static String citySearch;
	static String stateSearch;
	static String radius;
	static int numpages;
	static String page;
	static boolean firstpage;
	static List<EntriesA> listE;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MarketServlet() {
        super();
        // TODO Auto-generated constructor stub
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * Gathers Search Parameters
	 * Determines Search Results
	 * Displays Search Results
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
	    //out.print("<html><body><h1 align='center'>" + ConnectDatabase.getFMID() + "</h1></body></html>");
		
		zipSearch = request.getParameter("ZIP");
		citySearch = request.getParameter("CITY");
		stateSearch = request.getParameter("state");
		radius = request.getParameter("miles");
		page = request.getParameter("page");
		
		if(page == null) {
			firstpage = true;
			getMarkets.totalEntries = new ArrayList<EntriesA>();
			listE = new ArrayList<>();
			 try {
				 if((zipSearch.compareTo("") == 0) && (citySearch.compareTo("") == 0)) {
				 	listE = getMarkets.getTable();
				 }
				 else if(zipSearch.compareTo("") != 0) {
					 listE = getMarkets.getTable(zipSearch, radius);
				 }
				 else if(citySearch.compareTo("") != 0) {
					 listE = getMarkets.getTable(citySearch, stateSearch, radius);
				 }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			page = "1";
		}
		else {
			firstpage = false;
			listE = new ArrayList<>();
			listE = getMarkets.nextPage();
		}
		
		
		
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
	    		"pagination {\r\n" + 
	    		"  display: inline-block;\r\n" + 
	    		"}\r\n" + 
	    		"\r\n" + 
	    		".pagination a {\r\n" + 
	    		"  color: black;\r\n" + 
	    		"  float: left;\r\n" + 
	    		"  padding: 8px 16px;\r\n" + 
	    		"  text-decoration: none;\r\n" + 
	    		"}" +
	    		"</style></html>");

	    
	    
		 
		if(listE != null) {
			numpages = Integer.parseInt(getMarkets.results)/ 50;
			out.print("<h1>Markets</h1> <table style = \"width:100%\" id = \"table\">\r\n" + 
					"            <tr>\r\n" + 
					"                <th>Market Name</th>\r\n" + 
					"                <th>Address</th>\r\n" + 
					"                <th>City</th>\r\n" + 
					"                <th>County</th>\r\n" + 
					"                <th>State</th>\r\n" + 
					"                <th>Zip</th>\r\n" + 
					"                <th>Primary Season Dates</th>\r\n" + 
					"                <th>Primary Season Times</th>\r\n" + 
					"            </tr>");
			for(EntriesA e : listE) {
				out.print("<tr><td style=cursor:pointer onclick=\"window.location.href = '/MarketBook/info?id=" + e.getFMID() +"';\">" + e.getName() + "</td>");
				out.print("<td>" + e.getStreet() + "</td>");
				out.print("<td>" + e.getCity() + "</td>");
				out.print("<td>" + e.getCounty() + "</td>");
				out.print("<td>" + e.getState() + "</td>");
				out.print("<td>" + e.getZip() + "</td>");
				out.print("<td>" + e.getS1D() + "</td>");
				out.print("<td>" + e.getS1T() + "</td></tr>");
			}
			
			int a = Integer.parseInt(page);
			int b = a;
			
			out.print("</tr></table>"
					+ "<div class=\"pagination\">\r\n");
			if(page.compareTo("1") != 0) {
					out.print("  <a href=search?page=" + (b - 1) + ">&laquo;</a>\r\n");
			}
			while((a <= numpages) && (a <= b + 5)) {
				out.print("  <a href=search?page=" + a + ">" + a + "</a>\r\n"); 
				a++;
			}
			
			out.print("  <a href=search?page=" + (b + 1) + ">&raquo;</a>\r\n" + 
					"</div></html>");
		}
		else {
			out.print("<h1>No Markets Found for that search</h1>");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
	}

}

