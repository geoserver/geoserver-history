/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */
package org.vfny.geoserver.config;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.Servlet;

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

    /** a DOM class to read service configuration information */
    private ServiceConfig serviceGlobal;
     /** a DOM class to read wfs global configuration information */
    private WfsConfig wfsGlobal;
    /** Root directory for feature types */
    private String typeDir; //= ROOT_DIR + CONFIG_DIR + TYPE_DIR;
    /** Root directory of capabilities data */
    private String capabilitiesDir;// = ROOT_DIR + CONFIG_DIR + CAP_DIR;
    

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
	if (!rootDir.endsWith("/")) rootDir += "/";
	wfsGlobal = readWfsTags(rootDir + CONFIG_FILE);
	serviceGlobal = readServiceTags(rootDir + CONFIG_FILE);

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

    /**
     * constructs a ServiceConfig object, which encapsulates the values
     * used for the Service section of a capabilities object.
     */
    private static ServiceConfig readServiceTags(String configFile) {
	ServiceConfig service = null;
	try {
	    service = ServiceConfig.getInstance(configFile);
	} catch (ConfigurationException ce){
	    LOG.warning("problem reading config file: " + ce.getMessage());
	} 
	return service;
    }

    /**
     * constructs a WfsConfig object, which encapsulates all the GeoServer
     * specific values in the configuration file.  Note that readServiceTags
     * and readWfsTags currently use the same config file, they just
     * parse out different parts of it.  This allows for those files to be
     * easily split up in later versions.
     */
    private static WfsConfig readWfsTags(String configFile) {
	WfsConfig wfs = null;
	try {
	    wfs = WfsConfig.getInstance(configFile);
	} catch (ConfigurationException ce){
	    LOG.warning("problem reading config file: " + ce.getMessage());
	} 
	return wfs;
    }

    /** Returns root webserver application directory 
     * @return a string of the root directory, only works for drop
     * in war, not for embedded.
     */
    private static String guessRootDir() { 
        return System.getProperty("user.dir") + "/webapps/geoserver/";
    }    
    

    /** Returns the user-specified title of this service */
    public String getTitle() { return serviceGlobal.getTitle(); }        
    /** Returns user-specified abstract for this service */
    public String getAbstract() { return serviceGlobal.getAbstract(); }    
    /** Returns user-specified keywords for this service  */
    public String getKeywords() { 
	StringBuffer keywords = new StringBuffer();
	Iterator keywordIter = serviceGlobal.getKeywords().iterator();
	while (keywordIter.hasNext()) {
	    keywords.append(keywordIter.next().toString());
	    if (keywordIter.hasNext()){
		keywords.append(", ");
	    }
	}
	return keywords.toString(); 
    }    
    /** Returns URL for this service */
    public String getOnlineResource(){ 
	return serviceGlobal.getOnlineResource(); 
    }
    /** Returns URL for this service */
    //REVIST: should this be different from onlineResource?  Re-add url field?
    //REVISIT: put getDescribeURL and getFeatureURL, ect.?  Would be good for
    //DescribeResponse with multiple types, to keep it consistent with 
    //capabilities.
    public String getUrl(){ return wfsGlobal.getBaseUrl(); }
    /** Returns user-specified fees for this service */
    public String getFees() { return serviceGlobal.getFees(); }
    /** Returns user-specified access constraints for this service */
    public String getAccessConstraints(){
	return serviceGlobal.getAccessConstraints();
    }
    public boolean formatOutput(){
	return wfsGlobal.isVerbose();
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

    public int getMaxFeatures() { return wfsGlobal.getMaxFeatures(); }

    public Level getLogLevel() { return wfsGlobal.getLogLevel(); }
    
    public String getFilePrefixDelimiter() { 
	return wfsGlobal.getFilePrefixDelimiter(); 
    }

    /**
     * gets the default namespace prefix.  This is really more for backwards
     * compatibility, as all feature type directories should now be named
     * with their correct prefix namespace.  This is the prefix that will
     * go on typenames that don't have a prefix.
     *
     * @return the prefix to use for those featureTypes that don't have
     * a properly prepended prefix.
     */
    public String getDefaultNSPrefix(){
	return wfsGlobal.getDefaultPrefix();
    }

    /**
     * Gets the namespace uri corresponding to passed in prefix, as set
     * in the configuration file.
     * 
     * @param prefix the prefix to retrieve the uri for.
     * @return the uri corresponding to the prefix.
     */
    public String getNSUri(String prefix){
	return wfsGlobal.getUriFromPrefix(prefix);
    }
 
    /**
     * gets the namespace declartion associated with this prefix.  
     * to go in the root element.
     *
     * @param prefix the internal prefix that is mapped to a uri.
     * @return the xmlns:---="http:..." type declaration, using this
     * prefix and its associated uri.
     */
    public String getXmlnsDeclaration(String prefix){
	return "xmlns:" + prefix + "=\"" + getNSUri(prefix) + "\"";
    }

    /**
     * gets all the xmlns declarations mapped in this ConfigInfo.
     * 
     * @return the array of xmlns declarations.
     */
    public String[] getAllXmlns(){
	Set prefixSet = wfsGlobal.getNamespaces().keySet();
	String[] retStrings = new String[prefixSet.size()];
	Iterator prefixIter = prefixSet.iterator();
	int i = 0;
	while(prefixIter.hasNext()){
	    retStrings[i++] = getXmlnsDeclaration((String)prefixIter.next());
	}
	return retStrings;
    }

    /** 
     * Returns the current time as a string
     * @param wfsName Name of the WFS
     */
    public String getServiceXml(String wfsName) {
           
	return serviceGlobal.getWfsXml();
	
    }
}
