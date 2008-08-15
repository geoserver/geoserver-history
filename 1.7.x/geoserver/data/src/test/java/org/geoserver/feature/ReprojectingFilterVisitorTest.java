package org.geoserver.feature;

import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Intersects;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public class ReprojectingFilterVisitorTest extends TestCase {

    SimpleFeatureType ft;
    FilterFactory2 ff;
    ReprojectingFilterVisitor reprojector;

    protected void setUp() throws Exception {
        System.setProperty("org.geotools.referencing.forceXY", "true");
        ft = DataUtilities.createType("testType", "geom:Point:srid=4326,line:LineString,name:String,id:int");
        ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        reprojector = new ReprojectingFilterVisitor(ff, ft);
    }
    

    /**
     * Make sure it does not break with non spatial filters
     */
    public void testNoProjection() {
        Filter idFilter = ff.id(Collections.singleton(ff.featureId("testType:1")));
        Filter clone = (Filter) idFilter.accept(reprojector, null);
        assertNotSame(idFilter, clone);
        assertEquals(idFilter, clone);
    }
    
    public void testBboxNoReprojection() {
        // no reprojection needed in fact
        Filter bbox = ff.bbox(ff.property("geom"), 10, 10, 20, 20, "EPSG:4326");
        Filter clone = (Filter) bbox.accept(reprojector, null);
        assertNotSame(bbox, clone);
        assertEquals(bbox, clone);
    }
    
    public void testBboxReproject() {
        // see if coordinates gets flipped, urn forces lat/lon interpretation
        BBOX bbox = ff.bbox(ff.property("geom"), 10, 15, 20, 25, "urn:x-ogc:def:crs:EPSG:6.11.2:4326");
        Filter clone = (Filter) bbox.accept(reprojector, null);
        assertNotSame(bbox, clone);
        BBOX clonedBbox = (BBOX) clone;
        assertEquals(bbox.getPropertyName(), clonedBbox.getPropertyName());
        assertTrue(15 == clonedBbox.getMinX());
        assertTrue(10 == clonedBbox.getMinY());
        assertTrue(25 == clonedBbox.getMaxX());
        assertTrue(20 == clonedBbox.getMaxY());
        assertEquals("EPSG:4326", clonedBbox.getSRS());
    }
    
    public void testBboxReprojectUnreferencedProperty() {
        // see if coordinates gets flipped, urn forces lat/lon interpretation
        BBOX bbox = ff.bbox(ff.property("line"), 10, 15, 20, 25, "urn:x-ogc:def:crs:EPSG:6.11.2:4326");
        Filter clone = (Filter) bbox.accept(reprojector, null);
        assertNotSame(bbox, clone);
        assertEquals(bbox, clone);
    }
    
    public void testBboxReprojectUnreferencedBBox() {
        // see if coordinates gets flipped, urn forces lat/lon interpretation
        BBOX bbox = ff.bbox(ff.property("geom"), 10, 15, 20, 25, null);
        Filter clone = (Filter) bbox.accept(reprojector, null);
        assertNotSame(bbox, clone);
        assertEquals(bbox, clone);
    }
    
    public void testIntersectsReproject() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        LineString ls = gf.createLineString(new Coordinate[] {new Coordinate(10, 15), new Coordinate(20, 25)});
        ls.setUserData(CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326"));
        
        // see if coordinates gets flipped, urn forces lat/lon interpretation
        Intersects original = ff.intersects(ff.property("geom"), ff.literal(ls));
        Filter clone = (Filter) original.accept(reprojector, null);
        assertNotSame(original, clone);
        Intersects isClone = (Intersects) clone;
        assertEquals(isClone.getExpression1(), original.getExpression1());
        LineString clonedLs = (LineString) ((Literal) isClone.getExpression2()).getValue();
        assertTrue(15 == clonedLs.getCoordinateN(0).x);
        assertTrue(10 == clonedLs.getCoordinateN(0).y);
        assertTrue(25 == clonedLs.getCoordinateN(1).x);
        assertTrue(20 == clonedLs.getCoordinateN(1).y);
        assertEquals(CRS.decode("EPSG:4326"), clonedLs.getUserData());
    }
    
    public void testIntersectsUnreferencedGeometry() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        LineString ls = gf.createLineString(new Coordinate[] {new Coordinate(10, 15), new Coordinate(20, 25)});
        
        // see if coordinates gets flipped, urn forces lat/lon interpretation
        Intersects original = ff.intersects(ff.property("geom"), ff.literal(ls));
        Filter clone = (Filter) original.accept(reprojector, null);
        assertNotSame(original, clone);
        assertEquals(original, clone);
    }
    
    public void testIntersectsUnreferencedProperty() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        LineString ls = gf.createLineString(new Coordinate[] {new Coordinate(10, 15), new Coordinate(20, 25)});
        ls.setUserData(CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326"));
        
        // see if coordinates gets flipped, urn forces lat/lon interpretation
        Intersects original = ff.intersects(ff.property("line"), ff.literal(ls));
        Filter clone = (Filter) original.accept(reprojector, null);
        assertNotSame(original, clone);
        assertEquals(original, clone);
    }
    
    public void testPropertyEqualsFirstArgumentNotPropertyName() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        LineString ls = gf.createLineString(new Coordinate[] {new Coordinate(10, 15), new Coordinate(20, 25)});
        ls.setUserData(CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326"));
        
        // make sure a class cast does not occur, see: http://jira.codehaus.org/browse/GEOS-1860
        Function function = ff.function("geometryType", ff.property("geom"));
        PropertyIsEqualTo original = ff.equals(ff.literal("Point"), function);
        Filter clone = (Filter) original.accept(reprojector, null);
        assertNotSame(original, clone);
        assertEquals(original, clone);

        // try the opposite, literal and function
        original = ff.equals(function, ff.literal("Point"));
        clone = (Filter) original.accept(reprojector, null);
        assertNotSame(original, clone);
        assertEquals(original, clone);
    }
    
    public void testIntersectsWithFunction() throws Exception {
        Function function = new GeometryFunction();
        
        // see if coordinates gets flipped, urn forces lat/lon interpretation
        Intersects original = ff.intersects(ff.property("geom"), function);
        Filter clone = (Filter) original.accept(reprojector, null);
        assertNotSame(original, clone);
        Intersects isClone = (Intersects) clone;
        assertEquals(isClone.getExpression1(), original.getExpression1());
        LineString clonedLs = (LineString) isClone.getExpression2().evaluate(null);
        assertTrue(15 == clonedLs.getCoordinateN(0).x);
        assertTrue(10 == clonedLs.getCoordinateN(0).y);
        assertTrue(25 == clonedLs.getCoordinateN(1).x);
        assertTrue(20 == clonedLs.getCoordinateN(1).y);
        assertEquals(CRS.decode("EPSG:4326"), clonedLs.getUserData());
    }
    
    public void testPropertyEqualWithFunction() throws Exception {
        Function function = new GeometryFunction();
        
        // see if coordinates gets flipped, urn forces lat/lon interpretation
        PropertyIsEqualTo original = ff.equals(ff.property("geom"), function);
        PropertyIsEqualTo clone = (PropertyIsEqualTo) original.accept(reprojector, null);
        assertNotSame(original, clone);
        assertEquals(clone.getExpression1(), original.getExpression1());
        LineString clonedLs = (LineString) clone.getExpression2().evaluate(null);
        assertTrue(15 == clonedLs.getCoordinateN(0).x);
        assertTrue(10 == clonedLs.getCoordinateN(0).y);
        assertTrue(25 == clonedLs.getCoordinateN(1).x);
        assertTrue(20 == clonedLs.getCoordinateN(1).y);
        assertEquals(CRS.decode("EPSG:4326"), clonedLs.getUserData());
    }
    
    
    
    
    

    private final class GeometryFunction implements Function {
        final LineString ls;
        
        
        public GeometryFunction() throws Exception {
            GeometryFactory gf = new GeometryFactory();
            ls = gf.createLineString(new Coordinate[] {new Coordinate(10, 15), new Coordinate(20, 25)});
            ls.setUserData(CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326"));
        }

        public String getName() {
            return "function";
        }

        public List<Expression> getParameters() {
            return Collections.EMPTY_LIST;
        }

        public Object accept(ExpressionVisitor visitor, Object extraData) {
            return visitor.visit( this, extraData ); 
        }

        public Object evaluate(Object object) {
            return ls;
        }

        public <T> T evaluate(Object object, Class<T> context) {
            return (T) ls;
        }

        public Literal getFallbackValue() {
            return null;
        }
    }
}
