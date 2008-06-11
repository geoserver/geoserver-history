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

import org.geotools.xs.bindings.XSAnyTypeBinding;
import org.opengis.feature.Attribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Binding object for the type http://www.w3.org/2001/XMLSchema:anyType.
 *
 */
public class ISOXSAnyTypeBinding extends XSAnyTypeBinding {
    /**
     * Overrides to return <code>Object.class</code> instead of
     * <code>Map.class</code>
     *
     * @generated modifiable
     */
    public Class getType() {
        return Object.class;
    }

    /**
     * Override to encode object when its an instance of {@link Attribute}
     */
    public Element encode(Object object, Document document, Element value)
        throws Exception {
        if (!(object instanceof Attribute)) {
            return value;
        }

        Attribute attribute = (Attribute) object;

        Element encoding = EncodingUtils.encodeAttribute(attribute, document);

        return encoding;
    }
}
