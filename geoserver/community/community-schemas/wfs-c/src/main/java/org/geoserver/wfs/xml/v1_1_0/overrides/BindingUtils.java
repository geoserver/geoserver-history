/* Copyright (c) 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wfs.xml.v1_1_0.overrides;

import java.util.Map;

import javax.xml.namespace.QName;

import org.geotools.feature.Name;
import org.geotools.feature.iso.UserData;
import org.xml.sax.Attributes;

/**
 * Utility static methods to support the implementation of binding overrides for
 * complex features.
 */
public class BindingUtils {

    /**
     * Get a client property set in the UserData of an Attribute. Null is
     * returned if not present.
     * 
     * @param object
     *                the attribute object (a UserData Attribute)
     * @param name
     *                the name of the property to get
     * @return the client property or null if not present
     */
    public static Object getPropertyFromUserData(Object object, QName name) {
        UserData attribute = (UserData) object;
        Map clientProperties = (Map) ((UserData) attribute)
                .getUserData(Attributes.class);
        if (clientProperties == null) {
            return null;
        } else {
            return clientProperties.get(new Name(name.getNamespaceURI(), name
                    .getLocalPart()));
        }
    }

}
