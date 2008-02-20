/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.data.FeatureSource;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.styling.Style;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.vfny.geoserver.testdata.AbstractCiteDataTest;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.responses.map.svg.SVGMapProducer;
import java.awt.Color;
import java.io.ByteArrayOutputStream;


public class SVGMapProducerTest extends AbstractCiteDataTest {
    public void testHeterogeneousGeometry() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(new Coordinate(10, 10));
        LineString line = gf.createLineString(new Coordinate[] {
                    new Coordinate(50, 50), new Coordinate(100, 100)
                });
        Polygon polygon = gf.createPolygon(gf.createLinearRing(
                    new Coordinate[] {
                        new Coordinate(0, 0), new Coordinate(0, 200), new Coordinate(200, 200),
                        new Coordinate(200, 0), new Coordinate(0, 0)
                    }), null);

                SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
        b.setName( "test" );
        b.add("geom",Geometry.class);
        SimpleFeatureType type = b.buildFeatureType();

        SimpleFeature f1 = SimpleFeatureBuilder.build(type, new Object[] { point },null);
        SimpleFeature f2 = SimpleFeatureBuilder.build(type, new Object[] { line },null);
        SimpleFeature f3 = SimpleFeatureBuilder.build(type, new Object[] { polygon },null);

        MemoryDataStore ds = new MemoryDataStore();
        ds.createSchema(type);
        ds.addFeatures(new SimpleFeature[] { f1, f2, f3 });

        FeatureSource<SimpleFeatureType, SimpleFeature> fs = ds.getFeatureSource("test");

        final WMSMapContext map = new WMSMapContext();
        map.setAreaOfInterest(new Envelope(-250, 250, -250, 250));
        map.setMapWidth(300);
        map.setMapHeight(300);
        map.setBgColor(Color.red);
        map.setTransparent(false);

        // FilterFactory f = FilterFactory.createFilterFactory();
        // GeometryFilter filter =
        // f.createGeometryFilter(GeometryFilter.GEOMETRY_BBOX);
        //        
        // filter.addLeftGeometry(f.createAttributeExpression(type,"geom"));
        // filter.addRightGeometry(f.createBBoxExpression(new
        // Envelope(-100,100,-100,100)));
        Style basicStyle = getStyle("default.sld");
        map.addLayer(fs, basicStyle);

        SVGMapProducer producer = new SVGMapProducer();
        producer.setMapContext(map)
;        producer.produceMap();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        producer.writeTo(out);
    }
}
