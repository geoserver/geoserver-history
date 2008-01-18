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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Restlet for Style resources
 *
 * @author David Winslow <dwinslow@openplans.org> , The Open Planning Project
 */
class StyleListResource extends MapResource {
    private DataConfig myDC;

    public StyleListResource(){
        super();
    }

    public StyleListResource(Context context, Request request, Response response,
        DataConfig myDataConfig) {
        super(context, request, response);
        myDC = myDataConfig;
    }

    public void setDataConfig(DataConfig dc){
        myDC = dc;
    }

    public DataConfig getDataConfig(){
        return myDC;
    }

    public Map getMap() {
        Map styles = myDC.getStyles();
        Map templateContext = new HashMap();
        List styleList = new ArrayList();

        Iterator it = styles.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            styleList.add(entry.getKey().toString());
        }

        templateContext.put("styles", styleList);

        return templateContext;
    }

    public Map getSupportedFormats() {
        Map m = new HashMap();
        m.put("html", new HTMLFormat("HTMLTemplates/styles.ftl"));
        m.put("json", new JSONFormat());
        m.put(null, m.get("html"));

        return m;
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

        String styleName = (String) req.getAttributes().get("style");

        if (styleName == null) {
            Map styles = myDC.getStyles();
            Map templateContext = new HashMap();
            List styleList = new ArrayList();

            Iterator it = styles.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                styleList.add(entry.getKey().toString());
            }

            templateContext.put("styles", styleList);

            templateContext.put("requestURL", req.getResourceRef().getBaseRef());

            getResponse()
                .setEntity(HTMLTemplate.getHtmlRepresentation("HTMLTemplates/styles.ftl",
                    templateContext));
        } else {
            StyleConfig sc = (StyleConfig) myDC.getStyles().get(styleName);

            if (sc != null) {
                getResponse()
                    .setEntity(new FileRepresentation(sc.getFilename(), MediaType.APPLICATION_XML,
                        10));
            } else {
                getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                getResponse()
                    .setEntity(new StringRepresentation(
                        "Error - Couldn't find the requested resource", MediaType.TEXT_PLAIN));
            }
        }
    }
}
