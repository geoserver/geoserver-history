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
import com.vividsolutions.jts.geom.Point;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.gml3.bindings.GML;
import org.geotools.gml3.bindings.PointTypeBinding;
import org.opengis.feature.Attribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.namespace.QName;


/**
 * Binding object for the type http://www.opengis.net/gml:PointType.
 *
 */
public class ISOPointTypeBinding extends PointTypeBinding {
    public ISOPointTypeBinding(GeometryFactory gFactory) {
        super(gFactory);
    }

    public Object getProperty(Object object, QName name) {
        if (GML.pos.equals(name)) {
            Point point;

            if (object instanceof Attribute) {
                Attribute att = (Attribute) object;
                point = (Point) att.getValue();
            } else {
                point = (Point) object;
            }

            if (point != null) {
                DirectPosition2D dp = new DirectPosition2D();
                dp.setOrdinate(0, point.getX());
                dp.setOrdinate(1, point.getY());

                return dp;
            }
        }

        return null;
    }

    public Element encode(Object object, Document document, Element value)
        throws Exception {
        return value;
    }
}
