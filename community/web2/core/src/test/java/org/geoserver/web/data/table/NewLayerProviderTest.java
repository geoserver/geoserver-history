package org.geoserver.web.data.table;

import org.geoserver.data.test.MockData;
import org.geoserver.web.GeoServerWicketTestSupport;


public class NewLayerProviderTest extends GeoServerWicketTestSupport {
    
    @Override
    protected void populateDataDirectory(MockData dataDirectory)
            throws Exception {
        super.populateDataDirectory(dataDirectory);
        dataDirectory.addWellKnownCoverageTypes();
    }
    
    public void testFeatureType() {
        NewLayerPageProvider provider = new NewLayerPageProvider(MockData.CITE_PREFIX);
        provider.setShowPublished(true);
        assertTrue(provider.size() > 0);
        provider.setShowPublished(false);
        assertEquals(0, provider.size());
    }
    
    public void testCoverages() {
        NewLayerPageProvider provider = new NewLayerPageProvider(MockData.TASMANIA_DEM.getLocalPart());
        provider.setShowPublished(true);
        assertTrue(provider.size() > 0);
        provider.setShowPublished(false);
        assertEquals(0, provider.size());
    }

}
