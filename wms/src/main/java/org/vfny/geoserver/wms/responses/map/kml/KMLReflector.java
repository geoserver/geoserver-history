/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import org.geoserver.wms.WebMapService;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.GetMapResponse;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


/**
 * KML reflecting service.
 * <p>
 * This
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class KMLReflector {
    /**
     * kml/kmz mime types
     */
    public static final String KML_MIME_TYPE = "application/vnd.google-earth.kml+xml";
    public static final String KMZ_MIME_TYPE = "application/vnd.google-earth.kmz+xml";

    /** default 'kmscore' value */
    public static final Integer KMSCORE = new Integer(30);

    /** default 'kmattr' value */
    public static final Boolean KMATTR = Boolean.TRUE;

    /** default 'format' value */
    public static final String FORMAT = KML_MIME_TYPE;

    /**
     * web map service
     */
    WebMapService wms;

    public KMLReflector(WebMapService wms) {
        this.wms = wms;
    }

    public void wms(GetMapRequest request, HttpServletResponse response) throws Exception {
        
        //first set all the normal wms reflecting defaults
        wms.reflect( request );
        
        //set some kml specific defaults
        Map fo = request.getFormatOptions();
        if ( fo.get( "kmattr") == null ) {
            fo.put( "kmattr", KMATTR );
        }
        if ( fo.get( "kmscore" ) == null ) {
            fo.put( "kmscore", KMSCORE );
        }
        
        //set the format
        //TODO: create a subclass of GetMapRequest to store these values
        if ( request.getSuperOverlay() ) {
            request.setFormat( KML_MIME_TYPE );
        }
        else {
            request.setFormat( KMZ_MIME_TYPE );
        }
        
        response.setContentType( KML_MIME_TYPE );
        
        KMLNetworkLinkTransformer transformer = new KMLNetworkLinkTransformer();
        transformer.setEncodeAsRegion( request.getSuperOverlay() );
        transformer.transform( request, response.getOutputStream() );
    }
}
