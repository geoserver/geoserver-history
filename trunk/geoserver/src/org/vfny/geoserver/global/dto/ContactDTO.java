/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

/**
 * Data Transfer Object for Contact information.
 * 
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 * 
 * <p>
 * Represents a ContactDTO Information element such as:
 * </p>
 * <pre><code>
 * {ContactInformation}
 *   {ContactPersonPrimary}
 *     {ContactPerson}Chris Holmes{/ContactPerson}
 *     {ContactOrganization}TOPP{/ContactOrganization}
 *   {/ContactPersonPrimary}
 *   {ContactPosition}Computer Scientist{/ContactPosition}
 *   {ContactAddress}
 *     {AddressType}postal{/AddressType}
 *     {Address}Street addresss here{/Address}
 *     {City}New York{/City}
 *     {StateOrProvince}New York{/StateOrProvince}
 *     {PostCode}0001{/PostCode}
 *     {Country}USA{/Country}
 *   {/ContactAddress}
 *   {ContactVoiceTelephone}+1 301 283-1569{/ContactVoiceTelephone}
 *   {ContactFacsimileTelephone}+1 301 283-1569{/ContactFacsimileTelephone}
 * {/ContactInformation}
 * </code></pre>
 *
 * @author David Zwiers, Refractions Research, Inc.
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: ContactDTO.java,v 1.4 2004/01/31 00:27:26 jive Exp $
 */
public final class ContactDTO implements DataTransferObject {

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
	 * The contact online resource.
	 * 
	 * @uml.property name="onlineResource" multiplicity="(0 1)"
	 */
	private String onlineResource;


    /**
     * ContactConfig constructor.
     * 
     * <p>
     * does nothing
     * </p>
     */
    public ContactDTO() {
    }

    /**
     * Contact Data Transfer Object constructor.
     * 
     * <p>
     * Creates a copy of the ContactDTO specified.
     * </p>
     *
     * @param c The ContactDTO to create a copy of.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public ContactDTO(ContactDTO c) {
        if (c == null) {
            throw new NullPointerException("Requires a non null ContactDTO");
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
        onlineResource = c.getOnlineResource();
    }

    /**
     * Implement clone.
     * 
     * <p>
     * Creates a clone of the object. For exact notes see
     * </p>
     *
     * @return A new ContactConfig object.
     *
     * @see ContactConfig(ContactConfig).
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new ContactDTO(this);
    }

    /**
     * Implement equals.
     * 
     * <p>
     * Checks to see that the ContactConfig passed in is the same as this
     * ContactConfig.
     * </p>
     *
     * @param obj A ContactConfig object.
     *
     * @return true when they are the same.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof ContactDTO) || (obj == null)) {
            return false;
        }

        ContactDTO c = (ContactDTO) obj;

        return ((contactPerson == c.getContactPerson())
        && ((contactOrganization == c.getContactOrganization())
        && ((contactPosition == c.getContactPosition())
        && ((addressType == c.getAddressType())
        && ((address == c.getAddress())
        && ((addressCity == c.getAddressCity())
        && ((addressState == c.getAddressState())
        && ((addressPostalCode == c.getAddressPostalCode())
        && ((addressCountry == c.getAddressCountry())
        && ((contactVoice == c.getContactVoice())
        && ((contactFacsimile == c.getContactFacsimile())
        && ((onlineResource == c.getOnlineResource())
        && (contactEmail == c.getContactEmail())))))))))))));
    }

    public int hashCode() {
        int i = 1;

        if (contactPerson != null) {
            i *= contactPerson.hashCode();
        }

        if (address != null) {
            i *= address.hashCode();
        }

        if (contactVoice != null) {
            i *= contactVoice.hashCode();
        }

        if (contactEmail != null) {
            i *= contactEmail.hashCode();
        }

        return i;
    }

	/**
	 * getAddress purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="address"
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * getAddressCity purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="addressCity"
	 */
	public String getAddressCity() {
		return addressCity;
	}

	/**
	 * getAddressCountry purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="addressCountry"
	 */
	public String getAddressCountry() {
		return addressCountry;
	}

	/**
	 * getAddressPostalCode purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="addressPostalCode"
	 */
	public String getAddressPostalCode() {
		return addressPostalCode;
	}

	/**
	 * getAddressState purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="addressState"
	 */
	public String getAddressState() {
		return addressState;
	}

	/**
	 * getAddressType purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="addressType"
	 */
	public String getAddressType() {
		return addressType;
	}

	/**
	 * getContactEmail purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="contactEmail"
	 */
	public String getContactEmail() {
		return contactEmail;
	}

	/**
	 * getContactFacsimile purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="contactFacsimile"
	 */
	public String getContactFacsimile() {
		return contactFacsimile;
	}

	/**
	 * getContactOrganization purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="contactOrganization"
	 */
	public String getContactOrganization() {
		return contactOrganization;
	}

	/**
	 * getContactPerson purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="contactPerson"
	 */
	public String getContactPerson() {
		return contactPerson;
	}

	/**
	 * getContactPosition purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="contactPosition"
	 */
	public String getContactPosition() {
		return contactPosition;
	}

	/**
	 * getContactVoice purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @return
	 * 
	 * @uml.property name="contactVoice"
	 */
	public String getContactVoice() {
		return contactVoice;
	}

	/**
	 * setAddress purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 * 
	 * @uml.property name="address"
	 */
	public void setAddress(String string) {
		if (string != null) {
			address = string;
		}
	}

	/**
	 * setAddressCity purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 * 
	 * @uml.property name="addressCity"
	 */
	public void setAddressCity(String string) {
		if (string != null) {
			addressCity = string;
		}
	}

	/**
	 * setAddressCountry purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 * 
	 * @uml.property name="addressCountry"
	 */
	public void setAddressCountry(String string) {
		if (string != null) {
			addressCountry = string;
		}
	}

	/**
	 * setAddressPostalCode purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 * 
	 * @uml.property name="addressPostalCode"
	 */
	public void setAddressPostalCode(String string) {
		if (string != null) {
			addressPostalCode = string;
		}
	}

	/**
	 * setAddressState purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 * 
	 * @uml.property name="addressState"
	 */
	public void setAddressState(String string) {
		if (string != null) {
			addressState = string;
		}
	}

	/**
	 * setAddressType purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 * 
	 * @uml.property name="addressType"
	 */
	public void setAddressType(String string) {
		if (string != null) {
			addressType = string;
		}
	}

	/**
	 * setContactEmail purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 * 
	 * @uml.property name="contactEmail"
	 */
	public void setContactEmail(String string) {
		if (string != null) {
			contactEmail = string;
		}
	}

	/**
	 * setContactFacsimile purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 * 
	 * @uml.property name="contactFacsimile"
	 */
	public void setContactFacsimile(String string) {
		if (string != null) {
			contactFacsimile = string;
		}
	}

	/**
	 * setContactOrganization purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 * 
	 * @uml.property name="contactOrganization"
	 */
	public void setContactOrganization(String string) {
		if (string != null) {
			contactOrganization = string;
		}
	}

	/**
	 * setContactPerson purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 * 
	 * @uml.property name="contactPerson"
	 */
	public void setContactPerson(String string) {
		if (string != null) {
			contactPerson = string;
		}
	}

	/**
	 * setContactPosition purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 * 
	 * @uml.property name="contactPosition"
	 */
	public void setContactPosition(String string) {
		if (string != null) {
			contactPosition = string;
		}
	}

	/**
	 * setContactVoice purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 * 
	 * @param string
	 * 
	 * @uml.property name="contactVoice"
	 */
	public void setContactVoice(String string) {
		if (string != null) {
			contactVoice = string;
		}
	}

	/**
	 * @return Returns the onlineResource.
	 * 
	 * @uml.property name="onlineResource"
	 */
	public String getOnlineResource() {
		return onlineResource;
	}

	/**
	 * @param onlineResource The onlineResource to set.
	 * 
	 * @uml.property name="onlineResource"
	 */
	public void setOnlineResource(String onlineResource) {
		if (onlineResource != null) {
			this.onlineResource = onlineResource;
		}
	}

}
