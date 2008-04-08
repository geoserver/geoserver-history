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
 */
public class ISOGeometryPropertyTypeBinding extends GeometryPropertyTypeBinding {

    public Object getProperty(Object object, QName name) throws Exception {
        if (GML._Geometry.equals(name)) {
            return ((Attribute) object).getValue();
        }
        return null;
    }

}
