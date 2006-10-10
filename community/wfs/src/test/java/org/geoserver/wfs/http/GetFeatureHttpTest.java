package org.geoserver.wfs.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.geoserver.http.GeoServerHttpTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.meterware.httpunit.WebResponse;

public class GetFeatureHttpTest extends GeoServerHttpTestSupport {

	public void testGet() throws Exception {
		if ( isOffline() )
			return;
		
		WebResponse response = get( "wfs?request=GetFeature&typename=cdf:Fifteen" );
		Document doc = dom( response );
		print( doc, System.out );
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList featureMembers = doc.getElementsByTagName( "gml:featureMember" );
		assertFalse( featureMembers.getLength() == 0 );
	}
	
	public void testPost() throws Exception {
		
		if ( isOffline() )
			return;
		
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
		
		WebResponse response = post( "wfs", xml );
		
	//	print( response, System.out );
		
		Document doc = dom( response );
		
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList featureMembers = doc.getElementsByTagName( "gml:featureMember" );
		assertFalse( featureMembers.getLength() == 0 );
	
	}
	
	public void testPostWithFilter() throws Exception {
		if ( isOffline() )
			return;
		
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
			
		WebResponse response = post( "wfs", xml );
		Document doc = dom( response );
		
		assertEquals( "wfs:FeatureCollection", doc.getDocumentElement().getNodeName() );
		
		NodeList featureMembers = doc.getElementsByTagName( "gml:featureMember" );
		assertFalse( featureMembers.getLength() == 0 );
	}
	
	
}


