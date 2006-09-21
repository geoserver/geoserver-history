package org.geoserver.ows.http;

import java.util.Comparator;

/**
 * Compares two version numbers as strings of the form <major>.<minor>.<patch>. 
 * 
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class VersionComparator implements Comparator {

	/** 
	 * Flag indicating ascending sort order.
	 */
	public static final int ASCENDING = 1;
	/**
	 * Flag indicating descending sort order.
	 */
	public static final int DESCENDING = -1;
	
	/**
	 * Sort order;
	 */
	private int order;
	
	/**
	 * Creates a new comparator with a {@link #ASCENDING} order.
	 */
	public VersionComparator( ) {
		this( ASCENDING );
	}
	
	/**
	 * Creates a new comparator with the specified order.
	 *  
	 * @param order One of {@link #ASCENDING}, or {@link #DESCENDING}.
	 */
	public VersionComparator( int order ) {
		this.order = order;
	}
	
	/**
	 * Comares two version numbers.
	 * <p>
	 * <code>o1</code> and <code>o2</code> must be instances of String, and 
	 * must be of the form <major>.<minor>.<patch>
	 * </p>
	 */
	public int compare( Object o1, Object o2 ) {
		String v1 = (String) o1;
		String v2 = (String) o2;
		
		return order*v1.compareTo( v2 );
	}

}
