package org.geoserver.wms;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.config.impl.ServiceInfoImpl;

public class WMSInfoImpl extends ServiceInfoImpl implements WMSInfo {

    List<String> srs = new ArrayList<String>();
    WatermarkInfo watermark;
    String interpolation;
    
    public WatermarkInfo getWatermark() {
        return watermark;
    }
    
    public void setWatermark(WatermarkInfo watermark) {
        this.watermark = watermark;
    }

    public void setInterpolation(String interpolation) {
        this.interpolation = interpolation;
    }
    
    public String getInterpolation() {
        return interpolation;
    }

    public List<String> getSRS() {
        return srs;
    }

}
