/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml.v1_1_0;

import net.opengis.wfs.DescribeFeatureTypeType;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSDescribeFeatureTypeOutputFormat;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
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
    
    /** schema builder */
    FeatureTypeSchemaBuilder schemaBuilder;

    public XmlSchemaEncoder(String outputFormat, WFS wfs, Data catalog, GeoServerResourceLoader resourceLoader, FeatureTypeSchemaBuilder schemaBuilder) {
        super(outputFormat);
        this.wfs = wfs;
        this.catalog = catalog;
        this.resourceLoader = resourceLoader;
        this.schemaBuilder = schemaBuilder;
    }

    public String getMimeType(Object value, Operation operation)
        throws ServiceException {
        return "text/xml; subtype=gml/3.1.1";
    }

    protected void write(FeatureTypeInfo[] featureTypeInfos, OutputStream output,
        Operation describeFeatureType) throws IOException {
        //create the schema
        DescribeFeatureTypeType req = (DescribeFeatureTypeType)describeFeatureType.getParameters()[0];
        String proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(req.getBaseUrl(), wfs.getGeoServer().getProxyBaseUrl());
        XSDSchema schema = schemaBuilder.build(featureTypeInfos, proxifiedBaseUrl);

        //serialize
        schema.updateElement();
        final String encoding = wfs.getCharSet().name();
        XSDResourceImpl.serialize(output, schema.getElement(), encoding);
    }
    
    public static class V11 extends XmlSchemaEncoder {

        public V11(WFS wfs, Data catalog, GeoServerResourceLoader resourceLoader) {
            super("text/xml; subtype=gml/3.1.1",wfs,catalog,resourceLoader,new FeatureTypeSchemaBuilder.GML3(wfs,catalog,resourceLoader));
        }
        
    }
    
    public static class V10 extends XmlSchemaEncoder {

        public V10(WFS wfs, Data catalog, GeoServerResourceLoader resourceLoader) {
            super("XMLSCHEMA", wfs,catalog,resourceLoader, new FeatureTypeSchemaBuilder.GML2(wfs,catalog,resourceLoader));
        }
        
        @Override
        public String getMimeType(Object arg0, Operation arg1) throws ServiceException {
            return "text/xml";
        }
        
    }
}
