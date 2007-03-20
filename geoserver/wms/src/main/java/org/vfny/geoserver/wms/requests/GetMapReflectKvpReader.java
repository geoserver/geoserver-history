/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.wms.servlets.WMService;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;


/**
 * Extends the normal GetMap kvp reader to give some reasonable defaults.  This
 * will make it easier for new WMS users to get started quickly with WMS, since
 * they won't get lots of error messages until they get everything correct.
 *
 * <p>
 * Right now the defaults are hard coded, eventually we would like to be able
 * to let users somehow pass in what they would like set as their defaults.
 * But these ones seem to be pretty reasonable:
 *
 * <ul>
 * <li>FORMAT: image/png</li>
 * <li>WIDTH: 256</li>
 * <li>HEIGHT: 256</li>
 * <li>SRS: EPSG:4326</li>
 * <li>TRANSPARENT: true</li>
 * <li>STYLES: </li>
 * <li>BBOX: -180,-90,180,90</li>
 * <li>KMSCORE: 30</li>
 * </p>
 *
 * @author Chris Holmes, TOPP
 * @version $Id$
 *
 * @task TODO: load the defaults instead of this hard coding.
 */
public class GetMapReflectKvpReader extends GetMapKvpReader {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.requests.readers.wms");
    private static final Map WMS_DEFAULTS = new HashMap();

    /**
     * Creates a new GetMapKvpReader object.
     *
     * @param kvpPairs
     *            Key Values pairs of the request
     * @param service
     *            The service handling the request
     */
    public GetMapReflectKvpReader(Map kvpPairs, WMService service) {
        super(kvpPairs, service);
    }

    static {
        WMS_DEFAULTS.put("FORMAT", "image/png");
        WMS_DEFAULTS.put("WIDTH", "256");
        WMS_DEFAULTS.put("HEIGHT", "256");
        WMS_DEFAULTS.put("SRS", "EPSG:4326");
        WMS_DEFAULTS.put("TRANSPARENT", "true");
        WMS_DEFAULTS.put("STYLES", "");
        WMS_DEFAULTS.put("BBOX", "-180,-90,180,90");
        WMS_DEFAULTS.put("KMSCORE", "30");
    }

    /**
     * Produces a <code>GetMapRequest</code> instance by parsing the GetMap
     * mandatory, optional and custom parameters.  Supplies defaults before
     * attempting to pass to the mandatory parametsrs, unlike the parent
     * class that throws errors when things aren't there.
     *
     * @param httpRequest
     *            the servlet request who's application object holds the server
     *            configuration
     *
     * @return a <code>GetMapRequest</code> completely setted up upon the
     *         parameters passed to this reader
     *
     * @throws ServiceException
     *             DOCUMENT ME!
     */
    public Request getRequest(HttpServletRequest httpRequest)
        throws ServiceException {
        GetMapRequest request = new GetMapRequest((WMService) service);
        request.setHttpServletRequest(httpRequest);

        String version = getRequestVersion();
        request.setVersion(version);
        addDefaults();
        parseMandatoryParameters(request, true);
        parseOptionalParameters(request);

        return request;
    }

    /**
     * This method adds the wms defaults to the core kvpPairs map that
     * the root kvp parsing parent class creates.  It only adds defaults
     * when they are not already present.  This is a bit of a hack, since
     * it's going straight in to the map, but there were no other ways in.
     */
    public void addDefaults() {
        Iterator defaultPairsIterator = WMS_DEFAULTS.keySet().iterator();

        while (defaultPairsIterator.hasNext()) {
            Object key = defaultPairsIterator.next();

            if (!kvpPairs.containsKey(key)) {
                kvpPairs.put(key, WMS_DEFAULTS.get(key));
            }
        }
    }
}
