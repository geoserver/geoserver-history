/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.helpers;

import org.geotools.renderer.lite.LiteRenderer2;
import org.vfny.geoserver.WmsException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;


/**
 * Helper class to deal with JAI availability and image encoding
 */
public final class JAISupport {
    /** shared package's logger */
    private static final Logger LOGGER = Logger.getLogger(JAISupport.class.getPackage()
                                                                          .getName());

    /**
     * Set&lt;String&gt; of the MIME types the available JAI library supports,
     * or the empty set if it is not available.
     */
    private static Set supportedFormats;

    /**
     * Returns the set of supported formats by the available JAI library, or
     * the empty set if not available.
     *
     * @return Set&lt;String&gt; of the MIME types the available JAI library
     *         supports, or the empty set if it is not available.
     */
    public static Set getSupportedFormats() {
        if (supportedFormats == null) {
            //LiteRenderer renderer = null;
            String[] mimeTypes = null;

            LiteRenderer2 testRenderer = null;

            try {
                testRenderer = new LiteRenderer2();
                mimeTypes = ImageIO.getWriterMIMETypes();
            } catch (NoClassDefFoundError ncdfe) {
                supportedFormats = Collections.EMPTY_SET;
                LOGGER.warning("could not find jai: " + ncdfe);

                //this will occur if JAI is not present, so please do not
                //delete, or we get really nasty messages on getCaps for wms.
            }

            if ((testRenderer == null) || (mimeTypes == null)) {
                LOGGER.info("renderer was null, so jai not found");
                supportedFormats = Collections.EMPTY_SET;
            } else {
                supportedFormats = new HashSet();

                List formatsList = Arrays.asList(mimeTypes);

                for (Iterator it = formatsList.iterator(); it.hasNext();) {
                    String curFormat = it.next().toString();

                    if (!curFormat.equals("")) {
                        supportedFormats.add(curFormat);
                    }
                }

                if (LOGGER.isLoggable(Level.CONFIG)) {
                    StringBuffer sb = new StringBuffer(
                            "Supported JAIMapResponse's MIME Types: [");

                    for (Iterator it = supportedFormats.iterator();
                            it.hasNext();) {
                        sb.append(it.next());

                        if (it.hasNext()) {
                            sb.append(", ");
                        }
                    }

                    sb.append("]");
                    LOGGER.config(sb.toString());
                }
            }
        }

        return supportedFormats;
    }

    /**
     * Returns wether the JAI library is available by checking  the available
     * formats.
     *
     * @return <code>true</code> if JAI is available
     *
     * @see #getSupportedFormats()
     */
    public static boolean isJaiAvailable() {
        return getSupportedFormats().size() > 0;
    }

    /**
     * Encodes a BufferedImage using JAI in <code>format</code> format and
     * sends it to <code>outStream</code>.
     *
     * @param format the MIME type of the output image in which to encode
     *        <code>image</code> through JAI
     * @param image the actual image to be encoded in <code>format</code>
     *        format.
     * @param outStream the encoded image destination.
     *
     * @throws IOException if the image writing to <code>outStream</code>
     *         fails.
     * @throws IllegalArgumentException if <code>format</code> is not a
     *         supported output format for the installed JAI library.
     */
    protected void encode(String format, BufferedImage image,
        OutputStream outStream) throws IOException {
        if (format.equalsIgnoreCase("jpeg")) {
            format = "image/jpeg";
        }

        Iterator it = ImageIO.getImageWritersByMIMEType(format);

        if (!it.hasNext()) {
            throw new IllegalArgumentException("Format not supported: "
                + format);
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
