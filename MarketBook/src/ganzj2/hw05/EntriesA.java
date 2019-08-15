package ganzj2.hw05;

public class EntriesA {
	private String Name;
	private String Street;
	private String City;
	private String County;
	private String State;
	private String Zip;
	private String S1D;
	private String S1T;
	private String FMID;
	
	public EntriesA() {
		
	}
	
	/**
	 * 
	 * @param n - String Name
	 * @param s1 - String Street
	 * @param c1 - String City
	 * @param c2 - String County
	 * @param s2 - String State
	 * @param z - String Zip
	 * @param s1d - String Season 1 Date
	 * @param s1t - String Season 1 Time
	 * @param f - String FMID
	 */
	public EntriesA(String n, String s1, String c1, String c2, String s2, String z, String s1d, String s1t, String f) {
		this.Name = n;
		this.Street = s1;
		this.City = c1;
		this.County = c2;
		this.State = s2;
		this.Zip = z;
		this.S1D = s1d;
		this.S1T = s1t;
		this.FMID = f;
	}
	
	/**
	 * 
	 * @return String Object's Name
	 */
	public String getName() {
		return Name;
	}
	
	/**
	 * 
	 * @return String Object's Street
	 */
	public String getStreet() {
		return Street;
	}
	
	/**
	 * 
	 * @return String Object's City
	 */
	public String getCity() {
		return City;
	}
	
	/**
	 * 
	 * @return String Object's County
	 */
	public String getCounty() {
		return County;
	}
	
	/**
	 * 
	 * @return String Object's State
	 */
	public String getState() {
		return State;
	}
	
	/**
	 * 
	 * @return String Object's Zip
	 */
	public String getZip() {
		return Zip;
	}
	
	/**
	 * 
	 * @return String Object's Primary Season
	 */
	public String getS1D() {
		return S1D;
	}
	
	/**
	 * 
	 * @return String Object's Primary Time
	 */
	public String getS1T() {
		return S1T;
	}
	
	/**
	 * 
	 * @return String Object's FMID
	 */
	public String getFMID() {
		return FMID;
	}
}
