/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.csv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geoserver.csv.LayerResult.LayerOperation;
import org.geotools.data.DataSourceException;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.feature.AttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureIterator;
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
    /**
     * Subclasses can use this one, they will have to init the store and
     * ddlDelegate fields manually
     */
    protected CsvService() {
        // no init, just for subclasses
    }

    protected abstract JDBCDataStore getDataStore();

    protected abstract DDLDelegate getDDLDelegate();

    /**
     * Returns a list of geometric layer names
     * 
     * @return
     */
    public List<String> getGeometryLayers() throws IOException {
        JDBCDataStore store = getDataStore();
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
        JDBCDataStore store = getDataStore();
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
     * Uploads the specified file into the database, splits it into a set of
     * tables, creates the views, and returns a list of LayerResult objects
     * summarizing the upload outcome for each column
     * 
     * @param csvFile
     */
    public synchronized List<LayerResult> configureCsvFile(String targetGeometryTable,
            String joinField, File csvFile) throws IOException {
        JDBCDataStore store = getDataStore();

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
                    + " in the geometric table" + targetGeometryTable
                    + " available attributes are: "
                    + attributeNames(geomSchema));
        if (csvSchema.getAttributeType(joinField) == null)
            throw new IOException("Could not find the joinField " + joinField
                    + " in the csv file, available attributes are: "
                    + attributeNames(csvSchema));

        // build the target table for each attribute
        List<LayerResult> result = buildReplaceTables(geomSchema, joinField,
                csvReader);

        // import the data into the tables
        importData(csvReader, joinField);

        return result;
    }

    /**
     * Returns the layer description given the GeoServer layer name
     * 
     * @param layerId
     * @return
     */
    public abstract String getLayerDescription(String layerId)
            throws IOException;

    private void importData(CsvFileReader csvReader, String joinField)
            throws IOException {
        JDBCDataStore store = getDataStore();
        FeatureType csvSchema = csvReader.getFeatureType();

        // first off, grab the feature stores used for import
        Transaction t = new DefaultTransaction();
        Map<String, FeatureWriter> stores = new HashMap<String, FeatureWriter>();
        for (int i = 0; i < csvSchema.getAttributeCount(); i++) {
            final AttributeType attribute = csvSchema.getAttributeType(i);
            String attName = attribute.getLocalName();

            if (attName.equals(joinField))
                continue;
            stores.put(attName, store.getFeatureWriterAppend(attName, t));
        }

        // now read the features, split them into sub-features, and import each
        // into the associated feature store
        FeatureIterator it = null;
        try {
            it = csvReader.getFeatures();
            while (it.hasNext()) {
                Feature csvFeature = it.next();
                final Object joinFieldValue = csvFeature
                        .getAttribute(joinField);

                for (String attName : stores.keySet()) {
                    // build the shaved feature for insertion in the data table
                    FeatureWriter writer = stores.get(attName);
                    Feature f = writer.next();

                    f.setAttribute(joinField, joinFieldValue);
                    f.setAttribute(attName, csvFeature.getAttribute(attName));
                    writer.write();
                }
            }
            t.commit();
        } catch (Exception e) {
            t.rollback();
            throw new DataSourceException("Unexpected feature creation issue",
                    e);
        } finally {
            t.close();
            if (it != null)
                it.close();
            for (FeatureWriter writer : stores.values()) {
                writer.close();
            }
        }
    }

    /**
     * Manage the schemas:
     * <ul>
     * <li>drop all the tables that do need replacement</li>
     * <li>build the new ones, add the indexes required to get good performance
     * off joins</li>
     * <li>build the joined views</li>
     * </ul>
     * 
     * @param geomSchema
     * @param joinField
     * @param csvSchema
     * @return
     * @throws IOException
     */
    private List<LayerResult> buildReplaceTables(FeatureType geomSchema,
            String joinField, CsvFileReader csvReader) throws IOException {
        JDBCDataStore store = getDataStore();
        DDLDelegate ddlDelegate = getDDLDelegate();
        FeatureType csvSchema = csvReader.getFeatureType();
        AttributeType joinAttribute = csvSchema.getAttributeType(joinField);
        FeatureType[] dataSchemas = new FeatureType[csvSchema
                .getAttributeCount() - 1];
        Set<String> names = new HashSet<String>(Arrays.asList(store
                .getTypeNames()));
        List<LayerResult> results = new ArrayList<LayerResult>();
        for (int i = 0, j = 0; i < csvSchema.getAttributeCount(); i++) {
            final AttributeType attribute = csvSchema.getAttributeType(i);
            String attName = attribute.getLocalName();

            // do not drop tables whose name is the join field
            if (attName.equals(joinField))
                continue;

            // if needed, drop the view and the data table (and also register
            // the result)
            String viewName = attName + "_view";

            results.add(new LayerResult(viewName, csvReader.getDescription(i)));
            if (names.contains(attName)) {
                ddlDelegate.dropView(viewName);
                ddlDelegate.dropTable(attName);
                results.get(j).setOperation(LayerOperation.REPLACED);
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
                j++;
            } catch (Exception e) {
                throw new DataSourceException(
                        "Unexpected error occured during data import", e);
            }
        }

        return results;

    }

    private List<String> attributeNames(FeatureType geomSchema) {
        List<String> names = new ArrayList<String>();
        for (int i = 0; i < geomSchema.getAttributeCount(); i++) {
            names.add(geomSchema.getAttributeType(i).getLocalName());
        }
        return names;
    }

    /**
     * Returns true if the provided schema represents a joined view (data +
     * geom)
     * 
     * @param schema
     * @return
     */
    boolean isJoinedLayer(FeatureType schema) {
        return schema.getTypeName().endsWith("_view");
    }

}
