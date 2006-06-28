package org.geoserver.data;

import java.util.Collection;

import junit.framework.TestCase;

import org.geotools.catalog.ServiceFactory;
import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

public class CatalogLoaderTest extends TestCase {


	public void testLoadCatalog() throws Exception {
		ClassPathXmlApplicationContext context = 
			new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
		
		Collection factories = context.getBeansOfType( ServiceFactory.class )
			.values();
		assertFalse( factories.isEmpty() );
		
		GeoServerCatalog catalog = (GeoServerCatalog) context.getBean("catalog");
		assertFalse(catalog.members( null ).isEmpty());	
		
	}
}
