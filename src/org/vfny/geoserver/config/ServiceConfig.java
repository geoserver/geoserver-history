/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2003, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vfny.geoserver.config;

import org.vfny.geoserver.global.dto.ServiceDTO;
import java.net.URL;


/**
 * ServiceConfig purpose.
 * 
 * <p>
 * ServiceConfig is intended to be extended to provide some basic data storage
 * facilities.  This class represents the basic properties of a web service.
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: ServiceConfig.java,v 1.13 2004/01/21 00:41:36 jive Exp $
 */
public class ServiceConfig {
    /**
     * Represents when the web service is enabled/disabled. True when enabled.
     */
    private boolean enabled = true;

    /**
     * Online Reference URL for the web service. A location to look for when
     * additional assistance is required.
     */
    private URL onlineResource;

    /** The name of the service. */
    private String name;

    /** The title of the service. */
    private String title;

    /** A short abstract about the service. */
    private String _abstract;

    /** A list of keywords associated with the service. */
    private String[] keywords;

    /**
     * The fees associated with the service. When there are not any fees, the
     * value  "NONE" is used.
     */
    private String fees;

    /**
     * The access constraints associated with the service. When there are not
     * any,  the value "NONE" is used.
     */
    private String accessConstraints = "NONE";

    /**
     * Name of the person who maintains the web service. Should ideally be
     * contact  information such as webmaster&amp;geoserver.org .
     */
    private String maintainer;

    /**
     * ServiceConfig constructor.
     * 
     * <p>
     * Creates an empty ServiceConfig representation with default values.
     * </p>
     *
     * @see defaultSettings()
     */
    public ServiceConfig() {
        enabled = true;
        name = "";
        title = "";
        _abstract = "";
        keywords = new String[0];
        fees = "";
        accessConstraints = "NONE";
        maintainer = "";
    }

    /**
     * ServiceConfig constructor.
     * 
     * <p>
     * This is equivalent to calling the load method. When a null value is
     * passed in,  the default values are used. All non-primary datatypes are
     * cloned with the  exception of Strings (which have a singleton hash
     * table in memory representation).
     * </p>
     *
     * @param dto The ServiceDTO object to copy into the new ServiceConfig
     *        object.
     *
     * @throws NullPointerException if dto was null;
     */
    public ServiceConfig(ServiceDTO dto) {
        if (dto == null) {
            throw new NullPointerException("Non null ServiceDTO required");
        }

        update(dto);
    }

    /**
     * Implement loadDTO.
     * 
     * <p>
     * Takes a ServiceDTO and loads it into this ServiceConfig Object
     * </p>
     *
     * @param dto an instance of ServiceDTO
     *
     * @throws NullPointerException if dto is null
     *
     * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
     */
    public void update(ServiceDTO dto) {
        if (dto == null) {
            throw new NullPointerException(
                "Service Data Transfer Object required");
        }

        ServiceDTO s = (ServiceDTO) dto;
        enabled = s.isEnabled();
        name = s.getName();
        title = s.getTitle();
        _abstract = s.getAbstract();

        try {
            keywords = new String[s.getKeywords().length];

            for (int i = 0; i < keywords.length; i++)
                keywords[i] = s.getKeywords()[i];
        } catch (Exception e) {
            // should only happen for null
            keywords = new String[0];
        }

        fees = s.getFees();
        accessConstraints = s.getAccessConstraints();
        maintainer = s.getMaintainer();
        onlineResource = s.getOnlineResource();
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

    // name needed to not match as the DTOs do not follow the same inheritance struture.
    public ServiceDTO toServDTO() {
        ServiceDTO sDto = new ServiceDTO();
        sDto.setAbstract(_abstract);
        sDto.setAccessConstraints(accessConstraints);
        sDto.setEnabled(enabled);
        sDto.setFees(fees);

        String[] s = new String[keywords.length];

        for (int i = 0; i < keywords.length; i++)
            s[i] = keywords[i];

        sDto.setKeywords(s);
        sDto.setMaintainer(maintainer);
        sDto.setName(name);
        sDto.setOnlineResource(onlineResource);
        sDto.setTitle(title);

        return sDto;
    }

    /**
     * getName purpose.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * getOnlineResource purpose.
     *
     * @return
     */
    public URL getOnlineResource() {
        return onlineResource;
    }

    /**
     * getTitle purpose.
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * setName purpose.
     *
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * setOnlineResource purpose.
     *
     * @param url
     */
    public void setOnlineResource(URL url) {
        onlineResource = url;
    }

    /**
     * setTitle purpose.
     *
     * @param string
     */
    public void setTitle(String string) {
        title = string;
    }

    /**
     * getAbstract purpose.
     *
     * @return
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * getAccessConstraints purpose.
     *
     * @return
     */
    public String getAccessConstraints() {
        return accessConstraints;
    }

    /**
     * isEnabled purpose.
     *
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * getFees purpose.
     *
     * @return
     */
    public String getFees() {
        return fees;
    }

    /**
     * getKeywords purpose.
     *
     * @return
     */
    public String[] getKeywords() {
        return keywords;
    }

    /**
     * getMaintainer purpose.
     *
     * @return
     */
    public String getMaintainer() {
        return maintainer;
    }

    /**
     * setAbstract purpose.
     *
     * @param string
     */
    public void setAbstract(String string) {
        _abstract = string;
    }

    /**
     * setAccessConstraints purpose.
     *
     * @param string
     */
    public void setAccessConstraints(String string) {
        accessConstraints = string;
    }

    /**
     * setEnabled purpose.
     *
     * @param b
     */
    public void setEnabled(boolean b) {
        enabled = b;
    }

    /**
     * setFees purpose.
     *
     * @param string
     */
    public void setFees(String string) {
        fees = string;
    }

    /**
     * setKeywords purpose.
     *
     * @param list
     */
    public void setKeywords(String[] list) {
        keywords = list;
    }

    /**
     * setMaintainer purpose.
     *
     * @param string
     */
    public void setMaintainer(String string) {
        maintainer = string;
    }
}
