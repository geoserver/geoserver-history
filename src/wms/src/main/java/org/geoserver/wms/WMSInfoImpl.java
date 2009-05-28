package org.geoserver.wms;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.config.impl.ServiceInfoImpl;

public class WMSInfoImpl extends ServiceInfoImpl implements WMSInfo {

    List<String> srs = new ArrayList<String>();
    WatermarkInfo watermark;
    String interpolation;
    int maxBuffer;
    int maxRequestMemory;
    int maxRenderingTime;
    
    public int getMaxRequestMemory() {
        return maxRequestMemory;
    }

    public void setMaxRequestMemory(int maxRequestMemory) {
        this.maxRequestMemory = maxRequestMemory;
    }

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

    public int getMaxBuffer() {
        return maxBuffer;
    }

    public void setMaxBuffer(int maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    public int getMaxRenderingTime() {
        return maxRenderingTime;
    }

    public void setMaxRenderingTime(int maxRenderingTime) {
        this.maxRenderingTime = maxRenderingTime;
    }

}
