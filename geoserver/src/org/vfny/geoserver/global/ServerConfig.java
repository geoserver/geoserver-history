/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.geotools.data.Catalog;
import org.geotools.data.DataStoreFinder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.vfny.geoserver.config.*;
import org.vfny.geoserver.config.xml.*;


/**
 * complete configuration ser for the whole server
 *
 * @author Gabriel Roldán
 * @version $Id: ServerConfig.java,v 1.1.2.3 2004/01/02 17:34:57 dmzwiers Exp $
 */
public class ServerConfig extends AbstractConfig {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");

    //  private ZServerConfig zServerConfig;

    /** Default name for configuration directory */
    private static final String CONFIG_DIR = "WEB-INF/";

    /** Default name for configuration directory */
    private static final String DATA_DIR = "data/";

    /** server configuration singleton */
    private static ServerConfig serverConfig;

    /** DOCUMENT ME! */
    private WMSConfig mapServerConfig;

    /** DOCUMENT ME! */
    private WFSConfig featureServerConfig;

    /** DOCUMENT ME! */
    private CatalogConfig catalog;

    /** DOCUMENT ME! */
    private GlobalConfig globalConfig;

    /** Validation Configuration */
    private ValidationConfig validationConfig;
    private String rootDir;

    //HACK: this will no longer be necessary when the config loading is split
    //out, it's just necessary now so that the loading of styles can 
    private String dataDir;

    /**
     * Creates a new ServerConfig object.
     *
     * @param rootDir The directory on the computer running geoserver where
     *        geoserver is located.
     *
     * @throws ConfigurationException For any configuration problems.
     */
    private ServerConfig(String rootDir) throws ConfigurationException {
        /*this.rootDir = rootDir;

        String configDir = rootDir + CONFIG_DIR;
        String configFile = configDir + "services.xml";
        String catalogFile = configDir + "catalog.xml";
        LOGGER.fine("loading config file: " + configFile);

        Element configElem = loadConfig(configFile);
        Element catalogElem = loadConfig(catalogFile);
        this.dataDir = rootDir + DATA_DIR;

        String featureTypeDir = dataDir + "featureTypes";

        Iterator iter = DataStoreFinder.getAvailableDataStores();

        while (iter.hasNext()) {
            LOGGER.config(iter.next() + " is an available DataSource");
        }

        load(configElem, catalogElem, dataDir);

        validationConfig = new ValidationConfig(new File(dataDir, "validation"));
        */
        File f = new File(rootDir);
		XMLConfigReader cr = null;
        try{
        	cr = new XMLConfigReader(f);
        }catch(ConfigException e){
        	throw new ConfigurationException(e.toString());
        }
		this.rootDir = rootDir;	//check for removal
		ModelConfig config = cr.getModel();

		globalConfig = new GlobalConfig(config.getGlobal());

		featureServerConfig = new WFSConfig(config.getWfs());
		mapServerConfig = new WMSConfig(config.getWms());
		catalog = new CatalogConfig(config.getCatalog());
		validationConfig = new ValidationConfig(config);
    }

    /**
     * Creates a new ServerConfig Object for JUnit testing.
     * 
     * <p>
     * Configure based on provided Map, and CatalogConfig.
     * </p>
     * 
     * <p>
     * ServerConfig understands the followin entries in config map:
     * 
     * <ul>
     * <li>
     * dir: File (default to current directory
     * </li>
     * </ul>
     * </p>
     *
     * @param config a map of the config params.
     * @param gt2catalog The gt2 catalog.
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    /*private ServerConfig(Map config, Catalog gt2catalog)
        throws ConfigurationException {
        this.rootDir = get(config, "dir", new File(".")).toString();

        globalConfig = new GlobalConfig(config);

        featureServerConfig = new WFSConfig(config);
        mapServerConfig = new WMSConfig(config);
        catalog = new CatalogConfig(config, gt2catalog);
        validationConfig = new ValidationConfig(config);
    }*/
	public ServerConfig(ModelConfig config)
		throws ConfigurationException {
			
		//this.rootDir = rootDir;	//check for removal

		globalConfig = new GlobalConfig(config.getGlobal());

		featureServerConfig = new WFSConfig(config.getWfs());
		mapServerConfig = new WMSConfig(config.getWms());
		catalog = new CatalogConfig(config.getCatalog());
		validationConfig = new ValidationConfig(config);
	}

    /**
     * Gets the directory where geoserver data is stored.
     *
     * @return DOCUMENT ME!
     */
    //public String getDataDir() {
    //    return dataDir;
    //}

    /**
     * Gets the config for the WMSConfig.
     *
     * @return DOCUMENT ME!
     */
    public WMSConfig getWMSConfig() {
        return mapServerConfig;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WFSConfig getWFSConfig() {
        return featureServerConfig;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CatalogConfig getCatalog() {
        return catalog;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ValidationConfig getValidationConfig() {
        return validationConfig;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public static ServerConfig getInstance() {
        if (serverConfig == null) {
            throw new IllegalStateException(
                "The server configuration has not been loaded yet");
        }

        return serverConfig;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rootDir DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    public static void load(String rootDir) throws ConfigurationException {
        serverConfig = new ServerConfig(rootDir);
    }

	/**
	 * DOCUMENT ME!
	 *
	 * @param config DOCUMENT ME!
	 *
	 * @throws ConfigurationException DOCUMENT ME!
	 */
	public static void load(ModelConfig config) throws ConfigurationException {
		serverConfig = new ServerConfig(config);
	}

    /**
     * DOCUMENT ME!
     *
     * @param configFile DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    public static Element loadConfig(String configFile)
        throws ConfigurationException {
        try {
            LOGGER.fine("loading configuration file " + configFile);

            FileReader fis = new FileReader(configFile);
            InputSource in = new InputSource(fis);
            DocumentBuilderFactory dfactory = DocumentBuilderFactory
                .newInstance();
            dfactory.setNamespaceAware(true);

            Document serviceDoc = dfactory.newDocumentBuilder().parse(in);
            Element configElem = serviceDoc.getDocumentElement();

            return configElem;
        } catch (IOException ioe) {
            String message = "problem reading file " + configFile + "due to: "
                + ioe.getMessage();
            LOGGER.warning(message);
            throw new ConfigurationException(message, ioe);
        } catch (ParserConfigurationException pce) {
            String message = "trouble with parser to read xml, make sure class"
                + "path is correct, reading file " + configFile;
            LOGGER.warning(message);
            throw new ConfigurationException(message, pce);
        } catch (SAXException saxe) {
            String message = "trouble parsing XML in " + configFile + ": "
                + saxe.getMessage();
            LOGGER.warning(message);
            throw new ConfigurationException(message, saxe);
        }
    }

    /**
     * Sets everything up based on provided gt2 CatalogConfig.
     * 
     * <p>
     * This is a quick hack to allow for basic JUnit testing.
     * </p>
     * 
     * <p>
     * We need to get GeoServer to push more functionality into gt2 CatalogConfig
     * interface. Right now they have similar goals in life, we should set
     * things up so GeoServer config classes can implement the CatalogConfig
     * interface, and expand the CatalogConfig interface to the point it is useful.
     * </p>
     *
     * @param config DOCUMENT ME!
     * @param catalog DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    /*public static void load(Map config, Catalog catalog)
        throws ConfigurationException {
        serverConfig = new ServerConfig(config, catalog);
    }*/

    /**
     * DOCUMENT ME!
     *
     * @param configElem DOCUMENT ME!
     * @param catalogElement DOCUMENT ME!
     * @param featureTypeDir DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
  /*  private void load(Element configElem, Element catalogElement,
        String featureTypeDir) throws ConfigurationException {
        LOGGER.fine("parsing configuration documents");

        Element elem = (Element) configElem.getElementsByTagName("global").item(0);
        globalConfig = new GlobalConfig(elem);

        NodeList configuredServices = configElem.getElementsByTagName("service");
        int nServices = configuredServices.getLength();

        for (int i = 0; i < nServices; i++) {
            elem = (Element) configuredServices.item(i);

            String serviceType = elem.getAttribute("type");

            if ("WFSConfig".equalsIgnoreCase(serviceType)) {
                featureServerConfig = new WFSConfig(elem);
            } else if ("WMSConfig".equalsIgnoreCase(serviceType)) {
                mapServerConfig = new WMSConfig(elem);
            } else if ("Z39.50".equalsIgnoreCase(serviceType)) {
                //...
            } else {
                throw new ConfigurationException("Unknown service type: "
                    + serviceType);
            }
        }

        catalog = new CatalogConfig(catalogElement, dataDir);
    }*/

    /**
     * saves the server configuration to <code>dest</code> as an XML stream
     *
     *	TODO method body
     *
     * @param dest DOCUMENT ME!
     */
    public void save(OutputStream dest) {
    }

    /**
     * Returns the current time as a string.
     *
     * @return The current time.
     */
    public static String getCurrentTime() {
        return (new java.util.Date()).toString();
    }

    /**
     * Returns the declared xml header with the correct character set.
     *
     * @return The xml declaration with proper charSet.
     */
    public String getXmlHeader() {
        return "<?xml version=\"1.0\" encoding=\""
        + getGlobalConfig().getCharSet().displayName() + "\"?>";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getRootDir() {
        return rootDir;
    }

    /**
     * Returns the directory where the featureTypes are stored.
     *
     * @return A string of the dir where the featureType folders are.
     *
     * @task TODO:remove it when no longer needed
     */
    public String getTypeDir() {
        return getRootDir() + "featureTypes/";
    }

    /**
     * Returns root capabilities directory for this service.
     *
     * @return The directory where the capabilities are held.
     */
    public String getCapabilitiesDir() {
        return getRootDir() + "capabilities/";
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
    public boolean isPrintStack() {
        return false;
    }
}
