/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import java.nio.charset.Charset;
import java.util.logging.Level;

import org.vfny.geoserver.global.dto.ContactDTO;
import org.vfny.geoserver.global.dto.GeoServerDTO;

/**
 * Global GeoServer Configuration model.
 * 
 * <p>
 * GlobalConfig represents the configuration model needed to set up GeoServer
 * for use.
 * </p>
 *
 * @author David Zwiers, Refractions Research, Inc.
 * @version $Id: GlobalConfig.java,v 1.4 2004/01/12 23:55:27 dmzwiers Exp $
 */
public class GlobalConfig{
	public static final String CONFIG_KEY = "Config.Global";
    /** Sets the max number of Features returned by GetFeature */
    private int maxFeatures = 20000;

    /**
     * XML Verbosity.
     * 
     * <p>
     * Whether newlines and indents should be returned in XML responses.
     * </p>
     * 
     * <p>
     * This should be called something other than verbose. Verbose should
     * control things like printing out "magic" comments that tell people how
     * to edit the xml files by hand.
     * </p>
     * Default is false
     */
    private boolean verbose = true;

    /**
     * Number of decimal places returned in a GetFeature response.
     * 
     * <p>
     * Sets the max number of decimal places past the zero returned in a
     * GetFeature response.  Default is 4.
     * </p>
     * DZ - should it be moved to FeatureTypeInfo level? JG - no WMS also has
     * a getFeature response
     */
    private int numDecimals = 8;

    /**
     * Sets the global character set.
     * 
     * <p>
     * This could use some more testing from international users. What it does
     * is sets the encoding globally for all postgis database connections (the
     * charset tag in FeatureTypeInfo), as well as specifying the encoding
     * in the return
     * <code>org.vfny.geoserver.config.org.vfny.geoserver.global.xml</code>
     * header and mime type.
     * </p>
     * 
     * <p>
     * The default is UTF-8
     * </p>
     * 
     * <p>
     * Also be warned that GeoServer does not check if the CharSet is valid
     * before attempting to use it, so it will fail miserably if a bad charset
     * is used.
     * </p>
     */
    private Charset charSet;

    /**
     * The base URL where this servlet will run.
     * <p>
     * If running locally then <code>http://localhost:8080</code>
     * (or whatever port you're running on) should work.
     * </p>
     * <p>
     * If you are serving to the world then this must be the location where
     * the geoserver servlets appear
     * </p>
     * <p>
     * JG - can we figure this out at runtime?
     * </p>
     */
    private String baseUrl;

    /**
     * Define a base url for the location of the wfs schemas.
     * <p>
     * By default GeoServer  loads and references its own at
     * <code>/data/capabilities</code>.
     * </p>
     * <p>
     * The standalone Tomcat server needs SchemaBaseUrl defined for validation.
     * </p>
     */
    private String schemaBaseUrl;

    /**
     * Defines the Application logging level.
     * <p>
     * Common options are SEVERE, WARNING, INFO, CONFIG,  FINER, FINEST, in
     * order of Increasing statements logged.
     * </p>
     * <p>
     * There may be more then one point of control - the web containers often
     * controls logging, the jakarta commons logging system is used by struts,
     * these names seem taken from the jdk14 logging framework and GeoServer
     * seems to also use log4j.
     * </p>
     */
    private Level loggingLevel = null;

    /* Default name for configuration directory */
    //private static final String CONFIG_DIR = "WEB-INF/";

    /* Default name for configuration directory */
    //private static final String DATA_DIR = "data/";

    /**
     * The Server contact person and their contact information.
     */
    private ContactConfig contact = null;

    /**
     * GlobalConfig constructor.
     * 
     * <p>
     * Creates an instance of GlobalConfig and initializes to default settings.
     * </p>
     *
     * @see defaultSettings()
     */
    public GlobalConfig() {
		maxFeatures = 20000;
		verbose = true;
		numDecimals = 8;
		charSet = Charset.forName("ISO-8859-1");
		baseUrl = null;
		schemaBaseUrl = null;
		contact = null;
    }

	/**
	 * GlobalConfig constructor.
	 * 
	 * <p>
	 * Creates a copy of the GeoServerDTO object provided.  Charset is not cloned, everything
	 * else is.
	 * </p>
	 *
	 * @param g
	 */
	public GlobalConfig(GeoServerDTO g) {
		if (g == null) {
			throw new NullPointerException();
		}

		maxFeatures = g.getMaxFeatures();
		verbose = g.isVerbose();
		numDecimals = g.getNumDecimals();
		charSet = g.getCharSet();
		baseUrl = g.getBaseUrl();
		schemaBaseUrl = g.getSchemaBaseUrl();

		if (g.getContact() != null) {
			contact = new ContactConfig(g.getContact());
		} else {
			contact = new ContactConfig();
		}
	}

	/**
	 * Implement updateDTO.
	 * <p>
	 * Populates this instance with the GeoServerDTO object provided.
	 * </p>
	 * @see org.vfny.geoserver.config.DataStructure#updateDTO(java.lang.Object)
	 * 
	 * @param obj A valid GeoServerDTO object to populate this object from
	 * @return true when the parameter is valid and stored.
	 */
	public void update(GeoServerDTO g) {
		if (g == null) {
			throw new NullPointerException("GeoServer Data Transfer Object required");
		}

		maxFeatures = g.getMaxFeatures();
		verbose = g.isVerbose();
		numDecimals = g.getNumDecimals();
		charSet = g.getCharSet();
		baseUrl = g.getBaseUrl();
		schemaBaseUrl = g.getSchemaBaseUrl();

		if (g.getContact() != null) {
			contact = new ContactConfig(g.getContact());
		} else {
			contact = new ContactConfig();
		}
	}

    /**
     * Implement toDTO.
     * <p>
     * Creates a copy of the data in a GeoServerDTO representation
     * </p>
     * @see org.vfny.geoserver.config.DataStructure#toDTO()
     * 
     * @return a copy of the data in a GeoServerDTO representation
     */
    public GeoServerDTO toDTO(){
    	GeoServerDTO g = new GeoServerDTO();
		 g.setMaxFeatures(maxFeatures);
		 g.setVerbose(verbose);
		 g.setNumDecimals(numDecimals);
		 g.setCharSet(charSet);
		 g.setBaseUrl(baseUrl);
		 g.setSchemaBaseUrl(schemaBaseUrl);
		 g.setContact((ContactDTO)contact.toDTO());
    	return g;
    }

    /**
     * getBaseUrl purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * getCharSet purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public Charset getCharSet() {
        return charSet;
    }

    /**
     * getContact purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public ContactConfig getContact() {
        return contact;
    }

    /**
     * getLoggingLevel purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */

    //public Level getLoggingLevel() {
    //	return loggingLevel;
    //}

    /**
     * getMaxFeatures purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public int getMaxFeatures() {
        return maxFeatures;
    }

    /**
     * getNumDecimals purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public int getNumDecimals() {
        return numDecimals;
    }

    /**
     * getSchemaBaseUrl purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getSchemaBaseUrl() {
        return schemaBaseUrl;
    }

    /**
     * isVerbose purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * setBaseUrl purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param url
     */
    public void setBaseUrl(String url) {
        baseUrl = url;
    }

    /**
     * setCharSet purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param charset
     */
    public void setCharSet(Charset charset) {
        if (charset == null) {
            charset = Charset.forName("ISO-8859-1");
        }

        charSet = charset;
    }

    /**
     * setContact purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param contact
     */
    public void setContact(ContactConfig contact) {
        if (contact == null) {
            contact = new ContactConfig();
        }

        this.contact = contact;
    }

    /*
     * setLoggingLevel purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param i
     */

    //public void setLoggingLevel(Level level) {
    //	loggingLevel = level;
    //}

    /**
     * setMaxFeatures purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param i
     */
    public void setMaxFeatures(int i) {
        maxFeatures = i;
    }

    /**
     * setNumDecimals purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param i
     */
    public void setNumDecimals(int i) {
        numDecimals = i;
    }

    /**
     * setSchemaBaseUrl purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param url
     */
    public void setSchemaBaseUrl(String url) {
        schemaBaseUrl = url;
    }

    /**
     * setVerbose purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param b
     */
    public void setVerbose(boolean b) {
        verbose = b;
    }

    /**
     * getLoggingLevel purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public Level getLoggingLevel() {
        return loggingLevel;
    }

    /**
     * setLoggingLevel purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param level
     */
    public void setLoggingLevel(Level level) {
        loggingLevel = level;
    }
}
