package org.geoserver.wfs.http;

import org.geoserver.http.GeoServerHttpTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.meterware.httpunit.WebResponse;

public class GetFeatureWithLockHttpTest extends GeoServerHttpTestSupport {

	public void testUpdateLockedFeatureWithLockId() throws Exception {
		if ( isOffline() ) 
			return;
		
		//get feature
		String xml = 
			"<wfs:GetFeature " + 
			"service=\"WFS\" " + 
			"version=\"1.0.0\" " + 
			"expiry=\"10\" " + 
			"xmlns:cdf=\"http://www.opengis.net/cite/data\" " + 
			"xmlns:ogc=\"http://www.opengis.net/ogc\" " + 
			"xmlns:wfs=\"http://www.opengis.net/wfs\">" + 
			"<wfs:Query typeName=\"cdf:Locks\"/>" +  
		"</wfs:GetFeature>";
		
		WebResponse response = post( "wfs", xml );
		Document dom = dom( response );
		
		//get a fid
		assertEquals( "wfs:FeatureCollection", dom.getDocumentElement().getNodeName() );
		assertFalse( dom.getElementsByTagName( "cdf:Locks" ).getLength() == 0 );
		
		String fid = ((Element)dom.getElementsByTagName( "cdf:Locks" ).item( 0 )).getAttribute( "fid" );
		
		//lock a feature
		xml = 
		"<wfs:GetFeatureWithLock " + 
			"service=\"WFS\" " + 
			"version=\"1.0.0\" " + 
			"expiry=\"10\" " + 
			"xmlns:cdf=\"http://www.opengis.net/cite/data\" " + 
			"xmlns:ogc=\"http://www.opengis.net/ogc\" " + 
			"xmlns:wfs=\"http://www.opengis.net/wfs\">" + 
			"<wfs:Query typeName=\"cdf:Locks\">" + 
				"<ogc:Filter>" + 
					"<ogc:FeatureId fid=\"" + fid + "\"/>" +
				"</ogc:Filter>" + 
			"</wfs:Query>" + 
		"</wfs:GetFeatureWithLock>";
		
		response = post( "wfs", xml );
		dom = dom( response );
		print( dom, System.out );
		assertEquals( "wfs:FeatureCollection", dom.getDocumentElement().getNodeName() );
	
		String lockId = dom.getDocumentElement().getAttribute( "lockId" );
		
		//try to update it
		xml = "<wfs:Transaction " +
		"  service=\"WFS\" " +
		"  version=\"1.0.0\" " +
		"  xmlns:cdf=\"http://www.opengis.net/cite/data\" " +
		"  xmlns:ogc=\"http://www.opengis.net/ogc\" " +
		"  xmlns:wfs=\"http://www.opengis.net/wfs\" " +
		"> " +
		"  <wfs:LockId>" + lockId + "</wfs:LockId>" + 
		"  <wfs:Update typeName=\"cdf:Locks\"> " +
		"    <wfs:Property> " +
		"      <wfs:Name>cdf:id</wfs:Name> " +
		"      <wfs:Value>gfwlbt0001</wfs:Value> " +
		"    </wfs:Property> " +
		"    <ogc:Filter> " +
		"      <ogc:FeatureId fid=\"" + fid + "\"/> " +
		"    </ogc:Filter> " +
		"  </wfs:Update> " +
		"</wfs:Transaction> ";
		
		response = post( "wfs", xml );
		dom = dom( response );
		print( dom, System.out );
		
		assertEquals( "wfs:WFS_TransactionResponse", dom.getDocumentElement().getNodeName() );
		assertEquals( 1, dom.getElementsByTagName( "wfs:SUCCESS" ).getLength() );
		
		//release the lock
		get( "wfs?request=Release&lockId=" + lockId );
	}
	
	public void testUpdateLockedFeatureWithoutLockId() throws Exception {
		if ( isOffline() ) 
			return;
		
		//get feature
		String xml = 
			"<wfs:GetFeature " + 
			"service=\"WFS\" " + 
			"version=\"1.0.0\" " + 
			"expiry=\"10\" " + 
			"xmlns:cdf=\"http://www.opengis.net/cite/data\" " + 
			"xmlns:ogc=\"http://www.opengis.net/ogc\" " + 
			"xmlns:wfs=\"http://www.opengis.net/wfs\">" + 
			"<wfs:Query typeName=\"cdf:Locks\"/>" +  
		"</wfs:GetFeature>";
		
		WebResponse response = post( "wfs", xml );
		Document dom = dom( response );
		
		//get a fid
		assertEquals( "wfs:FeatureCollection", dom.getDocumentElement().getNodeName() );
		assertFalse( dom.getElementsByTagName( "cdf:Locks" ).getLength() == 0 );
		
		String fid = ((Element)dom.getElementsByTagName( "cdf:Locks" ).item( 0 )).getAttribute( "fid" );
		
		//lock a feature
		xml = 
		"<wfs:GetFeatureWithLock " + 
			"service=\"WFS\" " + 
			"version=\"1.0.0\" " + 
			"expiry=\"10\" " + 
			"xmlns:cdf=\"http://www.opengis.net/cite/data\" " + 
			"xmlns:ogc=\"http://www.opengis.net/ogc\" " + 
			"xmlns:wfs=\"http://www.opengis.net/wfs\">" + 
			"<wfs:Query typeName=\"cdf:Locks\">" + 
				"<ogc:Filter>" + 
					"<ogc:FeatureId fid=\"" + fid + "\"/>" +
				"</ogc:Filter>" + 
			"</wfs:Query>" + 
		"</wfs:GetFeatureWithLock>";
		
		response = post( "wfs", xml );
		dom = dom( response );
		assertEquals( "wfs:FeatureCollection", dom.getDocumentElement().getNodeName() );
	
		String lockId = dom.getDocumentElement().getAttribute( "lockId" );
		
		//try to update it
		xml = "<wfs:Transaction " +
		"  service=\"WFS\" " +
		"  version=\"1.0.0\" " +
		"  xmlns:cdf=\"http://www.opengis.net/cite/data\" " +
		"  xmlns:ogc=\"http://www.opengis.net/ogc\" " +
		"  xmlns:wfs=\"http://www.opengis.net/wfs\" " +
		"> " +
		"  <wfs:Update typeName=\"cdf:Locks\"> " +
		"    <wfs:Property> " +
		"      <wfs:Name>cdf:id</wfs:Name> " +
		"      <wfs:Value>gfwlbt0001</wfs:Value> " +
		"    </wfs:Property> " +
		"    <ogc:Filter> " +
		"      <ogc:FeatureId fid=\"" + fid + "\"/> " +
		"    </ogc:Filter> " +
		"  </wfs:Update> " +
		"</wfs:Transaction> ";
		
		response = post( "wfs", xml );
		dom = dom( response );
		
		assertEquals( "wfs:WFS_TransactionResponse", dom.getDocumentElement().getNodeName() );
		assertEquals( 1, dom.getElementsByTagName( "wfs:FAILED" ).getLength() );
		
		//release the lock
		get( "wfs?request=Release&lockId=" + lockId );
	}
	
	public void testGetFeatureWithLockReleaseActionSome() throws Exception {
		if ( isOffline() )
			return;
		
		String xml = 
			"<wfs:GetFeature" +
			"  service=\"WFS\"" +
			"  version=\"1.0.0\"" +
			"  expiry=\"10\"" +
			"  xmlns:cdf=\"http://www.opengis.net/cite/data\"" +
			"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
			"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
			">" +
			"  <wfs:Query typeName=\"cdf:Locks\"/>" +
			"</wfs:GetFeature>";
		WebResponse response = post( "wfs", xml );
		Document dom = dom ( response );
		
		//get two fids
		NodeList locks = dom.getElementsByTagName( "cdf:Locks" );
		String fid1 = ((Element)locks.item( 0 )).getAttribute( "fid" );
		String fid2 = ((Element)locks.item( 1 )).getAttribute( "fid" );
		
		xml = 
		"<wfs:GetFeatureWithLock" +
		"  service=\"WFS\"" +
		"  version=\"1.0.0\"" +
		"  expiry=\"10\"" +
		"  xmlns:cdf=\"http://www.opengis.net/cite/data\"" +
		"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
		"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
		">" +
		"  <wfs:Query typeName=\"cdf:Locks\">" +
		"    <ogc:Filter>" +
		"      <ogc:FeatureId fid=\"" + fid1 + "\"/>" +
		"      <ogc:FeatureId fid=\"" + fid2 + "\"/>" +
		"    </ogc:Filter>" +
		"  </wfs:Query>" +
		"</wfs:GetFeatureWithLock>";
		
		response = post( "wfs", xml );
		dom = dom( response );
		print( dom, System.out );
		
		assertEquals( "wfs:FeatureCollection", dom.getDocumentElement().getNodeName() );
	
		String lockId = dom.getDocumentElement().getAttribute( "lockId" );
		System.out.println( lockId );
		xml = 
		"<wfs:Transaction" +
		"  service=\"WFS\"" +
		"  version=\"1.0.0\"" +
		"  releaseAction=\"SOME\"" +
		"  xmlns:cdf=\"http://www.opengis.net/cite/data\"" +
		"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
		"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
		">" +
		"  <wfs:LockId>" + lockId + "</wfs:LockId>" +
		"  <wfs:Update typeName=\"cdf:Locks\">" +
		"    <wfs:Property>" +
		"      <wfs:Name>cdf:id</wfs:Name>" +
		"      <wfs:Value>gfwlrs0003</wfs:Value>" +
		"    </wfs:Property>" +
		"    <ogc:Filter>" +
		"      <ogc:FeatureId fid=\"" + fid1 + "\"/>" +
		"    </ogc:Filter>" +
		"  </wfs:Update>" +
		"</wfs:Transaction>";
		
		response = post( "wfs", xml );
		dom = dom( response );
		print( dom , System.out );
		
		assertEquals( "wfs:WFS_TransactionResponse", dom.getDocumentElement().getNodeName() );
		assertEquals( 1, dom.getElementsByTagName( "wfs:SUCCESS" ).getLength() );
		
		xml = 
			"<wfs:Transaction" +
			"  service=\"WFS\"" +
			"  version=\"1.0.0\"" +
			"  xmlns:cdf=\"http://www.opengis.net/cite/data\"" +
			"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
			"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
			">" +
			"  <wfs:LockId>" + lockId + "</wfs:LockId>" +
			"  <wfs:Update typeName=\"cdf:Locks\">" +
			"    <wfs:Property>" +
			"      <wfs:Name>cdf:id</wfs:Name>" +
			"      <wfs:Value>gfwlrs0004</wfs:Value>" +
			"    </wfs:Property>" +
			"    <ogc:Filter>" +
			"      <ogc:FeatureId fid=\"" + fid2 + "\"/>" +
			"    </ogc:Filter>" +
			"  </wfs:Update>" +
			"</wfs:Transaction>";
		
		response = post( "wfs", xml );
		dom = dom( response );
		print( dom, System.out );
		assertEquals( "wfs:WFS_TransactionResponse", dom.getDocumentElement().getNodeName() );
		assertEquals( 1, dom.getElementsByTagName( "wfs:SUCCESS" ).getLength() );
		
		get( "wfs?request=Release&lockId=" + lockId );
		
	}
}
