/** 
 * Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 * 
 * @author Arne Kepp / OpenGeo
 */
package org.geowebcache.layer.wms;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.ows.Dispatcher;
import org.geotools.util.logging.Logging;
import org.geowebcache.GeoWebCacheException;
import org.geowebcache.layer.TileResponseReceiver;

public class WMSGeoServerHelper extends WMSSourceHelper {
    
    private static Logger log = Logging.getLogger(WMSGeoServerHelper.class.toString());
    
    Dispatcher gsDispatcher;
    
    public WMSGeoServerHelper(Dispatcher gsDispatcher) {
        this.gsDispatcher = gsDispatcher;
    }
    
    protected byte[] makeRequest(TileResponseReceiver tileRespRecv,
            WMSLayer layer, String wmsParams, String expectedMimeType)
            throws GeoWebCacheException {
        
        FakeHttpServletRequest req = new FakeHttpServletRequest(wmsParams);
        FakeHttpServletResponse resp = new FakeHttpServletResponse();
        
        try {
            gsDispatcher.handleRequest(req, resp);
        } catch (Exception e) {
            log.fine(e.getMessage());
            
            throw new GeoWebCacheException("Problem communicating with GeoServer" + e.getMessage());
        }
        
        if(super.mimeStringCheck(expectedMimeType, resp.getContentType())) {
            byte[] bytes = resp.getBytes();
            
            log.finer("Received " + bytes.length);
            
            tileRespRecv.setStatus(200);
            
            return bytes;
        } else {
            log.severe("Unexpected response from GeoServer for request: " + wmsParams);
            
            throw new GeoWebCacheException("Unexpected response from GeoServer for request " + wmsParams);
        }
    }
}
