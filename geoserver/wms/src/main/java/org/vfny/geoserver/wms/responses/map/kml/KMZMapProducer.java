/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import org.geotools.map.MapLayer;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
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
 * KMZ files are a zipped KML file. The KML file must have an emcompasing <document> or <folder> element.
 * So if you have many different placemarks or ground overlays, they all need to be contained
 * within one <document> element, then zipped up and sent off with the extension "kmz".
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $
 * @author $Author: Brent Owens
 * @author Justin Deoliveira
 *
 */
class KMZMapProducer implements GetMapProducer {
    /**
     * Map context
     */
    WMSMapContext mapContext;

    /**
     * delegating producer for rendering.
     */
    PNGMapProducer mapProducer;

    /**
     * transformer for creating kml
     */
    KMLTransformer transformer;

    public KMZMapProducer(WMS wms) {
        mapProducer = new PNGMapProducer("image/png", wms);
    }

    public void abort() {
        mapContext = null;
        mapProducer = null;
        transformer = null;
    }

    public String getContentDisposition() {
        StringBuffer sb = new StringBuffer();
        for ( int i = 0; i < mapContext.getLayerCount(); i++) {
            MapLayer layer = mapContext.getLayer(i);
            String title = layer.getFeatureSource().getSchema() .getTypeName();
            if (title != null && !title.equals("")) {
                    sb.append( title ).append("_");
            }
        }
        if ( sb.length() > 0 ) {
            sb.setLength(sb.length()-1);
            return "attachment; filename=" + sb.toString() + ".kml";
        }
        return "attachment; filename=geoserver.kml";
    }

    public String getContentType() throws IllegalStateException {
        return KMZMapProducerFactory.MIME_TYPE;
    }

    public void produceMap(WMSMapContext map) throws WmsException {
        this.mapContext = map;
        transformer = new KMLTransformer();
        transformer.setKmz(true);
        
        //TODO: use GeoServer.isVerbose() to determine if we should indent?
        transformer.setIndentation(3);
    }

    public void writeTo(OutputStream out) throws ServiceException, IOException {
        //wrap the output stream in a zipped one
        ZipOutputStream zip = new ZipOutputStream(out);

        //first create an entry for the kml
        ZipEntry entry = new ZipEntry("wms.kml");
        zip.putNextEntry(entry);

        try {
            transformer.transform(mapContext, zip);
            zip.closeEntry();
        } catch (TransformerException e) {
            throw (IOException) new IOException().initCause(e);
        }

        //write the images
        for (int i = 0; i < mapContext.getLayerCount(); i++) {
            MapLayer mapLayer = mapContext.getLayer(i);

            //create a context for this single layer
            WMSMapContext mapContext = new WMSMapContext();
            mapContext.addLayer(mapLayer);
            mapContext.setRequest(this.mapContext.getRequest());
            mapContext.setMapHeight(this.mapContext.getMapHeight());
            mapContext.setMapWidth(this.mapContext.getMapWidth());
            mapContext.setAreaOfInterest(this.mapContext.getAreaOfInterest());
            mapContext.setBgColor(this.mapContext.getBgColor());
            mapContext.setBuffer(this.mapContext.getBuffer());
            mapContext.setContactInformation(this.mapContext.getContactInformation());
            mapContext.setKeywords(this.mapContext.getKeywords());
            mapContext.setAbstract(this.mapContext.getAbstract());
            mapContext.setTransparent(true);

            //render the map
            mapProducer.produceMap(mapContext);

            //write it to the zip stream
            entry = new ZipEntry("layer_" + i + ".png");
            zip.putNextEntry(entry);
            mapProducer.writeTo(zip);
            zip.closeEntry();
        }

        zip.finish();
        zip.flush();
    }
}
