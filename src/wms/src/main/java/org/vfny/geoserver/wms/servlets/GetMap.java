/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.servlets;

import org.geoserver.wms.WMS;
import org.geoserver.wms.WMSExtensions;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.wms.responses.GetMapResponse;


/**
 * WMS service wich returns request and response handlers to manage a GetMap
 * request
 *
 * @author Gabriel Rold?n
 * @version $Id$
 */
public class GetMap extends WMService {
    /**
     * Part of HTTP content type header.
     */
    public static final String URLENCODED = "application/x-www-form-urlencoded";

    /**
     * Creates a new GetMap object.
     *
     */
    public GetMap(WMS wms) {
        super("GetMap", wms);
    }

    protected GetMap(String id, WMS wms) {
        super(id, wms);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Response getResponseHandler() {
        return new GetMapResponse(WMSExtensions.findMapProducers(getApplicationContext()));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws java.lang.UnsupportedOperationException DOCUMENT ME!
     */
//    protected XmlRequestReader getXmlRequestReader() {
//        return new GetMapXmlReader(getWMS());
//    }

    /**
     * DOCUMENT ME!
     *
     * @param params DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
//    protected KvpRequestReader getKvpReader(Map params) {
//        Map layers = this.getWMS().getBaseMapLayers();
//        Map styles = this.getWMS().getBaseMapStyles();
//
//        GetMapKvpReader kvp = new GetMapKvpReader(params, getWMS());
//
//        // filter layers and styles if the user specified "layers=basemap"
//        // This must happen after the kvp reader has been initially called
//        if ((layers != null) && !layers.equals("")) {
//            kvp.filterBaseMap(layers, styles);
//        }
//
//        return kvp;
//    }

    /**
     * A method that decides if a request is a multipart request.
     * <p>
     * <a href="http://www.w3.org/TR/REC-html40/interact/forms.html#form-content-type">w3.org content type</a>
     * </p>
     *
     * @param req the servlet request
     * @return if this is multipart or not
     */
//    public boolean isURLEncoded(HttpServletRequest req) {
//        //Get the content type from the request
//        String contentType = req.getContentType();
//
//        //If there is no content type, then it is not multipart
//        if (contentType == null) {
//            return false;
//        }
//
//        //If it starts with multipart/ then it is multipart
//        return contentType.toLowerCase().startsWith(URLENCODED);
//    }
}
