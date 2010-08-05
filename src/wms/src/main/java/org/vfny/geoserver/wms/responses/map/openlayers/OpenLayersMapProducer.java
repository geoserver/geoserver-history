/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.openlayers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.MapLayerInfo;
import org.geoserver.wms.WMS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapLayer;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.AbstractGetMapProducer;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class OpenLayersMapProducer extends AbstractGetMapProducer implements
		GetMapProducer {
	/** A logger for this class. */
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.responses.wms.map.openlayers");

	/**
	 * The mime type for the response header
	 */
	private static final String MIME_TYPE = "text/html";
	
	/**
	 * The formats accepted in a GetMap request for this producer and stated in getcaps
	 */
	private static final String[] OUTPUT_FORMATS = {"application/openlayers", "openlayers"};
	
	/**
	 * Set of parameters that we can ignore, since they are not part of the
	 * OpenLayers WMS request
	 */
	private static final Set<String> ignoredParameters;

	static {
		ignoredParameters = new HashSet<String>();
		ignoredParameters.add("REQUEST");
		ignoredParameters.add("TILED");
		ignoredParameters.add("BBOX");
		ignoredParameters.add("SERVICE");
		ignoredParameters.add("VERSION");
		ignoredParameters.add("FORMAT");
		ignoredParameters.add("WIDTH");
		ignoredParameters.add("HEIGHT");
		ignoredParameters.add("SRS");
	}

	/**
	 * static freemaker configuration
	 */
	static Configuration cfg;

	static {
		cfg = new Configuration();
		cfg.setClassForTemplateLoading(OpenLayersMapProducer.class, "");
		BeansWrapper bw = new BeansWrapper();
		bw.setExposureLevel(BeansWrapper.EXPOSE_PROPERTIES_ONLY);
		cfg.setObjectWrapper(bw);
	}

	/**
	 * wms configuration
	 */
	WMS wms;

	/**
	 * The current template
	 */
	Template template;


	public OpenLayersMapProducer(WMS wms) {
	    super(MIME_TYPE, OUTPUT_FORMATS);
		this.wms = wms;
	}

	
	@Override
	public String getOutputFormat() {
	    return requestedOutputFormat == null ? OUTPUT_FORMATS[0] : requestedOutputFormat;
	}


	@SuppressWarnings("unchecked")
    public void writeTo(OutputStream out) throws ServiceException, IOException {
		try {
			// create the template
			Template template = cfg.getTemplate("OpenLayersMapTemplate.ftl");
			HashMap map = new HashMap();
			map.put("context", mapContext);
			map.put("pureCoverage", hasOnlyCoverages(mapContext));
			map.put("styles", styleNames(mapContext));
			map.put("request", mapContext.getRequest());
			map.put("maxResolution", new Double(getMaxResolution(mapContext
					.getAreaOfInterest())));

			String baseUrl = mapContext.getRequest().getBaseUrl();
			map.put("baseUrl", canonicUrl(baseUrl));
			map.put("parameters", getLayerParameter(mapContext.getRequest()
					.getHttpServletRequest()));
			map.put("units", getOLUnits(mapContext.getRequest()));

			if (mapContext.getLayerCount() == 1) {
				map.put("layerName", mapContext.getLayer(0).getTitle());
			} else {
				map.put("layerName", "Geoserver layers");
			}

			template.setOutputEncoding("UTF-8");
			template.process(map, new OutputStreamWriter(out, Charset.forName("UTF-8")));
		} catch (TemplateException e) {
			throw new WmsException(e);
		}

		mapContext = null;
		template = null;
	}

	/**
	 * Guesses if the map context is made only of coverage layers by looking
	 * at the wrapping feature type. Ugly, if you come up with better means of doing
	 * so, fix it.
	 * @param mapContext
	 * @return
	 */
	private boolean hasOnlyCoverages(WMSMapContext mapContext) {
        for (MapLayer layer : mapContext.getLayers()) {
            FeatureType schema = layer.getFeatureSource().getSchema();
            boolean grid = schema.getName().getLocalPart().equals("GridCoverage")
                    && schema.getDescriptor("geom") != null && schema.getDescriptor("grid") != null;
            if(!grid)
                return false;
        }
        return true;
    }
	
	private List<String> styleNames(WMSMapContext mapContext) {
	    if(mapContext.getLayerCount() != 1 || mapContext.getRequest() == null)
	        return Collections.emptyList();
	    
	    MapLayerInfo info = mapContext.getRequest().getLayers()[0];
	    return info.getOtherStyleNames();
	}



    /**
	 * OL does support only a limited number of unit types, we have to try and
	 * return one of those, otherwise the scale won't be shown. From the OL
	 * guide: possible values are "degrees" (or "dd"), "m", "ft", "km", "mi",
	 * "inches".
	 * 
	 * @param request
	 * @return
	 */
	private String getOLUnits(GetMapRequest request) {
		CoordinateReferenceSystem crs = request.getCrs();
		// first rough approximation, meters for projected CRS, degrees for the
		// others
		String result = crs instanceof ProjectedCRS ? "m" : "degrees";
		try {
			String unit = crs.getCoordinateSystem().getAxis(0).getUnit()
					.toString();
			// use the unicode escape sequence for the degree sign so its not
			// screwed up by different local encodings
			final String degreeSign = "\u00B0";
			if (degreeSign.equals(unit) || "degrees".equals(unit) || "dd".equals(unit))
				result = "degrees";
			else if ("m".equals(unit) || "meters".equals(unit))
				result = "m";
			else if ("km".equals(unit) || "kilometers".equals(unit))
				result = "mi";
			else if ("in".equals(unit) || "inches".equals(unit))
				result = "inches";
			else if ("ft".equals(unit) || "feets".equals(unit))
				result = "ft";
			else if ("mi".equals(unit) || "miles".equals(unit))
				result = "mi";
		} catch (Exception e) {
			LOGGER.log(Level.WARNING,
					"Error trying to determine unit of measure", e);
		}
		return result;
	}

	/**
	 * Returns a list of maps with the name and value of each parameter that we
	 * have to forward to OpenLayers. Forwarded parameters are all the provided
	 * ones, besides a short set contained in {@link #ignoredParameters}.
	 * 
	 * 
	 * 
	 * @param request
	 * @return
	 */
	private List getLayerParameter(HttpServletRequest request) {
		List result = new ArrayList();
		Enumeration en = request.getParameterNames();

		while (en.hasMoreElements()) {
			String paramName = (String) en.nextElement();

			if (ignoredParameters.contains(paramName.toUpperCase())) {
				continue;
			}

			// this won't work for multi-valued parameters, but we have none so
			// far (they are common just in HTML forms...)
			Map map = new HashMap();
			map.put("name", paramName);
			map.put("value", request.getParameter(paramName));
			result.add(map);
		}

		return result;
	}

	/**
	 * Makes sure the url does not end with "/", otherwise we would have URL lik
	 * "http://localhost:8080/geoserver//wms?LAYERS=..." and Jetty 6.1 won't
	 * digest them...
	 * 
	 * @param baseUrl
	 * @return
	 */
	private String canonicUrl(String baseUrl) {
		if (baseUrl.endsWith("/")) {
			return baseUrl.substring(0, baseUrl.length() - 1);
		} else {
			return baseUrl;
		}
	}

	private double getMaxResolution(ReferencedEnvelope areaOfInterest) {
		double w = areaOfInterest.getWidth();
		double h = areaOfInterest.getHeight();

		return ((w > h) ? w : h) / 256;
	}

	public void produceMap() throws WmsException {
	}


}
