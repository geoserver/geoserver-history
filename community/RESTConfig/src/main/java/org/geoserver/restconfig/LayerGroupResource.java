/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.config.WMSConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Restlet for Style resources
 *
 * @author David Winslow <dwinslow@openplans.org> , The Open Planning Project
 */
class LayerGroupResource extends MapResource {
    private WMSConfig myWMSConfig;

    public LayerGroupResource(){
        super();
    }

    public LayerGroupResource(Context context, Request request, Response response,
        WMSConfig wmsConfig) {
        super(context, request, response);
        myWMSConfig = wmsConfig;
    }

    public void setWMSConfig(WMSConfig c){
        myWMSConfig = c;
    }

    public WMSConfig getWMSConfig(){
        return myWMSConfig;
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();
        m.put("html", new HTMLFormat("HTMLTemplates/layergroups.ftl"));
        m.put("json", new JSONFormat());
        m.put("xml",  new AutoXMLFormat("layergroups"));
        m.put(null, m.get("html"));

        return m;
    }

    public Map getMap() {
        String group = (String)getRequest().getAttributes().get("group");
        Map context = new HashMap();
        Map layerGroups = myWMSConfig.getBaseMapLayers();
        List members = null;
        if (layerGroups != null && 
            layerGroups.containsKey(group)){
            members = Arrays.asList(layerGroups.get(group).toString().split(","));
        }

        List styles = null;
        if (myWMSConfig.getBaseMapStyles() != null &&
            myWMSConfig.getBaseMapStyles().containsKey(group)){
            styles = Arrays.asList(myWMSConfig.getBaseMapStyles().get(group).toString().split(","));
        }

        context.put("Members", members);
        context.put("Styles", null);
        context.put("SRS", null);
        context.put("Envelope", null);

        return context;
    }

    public void donthandleGet() {
        MediaType mt = null;
        Request req = getRequest();

        // Determine desired output format
        if (req.getResourceRef().getQueryAsForm().contains("format")) {
            mt = MediaType.valueOf(req.getResourceRef().getQueryAsForm().getFirstValue("format"));
        } else {
            mt = MediaType.TEXT_HTML;
        }
    }
}
