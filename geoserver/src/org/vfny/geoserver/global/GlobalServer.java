/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.File;
import java.util.logging.Logger;

import org.vfny.geoserver.config.ModelConfig;
import org.vfny.geoserver.config.xml.ConfigException;
import org.vfny.geoserver.config.xml.XMLConfigReader;
import org.vfny.geoserver.config.xml.XMLConfigWriter;

/**
 * complete configuration ser for the whole server
 *
 * @author Gabriel Roldán
 * @version $Id: GlobalServer.java,v 1.1.2.1 2004/01/03 00:20:15 dmzwiers Exp $
 */
public class GlobalServer extends GlobalAbstract {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");
            
    private ModelConfig model;

    /** server configuration singleton */
    private static GlobalServer serverConfig;

    /** DOCUMENT ME! */
    private GlobalWMS mapServerConfig;

    /** DOCUMENT ME! */
    private GlobalWFS featureServerConfig;

    /** DOCUMENT ME! */
    private GlobalCatalog catalog;

    /** DOCUMENT ME! */
    private GlobalData globalConfig;

    /** Validation Configuration */
    private GlobalValidation validationConfig;
    private String rootDir;


    /**
     * Creates a new GlobalServer object.
     *
     * @param rootDir The directory on the computer running geoserver where
     *        geoserver is located.
     *
     * @throws ConfigurationException For any configuration problems.
     */
    private GlobalServer(String rootDir) throws ConfigurationException {
        File f = new File(rootDir);
		XMLConfigReader cr = null;
        try{
        	cr = new XMLConfigReader(f);
        }catch(ConfigException e){
        	throw new ConfigurationException(e.toString());
        }
		this.rootDir = rootDir;
		ModelConfig config = cr.getModel();
		model = config;

		globalConfig = new GlobalData(config.getGlobal());

		featureServerConfig = new GlobalWFS(config.getWfs());
		mapServerConfig = new GlobalWMS(config.getWms());
		catalog = new GlobalCatalog(config.getCatalog());
		validationConfig = new GlobalValidation(config);
    }

    /**
     * Creates a new GlobalServer Object for JUnit testing.
     * 
     * <p>
     * Configure based on provided Map, and GlobalCatalog.
     * </p>
     * 
     * <p>
     * GlobalServer understands the followin entries in config map:
     * 
     * <ul>
     * <li>
     * dir: File (default to current directory
     * </li>
     * </ul>
     * </p>
     *
     * @param config a map of the config params.
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
	private GlobalServer(ModelConfig config) throws ConfigurationException {
		model = config;
		if(rootDir == null)
			throw new ConfigurationException("RootDir not specified, server was not loaded initially from configuration files.");

		globalConfig = new GlobalData(config.getGlobal());

		featureServerConfig = new GlobalWFS(config.getWfs());
		mapServerConfig = new GlobalWMS(config.getWms());
		catalog = new GlobalCatalog(config.getCatalog());
		validationConfig = new GlobalValidation(config);
	}

    /**
     * Gets the config for the GlobalWMS.
     *
     * @return DOCUMENT ME!
     */
    public GlobalWMS getWMS() {
        return mapServerConfig;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GlobalWFS getWFS() {
        return featureServerConfig;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GlobalData getGlobalData() {
        return globalConfig;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GlobalCatalog getCatalog() {
        return catalog;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GlobalValidation getValidationConfig() {
        return validationConfig;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    public static GlobalServer getInstance() {
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
        serverConfig = new GlobalServer(rootDir);
    }

	/**
	 * DOCUMENT ME!
	 *
	 * @param config DOCUMENT ME!
	 *
	 * @throws ConfigurationException DOCUMENT ME!
	 */
	public static void load(ModelConfig config) throws ConfigurationException {
		serverConfig = new GlobalServer(config);
	}

    /**
     * saves the server configuration to <code>dest</code> as an XML stream
     *
     * @TODO method body
     *
     * @param destDir DOCUMENT ME!
     */
    public void save(String destDir) throws ConfigurationException{
    	File dest = new File(destDir);
    	try{
			XMLConfigWriter cw = new XMLConfigWriter(model);
			cw.store(dest);
    	}catch(ConfigException e){
    		throw new ConfigurationException("Error saving configuration",e);
    	}
    }

    /**
     * Returns the current time as a string.
     *
     * @return The current time.
     */
    /*public static String getCurrentTime() {
        return (new java.util.Date()).toString();
    }*/

    /**
     * Returns the declared xml header with the correct character set.
     *
     * @return The xml declaration with proper charSet.
     */
    /*public String getXmlHeader() {
        return "<?xml version=\"1.0\" encoding=\""
        + getGlobalConfig().getCharSet().displayName() + "\"?>";
    }*/

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
    /*public String getTypeDir() {
        return getRootDir() + "featureTypes/";
    }*/

    /**
     * Returns root capabilities directory for this service.
     *
     * @return The directory where the capabilities are held.
     */
    /*public String getCapabilitiesDir() {
        return getRootDir() + "capabilities/";
    }*/

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
    /*public boolean isPrintStack() {
        return false;
    }*/
}
