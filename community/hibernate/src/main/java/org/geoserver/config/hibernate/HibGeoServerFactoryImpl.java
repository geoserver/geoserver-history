/* 
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.geoserver.catalog.MetadataLinkInfo;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServerFactory;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.JAIInfo;
import org.geoserver.config.LoggingInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.hibernate.beans.ContactInfoImplHb;
import org.geoserver.config.hibernate.beans.GeoServerInfoImplHb;
import org.geoserver.config.hibernate.beans.LoggingInfoImplHb;
import org.geoserver.config.hibernate.beans.MetadataLinkInfoImplHb;
import org.geoserver.config.impl.JAIInfoImpl;
import org.geoserver.config.impl.ServiceInfoImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class HibGeoServerFactoryImpl implements GeoServerFactory, ApplicationContextAware,
        Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2733985227400129195L;

    private final static Logger LOGGER = Logger.getLogger(HibGeoServerFactoryImpl.class);

    transient protected ApplicationContext applicationContext = null;

    public GeoServerInfo createGlobal() {
        return new GeoServerInfoImplHb();
    }

    public ContactInfo createContact() {
        return new ContactInfoImplHb();
    }

    public ServiceInfo createService() {
        return new ServiceInfoImpl();
    }

    public <T> T create(Class<T> clazz) {
        if (applicationContext != null) {
            final Collection extensions = applicationContext.getBeansOfType(
                    GeoServerFactory.Extension.class).values();
            for (Iterator e = extensions.iterator(); e.hasNext();) {
                Extension extension = (Extension) e.next();
                if (extension.canCreate(clazz)) {
                    return extension.create(clazz);
                }
            }
        }

        LOGGER.warn("Not creating class " + clazz.getName());
        return null;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public JAIInfo createJAI() {
        return new JAIInfoImpl();
    }

    public MetadataLinkInfo createMetadataLink() {
        return new MetadataLinkInfoImplHb();
    }

    public LoggingInfo createLogging() {
        return new LoggingInfoImplHb();
    }

}
