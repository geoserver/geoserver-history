/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0.overrides;

import org.geotools.gml3.bindings.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.feature.Attribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.namespace.QName;


/**
 * Binding object for the type http://www.opengis.net/gml:CodeType.
 *
 * @author Gabriel Roldan, Axios Engineering
 */
public class ISOCodeTypeBinding extends AbstractComplexBinding {
    /**
     *
     * @generated modifiable
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

    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return null;
    }

    public QName getTarget() {
        return GML.CodeType;
    }
}
