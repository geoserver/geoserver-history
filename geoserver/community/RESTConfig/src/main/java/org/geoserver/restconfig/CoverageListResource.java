package org.geoserver.restconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.FileRepresentation;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;

/**
 * Restlet for Style resources
 * 
 * @author David Winslow <dwinslow@openplans.org> , The Open Planning Project
 */
public class CoverageListResource extends MapResource {
    private DataConfig myDC;

    public Map getSupportedFormats(){
	Map m = new HashMap();
	
	m.put("html", new HTMLFormat("HTMLTemplates/coverages.ftl"));
	m.put("json", new JSONFormat());
	m.put(null, m.get("html"));

	return m;
    }

    public CoverageListResource(Context context, Request request, Response response,
	                    DataConfig myDataConfig) {
	super(context, request, response);
	myDC = myDataConfig;
    }

    public Map getMap(){
	Map m = new HashMap();
	String coverageName = (String)getRequest().getAttributes().get("coverage");
	Map coverages = myDC.getCoverages();
	List coverageList = new ArrayList();

	Iterator it = coverages.entrySet().iterator();
	while (it.hasNext()){
	    coverageList.add(((Map.Entry)it.next()).getKey().toString());
	}

	m.put("coverages", coverageList);
	return m;
    }

    private Map getCoverageConfigMap(CoverageConfig cc){
	Map m = new HashMap();
	m.put("coverageName", cc.getName());
	m.put("CRSDescription", cc.getCrs().getName());
	m.put("CRSFull", cc.getCrs().toString());
	m.put("DefaultStyle", cc.getDefaultStyle());
	m.put("Label", cc.getLabel());
	m.put("Description", cc.getDescription());
	m.put("Keywords", cc.getKeywords());
	return m;
    }
}
