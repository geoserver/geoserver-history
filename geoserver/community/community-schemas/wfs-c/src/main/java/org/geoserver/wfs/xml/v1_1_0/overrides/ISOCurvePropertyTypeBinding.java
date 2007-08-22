/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002-2006, GeoTools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geoserver.wfs.xml.v1_1_0.overrides;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import org.geotools.gml3.bindings.CurvePropertyTypeBinding;
import org.geotools.gml3.bindings.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.feature.Attribute;
import javax.xml.namespace.QName;


/**
 * Binding object for the type http://www.opengis.net/gml:CurvePropertyType.
 *
 * @generated
 */
public class ISOCurvePropertyTypeBinding extends CurvePropertyTypeBinding {
    public Object getProperty(Object object, QName name)
        throws Exception {
        if (GML._Curve.equals(name)) {
            Attribute att = (Attribute) object;
            Geometry geom = (Geometry) att.getValue();

            return geom;
        }

        return null;
    }
}
