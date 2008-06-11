/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.vfny.geoserver.global.dto.NameSpaceInfoDTO;


/**
 * NameSpaceConfig purpose.
 *
 * <p>
 * Represents the portion of a namespace required for the configuration of
 * geoserver. Defines namespaces to be used by the datastores.
 * </p>
 *
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id$
 */
public class NameSpaceConfig {
    //public static final String PREFIX_DELIMITER = ":";

    /** The namespace prefix. */
    private String prefix;

    /** The URI for this namespace. */
    private String uri;

    /** Whether this is the default namespace. */
    private boolean _default;

    /**
     * NameSpaceConfig constructor.
     *
     * <p>
     * Creates a NameSpaceConfig to represent an instance with default data.
     * </p>
     *
     * @see defaultSettings()
     */
    public NameSpaceConfig() {
        prefix = "";
        uri = "";
        _default = false;
    }

    /**
     * NameSpaceConfig constructor.
     *
     * <p>
     * Creates a copy of the NameSpaceConfig provided. If the NameSpaceConfig
     * provided  is null then default values are used. All the data structures
     * are cloned.
     * </p>
     *
     * @param ns The namespace to copy.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public NameSpaceConfig(NameSpaceInfoDTO ns) {
        if (ns == null) {
            throw new NullPointerException("");
        }

        prefix = ns.getPrefix();
        uri = ns.getUri();
        _default = ns.isDefault();
    }

    /**
     * Implement loadDTO.
     *
     * <p>
     * Imports the data contained in the NameSpaceInfoDTO object provided.
     * </p>
     *
     * @param dto An NameSpaceInfoDTO object
     *
     * @throws NullPointerException DOCUMENT ME!
     *
     * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
     */
    public void update(NameSpaceInfoDTO dto) {
        if (dto == null) {
            throw new NullPointerException("NameSpace Data Transfer Object required");
        }

        NameSpaceInfoDTO ns = (NameSpaceInfoDTO) dto;
        prefix = ns.getPrefix();
        uri = ns.getUri();
        _default = ns.isDefault();
    }

    /**
     * Implement toDTO.
     *
     * <p>
     * Creates a DTO representation of this Object as a NameSpaceInfoDTO
     * </p>
     *
     * @return a NameSpaceInfoDTO which representts the data in this class.
     *
     * @see org.vfny.geoserver.config.DataStructure#toDTO()
     */
    public NameSpaceInfoDTO toDTO() {
        NameSpaceInfoDTO nsDto = new NameSpaceInfoDTO();
        nsDto.setDefault(_default);
        nsDto.setPrefix(prefix);
        nsDto.setUri(uri);

        return nsDto;
    }

    /**
     * isDefault purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public boolean isDefault() {
        return _default;
    }

    /**
     * getPrefix purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * getUri purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getUri() {
        return uri;
    }

    /**
     * setDdefault purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param b
     */
    public void setDefault(boolean b) {
        _default = b;
    }

    /**
     * setPrefix purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setPrefix(String string) {
        prefix = string;
    }

    /**
     * setUri purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setUri(String string) {
        uri = string;
    }
}
