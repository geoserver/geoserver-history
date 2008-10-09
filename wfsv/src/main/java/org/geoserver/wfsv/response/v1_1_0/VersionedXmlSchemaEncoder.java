/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfsv.response.v1_1_0;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;

import net.opengis.wfs.DescribeFeatureTypeType;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.geoserver.ows.Response;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.geoserver.wfsv.VersionedDescribeResults;
import org.geoserver.wfsv.xml.v1_1_0.WFSVConfiguration;
import org.vfny.geoserver.global.Data;

public class VersionedXmlSchemaEncoder extends Response {
    /** wfs configuration */
    WFS wfs;

    /** the catalog */
    Data catalog;

    /** the geoserver resource loader */
    GeoServerResourceLoader resourceLoader;

    WFSVConfiguration configuration;

    public VersionedXmlSchemaEncoder(WFS wfs, Data catalog,
            GeoServerResourceLoader resourceLoader,
            WFSVConfiguration configuration) {
        super(VersionedDescribeResults.class, Collections
                .singleton("text/xml; subtype=gml/3.1.1"));
        this.wfs = wfs;
        this.catalog = catalog;
        this.resourceLoader = resourceLoader;
        this.configuration = configuration;
    }

    public String getMimeType(Object value, Operation operation)
            throws ServiceException {
        return "text/xml; subtype=gml/3.1.1";
    }

    public void write(Object value, OutputStream output,
            Operation describeFeatureType) throws IOException {
        VersionedDescribeResults results = (VersionedDescribeResults) value;

        // create the schema
        DescribeFeatureTypeType req = (DescribeFeatureTypeType) describeFeatureType
                .getParameters()[0];
        String proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(req
                .getBaseUrl(), wfs.getGeoServer().getProxyBaseUrl());
        FeatureTypeSchemaBuilder builder = null;
        if (results.isVersioned()) {
            builder = new VersionedSchemaBuilder(wfs, catalog, resourceLoader,
                    configuration);
        } else {
            builder = new FeatureTypeSchemaBuilder.GML3(wfs, catalog,
                    resourceLoader);
        }

        XSDSchema schema = builder.build(results.getFeatureTypeInfo(),
                proxifiedBaseUrl);

        // serialize
        schema.updateElement();
        XSDResourceImpl.serialize(output, schema.getElement());
    }

    public boolean canHandle(Operation operation) {
        return "DescribeVersionedFeatureType".equalsIgnoreCase(operation
                .getId())
                && operation.getService().getId().equalsIgnoreCase("wfsv");
    }

}
