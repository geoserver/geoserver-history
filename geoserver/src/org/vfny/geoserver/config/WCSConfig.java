/* Copyright (c) 2005 NATO - Undersea Research Centre.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.vfny.geoserver.global.dto.ServiceDTO;
import org.vfny.geoserver.global.dto.WCSDTO;

/**
 * WCS purpose.
 * 
 * <p>
 * Description of WCS  Used to store WCS data.
 * </p>
 * 
 * <p></p>
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: WCSConfig.java,v 0.1 Feb 15, 2005 5:26:50 PM $
 */
public class WCSConfig extends ServiceConfig {
    public static final String CONFIG_KEY = "Config.WCS";
    private boolean gmlPrefixing;
    
    /**
     * WCS constructor.
     * 
     * <p>
     * Creates a WCS to represent an instance with default data.
     * </p>
     *
     * @see defaultSettings()
     */
    public WCSConfig() {
        super();
    }

    /**
     * WCS constructor.
     * 
     * <p>
     * Creates a copy of the WCS provided. If the WCS provided  is null then
     * default values are used. All the data structures are cloned.
     * </p>
     *
     * @param w The WCS to copy.
     */
    public WCSConfig(WCSDTO w) {
        super(w.getService());
        gmlPrefixing = w.isGmlPrefixing();
    }

    /**
     * Implement loadDTO.
     * 
     * <p>
     * Takes a WMSDTO and loads it into this WMSConfig Object
     * </p>
     *
     * @param dto an instance of WMSDTO
     *
     * @throws NullPointerException DOCUMENT ME!
     *
     * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
     */
    public void update(WCSDTO dto) {
        if (dto == null) {
            throw new NullPointerException("WCS Data Transfer Object required");
        }

        super.update(dto.getService());
        gmlPrefixing = dto.isGmlPrefixing();
    }

    /**
     * Implement toDTO.
     * 
     * <p>
     * Returns a copy of the data in a ServiceDTO object
     * </p>
     *
     * @return a copy of the data in a ServiceDTO object
     *
     * @see org.vfny.geoserver.config.DataStructure#toDTO()
     */
    public WCSDTO toDTO() {
        WCSDTO WCSDto = new WCSDTO();
        WCSDto.setService((ServiceDTO) super.toServDTO());
        WCSDto.setGmlPrefixing(gmlPrefixing);
        return WCSDto;
    }

    /**
     * isGmlPrefixing purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public boolean isGmlPrefixing() {
        return gmlPrefixing;
    }

    /**
     * setGmlPrefixing purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param b
     */
    public void setGmlPrefixing(boolean b) {
        gmlPrefixing = b;
    }
}
