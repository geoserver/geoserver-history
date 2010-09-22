/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.legendgraphic;

import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.media.jai.PlanarImage;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetLegendGraphic;
import org.geoserver.wms.LegendGraphic;
import org.geotools.image.ImageWorker;
import org.springframework.util.Assert;

/**
 * PNG output format for the WMS {@link GetLegendGraphic} operation.
 * 
 * @author Gabriel Roldan
 * @author Justin Deoliveira
 * @version $Id: PNGLegendGraphicProducer.java 4776 2006-07-24 14:43:05Z afabiani $
 */
public class PNGLegendOutputFormat extends AbstractLegendGraphicOutputFormat {
    /**
     * Creates a new JAI based legend producer for creating <code>outputFormat</code> type images.
     * 
     * @param outputFormat
     *            DOCUMENT ME!
     */
    PNGLegendOutputFormat() {
        super();
    }

    /**
     * @param legend
     *            a {@link BufferedImageLegendGraphic}
     * @param output
     *            png image destination
     * @see GetLegendGraphicProducer#writeTo(java.io.OutputStream)
     */
    public void write(LegendGraphic legend, OutputStream output) throws IOException,
            ServiceException {
        Assert.isInstanceOf(BufferedImageLegendGraphic.class, legend);

        BufferedImage image = ((BufferedImageLegendGraphic) legend).getLegend();
        // /////////////////////////////////////////////////////////////////
        //
        // Reformatting this image for png
        //
        // /////////////////////////////////////////////////////////////////
        final MemoryCacheImageOutputStream memOutStream = new MemoryCacheImageOutputStream(output);
        final ImageWorker worker = new ImageWorker(image);
        final PlanarImage finalImage = (image.getColorModel() instanceof DirectColorModel) ? worker
                .forceComponentColorModel().getPlanarImage() : worker.getPlanarImage();

        // /////////////////////////////////////////////////////////////////
        //
        // Getting a writer
        //
        // /////////////////////////////////////////////////////////////////
        final Iterator it = ImageIO.getImageWritersByMIMEType("image/png");
        ImageWriter writer = null;

        if (!it.hasNext()) {
            throw new IllegalStateException("No PNG ImageWriter found");
        } else {
            writer = (ImageWriter) it.next();
        }

        // /////////////////////////////////////////////////////////////////
        //
        // Compression is available only on native lib
        //
        // /////////////////////////////////////////////////////////////////
        final ImageWriteParam iwp = writer.getDefaultWriteParam();

        if (writer.getClass().getName()
                .equals("com.sun.media.imageioimpl.plugins.png.CLibPNGImageWriter")) {
            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

            iwp.setCompressionQuality(0.75f); // we can control quality here
        }

        writer.setOutput(memOutStream);
        writer.write(null, new IIOImage(finalImage, null, null), iwp);
        memOutStream.flush();
        memOutStream.close();
        writer.dispose();
    }

    /**
     * @return {@code "image/png"}
     * @see org.geoserver.wms.GetLegendGraphicOutputFormat#getContentType()
     */
    public String getContentType() throws IllegalStateException {
        return "image/png";
    }

}
