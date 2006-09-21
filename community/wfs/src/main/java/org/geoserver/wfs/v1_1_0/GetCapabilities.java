package org.geoserver.wfs.v1_1_0;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import net.opengis.wfs.v1_1_0.GetCapabilitiesType;
import net.opengis.wfs.v1_1_0.WFSFactory;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.ows.Service;
import org.geoserver.ows.http.VersionComparator;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geotools.xml.transform.TransformerBase;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * WFS 1.1 GetCapabilities operation.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GetCapabilities implements ApplicationContextAware {

	/**
	 * WFS configuration
	 */
	WFS wfs;
	/**
	 * GeoServer catalog
	 */
	GeoServerCatalog catalog;
	/**
	 * The request
	 */
	GetCapabilitiesType request;
	
	/**
	 * The application context;
	 */
	ApplicationContext context;
	
	public GetCapabilities( WFS wfs, GeoServerCatalog catalog ) {
		this.wfs = wfs;
		this.catalog = catalog;
	}
	 
	public void setApplicationContext(ApplicationContext  context) throws BeansException {
		this.context = context;
	}
	
	public TransformerBase getCapabilities() throws Exception {
		return getCapabilities( request() );
	}
	
	public TransformerBase getCapabilities( GetCapabilitiesType request ) throws WFSException {
		//do the version negotiation dance
		
		//any accepted versions
		if ( request.getAcceptVersions() == null 
				|| request.getAcceptVersions().getVersion().isEmpty() ) {
			//no, response with this
			return new WFSCapabilitiesTransformer( wfs, catalog );
		}
		
		VersionComparator vc = new VersionComparator( VersionComparator.DESCENDING );
		
		//first figure out which versions are provided
		Set provided = new TreeSet( vc );
		Collection services = context.getBeansOfType( Service.class ).values();
		for ( Iterator s = services.iterator(); s.hasNext(); ) {
			Service service = (Service) s.next();
			if ( "WFS".equalsIgnoreCase( service.getId() ) ) {
				provided.add( service.getVersion() );
			}
		}
		
		//next figure out what the client accepts
		Set accepted = new TreeSet( vc );
		accepted.addAll( request.getAcceptVersions().getVersion() );
		
		//prune out those not provided
		for ( Iterator v = accepted.iterator(); v.hasNext(); ) {
			String version = (String) v.next();
			if ( !provided.contains( version ) )
				v.remove();
		}
		
		String version = null;
		if ( !accepted.isEmpty() ) {
			//return the highest version provided
			version = (String) accepted.iterator().next();
		}
		else {
			accepted = new TreeSet( vc );
			accepted.addAll( request.getAcceptVersions().getVersion() );
			
			LinkedList providedList = new LinkedList( provided );
			LinkedList acceptedList = new LinkedList( accepted );
			
			//if highest accepted less then lowest provided, send lowest
			if ( vc.compare( acceptedList.getFirst() , providedList.getLast() ) > 0 ) {
				version = (String) providedList.getLast();
			}
			
			//if lowest accepted is less then highest provided, send highest
			if ( vc.compare( acceptedList.getLast(), providedList.getFirst() ) < 0 ) {
				version = (String) providedList.getFirst();
			}
			
			if ( version == null ) {
				//go through from lowest to highest, and return highest which is less then 
				String last = (String) providedList.getLast();
				for ( int i = providedList.size() - 2; i > -1; i-- ) {
					String current = (String) providedList.get( i );
					
					//if current greater then
					if ( vc.compare( current, acceptedList.getFirst() ) < 0 ) {
						break;
					}
					
					last = current;
				}
				
				version = last;
			}
			
		}
		
		if ( "1.0.0".equals( version ) ) {
			return new org.geoserver.wfs.GetCapabilities( wfs, catalog ).getCapabilities();
		}
		
		if ( "1.1.0".equals( version ) ) {
			return new WFSCapabilitiesTransformer( wfs, catalog );
		}
		
		throw new WFSException( "Could not understand version:" + version );
	}
	
	GetCapabilitiesType request() {
		if ( request == null ) {
			request = WFSFactory.eINSTANCE.createGetCapabilitiesType();
		}
		
		return request;
	}
}
