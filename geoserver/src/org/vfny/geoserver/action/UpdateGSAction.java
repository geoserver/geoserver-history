/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/*
 * Created on Jan 6, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.GlobalConfig;
import org.vfny.geoserver.config.WFSConfig;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Update GeoServer with the current configuration.
 * <p>
 * This is a real ConfigAction - you need to be logged in to use it.
 * </p>
 *
 * @author User To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UpdateGSAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping,
    							 ActionForm form,
								 // UserContainer user,
								 HttpServletRequest request,
								 HttpServletResponse response)
        throws IOException, ServletException {
        GeoServer gs;
        ServletContext sc = request.getSession().getServletContext();
        gs = (GeoServer) sc.getAttribute(GeoServer.WEB_CONTAINER_KEY);

        try {
        	WMSDTO wmsDTO = getWMSConfig().toDTO();
        	WFSDTO wfsDTO = getWFSConfig().toDTO();
        	GeoServerDTO geoserverDTO = getGlobalConfig().toDTO();
        	DataDTO dataDTO = getDataConfig().toDTO();
        	// Use baseDir from initial load
        	//
        	File baseDir = gs.getData().getBaseDir();  
            gs.load( wmsDTO,wfsDTO,geoserverDTO,dataDTO, baseDir );
        } catch (ConfigurationException e) {
            throw new ServletException(e);
        }

        // We need to stay on the same page!
        
        return mapping.findForward("welcome");
    }
}
