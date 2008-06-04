package org.geoserver.security.decorators;

import java.io.IOException;

import org.geotools.data.DataAccess;
import org.geotools.data.FeatureSource;
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

    private static final String READ_ONLY = "This data access is read only";

    public ReadOnlyDataAccess(DataAccess<T, F> delegate) {
        super(delegate);
    }

    @Override
    public FeatureSource<T, F> getFeatureSource(Name typeName) throws IOException {
        return new ReadOnlyFeatureSource(super.getFeatureSource(typeName));
    }

    @Override
    public void createSchema(T featureType) throws IOException {
        throw new IOException(READ_ONLY);
    }

    @Override
    public void updateSchema(Name typeName, T featureType) throws IOException {
        throw new IOException(READ_ONLY);
    }

}
