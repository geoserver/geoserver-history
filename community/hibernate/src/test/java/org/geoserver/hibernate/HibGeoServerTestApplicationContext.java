/* 
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.hibernate;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.ui.context.Theme;
import org.springframework.web.context.WebApplicationContext;

/**
 * A spring application context used for GeoServer testing.
 * 
 */
public class HibGeoServerTestApplicationContext extends ClassPathXmlApplicationContext implements
        WebApplicationContext {

    ServletContext servletContext;

    public HibGeoServerTestApplicationContext(String configLocation, String dataPath)
            throws BeansException {
        this(new String[] { configLocation }, dataPath);
    }

    public HibGeoServerTestApplicationContext(String[] configLocation, String dataPath)
            throws BeansException {
        super(configLocation, false);

        MockServletContext ctx = new MockServletContext();
        ctx.addInitParameter("GEOSERVER_DATA_DIR", dataPath);
        ctx.addInitParameter("serviceStrategy", "PARTIAL-BUFFER2");

        this.servletContext = ctx;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public Theme getTheme(String themeName) {
        return null;
    }
}
