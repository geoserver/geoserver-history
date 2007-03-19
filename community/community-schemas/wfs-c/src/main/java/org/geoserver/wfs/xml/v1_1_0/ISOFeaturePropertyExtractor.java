/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.feature.Name;
import org.geotools.feature.iso.Types;
import org.geotools.gml3.bindings.GML;
import org.geotools.xml.PropertyExtractor;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.Schemas;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;


public class ISOFeaturePropertyExtractor implements PropertyExtractor {
    SchemaIndex schemaIndex;

    public ISOFeaturePropertyExtractor(SchemaIndex schemaIndex) {
        this.schemaIndex = schemaIndex;
    }

    public boolean canHandle(Object object) {
        boolean canHandle = object instanceof ComplexAttribute;

        return canHandle;
    }

    public List properties(Object object, XSDElementDeclaration element) {
        final XSDTypeDefinition typeDefinition;
        final ComplexAttribute feature = (ComplexAttribute) object;
        final ComplexType attributeType = (ComplexType) feature.getType();

        // find the type in the schema
        if (feature.getDescriptor() != null) {
            AttributeDescriptor descriptor = feature.getDescriptor();
            org.opengis.feature.type.Name name = descriptor.getName();
            QName qname = new QName(name.getNamespaceURI(), name.getLocalPart());
            XSDElementDeclaration e = schemaIndex.getElementDeclaration(qname);

            if (e != null) {
                element = e;
            }

            typeDefinition = element.getType();
        } else {
            org.opengis.feature.type.Name name = attributeType.getName();
            QName qname = new QName(name.getNamespaceURI(), name.getLocalPart());
            typeDefinition = schemaIndex.getTypeDefinition(qname);
        }

        List particles = Schemas.getChildElementParticles(typeDefinition, true);
        List properties = new ArrayList(2 * particles.size());

        for (Iterator p = particles.iterator(); p.hasNext();) {
            XSDParticle particle = (XSDParticle) p.next();
            XSDElementDeclaration attribute = (XSDElementDeclaration) particle.getContent();

            if (attribute.isElementDeclarationReference()) {
                attribute = attribute.getResolvedElementDeclaration();
            }

            // ignore gml attributes
            if (GML.NAMESPACE.equals(attribute.getTargetNamespace())) {
                continue;
            }

            String localPart = attribute.getName();
            String uri = attribute.getTargetNamespace();
            Name attributeName = new Name(uri, localPart);
            PropertyDescriptor descriptor = Types.descriptor(attributeType, attributeName);

            // make sure the feature type has an element
            if (descriptor == null) {
                continue;
            }

            Object attributeValue = feature.get(attributeName);

            if (attributeValue instanceof Collection) {
                for (Iterator it = ((Collection) attributeValue).iterator(); it.hasNext();) {
                    Object value = it.next();
                    properties.add(new Object[] { particle, value });
                }
            } else {
                properties.add(new Object[] { particle, attributeValue });
            }
        }

        return properties;
    }
}
