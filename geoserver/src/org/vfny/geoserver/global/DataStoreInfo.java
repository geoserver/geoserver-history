/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.vfny.geoserver.global.dto.data.*;

/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: DataStoreInfo.java,v 1.1.2.1 2004/01/05 22:14:40 dmzwiers Exp $
 */
public class DataStoreInfo extends Abstract {
    
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.config");


    /** DataStoreInfo we are representing */
    private DataStore dataStore = null;
    
    private DataStoreInfoDTO dsc;
    
    public DataStoreInfo(DataStoreInfoDTO config){
    	dsc = config;
    }
    
    Object getDTO(){
    	return dsc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getId() {
        return dsc.getId();
    }

    /**
     * By now just uses DataStoreFinder to find a new instance of a DataStoreInfo
     * capable of process <code>connectionParams</code>. In the future we can
     * see if it is better to cache or pool DataStores for performance, but
     * definitely we shouldn't maintain a single DataStoreInfo as instance
     * variable for synchronizing reassons
     * <p>
     * <p>
     * JG: Umm we actually require a single DataStoreInfo for for locking &
     * transaction support to work. DataStoreInfo is expected
     * to be thread aware (that is why it has Transaction Support).
     * </p>
     * @return DOCUMENT ME!
     *
     * @throws IOException if a datastore is found but can not be created for
     *         the passed parameters
     * @throws IllegalStateException if this DataStoreInfo is disabled by
     *         configuration
     * @throws NoSuchElementException if no DataStoreInfo is found
     * @throws DataSourceException DOCUMENT ME!
     */
    public synchronized DataStore getDataStore()
        throws IOException, IllegalStateException, NoSuchElementException {
        if (!isEnabled()) {
            throw new IllegalStateException(
                "this datastore is not enabled, check your configuration files");
        }
        if( dataStore == null ){
            try {
                dataStore = DataStoreFinder.getDataStore(dsc.getConnectionParams());
		LOGGER.fine("connection established by " + toString());
            } catch (Throwable ex) {
                throw new DataSourceException("can't create the datastore " +
                                            getId() + ": " +
                                            ex.getClass().getName() + ": " +
                                            ex.getMessage(), ex);
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
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTitle() {
        return dsc.getTitle();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAbstract() {
        return dsc.getAbstract();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEnabled() {
        return dsc.isEnabled();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NameSpace getNameSpace() {
    	return GeoServer.getInstance().getData().getNameSpace(dsc.getNameSpaceId());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return new StringBuffer("DataStoreConfig[namespace=").append(getNameSpace()
            .getPrefix()).append(", enabled=").append(isEnabled())
                                                             .append(", abstract=")
                                                             .append(getAbstract())
                                                             .append(", connection parameters=")
                                                             .append(dsc.getConnectionParams())
                                                             .append("]")
                                                             .toString();
    }
}
