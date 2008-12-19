/* Copyright (c) 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import org.restlet.Finder;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.GlobalConfig;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;

public class DefaultLayerConfigFinder extends Finder {

    private Data myData;
    private DataConfig myDataConfig;
    private GeoServer myGeoServer;
    private GlobalConfig myGlobalConfig;

    public void setData(Data d){
        myData = d;
    }

    public Data getData(){
        return myData;
    }

    public void setDataConfig(DataConfig dc){
        myDataConfig = dc;
    }
public DataConfig getDataConfig(){
        return myDataConfig;
    }

    public void setGeoServer(GeoServer geoserver){
        myGeoServer = geoserver;
    }

    public GeoServer getGeoServer(){
        return myGeoServer;
    }

    public void setGlobalConfig(GlobalConfig globalconfig){
        myGlobalConfig = globalconfig;
    }

    public GlobalConfig getGlobalConfig(){
        return myGlobalConfig;
    }

    public Resource findTarget(Request request, Response response){
        String layer = (String) request.getAttributes().get("layer");
        Resource r;

        if (getDataConfig().getDataFormatIds().contains(layer)) {
            r = new CoverageResource(
                    getData(), 
                    getDataConfig() 
            );
        } else 
        
        if (getDataConfig().getDataStores().containsKey(layer)) {
                r = new DefaultFeatureTypeConfigResource(
                    getData(),
                    getDataConfig()
                );
        } else {
            response.setEntity(
                new StringRepresentation(
                    "Failure while setting up folder: type not allowed!",
                    MediaType.TEXT_PLAIN)
                );
        	response.setStatus(Status.SERVER_ERROR_INTERNAL);
        	
        	return null;
        }

        r.init(getContext(), request, response);

        return r;
    }
}
