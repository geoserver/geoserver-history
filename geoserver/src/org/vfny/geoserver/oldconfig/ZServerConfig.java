/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.oldconfig;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoServer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class represents the global geoserver configuration options that are
 * used to configure the zserver module.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: ZServerConfig.java,v 1.3 2004/01/12 21:01:30 dmzwiers Exp $
 */
public class ZServerConfig
    implements java.io.Serializable
{
  /** Default name for zserver module mappings */
  public static final String GEO_MAP_FILE = "z3950-geo.map";

  /** The tag for which port to run zserver on. */
  public static final String PORT_TAG = "Port";

  /** The root tag for zserver params. */
  public static final String ROOT_TAG = "ZServer";

  /** The tag for the location of the fieldmap file to use. */
  public static final String FIELDMAP_TAG = "FieldMap";

  /** The xml tag for whether zserver module should be run. */
  public static final String RUN_ATTR = "run";

  /** Standard logging in stance for class */
  private static final Logger LOGGER = Logger.getLogger(
      "org.vfny.geoserver.config");

  /** The configuration singleton. */
  private static GeoServer cfgInfo = ConfigInfo.getInstance().getGeoServer();

  /** The port to run zserver on.  Default is 5210 */
  private String port = "5210";

  /** The folder where the metadata files are. */
  private String dataFolder = ConfigInfo.getInstance().getRootDir() + "featureTypes/";

  /** The location of the mapping between field names and numbers. */
  private String fieldmap;

  /** The location of the lucene database. */
  private String database;

  /** Whethere the zserver should be run. */
  private boolean runServer = true;

  /** The holds the mappings between prefixes and uri's */
  private Properties zserverProps;

  /**
   * Private constructor specifying if server should be run.   Thould
   * generally only be run with false as the param, since other information
   * needs to be set if we are to be run.
   *
   * @param runServer should be <tt>false</tt>.
   */
  private ZServerConfig(boolean runServer)
  {
    this.runServer = runServer;
  }

  /**
   * Constructor with necessary params to run the server.  Assumes runServer
   * is true.
   *
   * @param port The port to run the zserver on.
   * @param fields The location of the field mappings.
   * @param database The location of the backend database of metadata.
   */
  private ZServerConfig(String port, String fields, String database)
  {
    if ( (port != null) && !port.equals("")) {
      this.port = port;
    }

    this.fieldmap = fields;
    this.database = database;
  }

  /**
   * Static factory, reads a ZServerConfig from an xml file, using the
   * default root tag.
   *
   * @param configFile The path to the configuration file.
   *
   * @return The ZServerConfig object constructed from the xml elements of
   *         the file.
   *
   * @throws ConfigurationException For any config problems.
   */
  public static ZServerConfig getInstance(String configFile)
      throws ConfigurationException
  {
    return getInstance(configFile, ROOT_TAG);
  }

  /**
   * Static factory, reads a ZServerConfig from an xml file, using the passed
   * in root tag.
   *
   * @param configFile The path to the configuration file.
   * @param rootTag The tag of the element whose children are the appropriate
   *        configuration elements.
   *
   * @return The ZServerConfig object constructed from the xml elements of
   *         the file.
   *
   * @throws ConfigurationException For any config problems.
   */
  public static ZServerConfig getInstance(String configFile, String rootTag)
      throws ConfigurationException
  {
    try {
      //FileInputStream fis = new FileInputStream(configFile);
      FileReader fis = new FileReader(configFile);
      InputSource in = new InputSource(fis);
      DocumentBuilderFactory dfactory = new org.apache.xerces.jaxp.
          DocumentBuilderFactoryImpl();
      dfactory.setNamespaceAware(true);

      Document wfsDoc = dfactory.newDocumentBuilder().parse(in);
      Element configElem = wfsDoc.getDocumentElement();
      String configTag = configElem.getTagName();
      LOGGER.finer("root tag is " + configTag);

      if (!configTag.equals(rootTag)) {
        configElem = (Element) configElem.getElementsByTagName(rootTag)
            .item(0);

        if (configElem == null) {
          String message = "could not find zserver tag: " + rootTag
              + " in file: " + configFile
              + ".  Zserver module is not" + " being run";
          LOGGER.info(message);

          //throw new ConfigurationException(message);
          //instead of throwing exception, just search whole document.
          return new ZServerConfig(false);
        }
      }

      String runZserver = configElem.getAttribute(RUN_ATTR);

      if ( (runZserver == null) || !runZserver.equals("false")) {
        String port = findTextFromTag(configElem, PORT_TAG);
        String rootDir = ConfigInfo.getInstance().getRootDir();
        String fieldMap = rootDir + GEO_MAP_FILE;
        String database = rootDir + "zserver-index";

        return new ZServerConfig(port, fieldMap, database);
      }
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

    return new ZServerConfig(false);
  }

  //TODO: put this in a common utility file, as 3 classes use it, and
  //it's a bit confusing getting the ServiceConfig log messages.
  private static String findTextFromTag(Element root, String tag)
  {
    return ServiceConfigOld.findTextFromTag(root, tag);
  }

  /**
   * Whether the zserver module should be run.
   *
   * @return <tt>true</tt> if the server should be run.
   */
  public boolean run()
  {
    return this.runServer;
  }

  /**
   * Returns the properties to run the zserver, in that module's  preferred
   * format.
   *
   * @return the Properties to run the zserver.
   */
  public Properties getProps()
  {
    if (zserverProps == null) {
      zserverProps = new Properties();
      zserverProps.put("port", port);
      zserverProps.put("datafolder", dataFolder);
      zserverProps.put("fieldmap", fieldmap);
      zserverProps.put("database", database);
    }

    return zserverProps;
  }
}
