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
package org.vnfy.geoserver.config;


/**
 * Contact Purpose.
 * 
 * <p>Represents a Contact Information element such as: <code> &lt;ContactInformation&gt;<br>
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
 * @version $Id: Contact.java,v 1.1.2.1 2003/12/30 23:39:15 dmzwiers Exp $
 *
 */
public class Contact implements Cloneable{
	
	/*
	 * The list of text data fields represented by this class. 
	 */
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
    
    public Contact(){
    	defaultSettings();
    }
    
    private void defaultSettings(){
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
    
    public Contact(Contact c){
    	if(c == null){
    		defaultSettings();
    		return;
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
    
    public Object clone(){
    	return new Contact(this);
    }
    
    public boolean equals(Object obj){
    	Contact c = (Contact)obj;
		return (contactPerson == c.getContactPerson() &&
		(contactOrganization == c.getContactOrganization() &&
		(contactPosition == c.getContactPosition() &&
		(addressType == c.getAddressType() &&
		(address == c.getAddress() &&
		(addressCity == c.getAddressCity() &&
		(addressState == c.getAddressState() &&
		(addressPostalCode == c.getAddressPostalCode() &&
		(addressCountry == c.getAddressCountry() &&
		(contactVoice == c.getContactVoice() &&
		(contactFacsimile == c.getContactFacsimile() &&
		(contactEmail == c.getContactEmail()))))))))))));
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