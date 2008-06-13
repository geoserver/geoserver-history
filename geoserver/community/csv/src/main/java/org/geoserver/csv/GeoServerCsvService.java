/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.postgis.PostgisDataStore;
import org.geotools.feature.FeatureType;
import org.geotools.styling.SLDTransformer;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.DataStoreInfo;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.dto.DataDTO;
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
        if (store == null)
            return null;
        return new PostgisDDLDelegate((PostgisDataStore) store);
    }

    /**
     * Overrides the base class to add GeoServer layer configuration
     */
    @Override
    public List<LayerResult> configureCsvFile(String targetGeometryTable,
            String joinField, File csvFile) throws IOException {
        List<LayerResult> results = super.configureCsvFile(
                targetGeometryTable, joinField, csvFile);
        try {
            FeatureTypeConfig geomConfig = dataConfig
                    .getFeatureTypeConfig(dataStoreId + ":"
                            + targetGeometryTable);
            for (LayerResult layerResult : results) {
                final String qualifiedName = dataStoreId + ":"
                        + layerResult.getLayerName();
                FeatureTypeConfig ftConfig = dataConfig
                        .getFeatureTypeConfig(qualifiedName);

                if (ftConfig == null) {
                    final FeatureType schema = getDataStore().getSchema(
                            layerResult.getLayerName());
                    ftConfig = new FeatureTypeConfig(dataStoreId, schema, true);
                }
                ftConfig.setAbstract(layerResult.getLayerDescription());
                ftConfig.setTitle(layerResult.getLayerDescription());
                ftConfig.setSRSHandling(geomConfig.getSRSHandling());
                ftConfig.setSRS(geomConfig.getSRS());
                ftConfig.setLatLongBBox(geomConfig.getLatLongBBox());
                ftConfig.setNativeBBox(geomConfig.getNativeBBox());

                // handle style
                File styleFile = new File(GeoserverDataDirectory
                        .findCreateConfigDir("styles"), layerResult
                        .getLayerName());
                SLDTransformer tx = new SLDTransformer();
                tx.setIndentation(2);
                tx.transform(catalog.getStyle(geomConfig.getDefaultStyle()),
                        new FileOutputStream(styleFile));
                StyleConfig newStyle = new StyleConfig();
                newStyle.setId(layerResult.getLayerName());
                newStyle.setFilename(styleFile);
                dataConfig.addStyle(newStyle.getId(), newStyle);
                ftConfig.setDefaultStyle(newStyle.getId());

                dataConfig.removeFeatureType(qualifiedName);
                dataConfig.addFeatureType(qualifiedName, ftConfig);
            }
            catalog.load(dataConfig.toDTO());
            XMLConfigWriter.store((DataDTO) dataConfig.toDTO(),
                    GeoserverDataDirectory.getGeoserverDataDirectory());
        } catch (Exception e) {
            throw new DataSourceException(
                    "Error occurred trying to write out the GeoServer configuration",
                    e);
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

    public String getDataStoreId() {
        return dataStoreId;
    }

    public void setDataStoreId(String dataStoreId) {
        this.dataStoreId = dataStoreId;
    }
    
    @Override
    public List<String> getDataLayers() throws IOException {
        return filterLayers(super.getDataLayers());
    }

    private List<String> filterLayers(List<String> result) {
        // make sure we return only those known to GeoServer
        List<String> filtered = new ArrayList<String>();
        for (int i = 0; i < result.size(); i++) {
            try {
                catalog.getFeatureTypeInfo(result.get(i));
                filtered.add(result.get(i));
            } catch(Exception e) {
                //
            }
        }
        return filtered;
    }
    
    @Override
    public List<String> getGeometryLayers() throws IOException {
        return filterLayers(super.getGeometryLayers());
    }

}
