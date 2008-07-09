/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.io.IOException;

import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.impl.CatalogImpl;
import org.geoserver.security.SecureCatalogImpl;
import org.geoserver.security.SecureCatalogImpl.Response;
import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;
import org.geotools.data.DataStore;
import org.opengis.util.ProgressListener;

/**
 * Given a {@link DataStoreInfo} makes sure no write operations can be performed
 * through it
 * 
 * @author Andrea Aime - TOPP
 * 
 * @param <T>
 * @param <F>
 */
public class SecuredDataStoreInfo extends DecoratingDataStoreInfo {

    WrapperPolicy policy;

    public SecuredDataStoreInfo(DataStoreInfo delegate, WrapperPolicy policy) {
        super(delegate);
        this.policy = policy;
    }

    @Override
    public DataStore getDataStore(ProgressListener listener) throws IOException {
        final DataStore ds = super.getDataStore(listener);
        if (ds == null)
            return null;
        else if(policy == WrapperPolicy.METADATA)
            throw SecureCatalogImpl.unauthorizedAccess(this.getName());
        else
            return new ReadOnlyDataStore(ds, policy.response == Response.CHALLENGE);
    }

}
