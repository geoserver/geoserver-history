/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.vfny.geoserver.config.DataConfig;


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
        m.put("xml", new AutoXMLFormat());
        m.put(null, m.get("html"));

        return m;
    }
}
