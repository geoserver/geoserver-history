 /* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */

package org.vfny.geoserver.config;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.io.FileInputStream;  
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException; 

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.DocumentHandler;
import org.xml.sax.SAXException;

import org.geotools.resources.Geotools;
import org.geotools.resources.Log4JFormatter;
import org.geotools.resources.MonolineFormatter;

/**
 * This class represents the global geoserver configuration options that
 * are not part of the Service element of a capabilities response.  They
 * are currently part of the same config file, but making a seperate class
 * allows us to easily seperate the elements out to different configuration
 * files in the future, such as for a WMS or WCS.  
 *
 * @author Chris Holmes, TOPP
 * @version $VERSION$
**/
public class WfsConfig implements java.io.Serializable {

    public static final String NAMESPACE_TAG = "Namespace";
    
    public static final String VERBOSE_TAG = "Verbose";

    /** Regular Expression to split values from spaces or commas */
    public static final String WHITE_SPACE_OR_COMMA = "[\\s,]+";

    public static final String ROOT_TAG = "GlobalConfiguration";

    public static final String PREFIX_ATTR = "prefix";

    public static final String URI_ATTR = "uri";
    
    public static final String DEFAULT_ATTR = "default";

    public static final String ONLINE_TAG = "OnlineResource";

    public static final String URL_TAG = "URL";

    public static final String MAX_TAG = "MaxFeatures";

    public static final String DEFAULT_PREFIX = "myns";

    public static final String LOG_TAG = "LoggingLevel";
    
    public static final String DELIMIT_TAG = "PrefixDelimiter";

    public static final String DEFAULT_PREFIX_DELIMITER = "--";

	
        /** Standard logging in stance for class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.config");

    private Level logLevel = Logger.getLogger("org.vfny.geoserver").getLevel();
    
    private int maxFeatures = 1000000;

    private String defaultPrefix;

    private String prefixDelimiter = DEFAULT_PREFIX_DELIMITER;
    
    private String baseUrl;
    
    /** The holds the mappings between prefixes and uri's*/
    private Map nameSpaces = new HashMap();

    /** determines whether spaces and line feeds should be added to output*/
    //put in wfs-config?  Maybe if we have some more config options.
    private boolean verbose = false;
    
    /**
     * Constructor.
     *
     */
    private WfsConfig(){
    } 

    /**
     * static factory, reads a WfsConfig from an xml file, using
     * the default root tag.
     *
     * @param configFile the path to the configuration file.
     * @return the WfsConfig object constructed from the xml elements
     * of the file.
     */
    public static WfsConfig getInstance(String configFile) 
    throws ConfigurationException {
	return getInstance(configFile, ROOT_TAG);
    }

    /**
     * static factory, reads a WfsConfig from an xml file, using
     * the passed in root tag.
     *
     * @param configFile the path to the configuration file.
     * @param rootTag the tag of the element whose children are the appropriate
     * configuration elements.
     * @return the WfsConfig object constructed from the xml elements
     * of the file.
     */
    public static WfsConfig getInstance(String configFile, String rootTag) 
	throws ConfigurationException{
	WfsConfig wfsConfig = new WfsConfig();
	try {
	    //FileInputStream fis = new FileInputStream(configFile);
	    FileReader fis = new FileReader(configFile);
	    LOGGER.info("got input reader, about to make input source");
	    InputSource in = new InputSource(fis);
	     DocumentBuilderFactory dfactory = new org.apache.xerces.jaxp.DocumentBuilderFactoryImpl();
	     //DocumentBuilderFactory dfactory = 
	     //DocumentBuilderFactory.newInstance();
	    dfactory.setNamespaceAware(true);
	    Document wfsDoc = dfactory.newDocumentBuilder().parse(in);
	    Element configElem = wfsDoc.getDocumentElement();
	    String configTag = configElem.getTagName();
	    LOGGER.finer("root tag is " + configTag);
	    if (!configTag.equals(rootTag)){
		configElem = 
		    (Element)configElem.getElementsByTagName(rootTag).item(0);
		if (configElem == null) {
		    String message = "could not find root tag: " + rootTag + 
			" in file: " + configFile;
		    LOGGER.warning(message);
		    //throw new ConfigurationException(message);
		    //instead of throwing exception, just search whole document.
		    configElem = wfsDoc.getDocumentElement();
		}
	    }
	
	    String logLevel = findTextFromTag(configElem, LOG_TAG);
	    wfsConfig.setLogLevel(logLevel);
	    // Level level = wfsConfig.getLogLevel(); 
	    //init this now so the rest of the config has correct log levels.
	    //Log4JFormatter.init("org.geotools", level);
	    // Log4JFormatter.init("org.vfny.geoserver", level);

	    Element verboseElem = 
		(Element) configElem.getElementsByTagName(VERBOSE_TAG).item(0);
	    if (verboseElem != null){
		if (!verboseElem.getAttribute("value").equals("false")){
		    wfsConfig.setVerbose(true);
		} else {
		    wfsConfig.setVerbose(false);
		}
	    } else {
		wfsConfig.setVerbose(false);
	    }

	    String baseUrl = findTextFromTag(configElem, URL_TAG);
	    wfsConfig.setBaseUrl(baseUrl);

	    wfsConfig.setMaxFeatures(findTextFromTag(configElem, MAX_TAG));
	    String delimiter = findTextFromTag(configElem, DELIMIT_TAG);
	    LOGGER.info("delimiter is " + delimiter);
	    wfsConfig.setFilePrefixDelimiter(delimiter);

	    //HashMap namespaces = new HashMap();
	    NodeList namespaceElems = 
		configElem.getElementsByTagName(NAMESPACE_TAG);
	    LOGGER.finer("namespaceElems is " + namespaceElems);
	    boolean newDefault = false;
	    for (int i = 0; i < namespaceElems.getLength(); i++){
		Element curNamespace = (Element) namespaceElems.item(i);
		LOGGER.finer("cur namespace = " + curNamespace);
		String prefix = curNamespace.getAttribute(PREFIX_ATTR);
		String uri = curNamespace.getAttribute(URI_ATTR);
		String defaultA = curNamespace.getAttribute(DEFAULT_ATTR);
		if (prefix == null || uri == null) {
		    String message = "All <Namespace> elements must contain" +
		    "both prefix and uri attributes.";
		    //REVISIT: if we don't throw exception then the user will 
		    //just not have all the namespaces he may have intended.
		    //throw new ConfigurationException(message);
		    LOGGER.warning(message);
		}
		LOGGER.config("adding namespace: " + prefix + ":" + uri);
		wfsConfig.addNamespace(prefix, uri);
		if (defaultA != null && defaultA.equals("true")){
		    LOGGER.config("setting default namespace to " + uri);
		    wfsConfig.setDefaultPrefix(prefix);
		    newDefault = true;
		}

	    }
	    if (!newDefault) {
		String defaultURI = ServiceConfig.findTextFromTag
		    (configElem, ServiceConfig.ONLINE_TAG);
		defaultURI += defaultURI.endsWith("/") ? "" : "/";
		defaultURI += DEFAULT_PREFIX;
		LOGGER.finest("adding uri " + defaultURI);
		wfsConfig.addNamespace(DEFAULT_PREFIX, defaultURI);
		wfsConfig.setDefaultPrefix(DEFAULT_PREFIX);
	    }
		//wfsConfig.setNamespaces(namespaces);
	    fis.close();

	  

	} catch (IOException ioe) {
	    String message = "problem reading file " + configFile  + "due to: "
		+ ioe.getMessage();
	    LOGGER.warning(message);
	    throw new ConfigurationException(message, ioe);
	} catch (ParserConfigurationException pce) {
	    String message = "trouble with parser to read xml, make sure class"
		+ "path is correct, reading file " + configFile;
	    LOGGER.warning(message);
	    throw new ConfigurationException(message, pce);
	} catch (SAXException saxe){
	    String message = "trouble parsing XML in " + configFile 
		+ ": " + saxe.getMessage();
	    LOGGER.warning(message);
	    throw new ConfigurationException(message, saxe);
	}
	return wfsConfig;
    }
    
    //TODO: put this in a common utility file, as 3 classes use it, and 
    //it's a bit confusing getting the ServiceConfig log messages.
    private static String findTextFromTag(Element root, String tag){
	return ServiceConfig.findTextFromTag(root, tag);
    }

    void setFilePrefixDelimiter(String delimiter){
	if (!delimiter.equals("")) {
	    LOGGER.config("Setting new file prefix delimiter: " + delimiter);
	    this.prefixDelimiter = delimiter;
	}
    }

    public String getFilePrefixDelimiter() {
	return this.prefixDelimiter;
    }

    /**
     * Sets the maximum number of features that should be returned by a 
     * getFeatures request.
     * 
     * @param max A string of the max features to set.  Should be parseable
     * to an integer.  If this method can not parse the string then it just
     * uses the default maxFeatures of this class.
     */
    void setMaxFeatures(String max){
	LOGGER.finest("setting max features with " + max);
	//if (!max.equals("")){
	    try {
		this.maxFeatures = Integer.parseInt(max);
		LOGGER.config("setting max features to " + max);
	    } catch (NumberFormatException e){
		LOGGER.finer("could not parse maxFeatures: " + max + ", " +
			       "using default: " + this.maxFeatures);
	    }
	    //}
    }

    /**
     * Gets the maximum number of features that should be returned by
     * a get features request.
     *
     * @return the max number of features to return.
     */
    public int getMaxFeatures(){
	return this.maxFeatures;
    }

    /**
     * Sets the logging level to be used by GeoServer.  Unlike the other
     * methods of this class, this method actually performs configuration
     * action.  This is so logging gets set as soon as possible, so the 
     * reporting for the rest of the configuration process occurs at the right
     * level of detail.  
     *
     * @param level a String representation of the level to set.  Can be any 
     * java.util.logging.Level string representation, including integers.  If
     * the level can not be parsed than the levels set in the jre 
     * logging.properties are used.
     */
    void setLogLevel(String level){
	try {
	    
	    logLevel = Level.parse(level);
	    LOGGER.config("setting LogLevel to " + logLevel);
	    Log4JFormatter.init("org.geotools", logLevel);
	     Log4JFormatter.init("org.vfny.geoserver", logLevel);
	} catch (IllegalArgumentException e){
	    LOGGER.config("could not parse LogLevel: " + level + ", using " +
			   "level: " + this.logLevel + ", found in " +
			   "logging.properties file in java home");
	}
    }

    /**
     * gets the logging level.
     * 
     * @return the level to be used for logging.
     */
    public Level getLogLevel(){
	return this.logLevel;
    }

    /** 
     * Returns true if the Verbose tag is present in the config file. 
     *
     * @return <tt>true</tt> if the verbose tag was present, <tt>false</tt> 
     * otherwise
     */
    public boolean isVerbose(){
	return verbose;
    }

    /**
     * sets the whether responses should have line feeds and nice formatting*/
    void setVerbose(boolean verbose){
	LOGGER.config("Output from GeoServer will " + (verbose ? "" : "not") +
		      "insert newlines and indents into return xml");
	this.verbose = verbose;
    }

    /** 
    * Gets the map of prefixes to namespace uris.  
    */
    Map getNamespaces()
    {
        return this.nameSpaces;
    }
    
    void addNamespace(String prefix, String namespace){
	nameSpaces.put(prefix, namespace);
    }

    /** 
     * Sets the map of prefixes to namespace uris.
     */
    void setNamespaces(Map nameSpaces){
	this.nameSpaces = nameSpaces;
    }

     /** 
    * Gets the base url of this wfs.  
    */
    public String getBaseUrl()
    {
        return this.baseUrl;
    }
    
    /** 
     * Sets the base url of this wfs.
     */
    void setBaseUrl(String baseUrl){
	this.baseUrl = baseUrl;
    }

    /**
     * retrieves the uri for the passed in prefix.
     */
    public String getUriFromPrefix(String prefix){
	return (String)nameSpaces.get(prefix);
    }

    void setDefaultPrefix(String prefix){
	this.defaultPrefix = prefix;
    }

    /**
     * gets the prefix to be used if no prefix is set/
     */
    public String getDefaultPrefix(){
	return this.defaultPrefix;
    }

    /**
     * Override of toString method. */
    public String toString() {
        StringBuffer returnString = new StringBuffer("\nWfsConfig:");
	returnString.append("\n   [verbose: " + verbose + "] ");
	returnString.append("\n   [namespaces: "); 
        Iterator i = nameSpaces.keySet().iterator();
        while(i.hasNext()) {
            String prefix = (String) i.next();
            returnString.append(prefix + "=" + nameSpaces.get(prefix));
	    if (i.hasNext()) returnString.append(", ");
        }
	returnString.append("] ");
	returnString.append("\n   [defaultPrefix: " + defaultPrefix + "] ");
        return returnString.toString();
    }


}
