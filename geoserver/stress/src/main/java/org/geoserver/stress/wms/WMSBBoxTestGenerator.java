package org.geoserver.stress.wms;

import java.io.FileWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.data.ows.Layer;
import org.geotools.data.wms.WebMapServer;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class WMSBBoxTestGenerator {
    public static void main(String[] args) throws Exception {
        URL url = new URL(
                        "http://localhost:8080/geoserver/wms?service=WMS&request=GetCapabilities");
        WebMapServer server = new WebMapServer(url);
        Layer l = getLayer(server, "topp:states");
        System.out.println(l);
        
        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(WMSBBoxTestGenerator.class, "");
        Map map = new HashMap();
        map.put("testname", "StatesWMSGetMap");
        map.put("host", url.getHost());
        map.put("port", String.valueOf(url.getPort()));
        map.put("geoserver", "geoserver"); // TODO: extract from URL
        Template template = cfg.getTemplate("wms.ftl");
        FileWriter writer = new FileWriter("plans/StatesWMSGetMap.jmx");
        template.process(map, writer);
    }

    private static Layer getLayer(WebMapServer server, String name) {
        List layers = server.getCapabilities().getLayerList();
        for (Iterator it = layers.iterator(); it.hasNext();) {
            Layer l = (Layer) it.next();
            if (l.getName() != null && l.getName().equals(name))
                return l;
        }
        return null;
    }
}
