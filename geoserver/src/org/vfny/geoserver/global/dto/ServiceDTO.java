/* Copyright (c) 2001 - 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

import java.util.LinkedList;
import java.util.List;


/**
 * Data Transfer Object representing GeoServer Service information.
 * 
 * <p>
 * ServiceConfig is intended to be extended to provide some basic data storage
 * facilities.  This class represents the basic properties of a web service.
 * </p>
 * 
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 * 
 * <p>
 * It is very tempting to force Web Services to completely define their own DTO
 * elements (rather than trying for reuse using subclassing or aggregation) -
 * simply to force each service to document what <b>it</b> means by each of
 * these peices of information.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: ServiceDTO.java,v 1.1.2.1 2004/01/04 06:21:33 jive Exp $
 */
public final class ServiceDTO implements DataStructure {
    /**
     * Represents when the web service is enabled/disabled. True when enabled.
     */
    private boolean enabled = true;

    /**
     * Online Reference URL for the web service. A location to look for when
     * additional assistance is required.
     */
    private String onlineResource;

    /** The name of the service. */
    private String name;

    /** The title of the service. */
    private String title;

    /** A short abstract about the service. */
    private String _abstract;

    /** A list of keywords associated with the service. */
    private List keywords;

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
     * contact  information such as a email address.
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
    public ServiceDTO() {
        defaultSettings();
    }

    /**
     * ServiceConfig constructor.
     * 
     * <p>
     * This is equivalent to calling the clone method. When a null value is
     * passed in,  the default values are used. All non-primary datatypes are
     * cloned with the  exception of Strings (which have a singleton hash
     * table in memory representation).
     * </p>
     *
     * @param s The ServiceConfig object to copy into the new ServiceConfig
     *        object.
     *
     * @see defaultSettings()
     * @see clone()
     */
    public ServiceDTO(ServiceDTO s) {
        if (s == null) {
            defaultSettings();

            return;
        }

        enabled = s.isEnabled();
        name = s.getName();
        title = s.getTitle();
        _abstract = s.getAbstract();

        try {
            keywords = CloneLibrary.clone(s.getKeywords());
        } catch (Exception e) {
            // should only happen for null
            keywords = new LinkedList();
        }

        fees = s.getFees();
        accessConstraints = s.getAccessConstraints();
        maintainer = s.getMaintainer();
    }

    /**
     * defaultSettings purpose.
     * 
     * <p>
     * Sets the class variables to default settings. This method should only be
     * used  by the constructors.
     * </p>
     */
    private void defaultSettings() {
        enabled = true;
        name = "";
        title = "";
        _abstract = "";
        keywords = new LinkedList();
        fees = "";
        accessConstraints = "NONE";
        maintainer = "";
    }

    /**
     * Implements clone.
     *
     * @return An instance of a ServiceConfig object which represents a copy of
     *         the existing ServiceConfig Object.
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new ServiceDTO(this);
    }

    /**
     * Implement equals.
     *
     * @param obj The ServiceConfig object which will be tested.
     *
     * @return true when the classes are equal.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof ServiceDTO) || (obj == null)) {
            return false;
        }

        ServiceDTO s = (ServiceDTO) obj;
        boolean r = true;
        r = r && (enabled == s.isEnabled());
        r = r && (name == s.getName());
        r = r && (title == s.getTitle());
        r = r && (_abstract == s.getAbstract());

        if (keywords != null) {
            r = r && keywords.equals(s.getKeywords());
        } else if (s.getKeywords() != null) {
            return false;
        }

        r = r && (fees == s.getFees());
        r = r && (accessConstraints == s.getAccessConstraints());
        r = r && (maintainer == s.getMaintainer());

        return r;
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
    public String getOnlineResource() {
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
     * @param string
     */
    public void setOnlineResource(String string) {
        onlineResource = string;
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
    public List getKeywords() {
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
    public void setKeywords(List list) {
        keywords = list;
    }

    /**
     * addKeyword purpose.
     *
     * @param item The keyword to add to the list.
     *
     * @see java.util.List#add
     */
    public void addKeyword(String item) {
        keywords.add(item);
    }

    /**
     * removeKeyword purpose.
     *
     * @param item The keyword to remove from the list.
     *
     * @return true if the item was removed.
     *
     * @see java.util.List#remove
     */
    public boolean removeKeyword(String item) {
        return keywords.remove(item);
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
