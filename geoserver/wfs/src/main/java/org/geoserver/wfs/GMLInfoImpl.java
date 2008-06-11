package org.geoserver.wfs;

public class GMLInfoImpl implements GMLInfo {

    SrsNameStyle srsNameStyle;
    boolean featureBounding;
    
    public SrsNameStyle getSrsNameStyle() {
        return srsNameStyle;
    }

    public void setSrsNameStyle(SrsNameStyle srsNameStyle) {
        this.srsNameStyle = srsNameStyle;
    }
    
    public boolean isFeatureBounding() {
        return featureBounding;
    }

    public void setFeatureBounding(boolean featureBounding) {
        this.featureBounding = featureBounding;
    }



}
