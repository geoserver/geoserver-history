package org.geoserver.wcs;

import org.geoserver.config.impl.ServiceInfoImpl;

public class WCSInfoImpl extends ServiceInfoImpl implements WCSInfo {

    boolean gmlPrefixing;
    
    public boolean isGMLPrefixing() {
        return gmlPrefixing;
    }

    public void setGMLPrefixing(boolean gmlPrefixing) {
        this.gmlPrefixing = gmlPrefixing;
    }

}
