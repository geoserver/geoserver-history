/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.io.IOException;

import org.geoserver.security.SecureCatalogImpl;
import org.geotools.data.DataAccess;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

/**
 * Given a {@link DataAccess} subclass makes sure no write operations can be
 * performed through it
 * 
 * @author Andrea Aime - TOPP
 * 
 * @param <T>
 * @param <F>
 */
public class ReadOnlyDataAccess<T extends FeatureType, F extends Feature> extends
        DecoratingDataAccess<T, F> {

    static final String READ_ONLY = "This data access is read only";
    boolean challenge;

    public ReadOnlyDataAccess(DataAccess<T, F> delegate, boolean challenge) {
        super(delegate);
        this.challenge = challenge;
    }

    @Override
    public FeatureSource<T, F> getFeatureSource(Name typeName) throws IOException {
        final FeatureSource<T, F> fs = super.getFeatureSource(typeName);
        if (fs == null)
            return null;
        
        // if we only have to conceal writability a wrapper would do
        if(!challenge)
            return new ReadOnlyFeatureSource(fs, false);
        
        // otherwise, not knowing if this call was made to access read or write wise, we have
        // to create silly wrappers that will complain only if the user actually tries
        // to write data
        else if(fs instanceof FeatureLocking)
            return new ReadOnlyFeatureLocking((FeatureLocking) fs, challenge);
        else if(fs instanceof FeatureStore)
            return new ReadOnlyFeatureStore((FeatureStore) fs, challenge);
        else
            return new ReadOnlyFeatureSource(fs, challenge);
    }

    @Override
    public void createSchema(T featureType) throws IOException {
        throw notifyUnsupportedOperation();
    }

    @Override
    public void updateSchema(Name typeName, T featureType) throws IOException {
        throw notifyUnsupportedOperation();
    }
    
    /**
     * Notifies the caller the requested operation is not supported, using a plain {@link UnsupportedOperationException}
     * in case we have to conceal the fact the data is actually writable, using an Acegi security exception otherwise
     * to force an authentication from the user
     */
    RuntimeException notifyUnsupportedOperation() {
        if(challenge) {
            return SecureCatalogImpl.unauthorizedAccess();
        } else
            return new UnsupportedOperationException("This datastore is read only");
    }

}
