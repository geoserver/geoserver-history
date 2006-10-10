package org.geoserver.wfs.http;

import org.geoserver.http.GeoServerHttpTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.meterware.httpunit.WebResponse;

public class LockFeatureHttpTest extends GeoServerHttpTestSupport {

	public void testLockActionSomeAlreadyLocked() throws Exception {
		if ( isOffline() )
			return;
		
		//get a feature
		String xml = 
		"<wfs:GetFeature" +
		"  service=\"WFS\"" +
		"  version=\"1.0.0\"" +
		"  outputFormat=\"GML2\"" +
		"  xmlns:cdf=\"http://www.opengis.net/cite/data\"" +
		"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
		"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
		">" +
		"  <wfs:Query typeName=\"cdf:Locks\" />" +
		"</wfs:GetFeature>";
		
		WebResponse response = post( "wfs", xml );
		Document dom = dom( response );
		
		assertEquals( "wfs:FeatureCollection", dom.getDocumentElement().getNodeName() );
		
		//get a fid
		String fid = 
			((Element) dom.getElementsByTagName( "cdf:Locks" ).item( 0 )).getAttribute( "fid" );
		
		//lock the feature
		xml = 
			"<wfs:LockFeature" +
			"  service=\"WFS\"" +
			"  version=\"1.0.0\"" +
			"  expiry=\"10\"" +
			"  xmlns:cdf=\"http://www.opengis.net/cite/data\"" +
			"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
			"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
			">" +
			"  <wfs:Lock typeName=\"cdf:Locks\">" +
			"    <ogc:Filter>" +
			"      <ogc:FeatureId fid=\"" + fid + "\"/>" +
			"    </ogc:Filter>" +
			"  </wfs:Lock>" +
			"</wfs:LockFeature>";
		response = post( "wfs", xml );
		dom = dom( response );
		print( dom, System.out );
		assertEquals( "WFS_LockFeatureResponse", dom.getDocumentElement().getNodeName() );
		
		String lockId = dom.getElementsByTagName( "LockId" ).item( 0 ).getFirstChild().getNodeValue();
		
		//try to lock again with releaseAction = SOME
		xml = 
			"<wfs:LockFeature" +
			"  service=\"WFS\"" +
			"  version=\"1.0.0\"" +
			"  expiry=\"10\"" +
			"  lockAction=\"SOME\"" + 
			"  xmlns:cdf=\"http://www.opengis.net/cite/data\"" +
			"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
			"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
			">" +
			"  <wfs:Lock typeName=\"cdf:Locks\">" +
			"    <ogc:Filter>" +
			"      <ogc:FeatureId fid=\"" + fid + "\"/>" +
			"    </ogc:Filter>" +
			"  </wfs:Lock>" +
			"</wfs:LockFeature>";
		response = post( "wfs", xml );
		dom = dom( response );
		print( dom, System.out );
		assertEquals( "WFS_LockFeatureResponse", dom.getDocumentElement().getNodeName() );
		
		assertFalse ( dom.getElementsByTagName( "FeaturesNotLocked" ).getLength() == 0 );
		
		//release the lock
		get( "wfs?request=Release&lockId=" + lockId );
	}
	
	public void testDeleteWithoutLockId() throws Exception {
		if ( isOffline() )
			return;
		
		if ( true ) return;
		
		//get a feature
		String xml = 
		"<wfs:GetFeature" +
		"  service=\"WFS\"" +
		"  version=\"1.0.0\"" +
		"  outputFormat=\"GML2\"" +
		"  xmlns:cdf=\"http://www.opengis.net/cite/data\"" +
		"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
		"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
		">" +
		"  <wfs:Query typeName=\"cdf:Locks\" />" +
		"</wfs:GetFeature>";
		
		WebResponse response = post( "wfs", xml );
		Document dom = dom( response );
		
		assertEquals( "wfs:FeatureCollection", dom.getDocumentElement().getNodeName() );
		
		//get a fid
		String fid = 
			((Element) dom.getElementsByTagName( "cdf:Locks" ).item( 0 )).getAttribute( "fid" );
		
		//lock the feature
		xml = 
			"<wfs:LockFeature" +
			"  service=\"WFS\"" +
			"  version=\"1.0.0\"" +
			"  expiry=\"10\"" +
			"  xmlns:cdf=\"http://www.opengis.net/cite/data\"" +
			"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
			"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
			">" +
			"  <wfs:Lock typeName=\"cdf:Locks\">" +
			"    <ogc:Filter>" +
			"      <ogc:FeatureId fid=\"" + fid + "\"/>" +
			"    </ogc:Filter>" +
			"  </wfs:Lock>" +
			"</wfs:LockFeature>";
		response = post( "wfs", xml );
		dom = dom( response );
		print( dom, System.out );
		assertEquals( "WFS_LockFeatureResponse", dom.getDocumentElement().getNodeName() );
		
		String lockId = dom.getElementsByTagName( "LockId" ).item( 0 ).getFirstChild().getNodeValue();
		
		xml = "<wfs:Transaction" +
		"  service=\"WFS\"" +
		"  version=\"1.0.0\"" +
		"  xmlns:cdf=\"http://www.opengis.net/cite/data\"" +
		"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
		"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
		">" +
		"  <wfs:Delete typeName=\"cdf:Locks\">" +
		"    <ogc:Filter>" +
		"      <ogc:FeatureId fid=\"" + fid + "\"/>" +
		"    </ogc:Filter>" +
		"  </wfs:Delete>" +
		"</wfs:Transaction>";
		response = post( "wfs", xml );
		dom = dom( response );
		print( dom, System.out );
		
		assertEquals( "ServiceExceptionReport", dom.getDocumentElement().getNodeName() );
		
		//release the lock
		get( "wfs?request=Release&lockId=" + lockId );
		
	}
	
	public void testUpdateWithLockId() throws Exception {
		if ( isOffline() )
			return;
		
		if ( true ) return;
		
		//get a feature
		String xml = 
		"<wfs:GetFeature" +
		"  service=\"WFS\"" +
		"  version=\"1.0.0\"" +
		"  outputFormat=\"GML2\"" +
		"  xmlns:cdf=\"http://www.opengis.net/cite/data\"" +
		"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
		"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
		">" +
		"  <wfs:Query typeName=\"cdf:Locks\" />" +
		"</wfs:GetFeature>";
		
		WebResponse response = post( "wfs", xml );
		Document dom = dom( response );
		
		assertEquals( "wfs:FeatureCollection", dom.getDocumentElement().getNodeName() );
		
		//get a fid
		String fid = 
			((Element) dom.getElementsByTagName( "cdf:Locks" ).item( 0 )).getAttribute( "fid" );
		
		//lock the feature
		xml = 
			"<wfs:LockFeature" +
			"  service=\"WFS\"" +
			"  version=\"1.0.0\"" +
			"  expiry=\"10\"" +
			"  xmlns:cdf=\"http://www.opengis.net/cite/data\"" +
			"  xmlns:ogc=\"http://www.opengis.net/ogc\"" +
			"  xmlns:wfs=\"http://www.opengis.net/wfs\"" +
			">" +
			"  <wfs:Lock typeName=\"cdf:Locks\">" +
			"    <ogc:Filter>" +
			"      <ogc:FeatureId fid=\"" + fid + "\"/>" +
			"    </ogc:Filter>" +
			"  </wfs:Lock>" +
			"</wfs:LockFeature>";
		response = post( "wfs", xml );
		dom = dom( response );
		print( dom, System.out );
		assertEquals( "WFS_LockFeatureResponse", dom.getDocumentElement().getNodeName() );
	
		//get the lockId
		String lockId = 
			dom.getElementsByTagName( "LockId" ).item( 0 ).getFirstChild().getNodeValue();
		
		//update the feawture
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
			"      <wfs:Value>lfbt0002</wfs:Value>" +
			"    </wfs:Property>" +
			"    <ogc:Filter>" +
			"      <ogc:FeatureId fid=\"" + fid + "\"/>" +
			"    </ogc:Filter>" +
			"  </wfs:Update>" +
			"</wfs:Transaction>";
		response = post( "wfs", xml );
		dom = dom( response );
		print( dom, System.out );
		assertFalse( dom.getElementsByTagName( "wfs:SUCCESS" ).getLength() == 0 );
	}
	
}
