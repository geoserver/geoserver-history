package org.geoserver.wfs.v1_1_0.http;

import java.io.InputStream;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.ows.http.XmlRequestReader;
import org.geoserver.wfs.xml.wfs.v1_1_0.WFS;
import org.geoserver.wfs.xml.wfs.v1_1_0.WFSConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;

public class WfsXmlReader extends XmlRequestReader  {

	GeoServerCatalog catalog;
	
	public WfsXmlReader( String element, GeoServerCatalog catalog ) {
		super( WFS.NAMESPACE, element, "1.1.0" );
		this.catalog = catalog;
	}

	public Object read(InputStream input) throws Exception {
		Configuration configuration = new WFSConfiguration( catalog ); 
	
		Parser parser = new Parser( configuration, input );
		return parser.parse();
	}

}
