/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.media.jai.PlanarImage;

import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.DefaultRasterMapProducer;

import com.sun.media.imageioimpl.plugins.gif.GIFImageWriter;
import com.sun.media.imageioimpl.plugins.gif.GIFImageWriterSpi;

/**
 * Handles a GetMap request that spects a map in GIF format.
 * 
 * @author Didier Richard
 * @version $Id
 */
class GIFMapProducer extends DefaultRasterMapProducer {

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
	 *            The stream to write to.
	 * 
	 * @throws WmsException
	 *             not really.
	 * @throws IOException
	 *             if encoding to <code>outStream</code> fails.
	 */
	protected void formatImageOutputStream(String format, BufferedImage image,
			OutputStream outStream) throws WmsException, IOException {

		final MemoryCacheImageOutputStream memOutStream = new MemoryCacheImageOutputStream(
				outStream);
		final PlanarImage encodedImage = PlanarImage.wrapRenderedImage(image);
		final ImageWriter gifWriter = new GIFImageWriter(
				new GIFImageWriterSpi());
		gifWriter.setOutput(memOutStream);
		gifWriter.write(null, new IIOImage(encodedImage, null, null), null);
		memOutStream.flush();
		memOutStream.close();
		outStream.flush();
		outStream.close();


	}
}
