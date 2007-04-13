/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import java.io.File;
import javax.servlet.ServletException;


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
 * @version $Id$
 *
 * @see org.vfny.geoserver.config.ConfigPlugIn
 * @REVISIT: There seems to be quite a bit of code duplication in this class
 *           with LoadXMLAction, loading things, especially with the
 *           validation stuff.  Anyway we could cut that down?  Have one call
 *           the other?  Too close to release to do sucha refactoring but
 *           in 1.4 we should. -CH
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

        //JD: kill this
        //        ServletContext sc = as.getServletContext()eos;
        //        File geoserverDataDir = GeoserverDataDirectory.getGeoserverDataDirectory(sc); //geoserver_home fix
        //
        //        try {
        //            File f = geoserverDataDir; //geoserver_home fix
        //            XMLConfigReader cr = new XMLConfigReader(f,sc);
        //            
        //            GeoServer gs = new GeoServer();
        //            sc.setAttribute(GeoServer.WEB_CONTAINER_KEY, gs);
        //            
        //            Data dt = new Data(f,gs);
        //            sc.setAttribute(Data.WEB_CONTAINER_KEY, dt);
        //            
        //            WFS wfs = new WFS();
        //            sc.setAttribute(WFS.WEB_CONTAINER_KEY, wfs);
        //            
        //            WMS wms = new WMS();
        //            sc.setAttribute(WMS.WEB_CONTAINER_KEY, wms);
        //            
        //            GeoValidator gv = new GeoValidator();
        //            sc.setAttribute(GeoValidator.WEB_CONTAINER_KEY, gv);
        //
        //            if (cr.isInitialized()) {
        //                gs.load(cr.getGeoServer(),sc);
        //                wfs.load(cr.getWfs());
        //                wms.load(cr.getWms());
        //                dt.load(cr.getData());
        //                
        //                wfs.setGeoServer(gs);
        //                wms.setGeoServer(gs);
        //                wfs.setData(dt);
        //                wms.setData(dt);
        //            } else {
        //                throw new ConfigurationException(
        //                    "An error occured loading the initial configuration.");
        //            }
        //
        //
        //            try {
        //            File plugInDir = findConfigDir(geoserverDataDir, "plugIns");
        //            File validationDir = findConfigDir(geoserverDataDir, "validation");
        //            	Map plugIns = null;
        //            	Map testSuites = null;
        //            	if(plugInDir.exists()){
        //            		plugIns = XMLReader.loadPlugIns(plugInDir);
        //            		if(validationDir.exists()){
        //            			testSuites = XMLReader.loadValidations(validationDir, plugIns);
        //            			gv.load(testSuites,plugIns);
        //            		}
        //            		testSuites = new HashMap();
        //            	}else{
        //            		plugIns = new HashMap();
        //            	}
        //            	wfs.setValidation(gv);
        //            } catch (Exception e) {
        //            	// LOG error
        //            	e.printStackTrace();
        //            }
        //        } catch (ConfigurationException e) {
        //            sc.setAttribute(GeoServer.WEB_CONTAINER_KEY, null);
        //            sc.setAttribute(Data.WEB_CONTAINER_KEY, null);
        //            sc.setAttribute(WFS.WEB_CONTAINER_KEY, null);
        //            sc.setAttribute(WMS.WEB_CONTAINER_KEY, null);
        //            sc.setAttribute(GeoValidator.WEB_CONTAINER_KEY, null);
        //            throw new ServletException(e);
        //        }
        started = true;
    }

    private File findConfigDir(File rootDir, String name)
        throws Exception {
        return GeoserverDataDirectory.findConfigDir(rootDir, name);
    }
}
