/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.test;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ui.context.Theme;
import org.springframework.web.context.WebApplicationContext;


/**
 * A spring application context used for GeoServer testing.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GeoServerTestApplicationContext extends ClassPathXmlApplicationContext
    implements WebApplicationContext {
    ServletContext servletContext;

    public GeoServerTestApplicationContext(String configLocation, ServletContext servletContext)
        throws BeansException {
        this(new String[] { configLocation }, servletContext);
    }

    public GeoServerTestApplicationContext(String[] configLocation, ServletContext servletContext)
        throws BeansException {
        super(configLocation, false);
        this.servletContext = servletContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public Theme getTheme(String themeName) {
        return null;
    }
}
