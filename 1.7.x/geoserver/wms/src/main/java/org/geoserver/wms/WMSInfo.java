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

    /**
     * The watermarking configuration.
     */
    WatermarkInfo getWatermark();

    /**
     * Sets the watermarking configuration.
     */
    void setWatermark(WatermarkInfo watermark);
    
    String getInterpolation();
    
    void setInterpolation( String interpolation );
    
    /**
     * The srs's that the wms service supports.
     */
    List<String> getSRS();

}
