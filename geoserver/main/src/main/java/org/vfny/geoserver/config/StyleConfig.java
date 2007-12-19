/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.vfny.geoserver.global.dto.StyleDTO;
import java.io.File;


/**
 * StyleConfig purpose.
 *
 * <p>
 * Defines the style ids to be used by the wms.
 * The files  must be contained in geoserver/misc/wms/styles.
 * We're  working on finding a better place for them,
 * but for now  that's where you must put them if you want them
 * on the server.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id$
 */
public class StyleConfig {
    /** The syle id. */
    private String id = "";

    /** The file which contains more information about the style. */
    private File filename = null;

    /** whether this is the system's default style. */
    private boolean _default = false;

    /**
     * StyleConfig constructor.
     *
     * <p>
     * Creates a StyleConfig to represent an instance with default data.
     * </p>
     *
     * @see defaultSettings()
     */
    public StyleConfig() {
        id = "";
        filename = null;
        _default = false;
    }

    /**
     * Simple copy constructor.
     * <p>
     * Used to duplicate a StyleConfig during editing.
     * </p>
     * @param style StyleConfig to copy
     */
    public StyleConfig(StyleConfig style) {
        if (style == null) {
            throw new NullPointerException("Non null StyleConfig required");
        }

        id = style.getId();
        filename = new File(style.getFilename().toString());
        _default = style.isDefault();
    }

    /**
     * StyleConfig constructor.
     *
     * <p>
     * Creates a copy of the StyleDTO provided. All the data structures are
     * cloned.
     * </p>
     *
     * @param style The style to copy.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public StyleConfig(StyleDTO style) {
        if (style == null) {
            throw new NullPointerException("Non null StyleDTO required");
        }

        id = style.getId();
        filename = new File(style.getFilename().toString());
        _default = style.isDefault();
    }

    /**
     * Implement loadDTO.
     *
     * <p>
     * Stores the data provided for the specified StyleDTO object
     * </p>
     *
     * @param obj a StyleDTO object
     *
     * @throws NullPointerException DOCUMENT ME!
     *
     * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
     */
    public void update(StyleDTO obj) {
        if (obj == null) {
            throw new NullPointerException("Style Data Transfer Object required");
        }

        StyleDTO sDto = (StyleDTO) obj;
        id = sDto.getId();
        filename = new File(sDto.getFilename().toString());
        _default = sDto.isDefault();
    }

    /**
     * Implement toDTO.
     *
     * <p>
     * Creates a StyleDTO which represents the data in this config object.
     * </p>
     *
     * @return a copy of this classes data in a StyleDTO object.
     *
     * @see org.vfny.geoserver.config.DataStructure#toDTO()
     */
    public StyleDTO toDTO() {
        StyleDTO sDto = new StyleDTO();
        sDto.setDefault(_default);
        sDto.setFilename(new File(filename.toString()));
        sDto.setId(id);

        return sDto;
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
     * getFilename purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public File getFilename() {
        return filename;
    }

    /**
     * getId purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * setDefault purpose.
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
     * setFilename purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param file
     */
    public void setFilename(File file) {
        filename = file;
    }

    /**
     * setId purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setId(String string) {
        id = string;
    }
}
