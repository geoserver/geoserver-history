/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.Servlet;
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
public class ConfigInfo {

    /** Class logger */
    private static Logger LOG = Logger.getLogger("org.vfny.geoserver.config");
    /** Root directory of webserver */
    //private static final String ROOT_DIR = guessRootDir();


    /** Default name for feature type schemas */
    private static final String CONFIG_FILE =  "configuration.xml";
    /** Default name for feature type schemas */
    private static final String CONFIG_DIR =  "data/";
    /** Default name for feature type schemas */
    private static final String TYPE_DIR =  "featureTypes/";
    /** Default name for feature type schemas */
    private static final String CAP_DIR =  "capabilities/";
    /** Default name of feature type information */
    public static final String INFO_FILE = "info.xml" ;
    /** Default name for feature type schemas */
    public static final String SCHEMA_FILE = "schema.xml";
    /** Default name for zserver module mappings */
    public static final String GEO_MAP_FILE = "z3950-geo.map";

    /** */
    private static ConfigInfo config = null;

    private String rootDir;

    /** a Castor class to read internal configuration information */
    private GlobalConfiguration global = new GlobalConfiguration();
    /** Root directory for feature types */
    private String typeDir; //= ROOT_DIR + CONFIG_DIR + TYPE_DIR;
    /** Root directory of capabilities data */
    private String capabilitiesDir;// = ROOT_DIR + CONFIG_DIR + CAP_DIR;
    

    /**
     * Constructor that reads in configuration information from FreeFS 
     * configuration file.  This information is primarily used in the 
     * 'Service' section of the return document.
     */
    //private ConfigInfo() {
    //    global = readProperties(ROOT_DIR);
    //LOG.finer("empty constructor called");
    //}

    /**
     * Constructor that reads in configuration information from FreeFS 
     * configuration file.  This information is primarily used in the 
     * 'Service' section of the return document.
     *
     *@param rootDir the directory holding all the configuration information.
     */
    private ConfigInfo(String rootDir) {
	LOG.finer("constructor called with " + rootDir);
	this.rootDir = rootDir;
            setTypeDir(rootDir + TYPE_DIR);
	    setCapabilitiesDir(rootDir + CAP_DIR);        
	global = readProperties(rootDir + CONFIG_FILE);
	
    }
    
    /** Returns root webserver application directory 
     *
     * @return the configuration information for the geoserver.
     * @tasks TODO: make sure that getInstance(configDir) is called
     * before this is.  FreefsLog does so now, but this should check
     * to make sure that happens, throw an exception if it doesn't, as
     * the guessRootDir is only a guess.
     */
    public static ConfigInfo getInstance() { 
        if(config == null) {
	    LOG.finer("getInstance with configDir argument should be passed" + 
		      " in first!!");
            String configFile = guessRootDir() + CONFIG_DIR + CONFIG_FILE;
	    
	    config = new ConfigInfo(configFile);
        }
        return config;
    }    

    /** Returns root webserver application directory.  This should
     * always be called before the no argument getInstance, as that
     * method just attempts to guess where the configuration root
     * directory is.
     *
     * @param configDir the base directory of the configuration info.
     * @return the configuration information for that directory.
     */
    public static ConfigInfo getInstance(String configDir) { 
        LOG.finer("called get instance with file " + configDir);
	if(config == null) {
	    LOG.finest("creating new configInfo");
	    config = new ConfigInfo(configDir);
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


    /** Returns root webserver application directory 
     * @return a string of the root directory, only works for drop
     * in war, not for embedded.
     */
    private static String guessRootDir() { 
        return System.getProperty("user.dir") + "/webapps/geoserver/";
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
    public String getTypeDir() { LOG.finer("returning typeDir " + typeDir); return typeDir; }    
    /** Returns user-specified fees for this service */
    public void setTypeDir(String typeDir) { this.typeDir = typeDir; LOG.finer("setting typedir " + typeDir);}

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
