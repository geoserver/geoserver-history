/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.data;

import org.geotools.catalog.Catalog;
import org.geotools.catalog.ServiceFactory;
import org.geotools.catalog.defaults.DefaultServiceFinder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import java.util.ArrayList;
import java.util.List;


/**
 * Finds services declared as beans in a spring context.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GeoServerServiceFinder extends DefaultServiceFinder implements ApplicationContextAware {
    ApplicationContext context;

    public GeoServerServiceFinder(Catalog catalog) {
        super(catalog);
    }

    public void setApplicationContext(ApplicationContext context)
        throws BeansException {
        this.context = context;
    }

    /**
     * Uses application context to locate instances of {@link ServiceFactory}.
     */
    public List getServiceFactories() {
        return new ArrayList(context.getBeansOfType(ServiceFactory.class).values());
    }
}
