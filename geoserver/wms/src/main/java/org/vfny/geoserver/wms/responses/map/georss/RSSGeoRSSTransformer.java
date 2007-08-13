/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.georss;

import org.geoserver.feature.ReprojectingFeatureCollection;
import org.geoserver.wms.util.WMSRequests;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
import org.geotools.xml.transform.Translator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.util.Requests;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.responses.featureInfo.FeatureTemplate;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;
import java.io.IOException;
import java.util.logging.Level;


/**
 * Encodes an RSS feed tagged with geo information.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class RSSGeoRSSTransformer extends GeoRSSTransformerBase {
    FeatureTemplate template = new FeatureTemplate();
        
    public Translator createTranslator(ContentHandler handler) {
        return new RSSGeoRSSTranslator(handler);
    }

    class RSSGeoRSSTranslator extends GeoRSSTranslatorSupport {
        public RSSGeoRSSTranslator(ContentHandler contentHandler) {
            super(contentHandler, null, null);
        }

        public void encode(Object o) throws IllegalArgumentException {
            WMSMapContext map = (WMSMapContext) o;

            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(null, "version", "version", null, "2.0");

            start("rss", atts);
            start("channel");

            StringBuffer title = new StringBuffer();

            for (int i = 0; i < map.getLayerCount(); i++) {
                MapLayer layer = map.getLayer(i);
                title.append(layer.getTitle()).append(",");
            }
            title.setLength(title.length()-1);
            
            element( "title", title.toString() );
            
            start( "link" );
            
            cdata(WMSRequests.getGetMapUrl(map.getRequest(),null,null,null));
            end( "link" );
            
            //element( "description", "description" );

            //items
            try {
                encodeItems(map);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            end("channel");
            end("rss");
        }

        void encodeItems(WMSMapContext map) throws IOException {
            ReferencedEnvelope mapArea = map.getAreaOfInterest();
            CoordinateReferenceSystem wgs84 = null;
            FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
            try {
                // this should never throw an exception, but we have to deal with it anyways
                wgs84 = CRS.decode("EPSG:4326");
            } catch(Exception e) {
                throw (IOException) (new IOException("Unable to decode WGS84...").initCause(e));
            }
            for (int i = 0; i < map.getLayerCount(); i++) {
                MapLayer layer = map.getLayer(i);
                DefaultQuery query = new DefaultQuery(layer.getQuery());

                FeatureCollection features = null;
                try {
                    FeatureSource source = layer.getFeatureSource();
                    
                    GeometryAttributeType at = source.getSchema().getPrimaryGeometry();
                    if(at == null) {
                        // geometryless layers...
                        features = source.getFeatures(query);
                    } else {
                        // make sure we are querying the source with the bbox in the right CRS, if
                        // not, reproject the bbox
                        ReferencedEnvelope env = new ReferencedEnvelope(mapArea);
                        CoordinateReferenceSystem sourceCRS = at.getCoordinateSystem();
                        if(sourceCRS != null && 
                            !CRS.equalsIgnoreMetadata(mapArea.getCoordinateReferenceSystem(), sourceCRS)) {
                            env = env.transform(sourceCRS, true);
                        }
                        
                        // build the mixed query
                        Filter original = query.getFilter();
                        Filter bbox = ff.bbox(at.getLocalName(), env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY(), null);
                        query.setFilter(ff.and(original, bbox));

                        // query and eventually reproject
                        features = source.getFeatures(query);
                        if(sourceCRS != null && !CRS.equalsIgnoreMetadata(wgs84, sourceCRS)) { 
                            ReprojectingFeatureCollection coll = new ReprojectingFeatureCollection(features, wgs84);
                            coll.setDefaultSource(sourceCRS);
                            features = coll;
                        }
                    }
                } catch (Exception e) {
                    String msg = "Unable to encode map layer: " + layer;
                    LOGGER.log(Level.SEVERE, msg, e);
                }

                if (features != null) {
                    FeatureIterator iterator = null;

                    try {
                        iterator = features.features();

                        while (iterator.hasNext()) {
                            encodeItem(iterator.next(), map, layer);
                        }
                    } finally {
                        if (iterator != null) {
                            features.close(iterator);
                        }
                    }
                }
            }
        }

        void encodeItem(Feature feature, WMSMapContext map, MapLayer layer)
            throws IOException {
            start("item");

            try {
                element("title", template.title(feature));
            }
            catch( Exception e ) {
                String msg = "Error occured executing title template for: " + feature.getID();
                LOGGER.log( Level.WARNING, msg, e );
            }
            
            //create the link as getFeature request with fid filter
            //TODO: throw this into a utility class
            //TODO: use an html based output format
            String link = Requests.getBaseUrl(map.getRequest().getHttpServletRequest(),
                    map.getRequest().getGeoServer());
            link += ("wfs?request=getfeature&service=wfs&version=1.0.0&featureid="
            + feature.getID());

            start("link");
            cdata(link);
            end("link");

            //element( "description", template.execute(feature));
            try {
                String description = template.description(feature);
                start("description");
                cdata(description);
                end("description");
            }
            catch( Exception e ) {
                String msg = "Error occured executing description template for: " + feature.getID();
                LOGGER.log( Level.WARNING, msg, e );
            }
            
            encodeGeometry(feature);

            end("item");
        }
    }
}
