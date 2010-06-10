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
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;

/**
 * A feature store wrappers that waits for the first attempt at writing to throw a security
 * exception notifying the user that the data is not writable. This is unfortunately needed since
 * the GeoTools datastore API does not allow to tell what kind of access a user is trying to make
 * when calling {@link DataAccess#getFeatureSource(org.opengis.feature.type.Name)}.
 * 
 * @author Josh Vote, CSIRO Earth Science and Resource Engineering
 */
public class ReadOnlySimpleFeatureStore extends ReadOnlySimpleFeatureSource implements
        SimpleFeatureStore {

    public ReadOnlySimpleFeatureStore(SimpleFeatureStore delegate, WrapperPolicy policy) {
        super(delegate, policy);
    }


    public Transaction getTransaction() {
        return null;
    }

    public void modifyFeatures(String name, Object attributeValue, Filter filter)
            throws IOException {
        throw unsupportedOperation();
    }

    public void modifyFeatures(String[] names, Object[] attributeValues, Filter filter)
            throws IOException {
        throw unsupportedOperation();
    }

    public List<FeatureId> addFeatures(
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection) throws IOException {
        throw unsupportedOperation();
    }

    public void setFeatures(FeatureReader<SimpleFeatureType, SimpleFeature> reader)
            throws IOException {
        throw unsupportedOperation();
    }


    public void modifyFeatures(Name[] attributeNames, Object[] attributeValues, Filter filter)
            throws IOException {
        throw unsupportedOperation();
    }


    public void modifyFeatures(AttributeDescriptor[] type, Object[] value, Filter filter)
            throws IOException {
        throw unsupportedOperation();
    }


    public void modifyFeatures(Name attributeName, Object attributeValue, Filter filter)
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


    public void setTransaction(Transaction transaction) {
        throw unsupportedOperation();
    }
    
    /**
     * Notifies the caller the requested operation is not supported, using a plain
     * {@link UnsupportedOperationException} in case we have to conceal the fact the data is
     * actually writable, using an Acegi security exception otherwise to force an authentication
     * from the user
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
