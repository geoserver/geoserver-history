package org.geoserver.csv;

import java.io.File;
import java.net.URISyntaxException;

import org.geotools.feature.FeatureType;

public class CsvServiceTest extends AbstractPostgisTest {

    private CsvService csv;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        csv = new CsvService(store, ddl) {

            @Override
            public String getLayerDescription(String layerId) {
                return "";
            }

        };
    }

    public void testImportCsvFile() throws Exception {
        File csvFile = getCsvFile("/csv/roadAttributes.csv");
        csv.configureCsvFile("road", "id", csvFile);
        // grab the geom column
        FeatureType roadType = store.getSchema("road");
        // grab the surface type and view
        FeatureType surfaceType = store.getSchema("surface");
        FeatureType surfaceViewType = store.getSchema("surface_view");
        assertEquals(1, surfaceType.getAttributeCount());
        // 1 extra column from the view, 1 extra column because the view has no fk
        assertEquals(1 + 1 + roadType.getAttributeCount(), surfaceViewType.getAttributeCount());
        // grab the max speed type and view
        FeatureType maxSpeedType = store.getSchema("maxSpeed");
        FeatureType maxSpeedViewType = store.getSchema("maxSpeed_view");
        assertEquals(1, maxSpeedType.getAttributeCount());
        assertEquals(1 + 1 + roadType.getAttributeCount(), maxSpeedViewType.getAttributeCount());
        
    }

    private File getCsvFile(final String fileLocation)
            throws URISyntaxException {
        return new File(CsvFileReaderHeaderTest.class.getResource(fileLocation)
                .toURI());
    }
}
