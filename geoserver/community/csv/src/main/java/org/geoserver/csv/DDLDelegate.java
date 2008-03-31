package org.geoserver.csv;

import java.io.IOException;

import org.geotools.feature.FeatureType;

public interface DDLDelegate {

    /**
     * Creates the schema for the specified feature type (which is supposed to
     * be geometryless)
     * 
     * @param type
     */
    public void createSchema(FeatureType type, String primaryKey)
            throws IOException;

    /**
     * Creates a view by joining the geometryTable and the dataTable using an
     * equi-join on the join field
     * 
     * @param geometryTable
     * @param dataTable
     * @param joinField
     * @param dataField
     * @param viewName
     */
    public void createView(String geometryTable, String dataTable,
            String joinField, String dataField, String viewName) throws IOException;

    /**
     * Drops the specified view
     * 
     * @param viewName
     */
    public void dropView(String viewName) throws IOException;

    /**
     * Drops the specified table
     * 
     * @param tableName
     */
    public void dropTable(String tableName) throws IOException;
}
