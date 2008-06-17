/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0.overrides;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.geotools.feature.iso.UserData;
import org.geotools.gml3.bindings.GML;
import org.geotools.util.Converters;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;


public class EncodingUtils {
    public static Element encodeAttribute(Attribute attribute, Document document) {
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

        // it might be it is not a complex type and thus its content has to be
        // encoded here, as ElementEncoderExecutor will not catch it
        if (!(attribute instanceof ComplexAttribute)) {
            Object attValue = attribute.getValue();

            // figure out if the node has any text
            Text text = null;

            for (int i = 0; i < encoding.getChildNodes().getLength(); i++) {
                org.w3c.dom.Node node = (org.w3c.dom.Node) encoding.getChildNodes().item(i);

                if (node instanceof Text) {
                    text = (Text) node;

                    break;
                }
            }

            try {
                String encodedValue = (String) Converters.convert(attValue, String.class);

                if (encodedValue != null) {
                    // set the text of the node
                    if (text == null) {
                        text = document.createTextNode(encodedValue);
                        encoding.appendChild(text);
                    } else {
                        text.setData(encodedValue);
                    }
                }
            } catch (Throwable t) {
                String msg = "Encode failed for " + name + ". Cause: " + t.getLocalizedMessage();
                throw new RuntimeException(msg, t);
            }
        }

        return encoding;
    }
}
