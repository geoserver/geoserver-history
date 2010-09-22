/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetLegendGraphic;
import org.geoserver.wms.GetLegendGraphicRequest;
import org.geoserver.wms.LegendGraphic;
import org.springframework.util.Assert;

/**
 * JAI based output format for the WMS {@link GetLegendGraphic} operation, that could potentially
 * create legend graphics on any format supported by JAI.
 * <p>
 * NOTE: the only actually supported format at the time of writing is {@code image/jpeg}. Many of
 * the MIME types that JAI claims to support do not actually work. This is mostly because of color
 * problems (ie. some formats only support grey scale, and we're giving it a ARGB buffered image).
 * </p>
 * <p>
 * In the event other format than jpeg is proven to work, besides constructing an instance of this
 * class with the appropriate MIME type constructor argument, make sure it's listed as supported in
 * the {@link JAISupport} utility class.
 * </p>
 * 
 * @author Gabriel Roldan
 * @version $Id$
 */
public class JAILegendOutputFormat extends AbstractLegendGraphicOutputFormat {

    /** holds the desired output format MIME type */
    private String outputFormat;

    /**
     * Creates a new JAI based legend producer for creating <code>outputFormat</code> type images.
     * 
     * @param outputFormat
     *            which JAI supported image mime type this output format has to produce (e.g.
     *            {@code "image/jpeg"})
     */
    JAILegendOutputFormat(final String outputFormat) {
        this.outputFormat = outputFormat;
    }

    /**
     * Overrides to force request.isTransparent() to false when the output format is
     * <code>image/jpeg</code>.
     * 
     * @return
     * 
     * @see AbstractLegendGraphicOutputFormat#produceLegendGraphic(GetLegendGraphicRequest)
     */
    @Override
    public BufferedImageLegendGraphic produceLegendGraphic(GetLegendGraphicRequest request)
            throws ServiceException {
        // HACK: should we provide a jpeg specific legend producer just
        // like for GetMap?
        if (outputFormat.startsWith("image/jpeg")) {
            // no transparency in jpeg
            request.setTransparent(false);
        }
        return super.produceLegendGraphic(request);
    }

    /**
     * Encodes the image created by the superclass to the format specified at the constructor and
     * sends it to {@code output}.
     * 
     * @param legend
     *            a {@link BufferedImageLegendGraphic}
     * @param output
     *            destination for the image written by {@link ImageIO} in the
     *            {@link #getContentType() supported format}
     * @see org.GetLegendGraphicOutputFormat.geoserver.wms.GetLegendGraphicProducer#writeTo(java.io.OutputStream)
     */
    public void write(LegendGraphic legend, OutputStream output) throws IOException,
            ServiceException {
        Assert.isInstanceOf(BufferedImageLegendGraphic.class, legend);

        BufferedImage legendImage = ((BufferedImageLegendGraphic) legend).getLegend();
        JAISupport.encode(this.outputFormat, legendImage, output);
    }

    /**
     * Returns the MIME type in which the legend graphic will be encoded.
     * 
     * @see org.GetLegendGraphicOutputFormat.geoserver.wms.GetLegendGraphicProducer#getContentType()
     */
    public String getContentType() throws IllegalStateException {
        return this.outputFormat;
    }

}
