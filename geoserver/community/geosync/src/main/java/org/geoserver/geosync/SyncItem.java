package org.geoserver.geosync;

import java.util.List;
import java.util.ArrayList;

public class SyncItem {
    private String myLayer;
    private String myAction;
    private List   myAffectedFeatures;
    private Object mySource;

    public SyncItem(){
    }

    public void setLayer(String layer){
        myLayer = layer;
    }

    public String getLayer(){
        return myLayer;
    }

    public void setAction(String action){
        myAction = action;
    }

    public String getAction(){
        return myAction;
    }

    public void setAffectedFeatures(List features){
        myAffectedFeatures = features;
    }

    public List getAffectedFeatures(){
        return myAffectedFeatures;
    }

    public Object getSource(){
        return mySource;
    }

    public void setSource(Object src){
        mySource = src;
    }

    public String toString(){
        return myAction + " on " + myLayer + "; affected " + myAffectedFeatures;
    }
}
