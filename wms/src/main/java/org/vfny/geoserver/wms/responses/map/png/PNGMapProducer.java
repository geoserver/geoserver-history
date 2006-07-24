/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.png;

import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.media.jai.PlanarImage;

import org.geotools.resources.image.ImageUtilities;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;

/**
 * Handles a GetMap request that spects a map in GIF format.
 * 
 * @author Didier Richard
 * @version $Id
 */
public class PNGMapProducer extends DefaultRasterMapProducer {
	/** DOCUMENT ME! */
	private static final Logger LOGGER = Logger.getLogger(PNGMapProducer.class
			.getPackage().getName());

	public PNGMapProducer(String format) {
		super(format);
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
	 * 
	 * @throws WmsException
	 *             not really.
	 * @throws IOException
	 *             if encoding to <code>outStream</code> fails.
	 */
	public void formatImageOutputStream(String format, BufferedImage image,
			OutputStream outStream) throws WmsException, IOException {

		// /////////////////////////////////////////////////////////////////
		//
		// Reformatting this image for png
		//
		// /////////////////////////////////////////////////////////////////
		final MemoryCacheImageOutputStream memOutStream = new MemoryCacheImageOutputStream(
				outStream);
		final PlanarImage encodedImage = PlanarImage.wrapRenderedImage(image);
		final PlanarImage finalImage = encodedImage.getColorModel() instanceof DirectColorModel ? ImageUtilities
				.reformatColorModel2ComponentColorModel(encodedImage)
				: encodedImage;

		// /////////////////////////////////////////////////////////////////
		//
		// Getting a writer
		//
		// /////////////////////////////////////////////////////////////////
		final Iterator it = ImageIO.getImageWritersByMIMEType(format);
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

}
