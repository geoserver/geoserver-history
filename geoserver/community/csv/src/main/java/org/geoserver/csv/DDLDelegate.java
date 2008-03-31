package org.geoserver.csv;

import java.io.IOException;

import org.geotools.feature.FeatureType;

public interface DDLDelegate {

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

    /**
     * Creates an index on the specified table column (the name of the index will be
     * tableName_columnName)
     * @param tableName
     * @param columnName
     * @throws IOException
     */
    public void createIndex(String tableName, String columnName) throws IOException;
}
