/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.geoserver.util.ReaderUtils;
import org.geotools.factory.Hints;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.vfny.geoserver.global.dto.WCSDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;
import org.vfny.geoserver.global.xml.XMLConfigReader;


/**
 * The application configuratoin facade.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class Config implements ApplicationContextAware {
    protected static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.global");
    WebApplicationContext context;
    XMLConfigReader reader;

//    public XMLConfigReader getXMLReader() throws ConfigurationException {
//        return reader;
//    }

    public File dataDirectory() {
        return GeoserverDataDirectory.getGeoserverDataDirectory();
    }

    public void setApplicationContext(ApplicationContext context)
        throws BeansException {
        
        this.context = (WebApplicationContext) context;

        // if the server admin did not set it up otherwise, force X/Y axis ordering
        // This one is a good place because we need to initialize this property
        // before any other opeation can trigger the initialization of the CRS subsystem
        if (System.getProperty("org.geotools.referencing.forceXY") == null) {
            System.setProperty("org.geotools.referencing.forceXY", "true");
        }
        if (System.getProperty("org.geotools.referencing.forceXY").equalsIgnoreCase("true")) {
            Hints.putSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING, "http");
        }

        //HACK: java.util.prefs are awful.  See
        //http://www.allaboutbalance.com/disableprefs.  When the site comes
        //back up we should implement their better way of fixing the problem.
        System.setProperty("java.util.prefs.syncInterval", "5000000");

        // HACK: under JDK 1.4.2 the native java image i/o stuff is failing
        // in all containers besides Tomcat. If running under jdk 1.4.2 we disable
        // the native codecs, unless the user forced the setting already
        if (System.getProperty("java.version").startsWith("1.4")
                && (System.getProperty("com.sun.media.imageio.disableCodecLib") == null)) {
            LOGGER.warning("Disabling mediaLib acceleration since this is a java 1.4 VM.\n"
                + "If you want to force its enabling, "
                + "set -Dcom.sun.media.imageio.disableCodecLib=true in your virtual machine");
            System.setProperty("com.sun.media.imageio.disableCodecLib", "true");
        }

        ServletContext sc = this.context.getServletContext();

        try {
            GeoserverDataDirectory.init(this.context);
            //before proceeding perform some configuration sanity checks
            try {
                doConfigSanityCheck();
            } catch (ConfigurationException ce) {
                LOGGER.severe(ce.getMessage());
                throw new BeanInitializationException(ce.getMessage());
            }
            
            reader = new XMLConfigReader(dataDirectory(), sc);
        } catch (ConfigurationException e) {
            String msg = "Error creating xml config reader";
            throw new BeanInitializationException(msg, e);
        }
    }

    /**
     * Performs a sanity check on the configuration files to allow for
     * the early failure of the bean initialization and providing
     * a meaningful failure message.
     * <p>
     * For the time being, it only ensures that the data dir exists.
     * </p>
     */
    private void doConfigSanityCheck() throws ConfigurationException {
        File dataDirectory = dataDirectory();
        try {
            ReaderUtils.checkFile(dataDirectory, true);
        } catch (FileNotFoundException e) {
            throw new ConfigurationException("Can't access the configuration directory. Reason: "
                    + e.getMessage());
        }
    }

    public WebApplicationContext getApplictionContext() {
        return context;
    }

    public DataDTO getData() {
        return reader.getData();
    }

    public GeoServerDTO getGeoServer() {
        return reader.getGeoServer();
    }

    public WMSDTO getWms() {
        return reader.getWms();
    }

    public WFSDTO getWfs() {
        return reader.getWfs();
    }

    public WCSDTO getWcs() {
        return reader.getWcs();
    }
}
