/*
 * Created on Jan 27, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.global;

import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.ContactConfig;
import org.vfny.geoserver.config.GlobalConfig;

/**
 * GeoServerConfigurationForm purpose.
 * <p>
 * Description of GeoServerConfigurationForm ...
 * </p>
 * 
 * <p>
 * Capabilities:
 * </p>
 * <ul>
 * <li>
 * Feature: description
 * </li>
 * </ul>
 * <p>
 * Example Use:
 * </p>
 * <pre><code>
 * GeoServerConfigurationForm x = new GeoServerConfigurationForm(...);
 * </code></pre>
 * 
 * @author User, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: GeoServerConfigurationForm.java,v 1.1 2004/01/31 00:27:29 jive Exp $
 */
public class GeoServerConfigurationForm extends ActionForm {
    
    private int maxFeatures;
    private boolean verbose;
    private int numDecimals;
    private String charset;
    private String baseURL;
    private String schemaBaseURL;
    private String loggingLevel;
    
    /** The name of the contact person */
    private String contactPerson;

    /** The name of the organization with which the contact is affiliated. */
    private String contactOrganization;

    /** The position of the contact within their organization. */
    private String contactPosition;

    /** The type of address specified, such as postal. */
    private String addressType;

    /** The actual street address. */
    private String address;

    /** The city of the address. */
    private String addressCity;

    /** The state/prov. of the address. */
    private String addressState;

    /** The postal code for the address. */
    private String addressPostalCode;

    /** The country of the address. */
    private String addressCountry;

    /** The contact phone number. */
    private String contactVoice;

    /** The contact Fax number. */
    private String contactFacsimile;

    /** The contact email address. */
    private String contactEmail;    
    
	private boolean verboseChecked;
    
    
    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);
       
        GlobalConfig globalConfig = (GlobalConfig) getServlet().getServletContext().getAttribute(GlobalConfig.CONFIG_KEY);

        maxFeatures = globalConfig.getMaxFeatures();
        verbose = globalConfig.isVerbose();
        verboseChecked = false;
        numDecimals = globalConfig.getNumDecimals();
        charset = globalConfig.getCharSet().name();
        baseURL = globalConfig.getBaseUrl();
        schemaBaseURL = globalConfig.getSchemaBaseUrl();
        if (globalConfig.getLoggingLevel() == null) {
            //@TODO - shouldn't have to do this.. should never return null
        	loggingLevel = Level.OFF.getName();   
        } else {
        	loggingLevel = globalConfig.getLoggingLevel().getName();
        }
        
        ContactConfig contactConfig = globalConfig.getContact();
        contactPerson = contactConfig.getContactPerson();
        contactOrganization = contactConfig.getContactOrganization();
        contactPosition = contactConfig.getContactPosition();
        
        addressType = contactConfig.getAddressType();
        address = contactConfig.getAddress();
        addressCity = contactConfig.getAddressCity();
        addressCountry = contactConfig.getAddressCountry();
        addressPostalCode = contactConfig.getAddressPostalCode();
        addressState = contactConfig.getAddressState();
        
        contactVoice = contactConfig.getContactVoice();
        contactFacsimile = contactConfig.getContactFacsimile();
        contactEmail = contactConfig.getContactEmail();
        
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }
	/**
	 * Access maxFeatures property.
	 * 
	 * @return Returns the maxFeatures.
	 */
	public int getMaxFeatures() {
		return maxFeatures;
	}

	/**
	 * Set maxFeatures to maxFeatures.
	 *
	 * @param maxFeatures The maxFeatures to set.
	 */
	public void setMaxFeatures(int maxFeatures) {
		this.maxFeatures = maxFeatures;
	}

	/**
	 * Access verbose property.
	 * 
	 * @return Returns the verbose.
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * Set verbose to verbose.
	 *
	 * @param verbose The verbose to set.
	 */
	public void setVerbose(boolean verbose) {
        verboseChecked = true;
		this.verbose = verbose;
	}

	/**
	 * Access verboseChecked property.
	 * 
	 * @return Returns the verboseChecked.
	 */
	public boolean isVerboseChecked() {
		return verboseChecked;
	}

	/**
	 * Set verboseChecked to verboseChecked.
	 *
	 * @param verboseChecked The verboseChecked to set.
	 */
	public void setVerboseChecked(boolean verboseChecked) {
		this.verboseChecked = verboseChecked;
	}

	/**
	 * Access numDecimals property.
	 * 
	 * @return Returns the numDecimals.
	 */
	public int getNumDecimals() {
		return numDecimals;
	}

	/**
	 * Set numDecimals to numDecimals.
	 *
	 * @param numDecimals The numDecimals to set.
	 */
	public void setNumDecimals(int numDecimals) {
		this.numDecimals = numDecimals;
	}

	/**
	 * Access charset property.
	 * 
	 * @return Returns the charset.
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * Set charset to charset.
	 *
	 * @param charset The charset to set.
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * Access baseURL property.
	 * 
	 * @return Returns the baseURL.
	 */
	public String getBaseURL() {
		return baseURL;
	}

	/**
	 * Set baseURL to baseURL.
	 *
	 * @param baseURL The baseURL to set.
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * Access schemaBaseURL property.
	 * 
	 * @return Returns the schemaBaseURL.
	 */
	public String getSchemaBaseURL() {
		return schemaBaseURL;
	}

	/**
	 * Set schemaBaseURL to schemaBaseURL.
	 *
	 * @param schemaBaseURL The schemaBaseURL to set.
	 */
	public void setSchemaBaseURL(String schemaBaseURL) {
		this.schemaBaseURL = schemaBaseURL;
	}

	/**
	 * Access loggingLevel property.
	 * 
	 * @return Returns the loggingLevel.
	 */
	public String getLoggingLevel() {
		return loggingLevel;
	}

	/**
	 * Set loggingLevel to loggingLevel.
	 *
	 * @param loggingLevel The loggingLevel to set.
	 */
	public void setLoggingLevel(String loggingLevel) {
		this.loggingLevel = loggingLevel;
	}

	/**
	 * Access address property.
	 * 
	 * @return Returns the address.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Set address to address.
	 *
	 * @param address The address to set.
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Access addressCity property.
	 * 
	 * @return Returns the addressCity.
	 */
	public String getAddressCity() {
		return addressCity;
	}

	/**
	 * Set addressCity to addressCity.
	 *
	 * @param addressCity The addressCity to set.
	 */
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	/**
	 * Access addressCountry property.
	 * 
	 * @return Returns the addressCountry.
	 */
	public String getAddressCountry() {
		return addressCountry;
	}

	/**
	 * Set addressCountry to addressCountry.
	 *
	 * @param addressCountry The addressCountry to set.
	 */
	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	/**
	 * Access addressPostalCode property.
	 * 
	 * @return Returns the addressPostalCode.
	 */
	public String getAddressPostalCode() {
		return addressPostalCode;
	}

	/**
	 * Set addressPostalCode to addressPostalCode.
	 *
	 * @param addressPostalCode The addressPostalCode to set.
	 */
	public void setAddressPostalCode(String addressPostalCode) {
		this.addressPostalCode = addressPostalCode;
	}

	/**
	 * Access addressState property.
	 * 
	 * @return Returns the addressState.
	 */
	public String getAddressState() {
		return addressState;
	}

	/**
	 * Set addressState to addressState.
	 *
	 * @param addressState The addressState to set.
	 */
	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	/**
	 * Access addressType property.
	 * 
	 * @return Returns the addressType.
	 */
	public String getAddressType() {
		return addressType;
	}

	/**
	 * Set addressType to addressType.
	 *
	 * @param addressType The addressType to set.
	 */
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	/**
	 * Access contactEmail property.
	 * 
	 * @return Returns the contactEmail.
	 */
	public String getContactEmail() {
		return contactEmail;
	}

	/**
	 * Set contactEmail to contactEmail.
	 *
	 * @param contactEmail The contactEmail to set.
	 */
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	/**
	 * Access contactFacsimile property.
	 * 
	 * @return Returns the contactFacsimile.
	 */
	public String getContactFacsimile() {
		return contactFacsimile;
	}

	/**
	 * Set contactFacsimile to contactFacsimile.
	 *
	 * @param contactFacsimile The contactFacsimile to set.
	 */
	public void setContactFacsimile(String contactFacsimile) {
		this.contactFacsimile = contactFacsimile;
	}

	/**
	 * Access contactOrganization property.
	 * 
	 * @return Returns the contactOrganization.
	 */
	public String getContactOrganization() {
		return contactOrganization;
	}

	/**
	 * Set contactOrganization to contactOrganization.
	 *
	 * @param contactOrganization The contactOrganization to set.
	 */
	public void setContactOrganization(String contactOrganization) {
		this.contactOrganization = contactOrganization;
	}

	/**
	 * Access contactPerson property.
	 * 
	 * @return Returns the contactPerson.
	 */
	public String getContactPerson() {
		return contactPerson;
	}

	/**
	 * Set contactPerson to contactPerson.
	 *
	 * @param contactPerson The contactPerson to set.
	 */
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	/**
	 * Access contactPosition property.
	 * 
	 * @return Returns the contactPosition.
	 */
	public String getContactPosition() {
		return contactPosition;
	}

	/**
	 * Set contactPosition to contactPosition.
	 *
	 * @param contactPosition The contactPosition to set.
	 */
	public void setContactPosition(String contactPosition) {
		this.contactPosition = contactPosition;
	}

	/**
	 * Access contactVoice property.
	 * 
	 * @return Returns the contactVoice.
	 */
	public String getContactVoice() {
		return contactVoice;
	}

	/**
	 * Set contactVoice to contactVoice.
	 *
	 * @param contactVoice The contactVoice to set.
	 */
	public void setContactVoice(String contactVoice) {
		this.contactVoice = contactVoice;
	}

}
