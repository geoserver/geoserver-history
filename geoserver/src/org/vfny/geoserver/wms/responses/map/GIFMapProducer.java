/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map;

import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;
import org.vfny.geoserver.wms.responses.WMSMapContext;
import org.vfny.geoserver.wms.responses.map.gif.GIFOutputStream;

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
    /**
     * Transforms the rendered image into the appropriate format, streaming to
     * the output stream.
     *
     * @param format The name of the format
     * @param image The image to be formatted.
     * @param outStream The stream to write to.
     *
     * @throws WmsException not really.
     * @throws IOException if encoding to <code>outStream</code> fails.
     */
    protected void formatImageOutputStream(String format, BufferedImage image,
        OutputStream outStream) throws WmsException, IOException {

    	WMSMapContext mapCtx = getMapContext();

        if (mapCtx.isTransparent()) {
            GIFOutputStream.writeGIF(outStream, image,
                GIFOutputStream.STANDARD_256_COLORS, mapCtx.getBgColor());
        } else {
            GIFOutputStream.writeGIF(outStream, image);
        }
    }
}
