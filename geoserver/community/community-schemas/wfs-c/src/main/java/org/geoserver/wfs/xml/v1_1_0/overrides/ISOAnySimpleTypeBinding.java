/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0.overrides;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.geotools.feature.iso.UserData;
import org.geotools.gml3.bindings.GML;
import org.geotools.util.Converters;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xs.bindings.XS;
import org.opengis.feature.Attribute;
import org.opengis.feature.type.Name;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;


/**
 * Binding object for the type http://www.opengis.net/gml:AbstractFeatureType.
 */
public class ISOAnySimpleTypeBinding extends AbstractComplexBinding {
    public ISOAnySimpleTypeBinding() {
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return XS.ANYTYPE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Attribute.class;
    }

    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        throw new UnsupportedOperationException();
    }

    public Element encode(Object object, Document document, Element value)
        throws Exception {
        Attribute attribute = (Attribute) object;

        Name name = attribute.getDescriptor().getName();
        String namespace = name.getNamespaceURI();
        String localName = name.getLocalPart();

        Element encoding = document.createElementNS(namespace, localName);

        String id = attribute.getID();

        if (id != null) {
            encoding.setAttributeNS(GML.NAMESPACE, "id", id);
        }

        Map definedAttributes = (Map) ((UserData) attribute).getUserData(Attributes.class);

        if (definedAttributes != null) {
            Map.Entry entry;

            for (Iterator it = definedAttributes.entrySet().iterator(); it.hasNext();) {
                entry = (Entry) it.next();

                Name key = (Name) entry.getKey();
                Object attValue = entry.getValue();

                String namespaceURI = key.getNamespaceURI();
                String qualifiedName = key.getLocalPart();
                String strAttValue = (String) Converters.convert(attValue, String.class);

                encoding.setAttributeNS(namespaceURI, qualifiedName, strAttValue);
            }
        }

        Object attContent = attribute.getValue();
        String attValue = (String) Converters.convert(attContent, String.class);

        if (attValue != null) {
            // figure out if the node has any text
            Text text = null;

            for (int i = 0; i < encoding.getChildNodes().getLength(); i++) {
                org.w3c.dom.Node node = encoding.getChildNodes().item(i);

                if (node instanceof Text) {
                    text = (Text) node;

                    break;
                }
            }

            // set the text of the node
            if (text == null) {
                text = document.createTextNode(attValue);
                encoding.appendChild(text);
            } else {
                text.setData(attValue);
            }
        }

        return encoding;
    }
}
