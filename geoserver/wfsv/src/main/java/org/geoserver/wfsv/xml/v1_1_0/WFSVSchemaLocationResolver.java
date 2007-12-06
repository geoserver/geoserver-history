/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv.xml.v1_1_0;

import org.eclipse.xsd.XSDSchema;
import org.geoserver.wfs.xml.v1_1_0.WFSSchemaLocationResolver;


/**
 * Schema location resolver, extending the WFS one
 */
public class WFSVSchemaLocationResolver extends WFSSchemaLocationResolver {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     *        @generated modifiable
     */
    public String resolveSchemaLocation(XSDSchema xsdSchema,
        String namespaceURI, String schemaLocationURI) {
        String location = super.resolveSchemaLocation(xsdSchema, namespaceURI,
                schemaLocationURI);

        if (location != null) {
            return location;
        }

        if (schemaLocationURI == null) {
            return null;
        }

        //if no namespace given, assume default for the current schema
        if (((namespaceURI == null) || "".equals(namespaceURI))
                && (xsdSchema != null)) {
            namespaceURI = xsdSchema.getTargetNamespace();
        }

        if ("http://www.opengis.net/wfsv".equals(namespaceURI)) {
            if (schemaLocationURI.endsWith("wfsv.xsd")) {
                return getClass().getResource("wfsv.xsd").toString();
            }
        }

        return null;
    }
}
