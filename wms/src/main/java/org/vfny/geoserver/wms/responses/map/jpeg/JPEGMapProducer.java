package org.vfny.geoserver.wms.responses.map.jpeg;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
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
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;

/**
 * Map producer for JPEG image format.
 * 
 * @author Simone Giannecchini
 * @since 1.4.x
 * 
 */
public final class JPEGMapProducer extends DefaultRasterMapProducer {
	/** Logger. */
	private final static Logger LOGGER = Logger.getLogger(JPEGMapProducer.class
			.toString());

	/** JPEG Native Acceleration Mode * */
	private Boolean JPEGNativeAcc;

	public JPEGMapProducer(String outputFormat, WMS wms) {
		super(outputFormat, wms);
		/**
		 * TODO To check Native Acceleration mode use the following variable
		 */
		this.JPEGNativeAcc = wms.getGeoServer().getJPEGNativeAcceleration();
	}

	protected void formatImageOutputStream(String format, BufferedImage image,
			OutputStream outStream) throws IOException {
		if (!format.equalsIgnoreCase(JPEGMapProducerFactory.MIME_TYPE))
			throw new IllegalArgumentException(new StringBuffer(
					"The provided format ").append(format).append(
					" is not the same as expected: ").append(
					JPEGMapProducerFactory.MIME_TYPE).toString());

		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("About to write a JPEG image.");
		// /////////////////////////////////////////////////////////////////
		//
		// Reformatting this image for png
		//
		// /////////////////////////////////////////////////////////////////
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("Encoding input image to write out as JPEG.");
		final ColorModel cm = image.getColorModel();
		final boolean indexColorModel = image.getColorModel() instanceof IndexColorModel;
		final int numBands = image.getSampleModel().getNumBands();
		final boolean hasAlpha = cm.hasAlpha();
		PlanarImage encodedImage = PlanarImage.wrapRenderedImage(image);
		if (indexColorModel || hasAlpha) {
			final ImageWorker worker = new ImageWorker();
			worker.setImage(image);
			if (indexColorModel)
				worker.forceComponentColorModel();
			if (hasAlpha)
				worker.retainBands(numBands - 1);
			encodedImage = worker.getPlanarImage();

		}

		// /////////////////////////////////////////////////////////////////
		//
		// Getting a writer
		//
		// /////////////////////////////////////////////////////////////////
		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("Getting a JPEG writer and configurint it.");
		final Iterator it = ImageIO.getImageWritersByMIMEType(format);
		ImageWriter writer = null;
		if (!it.hasNext())
			throw new IllegalStateException("No JPEG ImageWriter found");
		writer = (ImageWriter) it.next();
		if (writer.getClass().getName().equals(
				"com.sun.media.imageioimpl.plugins.jpeg.CLibJPEGImageWriter")
				&& !this.JPEGNativeAcc)
			writer = (ImageWriter) it.next();
		// /////////////////////////////////////////////////////////////////
		//
		// Compression is available on both lib
		//
		// /////////////////////////////////////////////////////////////////
		final ImageWriteParam iwp = writer.getDefaultWriteParam();
		final MemoryCacheImageOutputStream memOutStream = new MemoryCacheImageOutputStream(
				outStream);
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		// lossy compression
		iwp.setCompressionType("JPEG");
		iwp.setCompressionQuality(0.6f);// we can control quality here
		writer.setOutput(memOutStream);

		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("Writing out...");
		writer.write(null, new IIOImage(encodedImage, null, null), iwp);

		memOutStream.flush();
		writer.dispose();
		memOutStream.close();

		if (LOGGER.isLoggable(Level.FINE))
			LOGGER.fine("Writing a JPEG done!!!");
	}

	protected BufferedImage prepareImage(int width, int height) {
		return new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

	}
}
