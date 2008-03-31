package org.geoserver.csv;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataSourceException;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.postgis.PostgisDataStore;
import org.geotools.feature.AttributeType;
import org.geotools.feature.FeatureType;

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
    // blatant copy of what's in PostgisDataStore... unfortunately it's not
    // exposed
    private static Map<Class, String> CLASS_MAPPINGS = new HashMap<Class, String>() {
        {
            put(String.class, "VARCHAR");
            put(Boolean.class, "BOOLEAN");
            put(Integer.class, "INTEGER");
            put(Float.class, "REAL");
            put(Double.class, "DOUBLE PRECISION");
            put(BigDecimal.class, "DECIMAL");
            put(java.sql.Date.class, "DATE");
            put(java.util.Date.class, "DATE");
            put(java.sql.Time.class, "TIME");
            put(java.sql.Timestamp.class, "TIMESTAMP");
        }
    };

    PostgisDataStore store;

    public PostgisDDLDelegate(PostgisDataStore store) {
        this.store = store;
    }

    public void createSchema(FeatureType type, String primaryKey)
            throws IOException {
        // this is blatantly copied and adapted from
        // PostgisDataStore.makeSqlCreate
        // again, the intent of this one is a little different, could not reuse
        // the
        // postgis data store code directly
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE \"").append(type.getTypeName()).append("\"(\n");

        for (int i = 0; i < type.getAttributeCount(); i++) {
            final AttributeType attribute = type.getAttributeType(i);
            String typeName = (String) CLASS_MAPPINGS.get(attribute
                    .getBinding());
            if (typeName == null)
                throw new IOException("Unsupported attribute type "
                        + attribute.getBinding());

            // build attribute definition
            sb.append("\"").append(attribute.getLocalName()).append("\" ");
            sb.append(typeName);

            if (typeName.equals("VARCHAR")) {
                int length = 256;
                sb.append("(").append(length).append(")");
            }

            if (!attribute.isNillable()) {
                sb.append(" NOT NULL");
            }

            if (attribute.getLocalName().equals(primaryKey))
                sb.append(" PRIMARY KEY");

            if (i < type.getAttributeCount() - 1)
                sb.append(",\n");
            else
                sb.append(")");
        }

        String sql = sb.toString();
        executeSql(sql);
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
            String joinField, String dataField, String viewName) throws IOException {
        // create the view
        String createView = "CREATE VIEW \"" + viewName + "\" AS SELECT geom.*"
                + ", data.\"" + dataField + "\" " + "FROM \"" + geometryTable
                + "\" geom INNER JOIN \"" + dataTable + "\" data ON " + "geom.\""
                + joinField + "\"=" + "data.\"" + joinField + "\"";
        executeSql(createView);

        // register it among the geometry columns
        String gcRegister = "INSERT INTO geometry_columns "
                + "SELECT '', '" + store.getDatabaseSchemaName() + "', '" + viewName
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
}
