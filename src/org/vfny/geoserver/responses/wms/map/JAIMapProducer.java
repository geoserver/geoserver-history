/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.map;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.vfny.geoserver.WmsException;


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
public class JAIMapProducer extends DefaultRasterMapProducer {
    /** A logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.map");

    /** DOCUMENT ME! */
    private static final String DEFAULT_MAP_FORMAT = "image/png";

    /**
     *
     */
    public JAIMapProducer() {
        this(DEFAULT_MAP_FORMAT);
    }

    /**
     *
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
     * @throws WmsException
     * @throws IOException DOCUMENT ME!
     */
    protected void formatImageOutputStream(String format, BufferedImage image,
        OutputStream outStream) throws WmsException, IOException {
        if (format.equalsIgnoreCase("jpeg")) {
            format = "image/jpeg";
        }

        Iterator it = ImageIO.getImageWritersByMIMEType(format);

        if (!it.hasNext()) {
            throw new WmsException( //WMSException.WMSCODE_INVALIDFORMAT,
                "Format not supported: " + format);
        }

        ImageWriter writer = (ImageWriter) it.next();
        ImageOutputStream ioutstream = null;

        ioutstream = ImageIO.createImageOutputStream(outStream);
        writer.setOutput(ioutstream);
        writer.write(image);
        writer.dispose();
        ioutstream.close();
    }
}
