package org.geoserver.wfs.http;

import java.io.InputStream;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.ows.http.XmlReader;
import org.geoserver.wfs.xml.wfs.WFS;
import org.geoserver.wfs.xml.wfs.WFSConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;

public class WfsXmlReader extends XmlReader {

	GeoServerCatalog catalog;
	
	public WfsXmlReader( String element, GeoServerCatalog catalog ) {
		super( WFS.NAMESPACE, element, "1.0.0" );
		this.catalog = catalog;
	}

	public Object parse( InputStream input ) throws Exception {
	
		Configuration configuration = new WFSConfiguration( catalog ); 
			
		Parser parser = new Parser( configuration, input );
		return parser.parse();
	}

}
