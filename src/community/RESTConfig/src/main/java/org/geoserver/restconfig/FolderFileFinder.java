/* Copyright (c) 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import org.restlet.Finder;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.GlobalConfig;

public class FolderFileFinder extends Finder {

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
        String folder = (String)request.getAttributes().get("folder");
        Resource r;

        if (getDataConfig().getDataFormatIds().contains(folder)){
            return null; // TODO: Rewrite the coveragestore file resource :(
            // r = new CoverageFileResource(getData(), getDataConfig());           
        } else {
            r = new DataStoreFileResource(getData(), getDataConfig(), getGeoServer(), getGlobalConfig());
        }

        r.init(getContext(), request, response);

        return r;
    }
}
