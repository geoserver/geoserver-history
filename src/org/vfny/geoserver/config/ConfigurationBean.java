/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.vfny.geoserver.config.configuration.GlobalConfiguration;

/**
 * Reads all necessary configuration data and abstracts it away from the 
 * response servlets.
 * 
 * @author Rob Hranac, TOPP
 * @version $VERSION$
 */
public class ConfigurationBean {

    /** Class logger */
    private static Logger LOG = Logger.getLogger("org.vfny.geoserver.config");
    /** Root directory of webserver */
    private static final String ROOT_DIR = guessRootDir();
    /** Default name for feature type schemas */
    private static final String CONFIG_FILE =  "/configuration.xml";
    /** Default name for feature type schemas */
    private static final String CONFIG_DIR =  "/data";
    /** Default name for feature type schemas */
    private static final String TYPE_DIR =  "/featureTypes";
    /** Default name for feature type schemas */
    private static final String CAP_DIR =  "/capabilities";
    /** Default name of feature type information */
    public static final String TYPE_INFO = "info";    
    /** Default name for feature type schemas */
    public static final String TYPE_SCHEMA = "schema";

    /** */
    private static ConfigurationBean config = null;

    /** a Castor class to read internal configuration information */
    private GlobalConfiguration global = new GlobalConfiguration();
    /** Root directory for feature types */
    private String typeDir = ROOT_DIR + CONFIG_DIR + TYPE_DIR;
    /** Root directory of capabilities data */
    private String capabilitiesDir = ROOT_DIR + CONFIG_DIR + CAP_DIR;
    

    /**
     * Constructor that reads in configuration information from FreeFS 
     * configuration file.  This information is primarily used in the 
     * 'Service' section of the return document.
     */
    private ConfigurationBean() {
        global = readProperties(ROOT_DIR);
    }


    private ConfigurationBean(String rootDir) {
        global = readProperties(rootDir);
    }
    
    /** Returns root webserver application directory */
    public static ConfigurationBean getInstance() { 
        if(config == null) {
            String configFile = ROOT_DIR + CONFIG_DIR + CONFIG_FILE;
            config = new ConfigurationBean(configFile);
        }
        return config;
    }    

    /** Returns root webserver application directory */
    public static ConfigurationBean getInstance(String configFile) { 
        if(config == null) {
            config = new ConfigurationBean(configFile);
        }
        return config;
    }    


    private static GlobalConfiguration readProperties(String configFile) {
        GlobalConfiguration global = null;
        try {
            FileReader featureTypeDocument = new FileReader(configFile);
            global = (GlobalConfiguration) Unmarshaller.
                unmarshal(GlobalConfiguration.class, featureTypeDocument);
        }
        catch(FileNotFoundException e) {
            LOG.finest("Configuration file does not exist: " + configFile);
        }
        catch(MarshalException e) {
            LOG.finest("Castor could not unmarshal configuration file: " + 
                        configFile);
        }
        catch(ValidationException e) {
            LOG.finest("Castor says the config file isn't valid XML: "
                        + configFile);
        }        
        return global;
    } 


    /** Returns root webserver application directory */
    private static String guessRootDir() { 
        return System.getProperty("user.dir") + "/webapps/geoserver";
    }    
    

    /** Returns the user-specified title of this service */
    public String getTitle() { return global.getTitle(); }        
    /** Returns user-specified abstract for this service */
    public String getAbstract() { return global.getAbstract(); }    
    /** Returns user-specified keywords for this service  */
    public String getKeywords() { return global.getKeywords(); }    
    /** Returns URL for this service */
    public String getOnlineResource(){ return global.getOnlineResource(); }
    /** Returns user-specified fees for this service */
    public String getFees() { return global.getFees(); }
    /** Returns user-specified access constraints for this service */
    public String getAccessConstraints(){return global.getAccessConstraints();}
    /** Returns user-specified maintainer for this service */
    public String getMaintainer() { return global.getMaintainer(); }
    /** Returns user-specified URL for this service */
    public String getUrl() { return global.getURL(); }

    /** Returns fixed version number for this service */
    public String getFreeFsVersion() { return "0.9b"; }        
    /** Returns the current time as a string. */
    public String getCurrentTime() { return (new Date()).toString(); }
            

    /** Returns user-specified fees for this service */
    public String getTypeDir() { return typeDir; }    
    /** Returns user-specified fees for this service */
    public void setTypeDir(String typeDir) { this.typeDir = typeDir; }

    /** Returns root capabilities directory for this service */
    public String getCapabilitiesDir() { return capabilitiesDir; }
    /** Returns user-specified fees for this service */
    public void setCapabilitiesDir(String capabilitiesDir) { 
        this.capabilitiesDir = capabilitiesDir;
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
