/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

import java.util.HashMap;
import java.util.Map;


/**
 * Data Transfer Object for GeoServer Format information.
 *
 * <p>
 * Used to describe a Format, typically one specified in the catalog.xml
 * config file.
 * </p>
 *
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 * Example:<code> CoverageStoreInfoDTO dsiDto = new CoverageStoreInfoDTO();
 * dsiDto.setIde("myFormat"); dsiDto.setEnabled(true); dsiDto.setTile("My
 * Data Store"); Map m = new HashMap(); m.put("key","param");
 * dsiDto.setConnectionParams(m); </code>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id$
 */
public final class CoverageStoreInfoDTO implements DataTransferObject {
    /**
     * unique Format identifier
     *
     * @uml.property name="id" multiplicity="(0 1)"
     */
    private String id;

    /**
     * unique namespace to refer to this format
     *
     * @uml.property name="nameSpaceId" multiplicity="(0 1)"
     */
    private String nameSpaceId;

    /**
     *
     * @uml.property name="type" multiplicity="(0 1)"
     */
    private String type;

    /**
     *
     * @uml.property name="url" multiplicity="(0 1)"
     */
    private String url;

    /**
     * true if this Format is enabled
     *
     * @uml.property name="enabled" multiplicity="(0 1)"
     */
    private boolean enabled;

    /**
     * The title of this Format
     *
     * @uml.property name="title" multiplicity="(0 1)"
     */
    private String title;

    /** a short description about this Format */
    private String _abstract;

    /**
     * CoverageStoreInfo constructor.
     *
     * <p>
     * does nothing
     * </p>
     */
    public CoverageStoreInfoDTO() {
    }

    /**
     * CoverageStoreInfo constructor.
     *
     * <p>
     * Creates a copy of the CoverageStoreInfo provided. If the CoverageStoreInfo
     * provided  is null then default values are used. All the datastructures
     * are cloned.
     * </p>
     *
     * @param dto The format to copy.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public CoverageStoreInfoDTO(CoverageStoreInfoDTO dto) {
        if (dto == null) {
            throw new NullPointerException("Non-Null FormatDTO is requried");
        }

        id = dto.getId();
        nameSpaceId = dto.getNameSpaceId();
        type = dto.getType();
        url = dto.getUrl();
        enabled = dto.isEnabled();
        _abstract = dto.getAbstract();
    }

    /**
     * Implement clone.
     *
     * <p>
     * creates a clone of this object
     * </p>
     *
     * @return A copy of this CoverageStoreInfo
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new CoverageStoreInfoDTO(this);
    }

    /**
     * Implement equals.
     *
     * <p>
     * recursively tests to determine if the object passed in is a copy of this
     * object.
     * </p>
     *
     * @param obj The CoverageStoreInfo object to test.
     *
     * @return true when the object passed is the same as this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof CoverageStoreInfoDTO)) {
            return false;
        }

        CoverageStoreInfoDTO ds = (CoverageStoreInfoDTO) obj;
        boolean r = true;
        r = r && (id == ds.getId());
        r = r && (nameSpaceId == ds.getNameSpaceId());
        r = r && (type == ds.getType());
        r = r && (url == ds.getUrl());
        r = r && (enabled == ds.isEnabled());
        r = r && (_abstract == ds.getAbstract());

        return r;
    }

    /**
     * Implement hashCode.
     *
     * @return Service hashcode or 0
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int r = 1;

        if (id != null) {
            r *= id.hashCode();
        }

        if (nameSpaceId != null) {
            r *= nameSpaceId.hashCode();
        }

        if (type != null) {
            r *= type.hashCode();
        }

        if (url != null) {
            r *= url.hashCode();
        }

        if (_abstract != null) {
            r *= _abstract.hashCode();
        }

        return r;
    }

    /**
     * Short description of Format
     *
     * @return Short description
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * Value is <code>true</code> if the Format should be enabled.
     *
     * @return ture if Format shoudl be enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Unique identifier representing this Format.
     *
     * <p>
     * This value is used to refer to this Format by CoverageInfoDTO.
     * </p>
     *
     * @return an identifier, non null
     *
     * @uml.property name="id"
     */
    public String getId() {
        return id;
    }

    /**
     * Title for Format, used in error messages & configuration.
     *
     * @return Title dor the Format
     *
     * @uml.property name="title"
     */
    public String getTitle() {
        return title;
    }

    /**
     * Updates the Format abstract.
     *
     * @param description
     */
    public void setAbstract(String description) {
        _abstract = description;
    }

    /**
     * setEnabled purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param b
     *
     * @uml.property name="enabled"
     */
    public void setEnabled(boolean b) {
        enabled = b;
    }

    /**
     * Sets the unique identifier for this CoverageStoreInfoDTO.
     *
     * @param identifier non<code>null</code> identifier for Format
     *
     * @uml.property name="id"
     */
    public void setId(String identifier) {
        id = identifier;
    }

    /**
     * Set title used to identify this Format to the user.
     *
     * @param formatTitle Title used to identify Format to user
     *
     * @uml.property name="title"
     */
    public void setTitle(String formatTitle) {
        title = formatTitle;
    }

    /**
     * @return Returns the type.
     *
     * @uml.property name="type"
     */
    public String getType() {
        return type;
    }

    /**
     * @param type The type to set.
     *
     * @uml.property name="type"
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Returns the url.
     *
     * @uml.property name="url"
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url to set.
     *
     * @uml.property name="url"
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public String getNameSpaceId() {
        return nameSpaceId;
    }

    public void setNameSpaceId(String nameSpaceId) {
        this.nameSpaceId = nameSpaceId;
    }
}
