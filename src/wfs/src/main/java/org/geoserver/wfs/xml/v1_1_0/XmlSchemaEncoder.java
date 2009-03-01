/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0;

import net.opengis.wfs.DescribeFeatureTypeType;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;

import org.geoserver.wfs.WFSDescribeFeatureTypeOutputFormat;
import org.geoserver.wfs.WFSInfo;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;

import java.io.IOException;
import java.io.OutputStream;


public class XmlSchemaEncoder extends WFSDescribeFeatureTypeOutputFormat {
    /** wfs configuration */
    WFSInfo wfs;

    /** the catalog */
    Catalog catalog;

    /** the geoserver resource loader */
    GeoServerResourceLoader resourceLoader;

    public XmlSchemaEncoder(GeoServer gs, GeoServerResourceLoader resourceLoader) {
        super("text/xml; subtype=gml/3.1.1");
        this.wfs = gs.getService( WFSInfo.class );
        this.catalog = gs.getCatalog();
        this.resourceLoader = resourceLoader;
    }

    public String getMimeType(Object value, Operation operation)
        throws ServiceException {
        return "text/xml; subtype=gml/3.1.1";
    }

    protected void write(FeatureTypeInfo[] featureTypeInfos, OutputStream output,
        Operation describeFeatureType) throws IOException {
        
        GeoServerInfo global = wfs.getGeoServer().getGlobal();
        //create the schema
        DescribeFeatureTypeType req = (DescribeFeatureTypeType)describeFeatureType.getParameters()[0];
        String proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(req.getBaseUrl(), global.getProxyBaseUrl());
        FeatureTypeSchemaBuilder builder = new FeatureTypeSchemaBuilder.GML3(wfs.getGeoServer(),
                resourceLoader);
        XSDSchema schema = builder.build(featureTypeInfos, proxifiedBaseUrl);

        //serialize
        schema.updateElement();
        final String encoding = global.getCharset();
        XSDResourceImpl.serialize(output, schema.getElement(), encoding);
    }
}
