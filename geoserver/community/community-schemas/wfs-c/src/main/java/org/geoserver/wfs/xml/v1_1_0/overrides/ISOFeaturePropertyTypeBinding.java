/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0.overrides;

import org.geotools.gml3.bindings.FeaturePropertyTypeBinding;
import org.opengis.feature.Attribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Extends {@link FeaturePropertyTypeBinding} to encode xml attributes.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @since 1.6
 */
public class ISOFeaturePropertyTypeBinding extends FeaturePropertyTypeBinding {
    /**
     * Overrides to indicate a feature property might be
     * a single Attribute, not necessarly a Feature
     */
    public Class getType() {
        return Attribute.class;
    }

    public Element encode(Object object, Document document, Element value)
        throws Exception {
        Attribute attribute = (Attribute) object;
        Element encoding = EncodingUtils.encodeAttribute(attribute, document);

        return encoding;
    }
}
