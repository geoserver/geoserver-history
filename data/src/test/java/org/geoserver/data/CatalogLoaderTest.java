package org.geoserver.data;

import java.io.File;
import java.util.Collection;

import junit.framework.TestCase;

import org.geotools.catalog.ServiceFactory;
import org.geotools.catalog.property.PropertyServiceFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CatalogLoaderTest extends TestCase {


	public void testLoadCatalog() throws Exception {
		ClassPathXmlApplicationContext context = 
			new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
		
		Collection factories = context.getBeansOfType( ServiceFactory.class )
			.values();
		assertFalse( factories.isEmpty() );
		
		PropertyServiceFactory factory = 
			(PropertyServiceFactory) factories.iterator().next();
		assertTrue( factory.canProcess( new File( "/tmp" ).toURI() ) );
		
		GeoServerCatalog catalog = (GeoServerCatalog) context.getBean("catalog");
		assertFalse(catalog.members( null ).isEmpty());	
		
	}
}
