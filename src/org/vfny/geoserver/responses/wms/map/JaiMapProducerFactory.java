/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.map;

import org.geotools.renderer.lite.LiteRenderer2;
import org.vfny.geoserver.responses.wms.GetMapProducer;
import org.vfny.geoserver.responses.wms.GetMapProducerFactorySpi;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class JaiMapProducerFactory implements GetMapProducerFactorySpi {
    /** DOCUMENT ME!  */
    private static final Logger LOGGER = Logger.getLogger(JaiMapProducerFactory.class.getPackage()
                                                                                     .getName());

    /** The formats supported by this map delegate. */
    private static Set supportedFormats = null;

    /**
     *
     */
    public JaiMapProducerFactory() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "JAI backed raster maps producer";
    }

    /**
     * The formats this delegate supports. Includes those formats supported by
     * the Java ImageIO extension, mostly: <i>png, x-portable-graymap, jpeg,
     * jpeg2000, x-png, tiff, vnd.wap.wbmp, x-portable-pixmap,
     * x-portable-bitmap, bmp and x-portable-anymap</i>, but the specific ones
     * will depend on the platform and JAI version. At leas JPEG and PNG will
     * generally work.
     *
     * @return The list of the supported formats, as returned by the Java
     *         ImageIO extension.
     */
    public Set getSupportedFormats() {
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
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAvailable() {
        return getSupportedFormats().size() > 0;
    }

    /**
     * Evaluates if this Map producer can generate the map format specified by
     * <code>mapFormat</code>
     *
     * @param mapFormat the mime type of the output map format requiered
     *
     * @return true if class can produce a map in the passed format
     */
    public boolean canProduce(String mapFormat) {
        return getSupportedFormats().contains(mapFormat);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapFormat DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public GetMapProducer createMapProducer(String mapFormat)
        throws IllegalArgumentException {
    	if(!canProduce(mapFormat))
    		throw new IllegalArgumentException("Can't produce " + mapFormat + " format");
    	return new JAIMapProducer(mapFormat);
    }
}
