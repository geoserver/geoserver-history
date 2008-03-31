/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.csv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureStore;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypes;

/**
 * The main entry point for the csv services, acts as a facade for all the logic
 * in this module. It's an abstract class to facilitate testing and reuse, a
 * special subclass that works in a GeoServer only environment and that sports
 * the full functionality is provided as well.
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public abstract class CsvService {
    DDLDelegate ddlDelegate;

    JDBCDataStore store;

    /**
     * Builds a new csv service on top of the provided {@link JDBCDataStore} and
     * DDLDelegate
     * 
     * @param configuration
     */
    public CsvService(JDBCDataStore store, DDLDelegate ddlDelegate) {
        this.store = store;
        this.ddlDelegate = ddlDelegate;
    }

    /**
     * Uploads the specified file into the database, splits it into a set of
     * tables, creates the views, and returns a list of LayerResult objects
     * summarizing the upload outcome for each column
     * 
     * @param csvFile
     */
    public List<LayerResult> configureCsvFile(String targetGeometryTable,
            String joinField, File csvFile) throws IOException {
        // check the geometry layer is known
        FeatureType geomSchema = null;
        try {
            geomSchema = store.getSchema(targetGeometryTable);
        } catch (IOException e) {
            throw new IOException("Could not find the table "
                    + targetGeometryTable + " in the DB");
        }

        // grab the csv file and parse it
        CsvFileReader csvReader = new CsvFileReader(csvFile);
        FeatureType csvSchema = csvReader.getFeatureType();

        // make sure the join field is there in both tables
        if (geomSchema.getAttributeType(joinField) == null)
            throw new IOException("Could not find the joinField " + joinField
                    + " in the geometric table, available attributes are: "
                    + attributeNames(geomSchema));
        if (csvSchema.getAttributeType(joinField) == null)
            throw new IOException("Could not find the joinField " + joinField
                    + " in the data table, available attributes are: "
                    + attributeNames(csvSchema));

        // build the target table for each attribute
        List<LayerResult> result = buildReplaceTables(geomSchema, joinField,
                csvSchema);

        // 

        return result;
    }

    private List<LayerResult> buildReplaceTables(FeatureType geomSchema,
            String joinField, FeatureType csvSchema) throws IOException {
        // first off, drop all tables that we need to replace
        Set<String> names = new HashSet<String>(Arrays.asList(store
                .getTypeNames()));
        Set<String> replaced = new HashSet<String>();
        AttributeType joinAttribute = csvSchema.getAttributeType(joinField);
        FeatureType[] dataSchemas = new FeatureType[csvSchema
                .getAttributeCount() - 1];
        FeatureStore[] stores = new FeatureStore[dataSchemas.length];
        for (int i = 0, j = 0; i < csvSchema.getAttributeCount(); i++) {
            final AttributeType attribute = csvSchema.getAttributeType(i);
            String attName = attribute.getLocalName();

            // do not drop tables whose name is the join field
            if (attName.equals(joinField))
                continue;

            // if needed, drop the view and the data table
            String viewName = attName + "_view";
            if (names.contains(attName)) {
                ddlDelegate.dropView(viewName);
                ddlDelegate.dropTable(attName);
                replaced.add(attName);
            }

            // build the feature type, the table and the view
            try {
                dataSchemas[j] = FeatureTypes.newFeatureType(
                        new AttributeType[] { joinAttribute, attribute },
                        attName);
                store.createSchema(dataSchemas[j]);
                ddlDelegate
                        .createIndex(dataSchemas[j].getTypeName(), joinField);
                ddlDelegate.createView(geomSchema.getTypeName(), attName,
                        joinField, attName, viewName);
                stores[j] = (FeatureStore) store.getFeatureSource(attName);
                j++;
            } catch (Exception e) {
                throw new DataSourceException(
                        "Unexpected error occured during data import", e);
            }
        }

        return null;

    }

    private List<String> attributeNames(FeatureType geomSchema) {
        List<String> names = new ArrayList<String>();
        for (int i = 0; i < geomSchema.getAttributeCount(); i++) {
            names.add(geomSchema.getAttributeType(i).getLocalName());
        }
        return names;
    }

    /**
     * Returns a list of geometric layer names
     * 
     * @return
     */
    public List<String> getGeometryLayers() throws IOException {
        String[] typeNames = store.getTypeNames();
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < typeNames.length; i++) {
            FeatureType ft = store.getSchema(typeNames[i]);
            // geometryless
            if (ft.getDefaultGeometry() == null)
                continue;

            // joined?
            if (isJoinedLayer(ft))
                continue;

            result.add(typeNames[i]);
        }
        return result;
    }

    /**
     * Returns a list of data layer names
     * 
     * @return
     */
    public List<String> getDataLayers() throws IOException {
        String[] typeNames = store.getTypeNames();
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < typeNames.length; i++) {
            FeatureType ft = store.getSchema(typeNames[i]);
            // geometryless -> raw data
            if (ft.getDefaultGeometry() == null)
                continue;

            // joined?
            if (isJoinedLayer(ft))
                result.add(typeNames[i]);
        }
        return result;
    }

    /**
     * Returns true if the provided schema represents a joined view (data +
     * geom)
     * 
     * @param schema
     * @return
     */
    boolean isJoinedLayer(FeatureType schema) {
        return schema.getTypeName().endsWith("View");
    }

    /**
     * Returns the layer description given the GeoServer layer name
     * 
     * @param layerId
     * @return
     */
    public abstract String getLayerDescription(String layerId);
}
