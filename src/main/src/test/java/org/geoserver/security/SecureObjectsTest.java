package org.geoserver.security;

import org.geoserver.platform.GeoServerExtensions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public abstract class SecureObjectsTest extends TestCase {
    
    @Override
    protected void setUp() throws Exception {
        // setup extensions so that we can do extension point lookups
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                new String[] { "classpath:/securedObjectsContext.xml"});
        new GeoServerExtensions().setApplicationContext(ac);
    }
}
