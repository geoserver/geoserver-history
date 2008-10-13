package org.geoserver.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

/**
 * Custom session factory bean which loads mappings from classpath location
 * classpath:mappings.hbm.xml.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public class GeoServerSessionFactoryBean extends LocalSessionFactoryBean implements
        ApplicationContextAware {

    ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void postProcessConfiguration(Configuration config) throws HibernateException {
        super.postProcessConfiguration(config);

        ConfigurationInitializer.postProcessConfiguration(config, applicationContext);
        config.configure();
    }

}
