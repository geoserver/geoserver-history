/* 
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.vfny.geoserver.global.dto.LegendURLDTO;


/**
 * This class represents legend icon parameters.
 * 
 * @author Charles Kolbowicz
 */
public class LegendURL extends GlobalLayerSupertype {
    
    /**
     * Holds value of legend icon width.
     */
    private int width;
    
    /**
     * Holds value of legend icon height.
     */
    private int height;
    
    /**
     * Holds value of legend icon format.
     */
    private String format;
    
    /**
     * Holds value of legend icon onlineResource.
     */
    private String onlineResource;
    
    /**
     * Legend constructor.
     * 
     * <p>
     * Stores the new LegendURLDTO data for this LegendURL.
     * </p>
     *
     * @param config
     *
     * @throws NullPointerException when the param is null
     */
     public LegendURL(LegendURLDTO dto) {
        if (dto == null) {
            throw new NullPointerException();
        }
        onlineResource = dto.getOnlineResource();
        format = dto.getFormat();
        width = dto.getWidth();
        height = dto.getHeight();
    }
    
    /**
     * load purpose.
     * <p>
     * loads a new copy of data into this object.
     * </p>
     * @param dto
     */
    public void load(LegendURLDTO dto) {
    	if (dto == null) {
            throw new NullPointerException();
    	}
        onlineResource = dto.getOnlineResource();
        format = dto.getFormat();
        width = dto.getWidth();
        height = dto.getHeight();    	
    }

    
    Object toDTO() {
    	LegendURLDTO dto = new LegendURLDTO();
    	dto.setOnlineResource(onlineResource);
        dto.setFormat(format);
        dto.setWidth(width);
        dto.setHeight(height);
    	return dto;
    }
    
    /**
     * Getter for legend icon width.
     * @return Value of property width.
     */
    public int getWidth() {
        return this.width;
    }
    
    /**
     * Getter for  legend icon height.
     * @return Value of  legend icon height.
     */
    public int getHeight() {
        return this.height;
    }
    
    /**
     * Getter for legend icon format.
     * @return Value of  legend icon format.
     */
    public String getFormat() {
        return this.format;
    }
        
    /**
     * Getter for  legend icon onlineResource.
     * @return Value of  legend icon onlineResource.
     */
    public String getOnlineResource() {
        return this.onlineResource;
    }    
}
