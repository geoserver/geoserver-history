package org.openplans.geoserver.gt.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geotools.catalog.Catalog;
import org.geotools.catalog.ServiceFactory;
import org.geotools.catalog.defaults.DefaultServiceFinder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * An implementation of ServiceFinder used for finding services inside of a 
 * Spring context.
 * 
 * @author Justin Deoliveira, jdeolive@openplans.org
 *
 */
public class GSServiceFinder extends DefaultServiceFinder 
	implements ApplicationContextAware {

	public static final String ID = "org.openplans.geoserver.gt.serviceFinder";
	
	ApplicationContext context;
	
	public GSServiceFinder(Catalog catalog) {
		super(catalog);
	}

	public void setApplicationContext(ApplicationContext context) 
		throws BeansException {
		
		this.context = context;
	}
	
	public List getServiceFactories() {
		Map factories =
			context.getBeansOfType(ServiceFactory.class);
		
		return new ArrayList(factories.values());
	}
	
}
