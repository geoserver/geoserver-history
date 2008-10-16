/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.impl;

import java.io.IOException;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geotools.data.DataStore;
import org.opengis.util.ProgressListener;

/**
 * Default implementation of {@link DataStoreInfo}.
 * 
 */
public class DataStoreInfoImpl extends StoreInfoImpl implements DataStoreInfo {

    protected DataStoreInfoImpl() {
        super(null);
    }

    public DataStoreInfoImpl(Catalog catalog) {
        super(catalog);
    }

    public DataStoreInfoImpl(Catalog catalog,String id) {
        super(catalog,id);
    }

    public DataStore getDataStore(ProgressListener listener) throws IOException {
        return catalog.getResourcePool().getDataStore(this);
    }

    public boolean equals(Object obj) {
        if (!( obj instanceof DataStoreInfo ) ) {
            return false;
        }
        
        return super.equals( obj );
    }
}
