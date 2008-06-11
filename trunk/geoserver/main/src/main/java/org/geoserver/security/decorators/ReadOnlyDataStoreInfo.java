package org.geoserver.security.decorators;

import java.io.IOException;

import org.geoserver.catalog.DataStoreInfo;
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
public class ReadOnlyDataStoreInfo extends DecoratingDataStoreInfo {

    public ReadOnlyDataStoreInfo(DataStoreInfo delegate) {
        super(delegate);
    }

    @Override
    public DataStore getDataStore(ProgressListener listener) throws IOException {
        final DataStore ds = super.getDataStore(listener);
        if (ds == null)
            return null;
        else
            return new ReadOnlyDataStore(ds);
    }

}
