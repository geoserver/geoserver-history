package org.geoserver.hibernate;

import java.io.IOException;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

/**
 * Custom session factory bean which loads mappings from classpath location
 * classpath:mappings.hbm.xml.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public class GeoServerSessionFactoryBean extends LocalSessionFactoryBean implements ApplicationContextAware {

    ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void postProcessConfiguration(Configuration config) throws HibernateException {
        super.postProcessConfiguration(config);
     // gather up other mappings on the classpath
	    try {
	        Resource[] resources = applicationContext.getResources("classpath*:mappings.hbm.xml");
	        for (int i = 0; i < resources.length; i++) {
	        	config.addInputStream(resources[i].getInputStream());
	        }
	    } catch (IOException e) {
	        // TODO: log this
	    }
    }


}
