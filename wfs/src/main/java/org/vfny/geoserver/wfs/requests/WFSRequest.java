/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs.requests;

import org.geotools.validation.ValidationProcessor;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.WFS;
import org.vfny.geoserver.wfs.servlets.WFService;

/**
 * Defines a general WFS Request type
 *
 * @author Gabriel Rold?n
 * @version $Id: WFSRequest.java,v 1.7 2004/02/09 23:11:33 dmzwiers Exp $
 */
abstract public class WFSRequest extends Request {
    public static final String WFS_SERVICE_TYPE = "WFS";

    /**
     * Creates the new request, supplying the request name and the sevlet 
     * handling the request.
     * 
     * @param requestType name of hte request, (Example, GetCapabiliites)
     * @param service The servlet handling the WFS request.
     */
    public WFSRequest(String requestType, WFService service) {
    		super(WFS_SERVICE_TYPE,requestType,service);
    }

    /**
     * Sets the wfs service object. 
     */
    public void setWFService(WFService wfs) {
    		setServiceRef(wfs);
    }
    
    /**
     * Returns the wfs service object..
     */
    public WFService getWFService() {
    		return (WFService) getServiceRef();
    }
    
    /**
     * Convenience method for obtaining the global wfs service instance.
     */
    public WFS getWFS() {
    		return getWFService().getWFS();
    }
    
    /**
     * Convenience method for obtaining the global geoserver instance.
     */
    public GeoServer getGeoServer(){
	    	GeoServer gs = getWFS().getGeoServer();
	    	return gs;
    }

    
    public ValidationProcessor getValidationProcessor(){
	    	ValidationProcessor vp = getWFS().getValidation();
	    	return vp;
    }
}
