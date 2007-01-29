package org.geoserver.wfs.kvp;

import java.util.Map;

import net.opengis.wfs.GetCapabilitiesType;

public class GetCapabilitiesKvpRequestReader extends WFSKvpRequestReader {

	public GetCapabilitiesKvpRequestReader() {
		super( GetCapabilitiesType.class );
	}
	
	public Object read(Object request, Map kvp) throws Exception {
		request = super.read( request, kvp );
		
		//TODO: this is a cite thing, make configurable
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
