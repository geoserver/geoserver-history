/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.vfny.geoserver.global.dto.DataStoreInfoDTO;
import org.vfny.geoserver.util.DataStoreUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;


/**
 * DataStoreInfo purpose.
 *
 * <p>
 * Used to describe a datastore, typically one specified in the catalog.xml
 * config file.
 * </p>
 *
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id$
 */
public class DataStoreConfig {
    /** unique datasore identifier */
    private String id;

    /** unique namespace to refer to this datastore */
    private String nameSpaceId;

    /** wether this data store is enabled */
    private boolean enabled = true;

    /** a short description about this data store */
    private String title;

    /** a short description about this data store */
    private String _abstract;

    /** connection parameters to create the DataStoreInfo */
    private Map connectionParams;

    /** Config ONLY! DataStoreFactory used to test params */
    private DataStoreFactorySpi factory;

    /**
     * Create a new DataStoreConfig from a dataStoreId and factoryDescription
     *
     * <p>
     * Creates a DataStoreInfo to represent an instance with default data.
     * </p>
     *
     * @param dataStoreId Description of DataStore (see DataStoreUtils)
     * @param factoryDescription DOCUMENT ME!
     *
     * @see defaultSettings()
     */
    public DataStoreConfig(String dataStoreId, String factoryDescription) {
        this(dataStoreId, (DataStoreFactorySpi) DataStoreUtils.aquireFactory(factoryDescription));
    }

    /** Creates a new DataStoreConfig for the provided factory. */
    public DataStoreConfig(String dataStoreId, DataStoreFactorySpi factory) {
        this.factory = factory;
        id = dataStoreId;
        nameSpaceId = "";
        enabled = true;
        title = "";
        _abstract = "";
        connectionParams = DataStoreUtils.defaultParams(factory);
    }

    /**
     * DataStoreInfo constructor.
     *
     * <p>
     * Creates a copy of the DataStoreInfoDTO provided. All the datastructures
     * are cloned.
     * </p>
     *
     * @param dto The datastore to copy.
     */
    public DataStoreConfig(DataStoreInfoDTO dto) {
        reset(dto);
    }

    /**
     * Called to update Config class based on DTO information
     *
     * @param dto DOCUMENT ME!
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public void reset(DataStoreInfoDTO dto) {
        if (dto == null) {
            throw new NullPointerException("Non null DataStoreInfoDTO required");
        }

        factory = (DataStoreFactorySpi) DataStoreUtils.aquireFactory(dto.getConnectionParams());

        id = dto.getId();
        nameSpaceId = dto.getNameSpaceId();
        enabled = dto.isEnabled();
        _abstract = dto.getAbstract();
        connectionParams = new HashMap(dto.getConnectionParams());
    }

    /**
     * Implement loadDTO.
     *
     * <p>
     * Populates the data fields with the DataStoreInfoDTO provided.
     * </p>
     *
     * @param ds the DataStoreInfoDTO to use.
     *
     * @throws NullPointerException DOCUMENT ME!
     *
     * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
     */
    public void update(DataStoreInfoDTO ds) {
        if (ds == null) {
            throw new NullPointerException("DataStoreInfo Data Transfer Object required");
        }

        id = ds.getId();
        nameSpaceId = ds.getNameSpaceId();
        enabled = ds.isEnabled();
        _abstract = ds.getAbstract();

        try {
            connectionParams = new HashMap(ds.getConnectionParams()); //clone?
        } catch (Exception e) {
            connectionParams = new HashMap();
        }
    }

    /**
     * Implement toDTO.
     *
     * <p>
     * Create a DataStoreInfoDTO from the current config object.
     * </p>
     *
     * @return The data represented as a DataStoreInfoDTO
     *
     * @see org.vfny.geoserver.config.DataStructure#toDTO()
     */
    public DataStoreInfoDTO toDTO() {
        DataStoreInfoDTO ds = new DataStoreInfoDTO();
        ds.setId(id);
        ds.setNameSpaceId(nameSpaceId);
        ds.setEnabled(enabled);
        ds.setAbstract(_abstract);

        try {
            ds.setConnectionParams(new HashMap(connectionParams));
        } catch (Exception e) {
            // default already created  	
        }

        return ds;
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
     * This is the DataStore ID
     *
     * <p>
     *
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
        connectionParams = map;
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

    // Access to Dyanmic Content

    /**
     * It would be nice if we did not throw this away - but life is too short
     *
     * @return Real DataStore generated by this DataStoreConfig
     *
     * @throws IOException If DataStore could not be aquired
     */
    public DataStore findDataStore(ServletContext sc) throws IOException {
        return DataStoreUtils.acquireDataStore(connectionParams, sc);
    }

    /**
     * Get DataStoreFactorySpi used for this DataStoreConfig.
     *
     * @return DataStoreFactorySpi that this DataStoreConfig matches
     */
    public DataStoreFactorySpi getFactory() {
        return factory;
    }
}
