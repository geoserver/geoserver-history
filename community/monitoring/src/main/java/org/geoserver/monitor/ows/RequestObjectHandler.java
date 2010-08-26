package org.geoserver.monitor.ows;

import java.util.List;

/**
 * Class that extracts information from an ows request.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public abstract class RequestObjectHandler {

    String reqObjClassName;
    
    protected RequestObjectHandler(String reqObjClassName) {
        this.reqObjClassName = reqObjClassName;
    }
    
    public boolean canHandle(Object request) {
        Class clazz;
        try {
            clazz = Class.forName(reqObjClassName);
        } 
        catch (ClassNotFoundException e) {
            return false;
        }
        
        return clazz.isInstance(request);
    }
    
    public abstract List<String> getLayers(Object request);
}
