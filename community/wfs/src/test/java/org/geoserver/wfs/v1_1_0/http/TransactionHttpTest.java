package org.geoserver.wfs.v1_1_0.http;

import org.geoserver.http.GeoServerHttpTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.meterware.httpunit.WebResponse;

public class TransactionHttpTest extends GeoServerHttpTestSupport {

	public void testInsertWithNoSRS() throws Exception {
		if ( isOffline() ) 
			return;
		
		//1. do a getFeature
		String getFeature = "<wfs:GetFeature " + 
		  "service=\"WFS\" " + 
		  "version=\"1.1.0\" " +
		  "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " + 
		  "xmlns:ogc=\"http://www.opengis.net/ogc\" " + 
		  "xmlns:wfs=\"http://www.opengis.net/wfs\" " + 
		"> " + 
		  "<wfs:Query typeName=\"cgf:Points\"> " + 
		    "<wfs:PropertyName>cite:id</wfs:PropertyName> " + 
		  "</wfs:Query> " + 
		"</wfs:GetFeature>";
		
		WebResponse response = post( "wfs", getFeature );
		Document dom = dom( response );
		
		int n = dom.getElementsByTagName( "gml:featureMember" ).getLength();
		
		//perform an insert
		String insert = "<wfs:Transaction service=\"WFS\" version=\"1.1.0\" " + 
						"xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " + 
						"xmlns:ogc=\"http://www.opengis.net/ogc\" " +
						"xmlns:wfs=\"http://www.opengis.net/wfs\" " +
						"xmlns:gml=\"http://www.opengis.net/gml\"> " + 
						"<wfs:Insert > " +
						  "<cgf:Points>" + 
						      "<cgf:pointProperty>" + 
						        "<gml:Point>" + 
						         	"<gml:pos>1 1</gml:pos>" + 
					            "</gml:Point>" + 
					          "</cgf:pointProperty>" + 
					          "<cgf:id>t0002</cgf:id>" + 
				          "</cgf:Points>" + 
			          "</wfs:Insert>" + 
					"</wfs:Transaction>";
	
		response = post( "wfs", insert );
		dom = dom( response );
		print( dom, System.out );
		
		Element numberInserted = element( dom, "wfs:totalInserted" );
		assertNotNull( numberInserted );
		assertEquals( "1", numberInserted.getFirstChild().getNodeValue() );
		
		//do another get feature
		response = post( "wfs", getFeature );
		dom = dom( response );
		
		assertEquals( n+1, dom.getElementsByTagName( "gml:featureMember").getLength() );
	}
	
	public void testInsertWithSRS() throws Exception {
		if ( isOffline() ) 
			return;
		
		//1. do a getFeature
		String getFeature = "<wfs:GetFeature " + 
		  "service=\"WFS\" " + 
		  "version=\"1.1.0\" " +
		  "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " + 
		  "xmlns:ogc=\"http://www.opengis.net/ogc\" " + 
		  "xmlns:wfs=\"http://www.opengis.net/wfs\" " + 
		"> " + 
		  "<wfs:Query typeName=\"cgf:Points\"> " + 
		    "<wfs:PropertyName>cite:id</wfs:PropertyName> " + 
		  "</wfs:Query> " + 
		"</wfs:GetFeature>";
		
		WebResponse response = post( "wfs", getFeature );
		Document dom = dom( response );
		
		int n = dom.getElementsByTagName( "gml:featureMember" ).getLength();
		
		//perform an insert
		String insert = "<wfs:Transaction service=\"WFS\" version=\"1.1.0\" " + 
						"xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " + 
						"xmlns:ogc=\"http://www.opengis.net/ogc\" " +
						"xmlns:wfs=\"http://www.opengis.net/wfs\" " +
						"xmlns:gml=\"http://www.opengis.net/gml\"> " + 
						"<wfs:Insert srsName=\"EPSG:4326\"> " +
						  "<cgf:Points>" + 
						      "<cgf:pointProperty>" + 
						        "<gml:Point>" + 
						         	"<gml:pos>1 1</gml:pos>" + 
					            "</gml:Point>" + 
					          "</cgf:pointProperty>" + 
					          "<cgf:id>t0003</cgf:id>" + 
				          "</cgf:Points>" + 
			          "</wfs:Insert>" + 
					"</wfs:Transaction>";
	
		response = post( "wfs", insert );
		dom = dom( response );
		print( dom, System.out );
		
		Element numberInserted = element( dom, "wfs:totalInserted" );
		assertNotNull( numberInserted );
		assertEquals( "1", numberInserted.getFirstChild().getNodeValue() );
		
		//do another get feature
		response = post( "wfs", getFeature );
		dom = dom( response );
		
		NodeList pointsList = dom.getElementsByTagName( "cgf:Points" ); 
		assertEquals( n+1, pointsList.getLength() );
		
		//get the feature we inserted
		for ( int i = 0; i < pointsList.getLength(); i++) {
			Element points = (Element) pointsList.item( i );
			Element id = element( points, "cgf:id" );
			if ( "t0003".equals( id.getFirstChild().getNodeValue() ) ) {
				Element point = element( points, "gml:Point" );
				String text = point.getFirstChild().getFirstChild().getNodeValue();
				String[] xy = text.split( "," );
				
				assertFalse ( "1".equals( xy[ 0 ] ) );
				assertFalse ( "1".equals(  xy[ 1 ] ) );
			}
		}
	}
	
}
