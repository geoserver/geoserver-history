package org.geoserver.restconfig;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.geoserver.test.GeoServerTestSupport;
import org.w3c.dom.Document;

public class StyleTest extends GeoServerTestSupport {

	public void testGetS() throws Exception {
		String path = "rest/styles";
        Document d = getAsDOM(path);
		
		assertEquals( "html", d.getDocumentElement().getNodeName() );
	}
}
