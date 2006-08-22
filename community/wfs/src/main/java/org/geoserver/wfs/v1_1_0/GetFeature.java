package org.geoserver.wfs.v1_1_0;

import org.geotools.feature.FeatureCollection;

import net.opengis.wfs.v1_1_0.GetFeatureType;
import net.opengis.wfs.v1_1_0.WFSFactory;

/**
 * WFS 1.1.0 GetFeature operation.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GetFeature {

	
	GetFeatureType request;
	
	public FeatureCollection getFeature() {
		return getFeature( request() );
	}
	
	public FeatureCollection getFeature( GetFeatureType request ) {
		return null;
	}
	
	protected GetFeatureType request() {
		if ( request == null ) {
			synchronized ( this ) {
				if ( request == null ) {
					request = WFSFactory.eINSTANCE.createGetFeatureType();
				}
			}
		}
		
		return request;
	}
	
}
