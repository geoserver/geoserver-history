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
 * @author $Author: cholmesny $ (last modification)
 * @version $Id: GeoServerConfigurationForm.java,v 1.3 2004/09/09 17:04:42 cholmesny Exp $
 */
public class GeoServerConfigurationForm extends ActionForm {

	/**
	 * 
	 * @uml.property name="maxFeatures" multiplicity="(0 1)"
	 */
	private int maxFeatures;

	/**
	 * 
	 * @uml.property name="verbose" multiplicity="(0 1)"
	 */
	private boolean verbose;

	/**
	 * 
	 * @uml.property name="numDecimals" multiplicity="(0 1)"
	 */
	private int numDecimals;

	/**
	 * 
	 * @uml.property name="charset" multiplicity="(0 1)"
	 */
	private String charset;

	/**
	 * 
	 * @uml.property name="baseURL" multiplicity="(0 1)"
	 */
	private String baseURL;

	/**
	 * 
	 * @uml.property name="schemaBaseURL" multiplicity="(0 1)"
	 */
	private String schemaBaseURL;

	/**
	 * 
	 * @uml.property name="loggingLevel" multiplicity="(0 1)"
	 */
	private String loggingLevel;

	/**
	 * 
	 * @uml.property name="adminUserName" multiplicity="(0 1)"
	 */
	private String adminUserName;

	/**
	 * 
	 * @uml.property name="adminPassword" multiplicity="(0 1)"
	 */
	private String adminPassword;

	/**
	 * 
	 * @uml.property name="verboseExceptions" multiplicity="(0 1)"
	 */
	private boolean verboseExceptions;

	/**
	 * The name of the contact person
	 * 
	 * @uml.property name="contactPerson" multiplicity="(0 1)"
	 */
	private String contactPerson;

	/**
	 * The name of the organization with which the contact is affiliated.
	 * 
	 * @uml.property name="contactOrganization" multiplicity="(0 1)"
	 */
	private String contactOrganization;

	/**
	 * The position of the contact within their organization.
	 * 
	 * @uml.property name="contactPosition" multiplicity="(0 1)"
	 */
	private String contactPosition;

	/**
	 * The type of address specified, such as postal.
	 * 
	 * @uml.property name="addressType" multiplicity="(0 1)"
	 */
	private String addressType;

	/**
	 * The actual street address.
	 * 
	 * @uml.property name="address" multiplicity="(0 1)"
	 */
	private String address;

	/**
	 * The city of the address.
	 * 
	 * @uml.property name="addressCity" multiplicity="(0 1)"
	 */
	private String addressCity;

	/**
	 * The state/prov. of the address.
	 * 
	 * @uml.property name="addressState" multiplicity="(0 1)"
	 */
	private String addressState;

	/**
	 * The postal code for the address.
	 * 
	 * @uml.property name="addressPostalCode" multiplicity="(0 1)"
	 */
	private String addressPostalCode;

	/**
	 * The country of the address.
	 * 
	 * @uml.property name="addressCountry" multiplicity="(0 1)"
	 */
	private String addressCountry;

	/**
	 * The contact phone number.
	 * 
	 * @uml.property name="contactVoice" multiplicity="(0 1)"
	 */
	private String contactVoice;

	/**
	 * The contact Fax number.
	 * 
	 * @uml.property name="contactFacsimile" multiplicity="(0 1)"
	 */
	private String contactFacsimile;

	/**
	 * The contact email address.
	 * 
	 * @uml.property name="contactEmail" multiplicity="(0 1)"
	 */
	private String contactEmail;

	/**
	 * 
	 * @uml.property name="verboseChecked" multiplicity="(0 1)"
	 */
	private boolean verboseChecked;

	/**
	 * 
	 * @uml.property name="verboseExceptionsChecked" multiplicity="(0 1)"
	 */
	private boolean verboseExceptionsChecked;
	
	/** log to disk ? **/
	private String logLocation;
	 
    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);
       
        GlobalConfig globalConfig = (GlobalConfig) getServlet().getServletContext().getAttribute(GlobalConfig.CONFIG_KEY);

        maxFeatures = globalConfig.getMaxFeatures();
        verbose = globalConfig.isVerbose();
        verboseExceptions = globalConfig.isVerboseExceptions();
        verboseChecked = false;
        verboseExceptionsChecked = false;
        numDecimals = globalConfig.getNumDecimals();
        charset = globalConfig.getCharSet().name();
        baseURL = globalConfig.getBaseUrl();
        schemaBaseURL = globalConfig.getSchemaBaseUrl();
        adminUserName = globalConfig.getAdminUserName();
		adminPassword = globalConfig.getAdminPassword();
        if (globalConfig.getLoggingLevel() == null) {
            //@TODO - shouldn't have to do this.. should never return null
        	loggingLevel = Level.OFF.getName();   
        } else {
        	loggingLevel = globalConfig.getLoggingLevel().getName();
        }
        logLocation = globalConfig.getLogLocation();
        
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
	 * 
	 * @uml.property name="maxFeatures"
	 */
	public int getMaxFeatures() {
		return maxFeatures;
	}

	/**
	 * Set maxFeatures to maxFeatures.
	 * 
	 * @param maxFeatures The maxFeatures to set.
	 * 
	 * @uml.property name="maxFeatures"
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
	 * 
	 * @uml.property name="verbose"
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
		 * 
		 * @uml.property name="verboseChecked"
		 */
		public void setVerboseChecked(boolean verboseChecked) {
			this.verboseChecked = verboseChecked;
		}



	/**
		 * Access verboseChecked property.
		 * 
		 * @return Returns the verboseChecked.
		 */
		public boolean isVerboseExceptionsChecked() {
			return verboseExceptionsChecked;
		}

		/**
		 * Set verboseChecked to verboseChecked.
		 * 
		 * @param verboseChecked The verboseChecked to set.
		 * 
		 * @uml.property name="verboseExceptionsChecked"
		 */
		public void setVerboseExceptionsChecked(boolean verboseExceptionsChecked) {
			this.verboseExceptionsChecked = verboseExceptionsChecked;
		}


	
	/**
	 * Access verboseExceptions property.
	 * 
	 * @return Returns the verboseExceptions.
	 */
	public boolean isVerboseExceptions() {
		return verboseExceptions;
	}

	/**
	 * Set verboseExceptions to verboseExceptions.
	 * 
	 * @param verboseExceptions The verboseExceptions to set.
	 * 
	 * @uml.property name="verboseExceptions"
	 */
	public void setVerboseExceptions(boolean verboseExceptions) {
		verboseExceptionsChecked = true;
		this.verboseExceptions = verboseExceptions;
	}

	/**
	 * Access numDecimals property.
	 * 
	 * @return Returns the numDecimals.
	 * 
	 * @uml.property name="numDecimals"
	 */
	public int getNumDecimals() {
		return numDecimals;
	}

	/**
	 * Set numDecimals to numDecimals.
	 * 
	 * @param numDecimals The numDecimals to set.
	 * 
	 * @uml.property name="numDecimals"
	 */
	public void setNumDecimals(int numDecimals) {
		this.numDecimals = numDecimals;
	}

	/**
	 * Access charset property.
	 * 
	 * @return Returns the charset.
	 * 
	 * @uml.property name="charset"
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * Set charset to charset.
	 * 
	 * @param charset The charset to set.
	 * 
	 * @uml.property name="charset"
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * Access baseURL property.
	 * 
	 * @return Returns the baseURL.
	 * 
	 * @uml.property name="baseURL"
	 */
	public String getBaseURL() {
		return baseURL;
	}

	/**
	 * Set baseURL to baseURL.
	 * 
	 * @param baseURL The baseURL to set.
	 * 
	 * @uml.property name="baseURL"
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * Access schemaBaseURL property.
	 * 
	 * @return Returns the schemaBaseURL.
	 * 
	 * @uml.property name="schemaBaseURL"
	 */
	public String getSchemaBaseURL() {
		return schemaBaseURL;
	}

	/**
	 * Set schemaBaseURL to schemaBaseURL.
	 * 
	 * @param schemaBaseURL The schemaBaseURL to set.
	 * 
	 * @uml.property name="schemaBaseURL"
	 */
	public void setSchemaBaseURL(String schemaBaseURL) {
		this.schemaBaseURL = schemaBaseURL;
	}

	/**
	 * Access loggingLevel property.
	 * 
	 * @return Returns the loggingLevel.
	 * 
	 * @uml.property name="loggingLevel"
	 */
	public String getLoggingLevel() {
		return loggingLevel;
	}

	/**
	 * Set loggingLevel to loggingLevel.
	 * 
	 * @param loggingLevel The loggingLevel to set.
	 * 
	 * @uml.property name="loggingLevel"
	 */
	public void setLoggingLevel(String loggingLevel) {
		this.loggingLevel = loggingLevel;
	}

	/**
	 * Access address property.
	 * 
	 * @return Returns the address.
	 * 
	 * @uml.property name="address"
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Set address to address.
	 * 
	 * @param address The address to set.
	 * 
	 * @uml.property name="address"
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Access addressCity property.
	 * 
	 * @return Returns the addressCity.
	 * 
	 * @uml.property name="addressCity"
	 */
	public String getAddressCity() {
		return addressCity;
	}

	/**
	 * Set addressCity to addressCity.
	 * 
	 * @param addressCity The addressCity to set.
	 * 
	 * @uml.property name="addressCity"
	 */
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	/**
	 * Access addressCountry property.
	 * 
	 * @return Returns the addressCountry.
	 * 
	 * @uml.property name="addressCountry"
	 */
	public String getAddressCountry() {
		return addressCountry;
	}

	/**
	 * Set addressCountry to addressCountry.
	 * 
	 * @param addressCountry The addressCountry to set.
	 * 
	 * @uml.property name="addressCountry"
	 */
	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	/**
	 * Access addressPostalCode property.
	 * 
	 * @return Returns the addressPostalCode.
	 * 
	 * @uml.property name="addressPostalCode"
	 */
	public String getAddressPostalCode() {
		return addressPostalCode;
	}

	/**
	 * Set addressPostalCode to addressPostalCode.
	 * 
	 * @param addressPostalCode The addressPostalCode to set.
	 * 
	 * @uml.property name="addressPostalCode"
	 */
	public void setAddressPostalCode(String addressPostalCode) {
		this.addressPostalCode = addressPostalCode;
	}

	/**
	 * Access addressState property.
	 * 
	 * @return Returns the addressState.
	 * 
	 * @uml.property name="addressState"
	 */
	public String getAddressState() {
		return addressState;
	}

	/**
	 * Set addressState to addressState.
	 * 
	 * @param addressState The addressState to set.
	 * 
	 * @uml.property name="addressState"
	 */
	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	/**
	 * Access addressType property.
	 * 
	 * @return Returns the addressType.
	 * 
	 * @uml.property name="addressType"
	 */
	public String getAddressType() {
		return addressType;
	}

	/**
	 * Set addressType to addressType.
	 * 
	 * @param addressType The addressType to set.
	 * 
	 * @uml.property name="addressType"
	 */
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	/**
	 * Access contactEmail property.
	 * 
	 * @return Returns the contactEmail.
	 * 
	 * @uml.property name="contactEmail"
	 */
	public String getContactEmail() {
		return contactEmail;
	}

	/**
	 * Set contactEmail to contactEmail.
	 * 
	 * @param contactEmail The contactEmail to set.
	 * 
	 * @uml.property name="contactEmail"
	 */
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	/**
	 * Access contactFacsimile property.
	 * 
	 * @return Returns the contactFacsimile.
	 * 
	 * @uml.property name="contactFacsimile"
	 */
	public String getContactFacsimile() {
		return contactFacsimile;
	}

	/**
	 * Set contactFacsimile to contactFacsimile.
	 * 
	 * @param contactFacsimile The contactFacsimile to set.
	 * 
	 * @uml.property name="contactFacsimile"
	 */
	public void setContactFacsimile(String contactFacsimile) {
		this.contactFacsimile = contactFacsimile;
	}

	/**
	 * Access contactOrganization property.
	 * 
	 * @return Returns the contactOrganization.
	 * 
	 * @uml.property name="contactOrganization"
	 */
	public String getContactOrganization() {
		return contactOrganization;
	}

	/**
	 * Set contactOrganization to contactOrganization.
	 * 
	 * @param contactOrganization The contactOrganization to set.
	 * 
	 * @uml.property name="contactOrganization"
	 */
	public void setContactOrganization(String contactOrganization) {
		this.contactOrganization = contactOrganization;
	}

	/**
	 * Access contactPerson property.
	 * 
	 * @return Returns the contactPerson.
	 * 
	 * @uml.property name="contactPerson"
	 */
	public String getContactPerson() {
		return contactPerson;
	}

	/**
	 * Set contactPerson to contactPerson.
	 * 
	 * @param contactPerson The contactPerson to set.
	 * 
	 * @uml.property name="contactPerson"
	 */
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	/**
	 * Access contactPosition property.
	 * 
	 * @return Returns the contactPosition.
	 * 
	 * @uml.property name="contactPosition"
	 */
	public String getContactPosition() {
		return contactPosition;
	}

	/**
	 * Set contactPosition to contactPosition.
	 * 
	 * @param contactPosition The contactPosition to set.
	 * 
	 * @uml.property name="contactPosition"
	 */
	public void setContactPosition(String contactPosition) {
		this.contactPosition = contactPosition;
	}

	/**
	 * Access contactVoice property.
	 * 
	 * @return Returns the contactVoice.
	 * 
	 * @uml.property name="contactVoice"
	 */
	public String getContactVoice() {
		return contactVoice;
	}

	/**
	 * Set contactVoice to contactVoice.
	 * 
	 * @param contactVoice The contactVoice to set.
	 * 
	 * @uml.property name="contactVoice"
	 */
	public void setContactVoice(String contactVoice) {
		this.contactVoice = contactVoice;
	}

	/**
	 * 
	 * @uml.property name="adminUserName"
	 */
	//No sets yet, they will be needed for login config page though.
	public String getAdminUserName() {
		return adminUserName;
	}

	/**
	 * 
	 * @uml.property name="adminPassword"
	 */
	public String getAdminPassword() {
		return adminPassword;
	}
	
	/**
	 * @return The string representation of the path on disk in which the 
	 * server logs to.
	 */
	public String getLogLocation() {
		return logLocation;
	}
	
	/**
	 * @param logLocation The string representation of the path on disk in which 
	 * the server logs to.
	 */
	public void setLogLocation(String logLocation) {
		this.logLocation = logLocation;
	}

}
