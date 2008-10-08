/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import java.util.logging.Logger;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapLayer;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.xml.transform.Translator;
import org.vfny.geoserver.wms.WMSMapContext;
import org.xml.sax.ContentHandler;

import com.vividsolutions.jts.geom.Envelope;

/**
 * KML transformer for doing superoverlay regions.
 * 
 * @author Justin Deoliveira
 * @version $Id$
 */
public class KMLSuperOverlayTransformer extends KMLTransformerBase {
    /**
     * logger
     */
    static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.kml");

    /**
     * the world bounds
     */
    final static ReferencedEnvelope world = new ReferencedEnvelope(-180, 180, -90, 90,
            DefaultGeographicCRS.WGS84);

    /**
     * resolutions
     */
    final static double[] resolutions = new double[100];

    static {
        for (int i = 0; i < resolutions.length; i++) {
            resolutions[i] = world.getWidth() / ((0x01 << i) * 256);
        }
    }

    /**
     * The map context
     */
    final WMSMapContext mapContext;

    public KMLSuperOverlayTransformer(WMSMapContext mapContext) {
        this.mapContext = mapContext;
        setNamespaceDeclarationEnabled(false);
    }

    public Translator createTranslator(ContentHandler handler) {
        return new KMLSuperOverlayerTranslator(handler);
    }

    class KMLSuperOverlayerTranslator extends KMLTranslatorSupport {
        public KMLSuperOverlayerTranslator(ContentHandler contentHandler) {
            super(contentHandler);
        }

        public void encode(Object o) throws IllegalArgumentException {
            MapLayer mapLayer = (MapLayer) o;

            //calculate closest resolution
            ReferencedEnvelope extent = mapContext.getAreaOfInterest();
            double resolution = Math.max(extent.getWidth() / 256d, extent.getHeight() / 256d);

            //calculate the closest zoom level
            int i = 1;

            for (; i < resolutions.length; i++) {
                if (resolution > resolutions[i]) {
                    i--;

                    break;
                }
            }

            LOGGER.fine("resolution = " + resolution);
            LOGGER.fine("zoom level = " + i);

            //zoom out until the entire bounds requested is covered by a 
            //single tile
            Envelope top = null;

            while (i > 0) {
                resolution = resolutions[i];

                double tilelon = resolution * 256;
                double tilelat = resolution * 256;

                double lon0 = extent.getMinX() - world.getMinX();
                double lon1 = extent.getMaxX() - world.getMinX();

                int col0 = (int) Math.floor(lon0 / tilelon);
                int col1 = (int) Math.floor((lon1 / tilelon) - 1E-9);

                double lat0 = extent.getMinY() - world.getMinY();
                double lat1 = extent.getMaxY() - world.getMinY();

                int row0 = (int) Math.floor(lat0 / tilelat);
                int row1 = (int) Math.floor((lat1 / tilelat) - 1E-9);

                if ((col0 == col1) && (row0 == row1)) {
                    double tileoffsetlon = world.getMinX() + (col0 * tilelon);
                    double tileoffsetlat = world.getMinY() + (row0 * tilelat);

                    top = new Envelope(tileoffsetlon, tileoffsetlon + tilelon, tileoffsetlat,
                            tileoffsetlat + tilelat);

                    break;
                } else {
                    i--;
                }
            }

            if (top == null) {
                top = world;
            }

            LOGGER.fine("request = " + extent);
            LOGGER.fine("top level = " + top);

            //start document
            if (isStandAlone()) {
                start( "kml" );
            }
            
            start("Document");

            //encode top level region
            encodeRegion(top, 256, 1024);

            //encode the network links
            if (top != world) {
                //top left
                Envelope e00 = new Envelope(top.getMinX(), top.getMinX() + (top.getWidth() / 2d),
                        top.getMaxY() - (top.getHeight() / 2d), top.getMaxY());

                //top right
                Envelope e01 = new Envelope(e00.getMaxX(), top.getMaxX(), e00.getMinY(),
                        e00.getMaxY());

                //bottom left
                Envelope e10 = new Envelope(e00.getMinX(), e00.getMaxX(), top.getMinY(),
                        e00.getMinY());

                //bottom right
                Envelope e11 = new Envelope(e01.getMinX(), e01.getMaxX(), e10.getMinY(),
                        e10.getMaxY());

                encodeNetworkLink(e00, "00", mapLayer);
                encodeNetworkLink(e01, "01", mapLayer);
                encodeNetworkLink(e10, "10", mapLayer);
                encodeNetworkLink(e11, "11", mapLayer);
            } else {
                //divide up horizontally by two
                Envelope e0 = new Envelope(top.getMinX(), top.getMinX() + (top.getWidth() / 2d),
                        top.getMinY(), top.getMaxY());
                Envelope e1 = new Envelope(e0.getMaxX(), top.getMaxX(), top.getMinY(), top.getMaxY());

                encodeNetworkLink(e0, "0", mapLayer);
                encodeNetworkLink(e1, "1", mapLayer);
            }

            //encode the ground overlay(s)
            if (top == world) {
                //special case for top since it does not line up as a propery
                // tile -> split it in two
                encodeGroundOverlay(mapLayer, i, new Envelope(-180, 0, -90, 90));
                encodeGroundOverlay(mapLayer, i, new Envelope(0, 180, -90, 90));
            } else {
                //encode straight up
                encodeGroundOverlay(mapLayer, i, top);
            }

            //end document
            end("Document");
            
            if (isStandAlone()) {
                end( "kml" );
            }
        }

        void encodeGroundOverlay(MapLayer mapLayer, int drawOrder, Envelope box) {
            start("GroundOverlay");
            element("drawOrder", "" + drawOrder);

            start("Icon");

            String href = KMLUtils.getMapUrl(mapContext, mapLayer, 0, box,
                    new String[] { "width", "256", "height", "256" }, true);
            element("href", href);
            LOGGER.fine(href);
            end("Icon");

            encodeLatLonBox(box);
            end("GroundOverlay");
        }

        void encodeRegion(Envelope box, int minLodPixels, int maxLodPixels) {
            //top level region
            start("Region");

            start("Lod");
            element("minLodPixels", "128");
            //element("minLodPixels", "" + minLodPixels );
            element("maxLodPixels", "" + maxLodPixels);
            //element("maxLodPixels", "-1" );
            end("Lod");

            encodeLatLonAltBox(box);

            end("Region");
        }

        void encodeNetworkLink(Envelope box, String name, MapLayer mapLayer) {
            start("NetworkLink");
            element("name", name);

            encodeRegion(box, 512, 2048);

            start("Link");

            String getMap = KMLUtils.getMapUrl(mapContext, mapLayer, 0, box,
                    new String[] {
                        "format", KMLMapProducer.MIME_TYPE, "width", "256", "height", "256",
                        "superoverlay", "true"
                    }, false);

            element("href", getMap);
            LOGGER.fine("Network link " + name + ":" + getMap);

            element("viewRefreshMode", "onRegion");

            end("Link");

            end("NetworkLink");
        }

        void encodeLatLonAltBox(Envelope box) {
            start("LatLonAltBox");
            encodeBox(box);
            end("LatLonAltBox");
        }

        void encodeLatLonBox(Envelope box) {
            start("LatLonBox");
            encodeBox(box);
            end("LatLonBox");
        }

        void encodeBox(Envelope box) {
            element("north", String.valueOf(box.getMaxY()));
            element("south", String.valueOf(box.getMinY()));
            element("east", String.valueOf(box.getMaxX()));
            element("west", String.valueOf(box.getMinX()));
        }
    }
}
