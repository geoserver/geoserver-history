/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.geotiff;

import org.geotools.coverage.FactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.GeneralEnvelope;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;


/**
 * Map producer for GeoTiff output format. It basically relies on the GeoTiff
 * module of geotools.
 *
 * @author Simone Giannecchini, GeoSolutions
 */
public class GeoTiffMapProducer extends DefaultRasterMapProducer {
    /** A logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.map.geotiff");

    /** GridCoverageFactory. */
    private final static GridCoverageFactory factory = FactoryFinder.getGridCoverageFactory(null);

    /**
     * Constructo for a {@link GeoTiffMapProducer}.
     *
     * @param oformat
     *            output format we want the image to be encoded in.
     * @param mime_type
     *            for the requested output format.
     * @param wms
     *            that is asking us to encode the image.
     */
    public GeoTiffMapProducer(String oformat, String mime_type, WMS wms) {
        super(oformat, mime_type, wms);
    }

    public void formatImageOutputStream(RenderedImage image, OutputStream outStream)
        throws WmsException, IOException {
        // crating a grid coverage
        final GridCoverage2D gc = factory.create("geotiff", image,
                new GeneralEnvelope(mapContext.getAreaOfInterest()));

        // tiff
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Writing tiff image ...");
        }

        // do we want it to be 8 bits?
        if (this.format.equalsIgnoreCase("image/tiff8") || (this.mapContext.getPaletteInverter() != null)) {
            image = forceIndexed8Bitmask(image);
        }

        // writing it out
        final ImageOutputStream imageOutStream = ImageIO.createImageOutputStream(outStream);
        final GeoTiffWriter writer = new GeoTiffWriter(imageOutStream);
        writer.write(gc, null);
        imageOutStream.flush();
        imageOutStream.close();

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Writing tiff image done!");
        }
    }

    public String getContentDisposition() {
        // can be null
        return null;
    }
}
