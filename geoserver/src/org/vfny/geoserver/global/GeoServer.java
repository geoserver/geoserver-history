/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;

/**
 * complete configuration ser for the whole server
 *
 * @author Gabriel Roldán
 * @version $Id: GeoServer.java,v 1.1.2.8 2004/01/07 21:23:08 dmzwiers Exp $
 */
public class GeoServer extends Abstract{// implements org.apache.struts.action.PlugIn{
	
	/** DOCUMENT ME! */
	private Level loggingLevel = Logger.getLogger("org.vfny.geoserver")
	                                   .getLevel();
	                                   
	private GeoServerDTO geoServer;
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getAddress() {
		return notNull(geoServer.getContact().getAddress());
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getAddressCity() {
		return notNull(geoServer.getContact().getAddressCity());
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getAddressCountry() {
		return notNull(geoServer.getContact().getAddressCountry());
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getAddressPostalCode() {
		return notNull(geoServer.getContact().getAddressPostalCode());
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getAddressState() {
		return notNull(geoServer.getContact().getAddressState());
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getAddressType() {
		return notNull(geoServer.getContact().getAddressType());
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getBaseUrl() {
	    return geoServer.getBaseUrl();
	}
	 
	
	    /**
	     * DOCUMENT ME!
	     *
	     * @return DOCUMENT ME!
	     */
	    public Charset getCharSet() {
	        return geoServer.getCharSet();
	    }
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContactEmail() {
		return notNull(geoServer.getContact().getContactEmail());
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContactFacsimile() {
		return notNull(geoServer.getContact().getContactFacsimile());
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContactOrganization() {
		return notNull(geoServer.getContact().getContactOrganization());
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContactPerson() {
		return notNull(geoServer.getContact().getContactPerson());
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContactPosition() {
		return notNull(geoServer.getContact().getContactPosition());
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getContactVoice() {
		return notNull(geoServer.getContact().getContactVoice());
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public Level getLoggingLevel() {
	    return loggingLevel;
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public int getMaxFeatures() {
	    return geoServer.getMaxFeatures();
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getMimeType() {
	    return "text/xml; charset=" + getCharSet().displayName();
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public int getNumDecimals() {
	    return geoServer.getNumDecimals();
	}
	/**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public String getSchemaBaseUrl() {
	    return geoServer.getSchemaBaseUrl();
	}
	/**
	 * wether xml documents should be pretty formatted
	 *
	 * @return DOCUMENT ME!
	 */
	public boolean isVerbose() {
	    return geoServer.isVerbose();
	}
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.global");
            
    public static final String SESSION_KEY = "GeoServer";

    /** server configuration singleton */
    //private static GeoServer serverConfig;

    /** DOCUMENT ME! */
    private WMS wms;

    /** DOCUMENT ME! */
    private WFS wfs;

    /** DOCUMENT ME! */
    private Data data;

    /** Validation Configuration */
    private Validation validation;

	/**
	 * GeoServer constructor.
	 * <p>
	 * To be called by the Struts ActionServlet
	 * </p>
	 *
	 */
	public GeoServer(){
		wms = null;
		wfs = null;
		data = null;
		validation = null;
		geoServer = null;
	}

    /**
     * Creates a new GeoServer object.
     *
     * @param rootDir The directory on the computer running geoserver where
     *        geoserver is located.
     *
     * @throws ConfigurationException For any configuration problems.
     */
    /*private GeoServer(String rootDir) throws ConfigurationException {
        File f = new File(rootDir);
		XMLConfigReader cr = null;
        cr = new XMLConfigReader(f);
		this.rootDir = rootDir;
		
		if(cr.isInitialized()){	
			geoServer = cr.getGeoServer();
			wfs = new WFS(cr.getWfs());
			wms = new WMS(cr.getWms());
			data = new Data(cr.getData());
			validation = new Validation();
		}else
			throw new ConfigurationException("An error occured loading the initial configuration.");
    }*/

    /**
     * Creates a new GeoServer Object for JUnit testing.
     * 
     * <p>
     * Configure based on provided Map, and Data.
     * </p>
     * 
     * <p>
     * GeoServer understands the followin entries in config map:
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
	/*private GeoServer(WMSDTO wms, WFSDTO wfs, GeoServerDTO geoServer, DataDTO data) throws ConfigurationException {
		if(rootDir == null)
			throw new ConfigurationException("RootDir not specified, server was not loaded initially from configuration files.");
		this.geoServer = geoServer;
		this.wfs = new WFS(wfs);
		this.wms = new WMS(wms);
		this.data = new Data(data);
		this.validation = new Validation();
	}*/

    /**
     * Gets the config for the WMS.
     *
     * @return DOCUMENT ME!
     */
    public WMS getWMS() {
        return wms;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WFS getWFS() {
        return wfs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Data getData() {
        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Validation getValidationConfig() {
        return validation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    /*public static GeoServer getInstance() {
        if (serverConfig == null) {
            throw new IllegalStateException(
                "The server configuration has not been loaded yet");
        }

        return serverConfig;
    }*/

    /**
     * DOCUMENT ME!
     *
     * @param rootDir DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */
    /*public static void load(String rootDir) throws ConfigurationException {
        serverConfig = new GeoServer(rootDir);
    }*/

	/**
	 * DOCUMENT ME!
	 *
	 * @param config DOCUMENT ME!
	 *
	 * @throws ConfigurationException DOCUMENT ME!
	 */
	public void load(WMSDTO wms, WFSDTO wfs, GeoServerDTO geoServer, DataDTO data) throws ConfigurationException {
		load(geoServer);
		load(wms);
		load(wfs);
		load(data);
		// HACK
		if(validation==null)
			this.validation = new Validation();
	}
	
	public void load(WMSDTO wms, WFSDTO wfs) throws ConfigurationException {
		load(wms);
		load(wfs);
	}
	
	public void load(WMSDTO wms) throws ConfigurationException {
		if(wms!=null)
			this.wms = new WMS((WMSDTO)wms.clone());
		else
			throw new ConfigurationException("load(WMSDTO) expected a non-null value");
	}
	
	public void load(WFSDTO wfs) throws ConfigurationException {
		if(wfs!=null)
			this.wfs = new WFS((WFSDTO)wfs.clone());
		else
			throw new ConfigurationException("load(WFSDTO) expected a non-null value");
	}
	
	public void load(GeoServerDTO geoServer) throws ConfigurationException {
		if(geoServer!=null)
			this.geoServer = (GeoServerDTO)geoServer.clone();
		else
			throw new ConfigurationException("load(GeoServerDTO) expected a non-null value");
	}
	
	public void load(DataDTO data) throws ConfigurationException {
		if(data!=null)
			if(this.data == null)
				this.data = new Data((DataDTO)data.clone());
			else
				this.data.load((DataDTO)data.clone());
		else
			throw new ConfigurationException("load(DataDTO) expected a non-null value");
	}
	
	public static WMSDTO getDTO(WMS wms){
		return (WMSDTO)((WMSDTO)wms.toDTO()).clone();
	}

	public static WFSDTO getDTO(WFS wfs){
		return (WFSDTO)((WFSDTO)wfs.toDTO()).clone();
	}

	public static GeoServerDTO getDTO(GeoServer gs){
		return (GeoServerDTO)((GeoServerDTO)gs.toDTO()).clone();
	}

	public static DataDTO getDTO(Data dt){
		return (DataDTO)((DataDTO)dt.toDTO()).clone();
	}

	public WMSDTO toWMSDTO(){
		return getDTO(wms);
	}

	public WFSDTO toWFSDTO(){
		return getDTO(wfs);
	}

	public GeoServerDTO toGeoServerDTO(){
		return (GeoServerDTO)geoServer.clone();
	}

	public DataDTO toDataDTO(){
		return getDTO(data);
	}

	Object toDTO(){
		return geoServer;
	}
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    /*public String getRootDir() {
        return rootDir;
    }*/
}
