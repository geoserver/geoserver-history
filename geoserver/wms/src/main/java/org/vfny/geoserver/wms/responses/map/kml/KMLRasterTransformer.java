/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import com.vividsolutions.jts.geom.Envelope;

import net.opengis.ows.WGS84BoundingBoxType;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeocentricCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.vfny.geoserver.util.Requests;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.xml.sax.ContentHandler;
import java.util.Map;


/**
 * Tranforms a feature colleciton to a kml "Document" element which contains a
 * "Folder" element consisting of "GroundOverlay" elements.
 * <p>
 * Usage:
 * <pre>
 *  <code>
 *  //have a reference to a map context and output stream
 *  WMSMapContext context = ...
 *  OutputStream output = ...;
 *
 *  KMLRasterTransformer tx = new KMLRasterTransformer( context );
 *  for ( int i = 0; i < context.getLayerCount(); i++ ) {
 *    MapLayer mapLayer = mapConext.getMapLayer( i );
 *
 *    //transform
 *    tx.transform( mapLayer, output );
 *  }
 *  </code>
 * </pre>
 * </p>
 * <p>
 * The inline parameter {@link #setInline(boolean)} controls wether the images
 * for the request are refernces "inline" as local images, or remoteley as wms
 * requests.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class KMLRasterTransformer extends KMLTransformerBase {
    /**
     * The map context
     */
    final WMSMapContext mapContext;

    /**
     * Flag controlling wether images are refernces inline or as remote wms calls.
     */
    boolean inline = false;

    public KMLRasterTransformer(WMSMapContext mapContext) {
        this.mapContext = mapContext;

        setNamespaceDeclarationEnabled(false);
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public Translator createTranslator(ContentHandler handler) {
        return new KMLRasterTranslator(handler);
    }

    class KMLRasterTranslator extends KMLTranslatorSupport {
        public KMLRasterTranslator(ContentHandler handler) {
            super(handler);
        }

        public void encode(Object o) throws IllegalArgumentException {
            MapLayer mapLayer = (MapLayer) o;
            int mapLayerOrder = mapContext.indexOf(mapLayer);

            if ( isStandAlone() ) {
                start( "kml" );
            }
            
            //start("Document");
            //element("name", mapLayer.getTitle());

            //start the folder naming it 'layer_<mapLayerOrder>', this is 
            // necessary for a GroundOverlay
            start("Folder");
            element("name", "layer_" + mapLayerOrder);
            element("description", mapLayer.getTitle());

            start("GroundOverlay");
            //element( "name", feature.getID() );
            element("name", mapLayer.getTitle());
            element("drawOrder", Integer.toString(mapLayerOrder));

            //encode the icon
            start("Icon");

            encodeHref(mapLayer);

            element("viewRefreshMode", "never");
            element("viewBoundScale", "0.75");
            end("Icon");

            //encde the bounding box
            ReferencedEnvelope box = new ReferencedEnvelope(mapContext.getAreaOfInterest());
            boolean reprojectBBox = (box.getCoordinateReferenceSystem() != null)
            && !CRS.equalsIgnoreMetadata(box.getCoordinateReferenceSystem(), DefaultGeographicCRS.WGS84); 
            if (reprojectBBox) {
                try {
                    box = box.transform(DefaultGeographicCRS.WGS84, true);
                } catch(Exception e) {
                    throw new WmsException("Could not transform bbox to WGS84", "ReprojectionError", e);
                }
            }
            start("LatLonBox");
            element("north", Double.toString(box.getMaxY()));
            element("south", Double.toString(box.getMinY()));
            element("east", Double.toString(box.getMaxX()));
            element("west", Double.toString(box.getMinX()));
            end("LatLonBox");

            end("GroundOverlay");

            end("Folder");

            //end("Document");
            if (isStandAlone()) {
                end( "kml" );
            }
        }

        protected void encodeHref(MapLayer mapLayer) {
            if (inline) {
                //inline means reference the image "inline" as in kmz
                // use the mapLayerOrder
                int mapLayerOrder = mapContext.indexOf(mapLayer);
                element("href", "layer_" + mapLayerOrder + ".png");
            } else {
                //reference the image as a remote wms call
                element("href", KMLUtils.getMapUrl(mapContext, mapLayer, 0,  false));
            }
        }
    }
}
