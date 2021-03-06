/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.georss;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetMapOutputFormat;
import org.geoserver.wms.GetMapRequest;
import org.geoserver.wms.MapProducerCapabilities;
import org.geoserver.wms.WMS;
import org.geoserver.wms.WMSMapContext;
import org.geoserver.wms.map.XMLTransformerMap;

public class AtomGeoRSSMapOutputFormat implements GetMapOutputFormat {
    /** mime type */
    public static String MIME_TYPE = "application/atom+xml";
    
    static final MapProducerCapabilities ATOM_CAPABILITIES = new MapProducerCapabilities(false, false, false, true);

    /** format names/aliases */
    public static final Set<String> FORMAT_NAMES;
    static {
        String[] FORMATS = { MIME_TYPE, "atom", "application/atom xml" };
        Set<String> names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        names.addAll(Arrays.asList(FORMATS));
        FORMAT_NAMES = Collections.unmodifiableSet(names);
    }

    private WMS wms;

    public AtomGeoRSSMapOutputFormat(WMS wms) {
        this.wms = wms;
    }

    /**
     * @see org.geoserver.wms.GetMapOutputFormat#getMimeType()
     */
    public String getMimeType() {
        return MIME_TYPE;
    }

    /**
     * @see GetMapOutputFormat#getOutputFormatNames()
     */
    public Set<String> getOutputFormatNames() {
        return FORMAT_NAMES;
    }

    /**
     * @see org.geoserver.wms.GetMapOutputFormat#produceMap(org.geoserver.wms.WMSMapContext)
     */
    public XMLTransformerMap produceMap(WMSMapContext mapContext) throws ServiceException,
            IOException {

        AtomGeoRSSTransformer tx = new AtomGeoRSSTransformer(wms);
        GetMapRequest request = mapContext.getRequest();

        String geometryEncoding = (String) request.getFormatOptions().get("encoding");
        if ("gml".equals(geometryEncoding)) {
            tx.setGeometryEncoding(GeoRSSTransformerBase.GeometryEncoding.GML);
        } else if ("latlong".equals(geometryEncoding)) {
            tx.setGeometryEncoding(GeoRSSTransformerBase.GeometryEncoding.LATLONG);
        } else {
            tx.setGeometryEncoding(GeoRSSTransformerBase.GeometryEncoding.SIMPLE);
        }

        Charset encoding = wms.getCharSet();
        tx.setEncoding(encoding);

        XMLTransformerMap result = new XMLTransformerMap(mapContext, tx, mapContext, getMimeType());
        return result;
    }

    public MapProducerCapabilities getCapabilities(String format) {
        return ATOM_CAPABILITIES;
    }
    
    

}
