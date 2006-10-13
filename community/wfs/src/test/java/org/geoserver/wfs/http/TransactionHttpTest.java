package org.geoserver.wfs.http;

import org.geoserver.http.GeoServerHttpTestSupport;
import org.w3c.dom.Document;

import com.meterware.httpunit.WebResponse;

/**
 * This test must be run with the server configured with the wfs 1.0 cite configuration, with data
 * initialized.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class TransactionHttpTest extends GeoServerHttpTestSupport {

	public void testDelete() throws Exception {

		if ( isOffline() ) 
			return;
		
		//1. do a getFeature
		String getFeature = "<wfs:GetFeature " + 
		  "service=\"WFS\" " + 
		  "version=\"1.0.0\" " +
		  "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " + 
		  "xmlns:ogc=\"http://www.opengis.net/ogc\" " + 
		  "xmlns:wfs=\"http://www.opengis.net/wfs\" " + 
		"> " + 
		  "<wfs:Query typeName=\"cgf:Points\"> " + 
		    "<ogc:PropertyName>cite:id</ogc:PropertyName> " + 
		  "</wfs:Query> " + 
		"</wfs:GetFeature>";
		
		WebResponse response = post( "wfs", getFeature );
		Document dom = dom( response );
		
		assertEquals( 1, dom.getElementsByTagName( "gml:featureMember").getLength() );
		
		//perform a delete
		String delete = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\" " + 
						"xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " + 
						"xmlns:ogc=\"http://www.opengis.net/ogc\" " +
						"xmlns:wfs=\"http://www.opengis.net/wfs\"> " +
						"<wfs:Delete typeName=\"cgf:Points\"> " +
							"<ogc:Filter> " +
							"<ogc:PropertyIsEqualTo> " +
							"<ogc:PropertyName>cgf:id</ogc:PropertyName> " +
							"<ogc:Literal>t0000</ogc:Literal> " +
							"</ogc:PropertyIsEqualTo> " +
							"</ogc:Filter> " +
						"</wfs:Delete> " +
					"</wfs:Transaction>";
	
		response = post( "wfs", delete );
		//print( response, System.out );
		dom = dom( response );
		print( dom, System.out );
		
		assertEquals( "WFS_TransactionResponse", dom.getDocumentElement().getLocalName() );
		assertEquals( 1, dom.getElementsByTagName( "wfs:SUCCESS" ).getLength() );
		
		//do another get feature
		response = post( "wfs", getFeature );
		dom = dom( response );
		
		assertEquals( 0, dom.getElementsByTagName( "gml:featureMember").getLength() );
		
	}
	
	public void testInsert1() throws Exception {
		if ( isOffline() )
			return;
		
		String xml = 
			"<wfs:Transaction" +
			"  service=\"WFS\"" +
			"  version=\"1.0.0\"" +
			"  xmlns:cdf=\"http://www.opengis.net/cite/data\"" +
			"  xmlns:gml=\"http://www.opengis.net/gml\"" +
			"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
			"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
			">" +
			"  <wfs:Insert>" +
			"    <cdf:Inserts>" +
			"      <gml:boundedBy>" +
			"        <gml:Box srsName=\"EPSG:32615\">" +
			"          <gml:coordinates>500000,500000 500100,500100</gml:coordinates>" +
			"        </gml:Box>" +
			"      </gml:boundedBy>" +
			"      <cdf:id>ti0000</cdf:id>" +
			"      <gml:pointProperty>" +
			"        <gml:Point srsName=\"EPSG:32615\">" +
			"          <gml:coordinates>500050,500050</gml:coordinates>" +
			"        </gml:Point>" +
			"      </gml:pointProperty>" +
			"    </cdf:Inserts>" +
			"  </wfs:Insert>" +
			"</wfs:Transaction>";
	}
	
	public void testInsert() throws Exception {
		if ( isOffline() ) 
			return;
		
		//1. do a getFeature
		String getFeature = "<wfs:GetFeature " + 
		  "service=\"WFS\" " + 
		  "version=\"1.0.0\" " +
		  "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " + 
		  "xmlns:ogc=\"http://www.opengis.net/ogc\" " + 
		  "xmlns:wfs=\"http://www.opengis.net/wfs\" " + 
		"> " + 
		  "<wfs:Query typeName=\"cgf:Lines\"> " + 
		    "<ogc:PropertyName>cite:id</ogc:PropertyName> " + 
		  "</wfs:Query> " + 
		"</wfs:GetFeature>";
		
		WebResponse response = post( "wfs", getFeature );
		Document dom = dom( response );
		
		assertEquals( 1, dom.getElementsByTagName( "gml:featureMember").getLength() );
		
		//perform an insert
		String insert = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\" " + 
						"xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " + 
						"xmlns:ogc=\"http://www.opengis.net/ogc\" " +
						"xmlns:wfs=\"http://www.opengis.net/wfs\" " +
						"xmlns:gml=\"http://www.opengis.net/gml\"> " + 
						"<wfs:Insert > " +
						  "<cgf:Lines>" + 
						      "<cgf:lineStringProperty>" + 
						        "<gml:LineString>" + 
						         	"<gml:coordinates decimal=\".\" cs=\",\" ts=\" \">" + 
						"494475.71056415,5433016.8189323 494982.70115662,5435041.95096618" + 
									"</gml:coordinates>" + 
					            "</gml:LineString>" + 
					          "</cgf:lineStringProperty>" + 
					          "<cgf:id>t0002</cgf:id>" + 
				          "</cgf:Lines>" + 
			          "</wfs:Insert>" + 
					"</wfs:Transaction>";
	
		response = post( "wfs", insert );
		dom = dom( response );
		print( dom, System.out );
		
		assertTrue( dom.getElementsByTagName( "wfs:SUCCESS" ).getLength() != 0 );
		assertTrue( dom.getElementsByTagName( "wfs:InsertResult" ).getLength() != 0 );
		
		//do another get feature
		response = post( "wfs", getFeature );
		dom = dom( response );
		
		assertEquals( 2, dom.getElementsByTagName( "gml:featureMember").getLength() );
	}
	
	public void testUpdate() throws Exception {
	if ( isOffline() ) 
		return;
	
	//1. do a getFeature
	String getFeature = "<wfs:GetFeature " + 
	  "service=\"WFS\" " + 
	  "version=\"1.0.0\" " +
	  "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " + 
	  "xmlns:ogc=\"http://www.opengis.net/ogc\" " + 
	  "xmlns:wfs=\"http://www.opengis.net/wfs\" " + 
	"> " + 
	  "<wfs:Query typeName=\"cgf:Polygons\"> " + 
	    "<ogc:PropertyName>cite:id</ogc:PropertyName> " + 
	  "</wfs:Query> " + 
	"</wfs:GetFeature>";
	
	WebResponse response = post( "wfs", getFeature );
	Document dom = dom( response );
	
	assertEquals( 1, dom.getElementsByTagName( "gml:featureMember").getLength() );
	assertEquals( "t0002", dom.getElementsByTagName("cgf:id").item( 0 ).getFirstChild().getNodeValue() );
	
	//perform an update
	String insert = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\" " + 
					"xmlns:cgf=\"http://www.opengis.net/cite/geometry\" " + 
					"xmlns:ogc=\"http://www.opengis.net/ogc\" " +
					"xmlns:wfs=\"http://www.opengis.net/wfs\" " +
					"xmlns:gml=\"http://www.opengis.net/gml\"> " + 
					"<wfs:Update typeName=\"cgf:Polygons\" > " +
					  "<wfs:Property>" + 
					      "<wfs:Name>id</wfs:Name>" +
					      "<wfs:Value>t0003</wfs:Value>" + 
				      "</wfs:Property>" + 
				      "<ogc:Filter>" + 
				      	"<ogc:PropertyIsEqualTo>" + 
				      		"<ogc:PropertyName>id</ogc:PropertyName>" + 
				      		"<ogc:Literal>t0002</ogc:Literal>" + 
				      	"</ogc:PropertyIsEqualTo>" + 
				      "</ogc:Filter>" + 
		          "</wfs:Update>" + 
				"</wfs:Transaction>";

	response = post( "wfs", insert );
	//print( response, System.out );
	dom = dom( response );
	
	//do another get feature
	response = post( "wfs", getFeature );
	dom = dom( response );
	
	assertEquals( "t0003", dom.getElementsByTagName("cgf:id").item( 0 ).getFirstChild().getNodeValue() );
}
	
}
