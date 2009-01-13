package org.geoserver.security;

import java.io.File;

import org.acegisecurity.AcegiSecurityException;
import org.geoserver.data.test.MockData;
import org.geoserver.data.util.IOUtils;
import org.geoserver.test.GeoServerTestSupport;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.feature.FeatureCollection;
import org.opengis.filter.Filter;

/**
 * Tests that security rules are applied in a real simulation of a GeoServer
 * startup (loading the Spring context, reading the catalog and whatnot)
 * 
 * @author Andrea Aime
 * 
 */
public class SecureCatalogIntegrationTest extends GeoServerTestSupport {

    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        super.populateDataDirectory(dataDirectory);
        File security = new File(dataDirectory.getDataDirectoryRoot(), "security");
        security.mkdir();
        File layers = new File(security, "layers.properties");
        IOUtils.copy(SecureCatalogIntegrationTest.class
                .getResourceAsStream("functional.properties"), layers);
    }

    public void testFullAccess() throws Exception {
        FeatureSource source = getCatalog().getFeatureTypeInfo(MockData.LINES).getFeatureSource();
        FeatureCollection fc = source.getFeatures();
        FeatureStore store = (FeatureStore) source;
        store.removeFeatures(Filter.INCLUDE);
    }

    public void testCannotRead() throws Exception {
        try {
            getCatalog().getFeatureTypeInfo(MockData.BUILDINGS).getFeatureSource();
            fail("This should have failed with a security exception!");
        } catch (AcegiSecurityException e) {
            // fine, we should not be able to get to the feature source
        }
    }

    public void testCannotWrite() throws Exception {
        FeatureStore fs = (FeatureStore) getCatalog().getFeatureTypeInfo(MockData.DELETES)
                .getFeatureSource();
        try {
            fs.removeFeatures(Filter.INCLUDE);
            fail("This should have failed with a security exception!");
        } catch (AcegiSecurityException e) {
            // fine, we should not be able to get to the feature source
        }
    }
}
