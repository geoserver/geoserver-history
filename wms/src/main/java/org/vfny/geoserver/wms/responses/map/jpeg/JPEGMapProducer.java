package org.vfny.geoserver.wms.responses.map.jpeg;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
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

	/**
	 * This class overrides the setCompressionQuality() method to workaround a
	 * problem in compressing JPEG images using the javax.imageio package.
	 */
	public static class MyImageWriteParam extends JPEGImageWriteParam {
		public MyImageWriteParam() {
			super(Locale.getDefault());
		}

		/**
		 * This method accepts quality levels between 0 (lowest) and 1 (highest)
		 * and simply converts it to a range between 0 and 256; this is not a
		 * correct conversion algorithm. However, a proper alternative is a lot
		 * more complicated. This should do until the bug is fixed.
		 */
		public void setCompressionQuality(float quality) {
			if (quality < 0.0F || quality > 1.0F) {
				throw new IllegalArgumentException("Quality out-of-bounds!");
			}
			this.compressionQuality = 256 - (quality * 256);
		}
	}

	public JPEGMapProducer(String outputFormat, WMS wms) {
		super(outputFormat, wms);
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
			throw new IllegalStateException("No PNG ImageWriter found");
		writer = (ImageWriter) it.next();
		// //
		//
		// XXX Attention the native JPEG writer gives strange results I am doing
		// what follows to avoid using it.
		//
		// //
		if (writer.getClass().getName().equals(
				"com.sun.media.imageioimpl.plugins.jpeg.CLibJPEGImageWriter"))
			writer = (ImageWriter) it.next();

		// /////////////////////////////////////////////////////////////////
		//
		// Compression is available only on native lib
		//
		// /////////////////////////////////////////////////////////////////
		final ImageWriteParam iwp;
		final MemoryCacheImageOutputStream memOutStream = new MemoryCacheImageOutputStream(
				outStream);
		if (writer.getClass().getName().equals(
				"com.sun.media.imageioimpl.plugins.jpeg.CLibJPEGImageWriter")) {
			iwp = writer.getDefaultWriteParam();
			iwp.setCompressionType("JPEG");

		} else {
			iwp = new MyImageWriteParam();

		}
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwp.setCompressionQuality(0.75f);// we can control quality here
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
