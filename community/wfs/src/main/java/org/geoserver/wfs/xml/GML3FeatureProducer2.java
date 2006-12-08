package org.geoserver.wfs.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.opengis.wfs.FeatureCollectionType;

import org.eclipse.xsd.XSDSchema;
import org.geoserver.GeoServerResourceLoader;
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
	GeoServerResourceLoader resourceLoader;
	
	public GML3FeatureProducer2( WFS wfs, GeoServerCatalog catalog, GeoServerResourceLoader resourceLoader ) {
		this.wfs = wfs;
		this.catalog = catalog;
		this.resourceLoader = resourceLoader;
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

		List featureCollections = results.getFeature();
		
		//round up the info objects for each feature collection
		HashMap/*<String,Set>*/ ns2metas = new HashMap();
		for ( Iterator fc = featureCollections.iterator(); fc.hasNext(); ) {
			FeatureCollection features = (FeatureCollection) fc.next();
			FeatureType featureType = features.getSchema();
			
			String namespaceURI = featureType.getNamespace().toString();
			String prefix = catalog.getNamespaceSupport().getPrefix( namespaceURI );
			
			//load the metadata for the feature type
			FeatureTypeInfo meta = catalog.featureType( prefix, featureType.getTypeName() );
			
			//add it to the map
			Set metas = (Set) ns2metas.get( namespaceURI );
			if ( metas == null ) {
				metas = new HashSet();
				ns2metas.put( namespaceURI, metas );
			}
			
			metas.add( meta );
			
		}
		
		//get the wfs schema
		WFSConfiguration configuration = new WFSConfiguration( catalog );
		XSDSchema wfsSchema = configuration.getSchemaLocator().locateSchema( 
			null, org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE, null, null
		);
		
		//incorporate application schemas into the wfs schema
		for ( Iterator i = ns2metas.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry entry = (Map.Entry) i.next();
			String namespaceURI = (String) entry.getKey();
			Set metas = (Set) entry.getValue();
			
			//build the schema for the types in the single namespace
			XSDSchema schema = 
				new FeatureTypeSchemaBuilder.GML3( wfs, catalog, resourceLoader ).build( 
					(FeatureTypeInfo[]) metas.toArray( new FeatureTypeInfo[ metas.size() ] ) 
				);
			
			//declare the namespace
			String prefix = catalog.getNamespaceSupport().getPrefix ( namespaceURI );
			wfsSchema.getQNamePrefixToNamespaceMap().put( prefix, namespaceURI );
			
			//add the types + elements to the wfs schema
			for ( Iterator t = schema.getTypeDefinitions().iterator(); t.hasNext(); ) {
				wfsSchema.getTypeDefinitions().add( t.next() );
			}
			for ( Iterator e = schema.getElementDeclarations().iterator(); e.hasNext(); ) {
				wfsSchema.getElementDeclarations().add( e.next() );
			}
		}
		
		Encoder encoder = new Encoder( configuration, wfsSchema );
		
		//declare wfs schema location
		encoder.setSchemaLocation( 
			org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE, 
			ResponseUtils.appendPath( wfs.getSchemaBaseURL(), "wfs/1.1.0/wfs.xsd" )
		);
		
		//declare application schema namespaces
		HashMap ns2types = new HashMap();
		
		for ( Iterator i = ns2metas.entrySet().iterator(); i.hasNext(); ) {
			Map.Entry entry = (Map.Entry) i.next();
			
			String namespaceURI = (String) entry.getKey();
			Set metas = (Set) entry.getValue();
			
			StringBuffer typeNames = new StringBuffer();
			for ( Iterator m = metas.iterator(); m.hasNext(); ) {
				FeatureTypeInfo meta = (FeatureTypeInfo) m.next();
				typeNames.append( meta.name() );
				
				if ( m.hasNext() ) typeNames.append( "," );
			}
			
			//set the schema location
			encoder.setSchemaLocation( 
				namespaceURI, ResponseUtils.appendQueryString( 
					wfs.getOnlineResource().toString(), "version=1.1.0&request=DescribeFeatureType&typeName=" + typeNames.toString()
				)
			);
			
		}
		
		try {
			encoder.write( results, org.geoserver.wfs.xml.v1_1_0.WFS.FEATURECOLLECTION, output );
		} 
		catch (SAXException e) {
			String msg = "Error occurred encoding features";
			throw (IOException) new IOException( msg ).initCause( e );
		}
	}

}
