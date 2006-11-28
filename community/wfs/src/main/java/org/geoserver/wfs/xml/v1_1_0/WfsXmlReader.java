package org.geoserver.wfs.xml.v1_1_0;

import java.io.InputStream;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.ows.http.XmlRequestReader;
import org.geoserver.wfs.WFS;
import org.geotools.xml.Configuration;
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
		Configuration configuration = new WFSConfiguration( catalog ); 
	
		Parser parser = new Parser( configuration );
		
		//set the input source with the correct encoding
		InputSource source = new InputSource( input );
		source.setEncoding( wfs.getCharSet().name() );
		
		return parser.parse( source );
	}

}
