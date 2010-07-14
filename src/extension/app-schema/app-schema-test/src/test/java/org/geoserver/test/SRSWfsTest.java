/*
 * Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import junit.framework.Test;

import org.geotools.factory.Hints;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.w3c.dom.Document;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * This is to test encoding of SRS information and reprojection values in app-schema features.
 * 
 * @author Rini Angreani, Curtin University of Technology
 */
public class SRSWfsTest extends AbstractAppSchemaWfsTestSupport {

    final String EPSG_4326 = "urn:x-ogc:def:crs:EPSG:4326";

    final String EPSG_4283 = "urn:x-ogc:def:crs:EPSG:4283";

    final String DIMENSION = "2";

    /**
     * Read-only test so can use one-time setup.
     * 
     * @return
     */
    public static Test suite() {
        Test test = new OneTimeTestSetup(new SRSWfsTest());
        return test;
    }

    @Override
    protected NamespaceTestData buildTestData() {
        return new SRSMockData();
    }

    public SRSWfsTest() {
        // use the OGC standard for axis order
        Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, false);
    }
    
    @Override
    public void oneTimeTearDown() throws Exception {
        // reset system default
        Hints.removeSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        super.oneTimeTearDown();
    }

    /**
     * Test content of GetFeature response.
     */
    public void testGetFeatureContent() {
        String id = "1";

        Document doc = getAsDOM("wfs?request=GetFeature&typename=ex:geomContainer");
        LOGGER.info("WFS GetFeature&typename=ex:geomContainer response:\n" + prettyString(doc));
        assertXpathEvaluatesTo("2", "/wfs:FeatureCollection/@numberOfFeatures", doc);
        assertXpathCount(2, "//ex:geomContainer", doc);

        // 1st feature
        assertXpathEvaluatesTo(id, "(//ex:geomContainer)[1]/@gml:id", doc);
        // check srs properties
        assertXpathEvaluatesTo(EPSG_4283, "(//ex:geomContainer)[1]/ex:geom/gml:Polygon/@srsName", doc);
// TODO: Uncomment these lines when GEOT-2639 is fixed
//        assertXpathEvaluatesTo(DIMENSION,
//                "(//ex:geomContainer)[1]/ex:geom/gml:Polygon/@srsDimension", doc);
        // test geometry values
        assertXpathEvaluatesTo(
                "-1.2 52.5 -1.2 52.6 -1.1 52.6 -1.1 52.5 -1.2 52.5",
                "(//ex:geomContainer)[1]/ex:geom/gml:Polygon/gml:exterior/gml:LinearRing/gml:posList",
                doc);
        // test nested geometry
        assertXpathEvaluatesTo("nested.2",
                "(//ex:geomContainer)[1]/ex:nestedFeature/ex:nestedGeom/@gml:id", doc);
        assertXpathEvaluatesTo(EPSG_4283,
                "(//ex:geomContainer)[1]/ex:nestedFeature/ex:nestedGeom/ex:geom/gml:Point/@srsName",
                doc);
// TODO: Uncomment these lines when GEOT-2639 is fixed
//        assertXpathEvaluatesTo(
//                DIMENSION,
//                "(//ex:geomContainer)[1]/ex:nestedFeature/ex:nestedGeom/ex:geom/gml:Point/@srsDimension",
//                doc);
        assertXpathEvaluatesTo("42.58 31.29",
                "(//ex:geomContainer)[1]/ex:nestedFeature/ex:nestedGeom/ex:geom/gml:Point/gml:pos",
                doc);

        // second feature
        id = "2";
        assertXpathEvaluatesTo(id, "(//ex:geomContainer)[2]/@gml:id", doc);
        // check srs properties
        assertXpathEvaluatesTo(EPSG_4283, "(//ex:geomContainer)[2]/ex:geom/gml:Point/@srsName", doc);
// TODO: Uncomment these lines when GEOT-2639 is fixed
//        assertXpathEvaluatesTo(DIMENSION, "(//ex:geomContainer)[2]/ex:geom/gml:Point/@srsDimension",
//                doc);
        // test geometry values
        assertXpathEvaluatesTo("42.58 31.29", "(//ex:geomContainer)[2]/ex:geom/gml:Point/gml:pos",
                doc);
        // test nested geometry
        assertXpathEvaluatesTo("nested.1",
                "(//ex:geomContainer)[2]/ex:nestedFeature/ex:nestedGeom/@gml:id", doc);
        assertXpathEvaluatesTo(
                EPSG_4283,
                "(//ex:geomContainer)[2]/ex:nestedFeature/ex:nestedGeom/ex:geom/gml:Polygon/@srsName",
                doc);
// TODO: Uncomment these lines when GEOT-2639 is fixed
//        assertXpathEvaluatesTo(
//                DIMENSION,
//                "(//ex:geomContainer)[2]/ex:nestedFeature/ex:nestedGeom/ex:geom/gml:Polygon/@srsDimension",
//                doc);
        assertXpathEvaluatesTo(
                "-1.2 52.5 -1.2 52.6 -1.1 52.6 -1.1 52.5 -1.2 52.5",
                "(//ex:geomContainer)[2]/ex:nestedFeature/ex:nestedGeom/ex:geom/gml:Polygon/gml:exterior/gml:LinearRing/gml:posList",
                doc);
    }

    /**
     * Test SRS reprojection. Ensure both geometry values and SRS information are encoded correctly.
     * 
     * @throws NoSuchAuthorityCodeException
     * @throws FactoryException
     * @throws MismatchedDimensionException
     * @throws TransformException
     */
    public void testReproject() throws NoSuchAuthorityCodeException, FactoryException,
            MismatchedDimensionException, TransformException {

        // reprojected geometries
        CoordinateReferenceSystem sourceCRS = (CoordinateReferenceSystem) CRS.decode(EPSG_4283);
        CoordinateReferenceSystem targetCRS = (CoordinateReferenceSystem) CRS.decode(EPSG_4326);
        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
        GeometryFactory factory = new GeometryFactory();
        Polygon srcPolygon = factory.createPolygon(factory.createLinearRing(factory
                .getCoordinateSequenceFactory().create(
                        new Coordinate[] { new Coordinate(-1.2, 52.5), new Coordinate(-1.2, 52.6),
                                new Coordinate(-1.1, 52.6), new Coordinate(-1.1, 52.5),
                                new Coordinate(-1.2, 52.5) })), null);
        Polygon targetPolygon = (Polygon) JTS.transform(srcPolygon, transform);
        StringBuffer polygonBuffer = new StringBuffer();
        for (Coordinate coord : targetPolygon.getCoordinates()) {
            polygonBuffer.append(coord.x).append(" ");
            polygonBuffer.append(coord.y).append(" ");
        }
        String targetPolygonCoords = polygonBuffer.toString().trim();
        Point targetPoint = (Point) JTS.transform(
                factory.createPoint(new Coordinate(42.58, 31.29)), transform);
        String targetPointCoord = targetPoint.getCoordinate().x + " "
                + targetPoint.getCoordinate().y;

        Document doc = getAsDOM("wfs?request=GetFeature&typename=ex:geomContainer&srsname=urn:x-ogc:def:crs:EPSG::4326");
        LOGGER
                .info("WFS GetFeature&typename=ex:geomContainer&srsname=urn:x-ogc:def:crs:EPSG::4326 response:\n"
                        + prettyString(doc));
        assertXpathEvaluatesTo("2", "/wfs:FeatureCollection/@numberOfFeatures", doc);
        assertXpathCount(2, "//ex:geomContainer", doc);

        // 1st feature
        String id = "1";
        assertXpathEvaluatesTo(id, "(//ex:geomContainer)[1]/@gml:id", doc);
        // check srs properties
        assertXpathEvaluatesTo(EPSG_4326, "//ex:geomContainer[1]/ex:geom/gml:Polygon/@srsName", doc);
// TODO: Uncomment these lines when GEOT-2707 is fixed
//        assertXpathEvaluatesTo(DIMENSION,
//                "(//ex:geomContainer)[1]/ex:geom/gml:Polygon/@srsDimension", doc);
        // test values
        assertXpathEvaluatesTo(
                targetPolygonCoords,
                "(//ex:geomContainer)[1]/ex:geom/gml:Polygon/gml:exterior/gml:LinearRing/gml:posList",
                doc);
        // test nested geometry
        assertXpathEvaluatesTo("nested.2",
                "(//ex:geomContainer)[1]/ex:nestedFeature/ex:nestedGeom/@gml:id", doc);
        assertXpathEvaluatesTo(EPSG_4326,
                "(//ex:geomContainer)[1]/ex:nestedFeature/ex:nestedGeom/ex:geom/gml:Point/@srsName",
                doc);
// TODO: Uncomment these lines when GEOT-2707 is fixed
//        assertXpathEvaluatesTo(
//                DIMENSION,
//                "(//ex:geomContainer)[1]/ex:nestedFeature/ex:nestedGeom/ex:geom/gml:Point/@srsDimension",
//                doc);
        assertXpathEvaluatesTo(targetPointCoord,
                "(//ex:geomContainer)[1]/ex:nestedFeature/ex:nestedGeom/ex:geom/gml:Point/gml:pos",
                doc);

        // second feature
        id = "2";
        assertXpathEvaluatesTo(id, "(//ex:geomContainer)[2]/@gml:id", doc);
        // check srs properties
        assertXpathEvaluatesTo(EPSG_4326, "(//ex:geomContainer)[2]/ex:geom/gml:Point/@srsName", doc);
// TODO: Uncomment these lines when GEOT-2707 is fixed
//        assertXpathEvaluatesTo(DIMENSION, "(//ex:geomContainer)[2]/ex:geom/gml:Point/@srsDimension",
//                doc);
        // test values
        assertXpathEvaluatesTo(targetPointCoord, "(//ex:geomContainer)[2]/ex:geom/gml:Point/gml:pos",
                doc);
        // test nested geometry
        assertXpathEvaluatesTo("nested.1",
                "(//ex:geomContainer)[2]/ex:nestedFeature/ex:nestedGeom/@gml:id", doc);
        assertXpathEvaluatesTo(
                EPSG_4326,
                "(//ex:geomContainer)[2]/ex:nestedFeature/ex:nestedGeom/ex:geom/gml:Polygon/@srsName",
                doc);
// TODO: Uncomment these lines when GEOT-2707 is fixed
//        assertXpathEvaluatesTo(
//                DIMENSION,
//                "(//ex:geomContainer)[2]/ex:nestedFeature/ex:nestedGeom/ex:geom/gml:Polygon/@srsDimension",
//                doc);
        assertXpathEvaluatesTo(
                targetPolygonCoords,
                "(//ex:geomContainer)[2]/ex:nestedFeature/ex:nestedGeom/ex:geom/gml:Polygon/gml:exterior/gml:LinearRing/gml:posList",
                doc);
    }

    /**
     * Ensure filters are still working.
     */
    public void testFilters() {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=ex:geomContainer&srsname=urn:x-ogc:def:crs:EPSG::4326&featureid=1");
        LOGGER
                .info("WFS GetFeature&typename=ex:geomContainer&srsname=urn:x-ogc:def:crs:EPSG::4326&featureid=1"
                        + "response:\n" + prettyString(doc));
        assertXpathEvaluatesTo("1", "/wfs:FeatureCollection/@numberOfFeatures", doc);
        assertXpathCount(1, "//ex:geomContainer", doc);
    }
}
