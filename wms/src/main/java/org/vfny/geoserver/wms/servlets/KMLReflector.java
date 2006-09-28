/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.servlets;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geotools.styling.Style;
import org.vfny.geoserver.Response;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.util.requests.readers.KvpRequestReader;
import org.vfny.geoserver.util.requests.readers.XmlRequestReader;
import org.vfny.geoserver.wms.requests.GetKMLReflectKvpReader;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.GetMapResponse;
import org.vfny.geoserver.wms.responses.map.kml.KMLMapProducerFactory;




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
 */
public class KMLReflector extends WMService {

	private static Logger LOGGER = Logger.getLogger(
    					"org.vfny.geoserver.wms.servlets");
	
	final String KML_MIME_TYPE = "application/vnd.google-earth.kml+xml";
	final String KMZ_MIME_TYPE = "application/vnd.google-earth.kmz+xml";
	
	// Values for the prepared WMS request. Later move these to web.xml server config
	final int KMSCORE = 30;
	final boolean KMATTR = true;
	final boolean TRANSPARENT = true;
	final int WIDTH = 1024;
	final int HEIGHT = 1024;
	final String VERSION = "1.0.0";
	final String SRS = "EPSG:4326";
	final String DEFAULT_BBOX = "-180,-90,180,90";
	
	public KMLReflector(WMS wms) {
		super("kml_reflect", wms );
	}


	protected Response getResponseHandler() {
		WMSConfig config = (WMSConfig) getServletContext().getAttribute(WMSConfig.CONFIG_KEY);

        //return new GetMapResponse(config);
		return new GetMapResponse((WMS)getServiceRef(), getApplicationContext());
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
        for (Enumeration pnames = request.getParameterNames();
                pnames.hasMoreElements();) {
            paramName = (String) pnames.nextElement();
            paramValue = request.getParameter(paramName);
            requestParams.put(paramName.toUpperCase(), paramValue);
        }
        
        // Some settings for network links don't pass in a bounding box, so we will
        // supply one in that case that covers the whole world (pray your data isn't big and
        // the KMScore value isn't large)
        if (!requestParams.containsKey("BBOX"))
        	requestParams.put("BBOX", DEFAULT_BBOX);

        KvpRequestReader requestReader = getKvpReader(requestParams);
        
        GetMapRequest serviceRequest;
		try {
			serviceRequest = (GetMapRequest)requestReader.getRequest(request);
		} catch (ServiceException e) {
			e.printStackTrace();
			return;
		}
        
        final MapLayerInfo[] layers = serviceRequest.getLayers();
        LOGGER.info("KML NetworkLink sharing "+layers.length+" layer(s) created.");
        Style[] styles = null;
        if (serviceRequest.getStyles() != null && !serviceRequest.getStyles().isEmpty())
        	styles = (Style[])serviceRequest.getStyles().toArray(new Style[]{});
        
        // fill in our default values for the request if the user didn't pass the value in
        if (!requestParams.containsKey("TRANSPARENT")) 
        	serviceRequest.setTransparent(TRANSPARENT);
    	if (!requestParams.containsKey("KMATTR")) 
    		serviceRequest.setKMattr(KMATTR);
    	if (!requestParams.containsKey("KMSCORE")) 
    		serviceRequest.setKMScore(KMSCORE);
    	if (!requestParams.containsKey("WIDTH")) 
    		serviceRequest.setWidth(WIDTH);
    	if (!requestParams.containsKey("HEIGHT")) 
    		serviceRequest.setHeight(HEIGHT);
    	if (!requestParams.containsKey("VERSION")) 
    		serviceRequest.setVersion(VERSION);
    	// we use the mandatory SRS value of 4326 (lat/lon)
    	
		serviceRequest.setFormat(KML_MIME_TYPE); // output mime type of KML
		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<kml xmlns=\"http://earth.google.com/kml/2.0\">\n");
		sb.append("<Folder>\n");
		
		// make a network link for every layer
		for (int i=0; i<layers.length; i++) {
			//if (layers[i].getType() == MapLayerInfo.TYPE_VECTOR) {
				String style = "&styles="+layers[i].getDefaultStyle().getName();
				if (styles != null && styles.length>=i+1) // if the user specified styles
					style = "&styles="+styles[i].getName(); // use them, else we use the default style
	    		sb.append("<NetworkLink>\n");
				sb.append("<name>"+layers[i].getName()+"</name>\n");
				sb.append("<open>1</open>\n");
				sb.append("<visibility>1</visibility>\n");
				sb.append("<Url>\n");
				sb.append("<href><![CDATA["+serviceRequest.getBaseUrl()+
						"/wms?service=WMS&request=GetMap&format=application/vnd.google-earth.kmz+XML"+
						"&width="+WIDTH+
						"&height="+HEIGHT+
						"&srs="+SRS+
						"&layers="+layers[i].getName()+
						style+ // optional
						"&KMScore="+KMSCORE+
						"&KMAttr="+KMATTR+"]]></href>\n");
				sb.append("<viewRefreshMode>onStop</viewRefreshMode>\n");
				sb.append("<viewRefreshTime>3</viewRefreshTime>\n");
				sb.append("</Url>\n");
				sb.append("</NetworkLink>\n");
			//}
		}
		sb.append("</Folder>\n");
		sb.append("</kml>\n");
		byte[] kml_b = sb.toString().getBytes();
        out.write(kml_b);
        out.flush();
		
	}

}
