package org.geoserver.wfs.v1_1_0.http;

import org.geoserver.wfs.http.WfsHttpTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GetFeatureHttpTest extends WfsHttpTestSupport {

	protected boolean isLogging() {
		return true;
	}
	
	public void testGet() throws Exception {
		Document doc = getAsDOM( "wfs?request=GetFeature&typename=cdf:Fifteen&version=1.1.0" );
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList features = doc.getElementsByTagName( "cdf:Fifteen" );
		assertFalse( features.getLength() == 0 );
		
		for ( int i = 0; i < features.getLength(); i++ ) {
			Element feature = (Element) features.item( i );
			assertTrue( feature.hasAttribute( "gml:id" ) );
		}
	}
	
	public void testPost() throws Exception {
		
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
		
		Document doc = postAsDOM( "wfs", xml );
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList features = doc.getElementsByTagName( "cdf:Other" );
		assertFalse( features.getLength() == 0 );
		
		for ( int i = 0; i < features.getLength(); i++ ) {
			Element feature = (Element) features.item( i );
			assertTrue( feature.hasAttribute( "gml:id" ) );
		}
	
	}
	
	public void testPostWithFilter() throws Exception {
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
			
		Document doc = postAsDOM( "wfs", xml );
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList features = doc.getElementsByTagName( "cdf:Other" );
		assertFalse( features.getLength() == 0 );
		
		for ( int i = 0; i < features.getLength(); i++ ) {
			Element feature = (Element) features.item( i );
			assertTrue( feature.hasAttribute( "gml:id" ) );
		}
	}
	
	public void testResultTypeHitsGet() throws Exception {
		Document doc = getAsDOM( "wfs?request=GetFeature&typename=cdf:Fifteen&version=1.1.0&resultType=hits" );
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList features = doc.getElementsByTagName( "cdf:Fifteen" );
		assertEquals( 0, features.getLength()  );
		
		assertEquals( "15", doc.getDocumentElement().getAttribute( "numberOfFeatures") );
	}
	
	public void testResultTypeHitsPost() throws Exception {
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
		
		Document doc = postAsDOM( "wfs", xml );
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList features = doc.getElementsByTagName( "cdf:Fifteen" );
		assertEquals( 0, features.getLength()  );
		
		assertEquals( "7", doc.getDocumentElement().getAttribute( "numberOfFeatures") );
	}
	
	public void testWithGmlIdAttribute() throws Exception {
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
		        "<ogc:PropertyName>@gml:id</ogc:PropertyName> " + 
		        "<ogc:Literal>Other.0</ogc:Literal>" + 
		      "</ogc:PropertyIsEqualTo> " + 
		    "</ogc:Filter> " + 
		  "</wfs:Query> " + 
		"</wfs:GetFeature>";
		
		Document dom = postAsDOM( "wfs", xml );
		assertEquals( 1, dom.getElementsByTagName( "gml:featureMember" ).getLength() );
	}
	
	public void testWithSRS() throws Exception {
		String xml =  "<wfs:GetFeature xmlns:wfs=\"http://www.opengis.net/wfs\" version=\"1.1.0\" service=\"WFS\">" + 
        "<wfs:Query xmlns:cdf=\"http://www.opengis.net/cite/data\" typeName=\"cdf:Other\" srsName=\"urn:x-ogc:def:crs:EPSG:6.11.2:4326\"/>" + 
        "</wfs:GetFeature>";

		Document dom = postAsDOM( "wfs", xml );
		assertEquals( 1, dom.getElementsByTagName( "gml:featureMember" ).getLength() );
	}
	
}


