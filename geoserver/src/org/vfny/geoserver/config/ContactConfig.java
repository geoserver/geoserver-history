/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2003, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vfny.geoserver.config;

import org.vfny.geoserver.global.dto.ContactDTO;
/**
 * ContactConfig Purpose.
 * 
 * <p>Represents a ContactConfig Information element such as: <code> &lt;ContactInformation&gt;<br>
 * &lt;ContactPersonPrimary&gt;<br>
 * &lt;ContactPerson&gt;Chris Holmes&lt;/ContactPerson&gt;<br>
 * &lt;ContactOrganization&gt;TOPP&lt;/ContactOrganization&gt;<br>
 * &lt;/ContactPersonPrimary&gt;<br>
 * &lt;ContactPosition&gt;Computer Scientist&lt;/ContactPosition&gt;<br>
 * &lt;ContactAddress&gt;<br>
 * &lt;AddressType&gt;postal&lt;/AddressType&gt;<br>
 * &lt;Address&gt;Street addresss here&lt;/Address&gt;<br>
 * &lt;City&gt;New York&lt;/City&gt;<br>
 * &lt;StateOrProvince&gt;New York&lt;/StateOrProvince&gt;<br>
 * &lt;PostCode&gt;0001&lt;/PostCode&gt;<br>
 * &lt;Country&gt;USA&lt;/Country&gt;<br>
 * &lt;/ContactAddress&gt;<br>
 * &lt;ContactVoiceTelephone&gt;+1 301 283-1569&lt;/ContactVoiceTelephone&gt;<br>
 * &lt;ContactFacsimileTelephone&gt;+1 301 283-1569&lt;/ContactFacsimileTelephone&gt;<br>
 * &lt;/ContactInformation&gt;<br></code></p>
 *
 * @author David Zwiers, Refractions Research, Inc.
 * @version $Id: ContactConfig.java,v 1.2.2.5 2004/01/07 21:36:13 dmzwiers Exp $
 *
 */
public class ContactConfig implements DataStructure{
	
	/**
	 * The name of the contact person 
	 */
    private String contactPerson;
    
    /**
     * The name of the organization with which the contact is affiliated.
     */
    private String contactOrganization;
    
    /**
     * The position of the contact within their organization. 
     */
    private String contactPosition;
    
    /**
     * The type of address specified, such as postal.
     */
    private String addressType;
    
    /**
     * The actual street address.
     */
    private String address;
    
    /**
     * The city of the address.
     */
    private String addressCity;
    
    /**
     * The state/prov. of the address.
     */
    private String addressState;
    
    /**
     * The postal code for the address.
     */
    private String addressPostalCode;
    
    /**
     * The country of the address.
     */
    private String addressCountry;
    
    /**
     * The contact phone number.
     */
    private String contactVoice;
    
    /**
     * The contact Fax number.
     */
    private String contactFacsimile;
    
    /**
     * The contact email address. 
     */
    private String contactEmail;
    
    /**
     * ContactConfig constructor.
     * <p>
     * Creates an empty ContactConfig object which is intended to represent 
     * the data required for a human contact.
     * 
     * @see defaultSettings()
     * </p>
     *
     */
    public ContactConfig(){
		contactPerson = "";
		contactOrganization = "";
		contactPosition = "";
		addressType = "";
		address = "";
		addressCity = "";
		addressState = "";
		addressPostalCode = "";
		addressCountry = "";
		contactVoice = "";
		contactFacsimile = "";
		contactEmail = "";
    }
        
    /**
     * ContactConfig constructor.
     * <p>
     * Creates a copy of the ContactConfig specified. None of the data is cloned, as 
     * String are stored in a hashtable in memory.
     * </p>
     * @param c The ContactConfig to create a copy of. 
     */
    public ContactConfig(ContactConfig c){
    	if(c == null){
			throw new NullPointerException();
    	}
		contactPerson = c.getContactPerson();
		contactOrganization = c.getContactOrganization();
		contactPosition = c.getContactPosition();
		addressType = c.getAddressType();
		address = c.getAddress();
		addressCity = c.getAddressCity();
		addressState = c.getAddressState();
		addressPostalCode = c.getAddressPostalCode();
		addressCountry = c.getAddressCountry();
		contactVoice = c.getContactVoice();
		contactFacsimile = c.getContactFacsimile();
		contactEmail = c.getContactEmail();
    }
        
	/**
	 * ContactConfig constructor.
	 * <p>
	 * Creates a copy of the ContactConfig specified. None of the data is cloned, as 
	 * String are stored in a hashtable in memory.
	 * </p>
	 * @param c The ContactConfig to create a copy of. 
	 */
	public ContactConfig(ContactDTO c){
		if(c == null){
			throw new NullPointerException();
		}
		contactPerson = c.getContactPerson();
		contactOrganization = c.getContactOrganization();
		contactPosition = c.getContactPosition();
		addressType = c.getAddressType();
		address = c.getAddress();
		addressCity = c.getAddressCity();
		addressState = c.getAddressState();
		addressPostalCode = c.getAddressPostalCode();
		addressCountry = c.getAddressCountry();
		contactVoice = c.getContactVoice();
		contactFacsimile = c.getContactFacsimile();
		contactEmail = c.getContactEmail();
	}
	
	/**
	 * Implement updateDTO.
	 * <p>
	 * Populates this ContactConfig with the data provided if the param is valid.
	 * </p>
	 * @see org.vfny.geoserver.config.DataStructure#updateDTO(java.lang.Object)
	 * 
	 * @param obj A ContactDTO to populate this object with.
	 * @return true when the param is valid and stored.
	 */
	public boolean updateDTO(Object obj){
		if(obj == null || !(obj instanceof ContactDTO))
			return false;
		ContactDTO c = (ContactDTO)obj;
		contactPerson = c.getContactPerson();
		contactOrganization = c.getContactOrganization();
		contactPosition = c.getContactPosition();
		addressType = c.getAddressType();
		address = c.getAddress();
		addressCity = c.getAddressCity();
		addressState = c.getAddressState();
		addressPostalCode = c.getAddressPostalCode();
		addressCountry = c.getAddressCountry();
		contactVoice = c.getContactVoice();
		contactFacsimile = c.getContactFacsimile();
		contactEmail = c.getContactEmail();
		return true;
	}
	
	/**
	 * Implement toDTO.
	 * <p>
	 * creates a representation of the data in a ContactDTO object.
	 * </p>
	 * @see org.vfny.geoserver.config.DataStructure#toDTO()
	 * 
	 * @return A ContactDTO object that represents the data in this object.
	 */
	public Object toDTO(){
		ContactDTO c = new ContactDTO();
		 c.setContactPerson(contactPerson);
		 c.setContactOrganization(contactOrganization);
		 c.setContactPosition(contactPosition);
		 c.setAddressType(addressType);
		 c.setAddress(address);
		 c.setAddressCity(addressCity);
		 c.setAddressState(addressState);
		 c.setAddressPostalCode(addressPostalCode);
		 c.setAddressCountry(addressCountry);
		 c.setContactVoice(contactVoice);
		 c.setContactFacsimile(contactFacsimile);
		 c.setContactEmail(contactEmail);
		return c;
	}
    
    /**
     * Implement clone.
     * <p>
     * Creates a clone of the object. For exact notes see @see ContactConfig(ContactConfig) . 
     * </p>
     * @see java.lang.Object#clone()
     * 
     * @return A new ContactConfig object. 
     */
    public Object clone(){
    	return new ContactConfig(this);
    }
    
	/**
	 * getAddress purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * getAddressCity purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getAddressCity() {
		return addressCity;
	}

	/**
	 * getAddressCountry purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getAddressCountry() {
		return addressCountry;
	}

	/**
	 * getAddressPostalCode purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getAddressPostalCode() {
		return addressPostalCode;
	}

	/**
	 * getAddressState purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getAddressState() {
		return addressState;
	}

	/**
	 * getAddressType purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getAddressType() {
		return addressType;
	}

	/**
	 * getContactEmail purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getContactEmail() {
		return contactEmail;
	}

	/**
	 * getContactFacsimile purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getContactFacsimile() {
		return contactFacsimile;
	}

	/**
	 * getContactOrganization purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getContactOrganization() {
		return contactOrganization;
	}

	/**
	 * getContactPerson purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getContactPerson() {
		return contactPerson;
	}

	/**
	 * getContactPosition purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getContactPosition() {
		return contactPosition;
	}

	/**
	 * getContactVoice purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getContactVoice() {
		return contactVoice;
	}

	/**
	 * setAddress purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setAddress(String string) {
		if(string != null)
		address = string;
	}

	/**
	 * setAddressCity purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setAddressCity(String string) {
		if(string != null)
		addressCity = string;
	}

	/**
	 * setAddressCountry purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setAddressCountry(String string) {
		if(string != null)
		addressCountry = string;
	}

	/**
	 * setAddressPostalCode purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setAddressPostalCode(String string) {
		if(string != null)
		addressPostalCode = string;
	}

	/**
	 * setAddressState purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setAddressState(String string) {
		if(string != null)
		addressState = string;
	}

	/**
	 * setAddressType purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setAddressType(String string) {
		if(string != null)
		addressType = string;
	}

	/**
	 * setContactEmail purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setContactEmail(String string) {
		if(string != null)
		contactEmail = string;
	}

	/**
	 * setContactFacsimile purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setContactFacsimile(String string) {
		if(string != null)
		contactFacsimile = string;
	}

	/**
	 * setContactOrganization purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setContactOrganization(String string) {
		if(string != null)
		contactOrganization = string;
	}

	/**
	 * setContactPerson purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setContactPerson(String string) {
		if(string != null)
		contactPerson = string;
	}

	/**
	 * setContactPosition purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setContactPosition(String string) {
		if(string != null)
		contactPosition = string;
	}

	/**
	 * setContactVoice purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setContactVoice(String string) {
		if(string != null)
		contactVoice = string;
	}

}