package org.geoserver.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import org.geotools.data.postgis.PostgisDataStore;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.DataStoreInfo;
import org.vfny.geoserver.global.GeoserverDataDirectory;

public class GeoServerCsvService extends CsvService {
    static final Logger LOGGER = Logging.getLogger("csv");
    private Data catalog;

    public GeoServerCsvService(Data catalog) throws Exception {
        this.catalog = catalog;
        File csvDir = GeoserverDataDirectory.findCreateConfigDir("csv");
        File propFile = new File(csvDir, "csv.properties");

        Properties props = new Properties();
        if (!propFile.exists()) {
            LOGGER.severe("Could not find the "
                    + propFile.getAbsolutePath()
                    + " needed to run the CSV module");
        } else {
            props.load(new FileInputStream(propFile));
        }

        // don't screw up geoserver startup if the property file is not there
        this.store = null;
        DataStoreInfo info = catalog.getDataStoreInfo(props.getProperty("datastore"));
        if(info != null)
            this.store = (PostgisDataStore) info.getDataStore();
        this.ddlDelegate = new PostgisDDLDelegate((PostgisDataStore) store);
    }

    
    /**
     * We assume the description has been sotred in the feature type abstract
     */
    @Override
    public String getLayerDescription(String layerId) throws IOException {
        return catalog.getFeatureTypeInfo(layerId).getAbstract();
    }

}
