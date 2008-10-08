/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.IOException;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.data.util.CoverageStoreUtils;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.Driver;
import org.geotools.factory.Hints;
import org.vfny.geoserver.global.dto.CoverageStoreInfoDTO;


/**
 * This is the configuration iformation for one coverage Format.
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 *         
 *  @deprecated use {@link org.geoserver.catalog.CoverageStoreInfo}
 */
public final class CoverageStoreInfo extends GlobalLayerSupertype {
    /** for logging */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(CoverageStoreInfo.class.toString());

    ///**
    // * CoverageStoreInfo we are representing
    // */
    //private Format format = null;
    //
    ///**
    // * ref to the parent class's collection
    // */
    //private Data data;
    //
    ///**
    // *
    // */
    //private String id;
    //private SoftReference reader = null;
    //private SoftReference hintReader = null;
    //
    ///**
    // *
    // */
    //private String nameSpaceId;
    //
    ///**
    // *
    // */
    //private String type;
    //
    ///**
    // *
    // */
    //private String url;
    //private boolean enabled;
    //
    ///**
    // *
    // */
    //private String title;
    //private String _abstract;
    //
    ///**
    // * Storage for metadata
    // */
    //private Map meta;

    org.geoserver.catalog.CoverageStoreInfo cs;
    Catalog catalog;
    
    ///**
    // * CoverageStoreInfo constructor.
    // *
    // * <p>
    // * Stores the specified data for later use.
    // * </p>
    // *
    // * @param config
    // *            CoverageStoreInfoDTO the current configuration to use.
    // * @param data
    // *            Data a ref to use later to look up related informtion
    // */
    //public CoverageStoreInfo(CoverageStoreInfoDTO config, Data data) {
    //    this.data = data;
    //    meta = new HashMap(10);
    //    enabled = config.isEnabled();
    //    id = config.getId();
    //    nameSpaceId = config.getNameSpaceId();
    //    type = config.getType();
    //    url = config.getUrl();
    //    title = config.getTitle();
    //    _abstract = config.getAbstract();
    //    format = lookupFormat();
    //}
    
    public CoverageStoreInfo(org.geoserver.catalog.CoverageStoreInfo cs, Catalog catalog ) {
        this.cs = cs;
        this.catalog = catalog;
    }
    
    public void load(CoverageStoreInfoDTO dto ) {
        cs.setEnabled( dto.isEnabled() );
        cs.setName( dto.getId() );
        cs.setWorkspace( catalog.getWorkspaceByName( dto.getNameSpaceId() ));
        cs.setType( dto.getType() );
        cs.setURL( dto.getUrl() );
        cs.setDescription( dto.getTitle() );
    }

    private Driver lookupDriver() {
        final int length = CoverageStoreUtils.drivers.length;

        for (int i = 0; i < length; i++) {
            if (CoverageStoreUtils.drivers[i].getName().equals(getType())) {
                return CoverageStoreUtils.drivers[i];
            }
        }

        return null;
    }

    /**
     * toDTO purpose.
     *
     * <p>
     * This method is package visible only, and returns a reference to the
     * GeoServerDTO. This method is unsafe, and should only be used with extreme
     * caution.
     * </p>
     *
     * @return CoverageStoreInfoDTO the generated object
     */
    Object toDTO() {
        CoverageStoreInfoDTO dto = new CoverageStoreInfoDTO();
        dto.setAbstract(getAbstract());
        dto.setEnabled(isEnabled());
        dto.setId(getId());
        dto.setNameSpaceId(getNamesSpacePrefix());
        dto.setType(getType());
        dto.setTitle(getTitle());
        dto.setUrl(getUrl());
        
        //dto.setAbstract(_abstract);
        //dto.setEnabled(enabled);
        //dto.setId(id);
        //dto.setNameSpaceId(nameSpaceId);
        //dto.setType(type);
        //dto.setUrl(url);
        //dto.setTitle(title);

        return dto;
    }

    /**
     * getId purpose.
     *
     * <p>
     * Returns the format's id.
     * </p>
     *
     * @return String the id.
     */
    public String getId() {
        return cs.getName();
        //return id;
    }

    /**
     * DOCUMENT ME !
     *
     * @return Format
     *
     * @throws IllegalStateException
     *             if this CoverageStoreInfo is disabled by configuration
     * @throws NoSuchElementException
     *             if no CoverageStoreInfo is found
     */
    public Driver getDriver() throws IllegalStateException, NoSuchElementException {
        if (!isEnabled()) {
            throw new IllegalStateException("this format is not enabled, check your configuration");
        }

        Driver driver = cs.getDriver();
        if (driver == null) {
            LOGGER.warning("failed to establish connection with " + toString());
            throw new NoSuchElementException("No format found capable of managing " + toString());
        }

        return driver;
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
        return cs.getName();
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
        return cs.getDescription();
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
        return cs.isEnabled();
        //return enabled;
    }

    /**
     * Implement toString.
     *
     * @return String
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new StringBuffer("FormatConfig[type=").append(getType()).append(", enabled=")
                                                     .append(isEnabled()).append(", abstract=")
                                                     .append(getAbstract()).append("]").toString();
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
        return cs.getMetadata().get( key ) != null;
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
        cs.getMetadata().put( key, (Serializable) value);
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
        return cs.getMetadata().get( key );
        //return meta.get(key);
    }

    /**
     * @return Returns the type.
     */
    public String getType() {
        return cs.getType();
        //return type;
    }

    /**
     * @return Returns the url.
     */
    public String getUrl() {
        return cs.getURL();
        //return url;
    }

    /**
     * getNameSpace purpose.
     *
     * @return NameSpaceInfo the namespace for this format.
     */
    public NameSpaceInfo getNameSpace() {
        NamespaceInfo ns = catalog.getNamespaceByPrefix( cs.getWorkspace().getName() );
        return new NameSpaceInfo( ns, catalog );
        //return (NameSpaceInfo) data.getNameSpace(getNamesSpacePrefix());
    }

    /**
     * Access namespace id
     *
     * @return DOCUMENT ME!
     */
    public String getNamesSpacePrefix() {
        return cs.getWorkspace() != null ? cs.getWorkspace().getName() : null;
        //return nameSpaceId;
    }

    public synchronized CoverageAccess getCoverageAccess() {
        try {
            return catalog.getResourcePool().getCoverageAccess(cs, null);
        } 
        catch (IOException e) {
            throw new RuntimeException( e );
        }
    }

    public synchronized CoverageAccess createCoverageAccess(Hints hints) {
        try {
            return catalog.getResourcePool().getCoverageAccess(cs,hints);
        } 
        catch (IOException e) {
            throw new RuntimeException( e );
        }
    }
    
    public void dispose() {
        catalog.getResourcePool().clear( cs );
    }
}
