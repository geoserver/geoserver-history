package org.geoserver.wfs.xml.v1_1_0;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.http.response.FeatureTypeEncoder;
import org.geoserver.wfs.xml.FeatureTypeSchemaBuilder;

public class XmlSchemaEncoder extends FeatureTypeEncoder {
	
	/** wfs configuration */
	WFS wfs;
	/** the catalog */
	GeoServerCatalog catalog;
	
	public XmlSchemaEncoder( WFS wfs, GeoServerCatalog catalog ) {
		super( "text/xml; subtype=gml/3.1.1", "text/xml; subtype=gml/3.1.1" );
		this.wfs = wfs;
		this.catalog = catalog;
	}
	
	public void encode(FeatureTypeInfo[] metas, OutputStream output) throws IOException {
		
		//create the schema
		FeatureTypeSchemaBuilder builder = 
			new FeatureTypeSchemaBuilder.GML3( wfs, catalog );
		XSDSchema schema = builder.build( metas );
		
		//serialize
		schema.updateElement();
		XSDResourceImpl.serialize( output, schema.getElement() );
		
	}

}
