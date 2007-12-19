/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.servlets;

import com.vividsolutions.jts.geom.Envelope;
import org.geoserver.ows.util.KvpUtils;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.platform.GeoServerExtensions;
import org.geotools.styling.Style;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wms.requests.GetKMLReflectKvpReader;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.GetMapResponse;
import org.vfny.geoserver.wms.responses.map.kml.KMLMapProducerFactory;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This class takes in a simple WMS request, presumably from Google Earth, and
 * produces a completed WMS request that outputs KML/KMZ. To map a request to
 * this, simple pass a "layers=myLayer" parameter to "wms/kml_reflect":
 * <b>http://localhost:8080/geoserver/wms/kml_reflect?layers=states<b>
 * No extra information, such as styles or EPSG code need to be passed.
 * A request to kml_reflect will return a network link for each layer
 * passed in. Each network layer makes a full WMS request with these
 * default values:
 * - smart KMZ output (vector or raster output)
 * - KMScore value of 30
 * - Image size of 1024x1024
 * - Full attribution on vector features
 * - WMS version 1.0.0
 * - Transparent
 *
 *
 * @author Brent Owens
 *
 * @deprecated use {@link org.vfny.geoserver.wms.responses.map.kml.KMLReflector}.
 *
 */
public class KMLReflector extends WMService {
    private static Logger LOGGER = Logger.getLogger("org.vfny.geoserver.wms.servlets");
    final String KML_MIME_TYPE = "application/vnd.google-earth.kml+xml";
    final String KMZ_MIME_TYPE = "application/vnd.google-earth.kmz+xml";

    // Values for the prepared WMS request. Later move these to web.xml server config
    final int KMSCORE = 50;
    final int REFRESH = 1;
    final boolean KMATTR = true;
    final boolean TRANSPARENT = true;
    final int WIDTH = 1024;
    final int HEIGHT = 1024;
    final String VERSION = "1.0.0";
    final String SRS = "EPSG:4326";
    final String DEFAULT_BBOX = "-180,-90,180,90";

    public KMLReflector() {
        super("kml_reflect", null);
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        setWMS((WMS) config.getServletContext().getAttribute(WMS.WEB_CONTAINER_KEY));
    }

    protected Response getResponseHandler() {
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());

        //return new GetMapResponse(config);
        return new GetMapResponse(getWMS(), context);
    }

    protected KvpRequestReader getKvpReader(Map params) {
        return new GetKMLReflectKvpReader(params, this);
    }

    protected XmlRequestReader getXmlRequestReader() {
        /**
         * @todo Implement this org.vfny.geoserver.servlets.AbstractService
         *       abstract method
         */
        throw new java.lang.UnsupportedOperationException(
            "Method getXmlRequestReader() not yet implemented.");
    }

    /**
     * This will create a <folder> with <networklinks>, one network link
     * for each layer.
     * The only mandatory parameter is "layers" with the layer names.
     * Styles is optional, and so are all other parameters such as:
     * KMScore
     * KMAttr
     * Version
     * Width
     * Height
     * SRS
     *
     * The result is written to the buffered output stream.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        //set to KML mime type, so GEarth opens automatically
        response.setContentType(KMLMapProducerFactory.MIME_TYPE);

        // the output stream we will write to
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());

        Map requestParams = new HashMap();
        String paramName;
        String paramValue;

        // gather the parameters
        for (Enumeration pnames = request.getParameterNames(); pnames.hasMoreElements();) {
            paramName = (String) pnames.nextElement();
            paramValue = request.getParameter(paramName);
            requestParams.put(paramName.toUpperCase(), paramValue);
        }

        // Some settings for network links don't pass in a bounding box, so we will
        // supply one in that case that covers the whole world (pray your data isn't big and
        // the KMScore value isn't large)
        if (!requestParams.containsKey("BBOX")) {
            requestParams.put("BBOX", DEFAULT_BBOX);
        }

        KvpRequestReader requestReader = getKvpReader(requestParams);

        GetMapRequest serviceRequest;

        try {
            serviceRequest = (GetMapRequest) requestReader.getRequest(request);
        } catch (ServiceException e) {
            e.printStackTrace();

            return;
        }

        final MapLayerInfo[] layers = serviceRequest.getLayers();
        LOGGER.info("KML NetworkLink sharing " + layers.length + " layer(s) created.");

        Style[] styles = null;

        if ((serviceRequest.getStyles() != null) && !serviceRequest.getStyles().isEmpty()) {
            styles = (Style[]) serviceRequest.getStyles().toArray(new Style[] {  });
        }

        // fill in our default values for the request if the user didn't pass the value in
        if (!requestParams.containsKey("TRANSPARENT")) {
            serviceRequest.setTransparent(TRANSPARENT);
        }

        if (!requestParams.containsKey("KMATTR")) {
            serviceRequest.setKMattr(KMATTR);
        }

        if (!requestParams.containsKey("KMSCORE")) {
            serviceRequest.setKMScore(KMSCORE);
        }

        if (!requestParams.containsKey("WIDTH")) {
            serviceRequest.setWidth(WIDTH);
        }

        if (!requestParams.containsKey("HEIGHT")) {
            serviceRequest.setHeight(HEIGHT);
        }

        if (!requestParams.containsKey("VERSION")) {
            serviceRequest.setVersion(VERSION);
        }

        List filters = null;
        String filterKey = null;

        if (requestParams.containsKey("FILTER")) {
            String filter = (String) requestParams.get("FILTER");
            filters = KvpUtils.readFlat(filter, "()");
            filterKey = "filter";
        } else if (requestParams.containsKey("CQL_FILTER")) {
            String filter = (String) requestParams.get("CQL_FILTER");
            filters = KvpUtils.readFlat(filter, "|");
            filterKey = "cql_filter";
        } else if (requestParams.containsKey("FEATUREID")) {
            //JD: featureid semantics slightly different then other types of 
            // filters
            String filter = (String) requestParams.get("FEATUREID");
            filters = new ArrayList();

            for (int i = 0; i < layers.length; i++) {
                filters.add(filter);
            }

            filterKey = "featureid";
        }

        if ((filters != null) && (filters.size() != layers.length)) {
            throw (IOException) new IOException().initCause(new ServiceException(layers.length
                    + " layers specified, but " + filters.size() + " filters"));
        }

        //set the content disposition
        StringBuffer filename = new StringBuffer();

        for (int i = 0; i < layers.length; i++) {
            String name = layers[i].getName();

            //strip off prefix
            int j = name.indexOf(':');

            if (j > -1) {
                name = name.substring(j + 1);
            }

            filename.append(name + "_");
        }

        filename.setLength(filename.length() - 1);

        response.setHeader("Content-Disposition",
            "attachment; filename=" + filename.toString() + ".kml");

        // we use the mandatory SRS value of 4326 (lat/lon)
        serviceRequest.setFormat(KML_MIME_TYPE); // output mime type of KML

        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<kml xmlns=\"http://earth.google.com/kml/2.0\">\n");

        sb.append("<Folder>\n");

        String proxifiedBaseUrl = RequestUtils.baseURL(request);
        GeoServer gs = (GeoServer) GeoServerExtensions.extensions(GeoServer.class).get(0);
        proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(proxifiedBaseUrl, gs.getProxyBaseUrl());

        Envelope layerbbox = null;

        // make a network link for every layer
        for (int i = 0; i < layers.length; i++) {
            //if (layers[i].getType() == MapLayerInfo.TYPE_VECTOR) {
            String style = "&styles=" + layers[i].getDefaultStyle().getName();

            if ((styles != null) && (styles.length >= (i + 1))) { // if the user specified styles
                style = "&styles=" + styles[i].getName(); // use them, else we use the default style
            }

            String filter = (String) ((filters != null) ? filters.get(i) : null);

            if (filter != null) {
                filter = "&" + filterKey + "=" + filter;
            } else {
                filter = "";
            }

            if (serviceRequest.getSuperOverlay()) {
                Envelope bbox = serviceRequest.getBbox();

                sb.append("<NetworkLink>\n");
                sb.append("<name>" + layers[0].getName() + "</name>\n");
                sb.append("<Region>");
                sb.append("<LatLonAltBox>");
                sb.append("<north>" + bbox.getMaxY() + "</north>");
                sb.append("<south>" + bbox.getMinY() + "</south>");
                sb.append("<east>" + bbox.getMaxX() + "</east>");
                sb.append("<west>" + bbox.getMinX() + "</west>");
                sb.append("</LatLonAltBox>");
                sb.append("<Lod>");
                sb.append("<minLodPixels>256</minLodPixels>");
                sb.append("<maxLodPixels>-1</maxLodPixels>");
                sb.append("</Lod>");
                sb.append("</Region>");

                sb.append("<Link>\n");
                sb.append("<href><![CDATA[" + proxifiedBaseUrl
                    + "/wms?service=WMS&request=GetMap&format=application/vnd.google-earth.kml+XML"
                    + "&width=" + WIDTH + "&height=" + HEIGHT + "&srs=" + SRS + "&layers="
                    + layers[i].getName() + style + "&bbox=" + (String) requestParams.get("BBOX")
                    + filter + "&legend=" + String.valueOf(serviceRequest.getLegend())
                    + "&superoverlay=true]]></href>\n");
                sb.append("<viewRefreshMode>onRegion</viewRefreshMode>\n");

                sb.append("</Link>\n");
                sb.append("</NetworkLink>\n");
            } else {
                //               Envelope le = (layers[i].getFeature() == null ? layers[i].getBoundingBox() : layers[i].getFeature().getLatLongBoundingBox());
                Envelope le = layers[i].getLatLongBoundingBox();

                if (layerbbox == null) {
                    layerbbox = new Envelope(le);
                } else {
                    layerbbox.expandToInclude(le);
                }

                sb.append("<NetworkLink>\n");
                sb.append(getLookAt(le));
                sb.append("<name>" + layers[i].getName() + "</name>\n");
                sb.append("<open>1</open>\n");
                sb.append("<visibility>1</visibility>\n");
                sb.append("<Url>\n");
                sb.append("<href><![CDATA[" + proxifiedBaseUrl
                    + "/wms?service=WMS&request=GetMap&format=application/vnd.google-earth.kmz+XML"
                    + "&width=" + WIDTH + "&height=" + HEIGHT + "&srs=" + SRS + "&layers="
                    + layers[i].getName() + style + filter // optional
                    + "&KMScore=" + serviceRequest.getKMScore() + "&KMAttr="
                    + String.valueOf(serviceRequest.getKMattr()) + "&legend="
                    + String.valueOf(serviceRequest.getLegend()) + "]]></href>\n");
                sb.append("<viewRefreshMode>onStop</viewRefreshMode>\n");
                sb.append("<viewRefreshTime>" + REFRESH + "</viewRefreshTime>\n");
                sb.append("</Url>\n");
                sb.append("</NetworkLink>\n");
            }
        }

        sb.append(getLookAt(layerbbox));

        sb.append("</Folder>\n");

        sb.append("</kml>\n");

        byte[] kml_b = sb.toString().getBytes();
        out.write(kml_b);
        out.flush();
    }

    private String getLookAt(Envelope e) {
        double lon1 = e.getMinX();
        double lat1 = e.getMinY();
        double lon2 = e.getMaxX();
        double lat2 = e.getMaxY();

        double R_EARTH = 6.371 * 1000000; // meters
        double VIEWER_WIDTH = (22 * Math.PI) / 180; // The field of view of the google maps camera, in radians
        double[] p1 = getRect(lon1, lat1, R_EARTH);
        double[] p2 = getRect(lon2, lat2, R_EARTH);
        double[] midpoint = new double[] {
                (p1[0] + p2[0]) / 2, (p1[1] + p2[1]) / 2, (p1[2] + p2[2]) / 2
            };

        midpoint = getGeographic(midpoint[0], midpoint[1], midpoint[2]);

        // averaging the longitudes; using the rectangular coordinates makes the calculated center tend toward the corner that's closer to the equator. 
        midpoint[0] = ((lon1 + lon2) / 2);

        double distance = distance(p1, p2);

        double height = distance / (2 * Math.tan(VIEWER_WIDTH));

        LOGGER.fine("lat1: " + lat1 + "; lon1: " + lon1);
        LOGGER.fine("lat2: " + lat2 + "; lon2: " + lon2);
        LOGGER.fine("latmid: " + midpoint[1] + "; lonmid: " + midpoint[0]);

        return "<LookAt id=\"geoserver\">" + "  <longitude>" + ((lon1 + lon2) / 2)
        + "</longitude>      <!-- kml:angle180 -->" + "  <latitude>" + midpoint[1]
        + "</latitude>        <!-- kml:angle90 -->"
        + "  <altitude>0</altitude>       <!-- double --> " + "  <range>" + distance
        + "</range>              <!-- double -->" + "  <tilt>0</tilt>               <!-- float -->"
        + "  <heading>0</heading>         <!-- float -->"
        + "  <altitudeMode>clampToGround</altitudeMode> "
        + "  <!--kml:altitudeModeEnum:clampToGround, relativeToGround, absolute -->" + "</LookAt>";
    }

    private double[] getRect(double lat, double lon, double radius) {
        double theta = ((90 - lat) * Math.PI) / 180;
        double phi = ((90 - lon) * Math.PI) / 180;

        double x = radius * Math.sin(phi) * Math.cos(theta);
        double y = radius * Math.sin(phi) * Math.sin(theta);
        double z = radius * Math.cos(phi);

        return new double[] { x, y, z };
    }

    private double[] getGeographic(double x, double y, double z) {
        double theta;
        double phi;
        double radius;
        radius = distance(new double[] { x, y, z }, new double[] { 0, 0, 0 });
        theta = Math.atan2(Math.sqrt((x * x) + (y * y)), z);
        phi = Math.atan2(y, x);

        double lat = 90 - ((theta * 180) / Math.PI);
        double lon = 90 - ((phi * 180) / Math.PI);

        return new double[] { ((lon > 180) ? (lon - 360) : lon), lat, radius };
    }

    private double distance(double[] p1, double[] p2) {
        double dx = p1[0] - p2[0];
        double dy = p1[1] - p2[1];
        double dz = p1[2] - p2[2];

        return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }
}
