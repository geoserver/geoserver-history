/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import javax.servlet.ServletException;


/**
 * ConfigPlugIn purpose.
 *
 * <p>
 * Used to set-up config's memory model.   REQUIRED  Pre-condition must be run
 * after GeoServerPlugIn
 * </p>
 *
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id$
 *
 * @see org.vfny.geoserver.global.GeoServerPlugIn
 */
public class ConfigPlugIn implements PlugIn {
    /**
     * Implement destroy.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @see org.apache.struts.action.PlugIn#destroy()
     */
    public void destroy() {
        // does nothing.
    }

    /**
     * Implement init.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param arg0
     * @param arg1
     *
     * @throws ServletException
     *
     * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet,
     *      org.apache.struts.config.ModuleConfig)
     */
    public void init(ActionServlet arg0, ModuleConfig arg1)
        throws ServletException {
        //JD: delete this	
        //        ServletContext sc = arg0.getServletContext();
        //        WMS wms = (WMS) sc.getAttribute(WMS.WEB_CONTAINER_KEY);
        //        WFS wfs = (WFS) sc.getAttribute(WFS.WEB_CONTAINER_KEY);
        //
        //        if (wms == null || wfs == null) {
        //            GeoServerPlugIn gspi = new GeoServerPlugIn();
        //            gspi.init(arg0, arg1);
        //            wms = (WMS) sc.getAttribute(WMS.WEB_CONTAINER_KEY);
        //            wfs = (WFS) sc.getAttribute(WFS.WEB_CONTAINER_KEY);
        //
        //            if (wms == null || wfs == null) {
        //                throw new ServletException(
        //                    "GeoServerPlugIn Failed. Thus ConfigPlugIn cannot run.");
        //            }
        //        }
        //
        //        sc.setAttribute(WMSConfig.CONFIG_KEY, new WMSConfig((WMSDTO)wms.toDTO()));
        //        sc.setAttribute(WFSConfig.CONFIG_KEY, new WFSConfig((WFSDTO)wfs.toDTO()));
        //        sc.setAttribute(GlobalConfig.CONFIG_KEY,
        //            new GlobalConfig((GeoServerDTO)wfs.getGeoServer().toDTO()));
        //        sc.setAttribute(DataConfig.CONFIG_KEY, new DataConfig((DataDTO)wfs.getData().toDTO()));
        //        
        //        ValidationConfig vc = null;
        //		try{
        //			vc = new ValidationConfig((Map)wfs.getValidation().toPlugInDTO(), (Map)wfs.getValidation().toTestSuiteDTO());
        //		}catch(Exception e){
        //			//load error
        //			vc = new ValidationConfig();
        //		}
        //        sc.setAttribute(ValidationConfig.CONFIG_KEY, vc);
    }
}
