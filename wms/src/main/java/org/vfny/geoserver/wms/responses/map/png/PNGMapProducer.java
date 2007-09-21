/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.png;

import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.image.ImageWorker;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;


/**
 * Handles a GetMap request that spects a map in GIF format.
 *
 * @author Simone Giannecchini
 * @author Didier Richard
 * @version $Id
 */
public class PNGMapProducer extends DefaultRasterMapProducer {
    /** Logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.wms.responses.map.png");

    /** PNG Native Acceleration Mode * */
	protected Boolean PNGNativeAcc;

    public PNGMapProducer(String format, String mime_type, WMS wms) {
        super(format, mime_type, wms);
        this.PNGNativeAcc = wms.getGeoServer().getPNGNativeAcceleration();
    }

    /**
     * Transforms the rendered image into the appropriate format, streaming to
     * the output stream.
     * @param image
     *            The image to be formatted.
     * @param outStream
     *            The stream to write to.
     *
     * @throws WmsException
     *             not really.
     * @throws IOException
     *             if encoding to <code>outStream</code> fails.
     */
    public void formatImageOutputStream(RenderedImage image, OutputStream outStream)
        throws WmsException, IOException {
        // /////////////////////////////////////////////////////////////////
        //
        // Reformatting this image for png
        //
        // /////////////////////////////////////////////////////////////////
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Writing png image ...");
        }

        if (this.format.equalsIgnoreCase("image/png8") || (this.mapContext.getPaletteInverter() != null)) {
            image = forceIndexed8Bitmask(image);
        }

        new ImageWorker(image).writePNG(outStream, "FILTERED", 0.5f, PNGNativeAcc.booleanValue(),
            image.getColorModel() instanceof IndexColorModel);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Writing png image ... done!");
        }
    }

    public String getContentDisposition() {
        // can be null
        return null;
    }
}
