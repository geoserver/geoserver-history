/* Copyright (c) 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.util.Map;

import org.restlet.Finder;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.CoverageStoreConfig;

public class FolderConfigFinder extends Finder {

    private Data myData;
    private DataConfig myDataConfig;

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

    public Resource findTarget(Request request, Response response){
        String folder = (String)request.getAttributes().get("folder");
        Resource r = null;
        Map folders = FolderListFinder.getVirtualFolderMap(getDataConfig());
        Object resource = folders.get(folder);
        if (resource instanceof Map){
            r = new VirtualFolderResource(resource);
        } else if (resource instanceof CoverageStoreConfig){
            r = new CoverageStoreResource(getData(), getDataConfig()); // (CoverageStoreConfig) resource);
        } else if (resource instanceof DataStoreConfig) {
            r = new DataStoreResource(getData(), getDataConfig()); // (DataStoreConfig)resource);
        }
/** 
        if (getDataConfig().getDataFormatIds().contains(folder)){
            r = new CoverageStoreResource(getData(), getDataConfig());           
        } else {
            r = new DataStoreResource(getData(), getDataConfig());
        } 
*/

        if (r != null) r.init(getContext(), request, response);

        return r;
    }
}
