/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Extends GetMapProducer to allow users (for example, the meta tiler) to peek
 * inside th map production chain at a finer grained level
 * 
 * @author Andrea Aime
 * @author Simone Giannecchini - GeoSolutions SAS
 * 
 */
public interface RasterMapProducer extends GetMapProducer {
	/**
	 * Returns the raw image generated in the
	 * {@link GetMapProducer#produceMap(WMSMapContext)} step
	 * 
	 * @return
	 */
	public RenderedImage getImage();

	/**
	 * Transforms a rendered image into the appropriate format, streaming to the
	 * output stream.
	 * 
	 * @param image
	 *            The image to be formatted.
	 * @param outStream
	 *            The stream to write to.
	 * 
	 * @throws WmsException
	 * @throws IOException
	 */
	public void formatImageOutputStream(RenderedImage image,
			OutputStream outStream) throws WmsException, IOException;
}
