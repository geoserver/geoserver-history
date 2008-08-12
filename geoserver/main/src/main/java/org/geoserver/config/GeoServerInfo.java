/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config;

import java.io.Serializable;
import java.util.Map;

/**
 * Global GeoServer configuration.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public interface GeoServerInfo {

    /**
     * Identifier.
     */
    String getId();

    /**
     * The image configuration.
     * 
     * @uml.property name="imaging"
     * @uml.associationEnd inverse="geoServer:org.geoserver.config.ImagingInfo"
     */
    //ImagingInfo getImaging();

    /**
     * Sets the image configuration.
     * 
     * @param imaging
     *                The imaging to set.
     * @uml.property name="imaging"
     */
    //void setImaging(ImagingInfo imaging);

    /**
     * The contact information.
     * 
     * @uml.property name="contactInfo"
     * @uml.associationEnd inverse="geoServer:org.geoserver.config.ContactInfo"
     */
    ContactInfo getContact();

    /**
     * Sets the contact information.
     * 
     * @param contactInfo
     *                The contactInfo to set.
     * @uml.property name="contactInfo"
     */
    void setContact(ContactInfo contactInfo);

    /**
     * The default character set.
     * 
     * @uml.property name="charset"
     */
    String getCharset();

    /**
     * Sets the default character set.
     * 
     * @uml.property name="charset"
     */
    void setCharset(String charset);

    /**
     * Sets the administrator username.
     */
    String getAdminUsername();
    
    /**
     * The administrator username.
     *
     */
    void setAdminUsername( String adminUsername );
    
    /**
     * The administrator password.
     */
    String getAdminPassword();
   
    /**
     * Sets the administrator password.
     */
    void setAdminPassword( String adminPassword );
    
    /**
     * The title of the GeoServer instance.
     * 
     * @uml.property name="title"
     */
    String getTitle();

    /**
     * Sets the title of the GeoServer instance.
     * .
     * @uml.property name="title"
     */
    void setTitle(String title);

    /**
     * A global cap on the number of decimals to use when encoding floating 
     * point numbers.
     * 
     * @uml.property name="numDecimals"
     */
    int getNumDecimals();

    /**
     * Sets the global cap on the number of decimals to use when encoding floating 
     * point numbers.
     * @uml.property name="numDecimals"
     */
    void setNumDecimals(int numDecimals);

    /**
     * TODO: not sure what this is supposed to do.
     */
    String getOnlineResource();
    void setOnlineResource(String onlineResource);

    /**
     * The url of a proxy in front of the GeoServer instance.
     * <p>
     * This value is used when a reference back to the GeoServer instance must 
     * be made in a response.
     * </p>
     * @uml.property name="proxyBaseUrl"
     */
    String getProxyBaseUrl();

    /**
     * Sets The url of a proxy in front of the GeoServer instance.
     * @uml.property name="proxyBaseUrl"
     */
    void setProxyBaseUrl(String proxyBaseUrl);

    /**
     * The base url to use when including a reference to an xml schema document 
     * in a response.
     * @uml.property name="schemaBaseUrl"
     */
    String getSchemaBaseUrl();

    /**
     * Sets the base url to use when including a reference to an xml schema document 
     * in a response.
     * @uml.property name="schemaBaseUrl"
     */
    void setSchemaBaseUrl(String schemaBaseUrl);

    /**
     * Verbosity flag.
     * <p>
     * When set GeoServer will log extra information it normally would not.
     * </p>
     * @uml.property name="verbose"
     */
    boolean isVerbose();

    /**
     * Sets verbosity flag.
     * @uml.property name="verbose"
     */
    void setVerbose(boolean verbose);

    /**
     * Verbosity flag for exceptions.
     * <p>
     * When set GeoServer will include full stack traces for exceptions.
     * </p>
     * @uml.property name="verboseExceptions"
     */
    boolean isVerboseExceptions();

    /**
     * Sets verbosity flag for exceptions.
     * @uml.property name="verboseExceptions"
     */
    void setVerboseExceptions(boolean verboseExceptions);

    /**
     * The GeoServer logging level.
     * <p>
     * The name of the level is specific to GeoServer, and independent of the 
     * actual logging framework.
     * </p>
     */
    String getLoggingLevel();
    
    /**
     * Sets the logging level.
     */
    void setLoggingLevel( String loggingLevel );
    
    /**
     * The location where GeoServer logs to.
     * <p>
     * This value is intended to be used by adminstrators who require logs to be
     * written in a particular location.
     * </p>
     */
    String getLoggingLocation();
    
    /**
     * Sets the logging location.
     * 
     * @param loggingLocation A file or url to a location to log.
     */
    void setLoggingLocation( String loggingLocation );
    
    /**
     * Flag indicating if GeoServer logs to stdout.
     */
    boolean isStdOutLogging();
    
    /**
     * Sets stdout logging flag.
     */
    void setStdOutLogging( boolean supressStdOutLogging );
    
    /**
     * The update sequence.
     * <p>
     * This value is used by various ogc services to track changes to a capabilities
     * document.
     * </p>
     */
    int getUpdateSequence();
    
    /**
     * Sets the update sequence.
     */
    void setUpdateSequence( int updateSequence );
    
    /**
     * A map of metadata for services.
     *
     * @uml.property name="metadata"
     */
    Map<String, Serializable> getMetadata();

    /**
     * Client properties for services.
     * <p>
     * These values are transient, and not persistent.
     * </p>
     */
    Map<Object, Object> getClientProperties();
    
    /**
     * Disposes the global configuration object.
     */
    void dispose();
}
