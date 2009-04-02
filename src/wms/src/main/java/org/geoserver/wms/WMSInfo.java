package org.geoserver.wms;

import java.util.List;

import org.geoserver.config.ServiceInfo;

/**
 * Configuration object for Web Map Service.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public interface WMSInfo extends ServiceInfo {
    
    enum WMSInterpolation { Nearest, Bilinear, Bicubic }

    /**
     * The watermarking configuration.
     */
    WatermarkInfo getWatermark();

    /**
     * Sets the watermarking configuration.
     */
    void setWatermark(WatermarkInfo watermark);
    
    WMSInterpolation getInterpolation();
    
    void setInterpolation(WMSInterpolation interpolation);
    
    /**
     * The srs's that the wms service supports.
     */
    List<String> getSRS();
    
    /**
     * The maximum search radius for GetFeatureInfo
     */
    int getMaxBuffer();
    
    /**
     * Sets the maximum search radius for GetFeatureInfo
     * (if 0 or negative no maximum is enforced)
     */
    void setMaxBuffer(int buffer);

}
