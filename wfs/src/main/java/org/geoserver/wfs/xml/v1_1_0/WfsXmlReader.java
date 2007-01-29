package org.geoserver.wfs.xml.v1_1_0;

import java.io.InputStream;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.geoserver.ows.XmlRequestReader;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geotools.util.Version;
import org.geotools.xml.Parser;
import org.vfny.geoserver.global.Data;
import org.xml.sax.InputSource;

public class WfsXmlReader extends XmlRequestReader  {

	/**
	 * WFs configuration
	 */
	WFS wfs;
	
	/**
	 * Catalog
	 */
	Data catalog;
	
	/**
	 * Xml Configuration
	 */
	WFSConfiguration configuration;
	
	public WfsXmlReader( String element, WFS wfs, Data catalog, WFSConfiguration configuration ) {
		super( new QName( org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE, element ), new Version( "1.1.0" ) );
		this.wfs = wfs;
		this.catalog = catalog;
		this.configuration = configuration;
	}

	public Object read(InputStream input) throws Exception {
		
		//TODO: make this configurable?
		configuration.getProperties().add( Parser.Properties.PARSE_UNKNOWN_ELEMENTS);
		Parser parser = new Parser( configuration );
		
		//validate, make this configurable
		parser.setValidating( true );
		
		//set the input source with the correct encoding
		InputSource source = new InputSource( input );
		source.setEncoding( wfs.getCharSet().name() );
		
		Object parsed = parser.parse( source );
		
		//valid request? this should definitley be a configuration option
		//TODO: HACK, disabling validation for transaction
		if ( !"Transaction".equalsIgnoreCase( getElement().getLocalPart() ) ) {
			if ( !parser.getValidationErrors().isEmpty() ) {
				WFSException exception = new WFSException( "Invalid request", "InvalidParameterValue" );
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
