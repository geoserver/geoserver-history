/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geotools.validation.ValidationProcessor;
import org.geotools.validation.dto.TestDTO;
import org.geotools.validation.dto.TestSuiteDTO;
import org.vfny.geoserver.global.dto.ContactDTO;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;

import sun.security.action.GetBooleanAction;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * complete configuration ser for the whole server
 *
 * @author Gabriel Roldán
 * @author dzwiers
 * @version $Id: GeoServer.java,v 1.11 2004/02/09 23:11:35 dmzwiers Exp $
 */
public class GeoServer extends GlobalLayerSupertype {

	private int maxFeatures = Integer.MAX_VALUE;
	private boolean verbose = true;
	private int numDecimals = 4;
	private Charset charSet = Charset.forName("UTF-8");
	private String schemaBaseUrl;
	private String contactPerson;
	private String contactOrganization;
	private String contactPosition;
	private String addressType;
	private String address;
	private String addressCity;
	private String addressState;
	private String addressPostalCode;
	private String addressCountry;
	private String contactVoice;
	private String contactFacsimile;
	private String contactEmail;

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

    /** The reference to the wms configuration for this instance */
    //private WMS wms;

    /** The reference to the wfs configuration for this instance */
    //private WFS wfs;
    
    /** The validation processor */
    //ValidationProcessor processor;

    /**
     * The reference to the data configuration for this instance (formerly
     * CatalogConfig)
     */
    //private Data data;

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
        return notNull(address);
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
        return notNull(addressCity);
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
        return notNull(addressCountry);
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
        return notNull(addressPostalCode);
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
        return notNull(addressState);
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
        return notNull(addressType);
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
        return charSet;
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
        return notNull(contactEmail);
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
        return notNull(contactFacsimile);
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
        return notNull(contactOrganization);
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
        return notNull(contactPerson);
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
        return notNull(contactPosition);
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
        return notNull(contactVoice);
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
        return maxFeatures;
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
        return numDecimals;
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
        return schemaBaseUrl;
    }

    /**
     * whether xml documents should be pretty formatted
     *
     * @return true when verbose
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * Gets the config for the WMS.
     *
     * @return WMS the wms object
     */
    /*public WMS getWMS() {
        return wms;
    }*/

    /**
     * Gets the config for the WFS.
     *
     * @return WFS the wfs object
     */
    /*public WFS getWFS() {
        return wfs;
    }*/

    /**
     * Gets the config for the Data.
     *
     * @return Data the data object
     */
    /*public Data getData() {
        return data;
    }*/

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
    /*public void load(WMSDTO wms, WFSDTO wfs, GeoServerDTO geoServer,
        DataDTO data, File baseDir ) throws ConfigurationException {
        load(geoServer);
        load(wms);
        load(wfs);
        load(data, baseDir );
    }*/
    
    /*private Map testSuites;
    private Map plugIns;
    
    public void load(Map testSuites, Map plugIns){
    	this.testSuites = testSuites;this.plugIns = plugIns;
    	try{
    		processor = new ValidationProcessor(testSuites,plugIns);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public Map toPlugInDTO(){
    	return plugIns;
    }
    
    public Map toTestSuiteDTO(){
    	return testSuites;
    }*/

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
    /*public void load(WMSDTO wms, WFSDTO wfs) throws ConfigurationException {
        load(wms);
        load(wfs);
    }*/

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
    /*public void load(WMSDTO wms) throws ConfigurationException {
        if (wms != null) {
            this.wms = new WMS((WMSDTO) wms.clone());
        } else {
            throw new ConfigurationException(
                "load(WMSDTO) expected a non-null value");
        }
    }*/

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
    /*public void load(WFSDTO wfs) throws ConfigurationException {
        if (wfs != null) {
            this.wfs = new WFS((WFSDTO) wfs.clone());
        } else {
            throw new ConfigurationException(
                "load(WFSDTO) expected a non-null value");
        }
    }*/

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
    public void load(GeoServerDTO dto) throws ConfigurationException {
        if (dto != null) {
        	address = dto.getContact().getAddress();
        	addressCity = dto.getContact().getAddressCity();
        	addressCountry = dto.getContact().getAddressCountry();
        	addressPostalCode = dto.getContact().getAddressPostalCode();
        	addressState = dto.getContact().getAddressState();
        	addressType = dto.getContact().getAddressType();
        	charSet = dto.getCharSet();
        	contactEmail = dto.getContact().getContactEmail();
        	contactFacsimile = dto.getContact().getContactFacsimile();
        	contactOrganization = dto.getContact().getContactOrganization();
        	contactPerson = dto.getContact().getContactPerson();
        	contactPosition = dto.getContact().getContactPosition();
        	contactVoice = dto.getContact().getContactVoice();
        	loggingLevel = dto.getLoggingLevel();
        	maxFeatures = dto.getMaxFeatures();
        	numDecimals = dto.getNumDecimals();
        	schemaBaseUrl = dto.getSchemaBaseUrl();
        	verbose = dto.isVerbose();
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
    /*public void load(DataDTO data) throws ConfigurationException {
        if (data != null) {
                this.data.load((DataDTO) data.clone());
        } else {
            throw new ConfigurationException(
                "load(DataDTO) expected a non-null value");
        }
    }
    
    public void load(DataDTO data, File baseDir) throws ConfigurationException {
    	if (data != null) {
    		if (this.data == null) {
    			this.data = new Data((DataDTO) data.clone(),baseDir);
    		} else {
    			this.data.load((DataDTO) data.clone());
    		}
    	} else {
    		throw new ConfigurationException(
    		"load(DataDTO) expected a non-null value");
    	}
    }*/

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
    /*public static WMSDTO getDTO(WMS wms) {
        return (WMSDTO) ((WMSDTO) wms.toDTO()).clone();
    }*/

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
    /*public static WFSDTO getDTO(WFS wfs) {
        WFSDTO w = (WFSDTO) wfs.toDTO();
        w = (WFSDTO) w.clone();

        return (w);
    }*/

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
    /*public static GeoServerDTO getDTO(GeoServer gs) {
        return (GeoServerDTO) ((GeoServerDTO) gs.toDTO()).clone();
    }*/

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
    /*public static DataDTO getDTO(Data dt) {
        return (DataDTO) ((DataDTO) dt.toDTO()).clone();
    }*/

    /**
     * toWMSDTO purpose.
     * 
     * <p>
     * Generates a WMSDTO object from the WMS object inside this instance.
     * </p>
     *
     * @return WMSDTO the generated object
     */
    /*public WMSDTO toWMSDTO() {
        return getDTO(wms);
    }*/

    /**
     * toWFSDTO purpose.
     * 
     * <p>
     * Generates a WFSDTO object from the WFS object inside this instance.
     * </p>
     *
     * @return WFSDTO the generated object
     */
    /*public WFSDTO toWFSDTO() {
        return getDTO(wfs);
    }*/

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
    /*public GeoServerDTO toGeoServerDTO() {
        return (GeoServerDTO) toDTO();
    }*/

    /**
     * toDataDTO purpose.
     * 
     * <p>
     * Generates a DataDTO object from the Data object inside this instance.
     * </p>
     *
     * @return DataDTO the generated object
     */
    /*public DataDTO toDataDTO() {
        return getDTO(data);
    }*/

    /**
     * toDTO purpose.
     * 
     * <p>
     * This method is package visible only, and returns a reference to the
     * GeoServerDTO. This method is unsafe, and should only be used with
     * extreme caution.
     * </p>
     *
     * @return DTO the generated object
     */
    public Object toDTO() {
    	GeoServerDTO dto = new GeoServerDTO();
    	dto.setCharSet(charSet);
    	dto.setLoggingLevel(loggingLevel);
    	dto.setMaxFeatures(maxFeatures);
    	dto.setNumDecimals(numDecimals);
    	dto.setSchemaBaseUrl(schemaBaseUrl);
    	dto.setVerbose(verbose);
    	
    	ContactDTO cdto = new ContactDTO();
    	dto.setContact(cdto);
    	
    	cdto.setAddress(address);
    	cdto.setAddressCity(addressCity);
    	cdto.setAddressCountry(addressCountry);
    	cdto.setAddressPostalCode(addressPostalCode);
    	cdto.setAddressState(addressState);
    	cdto.setAddressType(addressType);
    	cdto.setContactEmail(contactEmail);
    	cdto.setContactFacsimile(contactFacsimile);
    	cdto.setContactOrganization(contactOrganization);
    	cdto.setContactPerson(contactPerson);
    	cdto.setContactPosition(contactPosition);
    	cdto.setContactVoice(contactVoice);
    	
        return dto;
    }
	/**
	 * Access processor property.
	 * 
	 * @return Returns the processor.
	 */
	/*public ValidationProcessor getProcessor() {
		return processor;
	}*/

}
