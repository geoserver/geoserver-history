package org.geoserver.stress.wms;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.wms.WebMapServer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Generates a load test based on a layer
 * TODO: split out a generic class, allow for multiple layers to be specified, and maybe
 * add variant generation (that is, dump multiple times the same template but with a slight
 * variation, such as the output format).
 * @author Andrea Aime - TOPP
 *
 */
public class WMSBBoxTestGenerator {
    public static void main(String[] args) throws Exception {
        int loopCount = 30;
        URL url = new URL("http://localhost:8080/geoserver/wms?service=WMS&request=GetCapabilities");
        WebMapServer server = new WebMapServer(url);
        Layer l = getLayer(server, "topp:states");
        String srs = getSRS(l);

        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(WMSBBoxTestGenerator.class, "");
        Map map = new HashMap();
        String planName = "StatesWMSGetMap";
        map.put("testname", planName);
        // __P(x,default) returns variable x if available, otherwise falls back
        // on the default
        map.put("host", "${__P(host," + url.getHost() + ")}");
        map.put("port", "${__P(port," + url.getPort() + ")}");
        map.put("path", "${__P(path," + "/geoserver" + ")}"); 
        map.put("numThreads", "${__P(threadcount,1)}");
        map.put("crs", srs);
        map.put("format", "image/png");
        map.put("styles", "");
        map.put("layers", l.getName());
        map.put("csvFileName", "./" + planName + ".csv");
        map.put("loopCount", new Integer(loopCount));

        Template template = cfg.getTemplate("wms.ftl");
        Writer templateWriter = new BufferedWriter(new FileWriter("plans/" + planName + ".jmx"));
        BufferedWriter csvWriter = new BufferedWriter(new FileWriter("plans/" + planName + ".csv"));
        template.process(map, templateWriter);
        generateRandomBoxes(l, srs, loopCount, csvWriter);
    }

    private static void generateRandomBoxes(Layer l, String srs, int count, Writer writer)
            throws IOException {
        try {
            CRSEnvelope ce = (CRSEnvelope) l.getBoundingBoxes().get(srs);
            for (int i = 0; i < count; i++) {
                double x1 = Math.random() * 0.8 * (ce.getMaxX() - ce.getMinX()) + ce.getMinX();
                double y1 = Math.random() * 0.8 * (ce.getMaxY() - ce.getMinY()) + ce.getMinY();
                double x2 = (0.2 + Math.random() * 0.8) * (ce.getMaxX() - x1) + x1;
                double y2 = (0.2 + Math.random() * 0.8) * (ce.getMaxY() - y1) + y1;
                int width = 500;
                int height = (int) (width / (x2 - x1) * (y2 - y1));
                writer.write(x1 + "," + y1 + "," + x2 + "," + y2 + ";" + width + ";" + height
                        + "\n");
            }
        } finally {
            writer.close();
        }
    }

    private static String getSRS(Layer l) {
        HashMap bboxes = l.getBoundingBoxes();
        if (bboxes.size() == 0)
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
