/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

import java.nio.charset.Charset;
import java.util.logging.Level;


/**
 * Data Transfer Object for Global GeoServer Configuration information.
 * 
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 *
 * @author David Zwiers, Refractions Research, Inc.
 * @version $Id: GeoServerDTO.java,v 1.12 2004/09/09 16:51:58 cholmesny Exp $
 */
public final class GeoServerDTO implements DataTransferObject {
	
	public static class Defaults {
		/**
		 * The default MaxFeatures is 10000
		 *
		 * @see #getMaxFeatures(int)
		 */
		public static final int MaxFeatures = 10000;

		/**
		 * The default encoding for GeoServer it UTF-8.
		 *
		 * @see #getCharSet()
		 */
		public static final Charset Encoding = Charset.forName("UTF-8");

		/** The default verbosity is true, human readable. */
		public static final boolean Verbose = true;

		/** Default is four decimal places. */
		public static final int NumDecimals = 4;

		/** The default logging level is info. */
		public static final Level LoggingLevel = Level.INFO;

		/** The default Administrator's user name (admin) */
		public static final String AdminUserName = "admin";

		/** The default Administrator's password (geoserver) */
		public static final String AdminPassword = "geoserver";

		/**
		 * The default verboseExceptions is false, so that by default the
		 * service exceptions don't look like someone 'kacked'.
		 */
		public static final boolean VerboseExceptions = false;
	}
	
    /** Sets the max number of Features returned by GetFeature */
    private int maxFeatures = Defaults.MaxFeatures;

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
    private boolean verbose = Defaults.Verbose;

    /**
     * Number of decimal places returned in a GetFeature response.
     * 
     * <p>
     * Sets the max number of decimal places past the zero returned in a
     * GetFeature response.  Default is 4.
     * </p>
     * DZ - should it be moved to FeatureTypeInfo level? JG - no WMS also has a
     * getFeature response
     */
    private int numDecimals = Defaults.NumDecimals;

    /**
     * Sets the global character set.
     * 
     * <p>
     * This could use some more testing from international users. What it does
     * is sets the encoding globally for all postgis database connections (the
     * charset tag in FeatureTypeInfo), as well as specifying the encoding in
     * the return
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
    private Charset charSet = Defaults.Encoding;

    /**
     * Define a base url for the location of the wfs schemas.
     * 
     * <p>
     * By default GeoServer  loads and references its own at
     * <code>/data/capabilities</code>.
     * </p>
     * 
     * <p>
     * The standalone Tomcat server needs SchemaBaseUrl defined for validation.
     * </p>
     */
    private String schemaBaseUrl;

    /**
     * Defines the Application logging level.
     * 
     * <p>
     * Common options are SEVERE, WARNING, INFO, CONFIG,  FINER, FINEST, in
     * order of Increasing statements logged.
     * </p>
     * 
     * <p>
     * There may be more then one point of control - the web containers often
     * controls logging, the jakarta commons logging system is used by struts,
     * these names seem taken from the jdk14 logging framework and GeoServer
     * seems to also use log4j.
     * </p>
     */
    private Level loggingLevel = Defaults.LoggingLevel;

    /** The Server contact person and their contact information. */
    private ContactDTO contact = null;

    /** The username for the administrator log-in to perform configuration */
    private String adminUserName = Defaults.AdminUserName;

    /** The password for the administrator log-in to perform configuration */
    private String adminPassword = Defaults.AdminPassword;
    
    /** Whether the exceptions returned to the client should contain full stack traces */
    private boolean verboseExceptions = Defaults.VerboseExceptions;

    /**
     * GlobalConfig constructor.
     * 
     * <p>
     * does nothing
     * </p>
     */
    public GeoServerDTO() {
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
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public GeoServerDTO(GeoServerDTO g) {
        if (g == null) {
            throw new NullPointerException();
        }

        maxFeatures = g.getMaxFeatures();
        verbose = g.isVerbose();
        numDecimals = g.getNumDecimals();
        charSet = g.getCharSet();
        schemaBaseUrl = g.getSchemaBaseUrl();
        loggingLevel = g.getLoggingLevel();
        verboseExceptions = g.isVerboseExceptions();

        if (g.getContact() != null) {
            contact = (ContactDTO) (g.getContact().clone());
        } else {
            contact = new ContactDTO();
        }
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
        return new GeoServerDTO(this);
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
        if ((obj == null) || !(obj instanceof GeoServerDTO)) {
            return false;
        }

        GeoServerDTO g = (GeoServerDTO) obj;
        boolean r = true;
        r = r && (maxFeatures == g.getMaxFeatures());
        r = r && (verbose == g.isVerbose());
        r = r && (numDecimals == g.getNumDecimals());

        if (charSet != null) {
            r = r && charSet.equals(g.getCharSet());
        } else if (g.getCharSet() != null) {
            return false;
        }

        r = r && (schemaBaseUrl == g.getSchemaBaseUrl());

        if (contact != null) {
            r = r && contact.equals(g.getContact());
        } else if (g.getContact() != null) {
            return false;
        }

        return r;
    }

    public int hashCode() {
        int i = 1;

        if (maxFeatures != 0) {
            i *= maxFeatures;
        }

        if (numDecimals != 0) {
            i *= numDecimals;
        }

        if (schemaBaseUrl != null) {
            i *= schemaBaseUrl.hashCode();
        }

        return i;
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
    public ContactDTO getContact() {
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
    public void setContact(ContactDTO contact) {
        if (contact == null) {
            contact = new ContactDTO();
        }

        this.contact = contact;
    }

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
        //init this now so the rest of the config has correct log levels.
        loggingLevel = level;
    }

    /**
     * Gets the user name of the administrator of GeoServer, for login
     * purposes.
     *
     * @return The administrator's password.
     */
    public String getAdminUserName() {
        return adminUserName;
    }

    /**
     * Sets the user name of the administrator of GeoServer, for login
     * purposes.
     *
     * @param username the String to set as the admin username.
     */
    public void setAdminUserName(String username) {
        this.adminUserName = username;
    }

    /**
     * Gets the password of the administrator of GeoServer, for login purposes.
     *
     * @return The password of the administrator.
     */
    public String getAdminPassword() {
        return adminPassword;
    }

    /**
     * Sets the password of the administrator of GeoServer, for login purposes.
     *
     * @param password The password to set as the login password.
     */
    public void setAdminPassword(String password) {
        this.adminPassword = password;
    }

    /**
     * Should we display stackTraces or not? (And give them a nice little
     * message instead?)
     *
     * @return Returns the showStackTraces.
     */
    public boolean isVerboseExceptions() {
        return verboseExceptions;
    }

    /**
     * If set to true, response exceptions will throw their stack trace back to
     * the end user.
     *
     * @param showStackTraces The showStackTraces to set.
     */
    public void setVerboseExceptions(boolean showStackTraces) {
        this.verboseExceptions = showStackTraces;
    }

    public String toString() {
        StringBuffer dto = new StringBuffer("[GeoServerDTO: \n");
        dto.append("   maxFeatures - " + maxFeatures);
        dto.append("\n   verbose - " + verbose);
        dto.append("\n   numDecimals - " + numDecimals);
        dto.append("\n   charSet - " + charSet);
        dto.append("\n   loggingLevel - " + loggingLevel);
        dto.append("\n   adminUserName - " + adminUserName);
        dto.append("\n   adminPassword - " + adminPassword);
        dto.append("\n   contact - " + contact);

        return dto.toString();
    }

}
