package org.geoserver.ows.http;

/**
 * Parses a key-value pair into a key-object pair.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public abstract class KvpParser {

	/**
	 * The key.
	 */
	String key;
	/**
	 * The class of parsed objects.
	 */
	Class binding;
	
	public KvpParser( String key, Class binding ) {
		this.key = key;
		this.binding = binding;
	}
	
	public String getKey() {
		return key;
	}
	
	public Class getBinding() {
		return binding;
	}
	
	/**
	 * Parses the string representation into the object representation.
	 *  
	 * @param value The string value.
	 * 
	 * @return The parsed object, or null if it could not be parsed.
	 * 
	 * @throws Exception In the event of an unsuccesful parse.
	 */
	public abstract Object parse(String value) throws Exception;
	
}
