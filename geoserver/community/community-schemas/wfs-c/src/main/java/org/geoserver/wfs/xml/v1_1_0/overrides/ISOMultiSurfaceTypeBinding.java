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
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.gml3.bindings.GML;
import org.geotools.gml3.bindings.MultiSurfaceTypeBinding;
import org.opengis.feature.Attribute;
import javax.xml.namespace.QName;


/**
 * Binding object for the type http://www.opengis.net/gml:MultiSurfaceType.
 *
 * @generated
 */
public class ISOMultiSurfaceTypeBinding extends MultiSurfaceTypeBinding {
    public ISOMultiSurfaceTypeBinding(GeometryFactory gf) {
        super(gf);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if (GML.surfaceMember.equals(name)) {
            Attribute att = (Attribute) object;
            MultiPolygon multiSurface = (MultiPolygon) att.getValue();

            if (multiSurface != null) {
                Polygon[] members = new Polygon[multiSurface.getNumGeometries()];

                for (int i = 0; i < members.length; i++) {
                    members[i] = (Polygon) multiSurface.getGeometryN(i);
                }

                return members;
            }
        }

        return null;
    }
}
