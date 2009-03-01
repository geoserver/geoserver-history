/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.servlets;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geoserver.ows.util.KvpMap;
import org.geoserver.ows.util.KvpUtils;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.WMS;
import org.geoserver.wms.kvp.GetMapKvpRequestReader;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.wms.requests.GetKMLReflectKvpReader;
import org.vfny.geoserver.wms.requests.GetMapRequest;


/**
 * This class takes in a simple WMS request, presumably from Google Earth, and
 * produces a completed WMS request that outputs KML/KMZ. To map a request to
 * this, simple pass a "layers=myLayer" parameter to "wms/kml_reflect":
 * <b>http://localhost:8080/geoserver/wms/kml_reflect?layers=states<b>
 * No extra information, such as styles or EPSG code need to be passed.
 * A request to kml_reflect will return a network link for each layer
 * passed in. Each network layer makes a full WMS request with these
 * default values:
 * - smart KMZ output (vector or raster output)
 * - KMScore value of 30
 * - Image size of 1024x1024
 * - Full attribution on vector features
 * - WMS version 1.0.0
 * - Transparent
 *
 *
 * @author Brent Owens
 * 
 * @deprecated use {@link org.vfny.geoserver.wms.responses.map.kml.KMLReflector}.
 *
 */
public class KMLReflector extends WMService {
    private static Logger LOGGER = 
        org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.wms.servlets");
    final String KML_MIME_TYPE = "application/vnd.google-earth.kml+xml";
    final String KMZ_MIME_TYPE = "application/vnd.google-earth.kmz+xml";

    org.vfny.geoserver.wms.responses.map.kml.KMLReflector delegate;

    public KMLReflector() {
        super("kml_reflect", null);
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        setWMS((WMS) config.getServletContext().getAttribute(WMS.WEB_CONTAINER_KEY));
        delegate = (org.vfny.geoserver.wms.responses.map.kml.KMLReflector)
            GeoServerExtensions.bean("kmlService");
    }

    protected KvpRequestReader getKvpReader(Map params) {
        return new GetKMLReflectKvpReader(params, getWMS());
    }

    /**
     * Delegate the request to the real request handler.
     *
     * @param request the HttpServletRequest being handled
     * @param response the HttpServletResponse to which the results should be written
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            GetMapKvpRequestReader requestReader = 
                new GetMapKvpRequestReader(getWMS());
            requestReader.setHttpRequest(request);

            GetMapRequest serviceRequest = null;

            try {
                serviceRequest = (GetMapRequest) requestReader.createRequest();
                Map parsedKvp = KvpUtils.normalize(request.getParameterMap());
                Map rawKvp = new KvpMap(parsedKvp);
                serviceRequest = 
                    (GetMapRequest) requestReader.read(serviceRequest, parsedKvp, rawKvp);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING,
                        "Failure while trying to parse KML reflector request",
                        e);
                if (e instanceof ServiceException){
                    throw (ServiceException)e;
                } else {
                    throw new ServiceException(e);
                }
            }
            
            try{
                delegate.wms(serviceRequest, response);
            } catch (Exception e){ 
                throw new ServletException(e);
            }
        }
}
