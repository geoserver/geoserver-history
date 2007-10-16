/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.geotools.data.VersioningFeatureLocking;
import org.geotools.data.VersioningFeatureSource;
import org.geotools.data.VersioningFeatureStore;
import org.geotools.data.postgis.FeatureDiffReader;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import java.io.IOException;


/**
 * Versioning wrapper that preserves the versioning nature of a feature locking
 *
 * @TODO: once the experiment is over, move this back to the main module
 * @author Andrea Aime, TOPP
 *
 */
public class GeoServerVersioningFeatureLocking extends GeoServerFeatureLocking
    implements VersioningFeatureLocking {
    GeoServerVersioningFeatureLocking(VersioningFeatureLocking locking, FeatureType schema,
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

    public FeatureCollection getLog(String fromVersion, String toVersion, Filter filter, String[] users, int maxFeatures)
        throws IOException {
        return ((VersioningFeatureSource) source).getLog(fromVersion, toVersion, filter, users, maxFeatures);
    }
}
