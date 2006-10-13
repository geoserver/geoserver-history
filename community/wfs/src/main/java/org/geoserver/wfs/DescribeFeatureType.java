/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.wfs.DescribeFeatureTypeType;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.feature.FeatureTypeInfo;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Handles a DescribeFeatureType request and creates a DescribeFeatureType
 * response GML string.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: DescribeResponse.java,v 1.22 2004/04/16 07:06:10 jive Exp $
 *
 * @task TODO: implement the response streaming in writeTo instead of the
 *       current String generation
 */
public class DescribeFeatureType {

	/**
     * Catalog reference
     */
    private GeoServerCatalog catalog;
	/**
	 * WFS service
	 */
    private WFS wfs;
    
    public DescribeFeatureType( WFS wfs, GeoServerCatalog catalog ) {
		this.catalog = catalog;
		this.wfs = wfs;
	}
    
    public WFS getWFS() {
		return wfs;
	}
    
    public void setWFS(WFS wfs) {
		this.wfs = wfs;
	}
    
    public GeoServerCatalog getCatalog() {
		return catalog;
	}
    
    public void setCatalog(GeoServerCatalog catalog) {
		this.catalog = catalog;
	}
    
    public FeatureTypeInfo[] describeFeatureType( DescribeFeatureTypeType request ) throws WFSException {
    
    	
		List names = new ArrayList( request.getTypeName() );
		
		try {
			//list of catalog handles
			List infos = catalog.featureTypes();
			ArrayList requested = new ArrayList();
			NamespaceSupport ns = catalog.getNamespaceSupport();
			if ( !names.isEmpty() ) {
				O:for ( Iterator t = names.iterator(); t.hasNext(); ) {
					QName name = (QName) t.next();
					
					for ( Iterator h = infos.iterator(); h.hasNext(); ) {
						FeatureTypeInfo meta = (FeatureTypeInfo) h.next();
						String namespace = ns.getURI( meta.namespacePrefix() );
						String local = meta.getTypeName();
						
						if ( namespace.equals( name.getNamespaceURI() ) 
								&& local.equals( name.getLocalPart() ) ) {
								
							//found, continue on and keep this handle in list
							requested.add( meta );
							continue O;
						}
					}
					
					//not found
					String msg = "Could not find type: " + name;
					throw new WFSException( msg );
				}
			}
			else {
				//if there are no specific requested types then get all.
				requested.addAll( infos );
			}

			return (FeatureTypeInfo[]) requested.toArray( new FeatureTypeInfo[ requested.size() ] );
		} 
		catch (IOException e) {
			throw new WFSException( e, null );
		} 	
	}
}
