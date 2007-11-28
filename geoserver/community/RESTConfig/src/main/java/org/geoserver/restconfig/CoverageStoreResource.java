package org.geoserver.restconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import org.geotools.geometry.GeneralEnvelope;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.config.DataConfig;

public class CoverageStoreResource extends MapResource {

	private DataConfig myDataConfig;

	public CoverageStoreResource(org.restlet.Context context, Request request,
			Response response, DataConfig config) {
		super(context, request, response);
		myDataConfig = config;
	}

	@Override
	public Map getMap() {
	    String storeName = (String)getRequest().getAttributes().get("coveragestore");
	    Map coverages = myDataConfig.getCoverages();
	    Map m = new HashMap();
	    List coverageNames = new ArrayList();
	    Iterator it = coverages.keySet().iterator();
	    while (it.hasNext()){
		String s = (String)it.next();
		if (s.startsWith(storeName)){
		   coverageNames.add(s.substring(storeName.length() + 1)); // , coverages.get(s)); 
		}
	    }

	    m.put("coverages", coverageNames);

	    return m; // coverages;
	}

	@Override
	public Map getSupportedFormats() {
		Map m = new HashMap();

		m.put("html", new HTMLFormat("HTMLTemplates/coveragestore.ftl"));
		m.put("json", new JSONFormat());
		m.put(null, m.get("html"));

		return m;
	}

	private Map getCoverageConfigMap(CoverageConfig cc) {
		Map m = new HashMap();
		m.put("WMSPath", cc.getWmsPath());
		m.put("CRS", cc.getCrs().getName());
		GeneralEnvelope env = cc.getEnvelope();
		List envPoints = new ArrayList();
		envPoints.add(env.getLowerCorner().getOrdinate(0));
		envPoints.add(env.getLowerCorner().getOrdinate(1));
		envPoints.add(env.getUpperCorner().getOrdinate(0));
		envPoints.add(env.getUpperCorner().getOrdinate(1));
		m.put("Envelope", envPoints);
		// m.put("CRSFull", cc.getCrs().toString());
		m.put("DefaultStyle", cc.getDefaultStyle());
		m.put("SupplementaryStyles", cc.getStyles());// TODO: does this
														// return a list of
														// strings or something
														// else?
		m.put("Label", cc.getLabel());
		m.put("Description", cc.getDescription());
		m.put("OnlineResource", cc.getMetadataLink().getAbout()); // TODO: get
																	// the
																	// actual
																	// URL, this
																	// may take
																	// some
																	// digging
		m.put("Keywords", cc.getKeywords());
		m.put("SupportedRequestCRSs", cc.getRequestCRSs());
		m.put("SupportedResponseCRSs", cc.getResponseCRSs());
		m.put("NativeFormat", cc.getNativeFormat());
		m.put("SupportedFormats", cc.getSupportedFormats());
		m.put("DefaultInterpolationMethod", cc.getDefaultInterpolationMethod());
		m.put("InterpolationMethods", cc.getInterpolationMethods());
		return m;
	}
}
