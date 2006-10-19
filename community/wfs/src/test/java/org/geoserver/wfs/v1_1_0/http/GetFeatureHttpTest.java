package org.geoserver.wfs.v1_1_0.http;

import org.geoserver.http.GeoServerHttpTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.meterware.httpunit.WebResponse;

public class GetFeatureHttpTest extends GeoServerHttpTestSupport {

	public void testGet() throws Exception {
		if ( isOffline() )
			return;
		
		WebResponse response = get( "wfs?request=GetFeature&typename=cdf:Fifteen&version=1.1.0" );
		Document doc = dom( response );
		print( doc, System.out );
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList features = doc.getElementsByTagName( "cdf:Fifteen" );
		assertFalse( features.getLength() == 0 );
		
		for ( int i = 0; i < features.getLength(); i++ ) {
			Element feature = (Element) features.item( i );
			assertTrue( feature.hasAttribute( "gml:id" ) );
		}
	}
	
	public void testPost() throws Exception {
		
		if ( isOffline() )
			return;
		
		String xml = "<wfs:GetFeature " + 
		  "service=\"WFS\" " + 
		  "version=\"1.1.0\" " +
		  "xmlns:cdf=\"http://www.opengis.net/cite/data\" " + 
		  "xmlns:ogc=\"http://www.opengis.net/ogc\" " + 
		  "xmlns:wfs=\"http://www.opengis.net/wfs\" " + 
		"> " + 
		  "<wfs:Query typeName=\"cdf:Other\"> " + 
		    "<wfs:PropertyName>cdf:string2</wfs:PropertyName> " + 
		  "</wfs:Query> " + 
		"</wfs:GetFeature>";
		
		WebResponse response = post( "wfs", xml );
		Document doc = dom( response );
		print( doc, System.out );
		
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList features = doc.getElementsByTagName( "cdf:Other" );
		assertFalse( features.getLength() == 0 );
		
		for ( int i = 0; i < features.getLength(); i++ ) {
			Element feature = (Element) features.item( i );
			assertTrue( feature.hasAttribute( "gml:id" ) );
		}
	
	}
	
	public void testPostWithFilter() throws Exception {
		if ( isOffline() )
			return;
		
		String xml = "<wfs:GetFeature " + 
		  "service=\"WFS\" " + 
		  "version=\"1.1.0\" " + 
		  "outputFormat=\"text/xml; subtype=gml/3.1.1\" " + 
		  "xmlns:cdf=\"http://www.opengis.net/cite/data\" " + 
		  "xmlns:wfs=\"http://www.opengis.net/wfs\" " + 
		  "xmlns:ogc=\"http://www.opengis.net/ogc\" > " + 
		  "<wfs:Query typeName=\"cdf:Other\"> " + 
		    "<ogc:Filter> " + 
		      "<ogc:PropertyIsEqualTo> " + 
		        "<ogc:PropertyName>cdf:integers</ogc:PropertyName> " + 
		        "<ogc:Add> " + 
		          "<ogc:Literal>4</ogc:Literal> " + 
		          "<ogc:Literal>3</ogc:Literal> " + 
		        "</ogc:Add> " + 
		      "</ogc:PropertyIsEqualTo> " + 
		    "</ogc:Filter> " + 
		  "</wfs:Query> " + 
		"</wfs:GetFeature>";
			
		WebResponse response = post( "wfs", xml );
		Document doc = dom( response );
		
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList features = doc.getElementsByTagName( "cdf:Other" );
		assertFalse( features.getLength() == 0 );
		
		for ( int i = 0; i < features.getLength(); i++ ) {
			Element feature = (Element) features.item( i );
			assertTrue( feature.hasAttribute( "gml:id" ) );
		}
	}
	
	public void testResultTypeHitsGet() throws Exception {
		if ( isOffline() )
			return;
		
		WebResponse response = get( "wfs?request=GetFeature&typename=cdf:Fifteen&version=1.1.0&resultType=hits" );
		Document doc = dom( response );
		print( doc, System.out );
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList features = doc.getElementsByTagName( "cdf:Fifteen" );
		assertEquals( 0, features.getLength()  );
		
		assertEquals( "15", doc.getDocumentElement().getAttribute( "numberOfFeatures") );
	}
	
	public void testResultTypeHitsPost() throws Exception {
		if ( isOffline() )
			return;
		
		String xml = "<wfs:GetFeature " + 
		  "service=\"WFS\" " + 
		  "version=\"1.1.0\" " + 
		  "outputFormat=\"text/xml; subtype=gml/3.1.1\" " + 
		  "xmlns:cdf=\"http://www.opengis.net/cite/data\" " + 
		  "xmlns:wfs=\"http://www.opengis.net/wfs\" " + 
		  "xmlns:ogc=\"http://www.opengis.net/ogc\" " +
		  "resultType=\"hits\"> " + 
		  "<wfs:Query typeName=\"cdf:Seven\"/> " + 
		"</wfs:GetFeature>";
		
		WebResponse response = post( "wfs", xml );
		Document doc = dom( response );
		print( doc, System.out );
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList features = doc.getElementsByTagName( "cdf:Fifteen" );
		assertEquals( 0, features.getLength()  );
		
		assertEquals( "7", doc.getDocumentElement().getAttribute( "numberOfFeatures") );
	}
	
}


