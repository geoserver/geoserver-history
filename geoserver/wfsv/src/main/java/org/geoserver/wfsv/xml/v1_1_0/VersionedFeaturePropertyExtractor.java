/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv.xml.v1_1_0;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.data.VersioningFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.util.logging.Logging;
import org.geotools.xml.PropertyExtractor;
import org.geotools.xs.XSConfiguration;
import org.opengis.feature.simple.SimpleFeature;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;

/**
 * Extracts the extra four properties out of a versioned data type. To be used
 * for encoding GetVersionedFeature output
 * @author Andrea Aime
 *
 */
public class VersionedFeaturePropertyExtractor implements PropertyExtractor {
    private static final String XSD_SCHEMA = "http://www.w3.org/2001/XMLSchema";

    private static final Logger LOGGER = Logging
            .getLogger("org.geoserver.wfsv.xml.v1_1_0");

    private static final XSDParticle VERSION;

    private static final XSDParticle AUTHOR;

    private static final XSDParticle DATE;

    private static final XSDParticle MESSAGE;

    static {
        XSDSchema schema = new XSConfiguration().schema();
        VERSION = particle(schema, "version", XSD_SCHEMA, "string", true, 0, 1);
        AUTHOR = particle(schema, "author", XSD_SCHEMA, "string", true, 0, 1);
        DATE = particle(schema, "date", XSD_SCHEMA, "dateTime", true, 0, 1);
        MESSAGE = particle(schema, "message", XSD_SCHEMA, "string", true, 0, 1);
    }

    static XSDParticle particle(XSDSchema schema, String elementName,
            String typeNS, String typeName, boolean nillable, int minOccurs,
            int maxOccurs) {
        XSDFactory factory = XSDFactory.eINSTANCE;
        XSDElementDeclaration element = factory.createXSDElementDeclaration();
        element.setName(elementName);
        element.setNillable(nillable);

        XSDTypeDefinition type = schema.resolveTypeDefinition(typeNS, typeName);
        element.setTypeDefinition(type);

        XSDParticle particle = factory.createXSDParticle();
        particle.setMinOccurs(minOccurs);
        particle.setMaxOccurs(maxOccurs);
        particle.setContent(element);
        return particle;
    }

    Data catalog;

    public VersionedFeaturePropertyExtractor(Data catalog) {
        this.catalog = catalog;
    }

    public boolean canHandle(Object object) {
        try {
            if (!(object instanceof SimpleFeature)
                    || object instanceof FeatureCollection)
                return false;

            SimpleFeature f = (SimpleFeature) object;
            FeatureTypeInfo info = catalog.getFeatureTypeInfo(f
                    .getFeatureType().getTypeName(), f.getFeatureType()
                    .getName().getNamespaceURI());
            return info != null
                    && info.getFeatureSource() instanceof VersioningFeatureSource;
        } catch (Exception e) {
            LOGGER
                    .log(
                            Level.FINE,
                            "Error occurred trying to determine versioning status of a feature type",
                            e);
            return false;
        }
    }

    public List properties(Object object, XSDElementDeclaration elem) {
        SimpleFeature f = (SimpleFeature) object;
        List particles = new ArrayList();
        particles.add(new Object[] { VERSION, f.getAttribute("version") });
        particles.add(new Object[] { AUTHOR, f.getAttribute("author") });
        particles.add(new Object[] { DATE, f.getAttribute("date") });
        particles.add(new Object[] { MESSAGE, f.getAttribute("message") });
        return particles;
    }

}
