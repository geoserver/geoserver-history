/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
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
 * @version $Id$
 */
public final class ContactDTO implements DataTransferObject {
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

    /**
     * The contact online resource.
     *
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
        && ((onlineResource == c.getOnlineResource()) && (contactEmail == c.getContactEmail())))))))))))));
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
     */
    public void setContactVoice(String string) {
        if (string != null) {
            contactVoice = string;
        }
    }

    /**
     * @return Returns the onlineResource.
     *
     */
    public String getOnlineResource() {
        return onlineResource;
    }

    /**
     * @param onlineResource The onlineResource to set.
     *
     */
    public void setOnlineResource(String onlineResource) {
        if (onlineResource != null) {
            this.onlineResource = onlineResource;
        }
    }
}
