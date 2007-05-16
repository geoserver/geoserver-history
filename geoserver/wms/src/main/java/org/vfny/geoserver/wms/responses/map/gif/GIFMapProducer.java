/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.gif;

import org.geotools.image.ImageWorker;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.io.OutputStream;


/**
 * Handles a GetMap request that spects a map in GIF format.
 *
 * @author Didier Richard
 * @author Simone Giannechini
 * @version $Id
 */
public final class GIFMapProducer extends DefaultRasterMapProducer {
    /**
     * Since the default palette has not transparency I am here tryin
     * to create a default one with transparency information inside.
     */
    private final static IndexColorModel DEFAULT_PALETTE;

    static {
        // Create a 6x6x6 color cube
        int[] cmap = new int[256];
        int i = 0;

        for (int r = 0; r < 256; r += 51) {
            for (int g = 0; g < 256; g += 51) {
                for (int b = 0; b < 256; b += 51) {
                    cmap[i++] = (r << 16) | (g << 8) | b;
                }
            }
        }

        // And populate the rest of the cmap with gray values
        int grayIncr = 256 / (256 - i);

        // The gray ramp will be between 18 and 252
        int gray = grayIncr * 3;

        for (; i < 255; i++) {
            cmap[i] = (gray << 16) | (gray << 8) | gray;
            gray += grayIncr;
        }

        DEFAULT_PALETTE = new IndexColorModel(8, 256, cmap, 0, true, 255, DataBuffer.TYPE_BYTE);
    }

    public GIFMapProducer(String format, WMS wms) {
        super(format, wms);
    }

    /**
     * Transforms the rendered image into the appropriate format,
     * streaming to the output stream.
     * @param image
     *            The image to be formatted.
     * @param outStream
     *            The stream to write to.cd
     * @throws WmsException
     *             not really.
     * @throws IOException
     *             if encoding to <code>outStream</code> fails.
     */
    public void formatImageOutputStream(BufferedImage image, OutputStream outStream)
        throws WmsException, IOException {
        new ImageWorker(image).forceIndexColorModelForGIF(true).writeGIF(outStream, "LZW", 0.75f);
    }

    public String getContentDisposition() {
        // can be null
        return null;
    }
}
