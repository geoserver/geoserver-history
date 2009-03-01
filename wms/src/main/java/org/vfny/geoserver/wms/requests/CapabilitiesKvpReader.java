/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.WMS;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.util.requests.CapabilitiesRequest;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;


/**
 * This utility reads in a GetCapabilities KVP request and turns it into an
 * appropriate internal CapabilitiesRequest object, upon request.
 *
 * @author Rob Hranac, TOPP
 * @author Gabriel Rold?n, Axios Engineering
 * @version $Id$
 */
public class CapabilitiesKvpReader extends KvpRequestReader {
    
    private WMS wms;

    /**
     * Creates a Capabilities Kvp Reader.
     * 
     * @param kvPairs the kvp set.
     * @param wms The wms service config facade.
     */
    public CapabilitiesKvpReader(Map kvPairs, WMS wms) {
        super(kvPairs, wms.getServiceInfo());
        this.wms = wms;
    }

    /**
     * Get Capabilities request.
     *
     * @return Capabilities request.
     *
     * @throws ServiceException DOCUMENT ME!
     */
    public Request getRequest(HttpServletRequest request)
        throws ServiceException {
        CapabilitiesRequest currentRequest = new WMSCapabilitiesRequest(wms);
        currentRequest.setHttpServletRequest(request);

        String reqVersion = wms.getVersion();

        if (keyExists("VERSION")) {
            reqVersion = getValue("VERSION");
        } else if (keyExists("WMTVER")) {
            reqVersion = getValue("WMTVER");
        }

        currentRequest.setVersion(reqVersion);
        
        if (keyExists("UPDATESEQUENCE")) {
        	currentRequest.setUpdateSequence(getValue("UPDATESEQUENCE"));
        }
        
        if(keyExists("NAMESPACE")) {
            currentRequest.setNamespace(getValue("NAMESPACE"));
        }

        return currentRequest;
    }
}
