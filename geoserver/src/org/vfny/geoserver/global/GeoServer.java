/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.vfny.geoserver.global.dto.ContactDTO;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * complete configuration ser for the whole server
 *
 * @author Gabriel Roldán
 * @author dzwiers
 * @version $Id: GeoServer.java,v 1.16 2004/03/30 04:49:11 cholmesny Exp $
 */
public class GeoServer extends GlobalLayerSupertype {
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
    private String title;
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

    /** Default Logging level */
    private Level loggingLevel = Logger.getLogger("org.vfny.geoserver")
                                       .getLevel();

    /**
     * The reference to the wms configuration for this instance
     *
     * @return DOCUMENT ME!
     */

    //private WMS wms;

    /**
     * The reference to the wfs configuration for this instance
     *
     * @return DOCUMENT ME!
     */

    //private WFS wfs;

    /**
     * The validation processor
     *
     * @return DOCUMENT ME!
     */

    //ValidationProcessor processor;

    /**
     * The reference to the data configuration for this instance (formerly
     * CatalogConfig)
     *
     * @return DOCUMENT ME!
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
        if (charSet != null) {
            return charSet;
        }

        return Charset.forName("UTF-8");
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
     * The Schema Base URL for this instance.  This should generally be a
     * local reference, as GeoServer by default puts up the schemas that it
     * needs and references them.  It could be used to specify an alternate
     * site for the schemas, however, for example if a user didn't want their
     * servlet container hit every time someone did a validation, they could
     * instead store it on another machine.  I don't really know if this is
     * useful to anyone...
     * </p>
     *
     * @return String the Schema Base URL for this instance.
     *
     * @task TODO: Right now this is broken, and I'm not quite sure if there's
     *       an elegant way to have this return the local schemas.  Perhaps we
     *       should just have it return 'local', and then the users of this
     *       method can do the local referencing themselves.  For now no one
     *       is using this  method, perhaps we should just leave it out for
     *       1.2.0, as it's very  obscure.  I think I only added it originally
     *       because I didn't want to  go through the busy work of cleaning up
     *       and figuring out how to copy over the ogc schemas.
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
     * @param dto DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */

    /*public WMS getWMS() {
       return wms;
       }*/

    /**
     * Gets the config for the WFS.
     *
     * @param dto DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
     */

    /*public WFS getWFS() {
       return wfs;
       }*/

    /**
     * Gets the config for the Data.
     *
     * @param dto DOCUMENT ME!
     *
     * @throws ConfigurationException DOCUMENT ME!
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
     * @param dto WMSDTO The wms data.
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
     * @param dto WMSDTO The wms data.
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
     * @param dto WMSDTO
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
     * @param dto WFSDTO
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
     * @param dto GeoServerDTO
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
     * @return DOCUMENT ME!
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

    /**
     * DOCUMENT ME!
     *
     * @return Returns the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * DOCUMENT ME!
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Property representing the contact party (person, position or
     * organization).
     * 
     * <p>
     * This is a derived property.
     * </p>
     *
     * @return Contact party (person, position or organization), null if
     *         unknown
     */
    public String getContactParty() {
        if ((getContactPerson() != null) && (getContactPerson().length() != 0)) {
            return getContactPerson(); // ie Chris Holmes 
        }

        if ((getContactPosition() != null)
                && (getContactPosition().length() != 0)) {
            return getContactPosition(); // ie Lead Developer 
        }

        if ((getContactOrganization() != null)
                && (getContactOrganization().length() != 0)) {
            return getContactOrganization(); // ie TOPP 
        }

        return null;
    }
}
