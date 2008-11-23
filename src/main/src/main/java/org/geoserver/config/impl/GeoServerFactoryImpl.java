/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config.impl;

import java.util.Collection;
import java.util.Iterator;

import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServerFactory;
import org.geoserver.config.GeoServerInfo;

import org.geoserver.config.ServiceInfo;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class GeoServerFactoryImpl implements GeoServerFactory,
        ApplicationContextAware {

    ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

    public GeoServerInfo createGlobal() {
        return new GeoServerInfoImpl();
    }

    public ContactInfo createContact() {
        return new ContactInfoImpl();
    }

    public ServiceInfo createService() {
        return new ServiceInfoImpl();
    }

    public Object create(Class clazz) {
        if (applicationContext != null) {
            Collection extensions = applicationContext.getBeansOfType(
                    GeoServerFactory.Extension.class).values();
            for (Iterator e = extensions.iterator(); e.hasNext();) {
                Extension extension = (Extension) e.next();
                if (extension.canCreate(clazz)) {
                    return extension.create(clazz);
                }
            }
        }

        return null;
    }
}
