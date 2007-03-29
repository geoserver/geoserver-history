/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0;

import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml3.bindings.GML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.BindingWalkerFactory;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.List;
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
public class ISOAbstractFeatureTypeBinding extends AbstractComplexBinding {
    FeatureTypeCache ftCache;
    BindingWalkerFactory bwFactory;

    public ISOAbstractFeatureTypeBinding(FeatureTypeCache ftCache, BindingWalkerFactory bwFactory) {
        this.ftCache = ftCache;
        this.bwFactory = bwFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return GML.AbstractFeatureType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Attribute.class;
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
        String typeName = name.getLocalPart();

        Element encoding = document.createElementNS(namespace, typeName);

        String id = attribute.getID();

        if (id != null) {
            encoding.setAttributeNS(GML.NAMESPACE, "id", id);
        }

        return encoding;
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        if (!(object instanceof ComplexAttribute)) {
            return null;
        }

        ComplexAttribute attribute = (ComplexAttribute) object;

        if (GML.boundedBy.equals(name)) {
            BoundingBox bounds = ((Feature) attribute).getBounds();

            return bounds;

            /*
             * if (bounds instanceof ReferencedEnvelope) { return bounds; }
             *
             * CoordinateReferenceSystem crs =
             * (feature.getFeatureType().getDefaultGeometry() != null) ?
             * feature.getFeatureType().getDefaultGeometry().getCoordinateSystem() :
             * null;
             *
             * return new ReferencedEnvelope(bounds, crs);
             */
        }

        Name attName = new org.geotools.feature.Name(name.getNamespaceURI(), name.getLocalPart());

        List list = attribute.get(attName);

        if ((list == null) || (list.size() == 0)) {
            return null;
        }

        if (list.size() == 1) {
            return list.get(0);
        }

        return list;
    }
}
