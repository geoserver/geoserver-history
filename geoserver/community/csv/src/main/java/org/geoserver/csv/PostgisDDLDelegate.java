/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.csv;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.geotools.data.DataSourceException;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.postgis.PostgisDataStore;

/**
 * DDLDelegate is used to support the process of data import by creating the
 * data table for it, and the process of the joining view creation, hiding the
 * rest of the classes from the specific SQL needed to perform these operations
 * (which is usually database dependent)
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public class PostgisDDLDelegate implements DDLDelegate {

    PostgisDataStore store;

    public PostgisDDLDelegate(PostgisDataStore store) {
        this.store = store;
    }

    /**
     * Executes the specified sql statement in a safe way on a connection
     * grabbed from the jdbc data store
     * 
     * @param sql
     * @throws IOException
     * @throws DataSourceException
     */
    void executeSql(String sql) throws IOException, DataSourceException {
        Connection conn = null;
        Statement st = null;
        try {
            conn = store.getConnection(Transaction.AUTO_COMMIT);
            st = conn.createStatement();
            st.execute(sql);
        } catch (SQLException e) {
            throw new DataSourceException("Error occurred executing " + sql, e);
        } finally {
            JDBCUtils.close(st);
            JDBCUtils.close(conn, null, null);
        }
    }

    public void createView(String geometryTable, String dataTable,
            String joinField, String dataField, String viewName)
            throws IOException {
        // create the view
        String createView = "CREATE VIEW \"" + viewName + "\" AS SELECT geom.*"
                + ", data.\"" + dataField + "\" " + "FROM \"" + geometryTable
                + "\" geom INNER JOIN \"" + dataTable + "\" data ON "
                + "geom.\"" + joinField + "\"=" + "data.\"" + joinField + "\"";
        executeSql(createView);

        // register it among the geometry columns
        String gcRegister = "INSERT INTO geometry_columns " + "SELECT '', '"
                + store.getDatabaseSchemaName() + "', '" + viewName
                + "', gc.f_geometry_column,"
                + "gc.coord_dimension, gc.srid, gc.type "
                + "FROM geometry_columns gc WHERE gc.f_table_name = '"
                + geometryTable + "'";
        executeSql(gcRegister);
    }

    public void dropTable(String tableName) throws IOException {
        executeSql("DROP TABLE \"" + tableName + "\"");
    }

    public void dropView(String viewName) throws IOException {
        executeSql("DELETE FROM geometry_columns WHERE f_table_name = '"
                + viewName + "'");
        executeSql("DROP VIEW \"" + viewName + "\"");
    }

    public void createIndex(String tableName, String columnName)
            throws IOException {
        executeSql("CREATE INDEX \"" + tableName + "_" + columnName
                + "\" ON \"" + tableName + "\"(\"" + columnName + "\")");
    }
}
