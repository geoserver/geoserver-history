package org.geoserver.wfs.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.opengis.wfs.FeatureCollectionType;

import org.eclipse.xsd.XSDSchema;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.http.util.ResponseUtils;
import org.geoserver.ows.ServiceException;
import org.geoserver.wfs.FeatureProducer;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.geotools.xml.Encoder;
import org.xml.sax.SAXException;

public class GML3FeatureProducer2 implements FeatureProducer {

	WFS wfs;
	GeoServerCatalog catalog;
	
	public GML3FeatureProducer2( WFS wfs, GeoServerCatalog catalog ) {
		this.wfs = wfs;
		this.catalog = catalog;
	}
	
	public Set getOutputFormats() {
		return new HashSet( 
			Arrays.asList( new String[] { "text/xml; subtype=gml/3.1.1" } )
		);
	}

	public String getContentEncoding() {
		return null;
	}

	public String getMimeType() {
		return "text/xml";
	}

	public void produce(String outputFormat, FeatureCollectionType results,
			OutputStream output) throws ServiceException, IOException {

		if ( results.getFeature().size() != 1 ) {
			throw new IllegalArgumentException( "A single feature collection expected" );
		}
		
		//TODO: multiple feature types
		FeatureCollection features = (FeatureCollection) results.getFeature().get( 0 );
		FeatureType featureType = features.getSchema();
		String namespaceURI = featureType.getNamespace().toString();
		String prefix = catalog.getNamespaceSupport().getPrefix( namespaceURI );
		FeatureTypeInfo meta = catalog.featureType( prefix, featureType.getTypeName() );
		
		//build the "application schema"
		XSDSchema schema = 
			new FeatureTypeSchemaBuilder.GML3( wfs, catalog ).build( new FeatureTypeInfo[] { meta } );
		
		WFSConfiguration configuration = new WFSConfiguration( catalog );
		XSDSchema wfsSchema = configuration.getSchemaLocator().locateSchema( 
			null, org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE, null, null
		);
		
		//incorporate the application schema into the wfs schema
		wfsSchema.getQNamePrefixToNamespaceMap().put( prefix, namespaceURI );
		for ( Iterator t = schema.getTypeDefinitions().iterator(); t.hasNext(); ) {
			wfsSchema.getTypeDefinitions().add( t.next() );
		}
		for ( Iterator e = schema.getElementDeclarations().iterator(); e.hasNext(); ) {
			wfsSchema.getElementDeclarations().add( e.next() );
		}
		
		Encoder encoder = new Encoder( configuration, wfsSchema );
		
		//declare wfs schema location
		encoder.setSchemaLocation( 
			org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE, 
			ResponseUtils.appendPath( wfs.getSchemaBaseURL(), "wfs/1.1.0/wfs.xsd" )
		);
		//declare the application schema namespace declaration
		encoder.setSchemaLocation( 
			namespaceURI, ResponseUtils.appendQueryString( 
				wfs.getOnlineResource().toString(), "version=1.1.0&request=DescribeFeatureType&typeName=" + meta.name() 
			)
		);
		
		try {
			encoder.write( features, org.geoserver.wfs.xml.v1_1_0.WFS.FEATURECOLLECTION, output );
		} 
		catch (SAXException e) {
			String msg = "Error occurred encoding features";
			throw (IOException) new IOException( msg ).initCause( e );
		}
	}

}
