package org.geoserver.data;

import java.util.Collection;

import org.geotools.catalog.ResolveAdapterFactory;
import org.geotools.catalog.adaptable.ResolveAdapterFactoryFinder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class GeoServerResolveAdapterFactoryFinder extends 
		ResolveAdapterFactoryFinder implements ApplicationContextAware {

	ApplicationContext applicationContext;
	
	public void setApplicationContext( ApplicationContext applicationContext ) 
		throws BeansException {
		
		this.applicationContext = applicationContext;
	}
	
	public Collection getResolveAdapterFactories() {
		return applicationContext.getBeansOfType( ResolveAdapterFactory.class )
			.values();
	}

}
