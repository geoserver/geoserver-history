package org.geoserver.wfs.v1_1_0;

import java.math.BigInteger;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.wfs.EMFUtils;
import org.geoserver.wfs.Query;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geotools.feature.FeatureCollection;
import org.opengis.filter.sort.SortBy;

import net.opengis.wfs.v1_1_0.GetFeatureType;
import net.opengis.wfs.v1_1_0.WFSFactory;
import net.opengis.wfs.v1_1_0.WFSPackage;

/**
 * WFS 1.1.0 GetFeature operation.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GetFeature extends org.geoserver.wfs.GetFeature {

	public GetFeature(WFS wfs, GeoServerCatalog catalog) {
		super(wfs, catalog, WFSFactory.eINSTANCE );
	}

	public void setSrsName( List srsName ) throws WFSException {
		batchSet( "srsName", srsName );
	}
	
	public void setSortBy( List sortBy ) throws WFSException {
		batchSet( "sortBy", sortBy );
	}
	
	protected EObject request() {
		if ( request == null ) {
			synchronized ( this ) {
				if ( request == null ) {
					request = factory.create( WFSPackage.eINSTANCE.getGetFeatureType() );
				}
			}
		}
		
		return request;
	}
	
	protected EObject query() {
		return factory.create( WFSPackage.eINSTANCE.getQueryType() );
	}
	
	protected Query query( EObject q ) {
		Query query = super.query( q );
		
		//set the wfs 1.1. specific stuff
		if ( EMFUtils.isSet( q, "srsName" ) ) {
			query.setSrsName( (String) EMFUtils.get( q, "srsName" ) );
		}
		
		if ( EMFUtils.isSet( q, "sortBy" ) ) {
			query.setSortBy( (SortBy) EMFUtils.get( q, "sortBy") );
		}
		
		return query;
	}
	
	
	
	
}
