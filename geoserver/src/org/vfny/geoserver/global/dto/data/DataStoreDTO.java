/* Copyright (c) 2001 - 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto.data;

import org.vfny.geoserver.config.CloneLibrary;
import org.vfny.geoserver.config.DataStructure;
import java.util.HashMap;
import java.util.Map;


/**
 * Data Transfer Object for GeoServer DataStore information.
 * 
 * <p>
 * Used to describe a datastore, typically one specified in the catalog.xml
 * config file.
 * </p>
 * 
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: DataStoreDTO.java,v 1.1.2.1 2004/01/04 06:21:33 jive Exp $
 */
public final class DataStoreDTO implements DataStructure {
    /** unique datasore identifier */
    private String id;

    /** unique namespace to refer to this datastore */
    private String nameSpaceId;

    /** wether this data store is enabled */
    private boolean enabled;

    /** a short description about this data store */
    private String title;

    /** a short description about this data store */
    private String _abstract;

    /** connection parameters to create the GlobalDataStore */
    private Map connectionParams;

    /**
     * GlobalDataStore constructor.
     * 
     * <p>
     * Creates a GlobalDataStore to represent an instance with default data.
     * </p>
     *
     * @see defaultSettings()
     */
    public DataStoreDTO() {
        defaultSettings();
    }

    /**
     * GlobalDataStore constructor.
     * 
     * <p>
     * Creates a copy of the GlobalDataStore provided. If the GlobalDataStore
     * provided  is null then default values are used. All the datastructures
     * are cloned.
     * </p>
     *
     * @param ds The datastore to copy.
     */
    public DataStoreDTO(DataStoreDTO ds) {
        if (ds == null) {
            defaultSettings();

            return;
        }

        id = ds.getId();
        nameSpaceId = ds.getNameSpaceId();
        enabled = ds.isEnabled();
        _abstract = ds.getAbstract();

        try {
            connectionParams = CloneLibrary.clone(ds.getConnectionParams()); //clone?
        } catch (Exception e) {
            connectionParams = new HashMap();
        }
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
        nameSpaceId = "";
        enabled = false;
        title = "";
        _abstract = "";
        connectionParams = new HashMap();
    }

    /**
     * Implement clone.
     * 
     * <p>
     * creates a clone of this object
     * </p>
     *
     * @return A copy of this GlobalDataStore
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new DataStoreDTO(this);
    }

    /**
     * Implement equals.
     * 
     * <p>
     * recursively tests to determine if the object passed in is a copy of this
     * object.
     * </p>
     *
     * @param obj The GlobalDataStore object to test.
     *
     * @return true when the object passed is the same as this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        DataStoreDTO ds = (DataStoreDTO) obj;
        boolean r = true;
        r = r && (id == ds.getId());
        r = r && nameSpaceId.equals(ds.getNameSpaceId());
        r = r && (enabled == ds.isEnabled());
        r = r && (_abstract == ds.getAbstract());

        if (connectionParams != null) {
            r = r && connectionParams.equals(ds.getConnectionParams());
        } else if (ds.getConnectionParams() != null) {
            return false;
        }

        return r;
    }

    /**
     * getAbstract purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * getConnectionParams purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public Map getConnectionParams() {
        return connectionParams;
    }

    /**
     * isEnabled purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public boolean isEnabled() {
        return enabled;
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
     * getNameSpace purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getNameSpaceId() {
        return nameSpaceId;
    }

    /**
     * getTitle purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * setAbstract purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setAbstract(String string) {
        if (string != null) {
            _abstract = string;
        }
    }

    /**
     * setConnectionParams purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param map
     */
    public void setConnectionParams(Map map) {
        if (map != null) {
            connectionParams = map;
        }
    }

    /**
     * setEnabled purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param b
     */
    public void setEnabled(boolean b) {
        enabled = b;
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
        if (string != null) {
            id = string;
        }
    }

    /**
     * setNameSpace purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param support
     */
    public void setNameSpaceId(String support) {
        if (support != null) {
            nameSpaceId = support;
        }
    }

    /**
     * setTitle purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setTitle(String string) {
        if (string != null) {
            title = string;
        }
    }
}
