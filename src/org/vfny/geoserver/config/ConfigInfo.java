/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.logging.Logger;
import java.util.Iterator;
import javax.servlet.ServletContext;
import javax.servlet.Servlet;
//import org.exolab.castor.xml.Unmarshaller;
//import org.exolab.castor.xml.MarshalException;
//import org.exolab.castor.xml.ValidationException;
//import org.vfny.geoserver.config.configuration.GlobalConfiguration;

/**
 * Reads all necessary configuration data and abstracts it away from the 
 * response servlets.
 * 
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
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
    private ServiceConfig global;
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


    private static ServiceConfig readProperties(String configFile) {
	ServiceConfig global = null;
	try {
	    global = ServiceConfig.getInstance(configFile);
	} catch (ConfigurationException ce){
	    LOG.warning("problem reading config file: " + ce.getMessage());
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
    public String getKeywords() { 
	StringBuffer keywords = new StringBuffer();
	Iterator keywordIter = global.getKeywords().iterator();
	while (keywordIter.hasNext()) {
	    keywords.append(keywordIter.next().toString());
	    if (keywordIter.hasNext()){
		keywords.append(", ");
	    }
	}
	return keywords.toString(); 
    }    
    /** Returns URL for this service */
    public String getOnlineResource(){ return global.getOnlineResource(); }
    /** Returns URL for this service */
    //REVIST: should this be different from onlineResource?  Re-add url field?
    public String getUrl(){ return global.getOnlineResource(); }
    /** Returns user-specified fees for this service */
    public String getFees() { return global.getFees(); }
    /** Returns user-specified access constraints for this service */
    public String getAccessConstraints(){return global.getAccessConstraints();}

    public boolean formatOutput(){
	return global.isVerbose();
    }
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
           
	return global.getWfsXml();
	/* StringBuffer tempResponse = new StringBuffer();
        
        // Set service section of Response, based on Configuration input
        tempResponse.append("  <Service>\n");
        tempResponse.append("    <Name>" + wfsName + "</Name>\n");
        tempResponse.append("    <Title>" + getTitle() +
            "</Title>\n");
        tempResponse.append("    <Abstract>" + getAbstract() + 
            "</Abstract>\n");
        tempResponse.append("    <Keywords>" + getKeywords() + 
            "</Keywords>\n");
        tempResponse.append("    <OnlineResource>" + 
            getOnlineResource() + "</OnlineResource>\n");
        tempResponse.append("    <Fees>" + getFees() + "</Fees>\n");
        tempResponse.append("    <AccessConstraints>" + 
            getAccessConstraints() + "</AccessConstraints>\n");
        tempResponse.append("  </Service>\n");
        
        // Concatenate into XML output stream
        return tempResponse.toString();
	*/
    }
}
