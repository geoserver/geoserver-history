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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.media.jai.PlanarImage;

import org.geotools.image.ImageWorker;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;

/**
 * Handles a GetMap request that spects a map in GIF format.
 * 
 * @author Didier Richard
 * @version $Id
 */
public final class PNGMapProducer extends DefaultRasterMapProducer {
	/** DOCUMENT ME! */
	private static final Logger LOGGER = Logger.getLogger(PNGMapProducer.class
			.getPackage().getName());

	/** PNG Native Acceleration Mode **/
	private Boolean PNGNativeAcc;

	public PNGMapProducer(String format, WMS wms) {
		super(format, wms);
		/**
		 * TODO
		 * 	To check Native Acceleration mode use the following variable
		 */
		this.PNGNativeAcc = wms.getGeoServer().getPNGNativeAcceleration();
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
	public void formatImageOutputStream(String format, BufferedImage image,
			OutputStream outStream) throws WmsException, IOException {
		if (!format.equalsIgnoreCase(PNGMapProducerFactory.MIME_TYPE))
			throw new IllegalArgumentException("The provided format " + format
					+ " is not the same as expected: "
					+ PNGMapProducerFactory.MIME_TYPE);

		// /////////////////////////////////////////////////////////////////
		//
		// Reformatting this image for png
		//
		// /////////////////////////////////////////////////////////////////
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("Preparing for writing for png image");
		final PlanarImage encodedImage = PlanarImage.wrapRenderedImage(image);
		final PlanarImage finalImage = encodedImage.getColorModel() instanceof DirectColorModel ? new ImageWorker(
				encodedImage).forceComponentColorModel().getPlanarImage()
				: encodedImage;
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("Encoded input image for png writer");
		// /////////////////////////////////////////////////////////////////
		//
		// Getting a writer
		//
		// /////////////////////////////////////////////////////////////////
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("Getting a writer");
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
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("Setting write parameters for this writer");
		final ImageWriteParam iwp = writer.getDefaultWriteParam();
		final MemoryCacheImageOutputStream memOutStream = new MemoryCacheImageOutputStream(
				outStream);
		if (writer.getClass().getName().equals(
				"com.sun.media.imageioimpl.plugins.png.CLibPNGImageWriter")) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("Writer is native");
			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwp.setCompressionType("HUFFMAN_ONLY");//best speed
			iwp.setCompressionQuality(8.0F/9.0F);// we can control quality here

		}
		else
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("Writer is NOT native");
		
		
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("About to write png image");
		writer.setOutput(memOutStream);
		writer.write(null, new IIOImage(finalImage, null, null), iwp);

		memOutStream.flush();
		writer.dispose();
		memOutStream.close();
		
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("Writing png image done!");

	}
	protected BufferedImage prepareImage(int width, int height) {
		return new BufferedImage(width, height,
				BufferedImage.TYPE_4BYTE_ABGR);
		
	}
}
