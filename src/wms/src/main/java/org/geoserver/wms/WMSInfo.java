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
    
    /**
     * The maximum search radius for GetFeatureInfo
     */
    int getMaxBuffer();
    
    /**
     * Sets the maximum search radius for GetFeatureInfo
     * (if 0 or negative no maximum is enforced)
     */
    void setMaxBuffer(int buffer);
    
    /**
     * Returns the max amount of memory, in kilobytes, that each WMS request
     * can allocate (each output format will make a best effort
     * attempt to respect it, but there are no guarantees)
     * @return the limit, or 0 if no limit
     */
    int getMaxRequestMemory();
    
    /**
     * Sets the max amount of memory, in kilobytes, that each WMS
     * request can allocate. Set it to 0 if no limit is desired. 
     */
    void setMaxRequestMemory(int max);

}
