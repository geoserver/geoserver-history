/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import junit.framework.TestCase;
import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureType;
import org.geotools.referencing.CRS;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Intersects;
import java.util.Collections;


public class ReprojectingFilterVisitorTest extends TestCase {
    FeatureType ft;
    FilterFactory2 ff;
    ReprojectingFilterVisitor reprojector;

    protected void setUp() throws Exception {
        System.setProperty("org.geotools.referencing.forceXY", "true");
        ft = DataUtilities.createType("testType",
                "geom:Point:srid=4326,line:LineString,name:String,id:int");
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
        BBOX bbox = ff.bbox(ff.property("geom"), 10, 15, 20, 25,
                "urn:x-ogc:def:crs:EPSG:6.11.2:4326");
        Filter clone = (Filter) bbox.accept(reprojector, null);
        assertNotSame(bbox, clone);

        BBOX clonedBbox = (BBOX) clone;
        assertEquals(bbox.getPropertyName(), clonedBbox.getPropertyName());
        assertTrue(10 == clonedBbox.getMinX());
        assertTrue(15 == clonedBbox.getMinY());
        assertTrue(20 == clonedBbox.getMaxX());
        assertTrue(25 == clonedBbox.getMaxY());
        assertEquals("urn:x-ogc:def:crs:EPSG:6.11.2:4326", clonedBbox.getSRS());
    }

    public void testBboxReprojectUnreferencedProperty() {
        // see if coordinates gets flipped, urn forces lat/lon interpretation
        BBOX bbox = ff.bbox(ff.property("line"), 10, 15, 20, 25,
                "urn:x-ogc:def:crs:EPSG:6.11.2:4326");
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
        LineString ls = gf.createLineString(new Coordinate[] {
                    new Coordinate(10, 15), new Coordinate(20, 25)
                });
        ls.setUserData(CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326"));

        // see if coordinates gets flipped, urn forces lat/lon interpretation
        Intersects original = ff.intersects(ff.property("geom"), ff.literal(ls));
        Filter clone = (Filter) original.accept(reprojector, null);
        assertNotSame(original, clone);

        Intersects isClone = (Intersects) clone;
        assertEquals(isClone.getExpression1(), original.getExpression1());

        LineString clonedLs = (LineString) ((Literal) isClone.getExpression2()).getValue();
        assertTrue(10 == clonedLs.getCoordinateN(0).x);
        assertTrue(15 == clonedLs.getCoordinateN(0).y);
        assertTrue(20 == clonedLs.getCoordinateN(1).x);
        assertTrue(25 == clonedLs.getCoordinateN(1).y);
        assertEquals(CRS.decode("EPSG:4326"), clonedLs.getUserData());
    }

    public void testIntersectsUnreferencedGeometry() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        LineString ls = gf.createLineString(new Coordinate[] {
                    new Coordinate(10, 15), new Coordinate(20, 25)
                });

        // see if coordinates gets flipped, urn forces lat/lon interpretation
        Intersects original = ff.intersects(ff.property("geom"), ff.literal(ls));
        Filter clone = (Filter) original.accept(reprojector, null);
        assertNotSame(original, clone);
        assertEquals(original, clone);
    }

    public void testIntersectsUnreferencedProperty() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        LineString ls = gf.createLineString(new Coordinate[] {
                    new Coordinate(10, 15), new Coordinate(20, 25)
                });
        ls.setUserData(CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326"));

        // see if coordinates gets flipped, urn forces lat/lon interpretation
        Intersects original = ff.intersects(ff.property("line"), ff.literal(ls));
        Filter clone = (Filter) original.accept(reprojector, null);
        assertNotSame(original, clone);
        assertEquals(original, clone);
    }
}
