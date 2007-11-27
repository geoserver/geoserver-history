package org.geoserver.restconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

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
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.config.WMSConfig;

/**
 * Restlet for Style resources
 * 
 * @author David Winslow <dwinslow@openplans.org> , The Open Planning Project
 */
class LayerGroupResource extends MapResource {
    private WMSConfig myWMSConfig;

    public LayerGroupResource(Context context,
	    Request request, 
	    Response response, 
	    WMSConfig wmsConfig) {
	super(context, request, response);
	myWMSConfig = wmsConfig;
    }

    public Map getSupportedFormats(){
	Map m = new HashMap();
	m.put("html", new HTMLFormat("HTMLTemplates/layergroups.ftl"));
	m.put("json", new JSONFormat());
	m.put(null, m.get("html"));
	return m;
    }

    public Map getMap(){
	Map context = new HashMap();
	Map layerGroups = myWMSConfig.getBaseMapLayers();
	List layerNames = new ArrayList();

	if (layerGroups != null){

	    Iterator it  = layerGroups.entrySet().iterator();
	    while (it.hasNext()){
		Map.Entry entry = (Map.Entry)it.next();
		Map addition = new HashMap();
		addition.put("name", entry.getKey());
		addition.put("members", Arrays.asList(entry.getValue().toString().split(",")));
		layerNames.add(addition);
	    }
	    context.put("layers", layerNames);
	}

	return context; 
    }


    public void donthandleGet() {
	MediaType mt = null;
	Request req = getRequest();

	// Determine desired output format
	if (req.getResourceRef().getQueryAsForm().contains("format")) {
	    mt = MediaType.valueOf(req.getResourceRef().getQueryAsForm()
		    .getFirstValue("format"));
	} else {
	    mt = MediaType.TEXT_HTML;
	}
    }
}
