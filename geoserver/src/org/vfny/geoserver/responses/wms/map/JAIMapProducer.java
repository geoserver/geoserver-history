/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.map;

import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.responses.wms.DefaultRasterMapProducer;
import org.vfny.geoserver.responses.wms.helpers.JAISupport;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;


/**
 * Generates a map using the geotools jai rendering classes.  Uses the Lite
 * renderer, loading the data on the fly, which is quite nice.  Thanks Andrea
 * and Gabriel.  The word is that we should eventually switch over to
 * StyledMapRenderer and do some fancy stuff with caching layers, but  I think
 * we are a ways off with its maturity to try that yet.  So Lite treats us
 * quite well, as it is stateless and therefor loads up nice and fast.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: JAIMapResponse.java,v 1.29 2004/09/16 21:44:28 cholmesny Exp $
 */
class JAIMapProducer extends DefaultRasterMapProducer {
    /** A logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.map");

    /** DOCUMENT ME! */
    private static final String DEFAULT_MAP_FORMAT = "image/png";

    /**
     * Creates a map producer that relies on JAI to encode the BufferedImage
     * generated the default (image/png) image format.
     *
     */
    public JAIMapProducer() {
        this(DEFAULT_MAP_FORMAT);
    }

    /**
     * Creates a map producer that relies on JAI to encode the BufferedImage
     * generated in <code>outputFormat</code> format.
     *
     * @param outputFormat the output format MIME type.
     */
    public JAIMapProducer(String outputFormat) {
        setOutputFormat(outputFormat);
    }

    /**
     * Transforms the rendered image into the appropriate format, streaming to
     * the output stream.
     *
     * @param format The name of the format
     * @param image The image to be formatted.
     * @param outStream The stream to write to.
     *
     * @throws WmsException not really.
     * @throws IOException if the image writing fails.
     */
    protected void formatImageOutputStream(String format, BufferedImage image,
        OutputStream outStream) throws WmsException, IOException {
        JAISupport.encode(format, image, outStream);
    }
}
