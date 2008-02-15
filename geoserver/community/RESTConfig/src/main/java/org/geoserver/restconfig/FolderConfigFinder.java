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
import org.vfny.geoserver.config.DataConfig;

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
        Resource r;

        if (getDataConfig().getDataFormatIds().contains(folder)){
            r = new CoverageStoreResource(getData(), getDataConfig());           
        } else {
            r = new DataStoreResource(getData(), getDataConfig());
        }

        r.init(getContext(), request, response);

        return r;
    }
}
