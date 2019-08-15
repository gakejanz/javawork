package ganzj2.hw05;

import java.util.*;

public class Zips {
	/**
	 * Function to produce search radius used to query database
	 * @param lat - String of searched Latidude
	 * @param lon - String of Search longitude
	 * @param radius - String of Search radius
	 * @return - Double Array of the 4 search limits
	 */
	public static double[] getZips(double lat, double lon, int radius){
		
		double[] list = new double[4];
		double la;
		double lo;
		double earthradius = 3956.0;
		double lat_rads = lat * (Math.PI / 180.0);
		double lon_rads = lon * (Math.PI / 180.0);
		double range = radius / earthradius;
		
		double upper = (lat_rads + range) * (180.0 / Math.PI);
		double lower = (lat_rads - range) * (180.0 / Math.PI);
		
		la = Math.asin(Math.sin(lat_rads) * Math.cos(range));
		lo = Math.atan2(Math.sin(Math.PI / 2) * Math.sin(range) * Math.cos(lat_rads), Math.cos(range) - Math.sin(lat_rads) * Math.sin(la) );
		
		double right = (((lon_rads + lo + Math.PI) % (2.0 * Math.PI)) - Math.PI) * (180.0 / Math.PI);
		
		lo = Math.atan2(Math.sin(3.0 * Math.PI / 2) * Math.sin(range) * Math.cos(lat_rads), Math.cos(range) - Math.sin(lat_rads) * Math.sin(la) );
		
		double left = ((((lon_rads + lo + Math.PI) % (2.0 * Math.PI)) - Math.PI) * (180.0 / Math.PI));
		
		list[0] = upper;
		list[1] = lower;
		list[2] = right;
		list[3] = left;
		
		return list;
	}
}
