package org.vfny.geoserver.wms.responses.map.ajax;

import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.vfny.geoserver.wms.responses.map.openlayers.OpenLayersMapProducer;

import junit.framework.TestCase;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public class OpenLayersMapTemplateTest extends TestCase {

    public void test() throws Exception {
        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(OpenLayersMapProducer.class, "");
        cfg.setObjectWrapper( new BeansWrapper() );
        
        Template template = cfg.getTemplate("OpenLayersMapTemplate.ftl");
        assertNotNull(template);
        
    }
    
    
}
