/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.exolab.castor.xml.*;
import org.vfny.geoserver.config.configuration.*;

/**
 * Reads all necessary configuration data and abstracts it away from the 
 * response servlets.
 * 
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
public class ConfigurationBean {

    /** Class logger */
    private static Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.config");

    /** A Castor-generated class to read internal configuration information */
    private GlobalConfiguration configuration = new GlobalConfiguration();
    
    /** Root directory of webserver */
    private static final String ROOT_DIR = findRootDirectory();
    
    /** Root directory for feature types */
    private static final String FEATURE_TYPE_DIR = ROOT_DIR + 
        "/data/featureTypes/";
    
    /** Root directory of capabilities data */
    private static final String CAPABILITIES_DIR = ROOT_DIR + 
        "/data/capabilities/";
    
    /** Global configuration filename */
    private static final String CONFIG_FILE = ROOT_DIR + 
        "/data/configuration.xml";
    
    /** Default name of feature type information */
    private static final String FEATURE_TYPE_INFO_NAME = "info";
    
    /** Default name for feature type schemas */
    private static final String FEATURE_TYPE_SCHEMA_NAME = "schema";
    

    /**
     * Constructor that reads in configuration information from FreeFS 
     * configuration file.  This information is primarily used in the 
     * 'Service' section of the return document.
     */
    public ConfigurationBean() {        
        try {
            FileReader featureTypeDocument = new FileReader(CONFIG_FILE );
            configuration = (GlobalConfiguration) Unmarshaller.
                unmarshal(GlobalConfiguration.class, featureTypeDocument);
        }
        catch( FileNotFoundException e ) {
            LOGGER.finest("Configuration file does not exist: " + 
                        CONFIG_FILE);
        }
        catch( MarshalException e ) {
            LOGGER.finest("Castor could not unmarshal configuration file: " + 
                        CONFIG_FILE);
            LOGGER.finest("Castor says: " + e.toString() );
        }
        catch( ValidationException e ) {
            LOGGER.finest("Castor says the config file isn't valid XML: "
                        + CONFIG_FILE);
            LOGGER.finest("Castor says: " + e.toString() );
        }        
    }
    

    /** Returns root webserver application directory */
    private static String findRootDirectory() { 
        return System.getProperty("user.dir") + "/webapps/geoserver";
    }    
    
    /** Returns the user-specified title of this service */
    public String getTitle() { return configuration.getTitle(); }
    
    
    /** Returns user-specified abstract for this service */
    public String getAbstract() { return configuration.getAbstract(); }
    
    
    /** Returns user-specified keywords for this service  */
    public String getKeywords() { return configuration.getKeywords(); }
    
    
    /** Returns URL for this service */
    public String getOnlineResource() { 
        return configuration.getOnlineResource();
    }
    
    
    /** Returns user-specified fees for this service */
    public String getFees() { return configuration.getFees(); }
    
    
    /** Returns user-specified access constraints for this service */
    public String getAccessConstraints() { 
        return configuration.getAccessConstraints();
    }
    
    /** 
     * Returns the current time as a string
     */
    public String getCurrentTime() {  
        Date now = new Date();
        return now.toString();
    }
    
    
    /** Returns user-specified maintainer for this service */
    public String getMaintainer() { return configuration.getMaintainer(); }

    
    /** Returns fixed version number for this service */
    public String getFreeFsVersion() { return "0.9b"; }
    
    
    /** Returns user-specified URL for this service */
    public String getUrl() { return configuration.getURL(); }
    
    
    /** Returns user-specified fees for this service */
    public String getFeatureTypeDirectory() { return FEATURE_TYPE_DIR; }
    
    
    /** Returns default feature type information name */
    public String getFeatureTypeInfoName() { return FEATURE_TYPE_INFO_NAME; }
    
    
    /** Returns root capabilities directory for this service */
    public String getCapabilitiesDirectory() { return CAPABILITIES_DIR; }
    
    
    /** Returns default feature type schema name */
    public String getFeatureTypeSchemaName() { 
        return FEATURE_TYPE_SCHEMA_NAME;
    }
    
    
    /** 
     * Returns the current time as a string
     * @param wfsName Name of the WFS
     */
    public String getServiceXml(String wfsName) {
            
        // SHOULD CHANGE THIS TO STRINGBUFFER
        String tempResponse = new String();
        
        // Set service section of Response, based on Configuration input
        tempResponse = tempResponse + "  <Service>\n";
        tempResponse = tempResponse + "    <Name>" + wfsName + "</Name>\n";
        tempResponse = tempResponse + "    <Title>" + getTitle() + 
            "</Title>\n";
        tempResponse = tempResponse + "    <Abstract>" + getAbstract() + 
            "</Abstract>\n";
        tempResponse = tempResponse + "    <Keywords>" + getKeywords() + 
            "</Keywords>\n";
        tempResponse = tempResponse + "    <OnlineResource>" + 
            getOnlineResource() + "</OnlineResource>\n";
        tempResponse = tempResponse + "    <Fees>" + getFees() + "</Fees>\n";
        tempResponse = tempResponse + "    <AccessConstraints>" + 
            getAccessConstraints() + "</AccessConstraints>\n";
        tempResponse = tempResponse + "  </Service>";
        
        // Concatenate into XML output stream
        return tempResponse;
    }
}
