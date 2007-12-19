/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.pdf;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;
import java.util.Collections;
import java.util.Map;
import java.util.Set;


/**
 * This class is used as part of the SPI auto discovery process which enables
 * new format producers to be plugged in.
 *
 * @author Pierre-Emmanuel Balageas, ALCER (http://www.alcer.com)
 * @author Simone Giannecchini - GeoSolutions
 * @version $Id$
 */
public class PDFMapProducerFactory implements GetMapProducerFactorySpi {
    /** the only MIME type this map producer supports */
    static final String MIME_TYPE = "application/pdf";

    /**
     * convenient singleton Set to expose the output format this producer
     * supports
     */
    private static final Set SUPPORTED_FORMATS = Collections.singleton(MIME_TYPE);

    /**
     * Creates a new PdfMapProducerFactory object.
     */
    public PDFMapProducerFactory() {
        super();
    }

    /**
     * get a readable name of the supported MIME type
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "Portable Document Format (PDF) map producer";
    }

    /**
     * Returns the Set of output format this producer supports
     *
     * @return Set of output format this producer supports (actually
     *         "application/pdf")
     */
    public Set getSupportedFormats() {
        return SUPPORTED_FORMATS;
    }

    /**
     * PDFMapProducer depends of iText-1.3.6.jar
     *
     * @return <code>true</code> if iText.jar is loaded <code>false</code>
     *         otherwise
     */
    public boolean isAvailable() {
        try {
            Class.forName("com.lowagie.text.pdf.PdfTemplate");
            Class.forName("com.lowagie.text.Document");
            Class.forName("com.lowagie.text.FontFactory");
            Class.forName("com.lowagie.text.pdf.DefaultFontMapper");
            Class.forName("com.lowagie.text.pdf.PdfContentByte");
            Class.forName("com.lowagie.text.pdf.PdfTemplate");
            Class.forName("com.lowagie.text.pdf.PdfWriter");

            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns wether the map producers created by this factory can create maps
     * in the passed output format.
     *
     * @param mapFormat
     *            a MIME type string to check if this producer is able to
     *            handle.
     *
     * @return <code>true</code> if <code>mapFormat ==
     *         "application/pdf"</code>,
     *         <code>false</code> otherwise.
     */
    public boolean canProduce(String mapFormat) {
        return MIME_TYPE.equals(mapFormat);
    }

    /**
     * DOCUMENT ME!
     *
     * @param mapFormat
     *            a MIME type string to check if this producer is able to
     *            handle.
     * @param config
     *            DOCUMENT ME!
     *
     * @return an instance of the PDFMapProducer if mapFormat is supported an
     *         IllegalArgumentException otherwise
     *
     * @throws IllegalArgumentException
     *             if mapFormat is not supported
     */
    public GetMapProducer createMapProducer(String mapFormat, WMS wms)
        throws IllegalArgumentException {
        if (!canProduce(mapFormat)) {
            throw new IllegalArgumentException(mapFormat + " not supported by this map producer");
        }

        return new PDFMapProducer(mapFormat, MIME_TYPE);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.factory.Factory#getImplementationHints() This just
     *      returns java.util.Collections.EMPTY_MAP
     */
    public Map getImplementationHints() {
        return java.util.Collections.EMPTY_MAP;
    }
}
