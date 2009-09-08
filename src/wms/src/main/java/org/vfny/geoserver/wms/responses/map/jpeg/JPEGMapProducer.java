/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.jpeg;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.operator.TranslateDescriptor;

import org.apache.tools.ant.taskdefs.optional.i18n.Translate;
import org.geoserver.wms.WMS;
import org.geotools.image.ImageWorker;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;

/**
 * Map producer for JPEG image format.
 *
 * @author Simone Giannecchini
 * @since 1.4.x
 *
 */
public final class JPEGMapProducer extends DefaultRasterMapProducer {
    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(JPEGMapProducer.class.toString());

    /** the only MIME type this map producer supports */
    static final String MIME_TYPE = "image/jpeg";

    /** JPEG Native Acceleration Mode * */
    private Boolean JPEGNativeAcc;


    public JPEGMapProducer(WMS wms) {
        super(MIME_TYPE, wms);

        /**
         * TODO To check Native Acceleration mode use the following variable
         */
        this.JPEGNativeAcc = wms.getJPEGNativeAcceleration();
        
        
    }

    @Override
    protected RenderedImage prepareImage(int width, int height, IndexColorModel palette, boolean transparent) {
        // there is no transparency in JPEG anyway :-)
        transparent = false;
        palette = null;
        return super.prepareImage(width, height, palette, transparent);
    }
    
    @Override
    protected long getDrawingSurfaceMemoryUse(int width, int height, IndexColorModel palette,
            boolean transparent) {
        // there is no transparency in JPEG anyway :-)
        transparent = false;
        palette = null;
        return super.getDrawingSurfaceMemoryUse(width, height, palette, transparent);
    }

    public void formatImageOutputStream(RenderedImage image, OutputStream outStream)
        throws IOException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("About to write a JPEG image.");
        }
        if (! JPEGNativeAcc.booleanValue()&&image.getMinX()!=0 || image.getMinY()!=0) {
        	// I don't make an expliciti copy anymore, hoping that sooner or later:
        	// A> this bug  will be fixed
        	// B> JDK JpegImageWriter will stop making a straight copy of the input raster
        	image= TranslateDescriptor.create(image, new Float(-image.getMinX()), new Float(-image.getMinY()), new InterpolationNearest(), null);
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
