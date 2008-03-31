package org.geoserver.csv;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.geotools.data.Transaction;
import org.geotools.data.postgis.PostgisDataStore;
import org.geotools.data.postgis.PostgisDataStoreFactory;
import org.geotools.test.OnlineTestCase;

/**
 * Base class for online tests that do need to connect to a postgis data store.
 * PLEASE NOTICE: to run these tests you'll need a Postgis datastore around and
 * you'll have to put copy the csv-postgis.properties file provided
 * in src/test/java/resources/ into your home/.geotools directory (and adapt it
 * so that the connection params do match you PC) 
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public abstract class AbstractPostgisTest extends OnlineTestCase {

    PostgisDataStore store;

    PostgisDDLDelegate ddl;

    @Override
    protected String getFixtureId() {
        return "csv-postgis";
    }

    @Override
    protected void connect() throws Exception {
        store = (PostgisDataStore) new PostgisDataStoreFactory()
                .createDataStore(fixture);

        // deep clean up
        String[] typeNames = store.getTypeNames();
        Connection conn = store.getConnection(Transaction.AUTO_COMMIT);
        Statement st = conn.createStatement();
        for (int i = 0; i < typeNames.length; i++) {
            try {
                st.execute("drop table \"" + typeNames[i] + "\" cascade");
            } catch (SQLException e1) {
                try {
                    st.execute("drop view \"" + typeNames[i] + "\" cascade");
                } catch (SQLException e2) {

                }
            }
        }
        st.execute("delete from geometry_columns");

        // create an empty geometric table
        st.execute("CREATE TABLE road (fid varchar PRIMARY KEY, id varchar )");
        st.execute("SELECT AddGeometryColumn"
                + "('public', 'road', 'geom', -1, 'LINESTRING', 2)");
        st.execute("ALTER TABLE road add name varchar;");
        st.execute("INSERT INTO road VALUES('road1', 'rd1', GeometryFromText('LINESTRING(0 0, 1 1)'))");
        st.execute("INSERT INTO road VALUES('road2', 'rd2', GeometryFromText('LINESTRING(0 0, -1 -1)'))");
        st.execute("INSERT INTO road VALUES('road3', 'rd3', GeometryFromText('LINESTRING(10 10, 20 20)'))");

        st.close();
        conn.close();

        // build the ddl delegate
        ddl = new PostgisDDLDelegate(store);
    }

    @Override
    protected void disconnect() throws Exception {
        if (store != null)
            store.dispose();
    }
}
