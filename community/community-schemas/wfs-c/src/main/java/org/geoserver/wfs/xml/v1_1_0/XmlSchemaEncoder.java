/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSDescribeFeatureTypeOutputFormat;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.geotools.data.DataStore;
import org.geotools.data.feature.FeatureAccess;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import java.io.IOException;
import java.io.OutputStream;


public class XmlSchemaEncoder extends WFSDescribeFeatureTypeOutputFormat {
    /** wfs configuration */
    WFS wfs;

    /** the catalog */
    Data catalog;

    /** the geoserver resource loader */
    GeoServerResourceLoader resourceLoader;

    public XmlSchemaEncoder(WFS wfs, Data catalog, GeoServerResourceLoader resourceLoader) {
        super("text/xml; subtype=gml/3.1.1");
        this.wfs = wfs;
        this.catalog = catalog;
        this.resourceLoader = resourceLoader;
    }

    public String getMimeType(Object value, Operation operation)
        throws ServiceException {
        return "text/xml; subtype=gml/3.1.1";
    }

    protected void write(FeatureTypeInfo[] featureTypeInfos, OutputStream output,
        Operation describeFeatureType) throws IOException {
        XSDSchema schema = null;

        FeatureTypeInfo fti;

        for (int i = 0; i < featureTypeInfos.length; i++) {
            fti = featureTypeInfos[i];

            DataStore dataStore = fti.getDataStoreInfo().getDataStore();

            if (dataStore instanceof FeatureAccess) {
                FeatureAccess fa = (FeatureAccess) dataStore;
                String uri = fti.getNameSpace().getURI();
                String localName = fti.getTypeName();
                Name typeName = new org.geotools.feature.Name(uri, localName);
                AttributeDescriptor descriptor = (AttributeDescriptor) fa.describe(typeName);
                XSDElementDeclaration elemDecl = (XSDElementDeclaration) descriptor.getUserData(XSDElementDeclaration.class);

                if (null != elemDecl) {
                    schema = elemDecl.getSchema();

                    break; //handle only one by now
                }
            }
        }

        if (schema == null) {
            //create the schema
            FeatureTypeSchemaBuilder builder = new FeatureTypeSchemaBuilder.GML3(wfs, catalog,
                    resourceLoader);
            schema = builder.build(featureTypeInfos);
        }

        //serialize
        schema.updateElement();
        XSDResourceImpl.serialize(output, schema.getElement());
    }
}
