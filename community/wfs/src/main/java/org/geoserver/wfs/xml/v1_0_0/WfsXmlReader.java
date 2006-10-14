package org.geoserver.wfs.xml.v1_0_0;

import java.io.InputStream;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.ows.http.XmlRequestReader;

import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;

public class WfsXmlReader extends XmlRequestReader {

	GeoServerCatalog catalog;
	
	public WfsXmlReader( String element, GeoServerCatalog catalog ) {
		super( WFS.NAMESPACE, element, "1.0.0" );
		this.catalog = catalog;
	}

	public Object read( InputStream input ) throws Exception {
	
		Configuration configuration = new WFSConfiguration( catalog ); 
			
		Parser parser = new Parser( configuration, input );
		return parser.parse();
	}

}
