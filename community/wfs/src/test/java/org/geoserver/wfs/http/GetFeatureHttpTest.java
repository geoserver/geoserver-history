package org.geoserver.wfs.http;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class GetFeatureHttpTest extends WfsHttpTestSupport {

	protected boolean isLogging() {
		return true;
	}
	
	public void testGet() throws Exception {
		Document doc = getAsDOM( "wfs?request=GetFeature&typename=cdf:Fifteen&version=1.0.0" );
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList featureMembers = doc.getElementsByTagName( "gml:featureMember" );
		assertFalse( featureMembers.getLength() == 0 );
	}
	
	public void testPost() throws Exception {
	
		String xml = "<wfs:GetFeature " + 
		  "service=\"WFS\" " + 
		  "version=\"1.0.0\" " +
		  "xmlns:cdf=\"http://www.opengis.net/cite/data\" " + 
		  "xmlns:ogc=\"http://www.opengis.net/ogc\" " + 
		  "xmlns:wfs=\"http://www.opengis.net/wfs\" " + 
		"> " + 
		  "<wfs:Query typeName=\"cdf:Other\"> " + 
		    "<ogc:PropertyName>cdf:string2</ogc:PropertyName> " + 
		  "</wfs:Query> " + 
		"</wfs:GetFeature>";
		
		Document doc = postAsDOM( "wfs", xml );
		
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList featureMembers = doc.getElementsByTagName( "gml:featureMember" );
		assertFalse( featureMembers.getLength() == 0 );
	
	}
	
	public void testPostWithFilter() throws Exception {
	
		String xml = "<wfs:GetFeature " + 
		  "service=\"WFS\" " + 
		  "version=\"1.0.0\" " + 
		  "outputFormat=\"GML2\" " + 
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
			
		Document doc  = postAsDOM( "wfs", xml );
		
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList featureMembers = doc.getElementsByTagName( "gml:featureMember" );
		assertFalse( featureMembers.getLength() == 0 );
	}
	
	
}


