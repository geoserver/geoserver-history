/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.georss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geoserver.feature.ReprojectingFeatureCollection;
import org.geotools.data.DefaultQuery;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapLayer;
import org.geotools.referencing.CRS;
import org.geotools.xml.transform.TransformerBase;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.wms.WMSMapContext;
import org.xml.sax.ContentHandler;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


public abstract class GeoRSSTransformerBase extends TransformerBase {
    /** logger */
    protected static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.georss");

    /**
     * Enumeration for geometry encoding.
     *
     * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
     *
     */
    public static class GeometryEncoding {
        private GeometryEncoding() {
        }

        public String getPrefix() {
            return null;
        }

        public String getNamespaceURI() {
            return null;
        }

        public void encode(Geometry g, GeoRSSTranslatorSupport translator) {
        }

        /**
         * "simple" encoding:
         *
         *  ex:
         *  <georss:point>45.256 -71.92</georss:point>,<georss:line>...</georss:line>,...
         */
        public static GeometryEncoding SIMPLE = new GeometryEncoding() {
                public String getPrefix() {
                    return "georss";
                }

                public String getNamespaceURI() {
                    return "http://www.georss.org/georss";
                }

                public void encode(Geometry g, GeoRSSTranslatorSupport t) {
                    if (g instanceof Point) {
                        Point p = (Point) g;
                        t.element("georss:point", p.getY() + " " + p.getX());
                    }

                    if (g instanceof LineString) {
                        LineString l = (LineString) g;

                        StringBuffer sb = new StringBuffer();

                        for (int i = 0; i < l.getNumPoints(); i++) {
                            Coordinate c = l.getCoordinateN(i);
                            sb.append(c.y).append(" ").append(c.x).append(" ");
                        }

                        sb.setLength(sb.length() - 1);

                        t.element("georss:line", sb.toString());
                    }

                    if (g instanceof Polygon) {
                        Polygon p = (Polygon) g;
                        LineString line = p.getExteriorRing();

                        StringBuffer sb = new StringBuffer();

                        for (int i = 0; i < line.getNumPoints(); i++) {
                            Coordinate c = line.getCoordinateN(i);
                            sb.append(c.y).append(" ").append(c.x).append(" ");
                        }

                        sb.setLength(sb.length() - 1);

                        t.element("georss:polygon", sb.toString());
                    }
                }
            };

        /**
         * gml encoding:
         *
         * ex:
         * <gml:Point>
         *   <gml:pos>45.256 -71.92</gml:pos>
         * </gml:Point>
         */
        public static GeometryEncoding GML = new GeometryEncoding() {
                public String getPrefix() {
                    return "gml";
                }

                public String getNamespaceURI() {
                    return "http://www.opengis.net/gml";
                }
            };

        /**
         * lat/long encoding:
         *
         * ex:
         * <geo:lat>45.256</geo:lat>
         * <geo:long>-71.92</geo:long>
         *
         */
        public static GeometryEncoding LATLONG = new GeometryEncoding() {
                public String getPrefix() {
                    return "geo";
                }

                public String getNamespaceURI() {
                    return "http://www.w3.org/2003/01/geo/wgs84_pos#";
                }

                public void encode(Geometry g, GeoRSSTranslatorSupport t) {
                    //encode the centroid
                    Point p = g.getCentroid();
                    t.element("geo:lat", "" + p.getY());
                    t.element("geo:long", "" + p.getX());
                }
            };
    }
    ;

    /**
     * Geometry encoding to use.
     */
    protected GeometryEncoding geometryEncoding = GeometryEncoding.LATLONG;

    public void setGeometryEncoding(GeometryEncoding geometryEncoding) {
        this.geometryEncoding = geometryEncoding;
    }

    abstract class GeoRSSTranslatorSupport extends TranslatorSupport {
        public GeoRSSTranslatorSupport(ContentHandler contentHandler, String prefix, String nsURI) {
            super(contentHandler, prefix, nsURI);

            nsSupport.declarePrefix(geometryEncoding.getPrefix(), geometryEncoding.getNamespaceURI());
        }

        /**
         * Encodes the geometry of a feature.
         *
         */
        protected void encodeGeometry(SimpleFeature feature) {
            if (feature.getDefaultGeometry() != null) {
                Geometry g = (Geometry) feature.getDefaultGeometry();

                //handle case of multi geometry with a single geometry in it
                if (g instanceof GeometryCollection) {
                    GeometryCollection mg = (GeometryCollection) g;

                    if (mg.getNumGeometries() == 1) {
                        g = mg.getGeometryN(0);
                    }
                }

                geometryEncoding.encode(g, this);
            }
        }

        //overrides to increase visiblity
        public void start(String element) {
            super.start(element);
        }

        public void element(String element, String content) {
            super.element(element, content);
        }
        
        @SuppressWarnings("unchecked")
        protected List loadFeatureCollections(WMSMapContext map) throws IOException {
            ReferencedEnvelope mapArea = map.getAreaOfInterest();
            CoordinateReferenceSystem wgs84 = null;
            FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
            try {
                // this should never throw an exception, but we have to deal with it anyways
                wgs84 = CRS.decode("EPSG:4326");
            } catch(Exception e) {
                throw (IOException) (new IOException("Unable to decode WGS84...").initCause(e));
            }
            
            List featureCollections = new ArrayList();
            for (int i = 0; i < map.getLayerCount(); i++) {
                MapLayer layer = map.getLayer(i);
                DefaultQuery query = new DefaultQuery(layer.getQuery());

                SimpleFeatureCollection features = null;
                try {
                    SimpleFeatureSource source;
                    source = (SimpleFeatureSource) layer.getFeatureSource();
                    
                    GeometryDescriptor gd = source.getSchema().getGeometryDescriptor();
                    if(gd == null) {
                        // geometryless layers...
                        features = source.getFeatures(query);
                    } else {
                        // make sure we are querying the source with the bbox in the right CRS, if
                        // not, reproject the bbox
                        ReferencedEnvelope env = new ReferencedEnvelope(mapArea);
                        CoordinateReferenceSystem sourceCRS = gd.getCoordinateReferenceSystem();
                        if(sourceCRS != null && 
                            !CRS.equalsIgnoreMetadata(mapArea.getCoordinateReferenceSystem(), sourceCRS)) {
                            env = env.transform(sourceCRS, true);
                        }
                        
                        // build the mixed query
                        Filter original = query.getFilter();
                        Filter bbox = ff.bbox(gd.getLocalName(), env.getMinX(), env.getMinY(), env.getMaxX(), env.getMaxY(), null);
                        query.setFilter(ff.and(original, bbox));

                        // query and eventually reproject
                        features = source.getFeatures(query);
                        if(sourceCRS != null && !CRS.equalsIgnoreMetadata(wgs84, sourceCRS)) { 
                            ReprojectingFeatureCollection coll = new ReprojectingFeatureCollection(features, wgs84);
                            coll.setDefaultSource(sourceCRS);
                            features = coll;
                        }
                        
                        if (features == null) 
                            throw new NullPointerException();
                        
                        featureCollections.add(features);
                       
                    }
                } catch (Exception e) {
                    String msg = "Unable to encode map layer: " + layer;
                    LOGGER.log(Level.SEVERE, msg, e);
                }
            }

            return featureCollections;
        }
        
    }
}
