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
import org.restlet.data.Status;
import org.restlet.data.MediaType;
import org.restlet.resource.StringRepresentation;

import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.global.xml.XMLConfigWriter;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.dto.DataDTO;
// import org.geoserver.data.util.CoverageStoreUtils;

import org.geoserver.rest.MapResource;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.JSONFormat;

public class CoverageStoreResource extends MapResource {
    private DataConfig myDataConfig;
    private Data myData;

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

    public void setData(Data d){
        myData = d;
    }

    public Data getData(){
        return myData;
    }

    public Map getMap(){
    	String storeName = (String) getRequest().getAttributes().get("coveragestore");
        Map m = new HashMap();
        CoverageStoreConfig csc = myDataConfig.getDataFormat(storeName);
        if (csc == null){
        	return null;
        }
        
        m.put("Enabled", Boolean.valueOf(csc.isEnabled()));
        m.put("Namespace", csc.getNameSpaceId());
        m.put("URL", csc.getUrl());
        m.put("Description", csc.getAbstract());
        m.put("Type", csc.getFactory().getName());
        
        return m;
    }

    public boolean allowPut(){
        return true;
    }

    public void putMap(Map details) throws Exception{
        String storeName = (String) getRequest().getAttributes().get("coveragestore");
        CoverageStoreConfig csc = myDataConfig.getDataFormat(storeName);
        boolean enabled = Boolean.parseBoolean((String)details.get("Enabled"));
        String nameSpaceId = (String)details.get("Namespace");
        String url = (String)details.get("URL");
        String description = (String)details.get("Description");
        String format = (String)details.get("Type");
        if (csc == null){
            getResponse().setEntity(
                    new StringRepresentation(
                        "Can't store " + storeName + " you fool!  Use the trunk version!",
                        MediaType.TEXT_PLAIN
                        )
                    );
            return;
            // csc = new CoverageStoreConfig(storeName, CoverageStoreUtils.acquireFormat(format));
        }

        csc.setEnabled(enabled);
        csc.setNameSpaceId(nameSpaceId);
        csc.setUrl(url);
        csc.setAbstract(description);

        myDataConfig.removeDataFormat(storeName);
        myDataConfig.addDataFormat(csc);

        saveConfiguration();

        getResponse().setEntity(
                new StringRepresentation(
                    "Successfully stored " + storeName + ".",
                    MediaType.TEXT_PLAIN
                    )
                );
        getResponse().setStatus(Status.SUCCESS_OK);
    }

    public boolean allowDelete(){
        return true;
    }

    public void handleDelete(){
        String storeName = (String) getRequest().getAttributes().get("coveragestore");
        if (myDataConfig.getDataFormat(storeName) == null){
            getResponse().setEntity(
                    new StringRepresentation("Couldn't find datastore " + storeName + " to delete.",
                        MediaType.TEXT_PLAIN
                        )
                    );
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
        }

        myDataConfig.removeDataFormat(storeName);

        try{
            saveConfiguration();
        } catch (Exception e){
            getResponse().setEntity(
                    new StringRepresentation("Error while saving configuration changes: " + e,
                        MediaType.TEXT_PLAIN
                        )
                    );
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        }

        getResponse().setEntity(
                new StringRepresentation(
                    "Successfully deleted " + storeName + ".",
                    MediaType.TEXT_PLAIN
                    )
                );
        getResponse().setStatus(Status.SUCCESS_OK);
    }
    
/*
 * 
 * hold on to this one, it lists all the coverages in the coverstore.  probably handy someplace, but not here
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
    */


    public Map getSupportedFormats() {
        Map m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/coveragestore.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("json", new JSONFormat());
        m.put("xml", new AutoXMLFormat("coveragestore"));
        m.put(null, m.get("html"));

        return m;
    }

    private void saveConfiguration() throws ConfigurationException{
        getData().load(getDataConfig().toDTO());
        XMLConfigWriter.store((DataDTO)getData().toDTO(),
            GeoserverDataDirectory.getGeoserverDataDirectory()
            );
    }
}
