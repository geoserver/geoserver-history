/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.IOException;

import org.geotools.data.DataSourceException;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureDiffReader;
import org.geotools.data.Query;
import org.geotools.data.VersioningFeatureLocking;
import org.geotools.data.VersioningFeatureSource;
import org.geotools.data.VersioningFeatureStore;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * Versioning wrapper that preserves the versioning nature of a feature locking
 *
 * @TODO: once the experiment is over, move this back to the main module
 * @author Andrea Aime, TOPP
 *
 */
public class GeoServerVersioningFeatureLocking extends GeoServerFeatureLocking
    implements VersioningFeatureLocking {
    GeoServerVersioningFeatureLocking(VersioningFeatureLocking locking, SimpleFeatureType schema,
        Filter definitionQuery, CoordinateReferenceSystem declaredCRS, int srsHandling) {
        super(locking, schema, definitionQuery, declaredCRS, srsHandling);
    }

    public void rollback(String toVersion, Filter filter, String[] users)
        throws IOException {
        ((VersioningFeatureStore) source).rollback(toVersion, filter, users);
    }

    public FeatureDiffReader getDifferences(String fromVersion, String toVersion, Filter filter, String[] users)
        throws IOException {
        // TODO: if we are bound to a smaller schema, we should remove the
        // hidden attributes from the differences
        return ((VersioningFeatureSource) source).getDifferences(fromVersion, toVersion, filter, users);
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> getLog(String fromVersion,
            String toVersion, Filter filter, String[] users, int maxFeatures)
        throws IOException {
        return ((VersioningFeatureSource) source).getLog(fromVersion, toVersion, filter, users, maxFeatures);
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> getVersionedFeatures(Query query)
            throws IOException {
        final VersioningFeatureSource versioningSource = ((VersioningFeatureSource) source);
        Query newQuery = adaptQuery(query, versioningSource.getVersionedFeatures().getSchema());
        
        CoordinateReferenceSystem targetCRS = query.getCoordinateSystemReproject();
        try {
            //this is the raw "unprojected" feature collection
            
            FeatureCollection<SimpleFeatureType, SimpleFeature> fc = versioningSource
                    .getVersionedFeatures(newQuery);

            return applyProjectionPolicies(targetCRS, fc);
        } catch (Exception e) {
            throw new DataSourceException(e);
        }
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> getVersionedFeatures(Filter filter)
            throws IOException {
        return getFeatures(new DefaultQuery(schema.getTypeName(), filter));
    }

    public FeatureCollection<SimpleFeatureType, SimpleFeature> getVersionedFeatures()
            throws IOException {
        return getFeatures(Query.ALL);
    }
    
    public String getVersion() throws IOException, UnsupportedOperationException {
        return ((VersioningFeatureStore) source).getVersion();
    }
}
