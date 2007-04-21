package org.vfny.geoserver.wms.responses.map.openlayers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.map.openlayers.OpenLayersMapProducer;
import org.w3c.dom.Document;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class OpenLayersMapTemplateTest extends WMSTestSupport {

    public void test() throws Exception {
        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(OpenLayersMapProducer.class, "");
        cfg.setObjectWrapper( new BeansWrapper() );
        
        Template template = cfg.getTemplate("OpenLayersMapTemplate.ftl");
        assertNotNull(template);
        
        GetMapRequest request = createGetMapRequest( MockData.BASIC_POLYGONS );
        WMSMapContext mapContext = new WMSMapContext();
        mapContext.addLayer( createMapLayer( MockData.BASIC_POLYGONS ) );
        mapContext.setRequest( request );
        mapContext.setMapWidth( 256 );
        mapContext.setMapHeight( 256 );
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        template.process( mapContext, new OutputStreamWriter( output ) );
        
        DocumentBuilder docBuilder = 
            DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = 
            docBuilder.parse( new ByteArrayInputStream( output.toByteArray() ) );
        assertNotNull( document );
        
        assertEquals( "html", document.getDocumentElement().getNodeName() );
    }
    
    
}
