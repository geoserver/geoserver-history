package org.geoserver.test;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ui.context.Theme;
import org.springframework.web.context.WebApplicationContext;

import com.mockrunner.mock.web.MockServletContext;

/**
 * A spring application context used for GeoServer testing.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GeoServerTestApplicationContext extends
        ClassPathXmlApplicationContext implements WebApplicationContext {

    ServletContext servletContext;
    
    public GeoServerTestApplicationContext(String configLocation, ServletContext servletContext ) throws BeansException {
        super(new String[]{configLocation}, false );
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public Theme getTheme(String themeName) {
        return null;
    }

}
