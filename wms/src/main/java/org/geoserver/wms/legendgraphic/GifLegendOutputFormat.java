/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.response.LegendGraphic;
import org.geotools.image.ImageWorker;
import org.springframework.util.Assert;
import org.vfny.geoserver.wms.responses.ImageUtils;

/**
 * Producer of legend graphics in image/gif format.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 */
public class GifLegendOutputFormat extends AbstractLegendGraphicOutputFormat {

    static final String MIME_TYPE = "image/gif";

    /**
     * @param legend
     *            a {@link BufferedImageLegendGraphic}
     * @param output
     *            gif image destination
     * @see GetLegendGraphicProducer#writeTo(java.io.OutputStream)
     */
    public void write(LegendGraphic legend, OutputStream output) throws IOException,
            ServiceException {

        Assert.isInstanceOf(BufferedImageLegendGraphic.class, legend);
        BufferedImage legendGraphic = ((BufferedImageLegendGraphic) legend).getLegend();

        RenderedImage forcedIndexed8Bitmask = ImageUtils.forceIndexed8Bitmask(legendGraphic, null);
        ImageWorker imageWorker = new ImageWorker(forcedIndexed8Bitmask);
        imageWorker.writeGIF(output, "LZW", 0.75f);
    }

    /**
     * @return {@code "image/gif"}
     * @see GetLegendGraphicProducer#getContentType()
     */
    public String getContentType() {
        return MIME_TYPE;
    }

}
