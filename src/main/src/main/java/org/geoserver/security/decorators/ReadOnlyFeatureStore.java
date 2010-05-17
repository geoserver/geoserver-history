/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.security.decorators;

import java.io.IOException;
import java.util.List;

import org.geoserver.security.SecureCatalogImpl;
import org.geoserver.security.SecureCatalogImpl.Response;
import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;
import org.geotools.data.DataAccess;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureStore;
import org.geotools.data.Transaction;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;

/**
 * A feature store wrappers that waits for the first attempt at writing to throw
 * a security exception notifying the user that the data is not writable. This
 * is unfortunately needed since the GeoTools datastore API does not allow to
 * tell what kind of access a user is trying to make when calling
 * {@link DataAccess#getFeatureSource(org.opengis.feature.type.Name)}.
 * 
 * @author Andrea Aime - TOPP
 * 
 * @param <T>
 * @param <F>
 */
public class ReadOnlyFeatureStore<T extends FeatureType, F extends Feature> extends
        ReadOnlyFeatureSource<T, F> implements FeatureStore<T, F> {

    public ReadOnlyFeatureStore(FeatureStore delegate, WrapperPolicy policy) {
        super(delegate, policy);
    }

    public List<FeatureId> addFeatures(FeatureCollection<T, F> collection) throws IOException {
        throw unsupportedOperation();
    }

    public Transaction getTransaction() {
        return null;
    }

    public void modifyFeatures(AttributeDescriptor[] type, Object[] value, Filter filter)
            throws IOException {
        throw unsupportedOperation();
    }

    public void modifyFeatures(AttributeDescriptor type, Object value, Filter filter)
            throws IOException {
        throw unsupportedOperation();
    }

    public void removeFeatures(Filter filter) throws IOException {
        throw unsupportedOperation();
    }

    public void setFeatures(FeatureReader<T, F> reader) throws IOException {
        throw unsupportedOperation();
    }

    public void setTransaction(Transaction transaction) {
        throw unsupportedOperation();
    }

    /**
     * Notifies the caller the requested operation is not supported, using a
     * plain {@link UnsupportedOperationException} in case we have to conceal
     * the fact the data is actually writable, using an Acegi security exception
     * otherwise to force an authentication from the user
     */
    protected RuntimeException unsupportedOperation() {
        String typeName = getSchema().getName().getLocalPart();
        if (policy.response == Response.CHALLENGE) {
            return SecureCatalogImpl.unauthorizedAccess(typeName);
        } else {
            return new UnsupportedOperationException(typeName + " is read only");
        }
    }

}
