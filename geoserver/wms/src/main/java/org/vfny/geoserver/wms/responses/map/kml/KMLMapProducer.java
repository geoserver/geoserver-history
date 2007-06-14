/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.AbstractGetMapProducer;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;
import javax.xml.transform.TransformerException;


/**
 * Handles a GetMap request that spects a map in KML format.
 *
 * @author Simone Giannecchini - GeoSolutions
 * @author James Macgill
 */
class KMLMapProducer extends AbstractGetMapProducer implements GetMapProducer {
    /** standard logger */
    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.responses.wms.kml");

    /**
     * encoder instance which does all the hard work
     *
     * @uml.property name="kmlEncoder"
     * @uml.associationEnd multiplicity="(0 1)"
     */

    // private EncodeKML kmlEncoder;
    /** kml transformer which turns the map contedxt into kml */
    private KMLTransformer transformer;

    public KMLMapProducer(String mapFormat, String mime_type) {
        super(mapFormat, mime_type);
    }

    /**
     * Request that encoding be halted if possible.
     *
     * @param gs
     *            The orriginating Service
     */
    public void abort(Service gs) {
        // this.kmlEncoder.abort();
        if (transformer != null) {
            // transformer.abort();
        }
    }

    /**
     * aborts the encoding.
     */
    public void abort() {
        LOGGER.fine("aborting KML map response");

        // if (this.kmlEncoder != null) {
        // LOGGER.info("aborting KML encoder");
        // this.kmlEncoder.abort();
        // }
        if (transformer != null) {
            LOGGER.info("aborting KML encoder");

            // transformer.abort();
        }
    }

    /**
     * Produce the actual map ready for outputing.
     *
     * @param map
     *            WMSMapContext describing what layers, styles, area of interest
     *            etc are to be used when producing the map.
     *
     * @throws WmsException
     *             thrown if anything goes wrong during the production.
     */
    public void produceMap() throws WmsException {
        transformer = new KMLTransformer();
    }

    /**
     * Pumps the map to the provided output stream. Note by this point that
     * produceMap should already have been called so little work should be done
     * within this method.
     *
     * @param out
     *            OutputStream to stream the map to.
     *
     * @throws ServiceException
     * @throws IOException
     *
     * @TODO replace stream copy with nio code.
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        // kmlEncoder.encodeKML(out);
        try {
            transformer.transform(mapContext, out);
        } catch (TransformerException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    public String getContentDisposition() {
        if (this.mapContext.getLayer(0) != null) {
            try {
                String title = this.mapContext.getLayer(0).getFeatureSource().getSchema()
                                              .getTypeName();

                if ((title != null) && !title.equals("")) {
                    return "attachment; filename=" + title + ".kml";
                }
            } catch (NullPointerException e) {
            }
        }

        return "attachment; filename=geoserver.kml";
    }
}
