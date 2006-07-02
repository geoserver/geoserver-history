package org.geoserver.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geotools.catalog.Resolve;
import org.geotools.catalog.ResolveAdapterFactory;
import org.geotools.catalog.ResolveAdapterFactoryFinder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class GeoServerResolveAdapterFactoryFinder implements
		ResolveAdapterFactoryFinder, ApplicationContextAware {

	ApplicationContext applicationContext;
	
	public boolean has( Resolve resolve, Class adapter ) {
		return factory( resolve, adapter) != null;
	}

	public ResolveAdapterFactory find( Resolve resolve, Class adapter ) {
		return factory( resolve, adapter );
	}

	public void setApplicationContext( ApplicationContext applicationContext ) 
		throws BeansException {
		
		this.applicationContext = applicationContext;
	}
	
	ResolveAdapterFactory factory( Resolve resolve, Class adapter ) {
		List matches = new ArrayList();
		
		for ( Iterator f = factories().iterator(); f. hasNext(); ) {
			ResolveAdapterFactory factory = (ResolveAdapterFactory) f.next();
			if ( factory.canAdapt( resolve, adapter ) ) {
				matches.add( factory );
			}
		}
		
		if ( matches.isEmpty() )
			return null;
		
		if ( matches.size() != 1 ) {
			String msg = "Multiple adapters found.";
			throw new IllegalStateException( msg );
		}
		
		return (ResolveAdapterFactory) matches.get( 0 );
	}
	
	Collection factories() {
		return applicationContext.getBeansOfType( ResolveAdapterFactory.class )
			.values();
	}

}
