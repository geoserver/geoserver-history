/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config.hibernate.beans;

import org.geoserver.config.ContactInfo;
import org.geoserver.config.impl.ContactInfoImpl;
import org.geoserver.hibernate.Hibernable;

public class ContactInfoImplHb extends ContactInfoImpl implements ContactInfo, Hibernable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7585201572391100517L;

    public ContactInfoImplHb() {
        setId("");
        setAddress("");
        setAddressCity("");
        setAddressCountry("");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ContactInfo)) {
            return false;
        }

        final ContactInfo other = (ContactInfo) obj;

        if (!(compare(getId(), other.getId())))
            return false;
        if (!(compare(getAddress(), other.getAddress())))
            return false;
        if (!(compare(getAddressCity(), other.getAddressCity())))
            return false;
        if (!(compare(getAddressCountry(), other.getAddressCountry())))
            return false;
        if (!(compare(getAddressPostalCode(), other.getAddressPostalCode())))
            return false;
        if (!(compare(getAddressState(), other.getAddressState())))
            return false;
        if (!(compare(getAddressType(), other.getAddressType())))
            return false;
        if (!(compare(getContactEmail(), other.getContactEmail())))
            return false;
        if (!(compare(getContactFacsimile(), other.getContactFacsimile())))
            return false;
        if (!(compare(getContactOrganization(), other.getContactOrganization())))
            return false;
        if (!(compare(getContactPerson(), other.getContactPerson())))
            return false;
        if (!(compare(getContactPosition(), other.getContactPosition())))
            return false;
        if (!(compare(getContactVoice(), other.getContactVoice())))
            return false;
        if (!(compare(getOnlineResource(), other.getOnlineResource())))
            return false;

        return true;
    }

    protected static boolean compare(String s1, String s2) {
        if (s1 == null) {
            if (s2 != null) {
                // Logging.getLogger(ContactInfoImplHb.class).severe("null:"+s2);
                return false;
            }
        } else if (!s1.equals(s2)) {
            // Logging.getLogger(ContactInfoImplHb.class).severe(s1+":"+s2);
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + getId() + "]@" + hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        if (getId() != null)
            hash = 7 * hash + getId().hashCode();
        if (getAddress() != null)
            hash = 7 * hash + getAddress().hashCode();
        if (getAddressCity() != null)
            hash = 7 * hash + getAddressCity().hashCode();
        if (getAddressCountry() != null)
            hash = 7 * hash + getAddressCountry().hashCode();
        if (getAddressPostalCode() != null)
            hash = 7 * hash + getAddressPostalCode().hashCode();
        if (getAddressState() != null)
            hash = 7 * hash + getAddressState().hashCode();
        if (getAddressType() != null)
            hash = 7 * hash + getAddressType().hashCode();
        if (getContactEmail() != null)
            hash = 7 * hash + getContactEmail().hashCode();
        if (getContactFacsimile() != null)
            hash = 7 * hash + getContactFacsimile().hashCode();
        if (getContactOrganization() != null)
            hash = 7 * hash + getContactOrganization().hashCode();
        if (getContactPerson() != null)
            hash = 7 * hash + getContactPerson().hashCode();
        if (getContactPosition() != null)
            hash = 7 * hash + getContactPosition().hashCode();
        if (getContactVoice() != null)
            hash = 7 * hash + getContactVoice().hashCode();
        if (getOnlineResource() != null)
            hash = 7 * hash + getOnlineResource().hashCode();

        return hash;
    }
}
