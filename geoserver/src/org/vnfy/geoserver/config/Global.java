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
package org.vnfy.geoserver.config;


//import java.util.logging.*;
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
 * @version $Id: Global.java,v 1.1.2.1 2003/12/30 23:39:15 dmzwiers Exp $
 */
public class Global implements Cloneable{
	
	/*
	 * The list of text data fields represented by this class. 
	 */
	//private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.config");
	//private Level loggingLevel = Logger.getLogger("org.vfny.geoserver").getLevel();
	
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
	  * well as specifying the encoding in the return org.vnfy.geoserver.config.org.vnfy.geoserver.config.xml header and mime type.
	  * The default is UTF-8.  Also be warned that GeoServer does not check if
	  * the CharSet is valid before attempting to use it, so it will fail
	  * miserably if a bad charset is used.
	  */
	 private Charset charSet;
	 private String baseUrl;
	 private String schemaBaseUrl;
	 
	 private Level loggingLevel = null;
	 
	/** Default name for configuration directory */
	//private static final String CONFIG_DIR = "WEB-INF/";

	/** Default name for configuration directory */
	//private static final String DATA_DIR = "data/";
	
	private Contact contact = null;
	
	public Global(){
		defaultSettings();
	}
	
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
			contact = null;
	}
	
	/**
	 * Implement clone.
	 * <p>
	 * Charset is not cloned.
	 * </p>
	 * @see java.lang.Object#clone()
	 * 
	 * @return
	 */
	public Object clone(){
		return new Global(this);
	}
	
	public boolean equals(Object obj){
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
