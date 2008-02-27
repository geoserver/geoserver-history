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

import java.net.URI;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.gml3.bindings.GML;
import org.geotools.gml3.bindings.MeasureTypeBinding;
import org.geotools.gml3.bindings.PointTypeBinding;
import org.geotools.measure.Measure;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeocentricCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Attribute;
import org.opengis.feature.GeometryAttribute;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.units.BaseUnit;
import javax.xml.namespace.QName;


/**
 * Binding object for the type http://www.opengis.net/gml:PointType.
 *
 */
public class ISOMeasureTypeBinding extends MeasureTypeBinding {
 

    public Object getProperty(Object object, QName name)throws Exception {
        if ("uom".equals(name.getLocalPart())) {
            Attribute attribute = (Attribute) object;
        	Measure measure = (Measure) attribute.getValue();

            if (measure.getUnit() != null) {
                return new URI(((BaseUnit) measure.getUnit()).getSymbol());
            }
        }
		return null;
    }

 
}
