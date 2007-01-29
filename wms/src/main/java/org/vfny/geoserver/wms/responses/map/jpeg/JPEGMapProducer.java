/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.jpeg;

import org.geotools.image.ImageWorker;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.media.jai.PlanarImage;


/**
 * Map producer for JPEG image format.
 *
 * @author Simone Giannecchini
 * @since 1.4.x
 *
 */
public final class JPEGMapProducer extends DefaultRasterMapProducer {
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

    protected void formatImageOutputStream(String format, BufferedImage image,
        OutputStream outStream) throws IOException {
        if (!format.equalsIgnoreCase(JPEGMapProducerFactory.MIME_TYPE)) {
            throw new IllegalArgumentException(new StringBuffer("The provided format ").append(
                    format).append(" is not the same as expected: ")
                                                                                       .append(JPEGMapProducerFactory.MIME_TYPE)
                                                                                       .toString());
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("About to write a JPEG image.");
        }

        new ImageWorker(image).writeJPEG(outStream, "JPEG", 0.75f, JPEGNativeAcc.booleanValue());

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Writing a JPEG done!!!");
        }
    }

    protected BufferedImage prepareImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }

    public String getContentDisposition() {
        // can be null
        return null;
    }
}
