/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.png;

import org.geotools.image.ImageWorker;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.WmsException;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;


/**
 * Handles a GetMap request that spects a map in PNG8 format. A simple extension over the PNG format.
 *
 * @author Andrea Aime
 * @author Didier Richard
 * @version $Id
 */
public final class PNG8MapProducer extends PNGMapProducer {
    /** Logger */
    private static final Logger LOGGER = Logger.getLogger(PNG8MapProducer.class.getPackage()
                                                                               .getName());

    public PNG8MapProducer(String format, WMS wms) {
        super(format, wms);
    }

    /**
     * Transforms the rendered image into the appropriate format, streaming to
     * the output stream.
     *
     * @param format
     *            The name of the format
     * @param image
     *            The image to be formatted.
     * @param outStream
     *            The stream to write to.
     * @throws WmsException
     *             not really.
     * @throws IOException
     *             if encoding to <code>outStream</code> fails.
     */
    public void formatImageOutputStream(String format, BufferedImage image, OutputStream outStream)
        throws WmsException, IOException {
        if (!format.equalsIgnoreCase(PNG8MapProducerFactory.FORMAT)) {
            throw new IllegalArgumentException("The provided format " + format
                + " is not the same as expected: " + PNG8MapProducerFactory.FORMAT);
        }

        // /////////////////////////////////////////////////////////////////
        //
        // Reformatting this image for png
        //
        // /////////////////////////////////////////////////////////////////
        //        new ImageWorker(image).forceIndexColorModelForGIF(true).writePNG(outStream, "FILTERED", 0.25f, PNGNativeAcc.booleanValue(),
        //            true);
        RenderedImage ri = new ImageWorker(image).forceIndexColorModelForGIF(false)
                                                 .getRenderedImage();
        ImageIO.write(ri, "png", outStream);
    }

    private static IndexColorModel model = null;

    protected BufferedImage prepareImage(int width, int height) {
        try {
            if (model == null) {
                BufferedImage bi = ImageIO.read(new File("c:\\temp\\pngvspng8\\states8gimp.png"));
                model = (IndexColorModel) bi.getColorModel();
            }
        } catch (Exception e) {
            e.printStackTrace();

            //            return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        }

        return new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED, model);

        //        return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }

    public String getContentDisposition() {
        // can be null
        return null;
    }

    public String getContentType() throws java.lang.IllegalStateException {
        return PNG8MapProducerFactory.MIME_TYPE;
    }

    public static void main(String[] args) {
        final Iterator it = ImageIO.getImageWritersByFormatName("PNG");
        ImageWriter writer = null;

        if (!it.hasNext()) {
            throw new IllegalStateException("No PNG ImageWriter found");
        } else {
            writer = (ImageWriter) it.next();
        }

        // /////////////////////////////////////////////////////////////////
        //
        // getting a stream
        //
        // /////////////////////////////////////////////////////////////////
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Setting write parameters for this writer");
        }

        ImageWriteParam iwp = null;
        iwp = writer.getDefaultWriteParam();
        System.out.println(Arrays.asList(iwp.getCompressionTypes()));
    }
}
