/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vfny.geoserver.global.dto.ContactDTO;
import org.vfny.geoserver.global.dto.GeoServerDTO;


/**
 * complete configuration ser for the whole server
 *
 * @author Gabriel Rold?n
 * @author dzwiers
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: GeoServer.java,v 1.23 2004/09/09 16:54:19 cholmesny Exp $
 */
public class GeoServer extends GlobalLayerSupertype {

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
    private String adminUserName = "admin";
    private String adminPassword;
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
    private String onlineResource;
    
    /** Should we throw the stack traces back in responses? */
    private boolean verboseExceptions = false;

    /** Default Logging level */
    private Level loggingLevel = Logger.getLogger("org.vfny.geoserver")
                                       .getLevel();


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
     * getOnlineResource purpose.
     * 
     * <p>
     * Returns the online Resource.
     * </p>
     *
     * @return String the online Resource.
     */
    public String getOnlineResource() {
        return notNull(onlineResource);
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
     * The Schema Base URL for this instance.  This should generally be a local
     * reference, as GeoServer by default puts up the schemas that it needs
     * and references them.  It could be used to specify an alternate site for
     * the schemas, however, for example if a user didn't want their servlet
     * container hit every time someone did a validation, they could instead
     * store it on another machine.  I don't really know if this is useful to
     * anyone...
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
            Log4JFormatter.init("org.geotools", loggingLevel);
            Log4JFormatter.init("org.vfny.geoserver", loggingLevel);
            maxFeatures = dto.getMaxFeatures();
            numDecimals = dto.getNumDecimals();
            onlineResource = dto.getContact().getOnlineResource();
            schemaBaseUrl = dto.getSchemaBaseUrl();
            verbose = dto.isVerbose();
            adminUserName = dto.getAdminUserName();
            adminPassword = dto.getAdminPassword();
            verboseExceptions = dto.isVerboseExceptions();
        } else {
            throw new ConfigurationException(
                "load(GeoServerDTO) expected a non-null value");
        }
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
        dto.setAdminUserName(adminUserName);
        dto.setAdminPassword(adminPassword);
        dto.setVerboseExceptions(verboseExceptions);

        ContactDTO cdto = new ContactDTO();

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
        cdto.setOnlineResource(onlineResource);

        dto.setContact(cdto);

        return dto;
    }

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

    public String getAdminUserName() {
        return adminUserName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public String toString() {
        StringBuffer geoserver = new StringBuffer("[GeoServer: \n");
        geoserver.append("   maxFeatures - " + maxFeatures);
        geoserver.append("\n   verbose - " + verbose);
        geoserver.append("\n   numDecimals - " + numDecimals);
        geoserver.append("\n   charSet - " + charSet);
        geoserver.append("\n   loggingLevel - " + loggingLevel);
        geoserver.append("\n   adminUserName - " + adminUserName);
        geoserver.append("\n   adminPassword - " + adminPassword);

        return geoserver.toString();
    }
	
	/**
	 * Should we display stackTraces or not? (And give them a nice
     * little message instead?)
	 * 
	 * @return Returns the showStackTraces.
	 */
	public boolean isVerboseExceptions() {
		return verboseExceptions;
	}
	/**
	 * If set to true, response exceptions will throw their stack trace
     * back to the end user.
	 *
	 * @param showStackTraces The showStackTraces to set.
	 */
	public void setVerboseExceptions(boolean showStackTraces) {
		this.verboseExceptions = showStackTraces;
	}
	
}
