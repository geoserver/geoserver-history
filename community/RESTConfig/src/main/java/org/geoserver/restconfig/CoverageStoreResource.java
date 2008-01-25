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

import org.restlet.data.Request;
import org.restlet.data.Response;
import org.vfny.geoserver.config.DataConfig;


public class CoverageStoreResource extends MapResource {
    private DataConfig myDataConfig;

    public CoverageStoreResource(){
        super();
    }

    public CoverageStoreResource(org.restlet.Context context, Request request, Response response,
        DataConfig config) {
        super(context, request, response);
        myDataConfig = config;
    }

    public void setDataConfig(DataConfig dc){
        myDataConfig = dc;
    }

    public DataConfig getDataConfig(){
        return myDataConfig;
    }

    @Override
    public Map getMap() {
        String storeName = (String) getRequest().getAttributes().get("coveragestore");
        Map coverages = myDataConfig.getCoverages();
        Map m = new HashMap();
        List coverageNames = new ArrayList();
        Iterator it = coverages.keySet().iterator();

        while (it.hasNext()) {
            String s = (String) it.next();

            if (s.startsWith(storeName)) {
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
        m.put("xml", new AutoXMLFormat("coveragestore"));
        m.put(null, m.get("html"));

        return m;
    }
}
