/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.data;

import org.geotools.catalog.ResolveAdapterFactory;
import org.geotools.catalog.adaptable.ResolveAdapterFactoryFinder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import java.util.Collection;


public class GeoServerResolveAdapterFactoryFinder extends ResolveAdapterFactoryFinder
    implements ApplicationContextAware {
    ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Collection getResolveAdapterFactories() {
        return applicationContext.getBeansOfType(ResolveAdapterFactory.class).values();
    }
}
