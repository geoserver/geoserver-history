/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.map;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetMapOutputFormat;
import org.geoserver.wms.WMSMapContext;

/**
 * Extends GetMapOutputFormat to allow users (for example, the meta tiler) to peek inside th map
 * production chain at a finer grained level
 * 
 * @author Andrea Aime
 * @author Simone Giannecchini - GeoSolutions SAS
 * 
 */
public interface RasterMapOutputFormat extends GetMapOutputFormat {

    /**
     * Transforms a rendered image into the appropriate format, streaming to the output stream.
     * 
     * @param image
     *            The image to be formatted.
     * @param outStream
     *            The stream to write to.
     * 
     * @throws ServiceException
     * @throws IOException
     */
    public void formatImageOutputStream(RenderedImage image, OutputStream outStream,
            WMSMapContext mapContext) throws ServiceException, IOException;

    /**
     * @see org.geoserver.wms.GetMapOutputFormat#produceMap(org.geoserver.wms.WMSMapContext)
     */
    public abstract RenderedImageMap produceMap(WMSMapContext mapContext) throws ServiceException,
            IOException;

}
