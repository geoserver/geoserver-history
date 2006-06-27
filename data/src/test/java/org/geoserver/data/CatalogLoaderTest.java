package org.geoserver.data;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public class CatalogLoaderTest extends TestCase {


	public void testLoadCatalog() throws Exception {
		ClassPathXmlApplicationContext context = 
			new ClassPathXmlApplicationContext("classpath*:**/applicationContext.xml");
		
		GeoServerCatalog catalog = (GeoServerCatalog) context.getBean("catalog");
		assertFalse(catalog.members( null ).isEmpty());	
		
	}
}
