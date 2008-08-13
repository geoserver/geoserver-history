/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.svg;

import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.platform.ServiceException;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;

/**
 * Intermediate SVG map producer that instantiates and completely delegates to
 * the appropriate one depending on the {@link WMS#getSvgRenderer()} setting.
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @see SVGBatikMapProducer
 * @see SVGMapProducer
 */
public class SvgMapProducerProxy implements GetMapProducer {

    static final String MIME_TYPE = "image/svg+xml";

    /**
     * The actual SVG map producer to use depending on the
     * {@link WMS#getSvgRenderer()} setting
     */
    private final GetMapProducer svgProducer;

    /**
     * @param formatName
     *            the format name as advertised in the capabilities, allows for
     *            easy
     * 
     */
    public SvgMapProducerProxy(final String formatName, WMS wms) {
        final String svgRendererTypeSetting = wms.getSvgRenderer();
        if (WMSConfig.SVG_SIMPLE.equals(svgRendererTypeSetting)) {
            svgProducer = new SVGMapProducer(formatName, MIME_TYPE);
        } else if (WMSConfig.SVG_BATIK.equals(svgRendererTypeSetting)) {
            svgProducer = new SVGBatikMapProducer(formatName, MIME_TYPE, wms);
        } else {
            //no setting, do the default
            svgProducer = new SVGMapProducer(formatName, MIME_TYPE);
        }
    }

    public void abort() {
        svgProducer.abort();
    }

    public String getContentDisposition() {
        return svgProducer.getContentDisposition();
    }

    public String getContentType() throws IllegalStateException {
        return svgProducer.getContentType();
    }

    public WMSMapContext getMapContext() {
        return svgProducer.getMapContext();
    }

    public String getOutputFormat() {
        return svgProducer.getOutputFormat();
    }

    public void produceMap() throws WmsException {
        svgProducer.produceMap();
    }

    public void setContentType(String mime) {
        svgProducer.setContentType(mime);
    }

    public void setMapContext(WMSMapContext mapContext) {
        svgProducer.setMapContext(mapContext);
    }

    public void setOutputFormat(String format) {
        svgProducer.setOutputFormat(format);
    }

    public void writeTo(OutputStream out) throws ServiceException, IOException {
        svgProducer.writeTo(out);
    }

}
