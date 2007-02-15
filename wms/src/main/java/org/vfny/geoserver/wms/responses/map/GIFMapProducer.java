/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map;

import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;
import org.vfny.geoserver.wms.responses.map.gif.Gif89Encoder;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Handles a GetMap request that spects a map in GIF format.
 *
 * @author Didier Richard
 * @version $Id
 */
class GIFMapProducer extends DefaultRasterMapProducer {
    public GIFMapProducer(String format) {
        super(format);
    }

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
            Gif89Encoder gifenc = new Gif89Encoder(image, mapCtx.getBgColor(), 2); // 2= colour reduction pixel sample factor (1=look at all pixels, but its slow) 
            gifenc.setComments("produced by Geoserver");

            gifenc.getFrameAt(0).setInterlaced(false);
            gifenc.encode(outStream);
        } else {
            Gif89Encoder gifenc = new Gif89Encoder(image, null, 2); // 2= colour reduction pixel sample factor (1=look at all pixels, but its slow)
            gifenc.setComments("produced by Geoserver");
            // gifenc.setTransparentIndex(transparent_index);
            gifenc.getFrameAt(0).setInterlaced(false);
            gifenc.encode(outStream);
        }
    }

    public String getContentDisposition() {
        // this can be null
        return null;
    }
}
