/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import java.nio.charset.*;
import java.util.logging.*;


/**
 * Global GeoServer Configuration model.
 * 
 * <p>
 * GlobalConfig represents the configuration model needed to set up GeoServer
 * for use.
 * </p>
 *
 * @author David Zwiers, Refractions Research, Inc.
 * @version $Id: GlobalConfig.java,v 1.3.2.3 2004/01/01 00:20:23 jive Exp $
 */
public class GlobalConfig implements DataStructure {
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
     * DZ - should it be moved to FeatureTypeConfig level? JG - no WMS also has
     * a getFeature response
     */
    private int numDecimals = 8;

    /**
     * Sets the global character set.
     * 
     * <p>
     * This could use some more testing from international users. What it does
     * is sets the encoding globally for all postgis database connections (the
     * charset tag in FeatureTypeConfig), as well as specifying the encoding
     * in the return
     * <code>org.vfny.geoserver.config.org.vfny.geoserver.config.xml</code>
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
        defaultSettings();
    }

    /**
     * GlobalConfig constructor.
     * 
     * <p>
     * Creates a copy of the GlobalConfig object provided, or sets the values
     * to default if one is not provided.  Charset is not cloned, everything
     * else is.
     * </p>
     *
     * @param g
     */
    public GlobalConfig(GlobalConfig g) {
        if (g == null) {
            defaultSettings();

            return;
        }

        maxFeatures = g.getMaxFeatures();
        verbose = g.isVerbose();
        numDecimals = g.getNumDecimals();
        charSet = g.getCharSet();
        baseUrl = g.getBaseUrl();
        schemaBaseUrl = g.getSchemaBaseUrl();

        if (g.getContact() != null) {
            contact = (ContactConfig) (g.getContact().clone());
        } else {
            contact = new ContactConfig();
        }
    }

    /**
     * defaultSettings purpose.
     * 
     * <p>
     * Sets all the default values for the class. This method should only be
     * called by constructors.
     * </p>
     */
    private void defaultSettings() {
        maxFeatures = 20000;
        verbose = true;
        numDecimals = 8;
        charSet = Charset.forName("ISO-8859-1");
        baseUrl = null;
        schemaBaseUrl = null;
        contact = null;
    }

    /**
     * Implement clone.
     * 
     * <p>
     * Charset is not cloned, everything else is. Strings are reference  copied
     * because of the Hashtable implementation in memory.
     * </p>
     *
     * @return A new GlobalConfig object which is a copy of this object.
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new GlobalConfig(this);
    }

    /**
     * Implement equals.
     * 
     * <p>
     * Compares the equality of the two objects.
     * </p>
     *
     * @param obj The object to checked for equivalence.
     *
     * @return true when the objects are the same.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof GlobalConfig)) {
            return false;
        }

        GlobalConfig g = (GlobalConfig) obj;
        boolean r = true;
        r = r && (maxFeatures == g.getMaxFeatures());
        r = r && (verbose == g.isVerbose());
        r = r && (numDecimals == g.getNumDecimals());

        if (charSet != null) {
            r = r && charSet.equals(g.getCharSet());
        } else if (g.getCharSet() != null) {
            return false;
        }

        r = r && (baseUrl == g.getBaseUrl());
        r = r && (schemaBaseUrl == g.getSchemaBaseUrl());

        if (contact != null) {
            r = r && contact.equals(g.getContact());
        } else if (g.getContact() != null) {
            return false;
        }

        return r;
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
