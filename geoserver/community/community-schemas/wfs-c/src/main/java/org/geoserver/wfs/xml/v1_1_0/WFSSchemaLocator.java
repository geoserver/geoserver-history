/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.geotools.data.DataStore;
import org.geotools.data.feature.FeatureAccess;
import org.geotools.data.feature.FeatureSource2;
import org.geotools.xml.Configuration;
import org.geotools.xml.SchemaLocator;
import org.opengis.feature.type.AttributeDescriptor;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;


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
        return createSchema(null);
    }

    /**
     * Creates the schema using this SchemaLocator.  Note that in order to locate
     * schemas, you need to know *where* the schemas are, and they might be
     * in different places, depending on who's calling the server.  Thus the
     * baseUrl parameter.
     * @param baseUrl If this parameter is not null, its value will be used as the starting
     * point for figuring out the baseUrl for any schemas built with this class.
     * @return
     * @throws Exception
     */
    protected XSDSchema createSchema(String baseUrl) throws Exception {
        XSDSchema wfsSchema = super.createSchema();

        //incorporate application schemas into the wfs schema
        Collection featureTypeInfos = catalog.getFeatureTypeInfos().values();

        for (Iterator i = featureTypeInfos.iterator(); i.hasNext();) {
            XSDSchema schema = null;

            FeatureTypeInfo meta = (FeatureTypeInfo) i.next();

            // we get a geoserver feature source wrapper so it is not
            // enough to check for featureSource instanceof FeatureSource2
            DataStore dataStore = meta.getDataStoreInfo().getDataStore();

            if (dataStore instanceof FeatureAccess) {
                String name = meta.getTypeName();
                FeatureSource2 source = (FeatureSource2) dataStore.getFeatureSource(name);
                AttributeDescriptor descriptor = (AttributeDescriptor) source.describe();
                XSDElementDeclaration elemDecl = (XSDElementDeclaration) descriptor
                        .getUserData(XSDElementDeclaration.class);

                if (elemDecl != null) {
                    schema = elemDecl.getSchema();
                }
            }

            if (schema == null) {
                // build the schema for the types in the single namespace
                schema = schemaBuilder.build(new FeatureTypeInfo[] { meta }, baseUrl);
            }

            // declare the namespace
            String prefix = meta.getNameSpace().getPrefix();
            String namespaceURI = meta.getNameSpace().getURI();
            Map namePrefixToNamespaceMap = wfsSchema.getQNamePrefixToNamespaceMap();
            namePrefixToNamespaceMap.put(prefix, namespaceURI);

            Map schemaPrefixes = schema.getQNamePrefixToNamespaceMap();

            for (Iterator it = schemaPrefixes.entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();

                if (!namePrefixToNamespaceMap.containsKey(entry.getKey())) {
                    namePrefixToNamespaceMap.put(entry.getKey(), entry.getValue());
                }
            }

            // add the types + elements to the wfs schema
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
