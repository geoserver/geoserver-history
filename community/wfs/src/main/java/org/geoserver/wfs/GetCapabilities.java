/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

import net.opengis.wfs.GetCapabilitiesType;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.ows.http.VersionComparator;

/**
 * Web Feature Service GetCapabilities operation.
 * <p>
 * This operation returns a {@link org.geotools.xml.transform.TransformerBase} instance
 * which will serialize the wfs capabilities document. This class uses ows version negotiation 
 * to determine which version of the wfs capabilities document to return.
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GetCapabilities {

	/**
	 * WFS service configuration
	 */
	WFS wfs;
	/**
	 * The catalog
	 */
	GeoServerCatalog catalog;

	/**
	 * Creates a new wfs GetCapabilitis operation.
	 * 
	 * @param wfs The wfs configuration
	 * @param catalog The geoserver catalog.
	 */
	public GetCapabilities( WFS wfs, GeoServerCatalog catalog ) {
		this.wfs = wfs;
		this.catalog = catalog;
	}
	
	public CapabilitiesTransformer run( GetCapabilitiesType request ) throws WFSException {
		
		//do the version negotiation dance
		
		//any accepted versions
		if ( request.getAcceptVersions() == null 
				|| request.getAcceptVersions().getVersion().isEmpty() ) {
			//no, respond with highest
			return new CapabilitiesTransformer.WFS1_1( wfs, catalog );
		}
		
		VersionComparator vc = new VersionComparator( VersionComparator.DESCENDING );
		
		//first figure out which versions are provided
		//TODO: use an extension point?
		Set provided = new TreeSet( vc );
		provided.add( "1.0.0" );
		provided.add( "1.1.0" );
		
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
			return new CapabilitiesTransformer.WFS1_0( wfs, catalog );
		}
		
		if ( "1.1.0".equals( version ) ) {
			return new CapabilitiesTransformer.WFS1_1( wfs, catalog );
		}
		
		throw new WFSException( "Could not understand version:" + version );
		
	}
}
