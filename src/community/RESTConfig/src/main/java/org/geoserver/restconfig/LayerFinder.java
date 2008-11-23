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
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.CoverageStoreConfig;

import java.util.Map;

public class LayerFinder extends Finder {

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
        String folder = (String) request.getAttributes().get("folder");
        Resource r = null;
        Map folders = FolderListFinder.getVirtualFolderMap(getDataConfig());
        Object o = folders.get(folder);

        if (o instanceof Map){
            r = new VirtualLayerResource(getData(), getDataConfig());
        } else if (o instanceof DataStoreConfig) {
            r = new FeatureTypeResource(getData(), getDataConfig());
        } else if (o instanceof CoverageStoreConfig) {
            r = new CoverageResource(getData(), getDataConfig());
        }

        if (r != null) r.init(getContext(), request, response);
        
        return r;
    }
}
