/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.oldconfig;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.xml.parsers.*;

import org.vfny.geoserver.config.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * This class represents the global geoserver configuration options that are
 * not part of the Service element of a capabilities response.  They are
 * currently part of the same config file, but making a seperate class allows
 * us to easily seperate the elements out to different configuration files in
 * the future, such as for a WMS or WCS.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: OldWfsConfig.java,v 1.2 2003/12/16 18:46:08 cholmesny Exp $
 */
public class OldWfsConfig
    implements java.io.Serializable
{
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
  public static final String DECIMAL_TAG = "NumDecimals";
  public static final String CHARSET_TAG = "CharSet";
  public static final String DEFAULT_PREFIX = "myns";
  public static final String LOG_TAG = "LoggingLevel";
  public static final String DELIMIT_TAG = "PrefixDelimiter";
  public static final String DEFAULT_PREFIX_DELIMITER = "--";
  public static final String SCHEMA_BASE_TAG = "SchemaBaseUrl";

  /** Standard logging in stance for class */
  private static final Logger LOGGER = Logger.getLogger(
      "org.vfny.geoserver.config");
  private Level logLevel = Logger.getLogger("org.vfny.geoserver").getLevel();
  private int maxFeatures = 1000000;
  private String defaultPrefix;
  private String prefixDelimiter = DEFAULT_PREFIX_DELIMITER;
  private String baseUrl;
  private int numDecimals = 4;
  private String charSet = "UTF-8";
  private boolean useCharSetPG = false;
  private String schemaBaseUrl = null;

  /** The holds the mappings between prefixes and uri's */
  private Map nameSpaces = new HashMap();

  //put in wfs-config?  Maybe if we have some more config options.

  /** determines whether spaces and line feeds should be added to output */
  private boolean verbose = false;

  /**
   * Constructor.
   */
  private OldWfsConfig()
  {
  }

  /**
   * static factory, reads a WfsConfig from an xml file, using the default
   * root tag.
   *
   * @param configFile the path to the configuration file.
   *
   * @return the WfsConfig object constructed from the xml elements of the
   *         file.
   *
   * @throws ConfigurationException For problems reading the
   *         configuration.xml file.
   */
  public static OldWfsConfig getInstance(String configFile)
      throws ConfigurationException
  {
    return getInstance(configFile, ROOT_TAG);
  }

  /**
   * Static factory, reads a WfsConfig from an xml file, using the passed in
   * root tag.
   *
   * @param configFile the path to the configuration file.
   * @param rootTag the tag of the element whose children are the appropriate
   *        configuration elements.
   *
   * @return the WfsConfig object constructed from the xml elements of the
   *         file.
   *
   * @throws ConfigurationException For problems reading the
   *         configuration.xml file.
   */
  public static OldWfsConfig getInstance(String configFile, String rootTag)
      throws ConfigurationException
  {
    OldWfsConfig wfsConfig = new OldWfsConfig();

    try {
      FileReader fis = new FileReader(configFile);
      LOGGER.finest("got input reader, about to make input source");

      InputSource in = new InputSource(fis);
      DocumentBuilderFactory dfactory = DocumentBuilderFactory
          .newInstance();
      dfactory.setNamespaceAware(true);

      Document wfsDoc = dfactory.newDocumentBuilder().parse(in);
      Element configElem = wfsDoc.getDocumentElement();
      String configTag = configElem.getTagName();
      LOGGER.finer("root tag is " + configTag);

      if (!configTag.equals(rootTag)) {
        configElem = (Element) configElem.getElementsByTagName(rootTag)
            .item(0);

        if (configElem == null) {
          String message = "could not find root tag: " + rootTag
              + " in file: " + configFile;
          LOGGER.warning(message);

          //throw new ConfigurationException(message);
          //instead of throwing exception, just search whole document.
          configElem = wfsDoc.getDocumentElement();
        }
      }

      String logLevel = findTextFromTag(configElem, LOG_TAG);
      wfsConfig.setLogLevel(logLevel);

      Level level = wfsConfig.getLogLevel();

      //init this now so the rest of the config has correct log levels.
      Log4JFormatter.init("org.geotools", level);
      Log4JFormatter.init("org.vfny.geoserver", level);

      Element verboseElem = (Element) configElem.getElementsByTagName(
          VERBOSE_TAG)
          .item(0);

      if (verboseElem != null) {
        if (!verboseElem.getAttribute("value").equals("false")) {
          wfsConfig.setVerbose(true);
        }
        else {
          wfsConfig.setVerbose(false);
        }
      }
      else {
        wfsConfig.setVerbose(false);
      }

      String baseUrl = findTextFromTag(configElem, URL_TAG);
      wfsConfig.setBaseUrl(baseUrl);

      wfsConfig.setMaxFeatures(findTextFromTag(configElem, MAX_TAG));
      wfsConfig.setNumDecimals(findTextFromTag(configElem, DECIMAL_TAG));
      wfsConfig.setCharSet(findTextFromTag(configElem, CHARSET_TAG));
      wfsConfig.setSchemaUrl(findTextFromTag(configElem, SCHEMA_BASE_TAG));

      String delimiter = findTextFromTag(configElem, DELIMIT_TAG);
      LOGGER.finer("delimiter is " + delimiter);
      wfsConfig.setFilePrefixDelimiter(delimiter);

      //HashMap namespaces = new HashMap();
      NodeList namespaceElems = configElem.getElementsByTagName(NAMESPACE_TAG);
      LOGGER.finer("namespaceElems is " + namespaceElems);

      boolean defined = false;

      //loop through name space elements.
      for (int i = 0; i < namespaceElems.getLength(); i++) {
        Element curNamespace = (Element) namespaceElems.item(i);
        LOGGER.finer("cur namespace = " + curNamespace);

        String prefix = curNamespace.getAttribute(PREFIX_ATTR);
        String uri = curNamespace.getAttribute(URI_ATTR);
        String defaultA = curNamespace.getAttribute(DEFAULT_ATTR);

        if ( (prefix == null) || (uri == null)) {
          String message = "All <Namespace> elements must contain"
              + "both prefix and uri attributes.";

          //REVISIT: if we don't throw exception then the user will
          //just not have all the namespaces he may have intended.
          //throw new ConfigurationException(message);
          LOGGER.warning(message);
        }

        if (i == 0) {
          //if there is at least one have the first one be the
          //default namespace, unless default is explicitly set.
          wfsConfig.setDefaultPrefix(prefix);
          defined = true;
        }

        LOGGER.config("adding namespace: " + prefix + ":" + uri);
        wfsConfig.addNamespace(prefix, uri);

        //if default is defined and set to true then set it as the
        //default, over-riding the first default.
        if ( (defaultA != null) && defaultA.equals("true")) {
          LOGGER.config("setting default namespace to " + uri);
          wfsConfig.setDefaultPrefix(prefix);
        }
      }

      //if no namespaces are defined then construct a uri from the
      //Online Resource tag.
      if (!defined) {
        String defaultURI = ServiceConfigOld.findTextFromTag(configElem,
            ServiceConfigOld.ONLINE_TAG);

        if (defaultURI.equals("")) {
          LOGGER.warning("No Online Resource tag found, GeoServer "
                         + "will not work properly without it");
        }

        defaultURI += (defaultURI.endsWith("/") ? "" : "/");
        defaultURI += DEFAULT_PREFIX;
        LOGGER.finest("adding uri " + defaultURI);
        wfsConfig.addNamespace(DEFAULT_PREFIX, defaultURI);
        wfsConfig.setDefaultPrefix(DEFAULT_PREFIX);
      }

      fis.close();
    }
    catch (IOException ioe) {
      String message = "problem reading file " + configFile + "due to: "
          + ioe.getMessage();
      LOGGER.warning(message);
      throw new ConfigurationException(message, ioe);
    }
    catch (ParserConfigurationException pce) {
      String message = "trouble with parser to read xml, make sure class"
          + "path is correct, reading file " + configFile;
      LOGGER.warning(message);
      throw new ConfigurationException(message, pce);
    }
    catch (SAXException saxe) {
      String message = "trouble parsing XML in " + configFile + ": "
          + saxe.getMessage();
      LOGGER.warning(message);
      throw new ConfigurationException(message, saxe);
    }

    return wfsConfig;
  }

  //TODO: put this in a common utility file, as 3 classes use it, and
  //it's a bit confusing getting the ServiceConfig log messages.
  private static String findTextFromTag(Element root, String tag)
  {
    return ServiceConfigOld.findTextFromTag(root, tag);
  }

  /**
   * Sets the delimiter used in the featureType directory between namespaces
   * and types.  The default is '--', but users also may set it in
   * configuration.xml
   *
   * @param delimiter The string to seperate namespace prefixes and types.
   */
  void setFilePrefixDelimiter(String delimiter)
  {
    if (!delimiter.equals("")) {
      LOGGER.config("Setting new file prefix delimiter: " + delimiter);
      this.prefixDelimiter = delimiter;
    }
  }

  /**
   * Gets the delimiter used in the featureType directory between namespaces
   * and types.  The default is '--', but users also may set it in
   * configuration.xml
   *
   * @return The string to seperate namespace prefixes and types.
   */
  public String getFilePrefixDelimiter()
  {
    return this.prefixDelimiter;
  }

  /**
   * Sets the maximum number of features that should be returned by a
   * getFeatures request.
   *
   * @param max A string of the max features to set.  Should be parseable to
   *        an integer.  If this method can not parse the string then it
   *        just uses the default maxFeatures of this class.
   */
  void setMaxFeatures(String max)
  {
    LOGGER.finest("setting max features with " + max);

    //if (!max.equals("")){
    try {
      this.maxFeatures = Integer.parseInt(max);
      LOGGER.config("setting max features to " + max);
    }
    catch (NumberFormatException e) {
      LOGGER.finer("could not parse maxFeatures: " + max + ", "
                   + "using default: " + this.maxFeatures);
    }

    //}
  }

  /**
   * Gets the maximum number of features that should be returned by a get
   * features request.
   *
   * @return the max number of features to return.
   */
  public int getMaxFeatures()
  {
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
   *        java.util.logging.Level string representation, including
   *        integers.  If the level can not be parsed than the levels set in
   *        the jre  logging.properties are used.
   */
  void setLogLevel(String level)
  {
    try {
      logLevel = Level.parse(level);
      LOGGER.config("setting LogLevel to " + logLevel);
      Log4JFormatter.init("org.geotools", logLevel);
      Log4JFormatter.init("org.vfny.geoserver", logLevel);
    }
    catch (IllegalArgumentException e) {
      LOGGER.config("could not parse LogLevel: " + level + ", using "
                    + "level: " + this.logLevel + ", found in "
                    + "logging.properties file in java home");
    }
  }

  /**
   * gets the logging level.
   *
   * @return the level to be used for logging.
   */
  public Level getLogLevel()
  {
    return this.logLevel;
  }

  /**
   * Returns true if the Verbose tag is present in the config file.
   *
   * @return <tt>true</tt> if the verbose tag was present, <tt>false</tt>
   *         otherwise
   */
  public boolean isVerbose()
  {
    return verbose;
  }

  /**
   * sets the whether responses should have line feeds and nice formatting
   *
   * @param verbose whether out put should have line feeds and indents.
   */
  void setVerbose(boolean verbose)
  {
    LOGGER.config("Output from GeoServer will " + (verbose ? "" : "not")
                  + "insert newlines and indents into return xml");
    this.verbose = verbose;
  }

  /**
   * Gets the map of prefixes to namespace uris.
   *
   * @return The map of namespace prefixes to their uris.
   */
  Map getNamespaces()
  {
    return this.nameSpaces;
  }

  /**
   * Adds a namespace to the namespace map.
   *
   * @param prefix The prefix to map the namespace to.
   * @param namespace The namespace represented by the prefix.
   */
  void addNamespace(String prefix, String namespace)
  {
    nameSpaces.put(prefix, namespace);
  }

  /**
   * Sets the map of prefixes to namespace uris.
   *
   * @param nameSpaces The new map of namespaces.
   */
  void setNamespaces(Map nameSpaces)
  {
    this.nameSpaces = nameSpaces;
  }

  /**
   * Gets the base url of this wfs.
   *
   * @return The base of the servlet requests for this wfs.
   */
  public String getBaseUrl()
  {
    return this.baseUrl;
  }

  /**
   * Sets the base url of this wfs.
   *
   * @param baseUrl The base of the servlet requests for this wfs.
   */
  void setBaseUrl(String baseUrl)
  {
    this.baseUrl = baseUrl;
  }

  /**
   * Retrieves the uri for the passed in prefix.
   *
   * @param prefix The prefix to get from the namespace map.
   *
   * @return The namespace uri corresponding to this prefix.
   */
  public String getUriFromPrefix(String prefix)
  {
    return (String) nameSpaces.get(prefix);
  }

  /**
   * Sets the prefix to be used by default.
   *
   * @param prefix The prefix to be used by default.
   */
  void setDefaultPrefix(String prefix)
  {
    this.defaultPrefix = prefix;
  }

  /**
   * Gets the prefix to be used if no prefix is set/
   *
   * @return The default prefix.
   */
  public String getDefaultPrefix()
  {
    return this.defaultPrefix;
  }

  /**
   * Sets the char set to the passed in string and sets the boolean for the
   * featureType postgis datasources to use the character set. The logic
   * behind this is that if the user does not have a  CharSet element in
   * their configuration.xml file, they want to use UTF-8 for the xml and
   * mime encoding, and they do not want any  special charSet to be set by
   * the jdbc postgis driver.  If they do set the charSet to something, such
   * as iso-8859-1, they want that declared in the return xml and set for
   * all their postgis instances. Usually UTF-8 should suffice, but if users
   * are wanting to change everything about the encoding then it will go
   * across all aspects of geoserver.  If UTF-8 is desired for return, but a
   * special charset for the database then charset can be set in the
   * DatasourceParams of featureType.
   *
   * @param charSet The name of a valid encoding character set.
   *
   * @task TODO: do validation to make sure that the char set is  acceptable.
   *       We currently take any string, whichi will definitely mess things
   *       up if the user enters a non valid charset.
   */
  void setCharSet(String charSet)
  {
    if ( (charSet != null) && !charSet.equals("")) {
      this.charSet = charSet;
      setCharSetPostgis(true);
    }
  }

  /**
   * gets the char set to be used for replies.
   *
   * @return the charSet to set for xml and mime returns.
   */
  public String getCharSet()
  {
    return this.charSet;
  }

  /**
   * Sets the base schema url.
   *
   * @param baseUrl The url to use as the base for schema locations.
   */
  void setSchemaUrl(String baseUrl)
  {
    if ( (baseUrl != null) && !baseUrl.equals("")) {
      this.schemaBaseUrl = baseUrl;

      if (!schemaBaseUrl.endsWith("/")) {
        schemaBaseUrl += "/";
      }
    }
  }

  /**
   * Gets the base schema url.
   *
   * @return The url to use as the base for schema locations.
   */
  public String getSchemaUrl()
  {
    if (schemaBaseUrl != null) {
      return schemaBaseUrl;
    }
    else {
      return getBaseUrl() + "/data/capabilities/";
    }
  }

  /**
   * Sets whether the charSet returned by getCharSet should be used as the
   * charset for the postgis instances.  The current logic is that this will
   * only be set to tru if the char set is explicitly set in the
   * configuration.xml file.  The reason for this is that using the default
   * UTF-8 charset to set the charSet property of jdbc will mess things up,
   * while not setting any charset will work better. So if the user is to
   * explicitly say with the CharSet property that they want to use UTF-8,
   * then this will also get set for postgis.
   *
   * @param pgCharSet whether postgis should use the set char set.
   *
   * @task REVISIT: Rethink this logic, get user feedback.  Another good
   *       thing might be to make sure the charSet is valid before
   *       attempting to set it with postgis, as it will cause everything to
   *       mess up.
   */
  void setCharSetPostgis(boolean pgCharSet)
  {
    this.useCharSetPG = pgCharSet;
  }

  public boolean getCharSetPostgis()
  {
    return this.useCharSetPG;
  }

  /**
   * Sets the number of decimal places to be returned in the coordinates in a
   * getFeature request.
   *
   * @param numDecimals number of places past the decimal to report back.
   */
  void setNumDecimals(String numDecimals)
  {
    LOGGER.finest("setting num decimals with " + numDecimals);

    try {
      this.numDecimals = Integer.parseInt(numDecimals);
      LOGGER.config("setting num decimals to " + numDecimals);
    }
    catch (NumberFormatException e) {
      LOGGER.finer("could not parse numDecimals " + numDecimals + ", "
                   + "using default: " + this.numDecimals);
    }
  }

  /**
   * Gets the number of decimal places to use in returns
   *
   * @return the number of decimal places to return.
   */
  public int getNumDecimals()
  {
    return this.numDecimals;
  }

  /**
   * Override of toString method.
   *
   * @return the string representation of this configuration.
   */
  public String toString()
  {
    StringBuffer returnString = new StringBuffer("\nWfsConfig:");
    returnString.append("\n   [verbose: " + verbose + "] ");
    returnString.append("\n   [namespaces: ");

    Iterator i = nameSpaces.keySet().iterator();

    while (i.hasNext()) {
      String prefix = (String) i.next();
      returnString.append(prefix + "=" + nameSpaces.get(prefix));

      if (i.hasNext()) {
        returnString.append(", ");
      }
    }

    returnString.append("] ");
    returnString.append("\n   [defaultPrefix: " + defaultPrefix + "] ");

    return returnString.toString();
  }
}
