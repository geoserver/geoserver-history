package org.geoserver.hibernate;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

/**
 * Post processor which drops and recreates the underlying databased based on the session factory
 * configuration.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public class SessionFactoryPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {

        if ("sessionFactory".equals(beanName) && bean instanceof LocalSessionFactoryBean) {
            try {
                LocalSessionFactoryBean sessionFactory = (LocalSessionFactoryBean) bean;

                Configuration cfg = new Configuration();
                cfg.configure();

                // load all mappings
                Resource[] resources = applicationContext
                        .getResources("classpath*:mappings.hbm.xml");
                for (Resource resource : resources) {
                    cfg.addInputStream(resource.getInputStream());
                }

                SchemaExport export = new SchemaExport(cfg);
                export.drop(true, true);
                export.create(true, true);
            } catch (Exception e) {
                throw new BeanInitializationException(null, e);
            }
        }
        return bean;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

}
