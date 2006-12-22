package org.geoserver.wfs.xml.v1_1_0;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.geoserver.http.util.ResponseUtils;
import org.geoserver.ows.http.XmlRequestReader;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geotools.gml3.ApplicationSchemaConfiguration;
import org.geotools.xml.Parser;
import org.xml.sax.InputSource;

public class WfsXmlReader extends XmlRequestReader  {

	/**
	 * WFs configuration
	 */
	WFS wfs;
	
	/**
	 * Catalog
	 */
	GeoServerCatalog catalog;
	
	public WfsXmlReader( String element, WFS wfs, GeoServerCatalog catalog ) {
		super( org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE, element, "1.1.0" );
		this.wfs = wfs;
		this.catalog = catalog;
	}

	public Object read(InputStream input) throws Exception {
		WFSConfiguration configuration = new WFSConfiguration( catalog ); 
		
		//add dependencies on applicatoin schemas
//		for ( Enumeration p = catalog.getNamespaceSupport().getPrefixes(); p.hasMoreElements(); ) {
//			String prefix = (String) p.nextElement();
//			String uri = catalog.getNamespaceSupport().getURI( prefix );
//			
//			//load all the feature types for this namespace
//			List featureTypes = catalog.featureTypes( uri );
//			StringBuffer sb = new StringBuffer();
//			for ( Iterator m = featureTypes.iterator(); m.hasNext(); ) {
//				FeatureTypeInfo meta = (FeatureTypeInfo) m.next();
//				sb.append( meta.name() );
//				if ( m.hasNext() ) {
//					sb.append( "," ); 
//				}
//			}
//			
//			String schemaLocation = ResponseUtils.appendQueryString( 
//				wfs.getOnlineResource().toString(), 
//				"request=DescribeFeatureType&service=WFS&version=1.1.0&typeName=" + sb.toString()
//			);
//			
//			configuration.addDependency( new ApplicationSchemaConfiguration( uri, schemaLocation ) );
//		
//		}
		
		//TODO: make this configurable?
		configuration.getProperties().add( Parser.Properties.PARSE_UNKNOWN_ELEMENTS);
		Parser parser = new Parser( configuration );
		
		//set the input source with the correct encoding
		InputSource source = new InputSource( input );
		source.setEncoding( wfs.getCharSet().name() );
		
		Object parsed = parser.parse( source );
		
		//valid request? this should definitley be a configuration option
		
		//TODO: HACK, disabling for transaction
		if ( !"Transaction".equalsIgnoreCase( getElement() ) ) {
			if ( !parser.getValidationErrors().isEmpty() ) {
				WFSException exception = new WFSException( "Invalid request" );
				for ( Iterator e = parser.getValidationErrors().iterator(); e.hasNext(); ) {
					Exception error = (Exception) e.next();
					exception.getExceptionText().add( error.getLocalizedMessage() );
				}
				
				throw exception;
			}
		}
		
		
		return parsed;
	}

}
