/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses;

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

import org.geotools.resources.image.ImageUtilities;
import org.vfny.geoserver.ServiceException;

/**
 * Producer of legend graphics in all the formats available through JAI.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
class PNGLegendGraphicProducer extends DefaultRasterLegendProducer {


    /**
     * Creates a new JAI based legend producer for creating
     * <code>outputFormat</code> type images.
     *
     * @param outputFormat DOCUMENT ME!
     */
    PNGLegendGraphicProducer() {
        super();
    }

    /**
     * Encodes the image created by the superclss to the format specified at
     * the constructor and sends it to <code>out</code>.
     *
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducer#writeTo(java.io.OutputStream)
     */
	public void writeTo(OutputStream out) throws IOException, ServiceException {
		final BufferedImage image = super.getLegendGraphic();

		// /////////////////////////////////////////////////////////////////
		//
		// Reformatting this image for png
		//
		// /////////////////////////////////////////////////////////////////
		final MemoryCacheImageOutputStream memOutStream = new MemoryCacheImageOutputStream(
				out);
		final PlanarImage encodedImage = PlanarImage.wrapRenderedImage(image);
		final PlanarImage finalImage = encodedImage.getColorModel() instanceof DirectColorModel ? ImageUtilities
				.reformatColorModel2ComponentColorModel(encodedImage)
				: encodedImage;

		// /////////////////////////////////////////////////////////////////
		//
		// Getting a writer
		//
		// /////////////////////////////////////////////////////////////////
		final Iterator it = ImageIO.getImageWritersByMIMEType("image/png");
		ImageWriter writer = null;
		if (!it.hasNext()) {
			throw new IllegalStateException("No PNG ImageWriter found");
		} else
			writer = (ImageWriter) it.next();

		// /////////////////////////////////////////////////////////////////
		//
		// Compression is available only on native lib
		//
		// /////////////////////////////////////////////////////////////////
		final ImageWriteParam iwp = writer.getDefaultWriteParam();
		if (writer.getClass().getName().equals(
				"com.sun.media.imageioimpl.plugins.png.CLibPNGImageWriter")) {
			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

			iwp.setCompressionQuality(0.75f);// we can control quality here
		}

		writer.setOutput(memOutStream);
		writer.write(null, new IIOImage(finalImage, null, null), iwp);
		memOutStream.flush();
		memOutStream.close();
		writer.dispose();
    }

    /**
     * Returns the MIME type in which the legend graphic will be encoded.
     *
     * @see org.vfny.geoserver.wms.responses.GetLegendGraphicProducer#getContentType()
     */
    public String getContentType() throws IllegalStateException {
        return "image/png";
    }
}
