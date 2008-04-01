package org.geoserver.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.postgis.PostgisDataStore;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.DataStoreInfo;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.global.xml.XMLConfigWriter;

public class GeoServerCsvService extends CsvService {
    static final Logger LOGGER = Logging.getLogger("csv");

    private Data catalog;

    private DataConfig dataConfig;

    private String dataStoreId;

    public GeoServerCsvService(Data catalog, DataConfig dataConfig)
            throws Exception {
        this.catalog = catalog;
        this.dataConfig = dataConfig;
        File csvDir = GeoserverDataDirectory.findCreateConfigDir("csv");
        File propFile = new File(csvDir, "csv.properties");

        Properties props = new Properties();
        if (!propFile.exists()) {
            LOGGER.severe("Could not find the " + propFile.getAbsolutePath()
                    + " needed to run the CSV module");
        } else {
            props.load(new FileInputStream(propFile));
        }

        // don't screw up geoserver startup if the property file is not there
        dataStoreId = props.getProperty("datastore");
    }
    
    @Override
    protected JDBCDataStore getDataStore() {
        DataStoreInfo info = catalog.getDataStoreInfo(dataStoreId);
        if (info != null)
            return (PostgisDataStore) info.getDataStore();
        else
            return null;
    }
    
    @Override
    protected DDLDelegate getDDLDelegate() {
        JDBCDataStore store = getDataStore();
        if(store == null)
            return null;
        return new PostgisDDLDelegate((PostgisDataStore) store);
    }

    /**
     * Overrides the base class to add GeoServer layer configuration
     */
    @Override
    public List<LayerResult> configureCsvFile(String targetGeometryTable,
            String joinField, File csvFile) throws IOException {
        List<LayerResult> results = super.configureCsvFile(targetGeometryTable,
                joinField, csvFile);

        FeatureTypeConfig geomConfig = dataConfig
                .getFeatureTypeConfig(dataStoreId + ":" + targetGeometryTable);
        for (LayerResult layerResult : results) {
            final String qualifiedName = dataStoreId + ":"
                    + layerResult.getLayerName();
            FeatureTypeConfig ftConfig = dataConfig.getFeatureTypeConfig(qualifiedName); 

            if(ftConfig == null) {
                ftConfig = new FeatureTypeConfig(
                        dataStoreId,
                        getDataStore().getSchema(layerResult.getLayerName()), true);
            }
            ftConfig.setAbstract(layerResult.getLayerDescription());
            ftConfig.setTitle(layerResult.getLayerDescription());
            // ftConfig.setDataStoreId(dataStoreId);
            ftConfig.setDefaultStyle(geomConfig.getDefaultStyle());
            ftConfig.setSRSHandling(geomConfig.getSRSHandling());
            ftConfig.setSRS(geomConfig.getSRS());
            ftConfig.setLatLongBBox(geomConfig.getLatLongBBox());
            ftConfig.setNativeBBox(geomConfig.getNativeBBox());

            dataConfig.removeFeatureType(qualifiedName);
            dataConfig.addFeatureType(qualifiedName, ftConfig);
        }
        catalog.load(dataConfig.toDTO());
        try {
            XMLConfigWriter.store((DataDTO) dataConfig.toDTO(),
                    GeoserverDataDirectory.getGeoserverDataDirectory());
        } catch (ConfigurationException e) {
            throw new DataSourceException("Error occurred trying to write out the GeoServer configuration", e);
        }

        return results;
    }

    /**
     * We assume the description has been stored in the feature type abstract
     */
    @Override
    public String getLayerDescription(String layerId) throws IOException {
        return catalog.getFeatureTypeInfo(layerId).getAbstract();
    }

}
