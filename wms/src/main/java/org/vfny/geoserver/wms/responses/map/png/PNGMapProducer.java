/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.png;

import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.media.jai.PlanarImage;

import org.geotools.image.ImageWorker;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;

import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.media.jai.codecimpl.PNGImageEncoder;

/**
 * Handles a GetMap request that spects a map in GIF format.
 * 
 * @author Simone Giannecchini
 * @author Didier Richard
 * @version $Id
 */
public final class PNGMapProducer extends DefaultRasterMapProducer {
	/** Logger */
	private static final Logger LOGGER = Logger.getLogger(PNGMapProducer.class
			.getPackage().getName());

	/**
	 * Workaround class for compressing PNG using the default
	 * {@link PNGImageEncoder} shipped with the JDK.
	 * 
	 * <p>
	 * {@link PNGImageWriter} does not support
	 * {@link ImageWriteParam#setCompressionMode(int)} set to
	 * {@link ImageWriteParam#MODE_EXPLICIT}, it only allows
	 * {@link ImageWriteParam#MODE_DEFAULT}.
	 * 
	 * <p>
	 * 
	 * 
	 * @author Simone Giannecchini.
	 * 
	 */
	public final static class PNGImageWriteParam extends ImageWriteParam {

		/**
		 * Default construnctor.
		 * 
		 * @param local
		 */
		public PNGImageWriteParam() {
			super();
			this.canWriteProgressive = true;
			this.canWriteCompressed = true;
			this.locale = Locale.getDefault();
		}
	}

	/** PNG Native Acceleration Mode * */
	private Boolean PNGNativeAcc;

	public PNGMapProducer(String format, WMS wms) {
		super(format, wms);
		/**
		 * TODO To check Native Acceleration mode use the following variable
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
		final PlanarImage finalImage = !(encodedImage.getColorModel() instanceof ComponentColorModel) ? new ImageWorker(
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
		// getting a stream
		//
		// /////////////////////////////////////////////////////////////////
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("Setting write parameters for this writer");
		ImageWriteParam iwp = null;
		final MemoryCacheImageOutputStream memOutStream = new MemoryCacheImageOutputStream(
				outStream);
		if (writer.getClass().getName().equals(
		// /////////////////////////////////////////////////////////////////
				//
				// compressing with native
				//
				// /////////////////////////////////////////////////////////////////
				"com.sun.media.imageioimpl.plugins.png.CLibPNGImageWriter")
				&& this.PNGNativeAcc.booleanValue()) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("Writer is native");
			iwp = writer.getDefaultWriteParam();
			// Define compression mode
			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			// best compression
			iwp.setCompressionType("FILTERED");
			// we can control quality here
			iwp.setCompressionQuality(0);
			// destination image type
			iwp.setDestinationType(new ImageTypeSpecifier(finalImage
					.getColorModel(), finalImage.getSampleModel()));

		} else {
			// /////////////////////////////////////////////////////////////////
			//
			// compressing with pure java
			//
			// /////////////////////////////////////////////////////////////////
			// //
			// pure java from native
			// //
			if (writer.getClass().getName().equals(
					"com.sun.media.imageioimpl.plugins.png.CLibPNGImageWriter")
					&& !PNGNativeAcc.booleanValue())
				writer = (ImageWriter) it.next();
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("Writer is NOT native");
			// instantiating PNGImageWriteParam
			iwp = new PNGImageWriteParam();
			// Define compression mode
			iwp.setCompressionMode(ImageWriteParam.MODE_DEFAULT);
		}

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
		return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

	}
}
