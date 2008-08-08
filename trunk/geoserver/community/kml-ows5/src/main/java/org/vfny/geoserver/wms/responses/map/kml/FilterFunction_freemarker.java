package org.vfny.geoserver.wms.responses.map.kml;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.geoserver.template.FeatureWrapper;
import org.geotools.filter.FunctionExpressionImpl;
import org.opengis.feature.simple.SimpleFeature;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class FilterFunction_freemarker extends FunctionExpressionImpl {
    private Configuration templateConfig;
    private StringTemplateLoader templateLoader;

    public FilterFunction_freemarker() {
        super("freemarker");
        // initialize the template engine, this is static to maintain a cache
        // over instantiations of kml writer
        templateConfig = new Configuration();
        templateConfig.setObjectWrapper(new FeatureWrapper());
        templateLoader = new StringTemplateLoader();
        templateConfig.setTemplateLoader(templateLoader);
    }

    @Override
    public int getArgCount() {
        return 1;
    }

    public Object evaluate(Object featureObj) {
        try {
            SimpleFeature feature = (SimpleFeature) featureObj;
            String template = getExpression(0).evaluate(feature, String.class);
    
            templateLoader.putTemplate("template", template);
            Template t = templateConfig.getTemplate("template");
            // t.setEncoding(charset.name());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Writer w = new OutputStreamWriter(bos);
            t.process(feature, w);
            return bos.toString();
        } catch(Exception e) {
            throw new RuntimeException("Issues occurred processing the template", e);
        }
    }

}
