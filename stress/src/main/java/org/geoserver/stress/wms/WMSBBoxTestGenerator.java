/* Copyright (c) 2001, 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.stress.wms;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.geotools.data.ows.Layer;
import org.geotools.data.wms.WebMapServer;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Generates a load test based on a layer
 * <p>
 * TODO: add variant generation (that is, dump multiple times the same template
 * but with a slight variation, such as the output format or some extra
 * parameters).
 *
 * @author Andrea Aime - TOPP
 */
public class WMSBBoxTestGenerator {
    public static void main(String[] args) throws Exception {
        int randomBboxCount = 30;
        URL url = new URL("http://localhost:8080/geoserver/wms?service=WMS&request=GetCapabilities");

        WMSBBoxTestGenerator generator = new WMSBBoxTestGenerator();
        generator.generatePlan(randomBboxCount, url, new String[] { "topp:states" },
            "StatesWMSGetMap");
        generator.generatePlan(randomBboxCount, url,
            new String[] { "tiger:poly_landmarks", "tiger:tiger_roads", "tiger:poly_landmarks" },
            "NycTigerWMSGetMap");
    }

    public void generatePlan(int randomBboxCount, URL url, String[] layerNames, String planName)
        throws IOException, ServiceException, TemplateException, NoSuchAuthorityCodeException,
            FactoryException {
        WebMapServer server = new WebMapServer(url);
        Layer[] layers = getLayers(server, layerNames);
        String srs = getSRS(layers[0]);

        Configuration cfg = new Configuration();
        cfg.setClassForTemplateLoading(WMSBBoxTestGenerator.class, "");

        Map map = new HashMap();
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
        map.put("layers", toLayerList(layers));
        map.put("csvFileName", "./" + planName + ".csv");
        map.put("loopCount", new Integer(randomBboxCount));

        Template template = cfg.getTemplate("wms.ftl");
        Writer templateWriter = new BufferedWriter(new FileWriter("plans/" + planName + ".jmx"));
        BufferedWriter csvWriter = new BufferedWriter(new FileWriter("plans/" + planName + ".csv"));
        template.process(map, templateWriter);
        generateRandomBoxes(getEnvelope(layers, srs), randomBboxCount, csvWriter);
    }

    private GeneralEnvelope getEnvelope(Layer[] layers, String srs)
        throws NoSuchAuthorityCodeException, FactoryException {
        GeneralEnvelope result = null;
        CoordinateReferenceSystem crs = CRS.decode(srs);

        for (int i = 0; i < layers.length; i++) {
            GeneralEnvelope ge = layers[i].getEnvelope(crs);

            if (result == null) {
                result = ge;
            } else {
                result.add(ge);
            }
        }

        return result;
    }

    private String toLayerList(Layer[] layers) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < layers.length; i++) {
            sb.append(layers[i].getName());

            if (i < (layers.length - 1)) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    private static void generateRandomBoxes(GeneralEnvelope env, int count, Writer writer)
        throws IOException {
        try {
            double xmax = env.getMaximum(0);
            double xmin = env.getMinimum(0);
            double ymax = env.getMaximum(1);
            double ymin = env.getMinimum(1);

            for (int i = 0; i < count; i++) {
                double x1 = (Math.random() * 0.8 * (xmax - xmin)) + xmin;
                double y1 = (Math.random() * 0.8 * (ymax - ymin)) + ymin;
                double x2 = ((0.2 + (Math.random() * 0.8)) * (xmax - x1)) + x1;
                double y2 = ((0.2 + (Math.random() * 0.8)) * (ymax - y1)) + y1;
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

        if (bboxes.size() == 0) {
            return "EPSG:4326";
        } else {
            return (String) bboxes.keySet().iterator().next();
        }
    }

    private static Layer[] getLayers(WebMapServer server, String[] names) {
        List layers = new ArrayList(server.getCapabilities().getLayerList());
        List nameList = Arrays.asList(names);

        for (Iterator it = layers.iterator(); it.hasNext();) {
            Layer l = (Layer) it.next();

            if ((l.getName() == null) || !nameList.contains(l.getName())) {
                it.remove();
            }
        }

        return (Layer[]) layers.toArray(new Layer[layers.size()]);
    }
}
