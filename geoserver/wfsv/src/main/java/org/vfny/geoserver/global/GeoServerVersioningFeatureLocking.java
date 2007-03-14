package org.vfny.geoserver.global;

import java.io.IOException;

import org.geotools.data.VersioningFeatureLocking;
import org.geotools.data.VersioningFeatureSource;
import org.geotools.data.VersioningFeatureStore;
import org.geotools.data.postgis.FeatureDiffReader;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureType;
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

    GeoServerVersioningFeatureLocking(VersioningFeatureLocking locking,
            FeatureType schema, Filter definitionQuery,
            CoordinateReferenceSystem forcedCRS) {
        super(locking, schema, definitionQuery, forcedCRS);

    }

    public void rollback(String toVersion, Filter filter) throws IOException {
        ((VersioningFeatureStore) source).rollback(toVersion, filter);
    }

    public FeatureDiffReader getDifferences(String fromVersion,
            String toVersion, Filter filter) throws IOException {
        // TODO: if we are bound to a smaller schema, we should remove the
        // hidden attributes from the differences
        return ((VersioningFeatureSource) source).getDifferences(fromVersion,
                toVersion, filter);
    }

    public FeatureCollection getLog(String fromVersion, String toVersion,
            Filter filter) throws IOException {
        return ((VersioningFeatureSource) source).getLog(fromVersion,
                toVersion, filter);
    }

}
