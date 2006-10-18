/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.servlets;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wms.requests.CapabilitiesKvpReader;
import org.vfny.geoserver.wms.requests.CapabilitiesXmlReader;
import org.vfny.geoserver.wms.responses.GetMapResponse;
import org.vfny.geoserver.wms.responses.WMSCapabilitiesResponse;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Rold?n
 * @version $Id: Capabilities.java,v 1.6 2004/02/09 23:29:46 dmzwiers Exp $
 */
public class Capabilities extends WMService {
	
	public Capabilities(WMS wms) {
		super("GetCapabilities",wms);
	}
	
    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected KvpRequestReader getKvpReader(Map params) {
        return new CapabilitiesKvpReader(params, this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected XmlRequestReader getXmlRequestReader() {
        return new CapabilitiesXmlReader(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Response getResponseHandler() {
    	
    		Set wmsGetMapFormats = 
    			GetMapResponse.loadImageFormats(getApplicationContext());
        return new WMSCapabilitiesResponse(wmsGetMapFormats, getApplicationContext());
    }
}
