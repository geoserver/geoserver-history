/* Copyright (c) 2001 - 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto.data;

import org.vfny.geoserver.config.DataStructure;
import java.io.File;


/**
 * Data Transfer Object for style information.
 * 
 * <p>
 * Defines the style ids to be used by the wms.  The files  must be contained
 * in geoserver/misc/wms/styles.  We're  working on finding a better place for
 * them, but for now  that's where you must put them if you want them on the
 * server.
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: StyleDTO.java,v 1.1.2.1 2004/01/04 06:21:33 jive Exp $
 */
public final class StyleDTO implements DataStructure {
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
    public StyleDTO() {
        defaultSettings();
    }

    /**
     * StyleConfig constructor.
     * 
     * <p>
     * Creates a copy of the StyleConfig provided. If the StyleConfig provided
     * is null then default values are used. All the data structures are
     * cloned.
     * </p>
     *
     * @param style The style to copy.
     */
    public StyleDTO(StyleDTO style) {
        if (style == null) {
            defaultSettings();

            return;
        }

        id = style.getId();
        filename = new File(style.getFilename().toString());
        _default = style.isDefault();
    }

    /**
     * defaultSettings purpose.
     * 
     * <p>
     * This method creates default values for the class. This method  should
     * noly be called by class constructors.
     * </p>
     */
    private void defaultSettings() {
        id = "";
        filename = null;
        _default = false;
    }

    /**
     * Implement clone.
     * 
     * <p>
     * creates a clone of this object
     * </p>
     *
     * @return A copy of this StyleConfig
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new StyleDTO(this);
    }

    /**
     * Implement equals.
     * 
     * <p>
     * recursively tests to determine if the object passed in is a copy of this
     * object.
     * </p>
     *
     * @param obj The StyleConfig object to test.
     *
     * @return true when the object passed is the same as this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        StyleDTO style = (StyleDTO) obj;
        boolean r = true;
        r = r && (id == style.getId());

        if (filename != null) {
            r = r && filename.equals(style.getFilename());
        }

        r = r && (_default == style.isDefault());

        return r;
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
