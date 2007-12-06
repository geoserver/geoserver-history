/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import org.geotools.map.MapLayer;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.AbstractGetMapProducer;
import org.vfny.geoserver.wms.responses.map.png.PNGMapProducer;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.transform.TransformerException;


/**
 * Handles a GetMap request that spects a map in KMZ format.
 *
 * KMZ files are a zipped KML file. The KML file must have an emcompasing
 * <document> or <folder> element. So if you have many different placemarks or
 * ground overlays, they all need to be contained within one <document> element,
 * then zipped up and sent off with the extension "kmz".
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $
 * @author $Author: Brent Owens
 * @author Justin Deoliveira
 *
 */
class KMZMapProducer extends AbstractGetMapProducer implements GetMapProducer {
    /** standard logger */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.kmz");

    /**
     * delegating producer for rendering.
     */
    PNGMapProducer mapProducer;

    /**
     * transformer for creating kml
     */
    KMLTransformer transformer;

    public KMZMapProducer(String mapFormat, String mime_type, WMS wms) {
        super(mapFormat, mime_type);
        mapProducer = new PNGMapProducer("image/png", "image/png", wms);
    }

    public void abort() {
        LOGGER.fine("aborting KMZ map response");
        mapContext = null;
        mapProducer = null;
        transformer = null;
    }

    public String getContentDisposition() {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < mapContext.getLayerCount(); i++) {
            MapLayer layer = mapContext.getLayer(i);
            String title = layer.getFeatureSource().getSchema().getTypeName();

            if ((title != null) && !title.equals("")) {
                sb.append(title).append("_");
            }
        }

        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);

            return "attachment; filename=" + sb.toString() + ".kml";
        }

        return "attachment; filename=geoserver.kml";
    }

    public String getContentType() throws IllegalStateException {
        return KMZMapProducerFactory.MIME_TYPE;
    }

    /**
     * Initializes the KML encoder. None of the map production is done here, it
     * is done in writeTo(). This way the output can be streamed directly to the
     * output response and not written to disk first, then loaded in and then
     * sent to the response.
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
        transformer.setKmz(true);

        // TODO: use GeoServer.isVerbose() to determine if we should indent?
        transformer.setIndentation(3);
    }

    /**
     * Makes the map and sends it to the zipped output stream The produceMap()
     * method does not create the map in this case. We produce the map here so
     * we can stream directly to the response output stream, and not have to
     * write to disk, then send it to the stream.
     *
     * @Note: Do not close the output stream in this method, it gets closed
     *        later on.
     *
     * @param out
     *            OutputStream to stream the map to.
     *
     * @throws ServiceException
     * @throws IOException
     *
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        // wrap the output stream in a zipped one
        ZipOutputStream zip = new ZipOutputStream(out);

        // first create an entry for the kml
        ZipEntry entry = new ZipEntry("wms.kml");
        zip.putNextEntry(entry);

        try {
            transformer.transform(mapContext, zip);
            zip.closeEntry();
        } catch (TransformerException e) {
            throw (IOException) new IOException().initCause(e);
        }

        // write the images
        for (int i = 0; i < mapContext.getLayerCount(); i++) {
            MapLayer mapLayer = mapContext.getLayer(i);

            // create a context for this single layer
            WMSMapContext mapContext = new WMSMapContext();
            mapContext.addLayer(mapLayer);
            mapContext.setRequest(this.mapContext.getRequest());
            mapContext.setMapHeight(this.mapContext.getMapHeight());
            mapContext.setMapWidth(this.mapContext.getMapWidth());
            mapContext.setAreaOfInterest(this.mapContext.getAreaOfInterest());
            mapContext.setBgColor(this.mapContext.getBgColor());
            mapContext.setBuffer(this.mapContext.getBuffer());
            mapContext.setContactInformation(this.mapContext
                .getContactInformation());
            mapContext.setKeywords(this.mapContext.getKeywords());
            mapContext.setAbstract(this.mapContext.getAbstract());
            mapContext.setTransparent(true);

            // render the map
            mapProducer.setMapContext(mapContext);
            mapProducer.produceMap();

            // write it to the zip stream
            entry = new ZipEntry("layer_" + i + ".png");
            zip.putNextEntry(entry);
            mapProducer.writeTo(zip);
            zip.closeEntry();
        }

        zip.finish();
        zip.flush();
    }
}
