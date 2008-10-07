package org.geoserver.hibernate;

import java.io.IOException;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

/**
 * Initializes a hibernate configuration "manually".
 * <p>
 * This class should not be used if you intend to use springs session factory 
 * support ({@link LocalSessionFactoryBean}. See {@link GeoServerSessionFactoryBean}
 * for this case.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class ConfigurationInitializer implements BeanFactoryPostProcessor,
	ApplicationContextAware {

	ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
		//create the hibernate configuration
		Configuration cfg = new Configuration();
		
		postProcessConfiguration( cfg, applicationContext );
		cfg.configure();
		
		//register the session factory
		SessionFactory sessionFactory = cfg.buildSessionFactory();
		sessionFactory.getCurrentSession().beginTransaction();
		
		beanFactory.registerSingleton( "sessionFactory", sessionFactory );
		
	}

	public static void postProcessConfiguration( Configuration cfg, ApplicationContext applicationContext ) {
		//gather up other mappings on the classpath
		try {
			Resource[] resources = 
				applicationContext.getResources( "classpath*:mappings.hbm.xml" );
			for ( int i = 0; i < resources.length; i++) {
				cfg.addInputStream( resources[i].getInputStream() );
			}
		} 
		catch (IOException e) {
			//TODO: log this
		}
	}
	
	public static void initializeDatabase(ApplicationContext applicationContext) {
		//create a configuration
		Configuration cfg = new Configuration();
		cfg.configure();
		
		//load the mappings
		postProcessConfiguration(cfg, applicationContext);
		
		//drop and create the database
		SchemaExport export = new SchemaExport( cfg );
		export.drop( true, true );
		export.create( true , true );
	}
}
