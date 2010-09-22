/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.gif;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.wms.WMS;
import org.geotools.image.ImageWorker;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;


/**
 * Handles a GetMap request that spects a map in GIF format.
 *
 * @author Didier Richard
 * @author Simone Giannecchini - GeoSolutions
 * @version $Id
 */
public final class GIFMapProducer extends DefaultRasterMapProducer {
   
    /** the only MIME type this map producer supports */
    static final String MIME_TYPE = "image/gif";

    public GIFMapProducer(WMS wms) {
        super(MIME_TYPE, wms);
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
    public void formatImageOutputStream(RenderedImage originalImage, OutputStream outStream)
        throws WmsException, IOException {
        // /////////////////////////////////////////////////////////////////
        //
        // Now the magic
        //
        // /////////////////////////////////////////////////////////////////
        new ImageWorker(super.forceIndexed8Bitmask(originalImage)).writeGIF(outStream, "LZW", 0.75f);
    }

    public String getContentDisposition() {
        // can be null
        return null;
    }


}
