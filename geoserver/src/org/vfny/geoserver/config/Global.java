/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2003, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vfny.geoserver.config;


import java.nio.charset.*;
import java.util.logging.*;
/**
 * Global purpose.
 * 
 * <p>
 * Represents the runtime Global attributes for a server configuration. 
 * This class should be used as a singleton, to be implemented by the classes user.
 * <p>
 * 
 * @author David Zwiers, Refractions Research, Inc.
 * @version $Id: Global.java,v 1.1.2.1 2003/12/31 20:05:33 dmzwiers Exp $
 */
public class Global implements DataStructure{
	
	/*
	 * The list of text data fields represented by this class. 
	 */
	
	/** Sets the max number of Features returned by GetFeature */
	 private int maxFeatures = 20000;

	 /**
	  * Whether newlines and indents should be returned in XML responses.
	  * Default is false
	  */
	 private boolean verbose = true;

	 /**
	  * Sets the max number of decimal places past the zero returned in a
	  * GetFeature response.  Default is 4 should it be moved to FeatureType
	  * level?
	  */
	 private int numDecimals = 8;

	 /**
	  * Sets the global character set.  This could use some more testing from
	  * international users, but what it does is sets the encoding globally for
	  * all postgis database connections (the charset tag in FeatureType), as
	  * well as specifying the encoding in the return 
	  * org.vfny.geoserver.config.org.vfny.geoserver.config.xml header and mime type.
	  * The default is UTF-8.  Also be warned that GeoServer does not check if
	  * the CharSet is valid before attempting to use it, so it will fail
	  * miserably if a bad charset is used.
	  */
	 private Charset charSet;
	 
	 /**
	  * The base URL where this servlet will run.  If running locally then 
	  * http://localhost:8080 (or whatever port you're running on) should work. 
	  * If you are serving to the world then this must be the location where the 
	  * geoserver servlets appear
	  */
	 private String baseUrl;
	 
	 /**
	  * Define a base url for the location of the wfs schemas. By default GeoServer 
	  * loads and references its own at <URL>/data/capabilities. Uncomment to enable. 
	  * The standalone Tomcat server needs SchemaBaseUrl defined for validation.
	  */
	 private String schemaBaseUrl;
	 
	 /**
	  * Defines the logging level.  Common options are SEVERE, WARNING, INFO, CONFIG, 
	  * FINER, FINEST, in order of Increasing statements logged.
	  */
	 private Level loggingLevel = null;
	 
	/* Default name for configuration directory */
	//private static final String CONFIG_DIR = "WEB-INF/";

	/* Default name for configuration directory */
	//private static final String DATA_DIR = "data/";
	
	/**
	 * The Server contact person and their contact information.
	 */
	private Contact contact = null;
	
	/**
	 * Global constructor.
	 * <p>
	 * Creates an instance of Global and initializes to default settings.
	 * </p>
	 * @see defaultSettings()
	 */
	public Global(){
		defaultSettings();
	}
	
	/**
	 * defaultSettings purpose.
	 * <p>
	 * Sets all the default values for the class.
	 * This method should only be called by constructors. 
	 * </p>
	 *
	 */
	private void defaultSettings(){
		maxFeatures = 20000;
		verbose = true;
		numDecimals = 8;
		charSet = Charset.forName("ISO-8859-1");
		baseUrl = null;
		schemaBaseUrl = null;
		contact = null;
	}
	
	/**
	 * 
	 * Global constructor.
	 * <p>
	 * Creates a copy of the Global object provided,
	 * or sets the values to default if one is not provided. 
	 * Charset is not cloned, everything else is.
	 * </p>
	 * @param g
	 */
	public Global(Global g){
		if(g==null){
			defaultSettings();
			return;
		}
		maxFeatures = g.getMaxFeatures();
		verbose = g.isVerbose();
		numDecimals = g.getNumDecimals();
		charSet = g.getCharSet();
		baseUrl = g.getBaseUrl();
		schemaBaseUrl = g.getSchemaBaseUrl();
		if(g.getContact()!=null)
			contact = (Contact)(g.getContact().clone());
		else
			contact = new Contact();
	}
	
	/**
	 * Implement clone.
	 * <p>
	 * Charset is not cloned, everything else is. Strings are reference 
	 * copied because of the Hashtable implementation in memory.
	 * </p>
	 * @see java.lang.Object#clone()
	 * 
	 * @return A new Global object which is a copy of this object.
	 */
	public Object clone(){
		return new Global(this);
	}
	
	/**
	 * Implement equals.
	 * <p>
	 * Compares the equality of the two objects. 
	 * </p>
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj The object to checked for equivalence.
	 * @return true when the objects are the same.
	 */
	public boolean equals(Object obj){
		if(obj == null || !(obj instanceof Global))
			return false;
		Global g = (Global)obj;
		boolean r = true;
		r = r && maxFeatures == g.getMaxFeatures();
		r = r && verbose == g.isVerbose();
		r = r && numDecimals == g.getNumDecimals();
		if(charSet != null)
			r = r && charSet.equals(g.getCharSet());
		else if(g.getCharSet()!=null) return false;
		r = r && baseUrl == g.getBaseUrl();
		r = r && schemaBaseUrl == g.getSchemaBaseUrl();
		if(contact!=null)
			r = r && contact.equals(g.getContact());
		else if(g.getContact()!=null) return false;
		
		return r;
	}
	
	/**
	 * getBaseUrl purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * getCharSet purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public Charset getCharSet() {
		return charSet;
	}

	/**
	 * getContact purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * getLoggingLevel purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	//public Level getLoggingLevel() {
	//	return loggingLevel;
	//}

	/**
	 * getMaxFeatures purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public int getMaxFeatures() {
		return maxFeatures;
	}

	/**
	 * getNumDecimals purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public int getNumDecimals() {
		return numDecimals;
	}

	/**
	 * getSchemaBaseUrl purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getSchemaBaseUrl() {
		return schemaBaseUrl;
	}

	/**
	 * isVerbose purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * setBaseUrl purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param url
	 */
	public void setBaseUrl(String url) {
		baseUrl = url;
	}

	/**
	 * setCharSet purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param charset
	 */
	public void setCharSet(Charset charset) {
		if(charset == null)
			charset = Charset.forName("ISO-8859-1");
		charSet = charset;
	}

	/**
	 * setContact purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param contact
	 */
	public void setContact(Contact contact) {
		if(contact == null)
			contact = new Contact();
		this.contact = contact;
	}

	/**
	 * setLoggingLevel purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param level
	 */
	//public void setLoggingLevel(Level level) {
	//	loggingLevel = level;
	//}

	/**
	 * setMaxFeatures purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param i
	 */
	public void setMaxFeatures(int i) {
		maxFeatures = i;
	}

	/**
	 * setNumDecimals purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param i
	 */
	public void setNumDecimals(int i) {
		numDecimals = i;
	}

	/**
	 * setSchemaBaseUrl purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param url
	 */
	public void setSchemaBaseUrl(String url) {
		schemaBaseUrl = url;
	}

	/**
	 * setVerbose purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param b
	 */
	public void setVerbose(boolean b) {
		verbose = b;
	}

	/**
	 * getLoggingLevel purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public Level getLoggingLevel() {
		return loggingLevel;
	}

	/**
	 * setLoggingLevel purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param level
	 */
	public void setLoggingLevel(Level level) {
		loggingLevel = level;
	}

}
