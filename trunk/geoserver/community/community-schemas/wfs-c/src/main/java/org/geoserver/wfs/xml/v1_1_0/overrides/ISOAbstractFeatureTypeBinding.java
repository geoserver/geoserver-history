/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0.overrides;

import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml3.bindings.AbstractFeatureTypeBinding;
import org.geotools.xml.BindingWalkerFactory;
import org.opengis.feature.Attribute;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
 * </code>
 *         </pre>
 *
 * </p>
 *
 * @generated
 */
public class ISOAbstractFeatureTypeBinding extends AbstractFeatureTypeBinding {
    public ISOAbstractFeatureTypeBinding(FeatureTypeCache ftCache, BindingWalkerFactory bwFactory) {
        super(ftCache, bwFactory);
    }

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

    /**
     * Override as a no-op as property extraction is already handled by
     * {@link ISOFeaturePropertyExtractor}
     */
    public Object getProperty(Object object, QName name)
        throws Exception {
        return null;
    }
}
