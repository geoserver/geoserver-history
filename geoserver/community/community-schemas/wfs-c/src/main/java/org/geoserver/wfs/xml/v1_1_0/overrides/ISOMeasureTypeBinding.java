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
import java.util.Map;

import javax.units.BaseUnit;
import javax.xml.namespace.QName;

import org.geotools.feature.Name;
import org.geotools.gml3.bindings.MeasureTypeBinding;
import org.geotools.measure.Measure;
import org.opengis.feature.Attribute;
import org.xml.sax.Attributes;

/**
 * Binding for complex feature Measure with unit-of-measure property.
 */
public class ISOMeasureTypeBinding extends MeasureTypeBinding {
    
    private static final Name UOM = new Name("uom");

    public Object getProperty(Object object, QName name) throws Exception {
        if (UOM.toString().equals(name.getLocalPart())) {
            Attribute attribute = (Attribute) object;
            /* first look for a client property and use it if it is set */
            Map clientProperties = (Map) attribute.getDescriptor().getUserData(
                    Attributes.class);
            if (clientProperties != null) {
                String uom = (String) clientProperties.get(UOM);
                if (uom != null) {
                    return new URI(uom);
                }
            }
            /* might have uom set somewhere else, such as data */
            Measure measure = (Measure) attribute.getValue();
            if (measure != null && measure.getUnit() != null) {
                return new URI(((BaseUnit) measure.getUnit()).getSymbol());
            }
        }
        return null;
    }

}
