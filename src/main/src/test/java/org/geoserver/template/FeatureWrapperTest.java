/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.template;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import freemarker.template.Configuration;
import freemarker.template.SimpleObjectWrapper;
import freemarker.template.Template;
import junit.framework.TestCase;
import org.geotools.data.DataUtilities;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;


public class FeatureWrapperTest extends TestCase {
    FeatureCollection<SimpleFeatureType, SimpleFeature> features;
    Configuration cfg;

    protected void setUp() throws Exception {
        super.setUp();

        //create some data
        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureType featureType = DataUtilities.createType("testType",
                "string:String,int:Integer,double:Double,geom:Point");

        features = new DefaultFeatureCollection(null, null) {};
        features.add(
            SimpleFeatureBuilder.build(featureType, new Object[] {
                "one", new Integer(1), new Double(1.1), gf.createPoint(new Coordinate(1, 1))
            }, "fid.1")
        );
        features.add(
            SimpleFeatureBuilder.build(featureType, new Object[] {
                "two", new Integer(2), new Double(2.2), gf.createPoint(new Coordinate(2, 2))
            }, "fid.2")
        );
        features.add(
            SimpleFeatureBuilder.build(featureType, new Object[] {
                "three", new Integer(3), new Double(3.3), gf.createPoint(new Coordinate(3, 3))
            }, "fid.3")
        );
        cfg = new Configuration();
        cfg.setClassForTemplateLoading(getClass(), "");
        cfg.setObjectWrapper(new FeatureWrapper());
    }

    public void testFeatureCollection() throws Exception {
        Template template = cfg.getTemplate("FeatureCollection.ftl");

        StringWriter out = new StringWriter();
        template.process(features, out);
        assertEquals("fid.1\nfid.2\nfid.3\n", out.toString());
    }

    public void testFeatureSimple() throws Exception {
        Template template = cfg.getTemplate("FeatureSimple.ftl");

        StringWriter out = new StringWriter();
        template.process(features.iterator().next(), out);

        //replace ',' with '.' for locales which use a comma for decimal point
        assertEquals("one\n1\n1.1\nPOINT (1 1)", out.toString().replace(',', '.'));
    }

    public void testFeatureDynamic() throws Exception {
        Template template = cfg.getTemplate("FeatureDynamic.ftl");

        StringWriter out = new StringWriter();
        template.process(features.iterator().next(), out);

        //replace ',' with '.' for locales which use a comma for decimal point
        assertEquals("string=one\nint=1\ndouble=1.1\ngeom=POINT (1 1)\n",
            out.toString().replace(',', '.'));
    }
}
