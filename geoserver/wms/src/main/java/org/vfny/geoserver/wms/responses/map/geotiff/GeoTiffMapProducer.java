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
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;


/**
 * Map producer for GeoTiff output format. It basically relies on the
 * GeoTiff module of geotools.
 *
 * @author Simone Giannecchini
 */
public class GeoTiffMapProducer extends DefaultRasterMapProducer {
    /** the only MIME type this map producer supports */
    static final String MIME_TYPE = "image/geotiff";

    /**
     * convenient singleton Set to expose the output format this
     * producer supports
     */
    private static final Set SUPPORTED_FORMATS = Collections.singleton(MIME_TYPE);

    /** GridCoverageFactory. */
    private final static GridCoverageFactory factory = FactoryFinder.getGridCoverageFactory(null);

    public GeoTiffMapProducer(String mime, WMS wms) {
        super(mime, wms);
    }

    public void formatImageOutputStream(BufferedImage image, OutputStream outStream)
        throws WmsException, IOException {
        // getting the context
        final WMSMapContext context = this.getMapContext();

        // crating a grid coverage
        final GridCoverage2D gc = factory.create("geotiff", image,
                new GeneralEnvelope(context.getAreaOfInterest()));

        // writing it out
        final ImageOutputStream imageOutStream = new MemoryCacheImageOutputStream(outStream);
        final GeoTiffWriter writer = new GeoTiffWriter(imageOutStream);
        writer.write(gc, null);
        imageOutStream.flush();
        imageOutStream.close();
    }

    public String getContentDisposition() {
        // can be null
        return null;
    }
}
