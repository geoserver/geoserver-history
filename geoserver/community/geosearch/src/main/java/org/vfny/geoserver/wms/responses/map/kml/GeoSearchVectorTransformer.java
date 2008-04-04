package org.vfny.geoserver.wms.responses.map.kml;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.ows.util.RequestUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geotools.map.MapLayer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.xml.transform.Translator;
import org.opengis.feature.simple.SimpleFeature;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.NameSpaceInfo;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.xml.sax.ContentHandler;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class GeoSearchVectorTransformer extends KMLVectorTransformer {

    Logger LOGGER = org.geotools.util.logging.Logging
        .getLogger("org.geoserver.geosearch");

    Data catalog;
    
    public GeoSearchVectorTransformer(WMSMapContext mapContext,
            MapLayer mapLayer, Data catalog) {
        super(mapContext, mapLayer);
        this.catalog = catalog;
    }

    public Translator createTranslator(ContentHandler handler) {
        return new GeoSearchKMLTranslator(handler);
    }

    protected class GeoSearchKMLTranslator extends KMLTranslator {

        public GeoSearchKMLTranslator(ContentHandler handler) {
            super(handler);
        }

        /**
         * Encodes a KML Placemark from a feature and optional name.
         */
        protected void encodePlacemark(SimpleFeature feature,
                FeatureTypeStyle[] styles) {
            Geometry geometry = featureGeometry(feature);
            Coordinate centroid = geometryCentroid(geometry);

            start("Placemark", KMLUtils.attributes(new String[] { "id",
                        feature.getID() }));

            // encode name + description only if kmattr was specified
            if (mapContext.getRequest().getKMattr()) {
                // name
                try {
                    encodePlacemarkName(feature, styles);
                } catch (Exception e) {
                    String msg = "Error occured processing 'title' template.";
                    LOGGER.log(Level.WARNING, msg, e);
                }

                // snippet (only used by OWS5 prototype at the moment)
                try {
                    encodePlacemarkSnippet(feature, styles);
                } catch (Exception e) {
                    String msg = "Error occured processing 'description' template.";
                    LOGGER.log(Level.WARNING, msg, e);
                }

                // description
                try {
                    encodePlacemarkDescription(feature, styles);
                } catch (Exception e) {
                    String msg = "Error occured processing 'description' template.";
                    LOGGER.log(Level.WARNING, msg, e);
                }
            }
            String id[] = feature.getID().split("\\.");
            
            //get namespace prefix
            NameSpaceInfo ns = catalog.getNameSpaceFromURI( feature.getFeatureType().getName().getNamespaceURI());
            
            GetMapRequest request = mapContext.getRequest();
            
            String link = 
                RequestUtils.proxifiedBaseURL(request.getBaseUrl(), request.getGeoServer().getProxyBaseUrl());
            link = ResponseUtils.appendPath(link, "geosearch/" + ns.getPrefix() + "/" + id[0] + "/" + id[1] + ".kml" );
            
            element("link", null, KMLUtils.attributes(
                        new String[]{
                        "rel","self",
                        "href", link}));

            // look at
            encodePlacemarkLookAt(centroid);

            // time
            try {
                encodePlacemarkTime(feature, styles);
            } catch (Exception e) {
                String msg = "Error occured processing 'time' template: "
                    + e.getMessage();
                LOGGER.log(Level.WARNING, msg);
                LOGGER.log(Level.FINE, "", e);
            }

            // style reference
            element("styleUrl", "#GeoServerStyle" + feature.getID());

            // encode extended data (kml 2.2)
            encodeExtendedData(feature);

            // geometry
            encodePlacemarkGeometry(geometry, centroid, styles);

            end("Placemark");
        }

    }
}
