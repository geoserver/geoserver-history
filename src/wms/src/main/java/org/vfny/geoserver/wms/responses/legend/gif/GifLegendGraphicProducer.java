/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.legend.gif;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.platform.ServiceException;
import org.geotools.image.ImageWorker;
import org.vfny.geoserver.wms.responses.DefaultRasterLegendProducer;
import org.vfny.geoserver.wms.responses.ImageUtils;


/**
 * Producer of legend graphics in image/gif format.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class GifLegendGraphicProducer extends DefaultRasterLegendProducer {
    /** DOCUMENT ME! */
    static final String MIME_TYPE = "image/gif";

    /**
     * Creates a new producer of legends in gif format.
     */
    public GifLegendGraphicProducer() {
        super();
    }

    /**
     * Encodes on the fly the image generated on {@linkPlain
     * DefaultRasterLegendProducer#produceLegendGraphic(GetLegendGraphicRequest)}
     * to <code>out</code> in "image/gif" format.
     *
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducer#writeTo(java.io.OutputStream)
     */
    public void writeTo(OutputStream out) throws IOException, ServiceException {
        //GR: shall we add a palette parameter to GetLegendGraphic too?
        final BufferedImage legendGraphic = getLegendGraphic();
        RenderedImage forcedIndexed8Bitmask = ImageUtils.forceIndexed8Bitmask(legendGraphic, null);
        ImageWorker imageWorker = new ImageWorker(forcedIndexed8Bitmask);
        imageWorker.writeGIF(out, "LZW", 0.75f);
    }

    /**
     * Returns the "image/gif" mime type since that is the only output format
     * this producer specializes on.
     *
     * @return <code>"image/gif"</code>
     *
     * @throws IllegalStateException if <code>super.getLegendGraphic() ==
     *         null</code>, to respect the workflow.
     *
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducer#getContentType()
     */
    public String getContentType() throws IllegalStateException {
        if (super.getLegendGraphic() == null) {
            throw new IllegalStateException("the image was not still produced");
        }

        return MIME_TYPE;
    }
}
