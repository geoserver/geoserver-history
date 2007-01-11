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
	
	/**
	 * Xml Configuration
	 */
	WFSConfiguration configuration;
	
	public WfsXmlReader( String element, WFS wfs, GeoServerCatalog catalog, WFSConfiguration configuration ) {
		super( org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE, element, "1.1.0" );
		this.wfs = wfs;
		this.catalog = catalog;
		this.configuration = configuration;
	}

	public Object read(InputStream input) throws Exception {
		
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
