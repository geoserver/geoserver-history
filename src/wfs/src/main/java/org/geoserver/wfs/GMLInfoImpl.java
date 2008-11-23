package org.geoserver.wfs;

public class GMLInfoImpl implements GMLInfo {

    SrsNameStyle srsNameStyle = SrsNameStyle.NORMAL;
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

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (featureBounding ? 1231 : 1237);
        result = prime * result
                + ((srsNameStyle == null) ? 0 : srsNameStyle.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!( obj instanceof GMLInfo)) 
            return false;
        
        final GMLInfo other = (GMLInfo) obj;
        if (featureBounding != other.isFeatureBounding())
            return false;
        if (srsNameStyle == null) {
            if (other.getSrsNameStyle() != null)
                return false;
        } else if (!srsNameStyle.equals(other.getSrsNameStyle()))
            return false;
        return true;
    }

    
}
