package org.geoserver.wfs.v1_1_0.http;

import org.geoserver.wfs.http.WfsHttpTestSupport;
import org.w3c.dom.Document;

public class GetFeatureWithLockHttpTest extends WfsHttpTestSupport {

	protected boolean isLogging() {
		return true;
	}
	
	public void test() throws Exception {
		String xml = 
			"<wfs:GetFeatureWithLock service=\"WFS\" version=\"1.1.0\" " + 
			"	  handle=\"GetFeatureWithLock-tc1\"" + 
			"	  expiry=\"5\"" + 
			"	  resultType=\"results\"" + 
			"	  xmlns:wfs=\"http://www.opengis.net/wfs\"" + 
			"	  xmlns:sf=\"http://cite.opengeospatial.org/gmlsf\">" + 
			"	  <wfs:Query handle=\"qry-1\" typeName=\"sf:PrimitiveGeoFeature\" />" + 
			"	</wfs:GetFeatureWithLock>";

		Document dom = postAsDOM( "wfs", xml );
	}
}
