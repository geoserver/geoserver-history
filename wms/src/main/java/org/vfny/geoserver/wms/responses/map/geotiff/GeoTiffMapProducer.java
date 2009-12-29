/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.geotiff;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.geoserver.wms.WMS;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.GeneralEnvelope;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;


/**
 * Map producer for GeoTiff output format. It basically relies on the GeoTiff
 * module of geotools.
 *
 *
 * @author Simone Giannecchini, GeoSolutions
 *
 */
public class GeoTiffMapProducer extends DefaultRasterMapProducer {

    /** A logger for this class. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.vfny.geoserver.responses.wms.map.geotiff");

    /** the only MIME type this map producer supports */
    static final String MIME_TYPE = "image/tiff";

    /** GridCoverageFactory. */
    private final static GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);

    private static final String[] OUTPUT_FORMATS = { "image/geotiff", "image/geotiff8" };

    /**
     * Constructo for a {@link GeoTiffMapProducer}.
     *
     * @param wms
     *            that is asking us to encode the image.
     */
    public GeoTiffMapProducer( WMS wms) {
        super(MIME_TYPE, OUTPUT_FORMATS, wms);
    }

    public void formatImageOutputStream(RenderedImage image, OutputStream outStream)
        throws WmsException, IOException {
        // crating a grid coverage
        final GridCoverage2D gc = factory.create("geotiff", image,new GeneralEnvelope(mapContext.getAreaOfInterest()));

        // tiff
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Writing tiff image ...");
        }

        // get the one required by the GetMapRequest
        final String format = getOutputFormat();
        // do we want it to be 8 bits?
        if (format.equalsIgnoreCase("image/geotiff8") || (this.mapContext.getPaletteInverter() != null)) {
            image = forceIndexed8Bitmask(image);
        }

        // writing it out
        final ImageOutputStream imageOutStream = ImageIO.createImageOutputStream(outStream);
		if(imageOutStream==null)
			throw new WmsException("Unable to create ImageOutputStream.");
		
        GeoTiffWriter writer = null;

		// write it out
		try{
			writer = new GeoTiffWriter(imageOutStream);
	        writer.write(gc, null);
		}finally{
			try{
				imageOutStream.close();
			}catch (Throwable e) {
				// eat exception to release resources silently
				if(LOGGER.isLoggable(Level.FINEST))
					LOGGER.log(Level.FINEST,"Unable to properly close output stream",e);
			}
			
			try{
				if(writer!=null)
					writer.dispose();
			}catch (Throwable e) {
				// eat exception to release resources silently
				if(LOGGER.isLoggable(Level.FINEST))
					LOGGER.log(Level.FINEST,"Unable to properly dispose writer",e);
			}
		}

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Writing tiff image done!");
        }
    }

    public String getContentDisposition() {
        // can be null
        return null;
    }
}
