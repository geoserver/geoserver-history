/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.w3c.dom.*;


/**
 * represents a ContactInformation element such as: <code> &lt;ContactInformation&gt;<br>
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
 * &lt;/ContactInformation&gt;<br></code>
 *
 * @author Gabriel Roldán
 * @version $Id: ContactConfig.java,v 1.1.2.2 2003/11/14 20:39:14 groldan Exp $
 *
 * @task REVISIT: may be it will be necessary to create a real contact
 *       hierarchy if we plan to add Catalog service.
 */
public class ContactConfig extends AbstractConfig {
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

    /**
     * Creates a new ContactConfig object.
     *
     * @param contactInfoElement DOCUMENT ME!
     */
    public ContactConfig(Element contactInfoElement) {
        if (contactInfoElement == null) {
            return;
        }

        Element elem;
        NodeList nodeList;
        elem = getChildElement(contactInfoElement, "ContactPersonPrimary");

        if (elem != null) {
            this.contactPerson = getChildText(elem, "ContactPerson");
            this.contactOrganization = getChildText(elem, "ContactOrganization");
        }

        this.contactPosition = getChildText(contactInfoElement,
                "ContactPosition");
        elem = getChildElement(contactInfoElement, "ContactAddress");

        if (elem != null) {
            this.addressType = getChildText(elem, "AddressType");
            this.address = getChildText(elem, "Address");
            this.addressCity = getChildText(elem, "City");
            this.addressState = getChildText(elem, "StateOrProvince");
            this.addressPostalCode = getChildText(elem, "PostCode");
            this.addressCountry = getChildText(elem, "Country");
        }

        this.contactVoice = getChildText(contactInfoElement,
                "ContactVoiceTelephone");
        this.contactFacsimile = getChildText(contactInfoElement,
                "ContactFacsimileTelephone");
        this.contactEmail = getChildText(contactInfoElement,
                "ContactElectronicMailAddress");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAddress() {
        return notNull(address);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAddressCity() {
        return notNull(addressCity);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAddressCountry() {
        return notNull(addressCountry);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAddressPostalCode() {
        return notNull(addressPostalCode);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAddressState() {
        return notNull(addressState);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAddressType() {
        return notNull(addressType);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContactEmail() {
        return notNull(contactEmail);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContactFacsimile() {
        return notNull(contactFacsimile);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContactOrganization() {
        return notNull(contactOrganization);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContactPerson() {
        return notNull(contactPerson);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContactPosition() {
        return notNull(contactPosition);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContactVoice() {
        return notNull(contactVoice);
    }
}
