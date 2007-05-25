/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0;

import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml3.bindings.GML;
import org.geotools.util.Converters;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.BindingWalkerFactory;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xs.bindings.XS;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.namespace.QName;


/**
 * Binding object for the type http://www.opengis.net/gml:AbstractFeatureType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *    &lt;complexType abstract=&quot;true&quot; name=&quot;AbstractFeatureType&quot;&gt;
 *        &lt;annotation&gt;
 *            &lt;documentation&gt;An abstract feature provides a set of common properties, including id, metaDataProperty, name and description inherited from AbstractGMLType, plus boundedBy.    A concrete feature type must derive from this type and specify additional  properties in an application schema. A feature must possess an identifying attribute ('id' - 'fid' has been deprecated).&lt;/documentation&gt;
 *        &lt;/annotation&gt;
 *        &lt;complexContent&gt;
 *            &lt;extension base=&quot;gml:AbstractGMLType&quot;&gt;
 *                &lt;sequence&gt;
 *                    &lt;element minOccurs=&quot;0&quot; ref=&quot;gml:boundedBy&quot;/&gt;
 *                    &lt;element minOccurs=&quot;0&quot; ref=&quot;gml:location&quot;&gt;
 *                        &lt;annotation&gt;
 *                            &lt;appinfo&gt;deprecated&lt;/appinfo&gt;
 *                            &lt;documentation&gt;deprecated in GML version 3.1&lt;/documentation&gt;
 *                        &lt;/annotation&gt;
 *                    &lt;/element&gt;
 *                    &lt;!-- additional properties must be specified in an application schema --&gt;
 *                &lt;/sequence&gt;
 *            &lt;/extension&gt;
 *        &lt;/complexContent&gt;
 *    &lt;/complexType&gt;
 *
 * </code>
 *         </pre>
 *
 * </p>
 *
 * @generated
 */
public class ISOElementTypeBinding extends AbstractComplexBinding {
    public ISOElementTypeBinding() {
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

        AttributeDescriptor descriptor = attribute.getDescriptor();
        Name name = descriptor.getName();
        String namespace = name.getNamespaceURI();
        String localName = name.getLocalPart();

        Element encoding = document.createElementNS(namespace, localName);

        String id = attribute.getID();

        if (id != null) {
            encoding.setAttributeNS(GML.NAMESPACE, "id", id);
        }

        Map definedAttributes = (Map) descriptor.getUserData(Attributes.class);

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

        return encoding;
    }
}
