/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.w3c.dom.*;
import org.xml.sax.*;
import java.io.*;
import java.util.logging.*;
import javax.xml.parsers.*;


/**
 * complete configuration ser for the whole server
 *
 * @author Gabriel Rold�n
 * @version 0.1
 */
public class ServerConfig {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");

    //  private ZServerConfig zServerConfig;

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
    private String rootDir;

    /**
     * Creates a new ServerConfig object.
     *
     * @param rootDir DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    private ServerConfig(String rootDir) throws ConfigurationException {
        this.rootDir = rootDir;

        String configFile = rootDir + "services.xml";
        String catalogFile = rootDir + "catalog.xml";

        Element configElem = loadConfig(configFile);
        Element catalogElem = loadConfig(catalogFile);

        load(configElem, catalogElem);
    }

    /**
     * DOCUMENT ME!
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
     * @param configFile DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    private static Element loadConfig(String configFile)
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
     * DOCUMENT ME!
     *
     * @param configElem DOCUMENT ME!
     * @param catalogElement DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    private void load(Element configElem, Element catalogElement)
        throws ConfigurationException {
        LOGGER.fine("parsing configuration documents");

        Element elem = (Element) configElem.getElementsByTagName("global").item(0);
        globalConfig = new GlobalConfig(elem);

        NodeList configuredServices = configElem.getElementsByTagName("service");
        int nServices = configuredServices.getLength();

        for (int i = 0; i < nServices; i++) {
            elem = (Element) configuredServices.item(i);

            String serviceType = elem.getAttribute("type");

            if ("WFS".equalsIgnoreCase(serviceType)) {
                featureServerConfig = new WFSConfig(elem);
            } else if ("WMS".equalsIgnoreCase(serviceType)) {
                mapServerConfig = new WMSConfig(elem);
            } else if ("Z39.50".equalsIgnoreCase(serviceType)) {
                //...
            } else {
                throw new ConfigurationException("Unknown service type: "
                    + serviceType);
            }
        }

        catalog = new CatalogConfig(catalogElement);
    }

    /**
     * saves the server configuration to <code>dest</code> as an XML stream
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
