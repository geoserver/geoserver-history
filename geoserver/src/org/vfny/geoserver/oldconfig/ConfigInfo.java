/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.oldconfig;

import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataSourceFinder;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoServer;

/**
 * Reads all necessary configuration data and abstracts it away from the
 * response servlets.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $VERSION$
 */
public class ConfigInfo
{
  /** Class logger */
  private static Logger LOGGER = Logger.getLogger("org.vfny.geoserver.config");

  /** Default name for feature type schemas */
  private static final String CONFIG_FILE = "configuration.xml";

  /** Default name for feature type schemas */
  private static final String CONFIG_DIR = "data/";

  /** Default name for feature type schemas */
  private static final String TYPE_DIR = "featureTypes/";

  /** Default name for feature type schemas */
  private static final String CAP_DIR = "capabilities/";

  /** Default name of feature type information */
  public static final String INFO_FILE = "info.xml";

  /** Default name for feature type schemas */
  public static final String SCHEMA_FILE = "schema.xml";

  /** Default name for zserver module mappings */
  public static final String GEO_MAP_FILE = "z3950-geo.map";

  /** */
  private static ConfigInfo config = null;
  private String rootDir;

  /** a DOM class to read service configuration information */
  private ServiceConfigOld serviceGlobal;

  /** a DOM class to read wfs global configuration information */
  private OldWfsConfig wfsGlobal;

  /** a DOM class to read wfs global configuration information */
  private ZServerConfig zGlobal;

  /** Root directory for feature types */
  private String typeDir; //= ROOT_DIR + CONFIG_DIR + TYPE_DIR;

  /** Root directory of capabilities data */
  private String capabilitiesDir; // = ROOT_DIR + CONFIG_DIR + CAP_DIR;

  /**
   * Constructor that reads in configuration information from FreeFS
   * configuration file.  This information is primarily used in the
   * 'ServiceConfig' section of the return document.
   *
   * @param rootDir the directory holding all the configuration information.
   */
  private ConfigInfo(String rootDir)
  {
    LOGGER.finer("constructor called with " + rootDir);
    this.rootDir = rootDir;
    setTypeDir(rootDir + TYPE_DIR);
    setCapabilitiesDir(rootDir + CAP_DIR);

    if (!rootDir.endsWith("/")) {
      rootDir += "/";
    }

    String cfgFile = rootDir + CONFIG_FILE;
    wfsGlobal = readWfsTags(cfgFile);
    serviceGlobal = readServiceTags(cfgFile);

    Iterator iter = DataSourceFinder.getAvailableDataSources();

    while (iter.hasNext()) {
      LOGGER.finer(iter.next() + " is an available DataSource");
    }
  }

  /**
   * Returns root webserver application directory
   *
   * @return the configuration information for the geoserver.
   *
   * @task TODO: make sure that getInstance(configDir) is called before this
   *       is.  FreefsLog does so now, but this should check to make sure
   *       that happens, throw an exception if it doesn't, as the
   *       guessRootDir is only a guess.
   */
  public static ConfigInfo getInstance()
  {
	geoServer = null;
    if (config == null) {
      LOGGER.finer("getInstance with configDir argument should be passed"
                   + " in first!!");

      String configFile = guessRootDir() + CONFIG_DIR + CONFIG_FILE;

      config = new ConfigInfo(configFile);
    }

    return config;
  }
	private static GeoServer geoServer = null;
  /**
   * Returns root webserver application directory.  This should always be
   * called before the no argument getInstance, as that method just attempts
   * to guess where the configuration root directory is.
   *
   * @param configDir the base directory of the configuration info.
   *
   * @return the configuration information for that directory.
   */
  public static ConfigInfo getInstance(String configDir, GeoServer gs)
  {
  	geoServer = gs;
    LOGGER.finer("called get instance with file " + configDir);

    if (config == null) {
      LOGGER.finest("creating new configInfo");
      config = new ConfigInfo(configDir);
    }

    return config;
  }
  
  public GeoServer getGeoServer(){
  	return geoServer;
  }

  /**
   * constructs a ServiceConfig object, which encapsulates the values used
   * for the ServiceConfig section of a capabilities object.
   *
   * @param configFile The file that contains the service tags.
   *
   * @return The singleton java object to query for service information.
   */
  private static ServiceConfigOld readServiceTags(String configFile)
  {
    ServiceConfigOld service = null;

    try {
      service = ServiceConfigOld.getInstance(configFile);
    }
    catch (ConfigurationException ce) {
      LOGGER.warning("problem reading config file: " + ce.getMessage());
    }

    return service;
  }

  /**
   * constructs a WfsConfig object, which encapsulates all the GeoServer
   * specific values in the configuration file.  Note that readServiceTags
   * and readWfsTags currently use the same config file, they just parse out
   * different parts of it.  This allows for those files to be easily split
   * up in later versions.
   *
   * @param configFile The file that contains the specific geoserver tags.
   *
   * @return The singleton object to query for configuration information.
   */
  private static OldWfsConfig readWfsTags(String configFile)
  {
    OldWfsConfig wfs = null;

    try {
      wfs = OldWfsConfig.getInstance(configFile);
    }
    catch (ConfigurationException ce) {
      LOGGER.warning("problem reading config file: " + ce.getMessage());
    }

    return wfs;
  }

  /**
   * constructs a ZServerConfig object, which encapsulates all the ZServer
   * module specific values in the configuration file.  Note that  all
   * config classes currently use the same config file, they just parse out
   * different parts of it.  This allows for those files to be easily split
   * up in later versions.  ZServer in particular will likely have its own
   * file, but for now not many fields are read.
   *
   * @param configFile The configuration file containing zserver config.
   *
   * @return The singleton that can return the zserver configuration.
   */
  private static ZServerConfig readZTags(String configFile)
  {
    ZServerConfig zConfig = null;

    try {
      zConfig = ZServerConfig.getInstance(configFile);
    }
    catch (ConfigurationException ce) {
      LOGGER.warning("problem reading config file: " + ce.getMessage());
    }

    return zConfig;
  }

  /**
   * Returns root webserver application directory
   *
   * @return a string of the root directory, only works for drop in war, not
   *         for embedded.
   */
  private static String guessRootDir()
  {
    return System.getProperty("user.dir") + "/webapps/geoserver/";
  }

  public String getRootDir()
  {
    return this.rootDir;
  }

  /**
   * Returns the user-specified title of this service
   *
   * @return The title of this service.
   */
  public String getTitle()
  {
    return serviceGlobal.getTitle();
  }

  /**
   * Returns user-specified abstract for this service
   *
   * @return The absract of this service.
   */
  public String getAbstract()
  {
    return serviceGlobal.getAbstract();
  }

  /**
   * Returns user-specified keywords for this service
   *
   * @return The keywords of this service.
   */
  public String getKeywords()
  {
    StringBuffer keywords = new StringBuffer();
    Iterator keywordIter = serviceGlobal.getKeywords().iterator();

    while (keywordIter.hasNext()) {
      keywords.append(keywordIter.next().toString());

      if (keywordIter.hasNext()) {
        keywords.append(", ");
      }
    }

    return keywords.toString();
  }

  /**
   * Returns online resource for this service.
   *
   * @return the online resource.
   */
  public String getOnlineResource()
  {
    return serviceGlobal.getOnlineResource();
  }

  /**
   * Returns URL for this service
   *
   * @return DOCUMENT ME!
   *
   * @task REVIST: should this be different from onlineResource?   Re-add url
   *       field?
   * @task REVISIT: put getDescribeURL and getFeatureURL, ect.?  Would be
   *       good for DescribeResponse with multiple types, to keep it
   *       consistent with  capabilities.
   */
  public String getUrl()
  {
    return wfsGlobal.getBaseUrl();
  }

  /**
   * Gets the online location where the ogc schemas for the wfs are stored.
   *
   * @return The base url to add the wfs/gml/filter suffix location on to.
   */
  public String getSchemaBaseUrl()
  {
    return wfsGlobal.getSchemaUrl();
  }

  /**
   * Returns user-specified fees for this service
   *
   * @return The fees associated with this service.
   */
  public String getFees()
  {
    return serviceGlobal.getFees();
  }

  /**
   * Returns user-specified access constraints for this service
   *
   * @return The access constraints of this service.
   */
  public String getAccessConstraints()
  {
    return serviceGlobal.getAccessConstraints();
  }

  public boolean formatOutput()
  {
    return wfsGlobal.isVerbose();
  }

  /**
   * Returns fixed version number for this service
   *
   * @return The version of this service.
   *
   * @task REVISIT: would be nice if we could somehow query this
   *       automatically.
   */
  public String getFreeFsVersion()
  {
    return "0.9b";
  }

  /**
   * Returns the current time as a string.
   *
   * @return The current time.
   */
  public String getCurrentTime()
  {
    return (new Date()).toString();
  }

  /**
   * Returns the directory where the featureTypes are stored.
   *
   * @return A string of the dir where the featureType folders are.
   */
  public String getTypeDir()
  {
    LOGGER.finer("returning typeDir " + typeDir);

    return typeDir;
  }

  /**
   * Sets the directory where featureType folders are stored.
   *
   * @param typeDir the directory set as typeDir.
   */
  public void setTypeDir(String typeDir)
  {
    this.typeDir = typeDir;
    LOGGER.finer("setting typedir " + typeDir);
  }

  /**
   * Returns root capabilities directory for this service.
   *
   * @return The directory where the capabilities are held.
   */
  public String getCapabilitiesDir()
  {
    return capabilitiesDir;
  }

  /**
   * Sets the root capabilities dir for this service.
   *
   * @param capabilitiesDir the directory to set where the capabilities are.
   */
  public void setCapabilitiesDir(String capabilitiesDir)
  {
    this.capabilitiesDir = capabilitiesDir;
  }

  public int getMaxFeatures()
  {
    return wfsGlobal.getMaxFeatures();
  }

  public Level getLogLevel()
  {
    return wfsGlobal.getLogLevel();
  }

  public String getFilePrefixDelimiter()
  {
    return wfsGlobal.getFilePrefixDelimiter();
  }

  /**
   * Returns the number of decimal places to return in GetFeature coordinates
   *
   * @return The number of decimal places.
   */
  public int getNumDecimals()
  {
    return wfsGlobal.getNumDecimals();
  }

  /**
   * Returns the character set set by the user, UTF-8 if none is set.
   *
   * @return The charSet set by the user.
   */
  public String getCharSet()
  {
    return wfsGlobal.getCharSet();
  }

  /**
   * Returns the declared xml header with the correct character set.
   *
   * @return The xml declaration with proper charSet.
   */
  public String getXmlHeader()
  {
    return "<?xml version=\"1.0\" encoding=\"" + getCharSet() + "\"?>";
  }

  /**
   * Returns the mimeType to set, using the charset from getCharSet
   *
   * @return The mime type with the proper charSet.
   */
  public String getMimeType()
  {
    return "text/xml; charset=" + getCharSet();
  }

  /**
   * Returns if the charset parameter for databases should be set to what
   * {@link #getCharSet} returns.
   *
   * @return <tt>true</tt> if a character set should be declared universally
   *         for db access.
   */
  public boolean useCharSetForDB()
  {
    return wfsGlobal.getCharSetPostgis();
  }

  /**
   * gets the default namespace prefix.  This is really more for backwards
   * compatibility, as all feature type directories should now be named with
   * their correct prefix namespace.  This is the prefix that will go on
   * typenames that don't have a prefix.
   *
   * @return the prefix to use for those featureTypes that don't have a
   *         properly prepended prefix.
   */
  public String getDefaultNSPrefix()
  {
    return wfsGlobal.getDefaultPrefix();
  }

  /**
   * Gets the namespace uri corresponding to passed in prefix, as set in the
   * configuration file.
   *
   * @param prefix the prefix to retrieve the uri for.
   *
   * @return the uri corresponding to the prefix.
   */
  public String getNSUri(String prefix)
  {
    return wfsGlobal.getUriFromPrefix(prefix);
  }

  /**
   * gets the namespace declartion associated with this prefix.   to go in
   * the root element.
   *
   * @param prefix the internal prefix that is mapped to a uri.
   *
   * @return the xmlns:---="http:..." type declaration, using this prefix and
   *         its associated uri.
   */
  public String getXmlnsDeclaration(String prefix)
  {
    return "xmlns:" + prefix + "=\"" + getNSUri(prefix) + "\"";
  }

  /**
   * gets all the xmlns declarations mapped in this ConfigInfo.
   *
   * @return the array of xmlns declarations.
   */
  public String[] getAllXmlns()
  {
    Set prefixSet = wfsGlobal.getNamespaces().keySet();
    String[] retStrings = new String[prefixSet.size()];
    Iterator prefixIter = prefixSet.iterator();
    int i = 0;

    while (prefixIter.hasNext()) {
      retStrings[i++] = getXmlnsDeclaration( (String) prefixIter.next());
    }

    return retStrings;
  }

  /**
   * Retrieves the appropriate xml to go in the service section of a
   * capabilities document.
   *
   * @param wfsName Name of the WFS
   *
   * @return An xml string of the service section of a capabilities document.
   */
  public String getServiceXml(String wfsName)
  {
    return serviceGlobal.getWfsXml();
  }

  void addPrefixNamespace(String prefix)
  {
    if (getNSUri(prefix) == null) {
      String defaultURI = getOnlineResource();
      defaultURI += (defaultURI.endsWith("/") ? "" : "/");

      String namespace = defaultURI + prefix;
      wfsGlobal.addNamespace(prefix, namespace);
    }
  }

  public Properties getZServerProps()
  {
    Properties retProps = null;

    if (runZServer()) {
      retProps = zGlobal.getProps();
    }

    return retProps;
  }

  /**
   * Whether the zserver module should be run.
   *
   * @return <tt>true</tt> if the zserver should run.
   */
  public boolean runZServer()
  {
    if (zGlobal == null) {
      //this should be configInfo.getTypeDir, but the constructor
      //of ConfigInfo calls this method, so the configInfo won't
      //yet be initialized.  Perhaps lazy initialization of zserver
      //variables in config info, this zserver config will then
      //be able to get the correct typedirs for sure.
      zGlobal = readZTags(rootDir + CONFIG_FILE);
    }

    return ( (zGlobal != null) && zGlobal.run());
  }

  /**
   * Gets if full stack traces should be reported in the ogc service
   * exceptions.
   *
   * @return <tt>true</tt> if the full stack trace should be printed for
   *         every service exception, <tt>false</tt> otherwise.
   *
   * @task TODO - implement this in WfsConfig, so users can configure how
   *       much is printed in return messages.
   */
  public boolean isPrintStack()
  {
    return false;
  }
}
