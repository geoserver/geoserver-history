/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.jpeg;

import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.wms.WMS;
import org.geotools.image.ImageWorker;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;

import com.sun.media.imageioimpl.common.PackageUtil;

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
    private boolean JPEGNativeAcc;


    public JPEGMapProducer(WMS wms) {
        super(MIME_TYPE, wms);
        this.JPEGNativeAcc = wms.getJPEGNativeAcceleration()&&PackageUtil.isCodecLibAvailable();
        
        
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
        
        float quality = (100 - wms.getJpegCompression()) / 100.0f;
        new ImageWorker(image).writeJPEG(outStream, "JPEG", quality, JPEGNativeAcc);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Writing a JPEG done!!!");
        }
    }

    public String getContentDisposition() {
        // can be null
        return null;
    }
}
