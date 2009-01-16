package org.geoserver.geosearch;

import org.vfny.geoserver.global.Data;

public class HTMLFeatureDescription extends GeoServerProxyAwareRestlet {
    private Data myCatalog;

    public void setCatalog(Data c){
        myCatalog = c;
    }

    public Data getCatalog(){
        return myCatalog;
    }
}
