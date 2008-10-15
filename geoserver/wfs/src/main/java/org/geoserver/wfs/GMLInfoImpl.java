package org.geoserver.wfs;

import java.io.Serializable;

public class GMLInfoImpl implements GMLInfo, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2955646733867099280L;
    
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
