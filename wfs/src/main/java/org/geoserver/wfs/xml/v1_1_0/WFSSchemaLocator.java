/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0;

import org.eclipse.xsd.XSDSchema;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.geotools.xml.Configuration;
import org.geotools.xml.SchemaLocator;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import java.util.Collection;
import java.util.Iterator;


/**
 * Schema locator which adds types defined in applications schemas to the wfs schema proper.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WFSSchemaLocator extends SchemaLocator {
    /** catalog used to look up application schema types */
    Data catalog;

    /** schema type builder */
    FeatureTypeSchemaBuilder schemaBuilder;

    public WFSSchemaLocator(Configuration configuration, Data catalog,
        FeatureTypeSchemaBuilder schemaBuilder) {
        super(configuration);
        this.catalog = catalog;
        this.schemaBuilder = schemaBuilder;
    }

    protected XSDSchema createSchema() throws Exception {
        XSDSchema wfsSchema = super.createSchema();

        //incorporate application schemas into the wfs schema
        Collection featureTypeInfos = catalog.getFeatureTypeInfos().values();

        for (Iterator i = featureTypeInfos.iterator(); i.hasNext();) {
            FeatureTypeInfo meta = (FeatureTypeInfo) i.next();

            //build the schema for the types in the single namespace
            XSDSchema schema = schemaBuilder.build(new FeatureTypeInfo[] { meta });

            //declare the namespace
            String prefix = meta.getNameSpace().getPrefix();
            String namespaceURI = meta.getNameSpace().getURI();
            wfsSchema.getQNamePrefixToNamespaceMap().put(prefix, namespaceURI);

            //add the types + elements to the wfs schema
            for (Iterator t = schema.getTypeDefinitions().iterator(); t.hasNext();) {
                wfsSchema.getTypeDefinitions().add(t.next());
            }

            for (Iterator e = schema.getElementDeclarations().iterator(); e.hasNext();) {
                wfsSchema.getElementDeclarations().add(e.next());
            }
        }

        return wfsSchema;
    }
}
