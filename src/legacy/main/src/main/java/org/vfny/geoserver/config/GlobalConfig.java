/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import java.nio.charset.Charset;

import org.vfny.geoserver.global.GeoServer;
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
 * @version $Id$
 */
public class GlobalConfig {
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
     * Default is true
     */
    private boolean verbose = true;

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
    private int numDecimals = 8;

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
    private Charset charSet;

    /**
     * The base URL where this servlet will run.
     *
     * <p>
     * If running locally then <code>http://localhost:8080</code> (or whatever
     * port you're running on) should work.
     * </p>
     *
     * <p>
     * If you are serving to the world then this must be the location where the
     * geoserver servlets appear
     * </p>
     *
     * <p>
     * JG - can we figure this out at runtime?
     * </p>
     */
    private String proxyBaseUrl;

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
     * 
     * See http://geoserver.org/display/GEOS/GSIP+13+-+Logging
     * for how this is now implemented.
     * </p>
     */
    private String log4jConfigFile = null;
    private boolean suppressStdOutLogging = false;
    /** custom log location **/
    private String logLocation = null;
    
    
    private String adminUserName;
    private String adminPassword;

    /** Whether the exceptions returned to the client should contain full stack traces */
    private boolean verboseExceptions;

    /** The Server contact person and their contact information. */
    private ContactConfig contact = null;

    private double jaiMemoryCapacity;
    private double jaiMemoryThreshold;
    private int jaiTileThreads;
    private int jaiTilePriority;
    private boolean jaiRecycling;
    private boolean imageIOCache;
    private boolean jaiJPEGNative;
    private boolean jaiPNGNative;

    /** tile cache location, full url or relative path */
    private String tileCache;
    
    private int updateSequence;

	private boolean jaiMosaicNative;

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
        charSet = Charset.forName("UTF-8");
        proxyBaseUrl = null;
        schemaBaseUrl = null;
        contact = null;
        verboseExceptions = true;
        logLocation = null;
    }

    /**
     * Instantiates the global config from the geoServer module.
     *
     * @param geoserver The geoServer module.
     */
    public GlobalConfig(GeoServer geoserver) {
        this((GeoServerDTO) geoserver.toDTO());
    }

    /**
     * GlobalConfig constructor.
     *
     * <p>
     * Creates a copy of the GeoServerDTO object provided.  Charset is not
     * cloned, everything else is.
     * </p>
     *
     * @param g
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public GlobalConfig(GeoServerDTO g) {
        if (g == null) {
            throw new NullPointerException();
        }

        maxFeatures = g.getMaxFeatures();
        verbose = g.isVerbose();
        numDecimals = g.getNumDecimals();
        charSet = g.getCharSet();
        proxyBaseUrl = g.getProxyBaseUrl();
        schemaBaseUrl = g.getSchemaBaseUrl();
        adminUserName = g.getAdminUserName();
        adminPassword = g.getAdminPassword();
        verboseExceptions = g.isVerboseExceptions();

        log4jConfigFile = g.getLog4jConfigFile();
        suppressStdOutLogging = g.getSuppressStdOutLogging();
        logLocation = g.getLogLocation();

        jaiMemoryCapacity = g.getJaiMemoryCapacity();
        jaiMemoryThreshold = g.getJaiMemoryThreshold();
        jaiTileThreads = g.getJaiTileThreads();
        jaiTilePriority = g.getJaiTilePriority();
        jaiRecycling = g.getJaiRecycling().booleanValue();
        imageIOCache = g.getImageIOCache().booleanValue();
        jaiJPEGNative = g.getJaiJPEGNative().booleanValue();
        jaiPNGNative = g.getJaiPNGNative().booleanValue();
        jaiMosaicNative=g.getJaiMosaicNative();
        tileCache = g.getTileCache();
        
        updateSequence = g.getUpdateSequence();

        if (g.getContact() != null) {
            contact = new ContactConfig(g.getContact());
        } else {
            contact = new ContactConfig();
        }
    }

    /**
     * Implement updateDTO.
     *
     * <p>
     * Populates this instance with the GeoServerDTO object provided.
     * </p>
     *
     * @param g A valid GeoServerDTO object to populate this object from
     *
     * @throws NullPointerException DOCUMENT ME!
     *
     * @see org.vfny.geoserver.config.DataStructure#updateDTO(java.lang.Object)
     */
    public void update(GeoServerDTO g) {
        if (g == null) {
            throw new NullPointerException("GeoServer Data Transfer Object required");
        }

        maxFeatures = g.getMaxFeatures();
        verbose = g.isVerbose();
        numDecimals = g.getNumDecimals();
        charSet = g.getCharSet();
        schemaBaseUrl = g.getSchemaBaseUrl();
        proxyBaseUrl = g.getProxyBaseUrl();

        verboseExceptions = g.isVerboseExceptions();

        log4jConfigFile = g.getLog4jConfigFile();
        suppressStdOutLogging = g.getSuppressStdOutLogging();
        logLocation = g.getLogLocation();

        jaiMemoryCapacity = g.getJaiMemoryCapacity();
        jaiMemoryThreshold = g.getJaiMemoryThreshold();
        jaiTileThreads = g.getJaiTileThreads();
        jaiTilePriority = g.getJaiTilePriority();
        jaiRecycling = g.getJaiRecycling().booleanValue();
        imageIOCache = g.getImageIOCache().booleanValue();
        jaiJPEGNative = g.getJaiJPEGNative().booleanValue();
        jaiPNGNative = g.getJaiPNGNative().booleanValue();

        tileCache = g.getTileCache();
        
        updateSequence = g.getUpdateSequence();

        if (g.getContact() != null) {
            contact = new ContactConfig(g.getContact());
        } else {
            contact = new ContactConfig();
        }
    }

    /**
     * Implement toDTO.
     *
     * <p>
     * Creates a copy of the data in a GeoServerDTO representation
     * </p>
     *
     * @return a copy of the data in a GeoServerDTO representation
     *
     * @see org.vfny.geoserver.config.DataStructure#toDTO()
     */
    public GeoServerDTO toDTO() {
        GeoServerDTO g = new GeoServerDTO();
        g.setMaxFeatures(maxFeatures);
        g.setVerbose(verbose);
        g.setAdminPassword(adminPassword);
        g.setAdminUserName(adminUserName);
        g.setNumDecimals(numDecimals);
        g.setCharSet(charSet);
        g.setSchemaBaseUrl(schemaBaseUrl);
        g.setVerboseExceptions(verboseExceptions);
        g.setContact((ContactDTO) contact.toDTO());
        g.setLog4jConfigFile(log4jConfigFile);
        g.setSuppressStdOutLogging(suppressStdOutLogging);
        g.setLogLocation(logLocation);
        g.setJaiMemoryCapacity(jaiMemoryCapacity);
        g.setJaiMemoryThreshold(jaiMemoryThreshold);
        g.setJaiTileThreads(jaiTileThreads);
        g.setJaiTilePriority(jaiTilePriority);
        g.setJaiRecycling(Boolean.valueOf(jaiRecycling));
        g.setImageIOCache(Boolean.valueOf(imageIOCache));
        g.setJaiJPEGNative(Boolean.valueOf(jaiJPEGNative));
        g.setJaiMosaicNative(jaiMosaicNative);
        g.setJaiPNGNative(Boolean.valueOf(jaiPNGNative));
        g.setProxyBaseUrl(proxyBaseUrl);
        g.setTileCache(tileCache);
        g.setUpdateSequence(updateSequence);

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
    public String getProxyBaseUrl() {
        return proxyBaseUrl;
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
    public void setProxyBaseUrl(String url) {
        proxyBaseUrl = url;
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
    public String getLog4jConfigFile() {
        return log4jConfigFile;
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
    public void setLog4jConfigFile(String s) {
        log4jConfigFile = s;
    }

    /**
     * Gets the user name of the administrator.
     *
     * @return The user name to be checked for on login.
     */
    public String getAdminUserName() {
        return adminUserName;
    }

    /**
     * Gets the password of the administrator.
     *
     * @return The password to be checked for on login.
     */
    public String getAdminPassword() {
        return adminPassword;
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

    /**
     * @return The string representation of the path on disk in which the
     * server logs to.
     */
    public String getLogLocation() {
        return logLocation;
    }

    /**
     * @param logLocation The string representation of the path on disk in which
     * the server logs to.
     */
    public void setLogLocation(String logLocation) {
        this.logLocation = logLocation;
    }

    /**
     * @return True if the server is logging to file, otherwise false.
     */
    public boolean getSuppressStdOutLogging() {
        return suppressStdOutLogging;
    }

    /**
     * Toggles server logging to file.
     */
    public void setSuppressStdOutLogging(boolean b) {
        this.suppressStdOutLogging = b;
    }

    public double getJaiMemoryCapacity() {
        return jaiMemoryCapacity;
    }

    public void setJaiMemoryCapacity(double jaiMemoryCapacity) {
        this.jaiMemoryCapacity = jaiMemoryCapacity;
    }

    public boolean isJaiRecycling() {
        return jaiRecycling;
    }

    public void setJaiRecycling(boolean jaiRecycling) {
        this.jaiRecycling = jaiRecycling;
    }

    public boolean isImageIOCache() {
        return imageIOCache;
    }

    public void setImageIOCache(boolean imageIOCache) {
        this.imageIOCache = imageIOCache;
    }

    public boolean isJaiJPEGNative() {
        return jaiJPEGNative;
    }

    public void setJaiJPEGNative(boolean jaiJPEGNative) {
        this.jaiJPEGNative = jaiJPEGNative;
    }

    public boolean isJaiPNGNative() {
        return jaiPNGNative;
    }

    public void setJaiPNGNative(boolean jaiPNGNative) {
        this.jaiPNGNative = jaiPNGNative;
    }

    public double getJaiMemoryThreshold() {
        return jaiMemoryThreshold;
    }

    public void setJaiMemoryThreshold(double jaiMemoryThreshold) {
        this.jaiMemoryThreshold = jaiMemoryThreshold;
    }

    public int getJaiTilePriority() {
        return jaiTilePriority;
    }

    public void setJaiTilePriority(int jaiTilePriority) {
        this.jaiTilePriority = jaiTilePriority;
    }

    public int getJaiTileThreads() {
        return jaiTileThreads;
    }

    public void setJaiTileThreads(int jaiTileThreads) {
        this.jaiTileThreads = jaiTileThreads;
    }

    /**
     * tile cache parameter
     * @see GeoServer#getTileCache()
     */
    public String getTileCache() {
        return tileCache;
    }

    public void setTileCache(String tileCache) {
        this.tileCache = tileCache;
    }

	/**
	 * @return the updateSequence
	 */
	public int getUpdateSequence() {
		return updateSequence;
	}

	/**
	 * @param updateSequence the updateSequence to set
	 */
	public void setUpdateSequence(int updateSequence) {
		this.updateSequence = updateSequence;
	}

	public Boolean getJaiMosaicNative() {
		return jaiMosaicNative;
	}

	public void setJaiMosaicNative(Boolean jaiMosaicNative) {
		this.jaiMosaicNative = jaiMosaicNative;
	}

	public void setJaiMosaicNative(boolean jaiMosaicNative) {
		this.jaiMosaicNative = jaiMosaicNative;
	}
}
