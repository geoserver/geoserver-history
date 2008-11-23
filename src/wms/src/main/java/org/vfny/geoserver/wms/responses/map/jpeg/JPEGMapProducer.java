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
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.InterpolationNearest;
import javax.media.jai.PlanarImage;
import javax.media.jai.operator.TranslateDescriptor;

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

    private boolean hasJAIWriter;

    public JPEGMapProducer(WMS wms) {
        super(MIME_TYPE, wms);

        /**
         * TODO To check Native Acceleration mode use the following variable
         */
        this.JPEGNativeAcc = wms.getGeoServer().getJPEGNativeAcceleration();
        try{
        	Class.forName("com.sun.media.imageioimpl.plugins.jpeg.CLibJPEGImageWriter") ;
        	hasJAIWriter=true;
        }catch (ClassNotFoundException e) {
        	hasJAIWriter=false;
		}
    }

    @Override
    protected RenderedImage prepareImage(int width, int height, IndexColorModel palette, boolean transparent) {
        //there is no transparency in JPEG anyway :-)
        transparent = false;
        palette = null;
        return super.prepareImage(width, height, palette, transparent);
    }

    public void formatImageOutputStream(RenderedImage image, OutputStream outStream)
        throws IOException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("About to write a JPEG image.");
        }

        final WritableRaster raster=(WritableRaster) image.getData();
        if(image instanceof BufferedImage){
            BufferedImage im = (BufferedImage) image;
            im.flush();
            im=null;
        }
        else
            PlanarImage.wrapRenderedImage(image).dispose();
        final BufferedImage finalImage = new BufferedImage(image.getColorModel(),raster.createWritableTranslatedChild(0, 0),image.getColorModel().isAlphaPremultiplied(),null);
        new ImageWorker(finalImage).writeJPEG(outStream, "JPEG", 0.75f, JPEGNativeAcc.booleanValue());

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Writing a JPEG done!!!");
        }
    }

    public String getContentDisposition() {
        // can be null
        return null;
    }
}

