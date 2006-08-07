/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.gif;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.PackedColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.imageio.IIOImage;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.media.jai.PlanarImage;

import org.geotools.resources.image.ImageUtilities;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;

import com.sun.media.imageioimpl.plugins.gif.GIFImageWriter;
import com.sun.media.imageioimpl.plugins.gif.GIFImageWriterSpi;

/**
 * Handles a GetMap request that spects a map in GIF format.
 * 
 * @author Didier Richard
 * @author Simone Giannechini
 * @version $Id
 */
public final class GIFMapProducer extends DefaultRasterMapProducer {
	/**
	 * Since the default palette has not transparency I am here tryin to create
	 * a default one with transparency information inside.
	 */
	private final static IndexColorModel DEFAULT_PALETTE;
	static {
		// Create a 6x6x6 color cube
		int[] cmap = new int[256];
		int i = 0;
		for (int r = 0; r < 256; r += 51) {
			for (int g = 0; g < 256; g += 51) {
				for (int b = 0; b < 256; b += 51) {
					cmap[i++] = (r << 16) | (g << 8) | b;
				}
			}
		}
		// And populate the rest of the cmap with gray values
		int grayIncr = 256 / (256 - i);

		// The gray ramp will be between 18 and 252
		int gray = grayIncr * 3;
		for (; i < 255; i++) {
			cmap[i] = (gray << 16) | (gray << 8) | gray;
			gray += grayIncr;
		}

		DEFAULT_PALETTE = new IndexColorModel(8, 256, cmap, 0, true, 255,
				DataBuffer.TYPE_BYTE);

	}

	public GIFMapProducer(String format) {
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
	 *            The stream to write to.cd
	 * @throws WmsException
	 *             not really.
	 * @throws IOException
	 *             if encoding to <code>outStream</code> fails.
	 */
	protected void formatImageOutputStream(String format, BufferedImage image,
			OutputStream outStream) throws WmsException, IOException {
		if (!format.equalsIgnoreCase(GifMapProducerFactory.MIME_TYPE))
			throw new IllegalArgumentException("The provided format " + format
					+ " is not the same as expected: "
					+ GifMapProducerFactory.MIME_TYPE);

		final MemoryCacheImageOutputStream memOutStream = new MemoryCacheImageOutputStream(
				outStream);
		PlanarImage encodedImage = PlanarImage.wrapRenderedImage(image);
		final ColorModel cm = image.getColorModel();
		if (cm instanceof PackedColorModel)
			encodedImage = ImageUtilities
					.reformatColorModel2ComponentColorModel(encodedImage);
		if (!(cm instanceof IndexColorModel))
			encodedImage = ImageUtilities
					.componentColorModel2IndexColorModel4GIF(encodedImage);
		encodedImage = ImageUtilities
				.convertIndexColorModelAlpha4GIF(encodedImage);
		final ImageWriter gifWriter = new GIFImageWriter(
				new GIFImageWriterSpi());
		final ImageWriteParam iwp = gifWriter.getDefaultWriteParam();
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwp.setCompressionType("LZW");
		iwp.setCompressionQuality(0.75f);

		gifWriter.setOutput(memOutStream);
		gifWriter.write(null, new IIOImage(encodedImage, null, null), null);
		memOutStream.flush();
		memOutStream.close();
		outStream.flush();
		outStream.close();

	}

	protected BufferedImage prepareImage(int width, int height) {

		final int size = width * height;
		final byte pixels[] = new byte[size];
		Arrays.fill(pixels, (byte) 255);

		// Create a data buffer using the byte buffer of pixel data.
		// The pixel data is not copied; the data buffer uses the byte buffer
		// array.
		DataBuffer dbuf = new DataBufferByte(pixels, width * height, 0);



		// Prepare a sample model that specifies a storage 4-bits of
		// pixel datavd in an 8-bit data element
		int bitMasks[] = new int[] { (byte) 0xf };
		SampleModel sampleModel = new SinglePixelPackedSampleModel(
				DataBuffer.TYPE_BYTE, width, height, bitMasks);

		// Create a raster using the sample model and data buffer
		WritableRaster raster = Raster.createWritableRaster(sampleModel, dbuf,
				null);

		// Combine the color model and raster into a buffered image
		return new BufferedImage(DEFAULT_PALETTE, raster, false, null);// new
																	// java.util.Hashtable());

	}
}
