/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config.impl;

import org.geoserver.config.ContactInfo;

public class ContactInfoImpl implements ContactInfo {

    String id;

    String address;

    String addressCity;

    String addressCountry;

    String addressPostalCode;

    String addressState;

    String addressType;

    String contactEmail;

    String contactFacsimile;

    String contactOrganization;

    String contactPerson;

    String contactPosition;

    String contactVoice;

    String onlineResource;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getAddressPostalCode() {
        return addressPostalCode;
    }

    public void setAddressPostalCode(String addressPostalCode) {
        this.addressPostalCode = addressPostalCode;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactFacsimile() {
        return contactFacsimile;
    }

    public void setContactFacsimile(String contactFacsimile) {
        this.contactFacsimile = contactFacsimile;
    }

    public String getContactOrganization() {
        return contactOrganization;
    }

    public void setContactOrganization(String contactOrganization) {
        this.contactOrganization = contactOrganization;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactPosition() {
        return contactPosition;
    }

    public void setContactPosition(String contactPosition) {
        this.contactPosition = contactPosition;

    }

    public String getContactVoice() {
        return contactVoice;
    }

    public void setContactVoice(String contactVoice) {
        this.contactVoice = contactVoice;
    }
    
    public String getOnlineResource() {
        return onlineResource;
    }
    
    public void setOnlineResource(String onlineResource) {
        this.onlineResource = onlineResource;
    }
    
    
}
