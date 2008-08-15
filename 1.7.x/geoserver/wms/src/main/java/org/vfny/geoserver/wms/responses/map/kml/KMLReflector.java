/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.geoserver.wms.WebMapService;
import org.vfny.geoserver.wms.requests.GetMapRequest;


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
    public static final Integer KMSCORE = new Integer(50);

    /** default 'kmattr' value */
    public static final Boolean KMATTR = Boolean.TRUE;

    /** default 'kmplacemark' value */
    public static final Boolean KMPLACEMARK = Boolean.FALSE;

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
        
        //first set up some of the normal wms defaults
        if ( request.getWidth() < 1 ) {
            request.setWidth( 1024 );
        }
        if ( request.getHeight() < 1 ) {
            request.setHeight( 1024 );
        }
        
        //set rest of the wms defaults
        wms.reflect( request );
        
        //set some kml specific defaults
        Map fo = request.getFormatOptions();
        if ( fo.get( "kmattr") == null ) {
            fo.put( "kmattr", KMATTR );
        }
        if ( fo.get( "kmscore" ) == null ) {
            fo.put( "kmscore", KMSCORE );
        }
        if (fo.get("kmplacemark") == null) {
            fo.put("kmplacemark", KMPLACEMARK);
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
        
        //set the content disposition
        StringBuffer filename = new StringBuffer();
        for ( int i = 0; i < request.getLayers().length; i++ ) {
            String name = request.getLayers()[i].getName();
            
            //strip off prefix
            int j = name.indexOf(':');
            if ( j > -1 ) {
                    name = name.substring( j + 1 );
            }
            
            filename.append(name + "_");
        }
        filename.setLength(filename.length()-1);
        response.setHeader("Content-Disposition", 
                        "attachment; filename=" + filename.toString() + ".kml");
        
        KMLNetworkLinkTransformer transformer = new KMLNetworkLinkTransformer();
        transformer.setIndentation(3);
        Charset encoding = request.getWMS().getCharSet();
        transformer.setEncoding(encoding);
        transformer.setEncodeAsRegion( request.getSuperOverlay() );
        transformer.transform( request, response.getOutputStream() );
        
     
    }
}
