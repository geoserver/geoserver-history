/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourcePool;
import org.geotools.data.DataAccess;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.vfny.geoserver.global.dto.DataStoreInfoDTO;


/**
 * This is the configuration iformation for one DataStore. This class can also
 * generate real datastores.
 * <p>
 * This class implements {@link org.geotools.catalog.Service} interface as a
 * link to a catalog.
 * </p>
 * @author Gabriel Rold?n
 * @author dzwiers
 * @author Justin Deoliveira
 * @version $Id$
 * 
 * @deprecated use {@link org.geoserver.catalog.DataStoreInfo}
 */
public class DataStoreInfo extends GlobalLayerSupertype {
    //
    ///** DataStoreInfo we are representing */
    //private DataStore dataStore = null;
    //
    ///** ref to the parent class's collection */
    //private Data data;
    //private String id;
    //private String nameSpaceId;
    //private boolean enabled;
    //private String title;
    //private String _abstract;
    //private Map connectionParams;
    //
    ///** Storage for metadata */
    //private Map meta;
    //
    ///**
    // * Directory associated with this DataStore.
    // *
    // * <p>
    // * This directory may be used for File based relative paths.
    // * </p>
    // */
    //File baseDir;
    //
    ///**
    // * URL associated with this DataStore.
    // *
    // * <p>
    // * This directory may be used for URL based relative paths.
    // * </p>
    // */
    //URL baseURL;

    org.geoserver.catalog.DataStoreInfo dataStore;
    Catalog catalog;
    
    ///**
    // * DataStoreInfo constructor.
    // *
    // * <p>
    // * Stores the specified data for later use.
    // * </p>
    // *
    // * @param config DataStoreInfoDTO the current configuration to use.
    // * @param data Data a ref to use later to look up related informtion
    // */
    //public DataStoreInfo(DataStoreInfoDTO config, Data data) {
    //    this.data = data;
    //    meta = new HashMap();
    //
    //    connectionParams = config.getConnectionParams();
    //    enabled = config.isEnabled();
    //    id = config.getId();
    //    nameSpaceId = config.getNameSpaceId();
    //    title = config.getTitle();
    //    _abstract = config.getAbstract();
    //}

    public DataStoreInfo(org.geoserver.catalog.DataStoreInfo dataStore, Catalog catalog ) {
        this.dataStore = dataStore;
        this.catalog = catalog;
    }
    
    public void load(DataStoreInfoDTO dto) {
        
        dataStore.getConnectionParameters().clear();
        dataStore.getConnectionParameters().putAll( dto.getConnectionParams() );
        dataStore.setEnabled( dto.isEnabled() );
        dataStore.setName(dto.getId() );
        dataStore.setWorkspace( catalog.getWorkspaceByName( dto.getNameSpaceId() ));
        dataStore.setDescription( dto.getTitle() );
    }
    
    /**
     * toDTO purpose.
     *
     * <p>
     * This method is package visible only, and returns a reference to the
     * GeoServerDTO. This method is unsafe, and should only be used with
     * extreme caution.
     * </p>
     *
     * @return DataStoreInfoDTO the generated object
     */
    Object toDTO() {
        DataStoreInfoDTO dto = new DataStoreInfoDTO();
        dto.setAbstract(getAbstract());
        dto.setConnectionParams(getParams());
        dto.setEnabled(isEnabled());
        dto.setId(getId());
        dto.setNameSpaceId(getNamesSpacePrefix());
        dto.setTitle(getTitle());

        return dto;
        
        //DataStoreInfoDTO dto = new DataStoreInfoDTO();
        //dto.setAbstract(_abstract);
        //dto.setConnectionParams(connectionParams);
        //dto.setEnabled(enabled);
        //dto.setId(id);
        //dto.setNameSpaceId(nameSpaceId);
        //dto.setTitle(title);
        //
        //return dto;
    }

    /**
     * getId purpose.
     *
     * <p>
     * Returns the dataStore's id.
     * </p>
     *
     * @return String the id.
     */
    public String getId() {
        return dataStore.getName();
        //return id;
    }

    protected Map getParams() {
        Map params = new HashMap(dataStore.getConnectionParameters());
        params.put("namespace", getNameSpace().getURI());

        return params;
        
        //Map params = new HashMap(connectionParams);
        //params.put("namespace", getNameSpace().getURI());
        //
        //return getParams(params, data.getBaseDir().toString());
    }

    

    public static Map getParams(Map connectionParams, String baseDir) {
        return ResourcePool.getParams(connectionParams, baseDir);
    }

    /**
     * By now just uses DataStoreFinder to find a new instance of a
     * DataStoreInfo capable of process <code>connectionParams</code>. In the
     * future we can see if it is better to cache or pool DataStores for
     * performance, but definitely we shouldn't maintain a single
     * DataStoreInfo as instance variable for synchronizing reassons
     *
     * <p>
     * JG: Umm we actually require a single DataStoreInfo for for locking &
     * transaction support to work. DataStoreInfo is expected to be thread
     * aware (that is why it has Transaction Support).
     * </p>
     *
     * @return DataStore
     *
     * @throws IllegalStateException if this DataStoreInfo is disabled by
     *         configuration
     * @throws NoSuchElementException if no DataStoreInfo is found
     */
    public synchronized DataAccess<? extends FeatureType, ? extends Feature> getDataStore()
        throws IllegalStateException, NoSuchElementException {
        if (!isEnabled()) {
            throw new IllegalStateException(
                "this datastore is not enabled, check your configuration");
        }
        
        try {
            return dataStore.getDataStore(null);
        } catch (IOException e) {
            throw new IllegalStateException( e );
        }
      

        //if (dataStore == null) {
        //    Map m = getParams();
        //    try {
        //        dataStore = DataStoreUtils.getDataStore(m);
        //        LOGGER.fine("connection established by " + toString());
        //    } catch (Throwable ex) {
        //        throw new IllegalStateException("can't create the datastore " + getId(), ex);
        //    }
        //
        //    if (dataStore == null) {
        //        // If datastore is not present, then disable it
        //        // (although no change in config).
        //        enabled = false;
        //        LOGGER.fine("failed to establish connection with " + toString());
        //        throw new NoSuchElementException("No datastore found capable of managing "
        //            + toString());
        //    }
        //}
        //
        //return dataStore;
    }

    /**
     * getTitle purpose.
     *
     * <p>
     * Returns the dataStore's title.
     * </p>
     *
     * @return String the title.
     */
    public String getTitle() {
        return dataStore.getDescription();
        //return title;
    }

    /**
     * getAbstract purpose.
     *
     * <p>
     * Returns the dataStore's abstract.
     * </p>
     *
     * @return String the abstract.
     */
    public String getAbstract() {
        return dataStore.getDescription();
        //return _abstract;
    }

    /**
     * isEnabled purpose.
     *
     * <p>
     * Returns true when the data store is enabled.
     * </p>
     *
     * @return true when the data store is enabled.
     */
    public boolean isEnabled() {
        return dataStore.isEnabled();
        //return enabled;
    }

    /**
     * getNameSpace purpose.
     *
     * <p>
     * Returns the namespace for this datastore.
     * </p>
     *
     * @return NameSpaceInfo the namespace for this datastore.
     */
    public NameSpaceInfo getNameSpace() {
        NamespaceInfo ns =catalog.getNamespaceByPrefix( dataStore.getWorkspace().getName() );
        return new NameSpaceInfo( ns, catalog );
        //return (NameSpaceInfo) data.getNameSpace(getNamesSpacePrefix());
    }

    /**
     * Access namespace id
     *
     * @return DOCUMENT ME!
     */
    public String getNamesSpacePrefix() {
        return dataStore.getWorkspace().getName();
        //return nameSpaceId;
    }

    /**
     * Implement toString.
     *
     * @return String
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new StringBuffer("DataStoreConfig[namespace=").append(getNameSpace().getPrefix())
                                                             .append(", enabled=")
                                                             .append(isEnabled())
                                                             .append(", abstract=")
                                                             .append(getAbstract())
                                                             .append(", connection parameters=")
                                                             .append(getParams()).append("]")
                                                             .toString();
    }

    /**
     * Implement containsMetaData.
     *
     * @param key
     *
     * @return
     *
     * @see org.geotools.data.MetaData#containsMetaData(java.lang.String)
     */
    public boolean containsMetaData(String key) {
        return dataStore.getMetadata().get( key ) != null;
        //return meta.containsKey(key);
    }

    /**
     * Implement putMetaData.
     *
     * @param key
     * @param value
     *
     * @see org.geotools.data.MetaData#putMetaData(java.lang.String,
     *      java.lang.Object)
     */
    public void putMetaData(String key, Object value) {
        dataStore.getMetadata().put( key, (Serializable) value );
        //meta.put(key, value);
    }

    /**
     * Implement getMetaData.
     *
     * @param key
     *
     * @return
     *
     * @see org.geotools.data.MetaData#getMetaData(java.lang.String)
     */
    public Object getMetaData(String key) {
        return dataStore.getMetadata().get( key );
        //return meta.get(key);
    }
    
    public void dispose() {
        catalog.getResourcePool().dispose( dataStore );
        
        //if(dataStore != null)
        //    dataStore.dispose();
    }
}
