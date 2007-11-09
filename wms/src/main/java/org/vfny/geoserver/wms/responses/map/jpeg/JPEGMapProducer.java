/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.jpeg;

import org.geotools.image.ImageWorker;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Map producer for JPEG image format.
 *
 * @author Simone Giannecchini
 * @since 1.4.x
 *
 */
public final class JPEGMapProducer extends DefaultRasterMapProducer {
    protected RenderedImage prepareImage(int width, int height, IndexColorModel palette, boolean transparent) {
        //there is no transparency in JPEG anyway :-)
        transparent = false;
        return super.prepareImage(width, height, palette, transparent);
    }
    /** Logger. */
    private final static Logger LOGGER = Logger.getLogger(JPEGMapProducer.class.toString());

    /** JPEG Native Acceleration Mode * */
    private Boolean JPEGNativeAcc;

    public JPEGMapProducer(String outputFormat, WMS wms) {
        super(outputFormat, wms);
        /**
         * TODO To check Native Acceleration mode use the following variable
         */
        this.JPEGNativeAcc = wms.getGeoServer().getJPEGNativeAcceleration();
    }

    public void formatImageOutputStream(RenderedImage image, OutputStream outStream)
        throws IOException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("About to write a JPEG image.");
        }

        new ImageWorker(image).writeJPEG(outStream, "JPEG", 0.75f, JPEGNativeAcc.booleanValue());

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Writing a JPEG done!!!");
        }
    }

    public String getContentDisposition() {
        // can be null
        return null;
    }
}
