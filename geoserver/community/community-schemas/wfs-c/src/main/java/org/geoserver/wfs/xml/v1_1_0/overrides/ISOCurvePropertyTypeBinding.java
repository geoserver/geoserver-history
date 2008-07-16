/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wfs.xml.v1_1_0.overrides;

import javax.xml.namespace.QName;

import org.geotools.gml3.bindings.CurvePropertyTypeBinding;
import org.geotools.gml3.bindings.GML;
import org.opengis.feature.Attribute;

/**
 * Binding object for the type http://www.opengis.net/gml:CurvePropertyType.
 * 
 * <p>
 * 
 * This version unpacks the Geometry inside a GeometricAttribute, so that it can
 * be exposed and encoded as GML, not left to be converted to WKT by fallback
 * encoding.
 * 
 * <p>
 * 
 * Client properties are also supported.
 */
public class ISOCurvePropertyTypeBinding extends CurvePropertyTypeBinding {

    /**
     * Get a property of the attribute.
     * 
     * @param object
     *                attribute (UserData Attribute)
     * @param name
     *                name of property
     * 
     * @see org.geotools.gml3.bindings.CurvePropertyTypeBinding#getProperty(java.lang.Object,
     *      javax.xml.namespace.QName)
     */
    public Object getProperty(Object object, QName name) throws Exception {
        if (GML._Curve.equals(name)) {
            return ((Attribute) object).getValue();
        } else {
            return BindingUtils.getPropertyFromUserData(object, name);
        }
    }

}
