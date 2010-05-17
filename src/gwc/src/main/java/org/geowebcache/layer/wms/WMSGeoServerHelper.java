/** 
 * Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 * 
 * @author Arne Kepp / OpenGeo
 */
package org.geowebcache.layer.wms;

import java.util.logging.Logger;

import org.geoserver.ows.Dispatcher;
import org.geotools.util.logging.Logging;
import org.geowebcache.GeoWebCacheException;
import org.geowebcache.conveyor.ConveyorTile;
import org.geowebcache.layer.TileResponseReceiver;
import org.geowebcache.mime.XMLMime;
import org.geowebcache.util.ServletUtils;

public class WMSGeoServerHelper extends WMSSourceHelper {
    
    private static Logger log = Logging.getLogger(WMSGeoServerHelper.class.toString());
    
    Dispatcher gsDispatcher;
    
    public WMSGeoServerHelper(Dispatcher gsDispatcher) {
        this.gsDispatcher = gsDispatcher;
    }
    
    protected byte[] makeRequest(TileResponseReceiver tileRespRecv,
            WMSLayer layer, String wmsParams, String expectedMimeType)
            throws GeoWebCacheException {
        
        // work around GWC setting the wrong regionation params
        if(tileRespRecv instanceof ConveyorTile &&
                ((ConveyorTile) tileRespRecv).getMimeType() == XMLMime.kml) {
            // handle format options in the middle of the request
            wmsParams = wmsParams.replaceAll("&format_options=.*&", "&");
            // handle format options at the end of the request 
            // (which is where it should be with the current GWC code)
            wmsParams = wmsParams.replaceAll("&format_options=.*$", "");
            wmsParams += "&format_options=" + ServletUtils.URLEncode("regionateBy:auto"); 
        }
        
        FakeHttpServletRequest req = new FakeHttpServletRequest(wmsParams);
        FakeHttpServletResponse resp = new FakeHttpServletResponse();
        
        try {
            gsDispatcher.handleRequest(req, resp);
        } catch (Exception e) {
            log.fine(e.getMessage());
            
            throw new GeoWebCacheException("Problem communicating with GeoServer" + e.getMessage());
        }
        
        if(super.mimeStringCheck(expectedMimeType, resp.getContentType())) {
            int responseCode = resp.getResponseCode();
            tileRespRecv.setStatus(responseCode);
            if(responseCode == 200) {
                byte[] bytes = resp.getBytes();
                
                log.finer("Received " + bytes.length);
                
                return bytes;
            } else if(responseCode == 204) {
                return new byte[0];
            } else {
                throw new GeoWebCacheException("Unexpected response from GeoServer for request " 
                        + wmsParams + ", got response code " + responseCode);
            }
        } else {
            log.severe("Unexpected response from GeoServer for request: " + wmsParams);
            
            throw new GeoWebCacheException("Unexpected response from GeoServer for request " + wmsParams);
        }
    }
}
