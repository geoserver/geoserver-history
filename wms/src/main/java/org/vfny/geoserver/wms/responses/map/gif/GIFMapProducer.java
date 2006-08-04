/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.gif;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.PackedColorModel;
import java.io.IOException;
import java.io.OutputStream;

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
		encodedImage=ImageUtilities.convertIndexColorModelAlpha4GIF(encodedImage);
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
}
