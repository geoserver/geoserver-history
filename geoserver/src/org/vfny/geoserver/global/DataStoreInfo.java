/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.vfny.geoserver.global.dto.DataStoreInfoDTO;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Logger;


/**
 * This is the configuration iformation for one DataStore. This class can also
 * generate real datastores.
 *
 * @author Gabriel Roldán
 * @author dzwiers
 * @version $Id: DataStoreInfo.java,v 1.1.2.6 2004/01/09 17:15:30 dmzwiers Exp $
 */
public class DataStoreInfo extends GlobalLayerSupertype {
    /** for logging */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");

    /** DataStoreInfo we are representing */
    private DataStore dataStore = null;

    /** ref to the parent class's collection */
    private Data data;

    /** The dataStore information for this object */
    private DataStoreInfoDTO dsc;

    /**
     * DataStoreInfo constructor.
     * 
     * <p>
     * Stores the specified data for later use.
     * </p>
     *
     * @param config DataStoreInfoDTO the current configuration to use.
     * @param data Data a ref to use later to look up related informtion
     */
    public DataStoreInfo(DataStoreInfoDTO config, Data data) {
        dsc = config;
        this.data = data;
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
        return dsc;
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
        return dsc.getId();
    }

    /**
     * By now just uses DataStoreFinder to find a new instance of a
     * DataStoreInfo capable of process <code>connectionParams</code>. In the
     * future we can see if it is better to cache or pool DataStores for
     * performance, but definitely we shouldn't maintain a single
     * DataStoreInfo as instance variable for synchronizing reassons
     * 
     * <p></p>
     * 
     * <p>
     * JG: Umm we actually require a single DataStoreInfo for for locking &
     * transaction support to work. DataStoreInfo is expected to be thread
     * aware (that is why it has Transaction Support).
     * </p>
     *
     * @return DataStore
     *
     * @throws IOException if a datastore is found but can not be created for
     *         the passed parameters
     * @throws IllegalStateException if this DataStoreInfo is disabled by
     *         configuration
     * @throws NoSuchElementException if no DataStoreInfo is found
     * @throws DataSourceException
     */
    public synchronized DataStore getDataStore()
        throws IOException, IllegalStateException, NoSuchElementException {
        if (!isEnabled()) {
            throw new IllegalStateException(
                "this datastore is not enabled, check your configuration files");
        }

        if (dataStore == null) {
            try {
                dataStore = DataStoreFinder.getDataStore(dsc
                        .getConnectionParams());
                LOGGER.fine("connection established by " + toString());
            } catch (Throwable ex) {
                throw new DataSourceException("can't create the datastore "
                    + getId() + ": " + ex.getClass().getName() + ": "
                    + ex.getMessage(), ex);
            }

            if (dataStore == null) {
                LOGGER.fine("failed to establish connection with " + toString());
                throw new NoSuchElementException(
                    "No datastore found capable of managing " + toString());
            }
        }

        return dataStore;
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
        return dsc.getTitle();
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
        return dsc.getAbstract();
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
        return dsc.isEnabled();
    }

    /**
     * getNameSpace purpose.
     * 
     * <p>
     * Returns the namespace for this datastore.
     * </p>
     *
     * @return NameSpace the namespace for this datastore.
     */
    public NameSpace getNameSpace() {
        return (NameSpace) data.getNameSpace(dsc.getNameSpaceId());
    }

    /**
     * Implement toString.
     *
     * @return String
     *
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new StringBuffer("DataStoreConfig[namespace=").append(getNameSpace()
                                                                         .getPrefix())
                                                             .append(", enabled=")
                                                             .append(isEnabled())
                                                             .append(", abstract=")
                                                             .append(getAbstract())
                                                             .append(", connection parameters=")
                                                             .append(dsc
            .getConnectionParams()).append("]").toString();
    }
}
