/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geotools.validation.ValidationProcessor;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;

import sun.security.action.GetBooleanAction;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * complete configuration ser for the whole server
 *
 * @author Gabriel Roldán
 * @author dzwiers
 * @version $Id: GeoServer.java,v 1.8 2004/02/02 08:56:45 jive Exp $
 */
public class GeoServer extends GlobalLayerSupertype { // implements org.apache.struts.action.PlugIn{

    /** For debugging */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.global");

    /**
     * For finding the instance of this class to use from the web container
     * 
     * <p>
     * ServletContext sc = ... GeoServer gs =
     * (GeoServer)sc.getAttribute(GeoServer.WEB_CONTAINER_KEY);
     * </p>
     */
    public static final String WEB_CONTAINER_KEY = "GeoServer";

    /** Default Logging level */
    private Level loggingLevel = Logger.getLogger("org.vfny.geoserver")
                                       .getLevel();

    /** The DTO for this geoserver instance, holds the instance data. */
    private GeoServerDTO geoServer;

    /** The reference to the wms configuration for this instance */
    private WMS wms;

    /** The reference to the wfs configuration for this instance */
    private WFS wfs;
    
    /** The validation processor */
    ValidationProcessor processor;

    /**
     * The reference to the data configuration for this instance (formerly
     * CatalogConfig)
     */
    private Data data;

    /**
     * getAddress purpose.
     * 
     * <p>
     * Returns the contact Address.
     * </p>
     *
     * @return String the contact Address.
     */
    public String getAddress() {
        return notNull(geoServer.getContact().getAddress());
    }

    /**
     * getAddressCity purpose.
     * 
     * <p>
     * Returns the contact City.
     * </p>
     *
     * @return String the contact City.
     */
    public String getAddressCity() {
        return notNull(geoServer.getContact().getAddressCity());
    }

    /**
     * getAddressCountry purpose.
     * 
     * <p>
     * Returns the contact Country.
     * </p>
     *
     * @return String the contact Country.
     */
    public String getAddressCountry() {
        return notNull(geoServer.getContact().getAddressCountry());
    }

    /**
     * getAddressPostalCode purpose.
     * 
     * <p>
     * Returns the contact PostalCode.
     * </p>
     *
     * @return String the contact PostalCode.
     */
    public String getAddressPostalCode() {
        return notNull(geoServer.getContact().getAddressPostalCode());
    }

    /**
     * getAddressState purpose.
     * 
     * <p>
     * Returns the contact State.
     * </p>
     *
     * @return String the contact State.
     */
    public String getAddressState() {
        return notNull(geoServer.getContact().getAddressState());
    }

    /**
     * getAddressType purpose.
     * 
     * <p>
     * Returns the contact Address Type.
     * </p>
     *
     * @return String the contact Address Type.
     */
    public String getAddressType() {
        return notNull(geoServer.getContact().getAddressType());
    }

    /**
     * getCharSet purpose.
     * 
     * <p>
     * Returns the default charset for this server instance.
     * </p>
     *
     * @return Charset the default charset for this server instance.
     */
    public Charset getCharSet() {
        return geoServer.getCharSet();
    }

    /**
     * getContactEmail purpose.
     * 
     * <p>
     * Returns the contact Email.
     * </p>
     *
     * @return String the contact Email.
     */
    public String getContactEmail() {
        return notNull(geoServer.getContact().getContactEmail());
    }

    /**
     * getContactFacsimile purpose.
     * 
     * <p>
     * Returns the contact Facsimile.
     * </p>
     *
     * @return String the contact Facsimile.
     */
    public String getContactFacsimile() {
        return notNull(geoServer.getContact().getContactFacsimile());
    }

    /**
     * getContactOrganization purpose.
     * 
     * <p>
     * Returns the contact Organization.
     * </p>
     *
     * @return String the contact Organization.
     */
    public String getContactOrganization() {
        return notNull(geoServer.getContact().getContactOrganization());
    }

    /**
     * getContactPerson purpose.
     * 
     * <p>
     * Returns the contact Person.
     * </p>
     *
     * @return String the contact Person.
     */
    public String getContactPerson() {
        return notNull(geoServer.getContact().getContactPerson());
    }

    /**
     * getContactPosition purpose.
     * 
     * <p>
     * Returns the contact Position.
     * </p>
     *
     * @return String the contact Position.
     */
    public String getContactPosition() {
        return notNull(geoServer.getContact().getContactPosition());
    }

    /**
     * getContactVoice purpose.
     * 
     * <p>
     * Returns the contact Phone.
     * </p>
     *
     * @return String the contact Phone.
     */
    public String getContactVoice() {
        return notNull(geoServer.getContact().getContactVoice());
    }

    /**
     * getLoggingLevel purpose.
     * 
     * <p>
     * Returns the Logging Level.
     * </p>
     *
     * @return String the Logging Level.
     */
    public Level getLoggingLevel() {
        return loggingLevel;
    }

    /**
     * getMaxFeatures purpose.
     * 
     * <p>
     * Returns the max number of features supported.
     * </p>
     *
     * @return String the max number of features supported.
     */
    public int getMaxFeatures() {
        return geoServer.getMaxFeatures();
    }

    /**
     * getMimeType purpose.
     * 
     * <p>
     * Returns the server default mimetype.
     * </p>
     *
     * @return String the server default mimetype.
     */
    public String getMimeType() {
        return "text/xml; charset=" + getCharSet().displayName();
    }

    /**
     * getNumDecimals purpose.
     * 
     * <p>
     * The default number of decimals allowed in the data.
     * </p>
     *
     * @return int the default number of decimals allowed in the data.
     */
    public int getNumDecimals() {
        return geoServer.getNumDecimals();
    }

    /**
     * getSchemaBaseUrl purpose.
     * 
     * <p>
     * The Schema Base URL for this instance.
     * </p>
     *
     * @return String the Schema Base URL for this instance.
     */
    public String getSchemaBaseUrl() {
        return geoServer.getSchemaBaseUrl();
    }

    /**
     * whether xml documents should be pretty formatted
     *
     * @return true when verbose
     */
    public boolean isVerbose() {
        return geoServer.isVerbose();
    }

    /**
     * Gets the config for the WMS.
     *
     * @return WMS the wms object
     */
    public WMS getWMS() {
        return wms;
    }

    /**
     * Gets the config for the WFS.
     *
     * @return WFS the wfs object
     */
    public WFS getWFS() {
        return wfs;
    }

    /**
     * Gets the config for the Data.
     *
     * @return Data the data object
     */
    public Data getData() {
        return data;
    }

    /**
     * load purpose.
     * 
     * <p>
     * Loads the specified DTOs.
     * </p>
     *
     * @param wms WMSDTO The wms data.
     * @param wfs WFSDTO The wfs data
     * @param geoServer GeoServerDTO The geo server data
     * @param data DataDTO The data for a catalog.
     *
     * @throws ConfigurationException If an error occurs.
     */
    public void load(WMSDTO wms, WFSDTO wfs, GeoServerDTO geoServer,
        DataDTO data, File baseDir ) throws ConfigurationException {
        load(geoServer);
        load(wms);
        load(wfs);
        load(data, baseDir );
    }
    
    private Map testSuites;
    private Map plugIns;
    
    public void load(Map testSuites, Map plugIns){
    	this.testSuites = testSuites;this.plugIns = plugIns;
    	processor = new ValidationProcessor(testSuites,plugIns);
    }
    
    public Map toPlugInDTO(){
    	return plugIns;
    }
    
    public Map toTestSuiteDTO(){
    	return testSuites;
    }

    /**
     * load purpose.
     * 
     * <p>
     * Loads the specified DTOs.
     * </p>
     *
     * @param wms WMSDTO The wms data.
     * @param wfs WFSDTO The wfs data
     *
     * @throws ConfigurationException If an error occurs.
     */
    public void load(WMSDTO wms, WFSDTO wfs) throws ConfigurationException {
        load(wms);
        load(wfs);
    }

    /**
     * load purpose.
     * 
     * <p>
     * Loads the WMSDTO into the current instance as a WMS object
     * </p>
     *
     * @param wms WMSDTO
     *
     * @throws ConfigurationException If an error occurs
     */
    public void load(WMSDTO wms) throws ConfigurationException {
        if (wms != null) {
            this.wms = new WMS((WMSDTO) wms.clone());
        } else {
            throw new ConfigurationException(
                "load(WMSDTO) expected a non-null value");
        }
    }

    /**
     * load purpose.
     * 
     * <p>
     * Loads the WFSDTO into the current instance as a WFS object
     * </p>
     *
     * @param wfs WFSDTO
     *
     * @throws ConfigurationException If an error occurs
     */
    public void load(WFSDTO wfs) throws ConfigurationException {
        if (wfs != null) {
            this.wfs = new WFS((WFSDTO) wfs.clone());
        } else {
            throw new ConfigurationException(
                "load(WFSDTO) expected a non-null value");
        }
    }

    /**
     * load purpose.
     * 
     * <p>
     * Loads the GeoServerDTO into the current instance as a GeoServer object
     * </p>
     *
     * @param geoServer GeoServerDTO
     *
     * @throws ConfigurationException If an error occurs
     */
    public void load(GeoServerDTO geoServer) throws ConfigurationException {
        if (geoServer != null) {
            this.geoServer = (GeoServerDTO) geoServer.clone();
        } else {
            throw new ConfigurationException(
                "load(GeoServerDTO) expected a non-null value");
        }
    }

    /**
     * Loads the DataDTO into the current instance as a Data object
     * 
     * @param data DataDTO
     *
     * @throws ConfigurationException If an error occurs
     */
    public void load(DataDTO data, File baseDir ) throws ConfigurationException {
        if (data != null) {
        	if (this.data == null) {
            	this.data = new Data((DataDTO) data.clone(), baseDir );
            } else {
                this.data.load((DataDTO) data.clone(), baseDir );
            }
        } else {
            throw new ConfigurationException(
                "load(DataDTO) expected a non-null value");
        }
    }

    /**
     * getDTO purpose.
     * 
     * <p>
     * Generates a WMSDTO for the WMS provided.
     * </p>
     *
     * @param wms WMS the object to generate from
     *
     * @return WMSDTO the generated object
     */
    public static WMSDTO getDTO(WMS wms) {
        return (WMSDTO) ((WMSDTO) wms.toDTO()).clone();
    }

    /**
     * getDTO purpose.
     * 
     * <p>
     * Generates a WFSDTO for the WFS provided.
     * </p>
     *
     * @param wfs WFS the object to generate from
     *
     * @return WFSDTO the generated object
     */
    public static WFSDTO getDTO(WFS wfs) {
        WFSDTO w = (WFSDTO) wfs.toDTO();
        w = (WFSDTO) w.clone();

        return (w);
    }

    /**
     * getDTO purpose.
     * 
     * <p>
     * Generates a GeoServerDTO for the GeoServer provided.
     * </p>
     *
     * @param gs GeoServer the object to generate from
     *
     * @return GeoServerDTO the generated object
     */
    public static GeoServerDTO getDTO(GeoServer gs) {
        return (GeoServerDTO) ((GeoServerDTO) gs.toDTO()).clone();
    }

    /**
     * getDTO purpose.
     * 
     * <p>
     * Generates a DataDTO for the Data provided.
     * </p>
     *
     * @param dt Data the object to generate from
     *
     * @return DataDTO the generated object
     */
    public static DataDTO getDTO(Data dt) {
        return (DataDTO) ((DataDTO) dt.toDTO()).clone();
    }

    /**
     * toWMSDTO purpose.
     * 
     * <p>
     * Generates a WMSDTO object from the WMS object inside this instance.
     * </p>
     *
     * @return WMSDTO the generated object
     */
    public WMSDTO toWMSDTO() {
        return getDTO(wms);
    }

    /**
     * toWFSDTO purpose.
     * 
     * <p>
     * Generates a WFSDTO object from the WFS object inside this instance.
     * </p>
     *
     * @return WFSDTO the generated object
     */
    public WFSDTO toWFSDTO() {
        return getDTO(wfs);
    }

    /**
     * toGeoServerDTO purpose.
     * 
     * <p>
     * Generates a GeoServerDTO object from the GeoServer object inside this
     * instance.
     * </p>
     *
     * @return GeoServerDTO the generated object
     */
    public GeoServerDTO toGeoServerDTO() {
        return (GeoServerDTO) geoServer.clone();
    }

    /**
     * toDataDTO purpose.
     * 
     * <p>
     * Generates a DataDTO object from the Data object inside this instance.
     * </p>
     *
     * @return DataDTO the generated object
     */
    public DataDTO toDataDTO() {
        return getDTO(data);
    }

    /**
     * toDTO purpose.
     * 
     * <p>
     * This method is package visible only, and returns a reference to the
     * GeoServerDTO. This method is unsafe, and should only be used with
     * extreme caution.
     * </p>
     *
     * @return WMSDTO the generated object
     */
    Object toDTO() {
        return geoServer;
    }
	/**
	 * Access processor property.
	 * 
	 * @return Returns the processor.
	 */
	public ValidationProcessor getProcessor() {
		return processor;
	}

}
