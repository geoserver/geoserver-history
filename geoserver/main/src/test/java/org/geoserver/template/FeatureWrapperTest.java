/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.template;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.feature.AttributeType;
import org.geotools.feature.DefaultFeature;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.DefaultFeatureType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleSequence;
import freemarker.template.Template;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class FeatureWrapperTest extends TestCase {
    FeatureType featureType;

    FeatureCollection features;

    Configuration cfg;

    protected void setUp() throws Exception {
        super.setUp();

        // create some data
        featureType = DataUtilities.createType("testType",
                "string:String,int:Integer,double:Double,geom:Point");

        features = createFeatureCollection(3);

        cfg = new Configuration();
        cfg.setClassForTemplateLoading(getClass(), "");
        cfg.setObjectWrapper(new FeatureWrapper());
    }

    /**
     * Creates <code>numberOfFeatures</code> features as test data according
     * to the test {@link #featureType}
     * 
     * @param numberOfFeatures
     *            how many features to create
     * @return the feature collection containing the test features
     */
    private FeatureCollection createFeatureCollection(final int numberOfFeatures) throws Exception {
        GeometryFactory gf = new GeometryFactory();
        FeatureCollection features = new DefaultFeatureCollection(null, null) {
        };
        for (int i = 1; i <= numberOfFeatures; i++) {
            Object[] attributes = new Object[4];
            attributes[0] = "name_" + i;
            attributes[1] = new Integer(1);
            attributes[2] = new Double(i + "." + i);
            attributes[3] = gf.createPoint(new Coordinate(i, i));
            String fid = "fid." + i;
            Feature feature = new DefaultFeature((DefaultFeatureType) featureType, attributes, fid) {
            };

            features.add(feature);
        }
        return features;
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

        // replace ',' with '.' for locales which use a comma for decimal point
        assertEquals("name_1\n1\n1.1\nPOINT (1 1)", out.toString().replace(',', '.'));
    }

    public void testFeatureDynamic() throws Exception {
        Template template = cfg.getTemplate("FeatureDynamic.ftl");

        StringWriter out = new StringWriter();
        template.process(features.iterator().next(), out);

        // replace ',' with '.' for locales which use a comma for decimal point
        assertEquals("string=name_1\nint=1\ndouble=1.1\ngeom=POINT (1 1)\n", out.toString()
                .replace(',', '.'));
    }
}
