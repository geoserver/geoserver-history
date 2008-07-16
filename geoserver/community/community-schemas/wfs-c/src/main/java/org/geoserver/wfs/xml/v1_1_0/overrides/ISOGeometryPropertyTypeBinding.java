/* Copyright (c) 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wfs.xml.v1_1_0.overrides;

import javax.xml.namespace.QName;

import org.geotools.gml3.bindings.GML;
import org.geotools.gml3.bindings.GeometryPropertyTypeBinding;
import org.opengis.feature.Attribute;

/**
 * Binding for gml:GeometryPropertyType
 * 
 * <p>
 * 
 * This version unpacks the Geometry inside a GeometricAttribute object, so that
 * it can be exposed and encoded as GML, not left to be converted to WKT by
 * fallback encoding.
 * 
 * <p>
 * 
 * Client properties are also supported.
 */
public class ISOGeometryPropertyTypeBinding extends GeometryPropertyTypeBinding {

    /**
     * Get a property of the attribute.
     * 
     * @param object
     *                attribute (UserData Attribute)
     * @param name
     *                name of property
     * @return the property or null if not present
     * 
     * @see org.geotools.gml3.bindings.GeometryPropertyTypeBinding#getProperty(java.lang.Object,
     *      javax.xml.namespace.QName)
     */
    public Object getProperty(Object object, QName name) throws Exception {
        if (GML._Geometry.equals(name)) {
            return ((Attribute) object).getValue();
        } else {
            return BindingUtils.getPropertyFromUserData(object, name);
        }
    }

}
