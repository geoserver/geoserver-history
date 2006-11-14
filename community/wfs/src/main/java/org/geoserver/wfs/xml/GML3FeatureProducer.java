package org.geoserver.wfs.xml;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.http.util.ResponseUtils;
import org.geoserver.wfs.GML2FeatureProducer;
import org.geoserver.wfs.WFS;
import org.geotools.gml.producer.FeatureTransformer;

public class GML3FeatureProducer extends GML2FeatureProducer {

	public GML3FeatureProducer(WFS wfs, GeoServerCatalog catalog) {
		super(wfs, catalog);
	}

	public Set getOutputFormats() {
		return new HashSet( 
			Arrays.asList( new String[] { "text/xml; subtype=gml/3.1.1" } )
		);
				
	}
	
	protected FeatureTransformer createTransformer() {
		return new GML3FeatureTransformer();
	}
	
	protected String wfsSchemaLocation( WFS wfs ) {
    	return ResponseUtils.appendPath( wfs.getSchemaBaseURL() , "wfs/1.1.0/wfs.xsd" );
    }
    
    protected String typeSchemaLocation( WFS wfs, FeatureTypeInfo meta ) {
    	return ResponseUtils.appendPath( wfs.getSchemaBaseURL(), "cite/cite-gmlsf0.xsd" );
//    	return ResponseUtils.appendQueryString( 
//			wfs.getOnlineResource().toString(), "version=1.1.0&request=DescribeFeatureType&typeName=" + meta.name()
//		);
    }
	
	
}
