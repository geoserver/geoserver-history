/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0.overrides;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.feature.Name;
import org.geotools.feature.iso.Types;
import org.geotools.gml3.bindings.GML;
import org.geotools.xml.PropertyExtractor;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.Schemas;
import org.geotools.xml.impl.GetPropertyExecutor;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
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
        if (null != attributeType.getUserData(XSDTypeDefinition.class)) {
            typeDefinition = (XSDTypeDefinition) attributeType.getUserData(XSDTypeDefinition.class);
        } else if (feature.getDescriptor() != null) {
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

        final boolean includeParents = true;
        List particles = Schemas.getChildElementParticles(typeDefinition, includeParents);
        List properties = new ArrayList(2 * particles.size());

        for (Iterator p = particles.iterator(); p.hasNext();) {
            XSDParticle particle = (XSDParticle) p.next();
            XSDElementDeclaration attribute = (XSDElementDeclaration) particle.getContent();

            if (attribute.isElementDeclarationReference()) {
                attribute = attribute.getResolvedElementDeclaration();
            }

            // ignore gml attributes as they're alread returned by BindingPropertyExtractor
            //if (GML.NAMESPACE.equals(attribute.getTargetNamespace())) {
            //    continue;
            //}
            String localPart = attribute.getName();
            String uri = attribute.getTargetNamespace();

            //REVISIT: this is to workaround old GT Feature style of having
            //attribute names with no namespace, but the ISO Feature adapter
            //sets the descriptor namespace to be the same than the feature type
            if (null == uri) {
                uri = feature.getType().getName().getNamespaceURI();
            }

            Name attributeName = new Name(uri, localPart);
            PropertyDescriptor descriptor = Types.descriptor(attributeType, attributeName);

            // make sure the feature type has an element
            if (descriptor == null) {
                if (GML.NAMESPACE.equals(attributeName.getNamespaceURI())) {
                    //Do not throw an exception since the geotools (non ISO)
                    //feature types does have the default gml properties defined
                    continue;
                }

                throw new NoSuchElementException("Property descriptor " + attributeName
                    + " not found in feature type " + attributeType.getName());
            }

            Object attributeValue = feature.get(attributeName);

            if ((attributeValue == null)
                    || ((attributeValue instanceof Collection)
                    && ((Collection) attributeValue).isEmpty())) {
                //check the case that we are asking for a property that is 
                // abstract, in that case ask for elements in teh same 
                // substitution group
                if (attribute.isAbstract()) {
                    List sub = attribute.getSubstitutionGroup();

                    for (Iterator s = sub.iterator(); s.hasNext();) {
                        XSDElementDeclaration e = (XSDElementDeclaration) s.next();

                        attributeName = new Name(e.getTargetNamespace(), e.getName());
                        attributeValue = feature.get(attributeName);

                        if (attributeValue != null) {
                            //found it, break out
                            break;
                        }
                    }
                }
            }

            if (attributeValue instanceof Collection) {
                Collection col = (Collection) attributeValue;
                int size = col.size();

                if (size == 0) {
                    attributeValue = null;
                } else if (size == 1) {
                    attributeValue = col.iterator().next();
                }
            }

            properties.add(new Object[] { particle, attributeValue });
        }

        return properties;
    }
}
