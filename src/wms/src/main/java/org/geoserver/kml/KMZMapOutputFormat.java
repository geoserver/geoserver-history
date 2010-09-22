/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.kml;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.transform.TransformerException;

import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetMapOutputFormat;
import org.geoserver.wms.WMS;
import org.geoserver.wms.WMSMapContext;
import org.geoserver.wms.map.AbstractMapOutputFormat;
import org.geoserver.wms.map.BufferedImageMap;
import org.geoserver.wms.map.PNGMapOutputFormat;
import org.geoserver.wms.map.XMLTransformerMap;
import org.geotools.map.MapLayer;
import org.geotools.xml.transform.TransformerBase;
import org.springframework.util.Assert;

/**
 * Handles a GetMap request that spects a map in KMZ format.
 * 
 * KMZ files are a zipped KML file. The KML file must have an emcompasing <document> or <folder>
 * element. So if you have many different placemarks or ground overlays, they all need to be
 * contained within one <document> element, then zipped up and sent off with the extension "kmz".
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $
 * @author $Author: Brent Owens
 * @author Justin Deoliveira
 * 
 */
public class KMZMapOutputFormat extends AbstractMapOutputFormat implements GetMapOutputFormat {
    /**
     * Official KMZ mime type
     */
    static final String MIME_TYPE = "application/vnd.google-earth.kmz+xml";

    public static final String[] OUTPUT_FORMATS = { MIME_TYPE, "application/vnd.google-earth.kmz",
            "kmz", "application/vnd.google-earth.kmz xml" };

    private WMS wms;

    public static class KMZMap extends XMLTransformerMap {
        public KMZMap(final WMSMapContext mapContext, TransformerBase transformer, String mimeType) {
            super(mapContext, transformer, mapContext, mimeType);
        }
    }

    public KMZMapOutputFormat(WMS wms) {
        super(KMZMap.class, MIME_TYPE, OUTPUT_FORMATS);
        this.wms = wms;
    }

    /**
     * Initializes the KML encoder. None of the map production is done here, it is done in
     * writeTo(). This way the output can be streamed directly to the output response and not
     * written to disk first, then loaded in and then sent to the response.
     * 
     * @param mapContext
     *            WMSMapContext describing what layers, styles, area of interest etc are to be used
     *            when producing the map.
     * @see org.geoserver.wms.GetMapOutputFormat#produceMap(org.geoserver.wms.WMSMapContext)
     */
    public KMZMap produceMap(WMSMapContext mapContext) throws ServiceException, IOException {
        KMLTransformer transformer = new KMLTransformer(wms);
        transformer.setKmz(true);
        Charset encoding = wms.getCharSet();
        transformer.setEncoding(encoding);
        // TODO: use GeoServer.isVerbose() to determine if we should indent?
        transformer.setIndentation(3);

        KMZMap map = new KMZMap(mapContext, transformer, MIME_TYPE);
        map.setContentDispositionHeader(mapContext, ".kmz");
        return map;
    }

    /**
     * Makes the map and sends it to the zipped output stream The produceMap() method does not
     * create the map in this case. We produce the map here so we can stream directly to the
     * response output stream, and not have to write to disk, then send it to the stream.
     * 
     * @param value
     *            a {@link XMLTransformerMap} as produced by this class'
     *            {@link #produceMap(WMSMapContext)}
     * @param out
     *            OutputStream to stream the map to.
     * 
     * @see org.geoserver.ows.Response#write(java.lang.Object, java.io.OutputStream,
     *      org.geoserver.platform.Operation)
     */
    @Override
    public void write(Object value, OutputStream output, Operation operation) throws IOException,
            ServiceException {
        Assert.isInstanceOf(XMLTransformerMap.class, value);

        final XMLTransformerMap map = (XMLTransformerMap) value;
        try {
            final KMLTransformer transformer = (KMLTransformer) map.getTransformer();
            final WMSMapContext mapContext = (WMSMapContext) map.getTransformerSubject();

            // wrap the output stream in a zipped one
            ZipOutputStream zip = new ZipOutputStream(output);

            // first create an entry for the kml
            ZipEntry entry = new ZipEntry("wms.kml");
            zip.putNextEntry(entry);

            try {
                transformer.transform(mapContext, zip);
                zip.closeEntry();
            } catch (TransformerException e) {
                throw (IOException) new IOException().initCause(e);
            }

            final PNGMapOutputFormat mapProducer = new PNGMapOutputFormat(wms);
            // write the images
            for (int i = 0; i < mapContext.getLayerCount(); i++) {
                MapLayer mapLayer = mapContext.getLayer(i);

                // create a context for this single layer
                WMSMapContext subContext = new WMSMapContext();
                subContext.addLayer(mapLayer);
                subContext.setRequest(mapContext.getRequest());
                subContext.setMapHeight(mapContext.getMapHeight());
                subContext.setMapWidth(mapContext.getMapWidth());
                subContext.setAreaOfInterest(mapContext.getAreaOfInterest());
                subContext.setBgColor(mapContext.getBgColor());
                subContext.setBuffer(mapContext.getBuffer());
                subContext.setContactInformation(mapContext.getContactInformation());
                subContext.setKeywords(mapContext.getKeywords());
                subContext.setAbstract(mapContext.getAbstract());
                subContext.setTransparent(true);

                // render the map
                BufferedImageMap imageMap;
                try {
                    imageMap = mapProducer.produceMap(subContext);
                } finally {
                    subContext.dispose();
                }

                // write it to the zip stream
                entry = new ZipEntry("layer_" + i + ".png");
                zip.putNextEntry(entry);
                mapProducer.write(imageMap, zip, operation);
                zip.closeEntry();
            }

            zip.finish();
            zip.flush();
        } finally {
            map.dispose();
        }
    }

}
