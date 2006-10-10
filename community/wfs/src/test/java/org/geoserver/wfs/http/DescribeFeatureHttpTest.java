package org.geoserver.wfs.http;

import org.geoserver.http.GeoServerHttpTestSupport;
import org.w3c.dom.Document;

import com.meterware.httpunit.WebResponse;

public class DescribeFeatureHttpTest extends GeoServerHttpTestSupport {

	public void testGet() throws Exception {
		if ( isOffline() )
			return;
		
		WebResponse response = get( "wfs?service=WFS&request=DescribeFeatureType&version=1.0.0" );
		Document doc = dom( response );
		assertEquals( "xs:schema", doc.getDocumentElement().getNodeName() );
    }
	
	public void testPost() throws Exception {
		if ( isOffline() )
			return;
		
		String xml = "<wfs:DescribeFeatureType " + 
			"service=\"WFS\" " + 
			"version=\"1.0.0\" " + 
			"xmlns:wfs=\"http://www.opengis.net/wfs\" />";
	
		WebResponse response = post( "wfs", xml );
        	Document doc = response.getDOM();
		assertEquals( "xs:schema", doc.getDocumentElement().getNodeName() );
	}
	
	public void testPostDummyFeature() throws Exception {
		if ( isOffline() ) 
			return;
		
		String xml = "<wfs:DescribeFeatureType " + 
		"service=\"WFS\" " + 
		"version=\"1.0.0\" " + 
		"xmlns:wfs=\"http://www.opengis.net/wfs\" >" + 
		 " <wfs:TypeName>cgf:DummyFeature</wfs:TypeName>" + 
		"</wfs:DescribeFeatureType>";

		WebResponse response = post( "wfs", xml );
    		Document doc = response.getDOM();
		assertEquals( "ServiceExceptionReport", doc.getDocumentElement().getNodeName() );
		
	}
}
