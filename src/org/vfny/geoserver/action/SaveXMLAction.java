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

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.xml.XMLConfigWriter;


/**
 * Save GeoServer state to XML.
 * <p>
 * This is a propert ConfigAction - you need to be logged in for this to work.
 * </p>
 */
public class SaveXMLAction extends ConfigAction {
	
    public ActionForward execute(ActionMapping mapping,
    		                     ActionForm form,
								 //UserContainer user,
								 HttpServletRequest request,
								 HttpServletResponse response)
        throws IOException, ServletException {
        GeoServer gs = getGeoServer(request);
        ServletContext sc = request.getSession().getServletContext();
        File rootDir = new File(sc.getRealPath("/"));

        try {
            XMLConfigWriter.store(gs.toWMSDTO(), gs.toWFSDTO(),
                gs.toGeoServerDTO(), gs.toDataDTO(), rootDir);
        } catch (ConfigurationException e) {
            throw new ServletException(e);
        }
        getApplicationState( request ).notifiySaveXML();	
        // We need to stash the current page?
        // or can we use null or something?
        //
        return mapping.findForward("welcome");
    }
}
