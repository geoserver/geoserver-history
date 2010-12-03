/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.svg;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Test;

import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.geoserver.wms.WMSMapContext;
import org.geoserver.wms.WMSTestSupport;
import org.geotools.data.FeatureSource;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class SVGMapProducerTest extends WMSTestSupport {

    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new SVGMapProducerTest());
    }

    public void testHeterogeneousGeometry() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(10, 10));
        LineString line = gf.createLineString(new Coordinate[] { new Coordinate(50, 50),
                new Coordinate(100, 100) });
        Polygon polygon = gf.createPolygon(
                gf.createLinearRing(new Coordinate[] { new Coordinate(0, 0),
                        new Coordinate(0, 200), new Coordinate(200, 200), new Coordinate(200, 0),
                        new Coordinate(0, 0) }), null);

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("test");
        ftb.add("geom", Geometry.class);
        SimpleFeatureType type = ftb.buildFeatureType();

        SimpleFeature f1 = SimpleFeatureBuilder.build(type, new Object[] { point }, null);
        SimpleFeature f2 = SimpleFeatureBuilder.build(type, new Object[] { line }, null);
        SimpleFeature f3 = SimpleFeatureBuilder.build(type, new Object[] { polygon }, null);

        MemoryDataStore ds = new MemoryDataStore();
        ds.createSchema(type);
        ds.addFeatures(new SimpleFeature[] { f1, f2, f3 });

        FeatureSource fs = ds.getFeatureSource("test");

        final WMSMapContext map = new WMSMapContext();
        map.setAreaOfInterest(new ReferencedEnvelope(-250, 250, -250, 250, null));
        map.setMapWidth(300);
        map.setMapHeight(300);
        map.setBgColor(Color.red);
        map.setTransparent(false);

        Style basicStyle = getCatalog().getStyleByName("Default").getStyle();
        map.addLayer(fs, basicStyle);

        SVGStreamingMapOutputFormat producer = new SVGStreamingMapOutputFormat();
        StreamingSVGMap encodeSVG = producer.produceMap(map);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encodeSVG.encode(out);
        // System.out.println(out.toString());

        String expectedDoc = "<?xml version=\"1.0\" standalone=\"no\"?>"
                + "<svg xmlns=\"http://www.w3.org/2000/svg\" " //
                + "    xmlns:xlink=\"http://www.w3.org/1999/xlink\" " //
                + "    stroke=\"green\"  "//
                + "    fill=\"none\"  " //
                + "    stroke-width=\"0.1%\" " //
                + "    stroke-linecap=\"round\" "//
                + "    stroke-linejoin=\"round\" "//
                + "    width=\"300\"  "//
                + "    height=\"300\"  " //
                + "    viewBox=\"-250.0 -250.0 500.0 500.0\"  "//
                + "    preserveAspectRatio=\"xMidYMid meet\"> "//
                + "  <g id=\"test\" class=\"Default\"> "//
                + "    <use x=\"10\" y=\"-10\" xlink:href=\"#point\"/> "//
                + "    <path d=\"M50 -50l50 -50 \"/> "//
                + "    <path d=\"M0 0l0 -200 200 0 0 200 -200 0 Z\"/> "//
                + "  </g> "//
                + "</svg> ";

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document expected = builder.parse(new InputSource(new StringReader(expectedDoc)));
        Document result = builder.parse(new ByteArrayInputStream(out.toByteArray()));

        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        XMLUnit.setIgnoreComments(true);
        XMLAssert.assertXMLEqual(expected, result);
    }
}