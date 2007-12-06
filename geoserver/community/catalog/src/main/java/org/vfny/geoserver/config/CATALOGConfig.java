/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.vfny.geoserver.global.CATALOG;
import org.vfny.geoserver.global.dto.CATALOGDTO;
import org.vfny.geoserver.global.dto.ServiceDTO;


/**
 * CATALOG purpose.
 *
 * <p>
 * Description of CATALOG  Used to store CATALOG data.
 * </p>
 *
 * <p></p>
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public class CATALOGConfig extends ServiceConfig {
    public static final String CONFIG_KEY = "Config.CATALOG";

    /**
     * CATALOG constructor.
     *
     * <p>
     * Creates a CATALOG to represent an instance with default data.
     * </p>
     *
     * @see defaultSettings()
     */
    public CATALOGConfig() {
        super();
    }

    /**
     * CATALOG constructor.
     *
     * <p>
     * Creates a copy of the CATALOG provided. If the CATALOG provided  is null then
     * default values are used. All the data structures are cloned.
     * </p>
     *
     * @param w The CATALOG to copy.
     */
    public CATALOGConfig(CATALOGDTO w) {
        super(w.getService());
    }

    /**
     * Creates the CATALOGConfig.
     *
     * @param catalog The catalog module.
     *
     */
    public CATALOGConfig(CATALOG catalog) {
        this((CATALOGDTO) catalog.toDTO());
    }

    /**
     * Implement loadDTO.
     *
     * <p>
     * Takes a CATALOGDTO and loads it into this CATALOGConfig Object
     * </p>
     *
     * @param dto an instance of CATALOGDTO
     *
     * @throws NullPointerException DOCUMENT ME!
     *
     * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
     */
    public void update(CATALOGDTO dto) {
        if (dto == null) {
            throw new NullPointerException(
                "CATALOG Data Transfer Object required");
        }

        super.update(dto.getService());
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
    public CATALOGDTO toDTO() {
        CATALOGDTO CATALOGDto = new CATALOGDTO();
        CATALOGDto.setService((ServiceDTO) super.toServDTO());

        return CATALOGDto;
    }
}
