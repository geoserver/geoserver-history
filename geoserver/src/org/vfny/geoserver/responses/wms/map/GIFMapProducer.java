/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.map;

import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.responses.wms.DefaultRasterMapProducer;
import org.vfny.geoserver.responses.wms.WMSMapContext;
import org.vfny.geoserver.responses.wms.map.gif.GIFOutputStream;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;


/**
 * Handles a GetMap request that spects a map in GIF format.
 *
 * @author Didier Richard
 * @version $Id
 */
class GIFMapProducer extends DefaultRasterMapProducer {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.map");

    /**
     * Transforms the rendered image into the appropriate format, streaming to
     * the output stream.
     *
     * @param format The name of the format
     * @param image The image to be formatted.
     * @param outStream The stream to write to.
     *
     * @throws WmsException
     * @throws IOException DOCUMENT ME!
     */
    protected void formatImageOutputStream(String format, BufferedImage image,
        OutputStream outStream) throws WmsException, IOException {
        LOGGER.fine("image/gif");

        WMSMapContext mapCtx = getMapContext();

        if (mapCtx.isTransparent()) {
            try {
                GIFOutputStream.writeGIF(outStream, image,
                    GIFOutputStream.STANDARD_256_COLORS, mapCtx.getBgColor());
            } catch (Exception e) {
                LOGGER.warning(e.toString());
            }
        } else {
            try {
                GIFOutputStream.writeGIF(outStream, image);
            } catch (Exception e) {
                LOGGER.warning(e.toString());
            }
        }
    }
}
