/* 
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog.hibernate.beans;


import java.io.IOException;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.impl.DataStoreInfoImpl;
import org.geoserver.hibernate.Hibernable;
import org.geotools.data.DataAccess;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.util.ProgressListener;

/**
 * Mainly copied from DataStoreInfoImpl.
 * <BR>
 * Hibernate needs a void constructor and getters/setters for all the persisting
 * attributes. GS original implementations did not provide all of the
 * needed accessors.
 *
 * @author ETj <etj at geo-solutions.it>
 */

public class DataStoreInfoImplHb 
        extends DataStoreInfoImpl
        implements Hibernable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7131867236029677990L;

	public DataStoreInfoImplHb(Catalog catalog, String id) {
        super(catalog, id);
    }

    public DataStoreInfoImplHb(Catalog catalog) {
        super(catalog);
    }

    public DataStoreInfoImplHb() {
    }

    @Override
    public DataAccess<? extends FeatureType, ? extends Feature> getDataStore(ProgressListener listener) throws IOException {

        if(catalog == null) throw new NullPointerException("catalog is null");
        if(catalog.getResourcePool() == null) throw new NullPointerException("resourcepool is null");

        return super.getDataStore(listener);
    }

    @Override
    public String toString() {
        return new StringBuilder(getClass().getSimpleName())
                    .append('[')
                    .append(getWorkspace() != null ? getWorkspace().getName() : "--").append(':')
                    .append(name)
                    .append(']')
                .toString();
    }

}
