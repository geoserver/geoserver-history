package org.geoserver.wms.responses.map.kml;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.text.cql2.CQL;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.expression.Expression;

import com.vividsolutions.jts.io.WKTReader;

public class FilterFunction_freemarkerTest extends TestCase {

    public void testTemplate() throws Exception {
        SimpleFeatureType ft = DataUtilities.createType("bridges",
                "the_geom:Point,FID:String,NAME:String");

        WKTReader reader = new WKTReader();
        SimpleFeatureBuilder sb = new SimpleFeatureBuilder(ft);
        sb.set("the_geom", reader.read("POINT(10 10)"));
        sb.set("FID", "Fid001");
        sb.set("NAME", "Pluto");
        SimpleFeature f = sb.buildFeature("fidxxx");

        String template = "Hello this is ${FID.value} my name is ${NAME.value}";
        Expression freemarker = CQL.toExpression("freemarker('" + template + "')");
        String result = freemarker.evaluate(f, String.class);
        String expected = "Hello this is " + f.getAttribute("FID") + " my name is "
                + f.getAttribute("NAME");
        assertEquals(expected, result);
    }
}
