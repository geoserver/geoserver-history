package org.geoserver.stress.wms;

import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.data.ows.CRSEnvelope;
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
        String srs = getSRS(l);
        
        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(WMSBBoxTestGenerator.class, "");
        Map map = new HashMap();
        map.put("testname", "StatesWMSGetMap");
        map.put("host", url.getHost());
        map.put("port", String.valueOf(url.getPort()));
        map.put("geoserver", "geoserver"); // TODO: extract from URL
        map.put("numThreads", "${__property(threadcount)}"); // gather property threadcount
        map.put("crs", srs); 
        map.put("format", "image/png"); 
        map.put("styles", ""); 
        map.put("layers", l.getName());
        map.put("bboxes", generateRandomBoxes(l, srs, 50));
        Template template = cfg.getTemplate("wms.ftl");
        FileWriter writer = new FileWriter("plans/StatesWMSGetMap.jmx");
        template.process(map, writer);
    }

    private static List generateRandomBoxes(Layer l, String srs, int count) {
        List result = new ArrayList(count);
        CRSEnvelope ce = (CRSEnvelope) l.getBoundingBoxes().get(srs);
        for (int i = 0; i < count; i++) {
           Map aBox = new HashMap();
           double x1 = Math.random() * 0.8 * (ce.getMaxX() - ce.getMinX()) + ce.getMinX();
           double y1 = Math.random() * 0.8 * (ce.getMaxY() - ce.getMinY()) + ce.getMinY();
           double x2 = (0.2 + Math.random() * 0.8) * (ce.getMaxX() - x1) + x1;
           double y2 = (0.2 + Math.random() * 0.8) * (ce.getMaxY() - y1) + y1;
           aBox.put("bbox", x1 + "," + y1 + "," + x2 + "," + y2);
           aBox.put("width", new Integer(500));
           aBox.put("height", new Integer((int) (500 / (x2 - x1) * (y2 - y1))));
           result.add(aBox);
        }
        return result;
    }

    private static String getSRS(Layer l) {
        HashMap bboxes = l.getBoundingBoxes();
        if(bboxes.size() == 0)
            return "EPSG:4326";
        else
            return (String) bboxes.keySet().iterator().next();
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
