package org.geoserver.wfs.http;

import org.w3c.dom.Document;

public class DescribeFeatureHttpTest extends WfsHttpTestSupport {

	public void testGet() throws Exception {
		Document doc = getAsDOM( "wfs?service=WFS&request=DescribeFeatureType&version=1.0.0" );
		assertEquals( "xs:schema", doc.getDocumentElement().getNodeName() );
    }
	
	public void testPost() throws Exception {
		String xml = "<wfs:DescribeFeatureType " + 
			"service=\"WFS\" " + 
			"version=\"1.0.0\" " + 
			"xmlns:wfs=\"http://www.opengis.net/wfs\" />";
	
		Document doc = postAsDOM( "wfs", xml );
        assertEquals( "xs:schema", doc.getDocumentElement().getNodeName() );
	}
	
	public void testPostDummyFeature() throws Exception {
		
		String xml = "<wfs:DescribeFeatureType " + 
		"service=\"WFS\" " + 
		"version=\"1.0.0\" " + 
		"xmlns:wfs=\"http://www.opengis.net/wfs\" >" + 
		 " <wfs:TypeName>cgf:DummyFeature</wfs:TypeName>" + 
		"</wfs:DescribeFeatureType>";

		Document doc = postAsDOM( "wfs", xml );
    	assertEquals( "ServiceExceptionReport", doc.getDocumentElement().getNodeName() );
	
	}
}
