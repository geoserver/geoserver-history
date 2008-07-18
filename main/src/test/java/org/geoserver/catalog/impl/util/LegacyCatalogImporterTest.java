package org.geoserver.catalog.impl.util;

import org.geoserver.catalog.impl.CatalogImpl;
import org.geoserver.catalog.util.LegacyCatalogImporter;
import org.geoserver.data.test.MockData;

import junit.framework.TestCase;

public class LegacyCatalogImporterTest extends TestCase {

    public void testMissingFeatureTypes() throws Exception {
        MockData mockData = new MockData();
        mockData.setUp();
        
        mockData.getFeatureTypesDirectory().delete();
        LegacyCatalogImporter importer = new LegacyCatalogImporter(new CatalogImpl());
        importer.imprt(mockData.getDataDirectoryRoot());
    }
    
    public void testMissingCoverages() throws Exception {
        MockData mockData = new MockData();
        mockData.setUp();
        
        mockData.getCoveragesDirectory().delete();
        LegacyCatalogImporter importer = new LegacyCatalogImporter(new CatalogImpl());
        importer.imprt(mockData.getDataDirectoryRoot());
    }
}
