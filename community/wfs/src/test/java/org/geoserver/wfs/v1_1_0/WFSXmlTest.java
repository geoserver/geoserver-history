package org.geoserver.wfs.v1_1_0;

import net.opengis.ows.v1_0_0.GetCapabilitiesType;

import org.geoserver.wfs.xml.v1_1_0.WFS;
import org.geoserver.wfs.xml.v1_1_0.WFSConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.test.XMLTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WFSXmlTest extends XMLTestSupport {

	protected Element createRootElement( Document doc ) {
		Element root = doc.createElementNS( 
			WFS.GETCAPABILITIES.getNamespaceURI(), WFS.GETCAPABILITIES.getLocalPart()
		);
		root.setAttribute( "version", "1.1.0" );
		
		return root;
	}

	protected void registerSchemaLocation( Element element ) {
		element.setAttribute( 
			"xs:schemaLocation", WFS.NAMESPACE + " wfs.xsd"	
		);
	}

	protected Configuration createConfiguration() {
		return new WFSConfiguration( null );
	}
	
	public void test() throws Exception {
		GetCapabilitiesType getCapabilities = (GetCapabilitiesType) parse();
		assertNotNull( getCapabilities );
	}

}
