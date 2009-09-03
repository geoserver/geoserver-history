package org.geoserver.wfs;

import java.util.HashMap;
import java.util.Map;

import org.geoserver.config.impl.ServiceInfoImpl;

public class WFSInfoImpl extends ServiceInfoImpl implements WFSInfo {

    protected Map<Version,GMLInfo> gml = new HashMap<Version, GMLInfo>();
    protected ServiceLevel serviceLevel = ServiceLevel.COMPLETE;
    protected int maxFeatures = Integer.MAX_VALUE;
    protected boolean featureBounding = true;
    
    public WFSInfoImpl() {
        setId( "wfs" );
    }
    
    public Map<Version, GMLInfo> getGML() {
        return gml;
    }

    public void setGML(Map<Version, GMLInfo> gml) {
        this.gml = gml;
    }

    public ServiceLevel getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(ServiceLevel serviceLevel) {
        this.serviceLevel = serviceLevel;
    }
    
    public void setMaxFeatures(int maxFeatures) {
        this.maxFeatures = maxFeatures;
    }
    
    public int getMaxFeatures() {
        return maxFeatures;
    }

    public void setFeatureBounding(boolean featureBounding) {
        this.featureBounding = featureBounding;
    }
    
    public boolean isFeatureBounding() {
        return featureBounding;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((gml == null) ? 0 : gml.hashCode());
        result = prime * result + maxFeatures;
        result = prime * result + (featureBounding ? 1231 : 1237);
        result = prime * result
                + ((serviceLevel == null) ? 0 : serviceLevel.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (!( obj instanceof WFSInfo) )
            return false;
        final WFSInfo other = (WFSInfo) obj;
        if (gml == null) {
            if (other.getGML() != null)
                return false;
        } else if (!gml.equals(other.getGML()))
            return false;
        if (maxFeatures != other.getMaxFeatures())
            return false;
        if (featureBounding != other.isFeatureBounding())
            return false;
        if (serviceLevel == null) {
            if (other.getServiceLevel() != null)
                return false;
        } else if (!serviceLevel.equals(other.getServiceLevel()))
            return false;
        return true;
    }
}
