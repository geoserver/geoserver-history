package org.geoserver.wps;

import junit.framework.Test;

import org.geoserver.test.GeoServerTestSupport;
import org.w3c.dom.Document;

public class GetCapabilitiesTest extends GeoServerTestSupport {

    //read-only test
    public static Test suite() {
        return new OneTimeTestSetup(new GetCapabilitiesTest());
    }
    
    public void test() throws Exception {
        Document d = getAsDOM( "wps?service=wps&request=getcapabilities" );
        assertEquals( "wps:Capabilities", d.getDocumentElement().getNodeName() );
        
        int np = d.getElementsByTagName( "wps:Process" ).getLength();
        assertTrue( np > 0 );
    }
}
