/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config;

/**
 * GeoServer contact information.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public interface ContactInfo {

    /**
     * Identifier.
     */
    String getId();

    /**
     * @uml.property name="address"
     */
    String getAddress();

    /**
     * @uml.property name="address"
     */
    void setAddress(String address);

    /**
     * @uml.property name="addressCity"
     */
    String getAddressCity();

    /**
     * @uml.property name="addressCity"
     */
    void setAddressCity(String addressCity);

    /**
     * @uml.property name="addressCountry"
     */
    String getAddressCountry();

    /**
     * @uml.property name="addressCountry"
     */
    void setAddressCountry(String addressCountry);

    /**
     * @uml.property name="addressPostalCode"
     */
    String getAddressPostalCode();

    /**
     * @uml.property name="addressPostalCode"
     */
    void setAddressPostalCode(String addressPostalCode);

    /**
     * @uml.property name="addressState"
     */
    String getAddressState();

    /**
     * @uml.property name="addressState"
     */
    void setAddressState(String addressState);

    /**
     * @uml.property name="addressType"
     */
    String getAddressType();

    /**
     * @uml.property name="addressType"
     */
    void setAddressType(String addressType);

    /**
     * @uml.property name="contactEmail"
     */
    String getContactEmail();

    /**
     * @uml.property name="contactEmail"
     */
    void setContactEmail(String contactEmail);

    /**
     * @uml.property name="contactFacsimile"
     */
    String getContactFacsimile();

    /**
     * @uml.property name="contactFacsimile"
     */
    void setContactFacsimile(String contactFacsimile);

    /**
     * @uml.property name="contactOrganization"
     */
    String getContactOrganization();

    /**
     * @uml.property name="contactOrganization"
     */
    void setContactOrganization(String contactOrganization);

    /**
     * @uml.property name="contactPerson"
     */
    String getContactPerson();

    /**
     * @uml.property name="contactPerson"
     */
    void setContactPerson(String contactPerson);

    /**
     * @uml.property name="contactPosition"
     */
    String getContactPosition();

    /**
     * @uml.property name="contactPosition"
     */
    void setContactPosition(String contactPosition);

    /**
     * @uml.property name="contactVoice"
     */
    String getContactVoice();

    /**
     * @uml.property name="contactVoice"
     */
    void setContactVoice(String contactVoice);
    
    String getOnlineResource();
    
    void setOnlineResource( String onlineResource );

}
