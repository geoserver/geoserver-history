/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.svg;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;

import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetMapOutputFormat;
import org.geoserver.wms.WMS;
import org.geoserver.wms.WMSMapContext;

/**
 * Handles a GetMap request that expects a map in SVG format.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 */
public final class SVGStreamingMapOutputFormat extends Response implements GetMapOutputFormat {

    private WMS wms;

    public SVGStreamingMapOutputFormat(WMS wms) {
        super(EncodeSVG.class, SVG.OUTPUT_FORMATS);
        this.wms = wms;
    }

    /**
     * @see org.geoserver.ows.Response#getMimeType(java.lang.Object,
     *      org.geoserver.platform.Operation)
     */
    @Override
    public String getMimeType(Object value, Operation operation) throws ServiceException {
        return getMimeType();
    }

    /**
     * @see org.geoserver.ows.Response#write(java.lang.Object, java.io.OutputStream,
     *      org.geoserver.platform.Operation)
     */
    @Override
    public void write(Object value, OutputStream output, Operation operation) throws IOException,
            ServiceException {
        EncodeSVG map = (EncodeSVG) value;
        map.encode(output);
    }

    /**
     * @return {@code true} if the WMS is configured for the {@link WMS#SVG_SIMPLE streaming} svg
     *         strategy
     * @see org.geoserver.wms.GetMapOutputFormat#enabled()
     */
    public boolean enabled() {
        boolean enabled = SVG.canHandle(wms, WMS.SVG_SIMPLE);
        return enabled;
    }
    
    /**
     * @return {@code true} if the WMS is configured for the {@link WMS#SVG_SIMPLE streaming} svg
     *         strategy
     * @see org.geoserver.ows.Response#canHandle(org.geoserver.platform.Operation)
     */
    public boolean canHandle(Operation operation) {
        return enabled();
    }

    /**
     * @return {@code ["image/svg+xml", "image/svg xml", "image/svg"]}
     * @see org.geoserver.wms.GetMapOutputFormat#getOutputFormatNames()
     */
    public Set<String> getOutputFormatNames() {
        return SVG.OUTPUT_FORMATS;
    }

    /**
     * @return {@code "image/svg+xml"}
     * @see org.geoserver.wms.GetMapOutputFormat#getMimeType()
     */
    public String getMimeType() {
        return SVG.MIME_TYPE;
    }

    /**
     * 
     * @see org.geoserver.wms.GetMapOutputFormat#produceMap(org.geoserver.wms.WMSMapContext)
     */
    public EncodeSVG produceMap(WMSMapContext mapContext) throws ServiceException, IOException {
        EncodeSVG svg = new EncodeSVG(mapContext);
        svg.setMimeType(getMimeType());
        return svg;
    }

}
