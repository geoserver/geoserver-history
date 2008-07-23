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

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import org.geotools.gml3.bindings.GML;
import org.geotools.gml3.bindings.MultiPointTypeBinding;
import org.opengis.feature.Attribute;
import javax.xml.namespace.QName;


/**
 * Binding object for the type http://www.opengis.net/gml:MultiPointType.
 *
 * @generated
 */
public class ISOMultiPointTypeBinding extends MultiPointTypeBinding {
    public ISOMultiPointTypeBinding(GeometryFactory gFactory) {
        super(gFactory);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if (GML.pointMember.equals(name)) {
            Attribute att = (Attribute) object;
            MultiPoint multiPoint = (MultiPoint) att.getValue();

            if (multiPoint != null) {
                Point[] members = new Point[multiPoint.getNumGeometries()];

                for (int i = 0; i < members.length; i++) {
                    members[i] = (Point) multiPoint.getGeometryN(i);
                }

                return members;
            }
        }

        return null;
    }
}
