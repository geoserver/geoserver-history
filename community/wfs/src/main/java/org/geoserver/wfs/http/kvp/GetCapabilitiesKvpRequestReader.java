package org.geoserver.wfs.http.kvp;

import java.util.Map;

import net.opengis.ows.v1_0_0.AcceptVersionsType;
import net.opengis.ows.v1_0_0.OWSFactory;
import net.opengis.wfs.GetCapabilitiesType;

public class GetCapabilitiesKvpRequestReader extends WFSKvpRequestReader {

	public GetCapabilitiesKvpRequestReader() {
		super( GetCapabilitiesType.class );
	}
	
	public Object read(Object request, Map kvp) throws Exception {
		request = super.read( request, kvp );
		
		//TODO: remove this class
//		//version
//		if ( kvp.c7ontainsKey( "version") ) {
//				
//			AcceptVersionsType acceptVersions = OWSFactory.eINSTANCE.createAcceptVersionsType();
//			acceptVersions.getVersion().add( kvp.get( "version") );
//			
//			GetCapabilitiesType getCapabilities = (GetCapabilitiesType) request;
//			getCapabilities.setAcceptVersions( acceptVersions );
//			
//		}
//		
		return request;
		
	} 
	

}
