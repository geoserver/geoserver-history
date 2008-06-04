package org.geoserver.security.decorators;

import java.io.IOException;

import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
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

    private static final String READ_ONLY = "This data store is read only";

    public ReadOnlyDataStore(DataStore delegate) {
        super(delegate);
    }

    @Override
    public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource(Name typeName)
            throws IOException {
        return new ReadOnlyFeatureSource(super.getFeatureSource(typeName));
    }

    @Override
    public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource(String typeName)
            throws IOException {
        return new ReadOnlyFeatureSource(super.getFeatureSource(typeName));
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
            Filter filter, Transaction transaction) throws IOException {
        throw new IOException(READ_ONLY);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
            Transaction transaction) throws IOException {
        throw new IOException(READ_ONLY);
    }

    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName,
            Transaction transaction) throws IOException {
        throw new IOException(READ_ONLY);
    }

    @Override
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        throw new IOException(READ_ONLY);
    }

    @Override
    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        throw new IOException(READ_ONLY);
    }

    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        throw new IOException(READ_ONLY);
    }

}
