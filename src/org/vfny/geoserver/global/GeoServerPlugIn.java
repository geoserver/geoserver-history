/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.geotools.validation.xml.XMLReader;
import org.vfny.geoserver.global.xml.XMLConfigReader;


/**
 * GeoServerPlugIn purpose.
 * 
 * <p>
 * Used to load the config into GeoServer. Is a pre-Condition for ConfigPlugIn.
 * This is started by struts.
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: GeoServerPlugIn.java,v 1.9 2004/02/20 00:28:19 dmzwiers Exp $
 *
 * @see org.vfny.geoserver.config.ConfigPlugIn
 */
public class GeoServerPlugIn implements PlugIn {
    /**
     * To allow for this class to be used as a precondition, and be pre-inited.
     *
     * @see org.vfny.geoserver.config.ConfigPlugIn
     */
    private boolean started = false;

    /**
     * Implement destroy.
     * 
     * <p>
     * Does Nothing
     * </p>
     *
     * @see org.apache.struts.action.PlugIn#destroy()
     */
    public void destroy() {
    }

    /**
     * Implement init.
     * 
     * <p>
     * This does the load of the config files for GeoServer. Check the struts
     * configuration if this is not laoding correctly.
     * </p>
     *
     * @param as Used to get ServletContext
     * @param mc Not used
     *
     * @throws javax.servlet.ServletException
     * @throws ServletException when a load error occurs
     *
     * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet,
     *      org.apache.struts.config.ModuleConfig)
     */
    public void init(ActionServlet as, ModuleConfig mc)
        throws javax.servlet.ServletException {
        if (started) {
            return;
        }

        ServletContext sc = as.getServletContext();
        String rootDir = sc.getRealPath("/");

        try {
            File f = new File(rootDir);
            XMLConfigReader cr = new XMLConfigReader(f);
            GeoServer gs = new GeoServer();
            sc.setAttribute(GeoServer.WEB_CONTAINER_KEY, gs);
            
            Data dt = new Data(f,gs);
            sc.setAttribute(Data.WEB_CONTAINER_KEY, dt);
            
            WFS wfs = new WFS();
            sc.setAttribute(WFS.WEB_CONTAINER_KEY, wfs);
            
            WMS wms = new WMS();
            sc.setAttribute(WMS.WEB_CONTAINER_KEY, wms);
            
            GeoValidator gv = new GeoValidator();
            sc.setAttribute(GeoValidator.WEB_CONTAINER_KEY, gv);

            if (cr.isInitialized()) {
                gs.load(cr.getGeoServer());
                wfs.load(cr.getWfs());
                wms.load(cr.getWms());
                dt.load(cr.getData());
                
                wfs.setGeoServer(gs);
                wms.setGeoServer(gs);
                wfs.setData(dt);
                wms.setData(dt);
            } else {
                throw new ConfigurationException(
                    "An error occured loading the initial configuration.");
            }


            try {
            	File plugInDir = new File(rootDir, "data/plugIns");
            	File validationDir = new File(rootDir, "data/validation");
            	Map plugIns = null;
            	Map testSuites = null;
            	if(plugInDir.exists()){
            		plugIns = XMLReader.loadPlugIns(plugInDir);
            		if(validationDir.exists()){
            			testSuites = XMLReader.loadValidations(validationDir, plugIns);
            			gv.load(testSuites,plugIns);
            		}
            		testSuites = new HashMap();
            	}else{
            		plugIns = new HashMap();
            	}
            	wfs.setValidation(gv);
            } catch (Exception e) {
            	// LOG error
            	e.printStackTrace();
            }
        } catch (ConfigurationException e) {
            sc.setAttribute(GeoServer.WEB_CONTAINER_KEY, null);
            sc.setAttribute(Data.WEB_CONTAINER_KEY, null);
            sc.setAttribute(WFS.WEB_CONTAINER_KEY, null);
            sc.setAttribute(WMS.WEB_CONTAINER_KEY, null);
            sc.setAttribute(GeoValidator.WEB_CONTAINER_KEY, null);
            throw new ServletException(e);
        }

        started = true;
    }
}
