package org.geoserver.wfs.xml.v1_1_0;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceImpl;

import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSDescribeFeatureTypeOutputFormat;

import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;

public class XmlSchemaEncoder extends WFSDescribeFeatureTypeOutputFormat {
	
	/** wfs configuration */
	WFS wfs;
	/** the catalog */
	Data catalog;
	/** the geoserver resource loader */
	GeoServerResourceLoader resourceLoader;
	
	public XmlSchemaEncoder( WFS wfs, Data catalog, GeoServerResourceLoader resourceLoader ) {
		super( "text/xml; subtype=gml/3.1.1" );
		this.wfs = wfs;
		this.catalog = catalog;
		this.resourceLoader = resourceLoader;
	}
	
	public String getMimeType() throws ServiceException {
		return "text/xml; subtype=gml/3.1.1";
	}
	
	protected void write(FeatureTypeInfo[] featureTypeInfos, OutputStream output, Operation describeFeatureType) throws IOException {
		//create the schema
		FeatureTypeSchemaBuilder builder = 
			new FeatureTypeSchemaBuilder.GML3( wfs, catalog, resourceLoader );
		XSDSchema schema = builder.build( featureTypeInfos );
		
		//serialize
		schema.updateElement();
		XSDResourceImpl.serialize( output, schema.getElement() );
		
	}

}
