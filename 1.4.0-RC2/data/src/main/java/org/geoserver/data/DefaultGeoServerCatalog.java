package org.geoserver.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geotools.catalog.Catalog;
import org.geotools.catalog.GeoResource;
import org.geotools.catalog.Service;
import org.geotools.catalog.defaults.DefaultCatalog;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Default catalog implementation. 
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class DefaultGeoServerCatalog extends DefaultCatalog implements 
	GeoServerCatalog {
	
	NamespaceSupport namespaceSupport;
	
	public DefaultGeoServerCatalog( ) {
		namespaceSupport = new NamespaceSupport();
	}
	
	public NamespaceSupport getNamespaceSupport() {
		return namespaceSupport;
	}
	
	public List services( Class resolvee ) throws IOException {
		return services( this, resolvee );
	}
	
	protected static List services( Catalog catalog, Class resolvee ) 
		throws IOException {
		
		List handles = new ArrayList();
		List services = catalog.members( null );
		for ( Iterator s = services.iterator(); s.hasNext(); ) {
			Service service = (Service) s.next();
			if ( service.canResolve( resolvee ) ) {
				handles.add( service );
			}
		}
	
		return handles;
	}
	
	public List resources( Class resolvee ) 
		throws IOException {
		
		return resources( this, resolvee );
	}
	
	protected static List resources( Catalog catalog, Class resolvee )
		throws IOException {
		
		List handles = new ArrayList(); 
		List services = catalog.members( null );
		for ( Iterator s = services.iterator(); s.hasNext(); ) {
			
			Service service = (Service) s.next();
			List resources = service.members( null );
			for ( Iterator r = resources.iterator(); r.hasNext(); ) {
				GeoResource resource = (GeoResource) r.next();
				if ( resource.canResolve( resolvee ) ) {
					handles.add( resource );
				}
			}
			
		}
		
		return handles;
	}
	
}
