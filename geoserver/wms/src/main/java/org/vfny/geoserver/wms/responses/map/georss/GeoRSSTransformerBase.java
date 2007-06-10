package org.vfny.geoserver.wms.responses.map.georss;

import java.util.logging.Logger;

import org.geotools.feature.Feature;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;

import org.xml.sax.ContentHandler;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public abstract class GeoRSSTransformerBase extends TransformerBase {

    /** logger */
    protected static Logger LOGGER = Logger.getLogger("org.geoserver.georss");
    
    /**
     * Enumeration for geometry encoding.
     * 
     * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
     *
     */
    public static class GeometryEncoding {
        
        private GeometryEncoding() {}
        
        public String getPrefix() {
            return null;
        }
        
        public String getNamespaceURI() {
            return null;
        }
        
        public void encode( Geometry g , GeoRSSTranslatorSupport translator ) {
            
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
            public void encode( Geometry g , GeoRSSTranslatorSupport t ) {
                if ( g instanceof Point ) {
                    Point p = (Point) g;
                    t.element( "georss:point", p.getX() + " " + p.getY() );
                }
                if ( g instanceof LineString ) {
                    LineString l = (LineString) g;
                    
                    StringBuffer sb = new StringBuffer();
                    for ( int i = 0; i < l.getNumPoints(); i++ ) { 
                        Coordinate c = l.getCoordinateN( i );
                        sb.append( c.x ).append( " " ).append( c.y ).append( " " );
                    }
                    sb.setLength( sb.length() - 1 );
                    
                    t.element( "georss:line", sb.toString() );
                }
                if ( g instanceof Polygon ) {
                    Polygon p = (Polygon) g;
                    LineString line = p.getExteriorRing();
                    
                    StringBuffer sb = new StringBuffer();
                    for ( int i = 0; i < line.getNumPoints(); i++ ) { 
                        Coordinate c = line.getCoordinateN( i );
                        sb.append( c.x ).append( " " ).append( c.y ).append( " " );
                    }
                    sb.setLength( sb.length() - 1 );
                    
                    t.element( "georss:polygon", sb.toString() );
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
                t.element( "geo:lat", ""+p.getY() );
                t.element( "geo:long", ""+p.getX() );
            }
        };
        
    };
    
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
        protected void encodeGeometry( Feature feature ) {
            if ( feature.getDefaultGeometry() != null ) {
                Geometry g = feature.getDefaultGeometry();
                
                //handle case of multi geometry with a single geometry in it
                if ( g instanceof GeometryCollection ) {
                    GeometryCollection mg = (GeometryCollection) g;
                    if ( mg.getNumGeometries() == 1 ) {
                        g = mg.getGeometryN(0);
                    }
                }
                
                geometryEncoding.encode( g, this );
            }  
        }
        
        //overrides to increase visiblity
        public void start(String element) {
            super.start(element);
        }
        public void element(String element, String content) {
            super.element(element, content);
        }
    }
}
