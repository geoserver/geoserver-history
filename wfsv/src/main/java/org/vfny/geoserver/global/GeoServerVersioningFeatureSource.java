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
 * Versioning wrapper that preserves the versioning nature of a feature source
 * 
 * @TODO: once the experiment is over, move this back to the main module
 * @author Andrea Aime, TOPP
 * 
 */
public class GeoServerVersioningFeatureSource extends GeoServerFeatureSource
        implements VersioningFeatureSource {

    GeoServerVersioningFeatureSource(VersioningFeatureSource source,
            FeatureType schema, Filter definitionQuery,
            CoordinateReferenceSystem forcedCRS) {
        super(source, schema, definitionQuery, forcedCRS);
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

    /**
     * Factory that make the correct decorator for the provided featureSource.
     * 
     * <p>
     * This factory method is public and will be used to create all required
     * subclasses. By comparison the constructors for this class have package
     * visibiliy.
     * </p>
     * 
     * @param featureSource
     * @param schema
     * @param definitionQuery
     * @param forcedCRS
     *            Geometries will be forced to this CRS (or null, if no forcing
     *            is needed)
     * 
     * @return
     */
    public static GeoServerFeatureSource create(
            VersioningFeatureSource featureSource, FeatureType schema,
            Filter definitionQuery, CoordinateReferenceSystem forcedCRS) {
        if (featureSource instanceof VersioningFeatureLocking) {
            return new GeoServerVersioningFeatureLocking(
                    (VersioningFeatureLocking) featureSource, schema,
                    definitionQuery, forcedCRS);
        } else if (featureSource instanceof VersioningFeatureStore) {
            return new GeoServerVersioningFeatureStore(
                    (VersioningFeatureStore) featureSource, schema,
                    definitionQuery, forcedCRS);
        }

        return new GeoServerVersioningFeatureSource(featureSource, schema,
                definitionQuery, forcedCRS);
    }

}
