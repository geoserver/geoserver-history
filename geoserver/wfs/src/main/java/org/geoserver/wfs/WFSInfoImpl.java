package org.geoserver.wfs;

import java.util.HashMap;
import java.util.Map;

import org.geoserver.config.impl.ServiceInfoImpl;

public class WFSInfoImpl extends ServiceInfoImpl implements WFSInfo {

    Map<Version,GMLInfo> gml = new HashMap<Version, GMLInfo>();
    ServiceLevel serviceLevel;
    
    public Map<Version, GMLInfo> getGML() {
        return gml;
    }

    public ServiceLevel getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(ServiceLevel serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    /**
     * @param gml the gml to set
     */
    public void setGML(Map<Version, GMLInfo> gml) {
        this.gml = gml;
    }

}
