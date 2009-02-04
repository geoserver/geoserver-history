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

import org.geoserver.rest.MapResource;
import org.geoserver.rest.format.DataFormat;
import org.geoserver.rest.format.FreemarkerFormat;
import org.geoserver.rest.format.MapJSONFormat;
import org.geoserver.rest.format.MapXMLFormat;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.vfny.geoserver.config.DataConfig;

/**
 * Restlet for Style resources
 *
 * @author David Winslow <dwinslow@openplans.org> , The Open Planning Project
 */
public class CoverageListResource extends MapResource {
    private DataConfig myDC;

    public CoverageListResource(DataConfig dc,Context context, Request request, Response response){
        super( context, request, response );
        setDataConfig(dc);
    }

    @Override
    protected Map<String, DataFormat> createSupportedFormats(Request request,
            Response response) {
        Map m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/coverages.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("json", new MapJSONFormat());
        m.put("xml", new MapXMLFormat("coveragestore"));
        m.put(null, m.get("html"));

        return m;
    }

    public void setDataConfig(DataConfig dc){
        myDC = dc;
    }

    public DataConfig getDataConfig(){
        return myDC;
    }

    public Map getMap() {
        Map m = new HashMap();
        List coverageList = new ArrayList();

        Map coverages = myDC.getCoverages();


        String coverageStoreName = getRequest().getAttributes().get("layer")!= null ? (String)getRequest().getAttributes().get("layer") : (String)getRequest().getAttributes().get("folder");
        Map folders = RESTUtils.getVirtualFolderMap(getDataConfig());
        Object resource = folders.get(coverageStoreName);
        if (resource instanceof Map){
            Iterator it = ((Map) resource).keySet().iterator();
            while (it.hasNext()) {
                coverageStoreName = (String) it.next();
                addCoverage(coverageStoreName, coverageList, coverages);
            }
        } else {
            addCoverage(coverageStoreName, coverageList, coverages);
        }

        m.put("coverages", coverageList);

        return m;
    }

    /**
     * @param coverageStoreName
     * @param coverageList
     * @param it
     */
    private void addCoverage(String coverageStoreName, List coverageList, Map coverages) {
        Iterator it = coverages.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String key = (String)entry.getKey();
            if (key.startsWith(coverageStoreName)){
                coverageList.add(key.substring(coverageStoreName.length() + 1));
            }
        }
    }
}
