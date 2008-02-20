/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.featureinfo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import freemarker.template.Configuration;
import freemarker.template.Template;
import junit.framework.TestCase;
import org.geoserver.template.FeatureWrapper;
import org.geotools.data.DataUtilities;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.DefaultFeatureCollections;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.vfny.geoserver.wms.responses.featureInfo.FeatureTemplate;
import org.w3c.dom.Document;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class FeatureDescriptionTemplateTest extends TestCase {
    public void testTemplate() throws Exception {
        Configuration cfg = new Configuration();
        cfg.setObjectWrapper(new FeatureWrapper());
        cfg.setClassForTemplateLoading(FeatureTemplate.class, "");

        Template template = cfg.getTemplate("description.ftl");
        assertNotNull(template);

        //create some data
        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureType featureType = DataUtilities.createType("testType",
                "string:String,int:Integer,double:Double,geom:Point");

        SimpleFeature f = SimpleFeatureBuilder.build(featureType, new Object[] {
                    "three", new Integer(3), new Double(3.3), gf.createPoint(new Coordinate(3, 3))
                }, "fid.3");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        template.process(f, new OutputStreamWriter(output));
        template.process(f, new OutputStreamWriter(System.out));

        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = docBuilder.parse(new ByteArrayInputStream(output.toByteArray()));

        assertNotNull(document);

        assertEquals("table", document.getDocumentElement().getNodeName());
    }

    //    public void testFeatureCollection() throws Exception {
    //        Configuration cfg = new Configuration();
    //        cfg.setObjectWrapper(new FeatureWrapper());
    //        cfg.setClassForTemplateLoading(FeatureDescriptionTemplate.class, "");
    //
    //        Template template = cfg.getTemplate("description.ftl");
    //        assertNotNull(template);
    //
    //        //create some data
    //        GeometryFactory gf = new GeometryFactory();
    //        FeatureType featureType = DataUtilities.createType("testType",
    //                "string:String,int:Integer,double:Double,geom:Point");
    //
    //        DefaultFeature f = new DefaultFeature((DefaultFeatureType) featureType,
    //            new Object[] {
    //                "three", new Integer(3), new Double(3.3), gf.createPoint(new Coordinate(3, 3))
    //            }, "fid.3") {
    //        };
    //        DefaultFeature f4 = new DefaultFeature((DefaultFeatureType) featureType,
    //            new Object[] {
    //                "four", new Integer(4), new Double(4.4), gf.createPoint(new Coordinate(4, 4))
    //            }, "fid.4") {
    //        };
    //        FeatureCollection<SimpleFeatureType, SimpleFeature> features = new DefaultFeatureCollection(null,null) {};
    //        features.add( f );
    //        features.add( f4 );
    //        
    //        template.process(features, new OutputStreamWriter( System.out ));
    //
    //    }
}
