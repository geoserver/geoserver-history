/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureLock;
import org.geotools.data.FeatureLocking;
import org.geotools.data.Query;
import org.geotools.data.postgis.PostgisFeatureLocking;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.filter.Filter;
import java.io.IOException;


/**
 * GeoServer wrapper for backend Geotools2 DataStore.
 * 
 * <p>
 * Support FeatureSource decorator for FeatureTypeInfo that takes care of
 * mapping the FeatureTypeInfo's FeatureSource with the schema and definition
 * query configured for it.
 * </p>
 * 
 * <p>
 * Because GeoServer requires that attributes always be returned in the same
 * order we need a way to smoothly inforce this. Could we use this class to do
 * so? It would need to support writing and locking though.
 * </p>
 *
 * @author Gabriel Roldán
 * @version $Id: GeoServerFeatureLocking.java,v 1.4 2004/01/31 00:27:23 jive Exp $
 */
public class GeoServerFeatureLocking extends GeoServerFeatureStore
    implements FeatureLocking {
    /**
     * Creates a new DEFQueryFeatureLocking object.
     *
     * @param locking GeoTools2 FeatureSource
     * @param schema DOCUMENT ME!
     * @param definitionQuery DOCUMENT ME!
     */
    GeoServerFeatureLocking(FeatureLocking locking, FeatureType schema,
        Filter definitionQuery) {
        super(locking, schema, definitionQuery);
    }

    FeatureLocking locking() {
        return (FeatureLocking) source;
    }

    /**
     * <p>
     * Description ...
     * </p>
     *
     * @param lock
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     *
     * @see org.vfny.geoserver.global.GeoServerFeatureStore#setFeatureLock(org.geotools.data.FeatureLock)
     */
    public void setFeatureLock(FeatureLock lock) {
        if (source instanceof FeatureLocking) {
            ((FeatureLocking) source).setFeatureLock(lock);
        } else {
            throw new UnsupportedOperationException(
                "FeatureTypeConfig does not supports locking");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param query DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws DataSourceException DOCUMENT ME!
     */
    public int lockFeatures(Query query) throws IOException {
        if (source instanceof FeatureLocking) {
            return ((FeatureLocking) source).lockFeatures(query);
        } else {
            throw new DataSourceException(
                "FeatureTypeConfig does not supports locking");
        }
    }

    /**
     * A custom hack for PostgisFeatureLocking?
     *
     * @param feature DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public int lockFeature(Feature feature) throws IOException {
        if (source instanceof PostgisFeatureLocking) {
            return ((PostgisFeatureLocking) source).lockFeature(feature);
        }

        throw new IOException(
            "FeatureTypeConfig does not support single FeatureLock");
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public int lockFeatures(Filter filter) throws IOException {
        filter = makeDefinitionFilter(filter);

        return locking().lockFeatures(filter);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public int lockFeatures() throws IOException {
        return locking().lockFeatures();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void unLockFeatures() throws IOException {
        locking().lockFeatures();
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void unLockFeatures(Filter filter) throws IOException {
        filter = makeDefinitionFilter(filter);

        locking().unLockFeatures(filter);
    }

    public void unLockFeatures(Query query) throws IOException {
        query = makeDefinitionQuery(query);

        locking().lockFeatures(query);
    }
}
