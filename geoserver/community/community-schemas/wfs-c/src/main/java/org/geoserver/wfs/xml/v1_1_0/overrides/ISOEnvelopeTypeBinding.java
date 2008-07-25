/* Copyright (c) 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wfs.xml.v1_1_0.overrides;

import javax.xml.namespace.QName;
import org.geotools.gml3.bindings.EnvelopeTypeBinding;
import org.geotools.gml3.bindings.GML;
import org.opengis.feature.Attribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Binding object for the type http://www.opengis.net/gml:Envelope
 * 
 * @author Peter Warren
 * 
 */
public class ISOEnvelopeTypeBinding extends EnvelopeTypeBinding {

    /**
     * Get a property of the attribute.
     * 
     * @param object
     *                attribute (UserData Attribute)
     * @param name
     *                name of property
     * 
     * @return the property or null if not present
     * 
     * @see org.geotools.gml3.bindings.EnvelopeTypeBinding#getProperty(java.lang.Object,
     *      javax.xml.namespace.QName)
     */
    public Object getProperty(Object object, QName name) {
        return BindingUtils.getPropertyFromUserData(object, name);
    }

    /**
     * Encode the Envelope object
     * 
     * @param object
     *                attribute (UserData Attribute)
     * @param document
     *                document to encode to
     * @param value
     *                the element to encode object to
     * 
     * @see org.geotools.gml3.bindings.EnvelopeTypeBinding#encode(java.lang.Object,
     *      org.w3c.dom.Document,g.w3c.dom.Element)
     */
    public Element encode(Object object, Document document, Element value)
            throws Exception {
        if (object instanceof Envelope) {
            Envelope envelope = (Envelope) object;
            if (envelope.isNull()) {
                value.appendChild(document.createElementNS(GML.NAMESPACE,
                        GML.Null.getLocalPart()));
            } else {
                Element lowerCorner = document.createElementNS(GML.NAMESPACE,
                        "lowerCorner");
                lowerCorner.appendChild(document.createTextNode(envelope
                        .getMinX()
                        + " " + envelope.getMinY()));
                value.appendChild(lowerCorner);
                Element upperCorner = document.createElementNS(GML.NAMESPACE,
                        "upperCorner");
                upperCorner.appendChild(document.createTextNode(envelope
                        .getMaxX()
                        + " " + envelope.getMaxY()));
                value.appendChild(upperCorner);
            }
        } else {
            // we will be creating internal object from individual attribute
            // values.
            Attribute envelope = (Attribute) object;
            Object lc = getProperty(envelope, new QName(GML.NAMESPACE,
                    "lowerCorner"));
            if (lc != null) {
                Element lowerCorner = document.createElementNS(GML.NAMESPACE,
                        "lowerCorner");
                lowerCorner.appendChild(document.createTextNode(lc.toString()));
                value.appendChild(lowerCorner);
            }
            Object uc = getProperty(envelope, new QName(GML.NAMESPACE,
                    "upperCorner"));
            if (uc != null) {
                Element upperCorner = document.createElementNS(GML.NAMESPACE,
                        "upperCorner");
                upperCorner.appendChild(document.createTextNode(uc.toString()));
                value.appendChild(upperCorner);
            }
        }
        return value;
    }

}
