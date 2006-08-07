package org.geoserver.ows.http;

import java.io.InputStream;

/**
 * Parses an XML stream into an object representation.
 * <p>
 *  
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public abstract class XmlReader {
	
	/**
	 * Name of element
	 */
	String namespace;
	/**
	 * Name of element
	 */
	String element;
	/**
	 * Appliction specific version number.
	 */
	String version;
	
	public XmlReader( String namespace, String element ) {
		this( namespace, element, null );
	}
	
	public XmlReader( String namespace, String element, String version ) {
		this.namespace = namespace;
		this.element = element;
		this.version = version;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public String getElement() {
		return element;
	}
	
	public String getVersion() {
		return version;
	}
	
	public abstract Object parse( InputStream input ) throws Exception;
}
