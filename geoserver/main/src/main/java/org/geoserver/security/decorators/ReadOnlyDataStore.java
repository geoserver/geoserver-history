/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.io.IOException;

import org.geoserver.security.SecureCatalogImpl;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * Given a {@link DataStore} subclass makes sure no write operations can be
 * performed through it
 * 
 * @author Andrea Aime - TOPP
 */
public class ReadOnlyDataStore extends DecoratingDataStore {
    boolean challenge;

    public ReadOnlyDataStore(DataStore delegate, boolean challenge) {
        super(delegate);
        this.challenge = challenge;
    }

    @Override
    public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource(Name typeName)
            throws IOException {
        final FeatureSource<SimpleFeatureType, SimpleFeature> fs = super.getFeatureSource(typeName);
        return wrapFeatureSource(fs);
    }

    @Override
    public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource(String typeName)
            throws IOException {
        final FeatureSource<SimpleFeatureType, SimpleFeature> fs = super.getFeatureSource(typeName);
        return wrapFeatureSource(fs);
            
    }

    @SuppressWarnings("unchecked")
    FeatureSource<SimpleFeatureType, SimpleFeature> wrapFeatureSource(
            final FeatureSource<SimpleFeatureType, SimpleFeature> fs) {
        if (fs == null)
            return null;
        
        // if we only have to conceal we're able to write, a read only wrapper would do
        if(!challenge)
            return new ReadOnlyFeatureSource(fs, false);
        
        // otherwise, not knowing if this call was made to access read or write wise, we have
        // to create silly wrappers that will complain only if the user actually tries
        // to write data
        else if(fs instanceof FeatureLocking)
            return new ReadOnlyFeatureLocking((FeatureLocking) fs, challenge);
        else if(fs instanceof FeatureSource)
            return new ReadOnlyFeatureStore((FeatureStore) fs, challenge);
        else
            return new ReadOnlyFeatureSource(fs, challenge);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
            Filter filter, Transaction transaction) throws IOException {
        throw notifyUnsupportedOperation();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
            Transaction transaction) throws IOException {
        throw notifyUnsupportedOperation();
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName,
            Transaction transaction) throws IOException {
        throw notifyUnsupportedOperation();
    }

    @Override
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        throw notifyUnsupportedOperation();
    }

    @Override
    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        throw notifyUnsupportedOperation();
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
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
