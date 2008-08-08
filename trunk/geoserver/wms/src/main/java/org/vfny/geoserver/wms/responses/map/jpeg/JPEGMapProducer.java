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
import javax.media.jai.operator.TranslateDescriptor;


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
        palette = null;
        return super.prepareImage(width, height, palette, transparent);
    }
    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(JPEGMapProducer.class.toString());

    /** JPEG Native Acceleration Mode * */
    private Boolean JPEGNativeAcc;

	private boolean hasJAIWriter;

    public JPEGMapProducer(String outputFormat, WMS wms) {
        super(outputFormat, wms);

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
    

    public void formatImageOutputStream(RenderedImage image, OutputStream outStream)
        throws IOException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("About to write a JPEG image.");
        }

        if((JPEGNativeAcc.booleanValue()||!hasJAIWriter)&&(image.getMinX()!=0 || image.getMinY()!=0)) {
        	// the JPEG native writer does a direct access to the writable raster in a way
            // that does not respect minx/miny settings. This in turn results in the issue
            // described at http://jira.codehaus.org/browse/GEOS-2061
            final WritableRaster raster=(WritableRaster) image.getData();
        	final BufferedImage finalImage= new BufferedImage(image.getColorModel(),raster.createWritableTranslatedChild(0, 0),image.getColorModel().isAlphaPremultiplied(),null);
            new ImageWorker(finalImage).writeJPEG(outStream, "JPEG", 0.75f, JPEGNativeAcc.booleanValue());
        } else {
        	new ImageWorker(image).writeJPEG(outStream, "JPEG", 0.75f, JPEGNativeAcc.booleanValue());
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Writing a JPEG done!!!");
        }
    }

    public String getContentDisposition() {
        // can be null
        return null;
    }
}
