package org.geoserver.restconfig;

import org.geoserver.rest.MapResource;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.FreemarkerFormat;
import org.geoserver.rest.JSONFormat;

import org.restlet.data.MediaType;

import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.config.CoverageConfig;
import org.vfny.geoserver.config.CoverageStoreConfig;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class VirtualLayerResource extends MapResource {
    Data myData;
    DataConfig myDataConfig;

    public VirtualLayerResource(Data d, DataConfig dc){
        myData = d;
        myDataConfig = dc;
    }

    public void setData(Data d){
        myData = d;
    }

    public void setDataConfig(DataConfig dc){
        myDataConfig =dc;
    }

    public Data getData(){
        return myData;
    }

    public DataConfig getDataConfig(){
        return myDataConfig;
    }

    public Map getSupportedFormats(){
        HashMap m = new HashMap();

        m.put("html", new FreemarkerFormat("HTMLTemplates/vlayer.ftl", getClass(), MediaType.TEXT_HTML));
        m.put("xml", new AutoXMLFormat());
        m.put("json", new JSONFormat());
        m.put(null, m.get("xml"));

        return m;
    }

    public Map getMap(){
        Map m = new HashMap();
        Map folders = FolderListFinder.getVirtualFolderMap(getDataConfig());
        String folderName = (String)getRequest().getAttributes().get("folder");
        String layerName  = (String)getRequest().getAttributes().get("layer");

        if (folders.containsKey(folderName) && folders.get(folderName) instanceof Map){
            Map folder = (Map)folders.get(folderName);
            if (folder.containsKey(layerName)){
                Object layer = folder.get(layerName);
                if (layer instanceof DataStoreConfig){
                    return DataStoreResource.getMap((DataStoreConfig)layer);
                } else if (layer instanceof CoverageStoreConfig){
                    return CoverageStoreResource.getMap((CoverageStoreConfig)layer);
                }
            }
        }

        return null;
    }
}
