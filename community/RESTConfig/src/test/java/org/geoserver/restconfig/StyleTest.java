package org.geoserver.restconfig;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.geoserver.test.GeoServerTestSupport;
import org.w3c.dom.Document;

public class StyleTest extends GeoServerTestSupport {

	public void testGetS() throws Exception {
		String path = "api/styles";
        Document d = getAsDOM(path);
//		InputStream in = get( path );
//        BufferedReader br = new BufferedReader(new InputStreamReader(in));
//        String line = null;
//
//        while ((line = br.readLine()) != null){
//            System.out.println(line);
//        }
		
		assertEquals( "html", d.getDocumentElement().getNodeName() );
	}
}
